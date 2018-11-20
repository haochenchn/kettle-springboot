package com.ch.dataclean.service;

import com.ch.dataclean.model.FileModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface FileHandleService {
    /**
     * 文件上传
     */
    FileModel fileUpload(MultipartFile file, String deptId) throws Exception;

    /**
     * 获取文件列表
     * @return
     * @throws Exception
     */
    List<FileModel> getFilesByPid(long pid) throws Exception;

    Object testTrans();
    Object testJob();
}
