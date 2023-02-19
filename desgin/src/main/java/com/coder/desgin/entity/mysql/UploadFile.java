package com.coder.desgin.entity.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.coder.desgin.entity.BaseFile;
import com.coder.desgin.entity.TempFileInfoVO;
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
public class UploadFile extends BaseFile  implements Serializable{

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

    public UploadFile(String fileName,Integer fileSize, String fileType, String fileMd5, String imageId,String fileResults) {
        setFileName(fileName);
        setFileSize(fileSize);
        setFileType(fileType);
        this.imageId = imageId;
        this.fileMd5 = fileMd5;
        this.fileResults = fileResults;
    }

    public UploadFile(String fileLocation,String fileName,Integer fileSize, String fileType, String fileMd5,String fileResults) {
        setFileName(fileName);
        setFileSize(fileSize);
        setFileType(fileType);
        this.fileLocation = fileLocation;
        this.fileMd5 = fileMd5;
        this.fileResults = fileResults;
    }

    public UploadFile(BaseFile file) {
        setFileName(file.getFileName());
        setFileSize(file.getFileSize());
        setFileType(file.getFileType());
    }

    public UploadFile(TempFileInfoVO fileInfoVO) {
        setFileName(fileInfoVO.getName());
        setFileMd5(fileInfoVO.getUniqueIdentifier());
        setFileSize(Math.toIntExact(fileInfoVO.getSize()));
        setFileType(fileInfoVO.getFileType());
    }
}
