package com.coder.desgin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.desgin.entity.mysql.User;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author coder
 * @Date 2023/1/27 22:05
 * @Description
 */

@CacheNamespace(blocking = true)
@Mapper
public interface UserDao extends BaseMapper<User> {

    /**
     * 查找用户信息
     * @param account 用户输入的账号
     * @return 返回在表格中查找到的用户
     */
    @Select("select a.*, b.image_url as imageUrl from usr_tbl a, image b where a.image_id = b.image_id and (username = #{param1} or user_id = #{param1} or emial=#{param1}})")
    User selectOne(String account);

    /**
     * 根据邮箱查找用户
     * @param email 邮箱
     * @return 邮箱对应的用户
     */
    User selectOneByEmail(String email);
}
