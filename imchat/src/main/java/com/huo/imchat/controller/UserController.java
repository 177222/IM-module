package com.huo.imchat.controller;

import com.huo.imchat.pojo.Result;
import com.huo.imchat.pojo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
    @PostMapping("/login")
    public Result login(@RequestBody User user, HttpSession session){
        Result result=new Result();
        //密码由数据库获取，以及搭配md5加盐实现非明文密码配对提高安全性
        if(user!=null&&"123".equals(user.getPasssword())){
            result.setFlag(true);
            //登录成功，将数据存储到session
            session.setAttribute("user",user.getUsername());
        }else {
            result.setFlag(false);
            result.setMessage("密码错误");
        }
        return result;
    }
    @GetMapping("/getUsername")
    public String getUsername(HttpSession session){
        String username=(String) session.getAttribute("user");
        return username;
    }
}
