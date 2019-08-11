package cn.net.metadata.base.controller.core;

import cn.net.metadata.base.model.core.User;
import cn.net.metadata.base.repository.core.UserRepository;
import cn.net.metadata.base.service.core.UserService;
import cn.net.metadata.base.utility.MDStringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.controller
 * @date 2018/7/18 17:54
 */
@RestController
@RequestMapping("user")
@Api(tags = "用户管理", description = "用户管理相关API")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @ModelAttribute
    public User fill(@RequestParam(required = false) Long id) {
        if (id != null) {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("该数据不存在，请检查对应的ID。"));
            // 修改时置空
            user.setRoles(new ArrayList<>());
            return user;
        }
        return new User();
    }


    /**
     * 根据单个ID查询用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('user:get')")
    public User get(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * 分页返回用户列表
     *
     * @param user
     * @param page
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('user:list','ROLE_ADMIN')")
    public Page<User> list(User user, Pageable page) {
        return userRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (MDStringUtils.isNotBlank(user.getUsername()))
                predicateList.add(criteriaBuilder.like(root.get("userName"), "%" + user.getUsername() + "%"));
            if (MDStringUtils.isNotBlank(user.getName()))
                predicateList.add(criteriaBuilder.like(root.get("name"), "%" + user.getName() + "%"));
            if (MDStringUtils.isNotBlank(user.getContact()))
                predicateList.add(criteriaBuilder.like(root.get("contact"), "%" + user.getContact() + "%"));
            if (MDStringUtils.isNotBlank(user.getAddress()))
                predicateList.add(criteriaBuilder.like(root.get("address"), "%" + user.getAddress() + "%"));
            if (user.getRoles() != null && user.getRoles().size() > 0)
                predicateList.add(criteriaBuilder.equal(root.joinList("roles"), user.getRoles()));
            query.where(predicateList.toArray(new Predicate[]{}));
            return query.getRestriction();
        }, page);
    }


    /**
     * 用户注册/保存新用户
     *
     * @param user
     * @return
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User save(@Validated User user) {
        try {
            user = userService.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("用户名已经存在。");
        }
        return user;
    }

    /**
     * 用户自助注册
     *
     * @param user
     * @return
     */
    @PostMapping("signup")
    @ApiOperation(value = "用户注册")
    public User register(@Validated User user) {
        try {
            user = userService.register(user);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("用户名已经存在。");
        }
        return user;
    }

    /**
     * 更新
     *
     * @param user
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('user:update')")
    public User update(User user) {
        return userService.update(user);
    }


    /**
     * 根据ID删除一个对象
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean delete(@PathVariable Long id) {
        userRepository.deleteById(id);
        return true;
    }
}
