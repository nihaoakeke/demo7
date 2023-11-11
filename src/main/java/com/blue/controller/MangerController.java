package com.blue.controller;


import com.blue.config.Log;
import com.blue.dao.UserMapper;
import com.blue.domain.Code;
import com.blue.domain.Result;
import com.blue.domain.User;
import com.blue.service.UserService;
import com.blue.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/mangers")
public class MangerController {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @Autowired
    RedisUtil redisUtil;


    @Log(operation = "删除用户")
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Integer id)
    {
        Integer num =userMapper.deleteById(id);
        Integer code = num !=null ? Code.DELETE_OK :Code.DELETE_ERR;
        String msg = num != null?"数据删除成功":"数据删除失败，请重试,请检查是否有信息输入错误";
        return new Result(code,"",msg);
    }

    @Log(operation = "得到所有用户信息")
    @GetMapping("/getAll")
    public Result getAll()
    {
        List<User> userList =userService.list();
        Integer code = userList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = userList != null?"数据查询成功":"数据查询失败，请重试,请检查是否有信息输入错误";
        Iterator<User> iterator = userList.iterator();
        while (iterator.hasNext()){
            User user1 = iterator.next();
            user1.setUpassword(null);
        }
       return new Result(code,userList,msg);
    }








}
