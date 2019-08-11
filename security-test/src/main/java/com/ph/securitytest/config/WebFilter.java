package com.ph.securitytest.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 *
 * @Author: PiHao
 * Date: Created in 2019-08-10
 * Time: 16:09
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "filter-config")
public class WebFilter {
	private List<String> sysAntMatchers;
	private List<String> antMatchers;
}
