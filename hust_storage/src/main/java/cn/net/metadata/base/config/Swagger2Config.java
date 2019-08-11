package cn.net.metadata.base.config;

import cn.net.metadata.base.config.jwt.WebFilterConfig;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author xiaopo
 * @package cn.net.metadata.mgather.configuration
 * @date 2018/4/27 14:49
 */
@Configuration
@EnableSwagger2
public class Swagger2Config implements WebMvcConfigurer {
    @Value("${springfox.swagger.enable:false}")
    boolean swaggerEnable;

    @Autowired
    private WebFilterConfig webFilterConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).enable(swaggerEnable)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(newArrayList(new ApiKey("Authorization", "Authorization", "header")))
                .securityContexts(securityContexts());
    }

    /**
     * @Author wjp
     * @Date 2018/9/7
     * <p>
     * 接口权限过滤（不需权限）
     */
    private List<SecurityContext> securityContexts() {
        return newArrayList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(Predicates.and(
                                webFilterConfig.getSysAntMatchers().stream()
                                        .map(pattern -> (Predicate<String>) input -> !new AntPathMatcher().match(pattern, input))
                                        .collect(Collectors.toList())
                        )).build()
        );
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
                new SecurityReference("Authorization", authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("华科查收查引查新流程管理系统RESTful APIs")
                .description("华科查收查引查新流程管理系统接口详细调用说明")
                .contact(new Contact("Xiaopo", "", "su@metadata.net.cn"))
                .version("1.0")
                .build();
    }
}
