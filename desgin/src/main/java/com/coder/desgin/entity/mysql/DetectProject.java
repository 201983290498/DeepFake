package com.coder.desgin.entity.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date; // todo 与sql.date的区别

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

    protected String projectLevel;

    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    protected Date createTime;

    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    protected Date finishTime;

    protected String projectName;

    /**
     * 检测项目的用户id
     */
    protected String userId;

    /**
     * 检测模式
     */
    protected String mode;

    public DetectProject(Date finishTime, String projectName, String userId, String mode) {
        this.finishTime = finishTime;
        this.projectName = projectName;
        if(userId == null) {
            userId = "default";
        }
        this.userId = userId;
        this.mode = mode;
    }
}
