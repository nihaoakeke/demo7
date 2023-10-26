package com.blue.controller;


import com.blue.config.Log;
import com.blue.dao.UserDao;
import com.blue.domain.Code;
import com.blue.domain.Comment;
import com.blue.domain.Result;
import com.blue.domain.User;
import com.blue.service.UserService;
import com.blue.utils.TokenUtils;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {



    @Autowired UserDao userDao;

    @Autowired UserService userService;


    @Log(operation = "用户通过账号密码登入")
    @PostMapping("/login")
    public Result passwordlogin(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username=user.getUName();
//        String uemail =user.getUEmail();
        User user1 = null;
        System.out.println("????????????????"+username+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        if(!username.contains("@"))
        {
             user1 =userService.selectByname(username);
        }
        else {
             user1 = userService.selectByeamil(username);
        }
        if(user1==null)
        {
            return new Result(Code.POST_ERR,"","输入的账户不存在");
        }
        System.out.println(user1.getUPassword());
        System.out.println(user.getUPassword());
        if(!user1.getUPassword().equals(user.getUPassword()))
        {
            return new Result(Code.POST_ERR,"","输入的密码错误");
        }
        String token= TokenUtils.sign(user);
        System.out.println(user.getUName());
        Cookie cookie = new Cookie("token", token);
//        设置cookie的作用域：为”/“时，以在webapp文件夹下的所有应用共享cookie
        cookie.setPath("/");
        response.addCookie(cookie);
        response.addHeader("Authorization", token);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
//        HashMap<String,Object> hs=new HashMap<>();
//        hs.put("token",token);
        user1.setUPassword(null);
        return new Result(Code.POST_OK,user1,"登入成功");
    }


//    @Log(operation = "用户通过邮箱进行登入")
//    @PostMapping("/email/login")
//    public Result emaillogin(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//        String email=user.getUEmail();
//        User user1 = userService.selectByeamil(email);
//        if(user1==null)
//        {
//            return new Result(Code.POST_ERR,"","输入的账户不存在");
//        }
//        System.out.println(user1.getUPassword());
//        System.out.println(user.getUPassword());
//        if(!user1.getUPassword().equals(user.getUPassword()))
//        {
//            return new Result(Code.POST_ERR,"","输入的密码错误");
//        }
//        String token= TokenUtils.sign(user);
//        System.out.println(user.getUName());
//        Cookie cookie = new Cookie("token", token);
////        设置cookie的作用域：为”/“时，以在webapp文件夹下的所有应用共享cookie
//        cookie.setPath("/");
//        response.addCookie(cookie);
//
//        response.addHeader("Authorization", token);
//        response.addHeader("Access-Control-Expose-Headers", "Authorization");
////        HashMap<String,Object> hs=new HashMap<>();
////        hs.put("token",token);
//        return new Result(Code.POST_OK,user1,"登入成功");
//    }
//    public ResponseEntity<?> test(HttpServletRequest request, @RequestBody User user){
//        Map<String ,String> map = new HashMap<>();
//        String username = user.getName();
//        String token = request.getHeader("Authorization");
//        if(token == null){
//            token= TokenUtils.sign(user);
//            map.put("message", "log success!");
//            System.out.println("dadasd");
//            return ResponseEntity.ok()
//                    .header("Authorization", token)
//                    .header("Access-Control-Expose-Headers", "Authorization")
//                    .body(map);
//        } else{
//            //Claims claims = JwtUtil.parseToken(token);
//            String role = null;
//            map.put("message", "welcome!" + username + role);
//            return ResponseEntity.ok(map);
//        }
//    }

    @Log(operation = "通过用户名搜索用户")
    @GetMapping("/selectUserByname")
    public Result selectUserByname(@RequestBody User user)
    {
        System.out.println(user);
        User user1 = userService.selectByname(user.getUName());
        Integer code = user1 != null ? Code.GET_OK : Code.GET_ERR;
        String msg = user1 != null ? "数据查询成功" : "数据查询失败,请重试，请检查是否有信息输入错误";
        user1.setUPassword(null);
        return new Result(code,user1,msg);
    }

    @Log(operation = "通过用户的笔名来搜索用户")
    @GetMapping ("/selectUserBynname")
    public Result selectUserBynname(@RequestBody User user)
    {
        User user1 = userService.selectBynickname(user.getUNickname());
        Integer code = user1 != null ? Code.GET_OK : Code.GET_ERR;
        String msg = user1 != null ? "数据查询成功" : "数据查询失败，请重试，请检查是否有信息输入错误";
        user1.setUPassword(null);
        return new Result(code,user1,msg);
    }

    @Log(operation="通过模糊笔名查询搜索用户")
    @GetMapping("/selectLikenickname")
    public Result selectLikenickname(@RequestBody User user)
    {
        List<User> userList = userService.selectLikenickname(user.getUNickname());
        Integer code = userList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = userList != null ? "数据查询成功" : "数据查询失败，请重试，请检查是否有信息输入错误";
        Iterator<User> iterator = userList.iterator();
        while (iterator.hasNext()){ //判断迭代器是否有元素
            User user1 = iterator.next();//获取集合下一个元
            user1.setUPassword(null);
        }
        return new Result(code,userList,msg);
    }

    @Log(operation = "通过模糊用户名来搜索用户")
    @GetMapping("/selectLikename")
    public Result selectLikename(@RequestBody User user)
    {
        List<User> userList = userService.selectLikename(user.getUName());
        Integer code = userList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = userList != null ? "数据查询成功" : "数据查询失败，请重试，请检查是否有信息输入错误";
        Iterator<User> iterator = userList.iterator();
        while (iterator.hasNext()){ //判断迭代器是否有元素
            User user1 = iterator.next();//获取集合下一个元
            user1.setUPassword(null);
        }
        return new Result(code,userList,msg);
    }

    @Log(operation = "通过用户的id来搜索用户")
    @GetMapping("/{id}")
    public Result selectLikename(@PathVariable Integer id)
    {
        User user = userDao.selectById(id);
        Integer code = user != null ? Code.GET_OK : Code.GET_ERR;
        String msg = user != null ? "数据查询成功" : "数据查询失败，请重试，请检查是否有信息输入错误";
        user.setUPassword(null);
        return new Result(code,user,msg);
    }

    @Log(operation = "更新用户信息")
    @PutMapping
    public Result updateUser(@RequestBody User user)
    {
        Integer num = userDao.updateById(user);
        Integer code = num !=null ? Code.UPDATE_OK :Code.UPDATE_ERR;
        String msg = num != null?"数据更新成功":"数据更新失败，请重试，请检查是否有信息输入错误";
        user.setUPassword(null);
        return new Result(code,user,msg);
    }

    @Log(operation = "删除用户")
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Integer id)
    {
        Integer num =userDao.deleteById(id);
        Integer code = num !=null ? Code.DELETE_OK :Code.DELETE_ERR;
        String msg = num != null?"数据删除成功":"数据删除失败，请重试,请检查是否有信息输入错误";
        return new Result(code,"",msg);
    }

    @Log(operation = "注册用户信息")
    @PostMapping
    public Result save(@RequestBody User user){

       Integer num = userDao.insert(user);
        Integer code = num !=null ? Code.SAVE_OK :Code.SAVE_ERR;
        String msg = num != null?"数据保存成功":"数据保存失败，请重试,请检查是否有信息输入错误";
        user.setUPassword(null);
        return new Result(code,user,msg);
    }

    @Log(operation = "得到所有用户信息")
    @GetMapping("/getAll")
    public Result getAll()
    {
        List<User> userList =userService.list();
        Integer code = userList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = userList != null?"数据查询成功":"数据查询失败，请重试,请检查是否有信息输入错误";
        Iterator<User> iterator = userList.iterator();
        while (iterator.hasNext()){ //判断迭代器是否有元素
            User user1 = iterator.next();//获取集合下一个元
            user1.setUPassword(null);
        }
       return new Result(code,userList,msg);
    }


}
