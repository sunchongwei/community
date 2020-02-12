package com.majiang.community.community.controller;

import com.majiang.community.community.dto.AccessTokenDTO;
import com.majiang.community.community.dto.GithubUser;
import com.majiang.community.community.mapper.UserMapper;
import com.majiang.community.community.model.User;
import com.majiang.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;


    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;
    
    @Autowired
    UserMapper userMapper;

    @GetMapping("/callback")
    public String AuthorizeController(@RequestParam("code")String code,
                                      @RequestParam("state")String state,
                                      HttpServletRequest request,
                                      HttpServletResponse response){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);

        //获取accessToken
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //获取
        GithubUser githubUser = githubProvider.getUser(accessToken);
        System.out.println(githubUser.getName());
        if(githubUser != null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);

            //根据tooken实现自动登录
            //写cookie
            response.addCookie(new Cookie("token",token));

            //登录成功，session
            request.getSession().setAttribute("githubUser",githubUser);
            //跳到首页
            return "redirect:/index";
        }else {
            //登录失败
            return "redirect:/index";

        }
    }
}
