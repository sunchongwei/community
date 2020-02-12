package com.majiang.community.community.controller;

import com.majiang.community.community.mapper.UserMapper;
import com.majiang.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("/")
    public String hello(HttpServletRequest request){
        //获取cookie中的token
        String token;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
           if(cookie.getName().equals("token")) {
               //获取token的值
               token = cookie.getValue();
               User user =  userMapper.findByToken(token);
               if(user != null){
                   request.getSession().setAttribute("user",user);
               }
               break;
           }
        }
        return "index";
    }


}
