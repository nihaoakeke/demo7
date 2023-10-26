package com.blue.controller;


import com.blue.config.Log;
import com.blue.dao.CommentDao;
import com.blue.domain.Code;
import com.blue.domain.Comment;
import com.blue.domain.News;
import com.blue.domain.Result;
import com.blue.service.CommentService;
import com.blue.utils.SenstiveUtils;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentDao commentDao;
    @Autowired
    private CommentService commentService;


    @Log(operation = "确认点赞某一个评论")
    @GetMapping("/okLike/{id}")
    public Result OKLikeById(@PathVariable Integer id)
    {
        Comment comment = commentService.getById(id);
        //RedisUtil.zIncrementScore("viewNum",id.toString(),1);
        if (comment == null) {
            return new Result(Code.GET_ERR,comment,"点赞失败，请重试");
        }
        return new Result(Code.GET_OK,comment,"点赞成功");

    }

    @Log(operation = "取消点赞某一个评论")
    @GetMapping("/flaseLike/{id}")
    public Result falseLikeById(@PathVariable Integer id)
    {
        Comment comment = commentService.getById(id);
        //RedisUtil.zIncrementScore("viewNum",id.toString(),1);
        if (comment == null) {
            return new Result(Code.GET_ERR,comment,"取消点赞失败，请重试");
        }
        return new Result(Code.GET_OK,comment,"取消点赞成功");
    }

    @Log(operation = "根据发表时间和用户进行查询")
    @GetMapping("/time/uid/{uid}")
    public Result getByuTId(@PathVariable Integer uid) {
        List<Comment> commentList= commentService.selectCommentByTimeUDesc(uid);
        Integer code = commentList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = commentList != null ? "数据查询成功" : "数据查询失败，请重试！";
        Iterator<Comment> iterator = commentList.iterator();
        while (iterator.hasNext()){ //判断迭代器是否有元素
            Comment comment = iterator.next();//获取集合下一个元素
            String name = comment.getCContent();
            comment.setCContent(EmojiParser.parseToUnicode(name));
        }
        return new Result(code,commentList,msg);
    }

    @Log(operation = "根据点赞量和用户进行排序查询")
    @GetMapping("/like/uid/{uid}")
    public Result getByuLId(@PathVariable Integer uid) {
        List<Comment> commentList= commentService.selectCommentByLikeUDesc(uid);
        Integer code = commentList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = commentList != null ? "数据查询成功" : "数据查询失败，请重试！";
        Iterator<Comment> iterator = commentList.iterator();
        while (iterator.hasNext()){ //判断迭代器是否有元素
            Comment comment = iterator.next();//获取集合下一个元素
            String name = comment.getCContent();
            comment.setCContent(EmojiParser.parseToUnicode(name));
        }
        return new Result(code,commentList,msg);
    }


    @Log(operation = "根据用户id进行查询评论")
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        Comment comment = commentDao.selectById(id);
        Integer code = comment != null ? Code.GET_OK : Code.GET_ERR;
        String msg = comment != null ? "数据查询成功" : "数据查询失败，请重试！";
        String name = comment.getCContent();
        comment.setCContent(EmojiParser.parseToUnicode(name));
        return new Result(code,comment,msg);
    }

    @Log(operation ="发表评论")
    @PostMapping
    public Result saveComment(@RequestBody Comment comment){

        if(comment.getCContent()!=null){
            String nameCovert = EmojiParser.parseToAliases(comment.getCContent());
            comment.setCContent(nameCovert);
        }

        String message = comment.getCContent();
        SenstiveUtils.init();
        comment.setCContent(SenstiveUtils.replaceSensitiveWord(message));

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        comment.setCTime(date);
        Integer num = commentDao.insert(comment);
        Integer code = num!=null?Code.SAVE_OK:Code.SAVE_ERR;
        String msg=num!=null?"保存成功":"保存失败，请重试";
        return new Result(code,comment,msg);
    }

    @Log(operation = "删除某一条评论")
    @DeleteMapping("/{id}")
    public Result deleteComment(@PathVariable Integer id)
    {
        Integer num =commentDao.deleteById(id);
        Integer code = num!=null?Code.DELETE_OK:Code.DELETE_ERR;
        String msg=num!=null?"删除成功":"删除失败，请重试";
        return new Result(code,"",msg);
    }

    @Log(operation = "更新评论信息")
    @PutMapping
    public Result updateComment(@RequestBody Comment comment)
    {
        if(comment.getCContent()!=null){
            String nameCovert = EmojiParser.parseToAliases(comment.getCContent());
            comment.setCContent(nameCovert);
        }
        String message = comment.getCContent();
        SenstiveUtils.init();
        comment.setCContent(SenstiveUtils.replaceSensitiveWord(message));
        Integer num = commentDao.updateById(comment);
        Integer code = num !=null ? Code.UPDATE_OK :Code.UPDATE_ERR;
        String msg = num != null?"数据更新成功":"数据更新失败，请重试";
        return new Result(code,comment,msg);
    }

    @Log(operation = "根据时间和博客id来查询评论")
    @GetMapping("/time/bid/{bid}")
    public Result selectBytime(@PathVariable Integer bid)
    {
        List<Comment> commentList = commentService.selectCommentByTimeDesc(bid);
        Integer code = commentList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = commentList != null?"数据查询成功":"数据查询失败，请重试";
        Iterator<Comment> iterator = commentList.iterator();
        while (iterator.hasNext()){ //判断迭代器是否有元素
            Comment comment = iterator.next();//获取集合下一个元素
            String name = comment.getCContent();
            comment.setCContent(EmojiParser.parseToUnicode(name));
        }
        return new Result(code,commentList,msg);
    }

    @Log(operation = "根据点赞量和博客来排序查询")
    @GetMapping("/like/bid/{bid}")
    public Result selectCommentByLikeDesc(@PathVariable Integer bid)
    {
        List<Comment> commentList = commentService.selectCommentByLikeDesc(bid);
        Integer code = commentList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = commentList != null?"数据查询成功":"数据查询失败，请重试";

        Iterator<Comment> iterator = commentList.iterator();
        while (iterator.hasNext()){ //判断迭代器是否有元素
            Comment comment = iterator.next();//获取集合下一个元素
            String name = comment.getCContent();
            comment.setCContent(EmojiParser.parseToUnicode(name));
        }
        return new Result(code,commentList,msg);
    }

}
