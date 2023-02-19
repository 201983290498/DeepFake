package com.coder.desgin.entity.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;

/**
 * @Author coder
 * @Date 2023/2/19 9:15
 * @Description
 */
@Data
@TableName("project_tbl")
public class DetectProject  implements Serializable {

    @TableId(value="detect_id", type=IdType.AUTO)
    private Long detectId;

    @TableField(value="projectLevel")
    private String projectLevel;

    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @TableField(value = "create_time")
    private Date createTime;

    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @TableField(value = "finish_time")
    private Date finishTime;

    @TableField(value = "project_name")
    private String projectName;

    @TableField(value = "user_id")
    private String userId;
}
