package com.coder.desgin.entity.dto;

import com.coder.desgin.entity.mysql.DetectProject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author coder
 * @Date 2023/3/7 16:24
 * @Description 用来传递getProjects请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DetectProjectDTO extends DetectProject implements Serializable {
    private Integer imageQuantity;

}
