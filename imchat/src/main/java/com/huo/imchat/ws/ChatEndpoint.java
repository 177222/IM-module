package com.huo.imchat.ws;

import com.alibaba.fastjson.JSON;
import com.huo.imchat.config.GetHttpSessionConfig;
import com.huo.imchat.mapper.MessageHistoryMapper;
import com.huo.imchat.pojo.MessageHistory;
import com.huo.imchat.utils.MessageUtils;
import com.huo.imchat.ws.pojo.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat",configurator = GetHttpSessionConfig.class)
@Component
public class ChatEndpoint {
    private HttpSession httpSession;

    private static final Map<String,Session> onlineUsers=new ConcurrentHashMap<>();
    /**
     * 建立websocket链接后调用
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        //保存session
        this.httpSession=(HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String user=(String)this.httpSession.getAttribute("user");
        onlineUsers.put(user,session);
        //广播上线消息
        String message = MessageUtils.getMessage(true, null, getAllUser());
        broadcastAllUsers(message);

    }

    /**
     * 获取在线用户
     * @return
     */
    private Set getAllUser(){
        Set<String> set = onlineUsers.keySet();
        return set;
    }

    /**
     * 对每一个用户发送消息
     * @param message
     */
    private void broadcastAllUsers(String message){
        try {
            Set<Map.Entry<String, Session>> entries = onlineUsers.entrySet();
            for (Map.Entry<String, Session> entry : entries) {
                Session session = entry.getValue();
                session.getBasicRemote().sendText(message);
            }
        }catch (Exception e){

        }
    }

    /**
     * 发送消息调用
     * @param message
     */
    @OnMessage
    public void onMessage(String message){
        //接收消息json，转化为对象
        Message msg= JSON.parseObject(message, Message.class);
        //获取对象信息
        String toName=msg.getToName();
        String mess=msg.getMessage();
        Session session=onlineUsers.get(toName);
        String user=(String)this.httpSession.getAttribute("user");
        String msg1 = MessageUtils.getMessage(false, user, mess);
        try {
            session.getBasicRemote().sendText(msg1);
            MessageHistory m=new MessageHistory();
            /*m.setMessage(mess);
            m.setUsername(user);
            m.setToUserName(toName);
            MessageHistoryMapper.insert(m);*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开websocket时调用
     * @param session
     */
    @OnClose
    public void onClose(Session session){
        //删除map中当前用户的session对象
        String user=(String)this.httpSession.getAttribute("user");
        onlineUsers.remove(user);
        //广播下线信息
        String message=MessageUtils.getMessage(true,null,getAllUser());
        broadcastAllUsers(message);
    }
}
