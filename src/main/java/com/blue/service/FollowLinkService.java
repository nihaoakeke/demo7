package com.blue.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.dao.FollowLinkDao;
import com.blue.dao.UserMapper;
import com.blue.domain.FollowLink;
import com.blue.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FollowLinkService extends ServiceImpl<FollowLinkDao, FollowLink> {

    @Autowired
    private FollowLinkDao followLinkDao;

    @Autowired
    private UserMapper userMapper;


    public Boolean okFollow(Integer fromuser, Integer touser, Date date)
    {
        FollowLink followLink = new FollowLink();
        followLink.setFromUser(fromuser);
        followLink.setToUser(touser);
        followLink.setFollowTime(date);
        followLinkDao.insert(followLink);
        User user = userMapper.selectById(touser);
        System.out.println(user);
        Integer num = user.getUFollowers();
        num++;
        user.setUFollowers(num);
        Integer number = userMapper.updateById(user);
        return number!=0?true:false;
    }


    public Boolean falseFollow(Integer fromuser,Integer touser)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("from_user",fromuser);
        qw.eq("to_user",touser);
        followLinkDao.delete(qw);
        User user = userMapper.selectById(touser);
        Integer num = user.getUFollowers();
        num--;
        user.setUFollowers(num);
        Integer number = userMapper.updateById(user);
        return number!=0?true:false;
    }

    public List<FollowLink> selectfollowers(Integer uid,Integer flag)
    {
        QueryWrapper qw = new QueryWrapper();
        if(flag==0) {
            qw.eq("to_user", uid);
        }
        else{
            qw.eq("from_user",uid);
        }
        List<FollowLink> followLinkList =followLinkDao.selectList(qw);
        return followLinkList;
    }


    public Boolean readFollow(Integer id)
    {
        FollowLink followLink = followLinkDao.selectById(id);
        followLink.setFollowFlag(1);
        Integer num =followLinkDao.updateById(followLink);
        return num!=0?true:false;
    }




}
