package com.chris.service;

import com.alibaba.fastjson.JSON;
import com.chris.Exception.BusinessException;
import com.chris.Exception.BusinessExceptionCode;
import com.chris.domain.User;
import com.chris.domain.UserExample;
import com.chris.dto.LoginDto;
import com.chris.dto.PageDto;
import com.chris.dto.ResourceDto;
import com.chris.dto.UserDto;
import com.chris.mapper.UserMapper;
import com.chris.mapper.my.MyUserMapper;
import com.chris.util.CopyUtil;
import com.chris.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private MyUserMapper myUserMapper;

    public void list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        UserExample example = new UserExample();
        List<User> userList = userMapper.selectByExample(example);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        pageDto.setTotal(pageInfo.getTotal());
        List<UserDto> userDtos = CopyUtil.copyList(userList, UserDto.class);
        pageDto.setList(userDtos);
    }

    public void save(UserDto userDto){
        User user = CopyUtil.copy(userDto, User.class);
        if(StringUtils.isEmpty(user.getId())){
            insert(user);
        }else{
            update(user);
        }
    }

    private void insert(User user) {
        user.setId(UuidUtil.getShortUuid());
        User userdb = this.selectByLoginName(user.getLoginName());
        if(userdb != null){
            throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
        }
        userMapper.insert(user);

    }

    private void update(User user){
        user.setPassword(null);
        userMapper.updateByPrimaryKeySelective(user);
    }

    public User selectByLoginName(String loginName){
        UserExample example = new UserExample();
        example.createCriteria().andLoginNameEqualTo(loginName);
        List<User> userList = userMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(userList)){
            return null;
        }
        return userList.get(0);
    }

    public void delete(String id){
        userMapper.deleteByPrimaryKey(id);
    }

    public void savePassword(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setPassword(userDto.getPassword());
        userMapper.updateByPrimaryKeySelective(user);
    }

    public LoginDto login(UserDto userDto){
        User user = selectByLoginName(userDto.getLoginName());
        if(user == null){
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        }else{
            if(user.getPassword().equals(userDto.getPassword())){
                LoginDto loginDto = CopyUtil.copy(user, LoginDto.class);
                setAuth(loginDto);
                return loginDto;
            }else{
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }

    private void setAuth(LoginDto loginDto){
        List<ResourceDto> resourceDtoList = myUserMapper.findResources(loginDto.getId());
        loginDto.setResources(resourceDtoList);
        HashSet<String> requestSet = new HashSet<>();
        if(!CollectionUtils.isEmpty(resourceDtoList)){
            for(int i=0;i<resourceDtoList.size();i++){
                ResourceDto resourceDto = resourceDtoList.get(i);
                String arrayString = resourceDto.getRequest();
                List<String> requestList = JSON.parseArray(arrayString, String.class);
                if(!CollectionUtils.isEmpty(requestList)){
                    requestSet.addAll(requestList);
                }
            }
        }
        log.info("Auth Requests: {}", requestSet);
        loginDto.setRequests(requestSet);
    }


}
