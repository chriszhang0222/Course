package com.chris.service;

import com.chris.domain.Category;
import com.chris.domain.CategoryExample;
import com.chris.dto.CategoryDto;
import com.chris.dto.PageDto;
import com.chris.mapper.CategoryMapper;
import com.chris.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    public List<CategoryDto> all(){
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> list = categoryMapper.selectByExample(categoryExample);
        List<CategoryDto> dtoList = list.stream().map((c) -> {
            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(c, categoryDto);
            return categoryDto;
        }).collect(Collectors.toList());
        return dtoList;
    }

    public void list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        pageDto.setTotal(pageInfo.getTotal());
        List<CategoryDto> dtoList = categoryList.stream().map((c) -> {
            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(c, categoryDto);
            return categoryDto;
        }).collect(Collectors.toList());
        pageDto.setList(dtoList);
    }

    public void save(CategoryDto categoryDto){
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto, category);
        if(StringUtils.isEmpty(category.getId())){
            insert(category);
        }else{
            update(category);
        }
    }

    private void insert(Category category) {
        category.setId(UuidUtil.getShortUuid());
        categoryMapper.insert(category);
    }

    private void update(Category category){
        categoryMapper.updateByPrimaryKey(category);
    }

    @Transactional
    public void delete(String id){
        deleteChildren(id);
        categoryMapper.deleteByPrimaryKey(id);
    }

    public void deleteChildren(String id){
        Category category = categoryMapper.selectByPrimaryKey(id);
        if("00000000".equals(category.getParent())){
            CategoryExample example = new CategoryExample();
            example.createCriteria().andParentEqualTo(category.getId());
            categoryMapper.deleteByExample(example);
        }
    }

}
