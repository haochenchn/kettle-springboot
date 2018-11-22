package com.ch.dataclean.service.impl;

import com.ch.dataclean.common.exception.BaseException;
import com.ch.dataclean.common.kettle.KettleManager;
import com.ch.dataclean.common.page.Page;
import com.ch.dataclean.common.util.CommonUtils;
import com.ch.dataclean.common.util.Constant;
import com.ch.dataclean.common.util.FileUtil;
import com.ch.dataclean.dao.DaoSupport;
import com.ch.dataclean.model.DataDeptModel;
import com.ch.dataclean.model.DataFormatCheckResultVo;
import com.ch.dataclean.model.FileModel;
import com.ch.dataclean.service.DataDeptService;
import com.ch.dataclean.service.FileHandleService;
import com.ch.dataclean.service.FormatCheckService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Description:
 * Created by Aaron on 2018/11/18
 */
@Service(value = "fileHandleService")
public class FileHandleServiceImpl implements FileHandleService {
    Logger logger = Logger.getLogger(FileHandleServiceImpl.class);

    @Value("${kettle.templates.path}")
    private String DATA_TEMPLATES_PATH;

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Autowired
    private KettleManager kettleManager;

    @Autowired
    private DataDeptService dataDeptService;
    @Autowired
    private FormatCheckService formatCheckService;

    @Override
    public FileModel fileUpload(MultipartFile file, String deptId, String desc) throws Exception {
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith("zip")) {
            throw new BaseException(fileName+ "文件格式不是zip文件");
        }

        DataDeptModel dept = dataDeptService.getDeptById(deptId);
        if(null == dept){
            throw new BaseException("数据部门不存在！");
        }
        FileModel pFile = new FileModel();
        int size = (int) file.getSize();
        System.out.println(fileName + "-->" + size);
        String extension = fileName.substring(fileName.lastIndexOf(".")+1);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");//设置日期格式
        //得到文件的相对路径
        StringBuilder relativePath = new StringBuilder().append(Constant.UPLOAD_PATH).append(dept.getName()).append("/").append(fileName)
                .append("-").append(df.format(new Date())).append("/");

