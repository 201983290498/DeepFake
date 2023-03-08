package com.coder.desgin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.desgin.entity.mysql.ProjectFile;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author coder
 * @Date 2023/2/19 14:01
 * @Description
 */
@Mapper
@CacheNamespace(blocking = true)
public interface ProjectFileDao extends BaseMapper<ProjectFile> {
}
