package com.chris.controller;

import com.chris.dto.PageDto;
import com.chris.dto.UserDto;
import com.chris.service.UserService;
import com.chris.util.CommonResponse;
import com.chris.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/admin/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/list")
    public PageDto list(@RequestBody PageDto pageDto){
        userService.list(pageDto);
        return pageDto;
    }

    @PostMapping("/save")
    public UserDto save(@RequestBody UserDto userDto){
        String password = DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes());
        userDto.setPassword(password);
        ValidatorUtil.require(userDto.getLoginName(), "login name");
        ValidatorUtil.length(userDto.getLoginName(), "login name", 1, 50);
        ValidatorUtil.length(userDto.getName(), "nickname", 1, 50);
        ValidatorUtil.require(userDto.getPassword(), "password");
        userService.save(userDto);
        return userDto;
    }


    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable String id){
        userService.delete(id);
        return "OK";
    }

    @PostMapping("/save-password")
    public UserDto reset(@RequestBody UserDto userDto){
        userDto.setPassword(DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes()));
        userService.savePassword(userDto);
        return userDto;
    }

}
