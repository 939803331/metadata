package cn.net.metadata.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;


@EnableSpringDataWebSupport
@SpringBootApplication
public class BaseApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(BaseApplication.class, args);
	}
	
}
