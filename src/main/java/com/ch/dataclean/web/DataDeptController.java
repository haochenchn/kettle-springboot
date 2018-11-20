package com.ch.dataclean.web;

import com.ch.dataclean.model.DataDeptModel;
import com.ch.dataclean.service.DataDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Description:
 * Created by Aaron on 2018/11/20
 */
@Controller
@RequestMapping("/dept")
public class DataDeptController extends BaseController {
    @Autowired
    private DataDeptService dataDeptService;

    @RequestMapping(value = "/getDepts")
    public Object getDepts(){
        try {
            List<DataDeptModel> depts = dataDeptService.getDepts();
            return data(success(),depts);
        } catch (Exception e) {
            e.printStackTrace();
            return error();
        }
    }
}
