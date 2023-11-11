package com.blue.controller;


import com.blue.config.Log;
import com.blue.dao.CommentDao;
import com.blue.domain.Code;
import com.blue.domain.Comment;
import com.blue.domain.News;
import com.blue.domain.Result;
import com.blue.service.CommentService;
import com.blue.utils.KeyUtils;
import com.blue.utils.RedisUtil;
import com.blue.utils.SenstiveUtils;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 确认点赞一个评论
     * @param id
     * @return
     */
    @Log(operation = "确认点赞某一个评论")
    @GetMapping("/okLike/{id}")
    public Result OKLikeById(@PathVariable Integer id) {
        Comment comment = commentService.getById(id);
        //RedisUtil.zIncrementScore("viewNum",id.toString(),1);
        if (comment == null) {
            return new Result(Code.GET_ERR,comment,"点赞失败，请重试");
        }
        return new Result(Code.GET_OK,comment,"点赞成功");
    }
    /**
     * 取消点一个赞
     * @param id
     * @return
     */
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

    /**
     * 根据发表的时间和用户进行查询
     * @param uid
     * @return
     */
    @Log(operation = "根据发表时间和用户进行查询")
    @GetMapping("/time/uid/{uid}")
    public Result getByuTId(@PathVariable Integer uid) {
        List<Comment> commentList= commentService.selectCommentByTimeUDesc(uid);
        Integer code = commentList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = commentList != null ? "数据查询成功" : "数据查询失败，请重试！";
        Iterator<Comment> iterator = commentList.iterator();
        while (iterator.hasNext()){
            Comment comment = iterator.next();
            String name = comment.getCContent();
            comment.setCContent(EmojiParser.parseToUnicode(name));
        }
        return new Result(code,commentList,msg);
    }
    /**
     * 根据用户的id进行查询评论
     * @param id
     * @return
     */
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
    /**
     * 发表评论
     * @param comment
     * @return
     */
    @Log(operation ="发表评论")
    @PostMapping("/register")
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
        comment.setCtime(date);
        comment.setCLike(0);
        Integer num = commentDao.insert(comment);
        Integer code = num!=null?Code.SAVE_OK:Code.SAVE_ERR;
        String msg=num!=null?"评论成功":"评论失败，请重试";
        return new Result(code,comment,msg);
    }
    /**
     * 删除评论信息
     * @param comment
     * @return
     */
    @Transactional
    @Log(operation = "删除评论")
    @DeleteMapping("/manager")
    public Result deleteComment(@RequestBody Comment comment) {
        List<Integer> list =comment.getDeleteid();
        for(Integer cid :list){
        Integer num =commentDao.deleteById(cid);
        commentService.deleteComment(cid);
        redisUtil.zRemove("cLike",cid.toString());}
        return new Result(Code.GET_OK,"","删除成功");
    }
    /**
     * 删除评论
     * @param
     * @return
     */
    @Transactional
    @Log(operation = "删除评论")
    @DeleteMapping("/delete")
    public Result userdeleteComment(@RequestBody Comment comment) {
        List<Integer> list =comment.getDeleteid();
        for(Integer cid :list){
            Integer num =commentDao.deleteById(cid);
            commentService.deleteComment(cid);
            redisUtil.zRemove("cLike",cid.toString());}
        return new Result(Code.GET_OK,"","删除成功");
    }
    /**
     * 更新评论信息
     * @param comment
     * @return
     */
    @Log(operation = "更新评论信息")
    @PutMapping()
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
        Comment comment1= commentDao.selectById(comment.getCId());
        return new Result(code,comment1,msg);
    }

    /**
     *
     * @param bid
     * @return
     */
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
        while (iterator.hasNext()){
            Comment comment = iterator.next();
            String name = comment.getCContent();
            comment.setCContent(EmojiParser.parseToUnicode(name));
        }
        return new Result(code,commentList,msg);
    }

    @GetMapping("/selectOne/{bid}/{flag}")
    public Result selectOneComment(@PathVariable Integer bid,@PathVariable Integer flag)
    {
        List<Comment> commentList = commentService.selectOneBybId(bid);
        Integer code = commentList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = commentList != null?"数据查询成功":"数据查询失败，请重试";
        if(flag==0){
            commentList.sort(Comparator.comparing(Comment::getCtime, Collections.reverseOrder()));
        }else{
            commentList.sort(Comparator.comparing(Comment::getCLike, Comparator.reverseOrder()));
        }
        return new Result(code,commentList,msg);
    }

    @GetMapping("/selectTwo/{cid}/{flag}")
    public Result selectTwo(@PathVariable Integer cid,@PathVariable Integer flag)
    {
        List<Comment> commentList = commentService.selectTwo(cid);
        Integer code = commentList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = commentList != null?"数据查询成功":"数据查询失败，请重试";
        if(flag==0){
            commentList.sort(Comparator.comparing(Comment::getCtime, Collections.reverseOrder()));
        }else{
            commentList.sort(Comparator.comparing(Comment::getCLike, Comparator.reverseOrder()));
        }
        return new Result(code,commentList,msg);
    }

    @GetMapping("/uid/{uid}/{flag}")
    public Result selectReply(@PathVariable Integer uid,@PathVariable Integer flag)
    {
        List<Comment> commentList =commentService.selectReply(uid);
        Integer code = commentList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = commentList != null?"数据查询成功":"数据查询失败，请重试";
        if(flag==0){
            commentList.sort(Comparator.comparing(Comment::getCtime, Collections.reverseOrder()));
        }else{
            commentList.sort(Comparator.comparing(Comment::getCLike, Comparator.reverseOrder()));
        }
        return new Result(code,commentList,msg);
    }

    @GetMapping("/like/{cid}/{postid}/{flag}")
    public Result likeBlog(@PathVariable Integer flag,@PathVariable Integer cid,@PathVariable Integer postid)
    {
        Comment comment =commentDao.selectById(cid);
        Integer authorid = comment.getUId();
        Map<String,String> map2 = new HashMap<>();
        if(flag==0){
            if(commentService.checkLike(authorid,postid,cid)!=null){
                return new Result(Code.GET_OK,"","1");
            }else{
                return new Result(Code.GET_OK,"","0");
            }
        } else if(flag==1){
            if(commentService.checkLike(authorid,postid,cid)!=null){
                return new Result(Code.GET_OK,"","您已经点过赞了");
            }
            Double score = redisUtil.zScore("CommentLike",KeyUtils.gengerateKey(authorid.toString(),postid.toString(),cid.toString()));
            if(score!=null){
                return new Result(Code.GET_OK,"","您只能点一次赞");
            }
            //把赞得数量加一
            redisUtil.zIncrementScore("cLike",cid.toString(),1);
            //把联合用户加进去
            redisUtil.zAdd("CommentLike", KeyUtils.gengerateKey(authorid.toString(),postid.toString(),cid.toString()),1);
            Double score1 = redisUtil.zScore("cLike",cid.toString());
            map2.put("cLike", String.valueOf(score1.intValue()));
            return new Result(Code.GET_OK,map2,"点赞成功");
        }else{
            redisUtil.zIncrementScore("cLike",cid.toString(),-1);
            redisUtil.zRemove("CommentLike",KeyUtils.gengerateKey(authorid.toString(),postid.toString(),cid.toString()));
            Double score1 = redisUtil.zScore("cLike",cid.toString());
            map2.put("cLike", String.valueOf(score1.intValue()));
            return new Result(Code.GET_OK,map2,"取消点赞成功");
        }

    }



//    /**
//     * 根据点赞量和用户进行排序查询
//     * @param uid
//     * @return
//     */
//    @Log(operation = "根据点赞量和用户进行排序查询")
//    @GetMapping("/like/uid/{uid}")
//    public Result getByuLId(@PathVariable Integer uid) {
//        List<Comment> commentList= commentService.selectCommentByLikeUDesc(uid);
//        Integer code = commentList != null ? Code.GET_OK : Code.GET_ERR;
//        String msg = commentList != null ? "数据查询成功" : "数据查询失败，请重试！";
//        Iterator<Comment> iterator = commentList.iterator();
//        while (iterator.hasNext()){ //判断迭代器是否有元素
//            Comment comment = iterator.next();//获取集合下一个元素
//            String name = comment.getCContent();
//            comment.setCContent(EmojiParser.parseToUnicode(name));
//        }
//        return new Result(code,commentList,msg);
//    }


}
