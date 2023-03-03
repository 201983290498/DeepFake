package com.coder.desgin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coder.common.util.Md5Util;
import com.coder.desgin.dao.ImageDao;
import com.coder.desgin.entity.mysql.Image;
import com.coder.desgin.service.ImageService;
import com.coder.desgin.service.OssService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

/**
 * @author coder
 */
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageDao imageDao;
    private final OssService ossService;

    public ImageServiceImpl(ImageDao imageDao, OssService ossService) {
        this.imageDao = imageDao;
        this.ossService = ossService;
    }

    @Override
    public Image selectById(String id) {
        return imageDao.selectById(id);
    }

    /**
     * 根据md5查找相关的文件对象
     *
     * @param md5 文件的md5码
     * @return 返回对应的文件
     */
    @Override
    public Image selectByMd5(String md5) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("md5", md5);
        return imageDao.selectOne(wrapper);
    }

    /**
     * @param image 图片
     * @return 插入成功之后获取Image
     * @Description 添加一条文件记录,直接记录文件的本地的位置, 根据md5判断本地是否已经存在该文件
     */
    @Override
    public Image insertOne(Image image) {
        Image image1 = selectByMd5(image.getMd5());
        if (image1 == null) {
            imageDao.insert(image);
            return image;
        } else {
            return image1;
        }
    }


    public Image insertOneByFile(File file) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(file);
        return insertOne(file.getName(), inputStream);
    }

    @Override
    public Image insertOneByFile(MultipartFile file) throws IOException {
        return insertOne(file.getName(), file.getInputStream());
    }

    /**
     * 因为需要重复读写, 所以弄一个暂存文件
     * @param fileName 文件名称
     * @param inputStream 文件的输入输出流
     * @return 返回插入好的图片
     * @Description 插入image的基本函数
     */
    public Image insertOne(String fileName, InputStream inputStream) {
        String filePath = "./" + UUID.randomUUID().toString().substring(0,10) + ".txt";
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream downloadFile = new FileOutputStream(filePath);
            int index;
            byte[] bytes = new byte[1024];
            while ((index = inputStream.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }
            downloadFile.close();
            inputStream.close();
            String md5 = Md5Util.getMd5(file);
            Image image = selectByMd5(md5);
            if (image != null){
                file.delete();
                return image;
            } else {
                String url = ossService.uploadFile(fileName, new FileInputStream(file));
                image =  new Image(url, md5);
                imageDao.insert(image);
                file.delete();
                return image;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据imageId 删除某个image记录
     *
     * @param imageId
     */
    @Override
    public void deleteById(String imageId) {
        Image image = imageDao.selectById(imageId);
        if (image.getImageUrl().contentEquals("http")) { // 上传的文件
            ossService.deleteFile(image.getImageUrl());
        }
        imageDao.deleteById(imageId);
    }
}
