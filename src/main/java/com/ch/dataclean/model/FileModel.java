package com.ch.dataclean.model;

import com.ch.dataclean.common.util.CommonUtils;
import org.apache.ibatis.type.Alias;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Description:文件上传实体
 * Created by Aaron on 2018/11/18
 */
@Alias("file")
public class FileModel extends BaseModel implements Serializable {
    private final long serialVersionUID = 13846812783412684L;

    private String name; //文件名
    private String path; //文件路径
    private String extension; //文件后缀
    private double fileSize; //文件大小
    private String pid; //父文件（如果是zip文件，其pid为0，否则为zip文件的id）
    private String deptId; //数据部门
    private String deptName;
    private String fileDesc; //描述
    private int importStatus; //数据导入状态，(1正在导入2数据格式不对3已导入4导入失败)
    private String importDesc;

    private InputStream fileInputstream; //解压后每个文件的输入流

    public FileModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }

    public int getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(int importStatus) {
        this.importStatus = importStatus;
    }

    public String getImportDesc() {
        return importDesc;
    }

    public void setImportDesc(String importDesc) {
        this.importDesc = importDesc;
    }

    public InputStream getFileInputstream() {
        return this.fileInputstream;
    }


    public void setFileInputstream(InputStream fileInputstream) {
        this.fileInputstream = fileInputstream;
    }
}
