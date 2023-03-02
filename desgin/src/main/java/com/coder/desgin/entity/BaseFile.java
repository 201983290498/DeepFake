package com.coder.desgin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @Author coder
 * @Date 2022/10/30 14:05
 * @Description 单文件, 主要是图片和单一地压缩文件
 */
@Data
public class BaseFile {

    @TableField(value="file_type")
    private String fileType="image";
    @TableField(value="file_name")
    private String fileName;

    @TableField(value="file_size")
    private Integer fileSize;

    @TableField(exist = false)
    private String base64;

    @TableField(exist = false)
    private String mode;
}
