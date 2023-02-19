package com.coder.desgin.service;

import com.coder.desgin.entity.mysql.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
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
     * 上传一个文件
     * @param file 文件
     * @return 返回上传记录
     * @throws FileNotFoundException file本地文件不存在
     */
    Image insertOne(File file) throws FileNotFoundException;


    /**
     * 上传文件
     * @param file 文件流
     * @return 返回上传成功的文件地址
     * @throws IOException 报错
     */
    Image insertOne(MultipartFile file) throws IOException;

    /**
     * 更新用户的头像
     * @param imageId 图片id
     * @param photo 头像
     * @throws IOException 输入输出错误
     */
    String updateUserPhoto(String imageId, MultipartFile photo) throws IOException;
}
