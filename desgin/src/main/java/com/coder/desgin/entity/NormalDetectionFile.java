package com.coder.desgin.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Pengfei Yue
 * @ClassName NormalDecFile
 * @date 2022/11/8
 * @description 普通检测上传文件Bean
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "普通检测文件类--响应类")
public class NormalDetectionFile extends BaseFile {

    /**
     * 普通检测类型：copymove、splicing、general
     */
    private String detectType;
}
