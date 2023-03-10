package com.coder.desgin.controller;


import com.coder.common.util.TokenUtil;
import com.coder.desgin.entity.Constants;
import com.coder.desgin.entity.mysql.User;
import com.coder.desgin.service.UserService;
import com.coder.common.util.RespMessageUtils;
import com.coder.common.util.login.VerificationCodeFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author coder
 */
@Controller
@RequestMapping("/users")
@Slf4j
@Api(tags ={"6.用户相关接口"})
public class UserController {
    private final UserService userService;
    /**
     * 验证码管理类
     */
    private final VerificationCodeFactory verificationCodeFactory;

    private final TokenUtil tokenUtil;

    public UserController(UserService userService, VerificationCodeFactory verificationCodeFactory, TokenUtil tokenUtil) {
        this.userService = userService;
        this.verificationCodeFactory = verificationCodeFactory;
        this.tokenUtil = tokenUtil;
    }

    /**
     * 查找账号是否存在, 存在则返回相应图片的id, 前端通过id去请求图片
     * @param account 用户输入的账号或者用户名
     * @return 成功,返回图片id; 失败, 报错。
     */
    @GetMapping("/account/isExist")
    @ResponseBody
    @ApiOperation("查账号--账号是否存在")
    public String isExist(String account){
        User user = userService.isExist(account);
        if(user==null){
            return RespMessageUtils.ERROR("用户不存在");
        }else{
            Map<String,Object> data = new HashMap<>(10);
            if(user.getImageId()!=null){
                data.put("imageUrl",user.getImageUrl());
            }else{
                data =null;
            }
            return RespMessageUtils.SUCCESS(data);
        }
    }

    @GetMapping("/account/emailExist")
    @ResponseBody
    @ApiOperation("查邮箱--邮箱是否存在")
    public String emailExist(String email){
        User user = userService.checkEmail(email);
        if (user == null) {
            return RespMessageUtils.ERROR("邮箱没有注册任何账号。");
        } else {
            return RespMessageUtils.SUCCESS("邮箱已经被注册。");
        }
    }

    /**
     * 用户登入
     * @param username 用户名
     * @param password 密码
     * @return 失败提示错误信息, 成功记录session,并且记录登入时间。
     */
    @ApiOperation(value = "查用户--用户登入")
    @PostMapping("/login")
    @ResponseBody
    public String login(String username, String password){
        User user = new User(username, password);
        User login = userService.checkAccount(user);
        if(login == null){
            return RespMessageUtils.ERROR("账号或者密码错误");
        }
        login.setToken(tokenUtil.sign(login.getUserId()));
        login.setCreateTime(new Date(System.currentTimeMillis()));
        login.setPassword("");
        login.setStatus(0);
        return RespMessageUtils.SUCCESS(login);
    }


    @PostMapping("/register")
    @ResponseBody
    @ApiOperation("更新用户--注册")
    public String register(User user, String repeatPwd, MultipartFile photo, String validateData){

        if(!repeatPwd.equals(user.getPassword())) {
            return RespMessageUtils.ERROR("两次输入的密码不正确!");
        }
        if (userService.checkEmail(user.getEmail()) != null) {
            return RespMessageUtils.ERROR("这个邮箱已经被注册了!");
        }
        if (!verificationCodeFactory.checkValidationInfo(user.getEmail(),validateData, Constants.REGISTER)) {
            return RespMessageUtils.ERROR("验证码错误");
        }
        // 注册
        try {
            userService.insertUser(user, photo);
        } catch (Exception e) {
            return RespMessageUtils.ERROR(e.getMessage());
        }
        return RespMessageUtils.SUCCESS("登入成功!");
    }

    @PostMapping("/forgetPwd")
    @ResponseBody
    @ApiOperation("更新用户--修改密码")
    public String changePwd(String email, String password, String repeatPwd, String validateData) {
        if (!password.equals(repeatPwd)) {
            return RespMessageUtils.ERROR("两次密码输入不符合。");
        }

        Boolean emailExist = verificationCodeFactory.checkValidationInfo(email, validateData, Constants.FORGET_PASSWORD);
        if (!emailExist) {
            return RespMessageUtils.ERROR("验证码错误!");
        }
        User newUser = userService.checkEmail(email);
        if (newUser != null) {
            newUser.setPassword(password);
            userService.updateOne(newUser);
            return RespMessageUtils.SUCCESS("修改成功!");
        } else {
            return RespMessageUtils.ERROR("邮箱对应的账号不存在。");
        }
    }

    @ResponseBody
    @PostMapping("/updateUser")
    @ApiOperation("更新用户--更新用户基本信息")
    public String updateUser(User user){
        log.warn("user " + user.getUserId() + "updated the personal information." + user);
        userService.updateOne(user);
        return RespMessageUtils.SUCCESS();
    }

    @ResponseBody
    @PostMapping("/updatePhoto")
    @ApiOperation("更新用户--更新用户头像")
    public String updatePhoto(String userId, MultipartFile photo) throws IOException {
        String url = userService.updatePhoto(userId, photo);
        return RespMessageUtils.SUCCESS(url);
    }
}