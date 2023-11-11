package com.blue.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.dao.ChatDao;
import com.blue.dao.CommentDao;
import com.blue.domain.Chat;
import com.blue.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ChatService extends ServiceImpl<ChatDao, Chat> {


    @Autowired
    private ChatDao chatDao;

    /**
     * 确认已读
     * @param id
     * @return
     */

    public Boolean okFlag(Integer id)
    {
        Chat chat =chatDao.selectById(id);
        chat.setChatFlag(1);
        chatDao.updateById(chat);
        return true;
    }

    /**
     * 查询两人的聊天记录
     * @param fromuser
     * @param touser
     * @return
     */

    public List<Chat> selectChatByFT(Integer fromuser,Integer touser)
    {

        QueryWrapper qw = new QueryWrapper();
        qw.eq("from_user",fromuser);
        qw.eq("to_user",touser);
//        qw.select("time");
//        qw.select("flag");
//        qw.select("message");
        List<Chat> chatList = chatDao.selectList(qw);
        return chatList;
    }

    /**
     *
     * @param fromuser
     * @return
     */
    public List<Chat> selectMessageByFrom(Integer fromuser)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("from_user",fromuser);
        qw.orderByDesc("chat_time");
        List<Chat> chatList = chatDao.selectList(qw);
        return chatList;
    }

    /**
     * 查询接收到的消息
     * @param touser
     * @return
     */
    public List<Chat> selectMessageByTo(Integer touser)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("to_user",touser);
        qw.orderByDesc("chat_time");
        List<Chat> chatList = chatDao.selectList(qw);
        return chatList;
    }

    /**
     * 查询发出去未读的
     * @param fromuser
     * @return
     */

    public List<Chat> selectMessageByFromUnread(Integer fromuser)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("from_user",fromuser);
        qw.orderByDesc("chat_time");
        qw.eq("chat_flag",0);
        List<Chat> chatList = chatDao.selectList(qw);
        return chatList;
    }


    /**
     * 查询接受消息未读的
     * @param touser
     * @return
     */
    public List<Chat> selectMessageByToUnread(Integer touser)
    {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("to_user",touser);
        qw.orderByDesc("chat_time");
        qw.eq("chat_flag",0);
        List<Chat> chatList = chatDao.selectList(qw);
        return chatList;
    }

}
