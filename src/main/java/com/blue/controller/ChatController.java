package com.blue.controller;

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

    @DeleteMapping("/{id}")
    public Result deleteMessage(@PathVariable Integer id)
    {
        Integer num =chatDao.deleteById(id);
        Integer code = num !=null ? Code.DELETE_OK :Code.DELETE_ERR;
        String msg = num != null?"数据保存成功":"数据保存失败，请重试";
        return new Result(code,"",msg);
    }


    @PutMapping
    public Result updateMessage(@RequestBody Chat chat)
    {
        Integer num =chatDao.updateById(chat);
        Integer code = num !=null ? Code.DELETE_OK :Code.DELETE_ERR;
        String msg = num != null?"数据保存成功":"数据保存失败，请重试";
        return new Result(code,"",msg);
    }


    @GetMapping("/message/{fromuser}/{touser}")
    public Result selectMessage(@PathVariable Integer fromuser,@PathVariable Integer touser)
    {
        List<Chat> chatList = chatService.selectChatByFT(fromuser,touser);
        Integer code = chatList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = chatList != null?"数据保存成功":"数据保存失败，请重试";
        return new Result(code,chatList,msg);
    }

    @GetMapping("/okflag/{id}")
    public Result okFlag(@PathVariable Integer id)
    {
        Boolean num = chatService.okFlag(id);
        Integer code = num !=false ? Code.GET_OK :Code.GET_ERR;
        String msg = num != false?"数据保存成功":"数据保存失败，请重试";
        return new Result(code,"",msg);
    }




}
