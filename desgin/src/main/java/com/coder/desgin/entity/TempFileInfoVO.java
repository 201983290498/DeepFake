package com.coder.desgin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文件模型参数
 * @author YouZ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempFileInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 附件编号
     */
    private String id;
    /**
     * 附件类型
     */
    private String fileType;
    /**
     * 附件名称
     */
    private String name;
    /**
     * 附件总大小
     */
    private Long size;
    /**
     * 附件地址
     */
    private String relativePath;
    /**
     * 附件MD5标识
     */
    private String uniqueIdentifier;
    /**
     * 附件所属项目ID
     */
    private String refProjectId;
}