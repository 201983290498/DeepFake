package com.coder.desgin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coder.desgin.entity.dto.DetectProjectDTO;
import com.coder.desgin.entity.mysql.DetectProject;
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

    /**
     * @param detectIds 需要删除的项目Id
     * @Description 删除项目
     */
    void deleteList(List<String> detectIds);

    /**
     *
     * @param userId 用户Id
     * @param detectIds 项目Ids
     * @return 是否是某个人
     * @Description 检验用户和项目的所属关系
     */
    boolean confirmOwnership(String userId, List<String> detectIds);

    /**
     * 条件查询用户检测记录
     * @param userId 用户Id
     * @param current 当前的查询页码
     * @param pageSize 页面的大小
     * @param field 查询的字段
     * @param value 查询字段的值
     * @param ordered 按照什么排序
     * @param orderField 排序的顺序
     * @return 返回满足条件查询的值
     */
    IPage<DetectRecord> selectSimilarRecords(String userId, Integer current, Integer pageSize, String field, String value, Boolean ordered, String orderField);

    /**
     * 更新
     * @param userId 用户Id
     * @param detectId 项目id
     * @param project 更新的实体
     */
    boolean updateById(DetectProject project);

    /**
     * 新增一个项目
     * @param project 项目的基本信息
     * @return 返回插入后的信息
     */
    DetectProject insertProject(DetectProject project);

    /**
     * 根据检测项目生成检测文件
     * @param detectId 项目Id
     */
    String postFinalResultTxt(Long detectId);

    /**
     * 查询项目的检测结果
     * @param userId 用户Id
     * @param detectId 项目Id
     * @return 检测结果的路径
     */
    String getProjectResults(String userId, Long detectId);
}
