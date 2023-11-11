package com.blue.controller;

import com.blue.config.Log;
import com.blue.domain.Code;
import com.blue.domain.Result;
import com.blue.domain.User;
import com.blue.service.MailService;
import com.blue.service.UserService;
import com.blue.utils.RedisUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@RequestMapping("/mails")
@RestController
public class MailController {
    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 就是发送邮箱的验证信息
     * @param email
     * @return
     */
    @Log(operation = "验证邮件")
    @GetMapping("/{email}")
    public Result getCheckCode(@PathVariable String email){
        String checkCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String message = "您的用户验证码为："+checkCode;
        Map<String,String> map2 = new HashMap<>();
        User user =userService.selectByeamil(email);
        System.out.println(user);
        if(user!=null)
        {
            return new Result(Code.GET_ERR,"","该邮箱已经注册");
        }
        try {
            mailService.sendSimpleMail(email, "尊敬的用户你好，您的用户验证码是:", message);
        }catch (Exception e){
            return new Result(Code.GET_ERR,"","该邮箱不存在，检查输入是否错误");
        }
        redisUtil.set(email,checkCode);
        return new Result(Code.GET_OK,redisUtil.get(email),"发送成功");
    }

    @Log(operation = "验证邮件")
    @GetMapping("/check/{email}")
    public Result getCheckCodeOther(@PathVariable String email){
        String checkCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String message = "您的用户验证码为："+checkCode;
        Map<String,String> map2 = new HashMap<>();
        User user =userService.selectByeamil(email);
        System.out.println(user);
        if(user==null)
        {
            return new Result(Code.GET_ERR,"","您输入的有效邮箱未进行注册");
        }
        try {
            mailService.sendSimpleMail(email, "尊敬的用户你好，您的用户验证码是:", message);
        }catch (Exception e){
            return new Result(Code.GET_ERR,"","该邮箱不存在，检查输入是否错误");
        }
        redisUtil.set(email,checkCode);
        return new Result(Code.GET_OK,redisUtil.get(email),"发送成功");
    }


}