package com.coder.desgin.entity.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.coder.desgin.entity.BaseFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author coder
 * @Date 2023/2/19 10:18
 * @Description
 */

@Data
@TableName("file_tbl")
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class File extends BaseFile  implements Serializable{

    @TableId(value="file_id", type = IdType.AUTO)
    private Long fileId;

    @TableField(value="file_location")
    private String fileLocation;

    @TableField(value="image_id")
    private String imageId;

    @TableField(value="file_md5")
    private String fileMd5;

    @TableField(value="image_quantity")
    private Integer imageQuality;

    @TableField("file_results")
    private String fileResults;

}
