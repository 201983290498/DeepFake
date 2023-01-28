package com.coder.desgin.entity;

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
    @TableId(type= IdType.ASSIGN_UUID)
    @TableField("user_id")
    private String id;

    /**
     * 邮箱验证
     */
    @TableField("email")
    private String email;

    /**
     * 用户名
     */
    @TableField("password")
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
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


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

    /**
     * Instantiates a new User.
     *
     * @param id           the id
     * @param email        the email
     * @param username     the username
     * @param password     the password
     * @param imageId      the image id
     * @param createTime   the creat time
     */
    public User(String id, String email, String username, String password, String imageId, Date createTime) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.imageId = imageId;
        this.createTime = createTime;
    }

    /**
     * Instantiates a new User.
     *
     * @param username   the username
     * @param createTime the creat time
     */
    public User(String username, Date createTime, String imageId) {
        this.username = username;
        this.createTime = createTime;
        this.imageId = imageId;
    }

}
