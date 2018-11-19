package com.ch.dataclean.model;

/**
 * Description:数据文件格式检查VO类
 * Created by Aaron on 2018/11/19
 */
public class DataFormatCheckResultVo {
    private boolean result; //检查结果
    private String remark; //结果备注

    public DataFormatCheckResultVo() {
    }

    public DataFormatCheckResultVo(boolean result, String remark) {
        this.result = result;
        this.remark = remark;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
