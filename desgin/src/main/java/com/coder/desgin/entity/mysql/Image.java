package com.coder.desgin.entity.mysql;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;

/**
 * @Author coder
 * @Date 2023/1/27 23:02
 * @Description
 */
@Data
@AllArgsConstructor
@TableName("image")
public class Image implements Serializable {
    public static final Integer PAGE_SIZE = 15;
    /**
     * 图片的id
     */
    @TableId(value="image_id", type = IdType.ASSIGN_UUID)
    private String imageId;

    /**
     * 图片的连接地址
     */
    private String imageUrl;

    private String md5;

    /**
     * 创建图片
     * @param imageUrl 图片的url地址
     */
    public Image(String imageUrl, String md5){
        this.imageUrl = imageUrl;
        this.md5 = md5;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageId='" + imageId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
