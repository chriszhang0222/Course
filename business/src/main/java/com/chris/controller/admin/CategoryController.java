package com.chris.controller.admin;

import com.chris.dto.CategoryDto;
import com.chris.dto.PageDto;
import com.chris.service.CategoryService;
import com.chris.util.CommonResponse;
import com.chris.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @PostMapping("/all")
    public List<CategoryDto> all(){
        return categoryService.all();
    }

    @PostMapping("/list")
    public PageDto list(@RequestBody PageDto pageDto){
        categoryService.list(pageDto);
        return pageDto;
    }

    @PostMapping("/save")
    public CategoryDto save(@RequestBody CategoryDto categoryDto){
        ValidatorUtil.require(categoryDto.getParent(), "parent id");
        ValidatorUtil.require(categoryDto.getName(), "category name");
        categoryService.save(categoryDto);
        return categoryDto;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable String id){
        categoryService.delete(id);
        return new CommonResponse("200", "OK");
    }
}
