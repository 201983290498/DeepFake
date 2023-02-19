package com.coder.desgin.entity.mysql;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * The type User.
 *
 * @Author coder
 * @Date 2023 /1/27 20:15
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("usr_tbl")
public class User implements Serializable {

    public static final Integer PAGE_SIZE = 15;
    /**
     * 用户的id
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "user_id")
    private String userId;

    /**
     * 邮箱验证
     */
    @TableField("email")
    private String email;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 人物的头像
     */
    @TableField("image_id")
    private String imageId;

    /**
     * 用户的状态，1表示活跃有效，0表示冻结，-1表示删除
     */
    @TableField("status")
    private Integer status;

    /**
     * 入园的年龄
     */
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @TableField(value = "create_time")
    private Date createTime;


    private String imageUrl;

    private String token;

    @TableField("first_name")
    private String firstName;

    @TableField("last_name")
    private String lastName;

    @TableField("phone_number")
    private String phoneNumber;

    @TableField("company")
    private String company;

    @TableField("city")
    private String city;

    @TableField("country")
    private String country;

    @TableField("zip_code")
    private String zipCode;

    @TableField("user_introduction")
    private String userIntroduction;


    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param password the password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
