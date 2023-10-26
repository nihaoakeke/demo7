package com.blue.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.dao.CommentDao;
import com.blue.dao.NewsDao;
import com.blue.domain.Comment;
import com.blue.domain.News;
import com.blue.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService extends ServiceImpl<NewsDao, News> {

    @Autowired
    private NewsDao newsDao;

    /**
     * 根据某个用户
     * @param uid
     * @return
     */
    public List<News> selectByUid(Integer uid)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("uId",uid);
        qw.orderByDesc("nTime");
        List<News> newsList = newsDao.selectList(qw);
        return newsList;
    }

    /**
     * 根据时间全部排序
     * @return
     */
    public List<News> selectAllByTime()
    {
        QueryWrapper qw = new QueryWrapper();
        qw.orderByDesc("nTime");
        List<News> newsList = newsDao.selectList(qw);
        return newsList;
    }

}
