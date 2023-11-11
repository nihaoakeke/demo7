package com.blue.controller;


import com.blue.config.Log;
import com.blue.dao.BlogLikeDao;
import com.blue.dao.CollectDao;
import com.blue.dao.CommentLikeDao;
import com.blue.dao.UserMapper;
import com.blue.domain.*;
import com.blue.service.FollowLinkService;
import com.blue.service.UserService;
import com.blue.utils.*;
//import com.blue.utils.TokenUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BlogLikeDao blogLikeDao;
    @Autowired
    private CommentLikeDao commentLikeDao;
    @Autowired
    private CollectDao collectDao;
    @Autowired
    private  UserService userService;
    @Autowired
    private  RedisUtil redisUtil;
    @Autowired
    private  FollowLinkService followLinkService;


    /**
     * 用户登入账号，可以邮箱或者用户名登入
     * @param user 登入用户
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @Log(operation = "用户通过账号登入")
    @PostMapping("/login")
    public Result passwordlogin(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username=user.getUname();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        User user1 = null;

        if(!username.contains("@")) {user1 =userService.selectByname(username);}
        else{user1 = userService.selectByeamil(username);}

        if(user1==null||user1.getUFlag()==2) {
            return new Result(Code.POST_ERR,"","输入的账户不存在,或者您的用户处于封禁状态");
        }
        if(!user1.getUpassword().equals(MD5Utils.md5(user.getUpassword()))) {
            return new Result(Code.POST_ERR,"","输入的密码错误");
        }
        String ip = KeyUtils.getIpAddress(request);
        String token2 = (String) redisUtil.get(String.valueOf(user1.getUId()));

        if(token2!=null){
            return new Result(Code.POST_ERR,"","您已经登入,不能重复登入");
        }
        String token= TokenUtils.sign(user1,ip);
        redisUtil.set(String.valueOf(user1.getUId()),token, 100*60 * 2);
        response.addHeader("Authorization", token);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        user1.setUpassword(null);user1.setUlastTime(date);
        return new Result(Code.POST_OK,user1,"登入成功");
    }
    /**
     * 退出登入
     * @param request
     * @return
     */
    @Log(operation = "退出登入")
    @GetMapping("/logut")
    public Result logout(HttpServletRequest request)
    {
        String token = request.getHeader("Authorization");
        String userId = TokenUtils.getUID(token);
        redisUtil.del(userId);
        return new Result(Code.GET_OK,"","退出成功");
    }
    /**
     * 通过笔名模糊查询用户
     * @param user
     * @return
     */
    @Log(operation="通过模糊笔名查询搜索用户")
    @GetMapping("/selectLikenickname")
    public Result selectLikenickname(@RequestBody User user)
    {
        List<User> userList = userService.selectLikenickname(user.getUNickname());
        Integer code = userList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = userList != null ? "数据查询成功" : "数据查询失败，请重试，请检查是否有信息输入错误";
        Iterator<User> iterator = userList.iterator();
        while (iterator.hasNext()){
            User user1 = iterator.next();
            user1.setUpassword(null);}
        return new Result(code,userList,msg);
    }
    /**
     * 通过模糊用户名来搜索用户
     * @param user
     * @return
     */
    @Log(operation = "通过模糊用户名来搜索用户")
    @GetMapping("/selectLikename")
    public Result selectLikename(@RequestBody User user)
    {
        List<User> userList = userService.selectLikename(user.getUname());
        Integer code = userList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = userList != null ? "数据查询成功" : "数据查询失败，请重试，请检查是否有信息输入错误";
        Iterator<User> iterator = userList.iterator();
        while (iterator.hasNext()){
            User user1 = iterator.next();
            user1.setUpassword(null);
        }
        return new Result(code,userList,msg);
    }
    /**
     * 通过用户的id来搜索一个用户信息
     * @param id 传入的id
     * @return
     */
    @Log(operation = "通过用户的id来搜索用户")
    @GetMapping("/{id}")
    public Result selectLikename(@PathVariable Integer id)
    {
        User user = userMapper.selectById(id);
        Integer code = user != null ? Code.GET_OK : Code.GET_ERR;
        String msg = user != null ? "数据查询成功" : "数据查询失败，请重试，请检查是否有信息输入错误";
        user.setUpassword(null);
        return new Result(code,user,msg);
    }

    /**
     * 更新用户的个人信息
     * @param user
     * @return
     */
    @Log(operation = "更新用户信息")
    @PutMapping
    public Result updateUser(@RequestBody User user)
    {
        Integer num = userMapper.updateById(user);
        Integer code = num !=null ? Code.UPDATE_OK :Code.UPDATE_ERR;
        String msg = num != null?"数据更新成功":"数据更新失败，请重试，请检查是否有信息输入错误";
        User user1 =userMapper.selectById(user.getUId());
        user1.setUpassword(null);
        return new Result(code,user1,msg);
    }
    /**
     * 重置用户的密码
     * @param jsonstring
     * @return
     */
    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody String jsonstring){
        Map<String, Object> stringHashMap = new HashMap<String, Object>();
        Gson gson = new Gson();
        Map map = gson.fromJson(String.valueOf(jsonstring), stringHashMap.getClass());
        String uemail = (String) map.get("uemail");
        String code = (String) map.get("code");
        String password1 = (String) map.get("password1");
        String password2 = (String) map.get("password2");
        String code1 = (String) redisUtil.get(uemail);
        if(!code1.equals(code)){
            return new Result(Code.POST_ERR,"","验证码错误，重置密码失败");
        }else if(!password1.equals(password2)){
            return new Result(Code.POST_ERR,"","两次密码不一样，重置密码失败");
        }
        else if(!PwdCheckUtil.checkPasswordLength(password1, "8", null)
                || !PwdCheckUtil.checkContainCase(password1)
                || !PwdCheckUtil.checkContainDigit(password1)
                || !PwdCheckUtil.checkContainSpecialChar(password1)
        ){
            return new Result(Code.POST_ERR,"","密码设置过于简单，请长度大于8，包含大小写，特殊字符，数字");
        }
        User user = userService.selectByeamil(uemail);
        user.setUpassword(MD5Utils.md5(password1));
        userMapper.updateById(user);
        return new Result(Code.POST_OK,"","重置密码成功");
    }
    /**
     * 注册用户
     * @param jsonstring
     * @return
     */
    @Log(operation = "注册用户信息")
    @PostMapping("/register")
    public Result save(@RequestBody String jsonstring){
        Map<String, Object> stringHashMap = new HashMap<String, Object>();
        Gson gson = new Gson();
        Map map = gson.fromJson(String.valueOf(jsonstring), stringHashMap.getClass());
        String username = (String) map.get("uname");String userpassword = (String) map.get("upassword");
        String useremail =(String) map.get("uemail");String userphone=(String)map.get("uphone");
        String code = (String) map.get("code");
        String code1 = (String) redisUtil.get(useremail);
        if(username==null||userpassword==null||userphone==null) {
            return new Result(Code.SAVE_ERR,"","用户信息不完善，注册失败");}
        if(!PwdCheckUtil.checkPasswordLength(userpassword, "8", null)
                || !PwdCheckUtil.checkContainCase(userpassword)
                || !PwdCheckUtil.checkContainDigit(userpassword)
                || !PwdCheckUtil.checkContainSpecialChar(userpassword)
        ){return new Result(Code.SAVE_ERR,"","密码设置过于简单，请长度大于8，包含大小写，特殊字符，数字");}
        if(!code.equals(code1))
        {
            return new Result(Code.SAVE_ERR,"","用户输入的验证码有问题，注册失败");}
        User user1= userService.selectByname(username);
        if(user1!=null){
            return new Result(Code.SAVE_ERR,"","该用户已经存在，请重新起用户名");
        }
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        String password = MD5Utils.md5(userpassword);
        User user = new User();
        user.setUpassword(password);user.setUregisTime(date);user.setUEmail(useremail);
        user.setUname(username);user.setUPhone(userphone);user.setUNickname(username);
        Integer num = userMapper.insert(user);
        Integer co = num !=null ? Code.SAVE_OK :Code.SAVE_ERR;
        String msg = num != null?"数据保存成功":"数据保存失败，请重试,请检查是否有信息输入错误";
        user.setUpassword(null);
        User user2 =userService.selectByname(username);
        return new Result(co,user2,"注册成功");
    }
    /**
     * 得到所有用户的信息
     * @return
     */
    @Log(operation = "得到所有用户信息")
    @GetMapping("/manager/getAll")
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
    /**
     * 关注不关注对方
     * @param followLink
     * @param flag
     * @return
     */
    @Log(operation = "关注用户操作")
    @PostMapping("/follow/{flag}")
    public Result okFollow(@RequestBody FollowLink followLink,@PathVariable Integer flag)
    {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        if(flag==1){
            if(userService.checkFollow(followLink.getFromUser(),followLink.getToUser())) {
                return new Result(Code.GET_OK,"","您已经关注过了，无法重复关注");
            }
            followLinkService.okFollow(followLink.getFromUser(),followLink.getToUser(),date);
            return new Result(Code.GET_OK,"","关注成功");}
        else{
            followLinkService.falseFollow(followLink.getFromUser(),followLink.getToUser());
            return new Result(Code.GET_OK,"","取关成功");}
    }
    /**
     * 查看是否关注
     * @param fromuser 关注方
     * @param touser 被关注方
     * @return
     */
    @Log(operation = "查看是否关注")
    @GetMapping("/checkFollow/{fromuser}/{touser}")
    public Result checkFollow(@PathVariable Integer fromuser,@PathVariable Integer touser) {
        Boolean flag = userService.checkFollow(fromuser,touser);
        Integer code = flag!=false?Code.GET_OK:Code.GET_ERR;
        String msg = flag!=false?"1":"0";
        return new Result(code,"",msg);
    }
    /**
     * 查看关注列表
     * @param uid
     * @param flag
     * @return
     */
    @Log(operation = "查看关注列表")
    @GetMapping("/followers/{uid}/{flag}")
    public Result selectFollowers(@PathVariable Integer uid,@PathVariable Integer flag) {
        List<FollowLink> followLinkList= followLinkService.selectfollowers(uid,flag);
        Integer code = followLinkList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = followLinkList != null?"数据查询成功":"数据查询失败，请重试，请检查是否有信息输入错误";
        return new Result(code,followLinkList,msg);
    }
    /**
     * 读关注信息
     * @param id
     * @return
     */
    @Log(operation = "已读关注消息")
    @GetMapping("/readFollow/{id}")
    public Result readFollow(@PathVariable Integer id) {
        Boolean flag = followLinkService.readFollow(id);
        Integer code = flag !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = flag != null?"已读成功":"操作失败，请重试";
        return new Result(code,"",msg);
    }
    /**
     * 收藏某一个博客
     * @param collect
     * @return
     */
    @Log(operation = "收藏一个博客")
    @PostMapping("/collect")
    public Result collectBlog(@RequestBody Collect collect) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        if(userService.checkCollect(collect.getUId(),collect.getBlogId())){
            return new Result(Code.POST_ERR,collect,"您已经收藏了，不能重复收藏");
        }
        collect.setCollectTime(date);
        Integer num = collectDao.insert(collect);
        Integer code = num !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = num != null?"收藏成功":"收藏失败，请重试";
        return new Result(code,collect,msg);
    }
    /**
     * 取消收藏一个博客
     * @param
     * @return
     */
    @Log(operation = "取消收藏一个博客")
    @DeleteMapping("/falseCollect")
    public Result falseCollect(@RequestBody Collect collect) {
        List<Integer> list =collect.getDeleteid();
        for(Integer collectid:list){
        Integer num = collectDao.deleteById(collectid);}
        return new Result(Code.GET_OK,"","操作成功");
    }
    /**
     * 查询收藏列表
     * @param uid
     * @return
     */
    @Log(operation = "查询收藏列表")
    @GetMapping("/selectCollect/{uid}")
    public Result selectCollect(@PathVariable Integer uid) {
        List<Collect> collectList=userService.selectCollectByuid(uid);
        Integer code = collectList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = collectList != null?"查询成功":"查询失败，请重试";
        return new Result(code,collectList,msg);
    }
    /**
     * 查询博客的点赞消息
     * @param uid
     * @return
     */
    @Log(operation = "查询博客的点赞消息")
    @GetMapping("/selectBlogLike/{uid}")
    public Result selectBlogLike(@PathVariable Integer uid) {
        List<BlogLike> blogLikeList =userService.selectBlogLike(uid);
        Integer code = blogLikeList!=null ? Code.GET_OK :Code.GET_ERR;
        String msg = blogLikeList != null?"查询成功":"查询失败，请重试";
        return new Result(code,blogLikeList,msg);
    }
    /**
     * 查询评论的点赞消息
     * @param uid
     * @return
     */
    @Log(operation = "查询评论点赞消息")
    @GetMapping("/selectCommentLike/{uid}")
    public Result selectCommentLike(@PathVariable Integer uid) {
        List<CommentLike> commentLikeList =userService.selectCommentLike(uid);
        Integer code =commentLikeList!=null ? Code.GET_OK :Code.GET_ERR;
        String msg = commentLikeList != null?"查询成功":"查询失败，请重试";
        return new Result(code,commentLikeList,msg);
    }
    /**
     * 设置用户的状态信息
     * @param uid
     * @param flag 0代表正常用户1代表管理员
     * @return
     */
    @Log(operation = "设置用户状态")
    @GetMapping("/manager/{uid}/{flag}")
    public Result deleteUser(@PathVariable Integer uid,@PathVariable Integer flag)
    {
       Boolean num = userService.updateUserFlag(uid,flag);
       Integer code = num !=false ? Code.GET_OK :Code.GET_ERR;
       String msg = num != false?"设置成功":"设置失败，请重试";
       return new Result(code,"",msg);
    }

    /**
     * 查询关注人的博客信息
     * @param uid
     * @return
     */
    @Log(operation = "查询关注的人的信息")
    @GetMapping("/selectFollowBlog/{uid}")
    public Result selectFollowBlog(@PathVariable Integer uid) {
        List<Blog> blogList = userService.selectFollowBlog(uid);
        Integer code = blogList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = blogList != null?"查询成功":"查询失败，请重试";
        return new Result(code,blogList,msg);
    }



}
