package com.coder.desgin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coder.common.util.*;
import com.coder.desgin.dao.FileDao;
import com.coder.desgin.dao.UserDao;
import com.coder.desgin.entity.DetectorRect;
import com.coder.desgin.entity.ImgDetectorResult;
import com.coder.desgin.entity.BaseFile;
import com.coder.desgin.entity.TempFileInfoVO;
import com.coder.desgin.entity.mysql.Image;
import com.coder.desgin.entity.mysql.UploadFile;
import com.coder.desgin.entity.mysql.User;
import com.coder.desgin.mq.producer.JavaEmailProducer;
import com.coder.desgin.mq.producer.RecordProducer;
import com.coder.desgin.mq.producer.RedisProducer;
import com.coder.desgin.service.FileService;
import com.coder.desgin.service.ImageService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * @Author coder
 * @Date 2022/11/3 15:28
 * @Description
 */
@Data
@Slf4j
@Service
public class FileServiceImpl implements FileService {
    private final HttpUtil httpUtil;

    private final FileDao fileDao;

    private final RedisUtil redisUtil;


    private final ImageService imageService;

    public final JavaEmailProducer emailAsynHandler;
    private final RecordProducer recordProducer;
    private final RedisProducer redisAsynHandler;

    private final UserDao userdao;

    @Value("${oss.server.url.prefix}")
    private String urlPrefix;

    public FileServiceImpl(HttpUtil httpUtil, FileDao fileDao, RedisUtil redis, ImageService imageService, JavaEmailProducer javaEmail, RecordProducer recordProducer, RedisProducer redisAsynHandler, UserDao userdao) {
        this.httpUtil = httpUtil;
        this.fileDao = fileDao;
        this.redisUtil = redis;
        this.imageService = imageService;
        this.emailAsynHandler = javaEmail;
        this.recordProducer = recordProducer;
        this.redisAsynHandler = redisAsynHandler;
        this.userdao = userdao;
    }

    @Override
    public List<ImgDetectorResult> detectFile(String filePath, String detectMode){
        // 打包参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("path", filePath);
        // url 使用默认参数
        JSONObject jsonObject = httpUtil.sendPost(null, params, detectMode, false);
        log.info(jsonObject.toString());
        List<ImgDetectorResult> analysisResult = getAnalysisResult(jsonObject);
        log.info("Analysis result: path".concat(filePath).concat(";  detections:").concat(analysisResult.toString()));
        return analysisResult;
    }

