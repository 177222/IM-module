package com.huo.imchat.pojo;

import lombok.Data;

import java.sql.Date;

@Data
public class MessageHistory {
    //随业务要求修改消息记录格式
    private String message;
    private Data time;
    private String username;
    private String toUserName;


}
