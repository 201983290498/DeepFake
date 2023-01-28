package com.coder.desgin.service.impl;

import com.coder.desgin.dao.ImageDao;
import com.coder.desgin.entity.mysql.Image;
import com.coder.desgin.service.ImageService;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageDao imageDao;

    public ImageServiceImpl(ImageDao imageDao) {
        this.imageDao = imageDao;
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
}
