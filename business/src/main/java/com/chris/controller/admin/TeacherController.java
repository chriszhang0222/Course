package com.chris.controller.admin;

import com.chris.dto.PageDto;
import com.chris.dto.TeacherDto;
import com.chris.service.TeacherService;
import com.chris.util.CommonResponse;
import com.chris.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/teacher")
@Slf4j
public class TeacherController {

    @Resource
    private TeacherService teacherService;

    @PostMapping("/all")
    public List<TeacherDto> all(){
        return teacherService.all();
    }

    @PostMapping("/list")
    public PageDto list(@RequestBody PageDto pageDto){
        teacherService.list(pageDto);
        return pageDto;
    }

    @PostMapping("/save")
    public TeacherDto save(@RequestBody TeacherDto teacherDto){
        ValidatorUtil.require(teacherDto.getName(), "Name");
        teacherService.save(teacherDto);
        return teacherDto;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable String id){
        teacherService.delete(id);
        return new CommonResponse("200", "OK");
    }
}
