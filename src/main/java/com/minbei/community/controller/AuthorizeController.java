package com.minbei.community.controller;

import com.minbei.community.dto.AccessTokenDTO;
import com.minbei.community.dto.GithubUser;
import com.minbei.community.mapper.UserMapper;
import com.minbei.community.model.User;
import com.minbei.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 *
 */

// @Controller：把当前类作为路由API的承载者
@Controller
public class AuthorizeController {

    // @Autowired: 自动把Spring容器里面的写好的实例化的实例加载到我当前使用的上下文
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserMapper userMapper;

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
     *  HttpServletRequest：Spring会把上下文中的request放在类中供我们使用
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request){
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
        GithubUser githubuser = githubProvider.getUser(accessToken);
        if(githubuser!=null){
            // 登录成功,写cookie和session

            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubuser.getName());
            user.setAccountId(String.valueOf(githubuser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            // 获取当前用户
            request.getSession().setAttribute("user",githubuser);
            // 重定向到index页面
            return "redirect:/";
        }else {
            // 登录失败
            return "redirect:/";
        }
    }
}
