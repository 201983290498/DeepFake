package com.coder.desgin.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OssServiceImplTest {

    @Autowired
    OssServiceImpl ossService;

    @Test
    void uploadFile() throws FileNotFoundException {
        String filePath = "C:\\Users\\coder\\AppData\\Local\\Temp\\tomcat-docbase.8080.1312784715417620577\\detectFile\\a4896c\\2.jpg";
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        String s = ossService.uploadFile(file.getName(), inputStream);
        System.out.println(s);
    }

    @Test
    void downloadFile() throws IOException {
        InputStreamReader inputStreamReader = ossService.downloadFile("");
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        LinkedList<String> results = new LinkedList<String>();
        while ((line = reader.readLine()) != null) {
            line = line.endsWith("\n") ? line : line + "\n";
            results.add(line);
        }
        System.out.println(results);
    }

    @Test
    void deleteFile() {
        ossService.deleteFile("https://deepfakedetector.oss-cn-hangzhou.aliyuncs.com/detectId1.txt");
    }
}