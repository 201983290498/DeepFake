package com.coder.desgin.service;

import com.coder.desgin.entity.mysql.User;

public interface UserService {

    /**
     * 插入用户
     * @param entity 新用户
     * @return 返回新的用户
     */
    User insertUser(User entity);

    /**
     * 检查用户
     * @param user 待检查的用户, 至少填充username和password属性
     * @return 是否存在该用户
     */
    Boolean checkAccount(User user);

    /**
     * 查看账户是否存在
     * @param account 账户
     * @return 返回相应的用户
     */
    User isExist(String account);
}
