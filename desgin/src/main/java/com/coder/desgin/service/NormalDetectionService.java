package com.coder.desgin.service;

import com.coder.desgin.entity.NormalDetectionFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface NormalDetectionService {

    String detectZip(NormalDetectionFile file, HttpServletRequest request) throws IOException;

    String detectImg(NormalDetectionFile file, HttpServletRequest request);
}
