package cn.net.metadata.base.controller.core;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.controller.core
 * @date 2018/8/16 19:04
 */
@RestController
@RequestMapping
@Api(value = "默认")
public class IndexController {
	
	@RequestMapping
	public String index() {
		return "<html><body style='text-align: center;margin-top:10%;'>Spring Boot Server Is Started !</body></html>";
	}
	
	
}
