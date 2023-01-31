package com.coder.desgin.service.impl;

import com.coder.desgin.dao.ImageDao;
import com.coder.desgin.entity.mysql.Image;
import com.coder.desgin.service.ImageService;
import com.coder.desgin.service.OssService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @Override
    public Image insertOne(MultipartFile file) throws IOException {
        String url = ossService.uploadFile(file);
        return insertOne(new Image(url));
    }
}