    public List<ImgDetectorResult> detectFile2(BaseFile file) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("file", JSON.toJSONString(file));
        JSONObject jsonObject = httpUtil.sendPost(null, params, file.getMode(), true);
        log.info(jsonObject.toString());
        List<ImgDetectorResult> analysisResult = getAnalysisResult(jsonObject);
        log.info("Analysis result: ".concat("detections:").concat(analysisResult.toString()));
        return analysisResult;
    }

    @Override
    public String detectZip(BaseFile file, Boolean sendFile, HttpServletRequest request) throws IOException{
        // zipPath 解压文件夹的路径
        User user = userdao.selectOneOnlyAccout(file.getUserId());
        if (sendFile) { // 本地不处理
            String zipPath = ZipUtil.base64ToFile(file.getBase64(), file.getFileName(), request);
            UploadFile uploadFile = new UploadFile(file);
            uploadFile.setFileMd5(Md5Util.getMd5(new File(zipPath))); // 文件的md5
            String result = JSON.toJSONString(detectFile2(file));
            String detectTextPath = mkResultText(result, "./" );
            emailAsynHandler.sendEmailMsg("file", user.getEmail(), detectTextPath);
            // 1. 检测结果上传
            Image image = imageService.insertOneByFile(new File(detectTextPath));
            insertRecord("on computer server", uploadFile, image.getImageUrl());
            return image.getImageUrl();
        } else {
            String zipPath = ZipUtil.base64ToFile(file.getBase64(), file.getFileName(), request);
            UploadFile uploadFile = new UploadFile(file);
            uploadFile.setFileMd5(Md5Util.getMd5(new File(zipPath))); // 文件的md5
            return detectZip(zipPath, uploadFile, user.getEmail());
        }
    }

    @Override
    public String detectZip(@NotNull String filePath, UploadFile file, String userEmail) throws IOException{
        String unZipPath = filePath.substring(0, filePath.lastIndexOf("."));
        unZipPath = unZipPath + "/" + unZipPath.substring(Math.max(unZipPath.lastIndexOf("\\"), filePath.lastIndexOf("\\")) + 1);
        unZipPath = ZipUtil.unZip(filePath, unZipPath);
        String result = JSON.toJSONString(detectFile(unZipPath, file.getMode()));
        String detectTextPath = mkResultText(result, unZipPath.substring(0, unZipPath.lastIndexOf('\\')));
        emailAsynHandler.sendEmailMsg("file", userEmail, detectTextPath);
        // 1. 检测结果上传
        Image image = imageService.insertOneByFile(new File(detectTextPath));
        insertRecord(filePath, file, image.getImageUrl());
        return image.getImageUrl();
    }

    @Override
    public ImgDetectorResult detectImg(@NotNull BaseFile file, Boolean sendFile, HttpServletRequest request) {
        String filePath = ImageUtil.generateImage(file.getFileName(), file.getBase64(), request);
        assert filePath != null;
        log.info("解压文件地址为:".concat(filePath));
        List<ImgDetectorResult> analysisResult;
        if (sendFile) {
            analysisResult = detectFile2(file);
        } else {
            analysisResult = detectFile(filePath, file.getMode());
        }
        try {
            // 1. 默认项目添加, 文件上传,添加
            UploadFile uploadFile = new UploadFile(file);
            uploadFile.setFileMd5(Md5Util.getMd5(new File(filePath)));
            insertRecord(filePath, uploadFile, JSON.toJSONString(analysisResult.get(0)));
            return analysisResult.get(0);
        } catch (Exception e) {
            log.warn("OSS或者mysql出现异常, 无法上传文件");
            return analysisResult.get(0);
        }
    }

    /**
     * 检测项目
     *
     * @param fileInfoVO    文件信息
     * @param filePath 文件的路径
     */
    @Override
    public void detectProject(TempFileInfoVO fileInfoVO, String filePath) throws FileNotFoundException {
        String fileResults;
        if (filePath.endsWith("zip")) {
            String unZipPath = filePath.substring(0, filePath.lastIndexOf("."));
            unZipPath = unZipPath + "/" + unZipPath.substring(Math.max(unZipPath.lastIndexOf("\\"), filePath.lastIndexOf("\\")) + 1);
            unZipPath = ZipUtil.unZip(filePath, unZipPath);
            String result = JSON.toJSONString(detectFile(unZipPath, fileInfoVO.getMode()));
            String detectTextPath = mkResultText(result, unZipPath.substring(0, unZipPath.lastIndexOf('\\')));
            // 检测结果上传, 获取成为url
            Image image = imageService.insertOneByFile(new File(detectTextPath));
            fileResults = image.getImageUrl();
        } else {
            List<ImgDetectorResult> imgDetectorResults = detectFile(filePath, fileInfoVO.getMode());
            fileResults = JSON.toJSONString(imgDetectorResults.get(0));
        }
        recordProducer.sendRecordMsg(filePath, fileInfoVO, fileResults);
    }

    /**
     * 传输文件版本的检测
     *
     * @param uploadFile 需要上传的文件
     */
    @Override
    public void detectProjectWithFile(UploadFile uploadFile, TempFileInfoVO fileInfoVO, String filePath) throws FileNotFoundException {
        String fileResults;
        if (uploadFile.getFileName().endsWith("zip")) {
            String result = JSON.toJSONString(detectFile2(uploadFile));
            String detectTextPath = mkResultText(result, "./");
            // 检测结果上传, 获取成为url
            Image image = imageService.insertOneByFile(new File(detectTextPath));
            fileResults = image.getImageUrl();
        } else {
            List<ImgDetectorResult> imgDetectorResults = detectFile2(uploadFile);
            fileResults = JSON.toJSONString(imgDetectorResults.get(0));
        }
        recordProducer.sendRecordMsg(filePath, fileInfoVO, fileResults);
    }

    /**
     * 发送文件给后端 检测
     *
     * @param uploadFile 需要上传的文件
     * @return 返回检测文本的地址
     */
    @Override
    public String detectZipWithFile(UploadFile uploadFile, String email) throws FileNotFoundException {
        String result = JSON.toJSONString(detectFile2(uploadFile));
        String detectTextPath = mkResultText(result, "./");
        emailAsynHandler.sendEmailMsg("file", email, detectTextPath);
        // 1. 检测结果上传
        Image image = imageService.insertOneByFile(new File(detectTextPath));
        insertRecord("on computer server", uploadFile, image.getImageUrl());
        return image.getImageUrl();
    }

    @Override
    public List<ImgDetectorResult> getAnalysisResult(JSONObject jsonMap){
        List<ImgDetectorResult> results = new ArrayList<>();
        JSONObject imgs= (JSONObject) jsonMap.get("anchors");
        for(Map.Entry<String, Object>entry: imgs.entrySet()){
            ImgDetectorResult result = new ImgDetectorResult();
            result.setImageName(entry.getKey());
            result.setImageUrl(urlPrefix + result.getImageName().hashCode() + "=" + result.getImageName());
            Map<String, JSONArray> detections = (Map<String, JSONArray>) entry.getValue();
            for(Map.Entry<String, JSONArray> detection: detections.entrySet()){
                JSONArray array = detection.getValue();
                DetectorRect rect = new DetectorRect();
                rect.setType(array.getString(4));
                rect.setConfidence(array.getDouble(5));
                rect.setX1(array.getDouble(0));
                rect.setY1(array.getDouble(1));
                rect.setX2(array.getDouble(2));
                rect.setY2(array.getDouble(3));
                result.getRects().add(rect);
            }
            results.add(result);
        }
        return results;
    }

    @Override
    public String checkMd5(String fileName, String md5, String mode) {
        String result = (String)redisUtil.get(md5+mode);
        if (StringUtils.isEmpty(result)) {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("file_md5", md5);
            wrapper.eq("mode", mode);
            wrapper.or();
            wrapper.eq("file_name", fileName);
            wrapper.eq("mode", mode);
            List<UploadFile> list = fileDao.selectList(wrapper);
            if (list.size() != 0) {
                UploadFile file = list.get(0);
                redisAsynHandler.sendMsg(md5+mode, JSON.toJSONString(file), null);
                String results = JSON.toJSONString(file);
//                if (results.startsWith("http")) {
//                    sendEmailByUrl(result);
//                }
                return results;
            } else {
                return null;
            }
        } else {
            return result;
        }
    }

    /**
     * 根据url发送过检测文本
     * @param result 检测结果
     */
    private void sendEmailByUrl(String result) {
        String filePath = "./" + UUID.randomUUID().toString().substring(0,10) + ".txt";
        String line;
        try {
            Writer writer = new FileWriter(filePath);
            InputStreamReader inputStreamReader = imageService.downloadByUrl(result);
//            emailAsynHandler.sendEmailMsg("file", user.getEmail(), new File(filePath).getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 插入一条记录, 更新所有的数据库
     *
     * @param filePath 文件名称
     * @param baseFile 文件基本信息
     * @param detectResult   检测结果json
     * @throws FileNotFoundException 检测文本生成出现问题
     */
    @Override
    public void insertRecord(String filePath, UploadFile baseFile, String detectResult) throws FileNotFoundException {
        recordProducer.sendRecordMsg(filePath,baseFile, detectResult);
    }

    /**
     * 插入检测记录和用户Id, 如果用户不存在相关检录，则创建默认记录，然后插入项目记录对应
     *
     * @param fileId 文件Id
     * @param userId 用户Id
     */
    @Override
    public void insertRecord(Long fileId, String userId, String mode) {
        recordProducer.sendRecordMsg(fileId, userId, mode);
    }

    /**
     * todo 目前只实现检测结果的文本 将检测结果转换成检测文件 or 返回图片的存储位置,需要返回注册邮箱。
     * @param imgsJson image的检测结果
     * @return 生成md5文件
     */
    public String mkResultText(String imgsJson, String dirPath){
        List<ImgDetectorResult> imgs = JSON.parseArray(imgsJson, ImgDetectorResult.class);
        String savePath = dirPath + "\\detect.txt";
        Writer writer;
        LinkedList<String> results = new LinkedList<>();
        try {
            writer = new FileWriter(savePath);
            for(ImgDetectorResult img: imgs){
                for(DetectorRect rect: img.getRects()){
                    results.add(img.getImageName() + ":    " + rect.toString() + "\n");
                }
            }
            results.sort(String::compareTo);
            for(String line: results){
                writer.write(line);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return savePath;
    }
}
