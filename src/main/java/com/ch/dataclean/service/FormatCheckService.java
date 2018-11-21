package com.ch.dataclean.service;

import com.ch.dataclean.model.DataFormatCheckResultVo;
import com.ch.dataclean.model.FileModel;

import java.util.List;

/**
 * Description:文件校验业务层
 * Created by Aaron on 2018/11/20
 */
public interface FormatCheckService {
    /**
     * 校验文件数据格式是否符合要求
     * @param deptId 部门id
     * @param files 上传文件集合
     * @return
     * @throws Exception
     */
    DataFormatCheckResultVo formatCheck(String deptId, List<FileModel> files) throws Exception;
}
