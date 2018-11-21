package com.ch.dataclean.common.util;

import java.net.URL;

/**
 * 获取项目资源的工具类
 */
public class ResourceLoader {
    public static String CLASS_PATH_PREFIX ="classpath:";

    /**
     * classpath中获取资源
     * @Title: getResource
     * @Description: classpath中获取资源
     * @param resource
     * @return
     */
    public static URL getResource(String resource) {
        ClassLoader classLoader = null;
        classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResource(resource);
    }

    /**
     *  classpath 中搜索路径
     * @Title: getPath
     * @Description:
     * @param resource
     * @return
     */
    public static String getPath(String resource){
        if(resource!=null){
            if(resource.startsWith(CLASS_PATH_PREFIX)){
                resource = getPath("")+resource.replaceAll(CLASS_PATH_PREFIX, "");
            }
        }
        URL url = getResource(resource);
        if(url==null)
            return null;
        return url.getPath().replaceAll("%20", " ");
    }

    /**
     *
     * @Title: getPath
     * @Description:
     * @param resource
     * @param clazz
     * @return
     */
    public static String getPath(String resource,Class clazz){
        URL url = getResource(resource, clazz);
        if(url==null)
            return null;
        return url.getPath().replaceAll("%20", " ");
    }

    /**
     * 指定class中获取资源
     * @Title: getResource
     * @Description: 指定class中获取资源
     * @param resource
     * @param clazz
     * @return
     */
    public static URL getResource(String resource,Class clazz){
        return clazz.getResource(resource);
    }




}