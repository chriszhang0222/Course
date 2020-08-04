package com.chris.controller;

import com.chris.Constants;
import com.chris.domain.User;
import com.chris.dto.LoginDto;
import com.chris.dto.PageDto;
import com.chris.dto.UserDto;
import com.chris.service.UserService;
import com.chris.util.CommonResponse;
import com.chris.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

    @PostMapping("/login")
    public LoginDto login(@RequestBody UserDto userDto, HttpServletRequest request){
        CommonResponse res = new CommonResponse();
        userDto.setPassword(DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes()));
        LoginDto loginDto = userService.login(userDto);
        request.getSession().setAttribute(Constants.LOGIN_USER, loginDto);
        return loginDto;
    }

    @GetMapping("/logout")
    public CommonResponse logout(HttpServletRequest request){
        request.getSession().removeAttribute(Constants.LOGIN_USER);
        return new CommonResponse("200", "OK");
    }

}
