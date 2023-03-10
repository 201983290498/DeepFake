package com.coder.common.util.login;

import com.coder.desgin.entity.Constants;
import com.coder.desgin.entity.ValidationInfo;
import com.coder.desgin.exception.MailMessageException;
import com.coder.desgin.mq.producer.JavaEmailProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.HashMap;

/**
 * 专门管理验证码的类
 * @author coder
 */
@Component
@Scope("singleton")
public class VerificationCodeFactory {

    private final JavaEmailProducer emailAsynHandler;

    /**
     * 验证码的有效时间
     */
    @Value("${email.timeout}")
    private Long validationTimeout;
    /**
     * HashMap底层时一个红黑树(二叉排序树,按照关键字的查找效率始终是logN)
     * 目前在队列中存在的有效验证码，主要用于邮箱+功能和验证码之间的映射
     * key: email
     * value: validationinfo
     */
    private final HashMap<String,String> messageMap = new HashMap<>();

    /**
     * 消息队列，主要看验证码是否过期。如果不使用该队列，每次都需要遍历HashMap删除过时信息，不合理。
     */
    private final ArrayDeque<ValidationInfo> validationInfoQueue = new ArrayDeque<>();

    public VerificationCodeFactory(JavaEmailProducer emailHandler) {
        this.emailAsynHandler = emailHandler;
    }

    /**
     * 删除消息队列中过时的验证信息
     */
    private void clearOutdatedInfo(){
        boolean flag=true;
        while(!validationInfoQueue.isEmpty()&&flag){
            ValidationInfo validationInfo = validationInfoQueue.getFirst();
            if(System.currentTimeMillis()-validationInfo.getCreateTime()>=validationInfo.getExpireTime()){
                //状态码失效
                messageMap.remove(validationInfo.getEmail() + validationInfo.getType());
                validationInfoQueue.removeFirst();
            }else{
                flag=false;
            }
        }
    }

    /**
     * 生成验证码的html文本页面
     * @param receiveEmail 接收邮箱
     * @param validationCode 验证码
     * @return 返回生成码页面
     */
    private String generationValidationHtml(String receiveEmail, String validationCode, String type){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm::ss");
        if (Constants.REGISTER.equals(type)) {
            return  "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>"
                    +"<p style='font-size:20px;font-weight:blod;'>尊敬的："+ receiveEmail +"用户,您好</p>"
                    +"<p style='text-indent:2em;font-size:20px'>欢迎注册DeepFakeApplication，您本次的验证码是 <span style='font-size:30px; font-weight:blod; color:red;'>"+ validationCode +"</span>,10分钟之内有效，请尽快填写!</p>"
                    +"<p style='text-align:right; padding-right:20px;'> <a href='https:www.coderSimple.com' style='font-size18px;'>DeepFakeApplication团队</a></p>"
                    +"<span style='font-size:18px; float:right; margin-right:60px;'>"+ sdf.format(new Date()) +"</span></body></html>";

        } else if (Constants.FORGET_PASSWORD.equals(type)) {
            return  "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>"
                    +"<p style='font-size:20px;font-weight:blod;'>尊敬的："+ receiveEmail +"用户,您好</p>"
                    +"<p style='text-indent:2em;font-size:20px'>您的账号正在修改密码，您本次的验证码是 <span style='font-size:30px; font-weight:blod; color:red;'>"+ validationCode +"</span>,10分钟之内有效，请尽快填写!</p>"
                    +"<p style='text-align:right; padding-right:20px;'> <a href='https:www.coderSimple.com' style='font-size18px;'>DeepFakeApplication团队</a></p>"
                    +"<span style='font-size:18px; float:right; margin-right:60px;'>"+ sdf.format(new Date()) +"</span></body></html>";
        } else if (Constants.DELETE.equals(type)) {
            return "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>"
                    +"<p style='font-size:20px;font-weight:blod;'>尊敬的："+ receiveEmail +"用户,您好</p>"
                    +"<p style='text-indent:2em;font-size:20px'>您的账号删除检测项目，您本次的验证码是 <span style='font-size:30px; font-weight:blod; color:red;'>"+ validationCode +"</span>,2分钟之内有效，请尽快填写! 如果不是本人操作, 请勿泄露验证码!</p>"
                    +"<p style='text-align:right; padding-right:20px;'> <a href='https:www.coderSimple.com' style='font-size18px;'>DeepFakeApplication团队</a></p>"
                    +"<span style='font-size:18px; float:right; margin-right:60px;'>"+ sdf.format(new Date()) +"</span></body></html>";
        } else if (Constants.CONFIRM.equals(type)) {
            return "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>"
                    +"<p style='font-size:20px;font-weight:blod;'>尊敬的："+ receiveEmail +"用户,您好</p>"
                    +"<p style='text-indent:2em;font-size:20px'>您本次操作的验证码是 <span style='font-size:30px; font-weight:blod; color:red;'>"+ validationCode +"</span>,2分钟之内有效，请尽快填写! 如果不是本人操作, 请勿泄露验证码!</p>"
                    +"<p style='text-align:right; padding-right:20px;'> <a href='https:www.coderSimple.com' style='font-size18px;'>DeepFakeApplication团队</a></p>"
                    +"<span style='font-size:18px; float:right; margin-right:60px;'>"+ sdf.format(new Date()) +"</span></body></html>";
        } else {
            return "";
        }
    }

    /**
     * 对验证码进行验证
     *
     * @param email   the email
     * @param message the message
     * @return the boolean
     * @throws MailMessageException the message exception
     */
    public Boolean checkValidationInfo(String email, String message, String type){
        clearOutdatedInfo();
        String msg = messageMap.get(email+type);
        return message != null && message.equals(msg);
    }

    /**
     * 根据邮箱发送验证码
     * @param email the email
     * @return Boolean
     */
    public Boolean sendValidationInfo(String email, String type, Long expireTime){
        clearOutdatedInfo();
        if (expireTime == null) {
            expireTime = validationTimeout;
        }
        // 获取验证码，先查看是否已经存在，不存在创建一个验证码
        String msg = messageMap.get(email+type);
        if(msg==null){
            ValidationInfo validationInfo = new ValidationInfo(email, type, expireTime);
            validationInfoQueue.addLast(validationInfo);
            msg = validationInfo.getMessage();
        }
        messageMap.put(email+type, msg);
        emailAsynHandler.sendEmailMsg("html", email, generationValidationHtml(email, msg, type));
        return true;
    }
}
