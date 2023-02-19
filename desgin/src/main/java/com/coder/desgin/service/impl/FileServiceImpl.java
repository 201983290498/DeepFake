package com.coder.desgin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coder.common.util.*;
import com.coder.desgin.dao.DetectProjectDao;
import com.coder.desgin.dao.FileDao;
import com.coder.desgin.dao.ProjectFileDao;
import com.coder.desgin.entity.DetectorRect;
import com.coder.desgin.entity.ImgDetectorResult;
import com.coder.desgin.entity.BaseFile;
import com.coder.desgin.entity.mysql.DetectProject;
import com.coder.desgin.entity.mysql.Image;
import com.coder.desgin.entity.mysql.ProjectFile;
import com.coder.desgin.entity.mysql.UploadFile;
import com.coder.desgin.service.FileService;
import com.coder.desgin.service.ImageService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.sql.Date;
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

    private final DetectProjectDao projectDao;

    private final ImageService imageService;

    public final ProjectFileDao projectFileDao;

    public FileServiceImpl(HttpUtil httpUtil, FileDao fileDao, RedisUtil redis, DetectProjectDao projectDao, ImageService imageService, ProjectFileDao projectFileDao) {
        this.httpUtil = httpUtil;
        this.fileDao = fileDao;
        this.redisUtil = redis;
        this.projectDao = projectDao;
        this.imageService = imageService;
        this.projectFileDao = projectFileDao;
    }

//     todo 因为没有中间文件, 所以没有md5, 添加中间文件
    @Override
    public String detectZip(BaseFile file, HttpServletRequest request) throws FileNotFoundException {
        // zipPath 解压文件夹的路径
        String unZipPath = ZipUtil.base64ToFile(file.getBase64(), file.getFileName(), request);
        String result = detectDir(unZipPath);
        String detectTextPath = mkResultText(result, unZipPath.substring(0, unZipPath.lastIndexOf('\\')));
        Image image = imageService.insertOne(new File(detectTextPath));
        insertRecord(unZipPath, file, image.getImageUrl());
        return image.getImageUrl();
    }

    @Override
    public String detectZip(@NotNull String filePath){
        String unZipPath = filePath.substring(0, filePath.lastIndexOf("."));
        unZipPath = unZipPath + "/" + unZipPath.substring(Math.max(unZipPath.lastIndexOf("/"), filePath.lastIndexOf("\\")) + 1);
        unZipPath = ZipUtil.unZip(filePath, unZipPath);
       return detectDir(unZipPath);
    }


    @Override
    public String detectDir(String dir){
        // 打包参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("path", dir);
        // url 使用默认参数
        JSONObject jsonObject = httpUtil.sendPost(null, params);
        log.info(jsonObject.toString());
        List<ImgDetectorResult> analysisResult = getAnalysisResult(jsonObject);
        log.info("Analysis result: path".concat(dir).concat(";  detections:").concat(analysisResult.toString()));
        return JSON.toJSONString(analysisResult);
    }

    @Override
    public ImgDetectorResult detectImg(@NotNull BaseFile file, HttpServletRequest request) {
        // filePath 解压的文件地址
        String filePath = ImageUtil.generateImage(file.getFileName(), file.getBase64(), request);
        log.info("解压文件地址为:".concat(filePath));
        // 打包参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("path", filePath);
        // url使用默认参数
        JSONObject jsonObject = httpUtil.sendPost(null, params);
        log.info(jsonObject.toString());
        List<ImgDetectorResult> analysisResult = getAnalysisResult(jsonObject);
        log.info("Analysis result: path".concat(filePath).concat(";  detections:").concat(analysisResult.toString()));
        // 将探索记录上传 1. 默认项目添加, 文件上传,添加
        try {
            insertRecord(filePath, file, analysisResult.get(0));
            return analysisResult.get(0);
        } catch (FileNotFoundException e) {
            log.warn("OSS或者mysql出现异常, 无法上传文件");
            return analysisResult.get(0);
        }
    }

    @Override
    public List<ImgDetectorResult> getAnalysisResult(JSONObject jsonMap){
        List<ImgDetectorResult> results = new ArrayList<>();
        JSONObject imgs= (JSONObject) jsonMap.get("anchors");
        for(Map.Entry<String, Object>entry: imgs.entrySet()){
            ImgDetectorResult result = new ImgDetectorResult();
            result.setImageName(entry.getKey());
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
    public String checkMd5(String md5) {
        String result = (String)redisUtil.get(md5);
        if (StringUtils.isEmpty(result)) {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("file_md5", md5);
            UploadFile file = fileDao.selectOne(wrapper);
            if (file != null) {
                return file.getFileMd5();
            } else {
                return null;
            }
        } else {
            return result;
        }
    }


    @Override
    public void insertRecord(String filePath, BaseFile file, Object result) throws FileNotFoundException {
        DetectProject detectProject = new DetectProject(new Date(System.currentTimeMillis()),"deepfake image detection");
        File uploadFile = new File(filePath);
        String md5 = Md5Util.getMd5(uploadFile);
        // 上传图片
        Image image = null;
        if (uploadFile.isFile()) {
            image = imageService.insertOne(uploadFile);
        } else {
            image = imageService.insertOne(new Image(filePath));
        }
        // 创建默认项目
        projectDao.insert(detectProject);
        UploadFile file1 = new UploadFile(file.getFileName(), file.getFileSize(), file.getFileType(), md5, image.getImageId(), JSON.toJSONString(result));
        // 创建对应的检测文件
        fileDao.insert(file1);
        // 创建并联条目
        projectFileDao.insert(new ProjectFile(detectProject.getDetectId(), file1.getFileId()));
        // 更新redis
        redisUtil.set(md5, JSON.toJSONString(result));
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
            results.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
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
