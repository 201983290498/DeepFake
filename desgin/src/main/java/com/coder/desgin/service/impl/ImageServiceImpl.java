package com.coder.desgin.service.impl;

import com.coder.desgin.dao.ImageDao;
import com.coder.desgin.entity.mysql.Image;
import com.coder.desgin.service.ImageService;
import com.coder.desgin.service.OssService;
import org.springframework.stereotype.Service;
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

    @Override
    public Image insertOne(Image image) {
        imageDao.insert(image);
        return image;
    }

    public Image insertOne(File file) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(file);
        String url = ossService.uploadFile(file.getName(), inputStream);
        Image image = new Image(url);
        imageDao.insert(image);
        return image;
    }

    @Override
    public Image insertOne(MultipartFile file) throws IOException {
        String url = ossService.uploadFile(file);
        return insertOne(new Image(url));
    }

    @Override
    public String updateUserPhoto(String imageId, MultipartFile photo) throws IOException {
        Image image = imageDao.selectById(imageId);
        ossService.deleteFile(image.getImageUrl());
        String url = ossService.uploadFile(photo);
        image.setImageUrl(url);
        imageDao.updateById(image);
        return url;
    }
}
