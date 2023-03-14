package com.coder.desgin.controller;

import com.coder.common.util.ZipUtil;
import com.coder.desgin.entity.TempFileInfoVO;
import com.coder.desgin.entity.TempChunkInfo;
import com.coder.desgin.entity.mysql.UploadFile;
import com.coder.desgin.service.FileService;
import com.coder.common.util.RespMessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
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
@Api(tags ={"2.大文件相关接口"})
@RestController
@RequestMapping("/bigfile")
@Slf4j
public class BigFileController {

    /**
     * 大文件相关文件夹
     */
    @Value("${bigFile.temp}")
    private String bigFileDirTemp;

    @Value("${bigFile.directory}")
    private String bigFileDir;

    @Value("${model.location}")
    private Boolean sendFile;

    private final FileService fileService;

    public BigFileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 上传文件块
     * @param chunk 文件块信息
     * @return 返回上传是否正确字符窜
     */
    @ApiOperation("文件分页上传")
    @PostMapping("/chunk")
    public String uploadChunk(TempChunkInfo chunk, HttpServletRequest request) throws IOException {
        String contentPath = request.getSession().getServletContext().getRealPath("/");
        MultipartFile file = chunk.getUpfile();
        String uid = chunk.getIdentifier();
        Integer chunkNumber = chunk.getChunkNumber();
        log.info("上传文件标识符uid:" + uid);
        File outFile = new File(contentPath+bigFileDirTemp+File.separator+uid, chunkNumber + ".part");
        InputStream inputStream = file.getInputStream();
        FileUtils.copyInputStreamToFile(inputStream, outFile);
        log.info("大文件输出地址为" + outFile);
        return RespMessageUtils.SUCCESS();
    }

    @GetMapping("/chunk")
    @ApiOperation("查文件--检测分片是否已经上传")
    public Object checkChunk(TempFileInfoVO chunk) {
        // todo 维护一张大文件表格
        return chunk;
    }

    @ApiOperation("文件分页合并--首页检测加合并")
    @PostMapping("/mergeFile")
    public String mergeFile(@RequestBody TempFileInfoVO fileInfoVO, HttpServletRequest request){
        String contentPath = request.getSession().getServletContext().getRealPath("/");
        File bigFileDirPath = new File(contentPath+bigFileDir);
        log.warn("Merging file exists in:" + bigFileDirPath);
        String finalFilePath = contentPath + bigFileDir + File.separator + fileInfoVO.getName();
        if (!bigFileDirPath.exists()) {
            bigFileDirPath.mkdirs();
        }
        String md5 = fileInfoVO.getUniqueIdentifier();
        System.out.println("merge:"+ md5);
        File fileDir = new File(contentPath+bigFileDirTemp+File.separator+ md5);
        try {
            if (fileDir.isDirectory()) {
                File[] subFiles = fileDir.listFiles();
                if (subFiles != null && subFiles.length > 0) {
                    File partFile = new File(finalFilePath);
                    for (int i = 1; i <= subFiles.length; i++) {
                        File s = new File(contentPath+bigFileDirTemp+File.separator+md5, i + ".part");
                        FileOutputStream destTempfos = new FileOutputStream(partFile, true);
                        FileUtils.copyFile(s,destTempfos);
                        destTempfos.close();
                    }
                    FileUtils.deleteDirectory(fileDir);
                }
            }
            // todo location 怎么弄 还有本地地址存在一些问题
            UploadFile uploadFile = new UploadFile(fileInfoVO);
            String textUrl;
            if (sendFile) {
                String s = ZipUtil.fileToBase64(finalFilePath);
                uploadFile.setBase64(s);
                textUrl = fileService.detectZipWithFile(uploadFile);
            }
            else {
                textUrl = fileService.detectZip(finalFilePath, uploadFile);
            }
            return RespMessageUtils.SUCCESS(textUrl);
        }
        catch (Exception e) {
            log.warn(e.getMessage());
            return RespMessageUtils.ERROR("服务器出错");
        }
    }

    @ApiOperation("文件分页合并--添加项目文件合并")
    @PostMapping("/projectFileMergeFile")
    public String mergeFileOnly(@RequestBody TempFileInfoVO fileInfoVO, HttpServletRequest request){
        String contentPath = request.getSession().getServletContext().getRealPath("/");
        File bigFileDirPath = new File(contentPath+bigFileDir);
        log.warn("Merging file exists in:" + bigFileDirPath);
        String finalFilePath = contentPath + bigFileDir + File.separator + fileInfoVO.getName();
        if (!bigFileDirPath.exists()) {
            bigFileDirPath.mkdirs();
        }
        String md5 = fileInfoVO.getUniqueIdentifier();
        System.out.println("merge:"+ md5);
        File fileDir = new File(contentPath+bigFileDirTemp+File.separator+ md5);
        try {
            if (fileDir.isDirectory()) {
                File[] subFiles = fileDir.listFiles();
                if (subFiles != null && subFiles.length > 0) {
                    File partFile = new File(finalFilePath);
                    for (int i = 1; i <= subFiles.length; i++) {
                        File s = new File(contentPath+bigFileDirTemp+File.separator+md5, i + ".part");
                        FileOutputStream destTempfos = new FileOutputStream(partFile, true);
                        FileUtils.copyFile(s,destTempfos);
                        destTempfos.close();
                    }
                    FileUtils.deleteDirectory(fileDir);
                }
            }
            finalFilePath.replace("/", "");
            if (sendFile) {
                UploadFile uploadFile = new UploadFile(fileInfoVO);
                String s = ZipUtil.fileToBase64(finalFilePath);
                uploadFile.setBase64(s);
                fileService.detectProjectWithFile(uploadFile, fileInfoVO, finalFilePath);
            }
            else {
                fileService.detectProject(fileInfoVO, finalFilePath);

            }
            fileService.detectProject(fileInfoVO, finalFilePath);
            return RespMessageUtils.SUCCESS();
        }
        catch (Exception e) {
            log.warn(e.getMessage());
            return RespMessageUtils.ERROR("文件合并时服务器出错。");
        }
    }

}
