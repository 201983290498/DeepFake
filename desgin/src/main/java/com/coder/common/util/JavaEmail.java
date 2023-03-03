package com.coder.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 这是一个邮件管理类，主要的功能是发送文本文件和验证码
 * The management of verification code including sending, checking and valid Message queue
 * @Author coder
 * @Date 2021 /12/1 20:20
 * @Description
 */
@Component
@Data
@Scope("singleton") // 设置单例模式
@AllArgsConstructor
@NoArgsConstructor
@PropertySource("classpath:mySetting.properties") // 加载配置文件
public class JavaEmail {

    /**
     * 发送邮件的授权邮箱
     */
    @Value("${email.emailAddress}")
    private String sendEmail;

    /**
     * 发送邮箱的授权码
     */
    @Value("${email.emailPwd}")
    private String pwd;


    /**
     * 以html文本作为邮件内容发送给用户
     * @param receiveEmail 接收邮件的邮箱
     * @param html 需要发送的html内容
     */
    public void sendHtmlMsg(String receiveEmail, String html){
        try{
            JavaMailSenderImpl sender = new JavaMailSenderImpl();
//            设置邮箱主机，如果是qq邮箱就是smtp.qq.com，网易smtp.163.com
            sender.setHost("smtp.qq.com");
//            设置编码集合
            sender.setDefaultEncoding("utf-8");
            //建立邮箱消息,我们需要以html格式发送邮件
            MimeMessage mailMessage = sender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);

//            设置收件人，寄件人和邮件的主题
            messageHelper.setTo(receiveEmail);
            messageHelper.setFrom(sendEmail);
            messageHelper.setSubject("DeepFakeApplication验证系统");

            messageHelper.setText(html,true);

            //设置发送的账号和密码，状态码
            sender.setUsername(sendEmail);
            sender.setPassword(pwd);

            Properties prop = new Properties();
            /* 让服务器去认证用户名和密码 */
            prop.put("mail.smtp.auth", "true");
            /* 连接超时时间 */
            prop.put("mail.smtp.timeout", "4000");
            sender.setJavaMailProperties(prop);
            sender.send(mailMessage);
        } catch (MessagingException e) {
            System.out.println("接收人或者寄件人邮箱错误");
            e.printStackTrace();
        }
    }

    /**
     * 使用单纯的javax发送邮件(带附件)。
     */
    public Boolean sendMessageWithFile(String receiveEmail, String file) throws MessagingException, UnsupportedEncodingException {
        Properties prop = new Properties();
        /* 让服务器去认证用户名和密码 */
        prop.put("mail.smtp.auth", "true");
        /* 连接超时时间 */
        prop.put("mail.smtp.timeout", "4000");
        prop.put("mail.smtp.host", "smtp.qq.com");
        // 根据属性创建一个邮件会话
        Session session = Session.getInstance(prop);
        // 打印调试信息
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(sendEmail);
        message.setRecipients(Message.RecipientType.TO, receiveEmail);
        message.setSubject("DeepFake检测文本");
        // 设置发送时间
        message.setSentDate(new Date());
        Multipart multipart = new MimeMultipart();
        BodyPart contentPart = new MimeBodyPart();
        contentPart.setText("DeepFakeApplication已经检测完成, 检测文本以发送在附件中");
        multipart.addBodyPart(contentPart);
        // 添加附件
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource dataSource = new FileDataSource(new File(file));
        // 添加附件内容
        messageBodyPart.setDataHandler(new DataHandler(dataSource));
        // 添加标题通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
        messageBodyPart.setFileName(MimeUtility.encodeText(dataSource.getName()));
        multipart.addBodyPart(messageBodyPart);
        // 发送带附件
        message.setContent(multipart);
        message.saveChanges();// 存储邮件信息

        // 发送邮件
        Transport transport = session.getTransport();
        // 发送邮箱和授权码
        transport.connect(sendEmail, pwd);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        return true;
    }

}
