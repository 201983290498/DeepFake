package com.coder.desgin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coder.common.util.RespMessageUtils;
import com.coder.desgin.entity.mysql.DetectRecord;
import com.coder.desgin.service.DetectProjectService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * todo 做一个资源拦截的工作
 * @Author coder
 * @Date 2023/2/20 0:04
 * @Description
 */
@Data
@Slf4j
@Controller
@RequestMapping("/detectProject")
public class DetectProjectController {

    private final DetectProjectService projectService;

    public DetectProjectController(DetectProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/records")
    @ResponseBody
    public String getAllDetectionRecords(String userId, Integer pageNum, Integer pageSize) {
        log.info(userId + "正在查询所有个人检测记录");
        if (pageNum == null){
            pageNum = 1;
        }
        IPage<DetectRecord> detectRecordIPage = projectService.selectRecordsByUserId(userId, pageNum, pageSize);
        return RespMessageUtils.SUCCESS(detectRecordIPage);
    }
}
