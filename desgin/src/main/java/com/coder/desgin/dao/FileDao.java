package com.coder.desgin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.desgin.entity.mysql.File;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author coder
 * @Date 2023/2/19 13:58
 * @Description
 */
@Mapper
@CacheNamespace(blocking = true)
public interface FileDao extends BaseMapper<File> {

}
