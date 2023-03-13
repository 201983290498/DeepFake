package com.coder.desgin.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coder.desgin.entity.mysql.UploadFile;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author coder
 * @Date 2023/2/19 13:58
 * @Description
 */
@Mapper
@CacheNamespace(blocking = true)
public interface FileDao extends BaseMapper<UploadFile> {

    @Select("select c.* from (select detect_id from project_tbl ${ew.customSqlSegment}) a left join project_file_tbl b on a.detect_id = b.detect_id left join file_tbl c on b.file_id = c.file_id ${w2.customSqlSegment}")
    List<UploadFile> selectListCondition(@Param("ew") QueryWrapper<UploadFile> wrapper, @Param("w2") QueryWrapper<UploadFile> wrapper2);
}
