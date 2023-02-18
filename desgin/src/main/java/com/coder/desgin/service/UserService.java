package com.coder.desgin.service;

import com.coder.desgin.entity.mysql.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author coder
 */
public interface UserService {

    /**
     * 插入用户
     * @param entity 新用户
     * @return 返回新的用户
     */
    User insertUser(User entity);

    /**
     * 插入用户, 同时上传图片
     * @param entity 需要注册的用户
     * @param photo 用户的头像
     * @return 返回是否注册成功
     * @throws Exception 注册可能失败, 主要是头像上传可能存在问题
     */
    User insertUser(User entity, MultipartFile photo) throws Exception;

    /**
     * 检查用户
     * @param user 待检查的用户, 至少填充username和password属性
     * @return 如果存在返回用户信息
     */
    User checkAccount(User user);

    /**
     * 查看账户是否存在
     * @param account 账户
     * @return 返回相应的用户
     */
    User isExist(String account);

    /**
     * 查看邮箱是否存在, 每个邮箱只能注册一次
     * @param email 邮箱地址
     * @return 返回对应的用户
     */
    User checkEmail(String email);

    /**
     * 更新用户
     * @param user 用户对象
     */
    void updateOne(User user);

    /**
     * 更新用户的头像
     * @param userId 用户的id
     * @param photo 头像
     * @return
     */
    String updatePhoto(String userId, MultipartFile photo);
}
