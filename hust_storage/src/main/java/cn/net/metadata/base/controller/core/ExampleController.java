package cn.net.metadata.base.controller.core;

import cn.net.metadata.base.common.RestResult;
import cn.net.metadata.base.common.RestResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p></p>
 *
 * @author SageZhang
 * @version 2018/10/26
 */
@RestController
@RequestMapping("api/demo")
@Api(tags = "示例", description = "示例api")
public class ExampleController {

    @ApiOperation(value = "示例api", notes = "示例api")
    @GetMapping
    public RestResult get() {
        try {
            String data = "这是数据";
            return RestResultGenerator.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResultGenerator.error(e.getMessage());
        }
    }
}
