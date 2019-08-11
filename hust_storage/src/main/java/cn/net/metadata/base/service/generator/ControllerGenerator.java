package cn.net.metadata.base.service.generator;

import cn.net.metadata.base.model.core.TableEntity;
import cn.net.metadata.base.utility.MDStringUtils;
import io.swagger.annotations.Api;
import lombok.Setter;
import net.sourceforge.jenesis4java.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class ControllerGenerator {

    private GeneratorSetting generatorSetting;

    private VirtualMachine vm;

    private CompilationUnit unit;

    private PackageClass cls;

    private String entityServiceName;

    private String varEntityServiceName;

    private String entityName;

    private String varEntityName;

    private TableEntity tableEntity;

    private static final String MANAGE_SUFFIX_DESC = "管理";

    private static final String API_SUFFIX_DESC = MANAGE_SUFFIX_DESC + "相关API";

    public static ControllerGenerator getInstance(GeneratorSetting generatorSetting) {
        return new ControllerGenerator(generatorSetting);
    }

    private ControllerGenerator(GeneratorSetting generatorSetting) {
        this.generatorSetting = generatorSetting;
        this.vm = VirtualMachine.getVirtualMachine();
    }

    public void build(TableEntity tableEntity) throws IOException {
        this.tableEntity = tableEntity;
        this.entityName = MDStringUtils.underline2Camel(tableEntity.getTableName().replaceAll("buss_", ""), false);
        this.varEntityName = StringUtils.uncapitalize(entityName);

        this.entityServiceName = entityName + "Service";
        this.varEntityServiceName = StringUtils.uncapitalize(entityName) + "Service";

        File projectPath = new DefaultResourceLoader().getResource("").getFile();
        while (!new File(projectPath.getPath() + File.separator + "src" + File.separator + "main").exists()) {
            projectPath = projectPath.getParentFile();
        }
        String generatedFilePath = projectPath + "/src/main/java";

        initialize(generatedFilePath, generatorSetting.getControllerPackage(), entityName);

        generate();
    }

    private void generate() {
        unit.encode();
    }

    private void initialize(String codebase, String packageName, String entityName) {
        String controllerName = entityName + "Controller";
        this.unit = this.vm.newCompilationUnit(codebase);
        this.unit.setNamespace(packageName);
        this.cls = this.unit.newPublicClass(controllerName);
        this.cls.setAccess(Access.PUBLIC);
        this.cls.setComment(Comment.MULTI_LINE, controllerName);
        addImports();
        addClassAnnotation();
        addMembers();
        addMethod();
    }

    private void addImports() {
        // cn.net.metadata.base.model.business
        this.cls.getUnit().addImport("cn.net.metadata.base.model.business." + this.entityName);
        this.cls.getUnit().addImport("cn.net.metadata.base.service.business." + this.entityServiceName);
        this.cls.getUnit().addImport(Api.class);
        this.cls.getUnit().addImport(Autowired.class);
        this.cls.getUnit().addImport(Pageable.class);
        this.cls.getUnit().addImport(Page.class);
        this.cls.getUnit().addImport(PreAuthorize.class);
        this.cls.getUnit().addImport(Validated.class);
        this.cls.getUnit().addImport("org.springframework.web.bind.annotation.*");
    }

    private void addClassAnnotation() {
        this.cls.addAnnotation("RestController");
        this.cls.addAnnotation("RequestMapping").addDefaultValueAttribute(vm.newString(this.varEntityName));

        this.cls.addAnnotation("Api").addAnnotationAttribute("tags").addValue(vm.newString(tableEntity.getTableComment() + MANAGE_SUFFIX_DESC));
        this.cls.getAnnotations().stream().filter(annotation -> annotation.getEffectiveName().equals("Api")).collect(Collectors.toList()).get(0)
                .addAnnotationAttribute("description", vm.newString(tableEntity.getTableComment() + API_SUFFIX_DESC));
    }

    private void addMembers() {
        ClassField field = this.cls.newField(vm.newType(this.entityServiceName), this.varEntityServiceName);
        field.setAccess(Access.PRIVATE);
        field.addAnnotation("Autowired");
    }

    private void addMethod() {
        createFillMethod();
        createGetMethod();
        createListMethod();
        createSaveMethod();
        createUpdateMethod();
        createDeleteMethod();
    }

    private void createFillMethod() {
        // save method
        ClassMethod fillMethod = cls.newMethod(vm.newType(this.entityName), "fill");
        fillMethod.setAccess(Access.PUBLIC);
        fillMethod.addParameter(vm.newType("@RequestParam(required = false) Long"), "id");
        fillMethod.addAnnotation("ModelAttribute");

        If ifOp = fillMethod.newIf(vm.newBinary(Binary.NOT_EQUAL, vm.newVar("id"), vm.newNull()));
        // return sampleRepository.findById(id).orElseThrow(() -> new RuntimeException("该数据不存在，请检查对应的ID。"));
        // sampleRepository.findById(id).orElseThrow(
        Invoke findByIdInvoke = vm.newInvoke(this.varEntityServiceName + ".findById(id)", "orElseThrow");
        findByIdInvoke.addArg(vm.newVar("() -> new RuntimeException(\"该数据不存在，请检查对应的ID。\")"));

        ifOp.newReturn().setExpression(findByIdInvoke);

        fillMethod.newReturn().setExpression(vm.newClass(vm.newType(this.entityName)));
    }

    private void createGetMethod() {
        // save method
        ClassMethod fillMethod = cls.newMethod(vm.newType(this.entityName), "get");
        fillMethod.setAccess(Access.PUBLIC);
        fillMethod.addParameter(vm.newType("@PathVariable Long"), "id");
        fillMethod.addAnnotation("GetMapping").addDefaultValueAttribute(vm.newString("{id}"));
        fillMethod.addAnnotation("PreAuthorize").addDefaultValueAttribute(
                vm.newString(ST.format("hasAuthority('<%1>:get')", this.varEntityName))
        );
        Invoke findByIdInvoke = vm.newInvoke(this.varEntityServiceName + ".findById(id)", "orElse");
        findByIdInvoke.addArg(vm.newNull());

        fillMethod.newReturn().setExpression(findByIdInvoke);
    }

    private void createListMethod() {
        String varPageableName = "pageable";
        // save method
        ClassMethod fillMethod = cls.newMethod(
                vm.newType(ST.format("Page\\<<%1>>", this.entityName))
                , "list");
        fillMethod.setAccess(Access.PUBLIC);
        fillMethod.addParameter(vm.newType(this.entityName), this.varEntityName);
        fillMethod.addParameter(vm.newType("Pageable"), varPageableName);

        fillMethod.addAnnotation("GetMapping");
        fillMethod.addAnnotation("PreAuthorize").addDefaultValueAttribute(
                vm.newString(ST.format("hasAuthority('<%1>:list')", this.varEntityName))
        );

        Invoke find4PageIdInvoke = vm.newInvoke(this.varEntityServiceName, "find4Page");
        find4PageIdInvoke.addArg(vm.newVar(this.varEntityName));
        find4PageIdInvoke.addArg(vm.newVar(varPageableName));

        fillMethod.newReturn().setExpression(find4PageIdInvoke);
    }

    private void createSaveMethod() {
        // save method
        ClassMethod fillMethod = cls.newMethod(vm.newType(this.entityName), "save");
        fillMethod.setAccess(Access.PUBLIC);
        fillMethod.addParameter(
                vm.newType(ST.format("@Validated <%1>", this.entityName)),
                this.varEntityName
        );
        fillMethod.addAnnotation("PostMapping");
        fillMethod.addAnnotation("PreAuthorize").addDefaultValueAttribute(
                vm.newString(ST.format("hasAuthority('<%1>:save')", this.varEntityName))
        );
        Invoke saveInvoke = vm.newInvoke(this.varEntityServiceName, "save");
        saveInvoke.addArg(vm.newVar(this.varEntityName));

        fillMethod.newReturn().setExpression(saveInvoke);
    }

    private void createUpdateMethod() {
        // save method
        ClassMethod fillMethod = cls.newMethod(vm.newType(this.entityName), "update");
        fillMethod.setAccess(Access.PUBLIC);
        fillMethod.addParameter(
                vm.newType(ST.format("@Validated <%1>", this.entityName)),
                this.varEntityName
        );
        fillMethod.addAnnotation("PutMapping");
        fillMethod.addAnnotation("PreAuthorize").addDefaultValueAttribute(
                vm.newString(ST.format("hasAuthority('<%1>:udpate')", this.varEntityName))
        );
        Invoke saveInvoke = vm.newInvoke(this.varEntityServiceName, "save");
        saveInvoke.addArg(vm.newVar(this.varEntityName));

        fillMethod.newReturn().setExpression(saveInvoke);
    }

    private void createDeleteMethod() {
        // save method
        ClassMethod fillMethod = cls.newMethod(vm.newType(Boolean.class), "delete");
        fillMethod.setAccess(Access.PUBLIC);
        fillMethod.addParameter(
                vm.newType("@PathVariable Long"),
                "id"
        );
        fillMethod.addAnnotation("DeleteMapping").addDefaultValueAttribute(vm.newString("{id}"));
        fillMethod.addAnnotation("PreAuthorize").addDefaultValueAttribute(
                vm.newString(ST.format("hasAuthority('<%1>:delete')", this.varEntityName))
        );
        Invoke deleteInvoke = vm.newInvoke(this.varEntityServiceName, "delete");
        deleteInvoke.addArg(vm.newVar("id"));
        fillMethod.newStmt(deleteInvoke);

        fillMethod.newReturn().setExpression(vm.newBoolean(true));
    }
}
