package cn.net.metadata.base.service.generator;

import cn.net.metadata.base.model.core.TableEntity;
import cn.net.metadata.base.service.BaseService;
import cn.net.metadata.base.utility.MDStringUtils;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.jenesis4java.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Optional;

public class ServiceGenerator {

    private GeneratorSetting generatorSetting;

    private VirtualMachine vm;

    private CompilationUnit unit;

    private PackageClass cls;

    private String entityRepositoryName;

    private String varEntityRepositoryName;

    private String entityName;

    private String varEntityName;

    public static ServiceGenerator getInstance(GeneratorSetting generatorSetting) {
        return new ServiceGenerator(generatorSetting);
    }

    private ServiceGenerator(GeneratorSetting generatorSetting) {
        this.generatorSetting = generatorSetting;
        this.vm = VirtualMachine.getVirtualMachine();
    }

    public void build(TableEntity tableEntity) throws IOException {
        this.entityName = MDStringUtils.underline2Camel(tableEntity.getTableName().replaceAll("buss_", ""), false);
        this.varEntityName = StringUtils.uncapitalize(entityName);

        this.entityRepositoryName = entityName + "Repository";
        this.varEntityRepositoryName = StringUtils.uncapitalize(entityName) + "Repository";

        File projectPath = new DefaultResourceLoader().getResource("").getFile();
        while (!new File(projectPath.getPath() + File.separator + "src" + File.separator + "main").exists()) {
            projectPath = projectPath.getParentFile();
        }
        String generatedFilePath = projectPath + "/src/main/java";

        initialize(generatedFilePath, generatorSetting.getServicePackage(), entityName);

        generate();
    }

    private void generate() {
        unit.encode();
    }

    private void initialize(String codebase, String packageName, String entityName) {
        String serviceName = entityName + "Service";
        this.unit = this.vm.newCompilationUnit(codebase);
        this.unit.setNamespace(packageName);
        this.cls = this.unit.newPublicClass(serviceName);
        this.cls.setAccess(Access.PUBLIC);
        this.cls.setComment(Comment.MULTI_LINE, serviceName);
        this.cls.setExtends("BaseService");
        addImports();
        addClassAnnotation();
        addMembers();
        addMethod();
    }

    private void addImports() {
        this.cls.getUnit().addImport(Slf4j.class);
        this.cls.getUnit().addImport(Autowired.class);
        this.cls.getUnit().addImport(Service.class);
        this.cls.getUnit().addImport(Transactional.class);
        this.cls.getUnit().addImport(Pageable.class);
        this.cls.getUnit().addImport(Page.class);
        this.cls.getUnit().addImport(Example.class);
        this.cls.getUnit().addImport(ExampleMatcher.class);
        this.cls.getUnit().addImport("cn.net.metadata.base.model.business." + this.entityName);
        this.cls.getUnit().addImport("cn.net.metadata.base.repository.business." + this.entityRepositoryName);
        this.cls.getUnit().addImport(BaseService.class);
        this.cls.getUnit().addImport(Optional.class);
    }

    private void addClassAnnotation() {
        this.cls.addAnnotation("Slf4j");
        this.cls.addAnnotation("Service");
        this.cls.addAnnotation("Transactional");
    }

    private void addMembers() {
        ClassField field = this.cls.newField(vm.newType(this.entityRepositoryName), this.varEntityRepositoryName);
        field.setAccess(Access.PRIVATE);
        field.addAnnotation("Autowired");
    }

    private void addMethod() {
        createSaveMethod();
        createUpdateMethod();
        createFind4PageMethod();
        createFindById();
        createDelete();
    }

    private void createSaveMethod() {
        // save method
        ClassMethod saveMethod = cls.newMethod(vm.newType(this.entityName), "save");
        saveMethod.setAccess(Access.PUBLIC);
        saveMethod.addParameter(vm.newType(this.entityName), this.varEntityName);

        Invoke saveInvoke = vm.newInvoke(this.varEntityRepositoryName, "save");
        saveInvoke.addArg(vm.newVar(this.varEntityName));

        saveMethod.newReturn().setExpression(saveInvoke);
    }

    private void createUpdateMethod() {
        // update method
        ClassMethod updateMethod = cls.newMethod(vm.newType(entityName), "update");
        updateMethod.setAccess(Access.PUBLIC);
        updateMethod.addParameter(vm.newType(this.entityName), this.varEntityName);

        Invoke updateInvoke = vm.newInvoke(this.varEntityRepositoryName, "save");
        updateInvoke.addArg(vm.newVar(this.varEntityName));

        updateMethod.newReturn().setExpression(updateInvoke);
    }

    private void createFind4PageMethod() {
        // find4Page method
        String returnClass = MessageFormat.format("Page<{0}>", this.entityName);
        ClassMethod find4PageMethod = cls.newMethod(vm.newType(returnClass), "find4Page");
        find4PageMethod.setAccess(Access.PUBLIC);
        find4PageMethod.addParameter(vm.newType(this.entityName), this.varEntityName);
        find4PageMethod.addParameter(vm.newType(Pageable.class), "pageable");

        Let exampleMatcher = find4PageMethod.newLet(vm.newType("ExampleMatcher"));

        Invoke exampleMatcherInvoke = vm.newInvoke("ExampleMatcher.matching().withIgnoreCase().withIgnoreNullValues()",
                "withStringMatcher");
        exampleMatcherInvoke.addArg(vm.newVar("ExampleMatcher.StringMatcher.CONTAINING"));

        // 赋值语句
        exampleMatcher.addAssign("exampleMatcher", exampleMatcherInvoke);

        // sampleRepository.findAll(Example.of(sample, exampleMatcher), pageable);
        Invoke findAllInvoke = vm.newInvoke(this.varEntityRepositoryName, "findAll");

        Invoke exampleOfInvoke = vm.newInvoke("Example", "of");
        exampleOfInvoke.addArg(vm.newVar(this.varEntityName));
        exampleOfInvoke.addArg(vm.newVar("exampleMatcher"));

        findAllInvoke.addArg(exampleOfInvoke);
        findAllInvoke.addArg(vm.newVar("pageable"));

        find4PageMethod.newReturn().setExpression(findAllInvoke);
    }

    private void createFindById() {
        String returnClass = MessageFormat.format("Optional<{0}>", this.entityName);
        ClassMethod findByIdMethod = cls.newMethod(vm.newType(returnClass), "findById");
        findByIdMethod.setAccess(Access.PUBLIC);
        findByIdMethod.addParameter(vm.newType(Long.class), "id");

        Invoke findByIdInvoke = vm.newInvoke(this.varEntityRepositoryName, "findById");
        findByIdInvoke.addArg(vm.newVar("id"));

        findByIdMethod.newReturn().setExpression(findByIdInvoke);
    }

    private void createDelete() {
        ClassMethod deleteMethod = cls.newMethod("delete");
        deleteMethod.setAccess(Access.PUBLIC);
        deleteMethod.addParameter(vm.newType(Long.class), "id");

        Invoke deleteByIdInvoke = vm.newInvoke(this.varEntityRepositoryName, "deleteById");
        deleteByIdInvoke.addArg(vm.newVar("id"));

        deleteMethod.newStmt(deleteByIdInvoke);
    }
}
