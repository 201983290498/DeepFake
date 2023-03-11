package com.coder.desgin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coder.common.util.*;
import com.coder.desgin.dao.FileDao;
import com.coder.desgin.entity.DetectorRect;
import com.coder.desgin.entity.ImgDetectorResult;
import com.coder.desgin.entity.BaseFile;
import com.coder.desgin.entity.TempFileInfoVO;
import com.coder.desgin.entity.mysql.Image;
import com.coder.desgin.entity.mysql.UploadFile;
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

    @Value("${oss.server.url.prefix}")
    private String urlPrefix;

    public FileServiceImpl(HttpUtil httpUtil, FileDao fileDao, RedisUtil redis, ImageService imageService, JavaEmailProducer javaEmail, RecordProducer recordProducer, RedisProducer redisAsynHandler) {
        this.httpUtil = httpUtil;
        this.fileDao = fileDao;
        this.redisUtil = redis;
        this.imageService = imageService;
        this.emailAsynHandler = javaEmail;
        this.recordProducer = recordProducer;
        this.redisAsynHandler = redisAsynHandler;
    }
    @Override
    public List<ImgDetectorResult> detectFile(String filePath, String detectMode){
        // 打包参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("path", filePath);
        // url 使用默认参数
        JSONObject jsonObject = httpUtil.sendPost(null, params, detectMode);
        log.info(jsonObject.toString());
        List<ImgDetectorResult> analysisResult = getAnalysisResult(jsonObject);
        log.info("Analysis result: path".concat(filePath).concat(";  detections:").concat(analysisResult.toString()));
        return analysisResult;
    }

    @Override
    public String detectZip(BaseFile file, HttpServletRequest request) throws IOException{
        // zipPath 解压文件夹的路径
        String zipPath = ZipUtil.base64ToFile(file.getBase64(), file.getFileName(), request);
        UploadFile uploadFile = new UploadFile(file);
        uploadFile.setFileMd5(Md5Util.getMd5(new File(zipPath))); // 文件的md5
        return detectZip(zipPath, uploadFile);
    }

    @Override
    public String detectZip(@NotNull String filePath, UploadFile file) throws IOException{
        String unZipPath = filePath.substring(0, filePath.lastIndexOf("."));
        unZipPath = unZipPath + "/" + unZipPath.substring(Math.max(unZipPath.lastIndexOf("\\"), filePath.lastIndexOf("\\")) + 1);
        unZipPath = ZipUtil.unZip(filePath, unZipPath);
        String result = JSON.toJSONString(detectFile(unZipPath, file.getMode()));
        String detectTextPath = mkResultText(result, unZipPath.substring(0, unZipPath.lastIndexOf('\\')));
        emailAsynHandler.sendEmailMsg("file", "1023668958@qq.com", detectTextPath);
        // 1. 检测结果上传
        Image image = imageService.insertOneByFile(new File(detectTextPath));
        insertRecord(filePath, file, image.getImageUrl());
        return image.getImageUrl();
    }

    @Override
    public ImgDetectorResult detectImg(@NotNull BaseFile file, HttpServletRequest request) {
        String filePath = ImageUtil.generateImage(file.getFileName(), file.getBase64(), request);
        assert filePath != null;
        log.info("解压文件地址为:".concat(filePath));
        List<ImgDetectorResult> analysisResult = detectFile(filePath, file.getMode());
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
                return JSON.toJSONString(file);
            } else {
                return null;
            }
        } else {
            return result;
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
