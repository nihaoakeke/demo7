package com.blue.controller;


import com.blue.dao.BlogDao;
import com.blue.dao.BlogLikeDao;
import com.blue.dao.CommentLikeDao;
import com.blue.domain.Blog;
import com.blue.domain.BlogLike;
import com.blue.domain.Comment;
import com.blue.domain.CommentLike;
import com.blue.service.BloagService;
import com.blue.service.CommentService;
import com.blue.utils.KeyUtils;
import com.blue.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@EnableScheduling
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ListenHandler {

    @Autowired
    private BlogDao blogDao;
    @Autowired
    private BlogLikeDao blogLikeDao;
    @Autowired
    private CommentLikeDao commentLikeDao;

    private final BloagService bloagService;

    private final RedisUtil redisUtil;

    private final CommentService commentService;

    private static final String VIEW_KEY = "blogView";
    private static final String LIKE_KEY = "blogLike";
    private static final String CLIKE_KEY = "cLike";

    @Autowired
    public ListenHandler(CommentService commentService,BloagService bloagService,RedisUtil redisUtil){
    this.bloagService = bloagService;
    this.redisUtil =redisUtil;
    this.commentService =commentService;
    }
    @PostConstruct
    public void init() throws Exception {
        log.info("数据初始化开始...");

                List<Blog> blogList = bloagService.list();
        blogList.forEach(blog -> {
                    //将浏览量、点赞数和评论数写入redis
                    redisUtil.zAdd(VIEW_KEY, blog.getBlogId().toString(), blog.getBlogView());
                    redisUtil.zAdd(LIKE_KEY,blog.getBlogId().toString(),blog.getBlogLike());
                });

        List<Comment> commentList = commentService.list();
        commentList.forEach(comment -> {
            redisUtil.zAdd(CLIKE_KEY,comment.getCId().toString(),comment.getCLike());
        });


        log.info("数据已写入redis...");
    }
    /**
     * 关闭时操作
     */
    @PreDestroy
    public void afterDestroy() {
        log.info("开始关闭...");
        Set<ZSetOperations.TypedTuple<String>> blogView = redisUtil.zReverseRangeWithScores("blogView", 0, 1000);
        Set<ZSetOperations.TypedTuple<String>> blogLike = redisUtil.zReverseRangeWithScores("blogLike", 0, 1000);
        Set<ZSetOperations.TypedTuple<String>> cLike = redisUtil.zReverseRangeWithScores("cLike", 0, 1000);
        Set<ZSetOperations.TypedTuple<String>> BlogLike= redisUtil.zReverseRangeWithScores("BlogLike", 0, 1000);
        Set<ZSetOperations.TypedTuple<String>> CommentLike= redisUtil.zReverseRangeWithScores("CommentLike", 0, 1000);
        writeNum(blogView, VIEW_KEY);
        writeNum(blogLike,LIKE_KEY);
        writeNum2(cLike,CLIKE_KEY);
        writeNum3(BlogLike);
        writeNum4(CommentLike);
//        writeNum3(rLikee,RLIKEE_KEY);
        log.info("redis写入数据库完毕");
    }

    @Scheduled(cron = "0/3 * * * * ?")
    public void updateNum() {
        log.info("周期任务开始执行...");
//        Set<ZSetOperations.TypedTuple<String>> viewNum = redisUtil.zReverseRangeWithScores("viewNum", 0, 10);
//        writeNum(viewNum, VIEW_KEY);
        Set<ZSetOperations.TypedTuple<String>> blogView = redisUtil.zReverseRangeWithScores("blogView", 0, 10);
        writeNum(blogView, VIEW_KEY);
        Set<ZSetOperations.TypedTuple<String>> blogLike = redisUtil.zReverseRangeWithScores("blogLike", 0, 10);
        writeNum(blogLike,LIKE_KEY);
        Set<ZSetOperations.TypedTuple<String>> cLike = redisUtil.zReverseRangeWithScores("cLike", 0, 10);
        writeNum2(cLike,CLIKE_KEY);
        Set<ZSetOperations.TypedTuple<String>> BlogLike= redisUtil.zReverseRangeWithScores("BlogLike", 0, 1000);
        Set<ZSetOperations.TypedTuple<String>> CommentLike= redisUtil.zReverseRangeWithScores("CommentLike", 0, 1000);
        writeNum3(BlogLike);
        writeNum4(CommentLike);
        log.info("周期任务执行完毕,redis写入数据库完毕");
    }



    @Scheduled(cron = "0/3 * * * * ?")
    public void updateNum3(){
        List<Blog> blogList =bloagService.seleceUnPublish();
        for(Blog blog :blogList){
            Date publishdate = blog.getBlogTime();
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            if(date.compareTo(publishdate)>=0){
                blog.setBlogPublish(1);
            }
            blogDao.updateById(blog);

        }
    }






    private void writeNum(Set<ZSetOperations.TypedTuple<String>> set, String fieldName) {
        set.forEach(item -> {
            Long id = Long.valueOf(item.getValue());
            Integer num = item.getScore().intValue();
            Blog blog = bloagService.getById(id);
            switch (fieldName) {
                case VIEW_KEY:
                    blog.setBlogView(num);
                    break;
                case LIKE_KEY:
                    blog.setBlogLike(num);
                    break;
                default:
                    return;
            }
            bloagService.updateNumById(blog);
            log.info("{} 更新完毕", fieldName);
        });
    }


    private void writeNum2(Set<ZSetOperations.TypedTuple<String>> set, String fieldName) {
        set.forEach(item -> {
            Long id = Long.valueOf(item.getValue());
            Integer num = item.getScore().intValue();
            Comment comment = commentService.getById(id);
            switch (fieldName) {
                case CLIKE_KEY:
                    comment.setCLike(num);
                    break;
                default:
                    return;
            }
            commentService.updateNumById(comment);
            log.info("{} 更新完毕", fieldName);
        });
    }
    private void writeNum3(Set<ZSetOperations.TypedTuple<String>> set) {
        set.forEach(item -> {
            String[] ans = KeyUtils.getKets(item.getValue());
            Integer authorid = Integer.valueOf(ans[0]);
            Integer postid = Integer.valueOf(ans[1]);
            Integer bid = Integer.valueOf(ans[2]);
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            BlogLike blogLike = new BlogLike();
            blogLike.setLikeTime(date);
            blogLike.setBlogId(bid);
            blogLike.setAuthorId(authorid);
            blogLike.setPostId(postid);
            blogLikeDao.insert(blogLike);
            redisUtil.zRemove("BlogLike",KeyUtils.gengerateKey(authorid.toString(),postid.toString(),bid.toString()));
            log.info("{} 更新完毕");
        });
    }

    private void writeNum4(Set<ZSetOperations.TypedTuple<String>> set) {
        set.forEach(item -> {
            String[] ans = KeyUtils.getKets(item.getValue());
            Integer authorid = Integer.valueOf(ans[0]);
            Integer postid = Integer.valueOf(ans[1]);
            Integer cid = Integer.valueOf(ans[2]);
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            CommentLike commentLike = new CommentLike();
            commentLike.setLikeTime(date);
            commentLike.setCommentId(cid);
            commentLike.setAuthorId(authorid);
            commentLike.setPostId(postid);
            commentLikeDao.insert(commentLike);
            redisUtil.zRemove("CommentLike",KeyUtils.gengerateKey(authorid.toString(),postid.toString(),cid.toString()));
            log.info("{} 更新完毕");
        });
    }



}
