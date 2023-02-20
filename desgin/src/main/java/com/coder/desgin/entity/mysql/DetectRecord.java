package com.coder.desgin.entity.mysql;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * @Author coder
 * @Date 2023/2/19 10:14
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DetectRecord extends DetectProject{

    @TableField(value = "detect_file")
    private String detectFile;

    @TableField(value="file_id")
    private String fileId;
}
