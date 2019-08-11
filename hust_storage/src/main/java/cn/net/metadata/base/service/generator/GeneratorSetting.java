package cn.net.metadata.base.service.generator;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "project")
public class GeneratorSetting {

    private String name;

    private String group;

    private String databaseName;

    public String getBaseNamespace() {
        return group + "." + name;
    }

    public String getControllerPackage() {
        return group + "." + name + ".controller.business";
    }

    public String getServicePackage() {
        return group + "." + name + ".service.business";
    }

    public String getRepositoryPackage() {
        return group + "." + name + ".repository.business";
    }

    public String getModelPackage() {
        return group + "." + name + ".model.business";
    }
}
