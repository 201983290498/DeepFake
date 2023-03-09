package com.coder.desgin.service;

import com.coder.desgin.entity.mysql.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author coder
 */
public interface ImageService {

    /**
     * 通过图片id获取图片的信息
     * @param id 文件id
     * @return 返回对应的文件
     */
    Image selectById(String id);

    /**
     * 根据md5查找相关的文件对象
     * @param md5 文件的md5码
     * @return 返回对应的文件
     */
    Image selectByMd5(String md5);

    /**
     * 添加一条文件记录,直接记录文件的本地的位置
     * @param image 图片
     * @return 插入成功之后获取Image
     */
    Image insertOne(Image image);

    /**
     * 上传一个文件到oss,并添加一条文件记录
     * @param file 文件
     * @return 返回上传记录
     * @throws FileNotFoundException file本地文件不存在
     */
    Image insertOneByFile(File file) throws FileNotFoundException;


    /**
     * 上传文件
     * @param file 文件流
     * @return 返回上传成功的文件地址
     * @throws IOException 报错
     */
    Image insertOneByFile(MultipartFile file) throws IOException;

    /**
     * 插入一条需要上传文件的记录
     * @param fileName 文件名
     * @param inputStream 文件输入流
     * @return 返回响应插入的文件类
     */
    Image insertOne(String fileName, InputStream inputStream);

    /**
     * 根据imageId 删除某个image记录
     * @param imageId 删除图片的图片id
     */
    void deleteById(String imageId);

    /**
     * @param urls 图片的地址
     * @Description 根据图片地址删除图片本地和服务器上的记录
     */
    void deleteByUrl(List<String> urls);
}
