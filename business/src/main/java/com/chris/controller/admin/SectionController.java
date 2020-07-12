package com.chris.controller.admin;

import com.chris.dto.SectionDto;
import com.chris.dto.subpagedto.SectionPageDto;
import com.chris.service.SectionService;
import com.chris.util.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/section")
@Slf4j
public class SectionController {

    @Resource
    private SectionService sectionService;

    @PostMapping("/list")
    public SectionPageDto list(@RequestBody SectionPageDto sectionPageDto){
        sectionService.list(sectionPageDto);
        return sectionPageDto;
    }

    @PostMapping("/save")
    public CommonResponse save(@RequestBody SectionDto sectionDto){
        CommonResponse<SectionDto> response = new CommonResponse<>();
        sectionService.save(sectionDto);
        response.setContent(sectionDto);
        return response;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable String id){
        CommonResponse response = new CommonResponse("200", "OK");
        sectionService.delete(id);
        return response;
    }
}
