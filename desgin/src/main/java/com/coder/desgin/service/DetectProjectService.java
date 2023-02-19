package com.coder.desgin.service;

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
     * @return 返回个人的检测行为
     */
    List<DetectRecord> selectRecordsByUserId(String userId, Integer pageNum);

    /**
     * 搜索所有的行为
     * @param pageNum 分页的页号
     * @return 返回所有的检测行为
     */
    List<DetectRecord> selectAllRecords(Integer pageNum);
}
