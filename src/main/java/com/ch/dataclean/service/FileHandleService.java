package com.ch.dataclean.service;

import org.springframework.web.multipart.MultipartFile;


public interface FileHandleService {
    /**
     * 文件上传
     */
    String fileUpload(MultipartFile file, String typeId) throws Exception;


    Object testTrans();
    Object testJob();
}
