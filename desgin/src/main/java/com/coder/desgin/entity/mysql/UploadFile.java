package com.coder.desgin.entity.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.coder.desgin.entity.BaseFile;
import com.coder.desgin.entity.TempFileInfoVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author coder
 * @Date 2023/2/19 10:18
 * @Description
 */

@Data
@TableName("file_tbl")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "上传文件--实体类")
public class UploadFile extends BaseFile  implements Serializable{

    @TableId(value="file_id", type = IdType.AUTO)
    private Long fileId;

    private String fileLocation;

    private String imageId;

    private String fileMd5;

    private Integer imageQuantity;

    private String fileResults;


    public UploadFile(String fileName,Integer fileSize, String fileType, String fileMd5, String imageId, String fileResults, String detectMode) {
        setFileName(fileName);
        setFileSize(fileSize);
        setFileType(fileType);
        setMode(detectMode);
        this.imageId = imageId;
        this.fileMd5 = fileMd5;
        this.fileResults = fileResults;
    }

    public UploadFile(BaseFile file) {
        super(file);
    }

    public UploadFile(TempFileInfoVO fileInfoVO) {
        setFileName(fileInfoVO.getName());
        setFileMd5(fileInfoVO.getUniqueIdentifier());
        setFileSize(Math.toIntExact(fileInfoVO.getSize()));
        setFileType(fileInfoVO.getFileType());
        setUserId(fileInfoVO.getUserId());
        setMode(fileInfoVO.getMode());
    }
}
