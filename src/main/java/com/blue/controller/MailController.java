package com.blue.controller;

import com.blue.config.Log;
import com.blue.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Random;


@RequestMapping("/mails")
@RestController
public class MailController {
    @Autowired
    private MailService mailService;

//    @RequestMapping("getCheckCode")
//    @ResponseBody
    @Log(operation = "验证邮件")
    @GetMapping("/{email}")
    public String getCheckCode(@PathVariable String email){
        String checkCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String message = "您的注册验证码为："+checkCode;
        try {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAA");
            mailService.sendSimpleMail(email, "注册验证码", message);
        }catch (Exception e){
            return "";
        }
        System.out.println("____________>>>>>>>>>>>");
        return checkCode;
    }
}