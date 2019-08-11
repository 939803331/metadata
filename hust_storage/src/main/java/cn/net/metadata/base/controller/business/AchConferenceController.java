package cn.net.metadata.base.controller.business;

import cn.net.metadata.base.model.business.AchConference;
import cn.net.metadata.base.service.business.AchConferenceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/* 
 * AchConferenceController
 */
@RestController
@RequestMapping("achConference")
@Api(tags = "管理", description = "管理相关API")
public class AchConferenceController {
    
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('achConference:delete')")
    public Boolean delete(@PathVariable Long id)
    {
        achConferenceService.delete(id);
        return true;
    }

    @ModelAttribute
    public AchConference fill(@RequestParam(required = false) Long id)
    {
        if (id != null) {
            return achConferenceService.findById(id).orElseThrow(() -> new RuntimeException("该数据不存在，请检查对应的ID。"));
        }
        return new AchConference();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('achConference:get')")
    public AchConference get(@PathVariable Long id)
    {
        return achConferenceService.findById(id).orElse(null);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('achConference:list')")
    public Page<AchConference> list(AchConference achConference, Pageable pageable)
    {
        return achConferenceService.find4Page(achConference, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('achConference:save')")
    public AchConference save(@Validated AchConference achConference)
    {
        return achConferenceService.save(achConference);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('achConference:udpate')")
    public AchConference update(@Validated AchConference achConference)
    {
        return achConferenceService.save(achConference);
    }

    @Autowired
    private AchConferenceService achConferenceService;
}