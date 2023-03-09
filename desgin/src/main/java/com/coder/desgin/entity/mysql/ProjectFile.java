package com.coder.desgin.entity.mysql;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author coder
 * @Date 2023/2/19 11:25
 * @Description
 */
@Data
@TableName("project_file_tbl")
@ApiModel(value = "项目文件类--实体类")
public class ProjectFile implements Serializable {

    private Long detectId;

    private Long fileId;

    public ProjectFile(Long detectId, Long fileId) {
        this.detectId = detectId;
        this.fileId = fileId;
    }
}
