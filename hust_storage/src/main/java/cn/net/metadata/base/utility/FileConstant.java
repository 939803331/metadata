package cn.net.metadata.base.utility;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.utility
 * @date 2018/7/19 15:04
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "file")
public class FileConstant {
	
	private String uploadPath;
	

	
}
