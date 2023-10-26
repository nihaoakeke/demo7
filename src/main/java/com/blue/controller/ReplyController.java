package com.blue.controller;


import com.blue.config.Log;
import com.blue.dao.ReplyDao;
import com.blue.domain.Code;
import com.blue.domain.Reply;
import com.blue.domain.Result;
import com.blue.service.ReplyService;
import com.blue.utils.SenstiveUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/replys")
public class ReplyController {
    @Autowired
    private ReplyDao replyDao;
    @Autowired
    private ReplyService replyService;

    @Log(operation = "回复评论点赞")
    @GetMapping("/okLike/{id}")
    public Result OKLikeById(@PathVariable Integer id)
    {
        Reply reply =replyService.getById(id);
        if (reply == null) {
            return new Result(Code.GET_ERR,reply,"点赞失败，请重试");
        }
        return new Result(Code.GET_OK,reply,"点赞成功");
    }

    @Log(operation = "取消回复评论的点赞")
    @GetMapping("/flaseLike/{id}")
    public Result falseLikeById(@PathVariable Integer id)
    {
        Reply reply =replyService.getById(id);
        if (reply == null) {
            return new Result(Code.GET_OK,reply,"取消点赞失败，请从事");
        }
        return new Result(Code.GET_ERR,reply,"取消点赞成功");
    }

    @Log(operation = "按照时间排序回复评论")
    @GetMapping("/time/uid/{uid}")
    public Result getByuTId(@PathVariable Integer uid) {
        List<Reply> replyList= replyService.selectByTimeUid(uid);
        Integer code =  replyList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = replyList != null ? "数据查询成功" : "数据查询失败，请重试！";
        return new Result(code,replyList,msg);
    }

    @Log(operation = "按照点赞量排序回复评论")
    @GetMapping("/like/uid/{uid}")
    public Result getByuLId(@PathVariable Integer uid) {
        List<Reply> replyList = replyService.selectByLikeUid(uid);
        Integer code = replyList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = replyList != null ? "数据查询成功" : "数据查询失败，请重试！";
        return new Result(code,replyList,msg);
    }

    @Log(operation = "按照点赞量和评论排序回复评论")
    @GetMapping("/like/cid/{cid}")
    public Result getBycLId(@PathVariable Integer cid) {
        List<Reply> replyList = replyService.selectByLikeCid(cid);
        Integer code = replyList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = replyList != null ? "数据查询成功" : "数据查询失败，请重试！";
        return new Result(code,replyList,msg);
    }


    @Log(operation = "按照时间和评论排序回复评论")
    @GetMapping("/time/cid/{cid}")
    public Result getBycTId(@PathVariable Integer cid) {
        List<Reply> replyList = replyService.selectByTimeCid(cid);
        Integer code = replyList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = replyList != null ? "数据查询成功" : "数据查询失败，请重试！";
        return new Result(code,replyList,msg);
    }


    @Log(operation = "发表回复评论")
    @PostMapping
    public Result saveReply(@RequestBody Reply reply){

        String mes = reply.getRContent();
        SenstiveUtils.init();
        reply.setRContent(SenstiveUtils.replaceSensitiveWord(mes));

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        reply.setRTime(date);
        Integer num = replyDao.insert(reply);
        Integer code = num!=null?Code.SAVE_OK:Code.SAVE_ERR;
        String msg = num!=null?"保存成功":"保存失败";
        return new Result(code,reply,msg);
    }

    @Log(operation = "删除回复评论")
    @DeleteMapping("/{id}")
    public Result deleteReply(@PathVariable Integer id)
    {
        Integer num =replyDao.deleteById(id);
        Integer code = num!=null?Code.DELETE_OK:Code.DELETE_ERR;
        String msg = num!=null?"删除成功":"删除失败，请重试";
        return new Result(code,"",msg);
    }

    @Log(operation = "更新回复评论信息")
    @PutMapping
    public Result updateReply(@RequestBody Reply reply)
    {
        String mes = reply.getRContent();
        SenstiveUtils.init();
        reply.setRContent(SenstiveUtils.replaceSensitiveWord(mes));
        Integer num = replyDao.updateById(reply);
        Integer code = num !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = num != null?"数据查询成功":"数据查询失败，请重试";
        return new Result(code,reply,msg);
    }





}
