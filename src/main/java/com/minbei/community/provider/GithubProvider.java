package com.minbei.community.provider;

import com.alibaba.fastjson.JSON;
import com.minbei.community.dto.AccessTokenDTO;
import com.minbei.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * 提供者：
 *  Github第三方提供者
 */

// @Component：仅仅的把当前类初始化到Spring容器的上下文
@Component
public class GithubProvider {

    // 获取AccessToken
    public String getAccessToken(AccessTokenDTO accessTokenDTO){

        //
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        // JSON.toJSONString(accessTokenDTO)：
        RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO), mediaType);
        Request request = new Request.Builder()
                // 需要调用的地址
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            // 获取包含AccessToken的字符串
            String string =response.body().string();
            // 通过split方法截取string中的&之前的信息
            String [] split = string.split("&");
            // 获取split数组中的0的索引的值
            String tokenString = split[0];
            String token = tokenString.split("=")[1];
            // 返回token
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取用户信息
    public GithubUser getUser(String accessToken){
        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                // 指定的通过accessToken获取用户信息的网址
                .url("http://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            // 获取response
            Response response = client.newCall(request).execute();
            // 得到当前response的String对象
            String string =response.body().string();
            // parseObject：把String自动转换成一个java的类对象
            GithubUser githubUser = JSON.parseObject(string,GithubUser.class);
            // 返回用户信息
            return githubUser;
        } catch (IOException e) {
        }
        return null;
    }

}
