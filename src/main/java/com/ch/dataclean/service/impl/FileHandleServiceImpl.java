package com.ch.dataclean.service.impl;

import com.ch.dataclean.common.kettle.KettleManager;
import com.ch.dataclean.service.FileHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Created by Aaron on 2018/11/18
 */
@Service(value = "fileHandleService")
public class FileHandleServiceImpl implements FileHandleService {
    @Autowired
    private KettleManager kettleManager;

    @Override
    public String fileUpload(MultipartFile file, String typeId) throws Exception {
        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize();
        System.out.println(fileName + "-->" + size);
        String path = "F:/test" ;
        File dest = new File(path + "/" + fileName);
        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        file.transferTo(dest); //保存文件

        return null;
    }




    public Object testTrans(){
        Map<String, String> mmcsMap = new HashMap<>();
        mmcsMap.put("param", "D:\\Test");
        try {
            kettleManager.callTrans("/","获取文件名列表", null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "123";
    }

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
