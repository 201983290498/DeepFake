package com.coder.desgin.dao;

import com.coder.desgin.entity.User;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;

/**
 * @Author coder
 * @Date 2023/1/27 22:05
 * @Description
 */

@CacheNamespace(blocking = true)
public interface UserDao {

    /**
     * 查找用户信息
     * @param account 用户输入的账号
     * @return 返回在表格中查找到的用户
     */
    @Results(id = "user")
    @Select("select * from tbl_user where username = #{param1} or id = #{#param1}")
    User selectOne(String account);


}
