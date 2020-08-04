package com.minbei.community.controller;

import com.minbei.community.dto.QuestionDTO;
import com.minbei.community.mapper.UserMapper;
import com.minbei.community.model.User;
import com.minbei.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 论坛首页
 */
@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model){

        if(request.getCookies()==null || request.getCookies().length ==0){

        }else{
            Cookie [] cookies = request.getCookies();
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }

        List<QuestionDTO> questionList = questionService.list();
        model.addAttribute("questions",questionList);

        return "index";
    }

}
