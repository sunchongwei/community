package com.majiang.community.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/admin")
    public String hello(){
        return "hello";
    }
}
