package com.chris.controller.admin;

import com.chris.dto.subpagedto.SectionPageDto;
import com.chris.service.SectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
