package com.coder.desgin.service.impl;

import com.coder.desgin.dao.ImageDao;
import com.coder.desgin.service.ImageService;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageDao imageDao;

    public ImageServiceImpl(ImageDao imageDao) {
        this.imageDao = imageDao;
    }
}
