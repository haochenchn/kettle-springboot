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
}
