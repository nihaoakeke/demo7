package com.blue.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.dao.LinkDao;
import com.blue.dao.UserDao;
import com.blue.domain.Link;
import com.blue.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class LinkService extends ServiceImpl<LinkDao, Link> {



    @Autowired
    private LinkDao linkDao;

    @Autowired
    private UserDao userDao;


    /**
     * 查询关系
     * @param fromuser
     * @param touser
     * @return
     */

    public Link selectByFT(Integer fromuser,Integer touser)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("fromUser",fromuser);
        qw.eq("toUser",touser);
        Link link = linkDao.selectOne(qw);
        return link;
    }


    /**
     * 接收方已读申请
     * @param fromuser
     * @param touser
     * @return
     */

    public Integer updateflag(Integer fromuser,Integer touser)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("fromUser",fromuser);
        qw.eq("toUser",touser);
        qw.ne("message","0");
        Link link = linkDao.selectOne(qw);
        link.setFlag(1);
        Integer num = linkDao.updateById(link);
        return num;
    }

    /**
     * 接收方查看收到的好友请求
     * @param touser
     * @return
     */

    public List<Link> selectMessageBytouser(Integer touser)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("toUser",touser);
        qw.orderByDesc("time");
        qw.ne("message","0");
        List<Link> linkList = linkDao.selectList(qw);
        return linkList;
    }

    /**
     * 接收方查看没读的好友申请
     * @param touser
     * @return
     */
    public List<Link>selectMessageByflag(Integer touser)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("toUser",touser);
        qw.orderByDesc("time");
        qw.eq("flag",0);
        qw.ne("message","0");
        List<Link> linkList = linkDao.selectList(qw);
        return linkList;
    }

    /**
     * 发送方查看所有的发送消息
     * @param fromuser
     * @return
     */

    public List<Link> selectMessageByfromuser(Integer fromuser)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("fromUser",fromuser);
        qw.orderByDesc("time");
        qw.ne("message","0");
        List<Link> linkList = linkDao.selectList(qw);
        return linkList;
    }

    /**
     * 发送方查看对方没读的
     * @param fromuser
     * @return
     */
    public List<Link> selectMessageByFFlag(Integer fromuser)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("fromUser",fromuser);
        qw.orderByDesc("time");
        qw.eq("flag",0);
        qw.ne("message","0");
        List<Link> linkList = linkDao.selectList(qw);
        return linkList;
    }

    /**
     * 发送方查询好友列表
     * @param fromuser
     * @return
     */

    public List<Link> selectFriend(Integer fromuser)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("fromUser",fromuser);
        qw.eq("unfriend",1);
        List<Link> linkList = linkDao.selectList(qw);
        return linkList;
    }


    public Boolean okFlag(Integer fromuser,Integer touser)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("fromUser",fromuser);
        qw.eq("toUser",touser);
        qw.ne("message","0");
        Link link =linkDao.selectOne(qw);
        link.setFlag(1);
        linkDao.updateById(link);
        return true;

    }
    /**
     * 通过好友申请
     * @param fromuser
     * @param touser
     * @return
     */
    public Boolean passApply(Integer fromuser,Integer touser)
    {

        QueryWrapper qw = new QueryWrapper();
        qw.eq("fromUser",fromuser);
        qw.eq("toUser",touser);
        Link link = linkDao.selectOne(qw);
        link.setUnfriend(1);
        linkDao.updateById(link);
        return true;
    }



    /**
     * 查询用户好友申请未通过的
     * @param fromuser
     * @return
     */
    public List<Link> selectUnpass(Integer fromuser)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("fromUser",fromuser);
        qw.eq("unfriend",0);
        List<Link> linkList = linkDao.selectList(qw);
        return linkList;
    }

    /**
     * 查看自己关注的人
     * @param fromuser
     * @return
     */
    public List<Link> selectFromuserFollower(Integer fromuser)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("fromUser",fromuser);
        qw.eq("follow",1);
        List<Link> linkList = linkDao.selectList(qw);
        return linkList;
    }

    /**
     * 查看自己被谁关注
     * @param touser
     * @return
     */
    public List<Link> selectTouserFollower(Integer touser)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("toUser",touser);
        qw.eq("follow",1);
        List<Link> linkList = linkDao.selectList(qw);
        return linkList;
    }

    /**
     * 确认关注对方
     * @param fromuser
     * @param touser
     * @return
     */
    public Boolean okFollwe(Integer fromuser,Integer touser)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("fromUser",fromuser);
        qw.eq("toUser",touser);
        Link link = linkDao.selectOne(qw);
        if(link==null)
        {
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            link.setFromUser(fromuser);
            link.setToUser(touser);
            link.setTime(date);
            link.setFollow(1);
            linkDao.insert(link);
            User user =  userDao.selectById(touser);
            Integer num = user.getUFollowers();
            num++;
            user.setUFollowers(num);
            userDao.updateById(user);
        }
        else{
            link.setFollow(1);
            User user =  userDao.selectById(touser);
            Integer num = user.getUFollowers();
            num++;
            user.setUFollowers(num);
            userDao.updateById(user);
            linkDao.updateById(link);
        }

        return true;


    }


}
