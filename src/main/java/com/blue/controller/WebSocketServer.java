package com.blue.controller;

import com.blue.dao.ChatDao;
import com.blue.dao.UserMapper;
import com.blue.domain.Chat;
import com.blue.domain.User;
import com.blue.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@ServerEndpoint("/webSocketServer/{fromUser}")
public class WebSocketServer {

    /**
     * 与客户端的连接会话，需要通过他来给客户端发消息
     */
    private Session session;
    private ApplicationContext applicationContext;
    @Autowired
    private ChatDao chatDao;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 当前用户ID
     */
    private String userId;

    /**
     *  concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     *  虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
     */
    private static CopyOnWriteArraySet<WebSocketServer> webSockets =new CopyOnWriteArraySet<>();

    /**
     *用来存在线连接用户信息
     */
    private static ConcurrentHashMap<String,Session> sessionPool = new ConcurrentHashMap<String,Session>();

    /**
     * 连接成功方法
     * @param session 连接会话
     * @param userId 用户编号
     */
    @OnOpen
    public void onOpen(Session session , @PathParam("fromUser") String userId){
        try {
            this.session = session;
            this.userId = userId;
            webSockets.add(this);
            sessionPool.put(userId, session);
            log.info("【websocket消息】 用户：" + userId + " 加入连接...");
        } catch (Exception e) {
            log.error("---------------WebSocket连接异常---------------");
        }
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose(){
        try {
            webSockets.remove(this);
            sessionPool.remove(this.userId);
            log.info("【websocket消息】 用户："+ this.userId + " 断开连接...");
        } catch (Exception e) {
            log.error("---------------WebSocket断开异常---------------");
        }
    }

    @OnMessage
    public void onMessage(@PathParam("fromUser") String userId, String body){
        try {
            //将Body解析
            JSONObject jsonObject = JSONObject.parseObject(body);
            //获取目标用户地址
            String[] targetUserId = (String[]) jsonObject.getJSONArray("targetUserId").toArray(new String[0]);
            //获取需要发送的消息
            String message = jsonObject.getString("message");
            Integer flag = Integer.valueOf(jsonObject.getString("flag"));
            jsonObject.put("userId" , userId);
            User user = userMapper.selectById(Integer.parseInt(userId));
            /**
             *
             * (1)给所有的用户进行发送消息
             * （2）给单个用户私聊发送消息
             * （3）群发消息，某个用户给多个用户发送消息
             */
            if(flag==0&&user.getUFlag()==1){
                sendAllMessage(Integer.parseInt(userId),message);
            }else if(flag==1){
                sendOneMessage(Integer.parseInt(userId),targetUserId[0],message);
            }else{
                sendMoreMessage(Integer.parseInt(userId),targetUserId,message);
            }


//            if(userId.equals(targetUserId)){
//                sendMoreMessage(new String[]{targetUserId} ,  JSONObject.toJSONString(jsonObject));
//            }else{
//                sendMoreMessage(new String[]{userId , targetUserId} ,  JSONObject.toJSONString(jsonObject));
//            }
        } catch (Exception e) {
            log.error("---------------WebSocket消息异常---------------");
        }
    }


    /**
     * 此为广播消息
     * @param message
     */
    public void sendAllMessage(Integer fromUser,String message) {
        log.info("【websocket消息】广播消息:"+message);
        for(WebSocketServer webSocket : webSockets) {
            Integer uid = Integer.valueOf(webSocket.userId);
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            Chat chat = new Chat();
            chat.setFromUser(fromUser);
            chat.setChatTime(date);
            chat.setChatFlag(0);
            chat.setChatMessage(message);
            chat.setToUser(uid);
            chatDao.insert(chat);
            try {
                if(webSocket.session.isOpen()) {
                    webSocket.session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                log.error("---------------WebSocket消息广播异常---------------");
            }
        }
    }

    /**
     * 单点消息
     * @param userId
     * @param message
     */
    public void sendOneMessage(Integer fromUser,String userId, String message) {
        Session session = sessionPool.get(userId);
        Chat chat =new Chat();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        chat.setChatMessage(message);
        chat.setChatTime(date);
        chat.setFromUser(Integer.valueOf(fromUser));
        chat.setToUser(Integer.valueOf(userId));
        chatDao.insert(chat);
        if (session != null&&session.isOpen()) {
            try {
                log.info("【websocket消息】 单点消息:"+message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                log.error("---------------WebSocket单点消息发送异常---------------");
            }
        }
    }

    /**
     * 发送多人单点消息
     * @param userIds
     * @param message
     */
    public void sendMoreMessage(Integer fromUser,String[] userIds, String message) {
        for(String userId:userIds) {
            Chat chat = new Chat();
            chat.setToUser(Integer.valueOf(userId));
            chat.setFromUser(fromUser);
            chat.setChatMessage(message);
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            chat.setChatTime(date);
            chatDao.insert(chat);
            Session session = sessionPool.get(userId);
            if (session != null&&session.isOpen()) {
                try {
                    log.info("【websocket消息】 单点消息:"+message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    log.error("---------------WebSocket多人单点消息发送异常---------------");
                }
            }
        }
    }
}
