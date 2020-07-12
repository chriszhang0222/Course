package com.chris.controller.admin;

import com.chris.dto.CourseDto;
import com.chris.dto.subpagedto.CoursePageDto;
import com.chris.service.CourseService;
import com.chris.util.CommonResponse;
import com.chris.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/admin/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    @PostMapping("/list")
    public CoursePageDto list(@RequestBody CoursePageDto coursePageDto){
        courseService.list(coursePageDto);
        return coursePageDto;
    }

    @PostMapping("/save")
    public CourseDto save(@RequestBody CourseDto courseDto){
        ValidatorUtil.require(courseDto.getName(), "Name");
        courseService.save(courseDto);
        return courseDto;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable String id){
        courseService.delete(id);
        return new CommonResponse("200", "OK");
    }
}
