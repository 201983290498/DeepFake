package com.coder.desgin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.desgin.entity.mysql.Image;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author coder
 */
@Mapper
@CacheNamespace(blocking = true)
public interface ImageDao extends BaseMapper<Image> {

}
