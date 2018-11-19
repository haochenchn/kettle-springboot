package com.ch.dataclean.common.util;

import com.ch.dataclean.model.DataFormatCheckResultVo;
import com.ch.dataclean.model.FileModel;
import org.apache.ibatis.annotations.Case;

import java.util.List;

/**
 * Description:数据文件格式校验工具类
 * Created by Aaron on 2018/11/19
 */
public class FormatCheckUtil {

    /**
     * 文件格式检查开始
     * @param beginRow
     * @param head
     * @return
     * //TODO
     */
    public static DataFormatCheckResultVo formatCheck(String deptId, List<FileModel> files){
        switch (deptId){
            case "1":
                break;
            case "2":
                break;
        }
        return new DataFormatCheckResultVo(true,"");
    }

    /**
     * 检查表头
     * @param beginRow 开始行
     * @param head 表头数组
     */
    public static boolean checkHead(int beginRow, String[] head){

         return true;
    }
}
