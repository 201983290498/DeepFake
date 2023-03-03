package com.coder.desgin.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
}