package com.blue.controller;


import com.blue.domain.Article;
import com.blue.domain.Blog;
import com.blue.domain.Comment;
import com.blue.domain.Reply;
import com.blue.service.ArticleService;
import com.blue.service.BloagService;
import com.blue.service.CommentService;
import com.blue.service.ReplyService;
import com.blue.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@EnableScheduling
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ListenHandler {


//    private final ArticleService articleService;
//    private final RedisUtil redisUtil;

    private final BloagService bloagService;

    private final ReplyService replyService;

    private final RedisUtil redisUtil;

    private final CommentService commentService;

//    private static final String VIEW_KEY = "viewNum";
    private static final String VIEW_KEY = "blogView";
//    private static final String COMMENT_KEY = "commentNum";
    private static final String LIKE_KEY = "blogLike";

    private static final String CLIKE_KEY = "cLike";

    private static final String RLIKEE_KEY = "rLikee";



//    @Autowired
//    public ListenHandler(ArticleService articleService, RedisUtil redisUtil) {
//        this.articleService = articleService;
//        this.redisUtil = redisUtil;
//    }

    @Autowired
    public ListenHandler(ReplyService replyService,CommentService commentService,BloagService bloagService,RedisUtil redisUtil){
    this.bloagService = bloagService;
    this.redisUtil =redisUtil;
    this.commentService =commentService;
    this.replyService =replyService;
    }



    @PostConstruct
    public void init() throws Exception {
        log.info("数据初始化开始...");
//        //将数据库中的数据写入redis
//        List<Article> articleLst = articleService.list();
//        articleLst.forEach(article -> {
//            //将浏览量、点赞数和评论数写入redis
//            redisUtil.zAdd(VIEW_KEY, article.getId().toString(), article.getViewNum());
//            redisUtil.zAdd(COMMENT_KEY, article.getId().toString(), article.getCommentNum());
//            redisUtil.zAdd(LIKE_KEY, article.getId().toString(), article.getLikeNum());
//        });

                List<Blog> blogList = bloagService.list();
        blogList.forEach(blog -> {
                    //将浏览量、点赞数和评论数写入redis
                    redisUtil.zAdd(VIEW_KEY, blog.getBlogId().toString(), blog.getBlogView());
                    redisUtil.zAdd(LIKE_KEY,blog.getBlogId().toString(),blog.getBlogLike());
//            redisUtil.zAdd(COMMENT_KEY, article.getId().toString(), article.getCommentNum());
//            redisUtil.zAdd(LIKE_KEY, article.getId().toString(), article.getLikeNum());
                });

        List<Comment> commentList = commentService.list();
        commentList.forEach(comment -> {
            redisUtil.zAdd(CLIKE_KEY,comment.getCId().toString(),comment.getCLike());
            //将浏览量、点赞数和评论数写入redis
//            redisUtil.zAdd(VIEW_KEY, blog.getBlogId().toString(), blog.getBlogView());
//            redisUtil.zAdd(LIKE_KEY,blog.getBlogId().toString(),blog.getBlogLike());
//            redisUtil.zAdd(COMMENT_KEY, article.getId().toString(), article.getCommentNum());
//            redisUtil.zAdd(LIKE_KEY, article.getId().toString(), article.getLikeNum());
        });

        List<Reply>  replyList= replyService.list();
        replyList.forEach(reply -> {
            redisUtil.zAdd(RLIKEE_KEY,reply.getRId().toString(),reply.getRLikee());
            //将浏览量、点赞数和评论数写入redis
//            redisUtil.zAdd(VIEW_KEY, blog.getBlogId().toString(), blog.getBlogView());
//            redisUtil.zAdd(LIKE_KEY,blog.getBlogId().toString(),blog.getBlogLike());
//            redisUtil.zAdd(COMMENT_KEY, article.getId().toString(), article.getCommentNum());
//            redisUtil.zAdd(LIKE_KEY, article.getId().toString(), article.getLikeNum());
        });

        log.info("数据已写入redis...");

//        log.info("开始关闭...");
//        //将redis中的数据写入数据库
//        Set<ZSetOperations.TypedTuple<String>> viewNum = redisUtil.zReverseRangeWithScores("viewNum", 0, 10);
//        Set<ZSetOperations.TypedTuple<String>> commentNum = redisUtil.zReverseRangeWithScores("commentNum", 0, 10);
//        Set<ZSetOperations.TypedTuple<String>> likeNum = redisUtil.zReverseRangeWithScores("likeNum", 0, 10);
//
//        writeNum(viewNum, VIEW_KEY);
//        writeNum(commentNum, COMMENT_KEY);
//        writeNum(likeNum, LIKE_KEY);
//        log.info("redis写入数据库完毕");
    }

    /**
     * 关闭时操作
     */
    @PreDestroy
    public void afterDestroy() {
        log.info("开始关闭...");
        //将redis中的数据写入数据库
//        Set<ZSetOperations.TypedTuple<String>> viewNum = redisUtil.zReverseRangeWithScores("viewNum", 0, 10);
//        Set<ZSetOperations.TypedTuple<String>> commentNum = redisUtil.zReverseRangeWithScores("commentNum", 0, 10);
//        Set<ZSetOperations.TypedTuple<String>> likeNum = redisUtil.zReverseRangeWithScores("likeNum", 0, 10);


//        writeNum(viewNum, VIEW_KEY);
//        writeNum(commentNum, COMMENT_KEY);
//        writeNum(likeNum, LIKE_KEY);
        Set<ZSetOperations.TypedTuple<String>> blogView = redisUtil.zReverseRangeWithScores("blogView", 0, 10);
        Set<ZSetOperations.TypedTuple<String>> blogLike = redisUtil.zReverseRangeWithScores("blogLike", 0, 10);
        Set<ZSetOperations.TypedTuple<String>> cLike = redisUtil.zReverseRangeWithScores("cLike", 0, 10);
        Set<ZSetOperations.TypedTuple<String>> rLikee = redisUtil.zReverseRangeWithScores("rLikee", 0, 10);
        writeNum(blogView, VIEW_KEY);
        writeNum(blogLike,LIKE_KEY);
        writeNum2(cLike,CLIKE_KEY);
        writeNum3(rLikee,RLIKEE_KEY);
        log.info("redis写入数据库完毕");
    }

//    @Scheduled(cron = "0/3 * * * * ?")
//    public void updateNum() {
//        log.info("周期任务开始执行...");
////        Set<ZSetOperations.TypedTuple<String>> viewNum = redisUtil.zReverseRangeWithScores("viewNum", 0, 10);
////        writeNum(viewNum, VIEW_KEY);
//        Set<ZSetOperations.TypedTuple<String>> blogView = redisUtil.zReverseRangeWithScores("blogView", 0, 10);
//        writeNum(blogView, VIEW_KEY);
//        Set<ZSetOperations.TypedTuple<String>> blogLike = redisUtil.zReverseRangeWithScores("blogLike", 0, 10);
//        writeNum(blogLike,LIKE_KEY);
//        Set<ZSetOperations.TypedTuple<String>> cLike = redisUtil.zReverseRangeWithScores("cLike", 0, 10);
//        writeNum2(cLike,CLIKE_KEY);
//        Set<ZSetOperations.TypedTuple<String>> rLikee = redisUtil.zReverseRangeWithScores("rLikee", 0, 10);
//        writeNum3(rLikee,RLIKEE_KEY);
//        log.info("周期任务执行完毕,redis写入数据库完毕");
//    }

    private void writeNum(Set<ZSetOperations.TypedTuple<String>> set, String fieldName) {
        set.forEach(item -> {
            Long id = Long.valueOf(item.getValue());
            Integer num = item.getScore().intValue();

//            Article article = articleService.getById(id);
            Blog blog = bloagService.getById(id);
            switch (fieldName) {
                case VIEW_KEY:
                    blog.setBlogView(num);
                    break;
//                case COMMENT_KEY:
//                    article.setCommentNum(num);
//                    break;
                case LIKE_KEY:
                    blog.setBlogLike(num);
                    break;
                default:
                    return;
            }

            //更新数据库
//            articleService.updateNumById(article);
            bloagService.updateNumById(blog);
            log.info("{} 更新完毕", fieldName);
        });
    }

    private void writeNum2(Set<ZSetOperations.TypedTuple<String>> set, String fieldName) {
        set.forEach(item -> {
            Long id = Long.valueOf(item.getValue());
            Integer num = item.getScore().intValue();

//            Article article = articleService.getById(id);
            Comment comment = commentService.getById(id);
            switch (fieldName) {
                case CLIKE_KEY:
                    comment.setCLike(num);
                    break;
                default:
                    return;
            }

            //更新数据库
//            articleService.updateNumById(article);
            commentService.updateNumById(comment);
            log.info("{} 更新完毕", fieldName);
        });
    }

    private void writeNum3(Set<ZSetOperations.TypedTuple<String>> set, String fieldName) {
        set.forEach(item -> {
            Long id = Long.valueOf(item.getValue());
            Integer num = item.getScore().intValue();

//            Article article = articleService.getById(id);
            Reply reply = replyService.getById(id);
            switch (fieldName) {
                case RLIKEE_KEY:
                    reply.setRLikee(num);
                    break;
                default:
                    return;
            }

            //更新数据库
//            articleService.updateNumById(article);
            replyService.updateNumById(reply);
            log.info("{} 更新完毕", fieldName);
        });
    }
}
