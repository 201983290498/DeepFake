package com.coder.desgin.service;

import com.coder.desgin.entity.mysql.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author coder
 */
public interface ImageService {

    /**
     * 通过图片id获取图片的信息
     * @param id
     * @return
     */
    Image selectById(String id);

    /**
     * 插入一张图片
     * @param image 图片
     * @return 插入成功之后获取Image
     */
    Image insertOne(Image image);


    /**
     * 上传文件
     * @param file 文件流
     * @return 返回上传成功的文件地址
     * @throws IOException 报错
     */
    Image insertOne(MultipartFile file) throws IOException;
}
