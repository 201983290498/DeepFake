package com.coder.desgin.controller;


import com.coder.desgin.entity.mysql.User;
import com.coder.desgin.service.UserService;
import com.coder.desgin.util.RespMessageUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    public String login(String username, String password){
        return null;
    }
}
