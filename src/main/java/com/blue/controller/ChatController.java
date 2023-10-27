package com.blue.controller;

import com.blue.config.Log;
import com.blue.dao.ChatDao;
import com.blue.domain.Chat;
import com.blue.domain.Code;
import com.blue.domain.Result;
import com.blue.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatController {
    @Autowired
    private ChatDao chatDao;

    @Autowired
    private ChatService chatService;


    @Log(operation = "发送一条消息")
    @PostMapping
    public Result sendMessage(@RequestBody Chat chat)
    {

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        chat.setTime(date);
        Integer num =chatDao.insert(chat);
        Integer code = num !=null ? Code.SAVE_OK :Code.SAVE_ERR;
        String msg = num != null?"数据保存成功":"数据保存失败，请重试";
        return new Result(code,chat,msg);

    }

    @Log(operation = "删除某一条消息")
    @DeleteMapping("/{id}")
    public Result deleteMessage(@PathVariable Integer id)
    {
        Integer num =chatDao.deleteById(id);
        Integer code = num !=null ? Code.DELETE_OK :Code.DELETE_ERR;
        String msg = num != null?"数据保存成功":"数据保存失败，请重试";
        return new Result(code,"",msg);
    }


    @Log(operation = "更新消息")
    @PutMapping
    public Result updateMessage(@RequestBody Chat chat)
    {
        Integer num =chatDao.updateById(chat);
        Integer id =chat.getChatId();
        Chat chat1 = chatDao.selectById(id);
        Integer code = num !=null ? Code.DELETE_OK :Code.DELETE_ERR;
        String msg = num != null?"数据更新成功":"数据更新失败，请重试";
        return new Result(code,chat1,msg);
    }


    @Log(operation = "查询两个用户的信息")
    @GetMapping("/message/{fromuser}/{touser}")
    public Result selectMessage(@PathVariable Integer fromuser,@PathVariable Integer touser)
    {
        List<Chat> chatList = chatService.selectChatByFT(fromuser,touser);
        Integer code = chatList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = chatList != null?"数据查询成功":"数据查询失败，请重试";
        return new Result(code,chatList,msg);
    }

    @Log(operation = "确认已读消息")
    @GetMapping("/okflag/{id}")
    public Result okFlag(@PathVariable Integer id)
    {
        Boolean num = chatService.okFlag(id);
        Integer code = num !=false ? Code.GET_OK :Code.GET_ERR;
        String msg = num != false?"确认成功":"确认失败，请重试";
        return new Result(code,"",msg);
    }

//    @Log(operation = "发送方")
//    @GetMapping("/getUnreadByFrom/{fromuser}")
//    public Result getUnreadByFrom(@PathVariable Integer fromuser)
//    {
//        List<Chat> chatList = chatService.selectMessageByFromUnread(fromuser);
//        Integer code = chatList !=null ? Code.GET_OK :Code.GET_ERR;
//        String msg = chatList != null?"数据保存成功":"数据保存失败，请重试";
//        return new Result(code,chatList,msg);
//    }


    @Log(operation = "接收方未读的消息")
    @GetMapping("/getUnreadByTo/{touser}")
    public Result getUnreadByTo(@PathVariable Integer touser)
    {
        List<Chat> chatList = chatService.selectMessageByTo(touser);
        Integer code = chatList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = chatList != null?"数据查询成功":"数据查询失败，请重试";
        return new Result(code,chatList,msg);

    }




}
