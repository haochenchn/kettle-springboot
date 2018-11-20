package com.ch.dataclean.service;

import com.ch.dataclean.model.DataDeptModel;

import java.util.List;

/**
 * Description:
 * Created by Aaron on 2018/11/19
 */
public interface DataDeptService {
    /**
     * 根据id查找数据属于的部门
     * @param id
     * @return
     * @throws Exception
     */
    DataDeptModel getDeptById(long id) throws Exception;

    /**
     * 查找数据部门列表
     * @return
     * @throws Exception
     */
    List<DataDeptModel> getDepts() throws Exception;
}
