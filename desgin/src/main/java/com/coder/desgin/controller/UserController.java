package com.coder.desgin.controller;


import com.coder.common.util.TokenUtil;
import com.coder.desgin.entity.mysql.User;
import com.coder.desgin.exception.MailMessageException;
import com.coder.desgin.service.UserService;
import com.coder.common.util.RespMessageUtils;
import com.coder.common.util.login.VerificationCodeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author coder
 */
@Controller
@RequestMapping("/users")
@Slf4j
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
    @PostMapping("/login")
    @ResponseBody
    public String login(String username, String password, HttpServletRequest request){
        User user = new User(username, password);
        User login = userService.checkAccount(user);
        if(login == null){
            return RespMessageUtils.ERROR("账号或者密码错误");
        }
        login.setToken(tokenUtil.sign(username));
        login.setCreateTime(new Date(System.currentTimeMillis()));
        return RespMessageUtils.SUCCESS(login);
    }

    /**
     * 生成验证码
     * @param email 需要发送的网络邮箱
     * @return 返回是否发送验证码
     */
    @ResponseBody
    @PostMapping("/register/genMsg")
    public String generateMsg(String email, String type){
        verificationCodeFactory.sendValidationInfo(email, type);
        return RespMessageUtils.SUCCESS("验证码已发送, 请注意查收!");
    }

    /**
     * 验证码校验
     * @param email 需要校验的邮箱
     * @param message 需要检验的信息
     * @return 返回验证码是否正确
     */
    @ResponseBody
    @PostMapping("/register/checkMsg")
    public String checkMsg(String email, String message, String type) {
        try {
            verificationCodeFactory.checkValidationInfo(email, message, type);
            return RespMessageUtils.SUCCESS();
        } catch (MailMessageException e) {
            log.info(e.getMessage());
            return RespMessageUtils.ERROR(e.getMessage());
        }
    }

    @PostMapping("/register")
    @ResponseBody
    public String register(User user, String repeatPwd, MultipartFile photo, String validateData){
        //验证失败，或者密码重复不正确
        if(!repeatPwd.equals(user.getPassword())) {
            return RespMessageUtils.ERROR("两次输入的密码不正确!");
        }
        if (userService.checkEmail(user.getEmail()) != null) {
            return RespMessageUtils.ERROR("这个邮箱已经被注册了!");
        }
        try {
            verificationCodeFactory.checkValidationInfo(user.getEmail(),validateData, "register");
        } catch (MailMessageException e) {
            return RespMessageUtils.ERROR(e.getMessage());
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
    public String changePwd(User user, String repeatPwd, String validateData) {
        if (!user.getPassword().equals(repeatPwd)) {
            return RespMessageUtils.ERROR("两次密码输入不符合。");
        }
        try {
            Boolean emailExist = verificationCodeFactory.checkValidationInfo(user.getEmail(), validateData, "forgetPwd");
            if (!emailExist) {
                return RespMessageUtils.ERROR("验证码错误!");
            }
        } catch (MailMessageException e) {
            return RespMessageUtils.ERROR(e.getMessage());
        }
        User newUser = userService.checkEmail(user.getEmail());
        if (newUser != null) {
            newUser.setPassword(user.getPassword());
            userService.updateOne(newUser);
            return RespMessageUtils.SUCCESS("修改成功!");
        } else {
            return RespMessageUtils.ERROR("邮箱对应的账号不存在。");
        }
    }

    @ResponseBody
    @PostMapping("/updateUser")
    public String updateUser(User user){
        log.warn("user " + user.getUserId() + "updated the personal information." + user);
        userService.updateOne(user);
        return RespMessageUtils.SUCCESS();
    }

    @ResponseBody
    @PostMapping("/updatePhoto")
    public String updatePhoto(String userId, MultipartFile photo) {
        String url = userService.updatePhoto(userId, photo);
        return RespMessageUtils.SUCCESS(url);
    }
}