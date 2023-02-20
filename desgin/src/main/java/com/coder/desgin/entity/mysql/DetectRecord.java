package com.coder.desgin.entity.mysql;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;


/**
 * @Author coder
 * @Date 2023/2/19 10:14
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DetectRecord extends DetectProject{

    @TableField(value = "detect_file")
    private String detectFile;

    @TableField(value="file_id")
    private String fileId;

    public DetectRecord(Long detectId, String projectLevel, Date createTime, Date finishTime, String projectName, String userId, String detectFile, String fileId) {
        super(detectId, projectLevel, createTime, finishTime, projectName, userId);
        this.detectFile = detectFile;
        this.fileId = fileId;
    }

    public DetectRecord(String detectFile, String fileId) {
        this.detectFile = detectFile;
        this.fileId = fileId;
    }

    public DetectRecord(Date finishTime, String projectName, String detectFile, String fileId) {
        super(finishTime, projectName);
        this.detectFile = detectFile;
        this.fileId = fileId;
    }
}
