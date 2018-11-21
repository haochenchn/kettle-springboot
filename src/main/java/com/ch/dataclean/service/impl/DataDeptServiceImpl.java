package com.ch.dataclean.service.impl;

import com.ch.dataclean.dao.DaoSupport;
import com.ch.dataclean.model.DataDeptModel;
import com.ch.dataclean.service.DataDeptService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 * Created by Aaron on 2018/11/19
 */
@Service(value = "dataDeptService")
public class DataDeptServiceImpl implements DataDeptService {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Override
    public DataDeptModel getDeptById(String id) throws Exception {
        return (DataDeptModel) dao.findForObject("dataDept.findDeptById",id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DataDeptModel> getDepts() throws Exception {
        return (List<DataDeptModel>) dao.findForList("dataDept.findAllDepts", null);
    }
}
