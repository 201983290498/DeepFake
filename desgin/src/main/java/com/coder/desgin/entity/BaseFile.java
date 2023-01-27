package com.coder.desgin.entity;

import lombok.Data;

/**
 * @Author coder
 * @Date 2022/10/30 14:05
 * @Description 单文件, 主要是图片和单一地压缩文件
 */
@Data
public class BaseFile {

    private String type;
    private String name;
    private Integer size;
    private String base64;


}
