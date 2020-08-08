package com.chris.service;

import com.alibaba.fastjson.JSON;
import com.chris.domain.ResourceExample;
import com.chris.dto.PageDto;
import com.chris.dto.ResourceDto;
import com.chris.mapper.ResourceMapper;
import com.chris.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceService {

    @Resource
    private ResourceMapper resourceMapper;

    public void list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        ResourceExample resourceExample = new ResourceExample();
        List<com.chris.domain.Resource> resourceList = resourceMapper.selectByExample(resourceExample);
        PageInfo<com.chris.domain.Resource> pageInfo = new PageInfo<>(resourceList);
        pageDto.setTotal(pageInfo.getTotal());
        List<ResourceDto> resourceDtoList = CopyUtil.copyList(resourceList, ResourceDto.class);
        pageDto.setList(resourceDtoList);

    }

    public void save(ResourceDto resourceDto){
        com.chris.domain.Resource resource = CopyUtil.copy(resourceDto, com.chris.domain.Resource.class);
        if(StringUtils.isEmpty(resource.getId())){
            insert(resource);
        }else{
            update(resource);
        }
    }

    private void update(com.chris.domain.Resource resource) {
    }

    private void insert(com.chris.domain.Resource resource) {
        resourceMapper.updateByPrimaryKey(resource);
    }

    public void delete(String id) {
        resourceMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public void saveJson(String json){
        List<ResourceDto> jsonList = JSON.parseArray(json, ResourceDto.class);
        List<ResourceDto> list = new ArrayList<>();

        if(!CollectionUtils.isEmpty(jsonList)){
            for(ResourceDto d: jsonList){
                d.setParent("");
                add(list, d);
            }
        }
        resourceMapper.deleteByPrimaryKey(null);
        for(int i=0;i<list.size();i++){
            insert(CopyUtil.copy(list.get(i), com.chris.domain.Resource.class));
        }
    }

    public void add(List<ResourceDto> list, ResourceDto dto){
        list.add(dto);
        if(!CollectionUtils.isEmpty(dto.getChildren())){
            for(ResourceDto d: dto.getChildren()){
                d.setParent(dto.getId());
                add(list, d);
            }
        }
    }

    public List<ResourceDto> loadTree(){
        ResourceExample example = new ResourceExample();
        example.setOrderByClause("id asc");
        List<com.chris.domain.Resource> resourceList = resourceMapper.selectByExample(example);
        List<ResourceDto> resourceDtoList = CopyUtil.copyList(resourceList, ResourceDto.class);
        for(int i = resourceDtoList.size()-1; i>=0;i--){
            ResourceDto child = resourceDtoList.get(i);
            if(StringUtils.isEmpty(child.getParent())){
                continue;
            }
            for(int j=i-1;j>=0;j--){
                ResourceDto parent = resourceDtoList.get(j);
                if(child.getParent().equals(parent.getId())){
                    if(CollectionUtils.isEmpty(parent.getChildren())){
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(0, child);
                    resourceDtoList.remove(child);
                }
            }
        }
        return resourceDtoList;
    }

}
