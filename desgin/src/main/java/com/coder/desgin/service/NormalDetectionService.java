package com.coder.desgin.service;

import com.coder.desgin.entity.NormalDetectionFile;

import javax.servlet.http.HttpServletRequest;

public interface NormalDetectionService {

    String detectZip(NormalDetectionFile file, HttpServletRequest request);

    String detectImg(NormalDetectionFile file, HttpServletRequest request);
}
