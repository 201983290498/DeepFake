package com.coder.desgin.entity.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public class DetectProject  implements Serializable {

    @TableId(value="detect_id", type=IdType.AUTO)
    protected Long detectId;

    @TableField(value="projectLevel")
    protected String projectLevel;

    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @TableField(value = "create_time")
    protected Date createTime;

    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @TableField(value = "finish_time")
    protected Date finishTime;

    @TableField(value = "project_name")
    protected String projectName;

    @TableField(value = "user_id")
    protected String userId;

    public DetectProject(Date finishTime, String projectName) {
        this.finishTime = finishTime;
        this.projectName = projectName;
    }
}
