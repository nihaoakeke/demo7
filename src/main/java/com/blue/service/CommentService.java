package com.blue.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.dao.CommentDao;
import com.blue.domain.Blog;
import com.blue.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService extends ServiceImpl<CommentDao, Comment> {

    @Autowired
    private CommentDao commentDao;



    public Boolean updateNumById(Comment comment) {
//        return baseMapper.updateNumById(article) > 0;
        return baseMapper.updateById(comment)>0;
    }

    /**
     * 根据博客的id查询
     * @param blogId
     * @return
     */
    public List<Comment> selectBybId(Integer blogId)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("blogId",blogId);
        List<Comment> list = commentDao.selectList(qw);
        return list;
    }

    /**
     *
     * @param uId
     * @return
     */

    public List<Comment> selectByuId(Integer uId)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("uId",uId);
        List<Comment> list = commentDao.selectList(qw);
        return list;
    }


    /**
     *根据点赞量对评论排序
     * @param blogId
     * @return
     */
    public List<Comment> selectCommentByLikeDesc(Integer blogId)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("blogId",blogId);
        qw.orderByDesc("cLike");
        List<Comment> commentList = commentDao.selectList(qw);
        return commentList;
    }

    /**
     * 根据用户的点赞量排序
     * @param uId
     * @return
     */

    public List<Comment> selectCommentByLikeUDesc(Integer uId)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("uId",uId);
        qw.orderByDesc("cLike");
        List<Comment> commentList = commentDao.selectList(qw);
        return commentList;
    }





    /**
     * 根据发表时间对评论排序
     * @param blogId
     * @return
     */
    public List<Comment> selectCommentByTimeDesc(Integer blogId)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("blogId",blogId);
        qw.orderByDesc("cTime");
        List<Comment> commentList = commentDao.selectList(qw);
        return commentList;

    }


    /**
     * 根据发表时间和用户对评论排序
     * @param uId
     * @return
     */
    public List<Comment> selectCommentByTimeUDesc(Integer uId)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("uId",uId);
        qw.orderByDesc("cTime");
        List<Comment> commentList = commentDao.selectList(qw);
        return commentList;

    }







}
