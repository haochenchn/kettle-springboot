package com.ch.dataclean.web;

import com.ch.dataclean.service.FileHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/fu")
public class FileHandleController extends BaseController {
    @Autowired
    private FileHandleService fileHandleService;

    /**
     * 实现文件上传
     */
    @PostMapping("/fileUpload")
    @ResponseBody
    public Object fileUpload(@RequestParam("fileName") MultipartFile file, String typeId, Model model){
        if(!file.isEmpty()){
            try {
                fileHandleService.fileUpload(file, typeId);
                return data(success(),"文件上传成功");
            } catch (Exception e) {
                e.printStackTrace();
                return data(error(),e.getMessage());
            }
        }else {
            return data(error(),"庆不要上传空文件");
        }
    }

    @RequestMapping("/toUploadStatus")
    @ResponseBody
    public Object toUploadStatus(){
        /*model.addAttribute("message", "The file is empty!");
        return "/uploadStatus";*/
        return data(success(),"这仅仅是一个测试");
    }

    @RequestMapping(value = "/testTrans")
    @ResponseBody
    public Object testTrans(){
        fileHandleService.testTrans();
        return "testTrans success";
    }

    @RequestMapping(value = "/testJob")
    @ResponseBody
    public Object testJob(){
        fileHandleService.testJob();
        return "testJob success";
    }

}
