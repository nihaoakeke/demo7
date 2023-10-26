package com.blue.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.dao.UserDao;
import com.blue.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserService extends ServiceImpl<UserDao, User> {

    @Autowired UserDao userDao;


    /**
     *
     * @param email
     * @return
     */

    public User selectByeamil(String email)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("uEmail",email);
        User user = userDao.selectOne(qw);
        return user;
    }

    /**
     * 根据用户名称来查询用户名称
     * @param name
     * @return
     */

    public User selectByname(String name)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("uName",name);
        User user = userDao.selectOne(qw);
        return user;
    }

    /**
     * 根据用户的笔名查询用户
     * @param nickname
     * @return
     */

   public User selectBynickname(String nickname)
   {
       QueryWrapper qw = new QueryWrapper();
       qw.eq("uNickname",nickname);
       User user = userDao.selectOne(qw);
       return user;
   }

    /**
     * 根据用户笔名来模糊查询
     * @param nickname
     * @return
     */
   public List<User> selectLikenickname(String nickname)
   {
       QueryWrapper qw = new QueryWrapper();
       qw.like("uNickname",nickname);
       List<User> userList = userDao.selectList(qw);
       return userList;
   }

    /**
     * 根据用户名来模糊查询
     * @param name
     * @return
     */

    public List<User> selectLikename(String name)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.like("uName",name);
        List<User> userList = userDao.selectList(qw);
        return userList;
    }










}
