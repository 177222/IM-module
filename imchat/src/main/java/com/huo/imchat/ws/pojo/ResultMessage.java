package com.huo.imchat.ws.pojo;

import lombok.Data;

@Data
public class ResultMessage {
    boolean system;
    Object message;
    String fromName;
}
