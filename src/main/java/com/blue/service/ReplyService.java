package com.blue.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.dao.NewsDao;
import com.blue.dao.ReplyDao;
import com.blue.domain.Blog;
import com.blue.domain.News;
import com.blue.domain.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReplyService extends ServiceImpl<ReplyDao, Reply> {

    @Autowired
    private ReplyDao replyDao;
    /**
     * 根据某个用户来查询回复，按照时间排序
     * @param uid
     * @return
     */


    public Boolean updateNumById(Reply reply) {
//        return baseMapper.updateNumById(article) > 0;
        return baseMapper.updateById(reply)>0;
    }
    public List<Reply> selectByTimeUid(Integer uid)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("uId",uid);
        qw.orderByDesc("rTime");
        List<Reply> newsList = replyDao.selectList(qw);
        return newsList;
    }

    /**
     * 根据某个用户，按照点赞量排序
     * @param uid
     * @return
     */
    public List<Reply> selectByLikeUid(Integer uid)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("uId",uid);
        qw.orderByDesc("rLikee");
        List<Reply> replyList = replyDao.selectList(qw);
        return replyList;
    }



    /**
     * 根据某个评论查看回复的评论，按照时间进行排序
     * @param cid
     * @return
     */
    public List<Reply> selectByTimeCid(Integer cid)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("cId",cid);
        qw.orderByDesc("rTime");
        List<Reply> replyList = replyDao.selectList(qw);
        return replyList;
    }

    /**
     * 根据某个评论查看回复的评论,按照点赞量进行排序
     * @param cid
     * @return
     */
    public List<Reply> selectByLikeCid(Integer cid)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("cId",cid);
        qw.orderByDesc("rLikee");
        List<Reply> replyList = replyDao.selectList(qw);
        return replyList;
    }




}
