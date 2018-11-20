package com.ch.dataclean.web;

import com.ch.dataclean.model.DataDeptModel;
import com.ch.dataclean.model.FileModel;
import com.ch.dataclean.service.DataDeptService;
import com.ch.dataclean.service.FileHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping(value = "/file")
public class FileHandleController extends BaseController {
    @Autowired
    private FileHandleService fileHandleService;
    @Autowired
    private DataDeptService dataDeptService;

    /**
     * 文件上传页面
     */
    @RequestMapping(value = "/toUpload")
    public String toUpload(Model model){
        try {
            List<DataDeptModel> depts = dataDeptService.getDepts();
            List<FileModel> files = fileHandleService.getFilesByPid(0L);
            model.addAttribute("files", files);
            model.addAttribute("depts", depts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "upload";
    }

    /**
     * 实现文件上传
     */
    @RequestMapping(value = "/fileUpload")
    @ResponseBody
    public Object fileUpload(@RequestParam("file") MultipartFile file, String deptId){
        if(!file.isEmpty()){
            try {
                fileHandleService.fileUpload(file, deptId);
                return data(success(),"文件上传成功");
            } catch (Exception e) {
                e.printStackTrace();
                return data(error(),e.getMessage());
            }
        }else {
            return data(error(),"请不要上传空文件");
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
