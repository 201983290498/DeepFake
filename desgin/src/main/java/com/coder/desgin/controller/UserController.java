package com.coder.desgin.controller;


import com.coder.desgin.entity.mysql.User;
import com.coder.desgin.service.UserService;
import com.coder.desgin.util.RespMessageUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查找账号是否存在, 存在则返回相应图片的id, 前端通过id去请求图片
     * @param account 用户输入的账号或者用户名
     * @return 成功,返回图片id; 失败, 报错。
     */
    @GetMapping("/account/is_exist")
    @ResponseBody
    public String isExist(String account){
        User user = userService.isExist(account);
        if(user==null){
            return RespMessageUtils.ERROR("用户不存在");
        }else{
            Map<String,Object> data = new HashMap<>();
            if(user.getImageId()!=null){
                data.put("imageId",user.getImageId());
            }else{
                data =null;
            }
            return RespMessageUtils.SUCCESS(data);
        }
    }

    /**
     * 用户登入
     * @param username 用户名
     * @param password 密码
     * @return 失败提示错误信息, 成功记录session,并且记录登入时间。
     */
    @PostMapping("/login")
    @ResponseBody
    public String login(String username, String password, HttpServletRequest request){
        User user = new User(username, password);
        User login = userService.checkAccount(user);
        if(login == null){
            return RespMessageUtils.ERROR("账号或者密码错误");
        }
        request.getSession().setAttribute("user",new User(username,new Date(System.currentTimeMillis()),user.getImageId()));
        return RespMessageUtils.SUCCESS(user);
    }
}
