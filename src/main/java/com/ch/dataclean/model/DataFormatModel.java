package com.ch.dataclean.model;

import org.apache.ibatis.type.Alias;

/**
 * Description:文件数据格式校验实体
 * Created by Aaron on 2018/11/20
 */
@Alias("dataFormat")
public class DataFormatModel extends BaseModel{
    private String typeName; //文件类型
    private long deptId;
    private String fileName; //标准文件名
    private String headArr; //表头数组（以 , 分隔）
    private int beginRow; //表头所在行
    private String extension; //后缀

    public DataFormatModel() {
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public long getDeptId() {
        return deptId;
    }

    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHeadArr() {
        return headArr;
    }

    public void setHeadArr(String headArr) {
        this.headArr = headArr;
    }

    public int getBeginRow() {
        return beginRow;
    }

    public void setBeginRow(int beginRow) {
        this.beginRow = beginRow;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
