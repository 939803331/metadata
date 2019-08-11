package cn.net.metadata.base.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author wjp
 * @Date 2018/8/17
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "filter-config")
public class WebFilterConfig {
    private List<String> sysAntMatchers;
    private List<String> antMatchers;
}
