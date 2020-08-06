package com.chris.controller;

import com.alibaba.fastjson.JSON;
import com.chris.Constants;
import com.chris.domain.User;
import com.chris.dto.LoginDto;
import com.chris.dto.PageDto;
import com.chris.dto.UserDto;
import com.chris.service.UserService;
import com.chris.util.CommonResponse;
import com.chris.util.UuidUtil;
import com.chris.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/admin/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

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
    public CommonResponse login(@RequestBody UserDto userDto, HttpServletRequest request){
        CommonResponse res = new CommonResponse();
        userDto.setPassword(DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes()));
        String imageCode = (String)redisTemplate.opsForValue().get(userDto.getImageCodeToken());
        if(StringUtils.isEmpty(imageCode)){
            res.setSuccess(false);
            res.setMessage("ImageCode has expired!");
            return res;
        }
        if(!imageCode.toLowerCase().equals(userDto.getImageCode().toLowerCase())){
            res.setSuccess(false);
            res.setMessage("ImageCode is not correct");
            return res;
        }
        LoginDto loginDto = userService.login(userDto);
        String token = Constants.LOGIN_USER + "-"+ UuidUtil.getShortUuid();
        loginDto.setToken(token);
        redisTemplate.opsForValue().set(token, JSON.toJSONString(loginDto), 3600, TimeUnit.SECONDS);
        res.setContent(loginDto);
        return res;
    }

    @GetMapping("/logout")
    public CommonResponse logout(HttpServletRequest request){
        request.getSession().removeAttribute(Constants.LOGIN_USER);
        return new CommonResponse("200", "OK");
    }

}
