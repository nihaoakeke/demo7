package com.blue.controller;


import com.blue.config.Log;
import com.blue.dao.LinkDao;
import com.blue.domain.Code;
import com.blue.domain.Link;
import com.blue.domain.Result;
import com.blue.service.LinkService;
import com.blue.utils.SenstiveUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class LinkController {

    @Autowired
    private LinkDao linkDao;

    @Autowired
    private LinkService linkService;


    @Log(operation = "发送好友申请")
    @PostMapping
    public Result sendMessage(@RequestBody Link link){
        Link link1=linkService.selectByFT(link.getFromUser(),link.getToUser());

        Integer code =null;
        String msg = null;
        if(link1==null)
        {
            String mes = link.getMessage();
            SenstiveUtils.init();
            link.setMessage(SenstiveUtils.replaceSensitiveWord(mes));

            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            link.setTime(date);

            Integer num = linkDao.insert(link);
             code = num!=null? Code.SAVE_OK:Code.SAVE_ERR;
             msg = num!=null?"保存成功":"保存失败";
            return new Result(code, link,msg);
        }
        else{
            String mes = link.getMessage();
            SenstiveUtils.init();
            link1.setMessage(SenstiveUtils.replaceSensitiveWord(mes));


            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            link1.setTime(date);


            Integer num = linkDao.updateById(link1);
             code = num!=null? Code.SAVE_OK:Code.SAVE_ERR;
             msg = num!=null?"保存成功":"保存失败";
             return new Result(code, link1,msg);
        }


    }

    @Log(operation = "删除某条好友申请")
    @DeleteMapping("/{fromuser}/{touser}")
    public Result deleteMessage(@PathVariable Integer fromuser,@PathVariable Integer touser)
    {

//        Link link = linkService.selectByFT(2,3);
//        link.setMessage("dadas");
//        linkDao.updateById(link);

        Link link = linkService.selectByFT(fromuser,touser);

        link.setMessage("0");
        link.setUnfriend(0);
        System.out.println();
        Integer num =linkDao.updateById(link);
        Integer code = num!=null?Code.DELETE_OK:Code.DELETE_ERR;
        String msg = num!=null?"删除成功":"删除失败，请重试";
        return new Result(code,link,msg);
    }

    @Log(operation = "更新某条好友申请")
    @PutMapping
    public Result updateById(@RequestBody Link link)
    {
        String mes = link.getMessage();
        SenstiveUtils.init();
        link.setMessage(SenstiveUtils.replaceSensitiveWord(mes));
        Integer num = linkDao.updateById(link);
        Integer code = num !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = num != null?"数据更新成功":"数据更新失败，请重试";
        return new Result(code, link,msg);
    }

    @Log(operation = "查询本用户未读的好友申请")
    @GetMapping("/touser/flag/{touser}")
    public Result selectMessageBytoUserFlag(@PathVariable Integer touser)
    {
        List<Link> linkList = linkService.selectMessageByflag(touser);
        Integer code = linkList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = linkList != null?"数据查询成功":"数据查询失败，请重试";
        return new Result(code, linkList,msg);
    }

    @Log(operation = "查询用户的收到的好友请求")
    @GetMapping("/touser/{touser}")
    public Result selectMessageBytoUser(@PathVariable Integer touser)
    {
        List<Link> linkList = linkService.selectMessageBytouser(touser);
        Integer code = linkList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = linkList != null?"数据查询成功":"数据查询失败，请重试";
        return new Result(code, linkList,msg);
    }

    @Log(operation = "查询本用户发送的好友申请")
    @GetMapping("/fromuser/{fromuser}")
    public Result selectMessageByFromuser(@PathVariable Integer fromuser)
    {
        List<Link> linkList = linkService.selectMessageByfromuser(fromuser);
        Integer code = linkList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = linkList != null?"数据查询成功":"数据查询失败，请重试";
        return new Result(code, linkList,msg);
    }

    @Log(operation = "查询发送出去的申请消息，对方未读的")
    @GetMapping("/fromuser/flag/{fromuser}")
    public Result selectMessageByFromuserFlag(@PathVariable Integer fromuser)
    {
        List<Link> linkList = linkService.selectMessageByFFlag(fromuser);
        Integer code = linkList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = linkList != null?"数据更新成功":"数据更新失败，请重试";
        return new Result(code, linkList,msg);
    }

    /**
     * 查询好友未申请通过的
     * @param fromuser
     * @return
     */

    @GetMapping("/unpass/{fromuser}")
    public Result selectUnpass(@PathVariable Integer fromuser)
    {
        List<Link> linkList =linkService.selectUnpass(fromuser);
        Integer code = linkList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = linkList != null?"数据查询成功":"数据查询失败，请重试";
        return new Result(code, linkList,msg);
    }


    /**
     * 确认关注对方
     * @param fromuser
     * @param touser
     * @return
     */
    @GetMapping("/okFollow/{fromuser}/{touser}")
    public Result okFollow(@PathVariable Integer fromuser,@PathVariable Integer touser)
    {
        Boolean num = linkService.okFollwe(fromuser,touser);
        Integer code = num !=false ? Code.GET_OK :Code.GET_ERR;
        String msg = num != false?"关注成功":"关注失败，请重试";
        return new Result(code,"",msg);
    }

    /**
     * 查询自己被谁关注
     * @param touser
     * @return
     */
    @GetMapping("/tofollower/{touser}")
    public Result selectTouserFollower(@PathVariable Integer touser)
    {
        List<Link> linkList = linkService.selectTouserFollower(touser);
        Integer code = linkList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = linkList != null?"数据更新成功":"数据更新失败，请重试";
        return new Result(code, linkList,msg);
    }


    /**
     * 查看自己关注了谁
     * @param fromuser
     * @return
     */
    @GetMapping("/fromfollower/{fromuser}")
    public Result selectFromuserFollower(@PathVariable Integer fromuser)
    {
        List<Link> linkList = linkService.selectFromuserFollower(fromuser);
        Integer code = linkList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = linkList != null?"数据查询成功":"数据查询失败，请重试";
        return new Result(code, linkList,msg);
    }

    /**
     * 查询好友列表
     * @param fromuser
     * @return
     */
    @GetMapping("/getfriend/{fromuser}")
    public Result selectFriend(@PathVariable Integer fromuser)
    {
        List<Link> linkList =linkService.selectFriend(fromuser);
        Integer code = linkList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = linkList != null?"数据查询成功":"数据查询失败，请重试";
        return new Result(code, linkList,msg);
    }

    /**
     * 通过好友申请
     * @param fromuser
     * @param touser
     * @return
     */
    @GetMapping("/passApply/{fromuser}/{touser}")
    public Result passApply(@PathVariable Integer fromuser,@PathVariable Integer touser)
    {
        Boolean num = linkService.passApply(fromuser,touser);
        Integer code = num !=false ? Code.GET_OK :Code.GET_ERR;
        String msg = num != false?"数据查询成功":"数据查询失败，请重试";
        return new Result(code, num ,msg);
    }





}
