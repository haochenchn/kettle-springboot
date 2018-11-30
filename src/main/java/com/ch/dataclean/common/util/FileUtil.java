package com.ch.dataclean.common.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Aaron on 2018/11/19
 */
public class FileUtil {
    /**
     * 获得指定文件的byte数组
     * @param filePath 文件路径
     */
    public static byte[] toByteArray(String filePath) throws IOException {
        File f = new File(filePath);
        return toByteArray(f);
    }

    public static byte[] toByteArray(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

    public static byte[] getContent(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        fi.close();
        return buffer;
    }

    /**
     * NIO方式
     */
    public static byte[] toByteArray2(String filePath) throws IOException {

        File f = new File(filePath);
        if (!f.exists()) {
            throw new FileNotFoundException(filePath);
        }
        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
     */
    public static byte[] toByteArray3(String filePath) throws IOException {
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(filePath, "r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0,
                    fc.size()).load();
            System.out.println(byteBuffer.isLoaded());
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                // System.out.println("remain");
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据byte数组，生成文件
     */
    public static void getFile(byte[] bfile, String filePath,String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+"\\"+fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取文件里的内容
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String fileReadToStr(String filePath) throws Exception {
        File file = new File(filePath);//定义一个file对象，用来初始化FileReader
        FileReader reader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader
        BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存
        StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
        String s = "";
        while ((s =bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
            sb.append(s + "\n");//将读取的字符串添加换行符后累加存放在缓存中
            System.out.println(s);
        }
        bReader.close();
        String str = sb.toString();
        System.out.println(str );
        return str;
    }

    /**
     * springmvc文件下载，返回ResponseEntity
     */
    public static ResponseEntity<byte[]> downloadFile(String fileName, File file) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", convertFileName(fileName));

        return new ResponseEntity<byte[]>(FileUtil.toByteArray(file),
                headers, HttpStatus.CREATED);
    }

    /**
     * 处理文件下载时中文文件名乱码及特殊字符问题
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String convertFileName(String fileName) throws UnsupportedEncodingException {
        String convertFileName;
        convertFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("%20", "\\+").replaceAll("%28", "\\(")
                .replaceAll("%29", "\\)").replaceAll("%3B", ";").replaceAll("%40", "@").replaceAll("%23", "\\#")
                .replaceAll("%26", "\\&").replaceAll("%2C", "\\,").replaceAll("%24", "\\$")
                .replaceAll("%25", "\\%").replaceAll("%5E", "\\^").replaceAll("%3D", "\\=")
                .replaceAll("%2B", "\\+").replaceAll("%5B", "\\[").replaceAll("%5D", "\\]")
                .replaceAll("%7B", "\\{").replaceAll("%7D", "\\}");
        return convertFileName;
    }

    /**
     * 文件下载
     * @param response
     * @param filePath 文件路径
     * @param isOnLine 是否在线预览
     * @throws Exception
     */
    public static void downLoad(HttpServletResponse response, String filePath, boolean isOnLine) throws Exception {
        File f = new File(filePath);
        if (!f.exists()) {
            response.sendError(404, "文件不存在!");
            return;
        }
        BufferedInputStream bis = null;
        OutputStream out = null;
        try{
            bis = new BufferedInputStream(new FileInputStream(f));
            byte[] buf = new byte[1024];
            int len = 0;

            response.reset(); // 非常重要
            if (isOnLine) { // 在线打开方式
                URL u = new URL("file:///" + filePath);
                response.setContentType(u.openConnection().getContentType());
                response.setHeader("Content-Disposition", "inline; filename=" + convertFileName(f.getName()));
                // 文件名应该编码成UTF-8
            } else { // 纯下载方式
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition", "attachment; filename=" + convertFileName(f.getName()));
            }
            out = response.getOutputStream();
            while ((len = bis.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            bis.close();
        }catch (Exception e){
            throw e;
        }finally {
            if(out != null){
                out.close();
            }
            if(bis != null){
                bis.close();
            }
        }
    }


    /**
     * 根据路径获取
     * @param filePath
     * @return
     */
    public static File getFile(String filePath){
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if(!parentDir.exists()){//判断文件目录是否存在
            parentDir.mkdirs();
        }
        return file;
    }

    /**
     * 获取文件基本路径
     * @return
     */
    public static String getBasePath(){
        return isLinux() ? "/home/upload/" : "D:/hx/upload/";
    }

    /**
     * 获取文件基本路径
     * @return
     */
    public static String getBasePath(String path){
        String basePath = isLinux() ? "/home/upload/" : "D:/hx/upload/";
        return basePath + path;
    }

    /**
     * 判断是否是linux系统
     * @return
     */
    public static boolean isLinux(){
        return System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0;
    }
}
