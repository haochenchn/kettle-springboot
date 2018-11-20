package com.ch.dataclean.model;

import org.apache.ibatis.type.Alias;

/**
 * Description:数据部门实体
 * Created by Aaron on 2018/11/19
 */
@Alias("dataDept")
public class DataDeptModel extends BaseModel {
    private String name;
    private String description;
    private String kettleJobName; //该部门对应的kettle导入job名
    private String kettleJobPath; //资源库内job相对路径，默认为/

    public DataDeptModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKettleJobName() {
        return kettleJobName;
    }

    public void setKettleJobName(String kettleJobName) {
        this.kettleJobName = kettleJobName;
    }

    public String getKettleJobPath() {
        return kettleJobPath;
    }

    public void setKettleJobPath(String kettleJobPath) {
        this.kettleJobPath = kettleJobPath;
    }
}
