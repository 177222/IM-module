package com.huo.imchat.utils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.huo.imchat.ws.pojo.ResultMessage;

public class MessageUtils {
    public static String getMessage(boolean isSystemMessage,String fromName,Object message){
        ResultMessage result=new ResultMessage();
        result.setSystem(isSystemMessage);
        result.setMessage(message);
        if(null!=fromName){
            result.setFromName(fromName);
        }
        return JSON.toJSONString(result);
    }
}
