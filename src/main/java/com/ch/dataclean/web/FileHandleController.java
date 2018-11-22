package com.ch.dataclean.web;

import com.ch.dataclean.common.page.Page;
import com.ch.dataclean.common.util.CommonUtils;
import com.ch.dataclean.model.DataDeptModel;
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
//            List<FileModel> files = fileHandleService.getFiles(null,"0");
//            model.addAttribute("files", files);
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
    public Object fileUpload(@RequestParam(value = "file", required = false) MultipartFile file, String deptId, String desc){
        if(null != file && !file.isEmpty()){
            try {
                CommonUtils.formCheck(deptId,"请选择部门");
                fileHandleService.fileUpload(file, deptId, desc);
                return data(success(),"文件上传成功");
            } catch (Exception e) {
                e.printStackTrace();
                return data(error(),e.getMessage());
            }
        }else {
            return data(error(),"请不要上传空文件");
        }
    }

    /**
     * 根据pid查询文件目录
     * search可以是文件名、部门名或描述
     */
    @RequestMapping(value = "/getFiles")
    public Object getFiles(String search, String pid, Page page, Model model){
        try {
             //Page page = new Page();
            fileHandleService.getFiles(search, pid,page);
            model.addAttribute("page",page);
            model.addAttribute("rows",page.getRows());
            return "upload::table_refresh";
        } catch (Exception e) {
            e.printStackTrace();
            return error();
        }
    }
    /**
     * 根据pid查询文件目录
     * search可以是文件名、部门名或描述
     */
    @RequestMapping(value = "/getFilesc")
    public Object getFilesc(String search, String pid, Page page, Model model){
        try {
            fileHandleService.getFiles(search, pid,page);
            model.addAttribute("page",page);
            model.addAttribute("rows",page.getRows());
            return "upload::tablec_refresh";
        } catch (Exception e) {
            e.printStackTrace();
            return error();
        }
    }

    /**
     * 根据pid查询文件目录
     * search可以是文件名、部门名或描述
     */
    @RequestMapping(value = "/getFilesJson")
    @ResponseBody
    public Object getFilesJson(String search, String pid, Page page){
        try {
            fileHandleService.getFiles(search, pid,page);
            return data(success(), page);
        } catch (Exception e) {
            e.printStackTrace();
            return data(error(),"查询失败");
        }
    }

    /**
     * 模板下载
     * @param deptId
     * @return
     */
    @RequestMapping(value = "/download")
    public Object downloadTempFile(String deptId){
        try {
            return fileHandleService.downloadTemplateFile(deptId);
        } catch (Exception e) {
            e.printStackTrace();
            return data(error(),"没有该部门的模板文件");
        }
    }

    @RequestMapping("/test")
    @ResponseBody
    public Object test(){
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

    /*
    <tr>
                    <td colspan="2"><p th:text="'Total:' + ${page.pages}">Total page</p></td>
                    <td><a th:href="@{/blog/pagehelper(page=1)}">first</a></td>
                    <td><a th:href="@{/blog/pagehelper(page=${page.nextPage})}">next</a></td>
                    <td><a th:href="@{/blog/pagehelper(page=${page.prePage})}">prex</a></td>
                    <td><a th:href="@{/blog/pagehelper(page=${page.lastPage})}">last</a></td>
                </tr>

    * */

}
