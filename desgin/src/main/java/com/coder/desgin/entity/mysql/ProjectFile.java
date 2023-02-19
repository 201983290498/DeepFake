package com.coder.desgin.entity.mysql;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author coder
 * @Date 2023/2/19 11:25
 * @Description
 */
@Data
@TableName("project_file_tbl")
public class ProjectFile implements Serializable {

    @TableField(value="detect_id")
    private String detectId;

    @TableField(value="file_id")
    private String fileId;
}
