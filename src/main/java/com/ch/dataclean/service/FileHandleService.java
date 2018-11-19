package com.ch.dataclean.service;

import com.ch.dataclean.model.FileModel;
import org.springframework.web.multipart.MultipartFile;


public interface FileHandleService {
    /**
     * 文件上传
     */
    FileModel fileUpload(MultipartFile file, String deptId) throws Exception;


    Object testTrans();
    Object testJob();
}
