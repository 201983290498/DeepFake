package com.coder.desgin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coder.desgin.entity.dto.DetectProjectDTO;
import com.coder.desgin.entity.mysql.DetectRecord;

import java.util.List;

/**
 * @author coder
 */
public interface DetectProjectService {

    /**
     * 通过用户id获取所有的检测行为
     * @param userId 用户Id
     * @param pageNum 分页的页号
     * @param pageSize 页面的大小
     * @return 返回个人的检测行为
     */
    IPage<DetectRecord> selectRecordsByUserId(String userId, Integer pageNum, Integer pageSize);

    /**
     * 搜索所有的行为
     * @param pageNum 分页的页号
     * @return 返回所有的检测行为
     */
    List<DetectRecord> selectAllRecords(Integer pageNum);

    List<DetectRecord> selectRecordsBySql(Integer pageNum);

    /**
     * 获取用户最近的检测记录
     * @param userId 用户的id
     * @param page 页数
     */
    IPage<DetectRecord> getRecentDetectedImages(String userId, Integer page);

    /**
     * 查询某人的项目记录
     * @param userId 用户id
     * @param current 当前页码
     * @param pageSize 页面大小
     * @return 返回个人项目
     */
    IPage<DetectProjectDTO> selectProjects(String userId, Integer current, Integer pageSize);

    /**
     *
     * @param userId 用户id
     * @param current 当前页
     * @param pageSize 页面大小
     * @param field 页面的字段
     * @param value 页面的价值
     * @param ordered 是否按照事件排序
     * @param orderField 排列的字段
     * @return 返回满足条件的项目
     */
    IPage<DetectProjectDTO>  selectSimilarProjects(String userId, Integer current, Integer pageSize, String field, String value, Boolean ordered, String orderField);
}
