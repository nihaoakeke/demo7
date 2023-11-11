package com.blue.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.dao.CommentDao;
import com.blue.dao.CommentLikeDao;
import com.blue.domain.Blog;
import com.blue.domain.Comment;
import com.blue.domain.CommentLike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CommentService extends ServiceImpl<CommentDao, Comment> {

    @Autowired
    private CommentDao commentDao;


    @Autowired
    private CommentLikeDao commentLikeDao;



    public Boolean updateNumById(Comment comment) {
//        return baseMapper.updateNumById(article) > 0;
        return baseMapper.updateById(comment)>0;
    }

    /**
     * 根据博客的id查询一级评论
     * @param blogId
     * @return
     */
    public List<Comment> selectOneBybId(Integer blogId)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("blog_id",blogId);
        qw.eq("parent_id",0);
        List<Comment> list = commentDao.selectList(qw);
//        list.sort(Comparator.comparing(Comment::getCtime));
        list.sort(Comparator.comparing(Comment::getCtime, Collections.reverseOrder()));
        return list;
    }

    /**
     * 查看二级评论以及更多的评论
     * @param commentId
     * @return
     */

    public List<Comment> selectTwo(Integer commentId)
    {
        if(commentId==0) {
            return null;
        }
        QueryWrapper qw = new QueryWrapper();
        qw.eq("parent_id",commentId);
        List<Comment> commentList = commentDao.selectList(qw);
        List<Comment> tempCommentList = new ArrayList<>();
        for(Comment comment:commentList){
            List<Comment> commentList1 = selectTwo(comment.getCId());
            tempCommentList.addAll(commentList1);
        }
        commentList.addAll(tempCommentList);
        return commentList;
    }


    public Boolean deleteComment(Integer cid)
    {
        List<Comment> commentList= selectTwo(cid);
        for(Comment comment : commentList){
            commentDao.deleteById(comment.getCId());
        }
        return true;
    }


    public List<Comment> selectReply(Integer uid)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("parent_user_id",uid);
        List<Comment> commentList = commentDao.selectList(qw);
        return commentList;
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



    public CommentLike checkLike(Integer authorid,Integer postid,Integer commentid)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("author_id",authorid);
        qw.eq("comment_id",commentid);
        qw.eq("like_post_id",postid);
        return commentLikeDao.selectOne(qw);
    }

    /**
     *
     * @param uid
     * @param bid
     * @return
     */
    public Comment checkCommentExist(Integer uid,Integer bid)
    {
        QueryWrapper qw = new QueryWrapper<>();
        qw.eq("comment_id",bid);
        qw.eq("user_id",uid);
        Comment comment = commentDao.selectOne(qw);
        return comment;
    }

}
