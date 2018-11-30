package com.ch.dataclean.service;

import com.ch.dataclean.common.page.Page;
import com.ch.dataclean.model.FileModel;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface FileHandleService {
    /**
     * 文件上传
     */
    FileModel fileUpload(MultipartFile file, String deptId, String desc) throws Exception;

    /**
     * 获取文件列表
     * @return
     * @throws Exception
     */
    Page getFiles(String search, String pid, Page page) throws Exception;

    /**
     * 下载模板文件
     * @param deptId 部门id
     */
    void downloadTemplateFile(HttpServletResponse response, String deptId) throws Exception;

    Object testTrans();
    Object testJob();
}
