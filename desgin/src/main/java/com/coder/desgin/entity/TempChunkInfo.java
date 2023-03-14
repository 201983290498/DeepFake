package com.coder.desgin.entity;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author coder
 * @Date 2023/01/09 15:46
 * @Description 1.分片文件信息
 *              2.使用transient关键字不序列化某个变量注意读取的时候，读取数据的顺序一定要和存放数据的顺序保持一致
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "文件分片类--响应类")
public class TempChunkInfo implements Serializable{
    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 当前文件块，从1开始
     */
    private Integer chunkNumber;

    /**
     * 每块大小
     */
    private Long chunkSize;
    /**
     * 当前分块大小
     */
    private Long currentChunkSize;

    /**
     * 总大小
     */
    private Long totalSize;

    /**
     * 文件标识
     */
    private String identifier;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 相对路径
     */
    private String relativePath;

    /**
     * 总块数
     */
    private Integer totalChunks;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 块内容
     */
    private transient MultipartFile upfile;

}
