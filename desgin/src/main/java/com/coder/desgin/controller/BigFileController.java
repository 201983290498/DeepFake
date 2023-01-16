package com.coder.desgin.controller;

import com.coder.desgin.entity.TempFileInfoVO;
import com.coder.desgin.entity.TempChunkInfo;
import com.coder.desgin.service.FileService;
import com.coder.desgin.util.RespMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@PropertySource(value = "classpath:mySetting.properties")
public class BigFileController {

    /**
     * 大文件相关文件夹
     */
    @Value("${bigFile.temp}")
    private String bigFileDirTemp;

    @Value("${bigFile.directory}")
    private String bigFileDir;

    private final FileService fileService;

    public BigFileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 上传文件块
     * @param chunk 文件块信息
     * @return 返回上传是否正确字符窜
     */
    @PostMapping("/chunk")
    public String uploadChunk(TempChunkInfo chunk, HttpServletRequest request) throws IOException {
        String contentPath = request.getSession().getServletContext().getRealPath("/");
        MultipartFile file = chunk.getUpfile();
        String uid = chunk.getIdentifier();
        Integer chunkNumber = chunk.getChunkNumber();
        log.info("上传文件标识符uid:" + uid);
        // todo 需要给大文件设立单独的文件夹, 使用md5作为他们文件夹的名称
        File outFile = new File(contentPath+bigFileDirTemp+File.separator+uid, chunkNumber + ".part");
        InputStream inputStream = file.getInputStream();
        FileUtils.copyInputStreamToFile(inputStream, outFile);
        return RespMessageUtils.SUCCESS();
    }

    @GetMapping("/chunk")
    public Object checkChunk(TempFileInfoVO chunk, HttpServletResponse response) {
        return chunk;
    }

    @PostMapping("/mergeFile")
    public String mergeFile(@RequestBody TempFileInfoVO fileInfoVO, HttpServletRequest request) throws Exception{
        String contentPath = request.getSession().getServletContext().getRealPath("/");
        File bigFileDirPath = new File(contentPath+bigFileDir);
        if (!bigFileDirPath.exists()) {
            bigFileDirPath.mkdirs();
        }
        String md5 = fileInfoVO.getUniqueIdentifier();
        System.out.println("merge:"+ md5);
        File fileDir = new File(contentPath+bigFileDirTemp+File.separator+ md5);
        if (fileDir.isDirectory()) {
            File[] subFiles = fileDir.listFiles();
            if (subFiles != null && subFiles.length > 0) {
                File partFile = new File(contentPath + bigFileDir + File.separator + fileInfoVO.getName());
                for (int i = 1; i <= subFiles.length; i++) {
                    File s = new File(contentPath+bigFileDirTemp+File.separator+md5, i + ".part");
                    FileOutputStream destTempfos = new FileOutputStream(partFile, true);
                    FileUtils.copyFile(s,destTempfos);
                    destTempfos.close();
                }
                FileUtils.deleteDirectory(fileDir);
            }
        }
//        fileService.detectZip(contentPath + bigFileDir + File.separator + fileInfoVO.getName());
        return RespMessageUtils.SUCCESS();
    }
}
