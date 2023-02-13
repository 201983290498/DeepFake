package com.coder.desgin.controller;

import com.coder.desgin.entity.mysql.Image;
import com.coder.desgin.service.ImageService;
import com.coder.common.util.RespMessageUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author coder
 */
@Controller
@RequestMapping(value = "/images")
public class ImageController {

    private final ImageService imageService;


    /**
     * Instantiates a new Image controller.
     *
     * @param imageService the image service
     */
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Login figure image response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/search/{imageId}")
    public String loginFigureImage(@PathVariable("imageId") String id){
        Image image = imageService.selectById(id);
        return RespMessageUtils.SUCCESS(image.getImageUrl());
    }

    /**
     * Up load image string.
     *
     * @param photo the photo
     * @return the string
     */
    @ResponseBody
    @PostMapping("/upload")
    public String upLoadImage(MultipartFile photo){
        /* 传递回去上传成功的信息 */
        Image upload = null;
        try {
            upload = imageService.insertOne(photo);
        } catch (IOException e) {
            return RespMessageUtils.ERROR("文件上传失败, 请稍后尝试。");
        }
        return RespMessageUtils.SUCCESS(upload.getImageId());
    }
}
