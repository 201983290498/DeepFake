package com.coder.desgin.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.desgin.entity.mysql.DetectProject;
import com.coder.desgin.entity.mysql.DetectRecord;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author coder
 */
@Mapper
@CacheNamespace(blocking = true)
public interface DetectProjectDao extends BaseMapper<DetectProject> {

    /**
     *  查询所有记录
     * @param page 分页插件
     * @param queryWrapper 查询参数
     * @return 返回用户记录
     */
    @Select("select a.*, c.file_name as detect_file, c.file_id from project_tbl a left join project_file_tbl b on a.detect_id = b.detect_id left join file_tbl c on b.file_id = c.file_id ${ew.customSqlSegment} order by a.create_time desc")
    IPage<DetectRecord> selectRecords(Page<DetectRecord> page, @Param(Constants.WRAPPER) Wrapper queryWrapper);

}