        pFile.setName(fileName);
        pFile.setPath(relativePath.toString());
        pFile.setExtension(extension);
        pFile.setFileSize(CommonUtils.formatDouble(size/1024, 2));
        pFile.setDeptId(deptId);
        pFile.setDeptName(dept.getName());
        pFile.setPid("0"); //zip文件父id为0
        pFile.setImportStatus(1);
        pFile.setFileDesc(desc);
        this.saveFileModel(pFile);
        List<FileModel> cFiles = this.getUnzipedFiles(file, pFile);
        if(null == cFiles || 0 == cFiles.size()){
            throw new BaseException(fileName + "文件解压失败！");
        }
        //******************************数据格式检查**************************************
        DataFormatCheckResultVo result = formatCheckService.formatCheck(deptId, cFiles);
        if(result.isResult()){ //格式校验通过
            //***************************保存文件***************************************
            this.writeToFile(cFiles);
            //***************************单独启一个线程跑kettle*************************
            //线程内要用到的值，设为final
            final String t_relative_path = relativePath.toString();
            final String t_job_path = dept.getKettleJobPath();
            final String t_job_name = dept.getKettleJobName();
            final String t_fileid = pFile.getId();
            new Thread(()->{
                //执行kettle脚本
                Map<String,String> variables = new HashMap<>();
                //传入文件解压出来后的路径
                variables.put("param",FileUtil.getBasePath(t_relative_path));
                try{
                    boolean re = kettleManager.callJob(t_job_path,t_job_name,variables, null);
                    if(re){
                        //更新状态为导入成功
                        this.updateFileStatus(t_fileid,Constant.IMPORT_STATUS_SUCCESS,"");
                    }
                }catch (Exception e){
                    //更新状态为导入失败
                    try {
                        this.updateFileStatus(t_fileid,Constant.IMPORT_STATUS_ERRORIMPORT,"");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }).start();
        }else { //校验不通过
            this.updateFileStatus(pFile.getId(),Constant.IMPORT_STATUS_ERRORFORMAT, result.getRemark());
            throw new BaseException(result.getRemark());
        }

        return pFile;
    }

    /**
     * 对zip类型的文件进行解压，文件保存到输入流
     */
    public List<FileModel> getUnzipedFiles(MultipartFile file, FileModel pFile) throws Exception {
        List<FileModel> cFiles = new ArrayList<>();
        String fileName = null;
        // 对文件进行解析
        ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream(), Charset.forName("GBK"));
        BufferedInputStream bs = new BufferedInputStream(zipInputStream);
        ZipEntry zipEntry;
        byte[] bytes = null;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) { // 获取zip包中的每一个zip file entry
            if(zipEntry.isDirectory()){
                continue;
            }
            fileName = zipEntry.getName();
            Assert.notNull(fileName, "压缩文件中子文件的名字格式不正确");
            FileModel cFile = new FileModel(); //压缩包内的文件
            int size = (int) zipEntry.getSize();
            bytes = new byte[size];
            cFile.setName(fileName);
            cFile.setPath(pFile.getPath());
            cFile.setExtension(fileName.substring(fileName.lastIndexOf(".")+1));
            cFile.setFileSize(CommonUtils.formatDouble(size/1024, 2));
            cFile.setDeptId(pFile.getDeptId());
            cFile.setDeptName(pFile.getDeptName());
            cFile.setPid(pFile.getId()); //zip文件父id为0

            bs.read(bytes, 0, size);
            InputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            cFile.setFileInputstream(byteArrayInputStream);
            cFiles.add(cFile);
        }
        return cFiles;
    }
    /**
     * 将文件信息保存到数据库
     * @param fileModel
     * @throws Exception
     */
    public void saveFileModel(FileModel fileModel) throws Exception{
        dao.save("file.insertFile", fileModel);
    }
    /**
     * 更新文件导入状态
     * @param id
     * @param status
     * @param desc
     * @throws Exception
     */
    public void updateFileStatus(String id, int status, String desc) throws Exception{
        Map<String,Object> param = new HashMap<>();
        param.put("id",id);
        param.put("importStatus",status);
        param.put("importDesc",desc);
        dao.update("file.updateFileStatus", param);
    }
    /**
     * 写入到文件
     */
    public void writeToFile(List<FileModel> fileModels) throws Exception{
        for(FileModel fileModel : fileModels){
            String relativePath = fileModel.getPath() + fileModel.getName();
            File destFile = FileUtil.getFile(FileUtil.getBasePath(relativePath));
            FileOutputStream fos = new FileOutputStream(destFile);
            InputStream fis = fileModel.getFileInputstream();
            byte[] buf = new byte[fis.available()];
            //读取源文件
            fis.read(buf);
            //将缓冲区内的数据写入到目标文件
            fos.write(buf);
            fos.flush();
            fos.close();
            fis.close();
            this.saveFileModel(fileModel);
        }
    }

    @SuppressWarnings("unchecked")
    public Page getFiles(String search, String pid, Page page) throws Exception{
        Map<String, String> param = new HashMap<>();
        param.put("search", search);
        param.put("pid", pid);
        page.queryForPage(dao.getSqlSessionTemplate(), "file.findFiles", param, page);
        return page;
    }

    public ResponseEntity<byte[]> downloadTemplateFile(String deptId) throws Exception {
        DataDeptModel dept = dataDeptService.getDeptById(deptId);
        if(null == dept){
            throw new BaseException("数据部门不存在！");
        }
        String filePath = DATA_TEMPLATES_PATH+ "/" + dept.getFileTemplate();
        File file = new File(filePath);
        if(!file.exists()){
            throw new BaseException("模板文件不存在");
        }
        return FileUtil.downloadFile(dept.getFileTemplate(), file);
    }


    /*===========================以下是测试========================*/
    public Object testTrans(){
        Map<String, String> mmcsMap = new HashMap<>();
        mmcsMap.put("param", "D:/Test");
        try {
            kettleManager.callTrans("/","获取文件名列表", mmcsMap, null);
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
