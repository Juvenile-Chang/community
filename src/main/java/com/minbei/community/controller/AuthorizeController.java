package com.minbei.community.controller;

import com.minbei.community.dto.AccessTokenDTO;
import com.minbei.community.dto.GithubUser;
import com.minbei.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 */

// @Controller：把当前类作为路由API的承载者
@Controller
public class AuthorizeController {

    // @Autowired: 自动把Spring容器里面的写好的实例化的实例加载到我当前使用的上下文
    @Autowired
    private GithubProvider githubProvider;

    // @Value("${参数}")：通过Value标签获取在application.properties里面配置的对应的值
    @Value("${github.client_id}")
    private String clientId;
    @Value("${github.client_secret}")
    private String clientSecret;
    @Value("${github.redirect_uri}")
    private String redirectUri;

    /**
     * @RequestParam(name = "code") String code :
     *  @RequestParam: 从页面网址中截取数值
     *  code:Git Hub里面配置的Client ID，用来执行跨平台登录
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state){
        // 创建一个AccessTokenDTO对象，用来存储信息
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setClient_id(clientId);
        /**
         * githubProvider.getAccessToken(accessTokenDTO)：
         *  通过githubProvider类中的getAccessToken方法获取accessToken值
         *  accessTokenDTO是封装好的API接口，运行之后就会得到对应的用户信息地址
         * githubProvider.getUser(accessToken)：
         *  通过githubProvider类中的getUser方法获取用户信息，
         */
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        return "index";
    }
}
