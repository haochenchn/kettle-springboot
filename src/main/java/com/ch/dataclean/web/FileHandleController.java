package com.ch.dataclean.web;

import com.ch.dataclean.service.FileHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/fu")
public class FileHandleController {
    @Autowired
    private FileHandleService fileHandleService;

    /**
     * 实现文件上传
     */
    @PostMapping("/fileUpload")
    public String fileUpload(@RequestParam("fileName") MultipartFile file, String typeId, Model model){
        String message = "success";
        if(file.isEmpty()){
            message = "The file is empty!";
        }else {
            try {
                fileHandleService.fileUpload(file, typeId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("message",message);
        return "/uploadStatus";
    }

    @GetMapping("/toUploadStatus")
    public String toUploadStatus(Model model){
        model.addAttribute("message", "The file is empty!");
        return "/uploadStatus";
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
