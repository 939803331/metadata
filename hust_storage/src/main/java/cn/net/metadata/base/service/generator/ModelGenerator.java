package cn.net.metadata.base.service.generator;

import cn.net.metadata.base.model.BaseModel;
import cn.net.metadata.base.model.core.TableColumnEntity;
import cn.net.metadata.base.model.core.TableEntity;
import cn.net.metadata.base.service.core.MySQLConstant;
import cn.net.metadata.base.utility.MDStringUtils;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.jenesis4java.*;
import org.springframework.core.io.DefaultResourceLoader;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ModelGenerator {

    private GeneratorSetting generatorSetting;

    private final VirtualMachine vm;

    private CompilationUnit unit;

    private PackageClass cls;

    private TableEntity tableEntity;

    private List<TableColumnEntity> tableColumnEntityList;

    public static ModelGenerator getInstance(GeneratorSetting generatorSetting) {
        return new ModelGenerator(generatorSetting);
    }

    private ModelGenerator(GeneratorSetting generatorSetting) {
        this.generatorSetting = generatorSetting;
        this.vm = VirtualMachine.getVirtualMachine();
    }

    public void build(TableEntity tableEntity, List<TableColumnEntity> tableColumnEntityList) throws IOException {
        this.tableEntity = tableEntity;
        this.tableColumnEntityList = tableColumnEntityList;

        File projectPath = new DefaultResourceLoader().getResource("").getFile();
        while (!new File(projectPath.getPath() + File.separator + "src" + File.separator + "main").exists()) {
            projectPath = projectPath.getParentFile();
        }
        String generatedFilePath = projectPath + "/src/main/java";

        String className = MDStringUtils.underline2Camel(tableEntity.getTableName().replaceAll("buss_", ""), false);
        initialize(generatedFilePath, generatorSetting.getModelPackage(), className);
        addMembers();

        generate();
    }

    private void addMembers() {
        for (TableColumnEntity tableColumnEntity : tableColumnEntityList) {

            if (MySQLConstant.SKIP_COLUMNS.contains(tableColumnEntity.getColumnName().toLowerCase())) {
                continue;
            }
            String fieldName = MDStringUtils.underline2Camel(tableColumnEntity.getColumnName(), true);
            ClassField field = this.cls.newField(vm.newType(MySQLConstant.getFieldTypeByDataType(tableColumnEntity.getDataType())), fieldName);
            field.setAccess(Access.PRIVATE);
            // @Column(name = "name")
            field.setComment(Comment.MULTI_LINE, tableColumnEntity.getColumnComment());
            field.addAnnotation("Column").addAnnotationAttribute("name", vm.newString(tableColumnEntity.getColumnName()));
        }
    }

    private void generate() {
        unit.encode();
    }

    private void initialize(String codebase, String packageName, String className) {
        this.unit = this.vm.newCompilationUnit(codebase);
        this.unit.setNamespace(packageName);
        this.cls = this.unit.newClass(className);
        this.cls.setAccess(Access.PUBLIC);
        this.cls.setComment(Comment.MULTI_LINE, className);
        this.cls.setExtends("BaseModel");
        this.cls.addImplements("Serializable");
        setSerialVersionUID();
        addImports();
        addClassAnnotation();
    }

    private void setSerialVersionUID() {
        ClassField field = this.cls.newField(vm.newType(long.class), "serialVersionUID");
        field.setAccess(Access.PRIVATE);
        field.isFinal(true);
        field.isStatic(true);
        field.setExpression(vm.newLong(-1L));
    }

    private void addImports() {
        this.cls.addImport(BaseModel.class);
        this.cls.addImport(ApiModel.class);
        this.cls.addImport(Getter.class);
        this.cls.addImport(Setter.class);
        this.cls.addImport(Column.class);
        this.cls.addImport(Entity.class);
        this.cls.addImport(Table.class);
        this.cls.addImport(Serializable.class);
    }

    private void addClassAnnotation() {
        this.cls.addAnnotation("Getter");
        this.cls.addAnnotation("Setter");
        this.cls.addAnnotation("Entity");
        this.cls.addAnnotation("Table").addAnnotationAttribute("name", vm.newString(tableEntity.getTableName()));
        this.cls.addAnnotation("ApiModel").addAnnotationAttribute("description", vm.newString(tableEntity.getTableComment()));
        this.cls.addAnnotation("org.hibernate.annotations.Table").addAnnotationAttribute("appliesTo").addValue(vm.newString(tableEntity.getTableName()));
        this.cls.getAnnotations().stream().filter(annotation -> annotation.getEffectiveName().equals("org.hibernate.annotations.Table")).collect(Collectors.toList()).get(0)
                .addAnnotationAttribute("comment", vm.newString(tableEntity.getTableComment()));
    }
}
