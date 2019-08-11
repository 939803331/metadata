package cn.net.metadata.base.controller.business;

import cn.net.metadata.base.model.business.FieldEnable;
import cn.net.metadata.base.service.business.FieldEnableService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/* 
 * FieldEnableController
 */
@RestController
@RequestMapping("fieldEnable")
@Api(tags = "管理", description = "管理相关API")
public class FieldEnableController {
    
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('fieldEnable:delete')")
    public Boolean delete(@PathVariable Long id)
    {
        fieldEnableService.delete(id);
        return true;
    }

    @ModelAttribute
    public FieldEnable fill(@RequestParam(required = false) Long id)
    {
        if (id != null) {
            return fieldEnableService.findById(id).orElseThrow(() -> new RuntimeException("该数据不存在，请检查对应的ID。"));
        }
        return new FieldEnable();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('fieldEnable:get')")
    public FieldEnable get(@PathVariable Long id)
    {
        return fieldEnableService.findById(id).orElse(null);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('fieldEnable:list')")
    public Page<FieldEnable> list(FieldEnable fieldEnable, Pageable pageable)
    {
        return fieldEnableService.find4Page(fieldEnable, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('fieldEnable:save')")
    public FieldEnable save(@Validated FieldEnable fieldEnable)
    {
        return fieldEnableService.save(fieldEnable);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('fieldEnable:udpate')")
    public FieldEnable update(@Validated FieldEnable fieldEnable)
    {
        return fieldEnableService.save(fieldEnable);
    }

    @Autowired
    private FieldEnableService fieldEnableService;
}