package com.coder.desgin.controller;

import com.coder.desgin.entity.TempFileInfoVO;
import com.coder.desgin.entity.TempChunkInfo;
import com.coder.desgin.util.RespMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *  大文件分片上传类
 * @author coder
 */
@RestController
@RequestMapping("/bigfile")
@Slf4j
public class BigFileController {

    @Value("${YouZ.upload.file.path}")
    private String decryptFilePath;
    @Value("${YouZ.upload.file.temp}")
    private String decryptFilePathTemp;


    /**
     * 上传文件块
     * @param chunk 文件块信息
     * @return
     */
    @PostMapping("/chunk")
    public String uploadChunk(@RequestBody TempChunkInfo chunk, HttpServletRequest request) throws IOException {
        MultipartFile file = chunk.getFileContent();
        String uid = chunk.getIdentifier();
        Integer chunkNumber = chunk.getChunkNumber();
        log.info("上传文件标识符uid:" + uid);
        // todo 需要给大文件设立单独的文件夹, 使用md5作为他们文件夹的名称
        File outFile = new File(decryptFilePathTemp+File.separator+uid, chunkNumber + ".part");
        InputStream inputStream = file.getInputStream();
        FileUtils.copyInputStreamToFile(inputStream, outFile);
        return RespMessageUtils.SUCCESS();
    }

    @PostMapping("/mergeFile")
    public String mergeFile(@RequestBody TempFileInfoVO fileInfoVO) throws Exception{
        String md5 = fileInfoVO.getUniqueIdentifier();
        System.out.println("merge:"+ md5);
        File fileDir = new File(decryptFilePathTemp+File.separator+ md5);
        if (fileDir.isDirectory()) {
            File[] subFiles = fileDir.listFiles();
            if (subFiles != null && subFiles.length > 0) {
                File partFile = new File(decryptFilePath + File.separator + fileInfoVO.getName());
                for (int i = 1; i <= subFiles.length; i++) {
                    File s = new File(decryptFilePathTemp+File.separator+md5, i + ".part");
                    FileOutputStream destTempfos = new FileOutputStream(partFile, true);
                    FileUtils.copyFile(s,destTempfos );
                    destTempfos.close();
                }
                FileUtils.deleteDirectory(fileDir);
            }
        }
        return RespMessageUtils.SUCCESS();
    }
}
