package com.coder.desgin.entity.mysql;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import java.io.Serializable;
import java.util.Arrays;

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
     * 图片的具体字节
     */
    @TableField(value = "image_bytes", jdbcType = JdbcType.BLOB, insertStrategy= FieldStrategy.NOT_NULL)
    private byte[] imageBytes;

    /**
     * 图片的连接地址
     */
    @TableField(value = "image_url")
    private String imageUrl;

    /**
     * 根据图片的具体内容随机生成一个图片
     * @param imageBytes 图片的字节
     */
    public Image(byte[] imageBytes){
        this.imageBytes = imageBytes;
    }

    /**
     * 创建图片
     * @param imageBytes 图片的字节内容
     * @param imageUrl 图片的url地址
     */
    public Image(byte[] imageBytes, String imageUrl){
        this.imageBytes = imageBytes;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageId='" + imageId + '\'' +
                ", imageBytes=" + Arrays.toString(imageBytes) +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
