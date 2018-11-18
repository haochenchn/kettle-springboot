package com.ch.dataclean.model;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Description:
 * Created by Aaron on 2018/11/18
 */
public class FileModel implements Serializable {
    private static final long serialVersionUID = 13846812783412684L;
    String fileName; //解压后文件的名字
    String fileType; //文件类型
    InputStream fileInputstream; //解压后每个文件的输入流


    public String getFileName() {
        return this.fileName;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getFileType() {
        return this.fileType;
    }


    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    public InputStream getFileInputstream() {
        return this.fileInputstream;
    }


    public void setFileInputstream(InputStream fileInputstream) {
        this.fileInputstream = fileInputstream;
    }
}
