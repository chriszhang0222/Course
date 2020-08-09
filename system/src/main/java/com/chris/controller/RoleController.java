package com.chris.controller;

import com.chris.dto.PageDto;
import com.chris.dto.RoleDto;
import com.chris.service.RoleService;
import com.chris.util.CommonResponse;
import com.chris.util.ValidatorUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @PostMapping("/list")
    public PageDto list(@RequestBody PageDto pageDto){
        roleService.list(pageDto);
        return pageDto;
    }

    @PostMapping("/save")
    public RoleDto save(@RequestBody RoleDto roleDto){
        ValidatorUtil.require(roleDto.getName(), "角色");
        ValidatorUtil.length(roleDto.getName(), "角色", 1, 50);
        ValidatorUtil.require(roleDto.getDesc(), "描述");
        ValidatorUtil.length(roleDto.getDesc(), "描述", 1, 100);
        roleService.save(roleDto);
        return roleDto;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable String id){
        roleService.delete(id);
        return new CommonResponse();
    }

    @GetMapping("/list-resource/{roleId}")
    public List<String> listResource(@PathVariable String roleId){
        return roleService.listResource(roleId);
    }

    @PostMapping("/save-resource")
    public RoleDto saveResource(@RequestBody RoleDto roleDto){
        roleService.saveResource(roleDto);
        return roleDto;
    }

    @PostMapping("/save-user")
    public RoleDto saveUser(@RequestBody RoleDto roleDto){
        roleService.saveUser(roleDto);
        return roleDto;
    }

    @GetMapping("/list-user/{roleId}")
    public List<String> listUser(@PathVariable String roleId){
        return roleService.listUser(roleId);
    }
}
