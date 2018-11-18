package com.whhx.dataclean.web;

import com.whhx.dataclean.common.kettle.KettleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/fu")
public class FileUploadController {
    @Autowired
    private KettleManager kettleManager;

    @GetMapping("toUploadStatus")
    public String toUploadStatus(Model model){
        model.addAttribute("message", "The file is empty!");
        return "/uploadStatus";
    }

    @RequestMapping(value = "/test")
    public Object test(){
        Map<String, String> mmcsMap = new HashMap<>();
        mmcsMap.put("param", "D:\\Test");
        try {
            kettleManager.callTrans("/","获取文件名列表", null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "123";
    }

    @RequestMapping(value = "/testJob")
    public Object testJob(){
        Map<String, String> mmcsMap = new HashMap<>();
        mmcsMap.put("param", "D:/Test");
        try {
            kettleManager.callJob("/","testJob1", mmcsMap, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "123";
    }

}
