package com.huo.imchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//注册微服务
//@EnableEurekaClient
public class ImchatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImchatApplication.class, args);
    }

}
