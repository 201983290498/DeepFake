package com.coder.desgin.controller;

import com.coder.desgin.entity.mysql.Image;
import com.coder.desgin.service.ImageService;
import com.coder.common.util.RespMessageUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
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
    public ResponseEntity<byte[]> loginFigureImage(@PathVariable("imageId") String id){
        Image image = imageService.selectById(id);
        MultiValueMap<String,String> header = new HttpHeaders();
        header.add("Content-Disposition", "attachment;filename=figureImage.jpg");
        HttpStatus ok = HttpStatus.OK;
        return new ResponseEntity<>(image.getImageBytes(),header,ok);
    }

    /**
     * Up load image string.
     *
     * @param photo the photo
     * @return the string
     * @throws IOException the io exception
     */
    @ResponseBody
    @PostMapping("/upload")
    public String upLoadImage(MultipartFile photo) throws IOException {
        /* 传递回去上传成功的信息 */
        Image upload = imageService.insertOne(new Image(photo.getBytes()));
        return RespMessageUtils.SUCCESS(upload.getImageId());
    }
}
