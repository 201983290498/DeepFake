package com.coder.desgin.entity;

import lombok.Data;

/**
 * @author Pengfei Yue
 * @ClassName NormalDecFile
 * @date 2022/11/8
 * @description 普通检测上传文件Bean
 */
@Data
public class NormalDetectionFile extends BaseFile {

    /**
     * 普通检测类型：copymove、splicing、general
     */
    private String detectType;
}
