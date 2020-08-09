package com.chris.service;

import com.chris.domain.*;
import com.chris.dto.PageDto;
import com.chris.dto.RoleDto;
import com.chris.mapper.RoleMapper;
import com.chris.mapper.RoleResourceMapper;
import com.chris.mapper.RoleUserMapper;
import com.chris.util.CopyUtil;
import com.chris.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleResourceMapper roleResourceMapper;

    @Resource
    private RoleUserMapper roleUserMapper;

    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        RoleExample roleExample = new RoleExample();
        List<Role> roleList = roleMapper.selectByExample(roleExample);
        PageInfo<Role> pageInfo = new PageInfo<>(roleList);
        pageDto.setTotal(pageInfo.getTotal());
        List<RoleDto> roleDtoList = CopyUtil.copyList(roleList, RoleDto.class);
        pageDto.setList(roleDtoList);
    }

    public void save(RoleDto roleDto) {
        Role role = CopyUtil.copy(roleDto, Role.class);
        if (StringUtils.isEmpty(roleDto.getId())) {
            this.insert(role);
        } else {
            this.update(role);
        }
    }

    /**
     * 新增
     */
    private void insert(Role role) {
        role.setId(UuidUtil.getShortUuid());
        roleMapper.insert(role);
    }

    /**
     * 更新
     */
    private void update(Role role) {
        roleMapper.updateByPrimaryKey(role);
    }

    /**
     * 删除
     */
    public void delete(String id) {
        roleMapper.deleteByPrimaryKey(id);
    }

    public List<String> listResource(String roleId){
        RoleResourceExample roleResourceExample = new RoleResourceExample();
        roleResourceExample.createCriteria().andRoleIdEqualTo(roleId);
        List<RoleResource> roleResourceList = roleResourceMapper.selectByExample(roleResourceExample);
        return roleResourceList.stream().map((r) -> r.getResourceId()).collect(Collectors.toList());
    }

    public void saveResource(RoleDto roleDto){
        String roleId = roleDto.getId();
        List<String> resourceIds = roleDto.getResourceIds();
        RoleResourceExample example = new RoleResourceExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        roleResourceMapper.deleteByExample(example);

        for(String id: resourceIds){
            RoleResource roleResource = new RoleResource();
            roleResource.setId(UuidUtil.getShortUuid());
            roleResource.setRoleId(roleId);
            roleResource.setResourceId(id);
            roleResourceMapper.insert(roleResource);
        }
    }

    public void saveUser(RoleDto roleDto){
        RoleUserExample example = new RoleUserExample();
        example.createCriteria().andRoleIdEqualTo(roleDto.getId());
        roleUserMapper.deleteByExample(example);
        String roleId = roleDto.getId();
        List<String> useridList = roleDto.getUserIds();
        for(String id: useridList){
            RoleUser roleUser = new RoleUser();
            roleUser.setId(UuidUtil.getShortUuid());
            roleUser.setRoleId(roleId);
            roleUser.setUserId(id);
            roleUserMapper.insert(roleUser);
        }
    }

    public List<String> listUser(String roleId){
        RoleUserExample example = new RoleUserExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<RoleUser> roleUserList = roleUserMapper.selectByExample(example);
        return roleUserList.stream().map((r) -> r.getUserId()).collect(Collectors.toList());
    }

}
