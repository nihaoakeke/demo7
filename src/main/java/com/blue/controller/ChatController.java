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
    /**
     * 就是某一个用户给另一个用户发布了消息
     * @param chat
     * @return
     */
    @Log(operation = "发送一条消息")
    @PostMapping
    public Result sendMessage(@RequestBody Chat chat) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        chat.setChatTime(date);
        chat.setChatFlag(0);
        Integer num =chatDao.insert(chat);
        Integer code = num !=null ? Code.SAVE_OK :Code.SAVE_ERR;
        String msg = num != null?"数据保存成功":"数据保存失败，请重试";
        return new Result(code,chat,msg);

    }
    /**
     * 删除某一条消息
     * @param chat
     * @return
     */
    @Log(operation = "删除某一条消息")
    @DeleteMapping("/delete")
    public Result deleteMessage(@RequestBody Chat chat)
    {
        List<Integer> chatlist =chat.getDeleteid();
        for(Integer id:chatlist) {
            Integer num = chatDao.deleteById(id);
        }
        return new Result(Code.GET_OK,"","删除成功");
    }
    /**
     * 更新聊太难的信息
     * @param chat
     * @return
     */
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
    /**
     * 查询两个用户之间的聊天记录信息
     * @param fromuser
     * @param touser
     * @return
     */
    @Log(operation = "查询两个用户的信息")
    @GetMapping("/message/{fromuser}/{touser}")
    public Result selectMessage(@PathVariable Integer fromuser,@PathVariable Integer touser)
    {
        List<Chat> chatList = chatService.selectChatByFT(fromuser,touser);
        Integer code = chatList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = chatList != null?"数据查询成功":"数据查询失败，请重试";
        return new Result(code,chatList,msg);
    }
    /**
     * 确认已读消息
     * @param id
     * @return
     */
    @Log(operation = "确认已读消息")
    @GetMapping("/okflag/{id}")
    public Result okFlag(@PathVariable Integer id) {
        Boolean num = chatService.okFlag(id);
        Integer code = num !=false ? Code.GET_OK :Code.GET_ERR;
        String msg = num != false?"确认成功":"确认失败，请重试";
        return new Result(code,"",msg);
    }
    /**
     * 查看自己未读的消息
     * @param touser
     * @return
     */
    @Log(operation = "接收方未读的消息")
    @GetMapping("/getUnreadByTo/{touser}")
    public Result getUnreadByTo(@PathVariable Integer touser) {
        List<Chat> chatList = chatService.selectMessageByTo(touser);
        Integer code = chatList !=null ? Code.GET_OK :Code.GET_ERR;
        String msg = chatList != null?"数据查询成功":"数据查询失败，请重试";
        return new Result(code,chatList,msg);
    }
}
