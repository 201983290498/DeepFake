package com.coder.desgin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class BaseFile {

    @TableField(value="file_type")
    private String fileType="image";
    @TableField(value="file_name")
    private String fileName;

    @TableField(value="file_size")
    private Integer fileSize;

    @TableField(exist = false)
    private String base64;

    @TableField(value = "mode")
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
