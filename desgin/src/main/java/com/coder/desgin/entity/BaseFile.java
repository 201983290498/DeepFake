package com.coder.desgin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author coder
 * @Date 2022/10/30 14:05
 * @Description 单文件, 主要是图片和单一地压缩文件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "基础本文类--实体类")
public class BaseFile {

    private String fileType="image";
    private String fileName;

    private Integer fileSize;

    @TableField(exist = false)
    private String base64;

    private String mode;

    /**
     * 文件归属
     */
    @TableField(exist = false)
    private String userId="default";

    public BaseFile(BaseFile file) {
        this.fileType = file.fileType;
        this.fileName = file.fileName;
        this.fileSize = file.fileSize;
        this.base64 = file.base64;
        this.mode = file.mode;
        this.userId = file.userId;
    }
}
