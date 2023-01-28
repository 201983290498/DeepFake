package com.coder.desgin.service;

import com.coder.desgin.entity.mysql.Image;

public interface ImageService {

    /**
     * 通过图片id获取图片的信息
     * @param id
     * @return
     */
    Image selectById(String id);

    Image insertOne(Image image);
}
