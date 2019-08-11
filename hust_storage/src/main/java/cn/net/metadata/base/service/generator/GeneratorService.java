package cn.net.metadata.base.service.generator;

import cn.net.metadata.base.model.core.Permission;
import cn.net.metadata.base.model.core.TableColumnEntity;
import cn.net.metadata.base.model.core.TableEntity;
import cn.net.metadata.base.repository.core.PermissionRepository;
import cn.net.metadata.base.repository.core.TableColumnEntityRepository;
import cn.net.metadata.base.repository.core.TableEntityRepository;
import cn.net.metadata.base.utility.MDStringUtils;
import cn.net.metadata.base.utility.Message;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author chencheng
 * @date 2018/12/11 18:04
 */
@Service
@Slf4j
public class GeneratorService {

    @Autowired
    private TableEntityRepository tableEntityRepository;

    @Autowired
    private TableColumnEntityRepository tableColumnEntityRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private GeneratorSetting generatorSetting;

    public void genByTableName(String tableName) throws IOException {
        TableEntity tableEntity = tableEntityRepository.findBussTableName(generatorSetting.getDatabaseName(), tableName);
        genCodeAndPermission(tableEntity);
    }

    public Message genAllTables() throws IOException {
        List<TableEntity> tableEntityList = tableEntityRepository.findBussTableNames(generatorSetting.getDatabaseName());
        for (TableEntity tableEntity : tableEntityList) {
            genCodeAndPermission(tableEntity);
        }
        Message message = Message.builder().code(200).message("genAllTables success").data(null).build();
        return message;
    }

    public List<TableEntity> queryAllTable() {
        List<TableEntity> tableEntityList = tableEntityRepository.findBussTableNames(generatorSetting.getDatabaseName());
        return tableEntityList;
    }

    private void genCodeAndPermission(TableEntity tableEntity) throws IOException {
        List<TableColumnEntity> tableColumnEntityList = tableColumnEntityRepository.findByTableName(tableEntity.getTableName());

        ModelGenerator.getInstance(generatorSetting).build(tableEntity, tableColumnEntityList);
        RepositoryGenerator.getInstance(generatorSetting).build(tableEntity);
        ServiceGenerator.getInstance(generatorSetting).build(tableEntity);
        ControllerGenerator.getInstance(generatorSetting).build(tableEntity);

        buildPermissionByTableEntity(tableEntity);
    }

    @Transactional
    public void buildPermissionByTableEntity(TableEntity tableEntity) {
        String className = MDStringUtils.underline2Camel(tableEntity.getTableName().replaceAll("buss_", ""), false);
        Permission permission = permissionRepository.findByEntityNameAndPermissionType(className, "menu");
        if (permission != null)
            permissionRepository.delete(permission);

        Permission menu = buildMenu(tableEntity, className);
        menu.setChildren(buildCRUDPermissions(tableEntity, className));
        permissionRepository.save(menu);
    }

    private Permission buildMenu(TableEntity tableEntity, String className) {
        Date now = new Date();
        Permission permission = new Permission();
        permission.setCreateBy(-1L);
        permission.setCreateTime(now);
        permission.setUpdateBy(-1L);
        permission.setUpdateTime(now);
        permission.setName(tableEntity.getTableComment() + "管理");
        permission.setPermissionType("menu");
        permission.setEntityName(className);
        return permission;
    }

    private List<Permission> buildCRUDPermissions(TableEntity tableEntity, String className) {
        String url = "/" + MDStringUtils.underline2Camel(tableEntity.getTableName().replaceAll("buss_", ""), true);
        List<Permission> curdPermissions = Lists.newArrayList();

        Map<String, String> methodMap = Maps.newLinkedHashMap();
        methodMap.put("get", "查询");
        methodMap.put("list", "列表查询");
        methodMap.put("save", "新增");
        methodMap.put("update", "更新");
        methodMap.put("delete", "删除");
        Date now = new Date();
        methodMap.forEach((authority, name) -> {
            Permission permission = new Permission();
            permission.setCreateBy(-1L);
            permission.setCreateTime(now);
            permission.setUpdateBy(-1L);
            permission.setUpdateTime(now);
            permission.setName(name);
            permission.setAuthority(StringUtils.uncapitalize(className) + ":" + authority);
            permission.setUrl(url);
            permission.setPermissionType("permission");
            permission.setEntityName(className);
            curdPermissions.add(permission);
        });

        return curdPermissions;
    }
}
