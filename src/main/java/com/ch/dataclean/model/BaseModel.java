package com.ch.dataclean.model;


import java.util.Date;

/**
 * Description:
 * Created by Aaron on 2018/11/19
 */
public class BaseModel {
    private long id;
    private Date createtime;
    private Date modifytime;
    private String createuser;
    private String isdelete; //删除标识（0未删除1已删除）

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }
}
