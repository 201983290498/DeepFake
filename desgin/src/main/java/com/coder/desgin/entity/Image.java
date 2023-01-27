package com.coder.desgin.entity;

import com.coder.desgin.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * @Author coder
 * @Date 2023/1/27 23:02
 * @Description
 */
@Data
@AllArgsConstructor
public class Image implements Serializable {
    public static final Integer PAGE_SIZE = 15;
    /**
     * 图片的id
     */
    @TableField(column = "img_id")
    private String imageId;

    /**
     * 图片的具体字节
     */
    @TableField(column = "img_bytes")
    private byte[] imageBytes;

    /**
     * 图片的连接地址
     */
    @TableField(column = "img_url")
    private String imageUrl;

    /**
     * 根据图片的具体内容随机生成一个图片
     * @param imageBytes 图片的字节
     */
    private Image(byte[] imageBytes){
        this.imageBytes = imageBytes;
        this.imageId = UUID.randomUUID().toString().substring(0, 25);
    }

    /**
     * 创建图片
     * @param imageBytes 图片的字节内容
     * @param imageUrl 图片的url地址
     */
    private Image(byte[] imageBytes, String imageUrl){
        this.imageBytes = imageBytes;
        this.imageUrl = imageUrl;
        this.imageId = UUID.randomUUID().toString().substring(0, 25);
    }
}
