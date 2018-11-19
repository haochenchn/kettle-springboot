package com.ch.dataclean.common.util;

import com.ch.dataclean.model.FileModel;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**  
 * 对文件或文件夹进行压缩和解压  
 *  
 */  
public class ZipUtil {   
    /**得到当前系统的分隔符*/  
//  private static String separator = System.getProperty("file.separator");   
  
    /**  
     * 添加到压缩文件中  
     * @param out  
     * @param f  
     * @param base  
     * @throws Exception  
     */  
    private void directoryZip(ZipOutputStream out, File f, String base) throws Exception {   
        // 如果传入的是目录   
        if (f.isDirectory()) {   
            File[] fl = f.listFiles();   
            // 创建压缩的子目录   
            out.putNextEntry(new ZipEntry(base + "/"));   
            if (base.length() == 0) {   
                base = "";   
            } else {   
                base = base + "/";   
            }   
            for (int i = 0; i < fl.length; i++) {   
                directoryZip(out, fl[i], base + fl[i].getName());   
            }   
        } else {   
            // 把压缩文件加入rar中   
            out.putNextEntry(new ZipEntry(base));   
            FileInputStream in = new FileInputStream(f);   
            byte[] bb = new byte[10240];   
            int aa = 0;   
            while ((aa = in.read(bb)) != -1) {   
                out.write(bb, 0, aa);   
            }   
            in.close();   
        }   
    }   
  
    /**  
     * 压缩文件  
     *   
     * @param zos  
     * @param file  
     * @throws Exception  
     */  
    private void fileZip(ZipOutputStream zos, File file) throws Exception {   
        if (file.isFile()) {   
            zos.putNextEntry(new ZipEntry(file.getName()));   
            FileInputStream fis = new FileInputStream(file);   
            byte[] bb = new byte[10240];   
            int aa = 0;   
            while ((aa = fis.read(bb)) != -1) {   
                zos.write(bb, 0, aa);   
            }   
            fis.close();   
            System.out.println(file.getName());   
        } else {   
            directoryZip(zos, file, "");   
        }   
    }   
  
    /**  
     * 解压缩文件  
     *   
     * @param zis  
     * @param file  
     * @throws Exception  
     */  
    private void fileUnZip(ZipInputStream zis, File file) throws Exception {   
        ZipEntry zip = zis.getNextEntry();   
        if (zip == null)   
            return;   
        String name = zip.getName();   
        File f = new File(file.getAbsolutePath() + "/" + name);   
        if (zip.isDirectory()) {   
            f.mkdirs();   
            fileUnZip(zis, file);   
        } else {   
            f.createNewFile();   
            FileOutputStream fos = new FileOutputStream(f);   
            byte b[] = new byte[10240];   
            int aa = 0;   
            while ((aa = zis.read(b)) != -1) {   
                fos.write(b, 0, aa);   
            }   
            fos.close();   
            fileUnZip(zis, file);   
        }   
    }   
       
    /**  
     * 根据filePath创建相应的目录  
     * @param filePath  
     * @return  
     * @throws IOException  
     */  
    private File mkdirFiles(String filePath) throws IOException{   
        File file = new File(filePath);   
        if(!file.getParentFile().exists()){   
            file.getParentFile().mkdirs();   
        }   
        file.createNewFile();   
           
        return file;   
    }   
  
    /**  
     * 对zipBeforeFile目录下的文件压缩，保存为指定的文件zipAfterFile  
     *   
     * @param zipBeforeFile     压缩之前的文件  
     * @param zipAfterFile      压缩之后的文件  
     */  
    public void zip(String zipBeforeFile, String zipAfterFile) {   
        try {   
               
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(mkdirFiles(zipAfterFile)));   
            fileZip(zos, new File(zipBeforeFile));   
            zos.close();   
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
    }   
  
    /**  
     * 解压缩文件unZipBeforeFile保存在unZipAfterFile目录下  
     *   
     * @param unZipBeforeFile       解压之前的文件  
     * @param unZipAfterFile        解压之后的文件  
     */  
    public void unZip(String unZipBeforeFile, String unZipAfterFile) {   
        try {   
            ZipInputStream zis = new ZipInputStream(new FileInputStream(unZipBeforeFile));   
            File f = new File(unZipAfterFile);   
            f.mkdirs();   
            fileUnZip(zis, f);   
            zis.close();   
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
    }

}  
