package com.coder.desgin.controller;

import com.coder.desgin.entity.TChunkInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

/**
 *  大文件分片上传类
 */
@Controller
public class FileController {

    private static String FILENAME = "";
    @Value("${YouZ.upload.file.path}")
    private String decryptFilePath;
    @Value("${YouZ.upload.file.temp}")
    private String decryptFilePathTemp;

    public String uploadChunk(TChunkInfo chunkInfo)
}
