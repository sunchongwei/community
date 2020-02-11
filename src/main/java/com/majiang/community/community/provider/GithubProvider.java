package com.majiang.community.community.provider;

import com.alibaba.fastjson.JSON;
import com.majiang.community.community.dto.AccessTokenDTO;
import com.majiang.community.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

@Component
public class GithubProvider {
    //component把当前的类初始化到spring的上下文
    //不需要实例化对象
    //自动放置到池子里面

    /**
     *
     * @param accessTokenDTO
     * @return
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
       MediaType  mediaType = MediaType.get("application/json; charset=utf-8");
       OkHttpClient client = new OkHttpClient();

       RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDTO));
       Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
       try (Response response = client.newCall(request).execute()) {
           String string  = response.body().string();
           //access_token=aec82b169830be0aae69c09d41771fabc8ae0670&scope=user&token_type=bearer
           String[] split = string.split("&");
           String tokenString = split[0];
           String[] split1 = tokenString.split("=");
           return split1[1];
       }catch (Exception e ){
            }
        return null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try{
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            //把string的json转化成对象
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        }catch(Exception e){
        }
        return null;
    }






}
