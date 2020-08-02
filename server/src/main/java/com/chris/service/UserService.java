package com.chris.service;

import com.chris.domain.User;
import com.chris.domain.UserExample;
import com.chris.dto.PageDto;
import com.chris.dto.UserDto;
import com.chris.mapper.UserMapper;
import com.chris.util.CopyUtil;
import com.chris.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

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
            throw new RuntimeException("User Name already Exist");
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
}
