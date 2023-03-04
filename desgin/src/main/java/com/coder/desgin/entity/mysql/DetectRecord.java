package com.coder.desgin.entity.mysql;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @Author coder
 * @Date 2023/2/19 10:14
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DetectRecord extends DetectProject implements Serializable{

    private String detectFile;

    private String fileId;

    private String fileLocation;

    private String fileResults;

}
