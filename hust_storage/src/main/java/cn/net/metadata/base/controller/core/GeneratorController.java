package cn.net.metadata.base.controller.core;

import cn.net.metadata.base.model.core.TableEntity;
import cn.net.metadata.base.service.generator.GeneratorService;
import cn.net.metadata.base.utility.Message;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.controller
 * @date 2018/7/18 17:54
 */
@RestController
@RequestMapping("generator")
@Api(tags = "全自动代码生成器", description = "")
public class GeneratorController {

    @Autowired
    private GeneratorService generatorService;

    /**
     * 自动生成代码
     *
     * @param tableName
     * @return Message
     */
    @GetMapping("{tableName}")
    public String generator(@PathVariable String tableName) {
        try {
            generatorService.genByTableName(tableName);
        } catch (IOException e) {
            return "error";
        }
        return "ok";
    }

    @GetMapping("tables")
    public List<TableEntity> queryAllTable() {
        return generatorService.queryAllTable();
    }
}
