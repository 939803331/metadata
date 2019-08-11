package cn.net.metadata.base.service.generator;

import cn.net.metadata.base.model.core.TableColumnEntity;
import cn.net.metadata.base.model.core.TableEntity;
import cn.net.metadata.base.utility.MDStringUtils;
import net.sourceforge.jenesis4java.*;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

public class RepositoryGenerator {

    private GeneratorSetting generatorSetting;

    private VirtualMachine vm;

    private CompilationUnit unit;

    private Interface anInterface;

    private TableEntity tableEntity;

    private List<TableColumnEntity> tableColumnEntityList;

    private static final String NAMESPACE = "cn.net.metadata.base.repository.business";

    public static RepositoryGenerator getInstance(GeneratorSetting generatorSetting) {
        return new RepositoryGenerator(generatorSetting);
    }

    private RepositoryGenerator(GeneratorSetting generatorSetting) {
        this.generatorSetting = generatorSetting;
        this.vm = VirtualMachine.getVirtualMachine();
    }

    public void build(TableEntity tableEntity) throws IOException {
        this.tableEntity = tableEntity;

        File projectPath = new DefaultResourceLoader().getResource("").getFile();
        while (!new File(projectPath.getPath() + File.separator + "src" + File.separator + "main").exists()) {
            projectPath = projectPath.getParentFile();
        }
        String generatedFilePath = projectPath + "/src/main/java";

        String entityName = MDStringUtils.underline2Camel(tableEntity.getTableName().replaceAll("buss_", ""), false);
        initialize(generatedFilePath, NAMESPACE, entityName);

        generate();
    }

    private void generate() {
        unit.encode();
    }

    private void initialize(String codebase, String packageName, String entityName) {
        String interfaceName = entityName + "Repository";
        this.unit = this.vm.newCompilationUnit(codebase);
        this.unit.setNamespace(packageName);
        this.anInterface = this.unit.newPublicInterface(interfaceName);
        this.anInterface.setAccess(Access.PUBLIC);
        this.anInterface.setComment(Comment.MULTI_LINE, interfaceName);
        String extendStr = MessageFormat.format("JpaRepository<{0}, Long>, JpaSpecificationExecutor<{1}>", entityName, entityName);
        this.anInterface.addExtends(extendStr);
        addImports(entityName);
    }

    private void addImports(String entityName) {
        this.anInterface.getUnit().addImport("cn.net.metadata.base.model.business." + entityName);
        this.anInterface.getUnit().addImport(JpaRepository.class);
        this.anInterface.getUnit().addImport(JpaSpecificationExecutor.class);
    }
}
