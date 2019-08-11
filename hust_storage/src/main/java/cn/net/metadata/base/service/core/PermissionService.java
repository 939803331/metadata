package cn.net.metadata.base.service.core;

import cn.net.metadata.base.model.core.Permission;
import cn.net.metadata.base.model.core.TableEntity;
import cn.net.metadata.base.repository.core.PermissionRepository;
import cn.net.metadata.base.service.BaseService;
import cn.net.metadata.base.utility.MDStringUtils;
import cn.net.metadata.base.utility.PackaageUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.service.core
 * @date 2018/7/20 12:58
 */
@Slf4j
@Service
@Transactional
public class PermissionService extends BaseService {

    private static Pattern pattern = Pattern.compile("(?<=\\()[^\\\\)]+");

    @Autowired
    PermissionRepository permissionRepository;


    @PreAuthorize("hasRole('ADMIN')")
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Permission update(Permission permission) {
        return permissionRepository.save(permission);
    }

    /**
     * 初始化所有权限
     */
    @PreAuthorize("hasRole('ADMIN')")
    public void initPermission() {
        // 获取root package下所有class
        Set<Class<?>> set = PackaageUtils.getClasses("cn.net.metadata.novelty");
        // 保存所有权限
        List<Permission> permissionList = new ArrayList<>();
        // 获取所有controller
        List<Class<?>> classList = set.stream().filter(f -> f.getAnnotation(RestController.class) != null).collect(Collectors.toList());
        // 获取所有权限
        for (Class<?> f : classList) {
            if (f.getAnnotation(RequestMapping.class) == null)
                continue;
            // 获取权限名称
            String permissionName = f.getSimpleName();
            String controllerUrl = toCover(String.join(",", f.getAnnotation(RequestMapping.class).value()));
            Api api = f.getAnnotation(Api.class);
            // 构建 parent
            String finalPermissionName = permissionName;
            Permission parent = permissionList.stream().filter(ft -> ft.getName().equalsIgnoreCase(finalPermissionName)).findFirst().orElse(new Permission());
            parent.setName(permissionName);
            if (api != null) {
                permissionName = String.join(",", api.tags());
                String finalPermissionName1 = permissionName;
                parent = permissionList.stream().filter(ft -> ft.getName().equalsIgnoreCase(finalPermissionName1)).findFirst().orElse(new Permission());
                parent.setName(permissionName);
                // 判断是否有父类
                if (permissionName.contains("-")) {
                    String grandpaPermissionName = permissionName.split("-")[0];
                    String parentPermissionName = permissionName.split("-")[1];
                    parent = permissionList.stream().filter(ft -> ft.getName().equalsIgnoreCase(parentPermissionName)).findFirst().orElse(new Permission());
                    parent.setName(parentPermissionName);

                    // 设置parent的父类
                    Permission grandpa = permissionList.stream().filter(f1 -> f1.getName().equalsIgnoreCase(grandpaPermissionName)).findFirst().orElse(null);
                    if (grandpa == null) {
                        grandpa = new Permission();
                        grandpa.setName(grandpaPermissionName);
                        permissionList.add(grandpa);
                    }
                    if (grandpa.getChildren() != null) {
                        grandpa.getChildren().add(parent);
                    } else {
                        grandpa.setChildren(Lists.newArrayList(parent));
                    }
                    permissionList.add(grandpa);
                } else {
                    permissionList.add(parent);
                }
            } else {
                permissionList.add(parent);
            }

            // 获取被权限监控的方法
            List<Method> methods = Stream.of(f.getDeclaredMethods()).filter(method -> method.getAnnotation(PreAuthorize.class) != null).collect(Collectors.toList());
            if (methods.stream().anyMatch(method -> method.getAnnotation(PreAuthorize.class) != null)) {
                for (Method method : methods) {
                    PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);
                    if (preAuthorize.value().contains("hasRole"))
                        continue;

                    Matcher matcher = pattern.matcher(preAuthorize.value());
                    while (matcher.find()) for (String authority : matcher.group().replaceAll("'", "").split(",")) {
                        if (authority.contains("ROLE_"))
                            continue;
                        String url = "";
                        Permission permission = new Permission();
                        permission.setAuthority(authority);
                        String handle = "", name = "";
                        //
                        if (method.getAnnotation(GetMapping.class) != null) {
                            handle = "get";
                            name = "查询";
                            url = controllerUrl + toCover(String.join(",", method.getAnnotation(GetMapping.class).value()));
                        } else if (method.getAnnotation(PostMapping.class) != null) {
                            handle = "insert";
                            name = "新增";
                            url = controllerUrl + toCover(String.join(",", method.getAnnotation(PostMapping.class).value()));
                        } else if (method.getAnnotation(PutMapping.class) != null) {
                            handle = "update";
                            name = "更新";
                            url = controllerUrl + toCover(String.join(",", method.getAnnotation(PutMapping.class).value()));
                        } else if (method.getAnnotation(DeleteMapping.class) != null) {
                            handle = "delete";
                            name = "删除";
                            url = controllerUrl + toCover(String.join(",", method.getAnnotation(DeleteMapping.class).value()));
                        } else if (method.getAnnotation(RequestMapping.class) != null) {
                            RequestMethod[] requestMethods = method.getAnnotation(RequestMapping.class).method();
                            List<String> names = new ArrayList<>();
                            List<String> handles = new ArrayList<>();
                            for (RequestMethod requestMethod : requestMethods) {
                                switch (requestMethod) {
                                    case GET: {
                                        names.add("查询");
                                        handles.add("get");
                                        break;
                                    }
                                    case POST: {
                                        names.add("新增");
                                        handles.add("insert");
                                        break;
                                    }
                                    case PUT: {
                                        names.add("更新");
                                        handles.add("update");
                                        break;
                                    }
                                    case DELETE: {
                                        names.add("删除");
                                        handles.add("delete");
                                        break;
                                    }
                                }
                            }
                            handle = String.join("-", handles);
                            name = String.join("-", names);
                            url = controllerUrl + toCover(String.join(",", method.getAnnotation(RequestMapping.class).value()));
                        }
                        // 使用api注释说明
                        if (method.getAnnotation(ApiOperation.class) != null) {
                            handle = "operation";
                            name = Stream.of(method.getAnnotation(ApiOperation.class).value()).collect(Collectors.joining(","));
                        }


                        // 判断是单个查询还是列表查询
                        if (handle.equalsIgnoreCase("get") && !url.endsWith("{id}")) {
                            handle = "list";
                            name = "列表查询";
                        }
//                        url = url.replaceAll("\\{id}", "");
                        url = url.replaceAll("\\{(.+?)\\}", "");

                        permission.setName(name);
                        permission.setHandle(handle);
                        permission.setUrl(url);
                        if (parent.getChildren() == null)
                            parent.setChildren(new ArrayList<>());
                        parent.getChildren().add(permission);
                    }
                }
            } else {
                permissionList.remove(parent);
            }
        }

        // 设置基础属性
        permissionList.forEach(this::setCreate);

        // 先删除子类，再删除父类
        permissionRepository.deleteByAuthorityIsNull();
        permissionRepository.deleteAll();
        //
        permissionRepository.saveAll(permissionList);
    }

    private void setCreate(Permission permission) {
        permission.setCreateBy(-1L);
        permission.setCreateTime(new Date());
        if (permission.getChildren() != null) {
            permission.getChildren().forEach(this::setCreate);
        }
    }

    private String toCover(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        if (!str.startsWith("/")) {
            return "/" + str;
        }
        return str;
    }

}
