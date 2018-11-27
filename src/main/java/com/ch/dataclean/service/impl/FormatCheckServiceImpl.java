package com.ch.dataclean.service.impl;

import com.ch.dataclean.dao.DaoSupport;
import com.ch.dataclean.model.DataFormatCheckResultVo;
import com.ch.dataclean.model.DataFormatModel;
import com.ch.dataclean.model.FileModel;
import com.ch.dataclean.service.FormatCheckService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Aaron on 2018/11/19
 */
@Service
public class FormatCheckServiceImpl implements FormatCheckService {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    /**
     * 文件格式检查开始
     */
    public DataFormatCheckResultVo formatCheck(String deptId, List<FileModel> files) throws Exception {
        List<DataFormatModel> dataFormats = this.getDataFormatsByDeptid(deptId);
        if(null == dataFormats || 0 >= dataFormats.size()){
            return new DataFormatCheckResultVo(false,"未找到该部门的数据规范，无法进行数据格式校验");
        }
        for(FileModel file : files){
            boolean nameFormatFlag = false; //文件名称是否规范标识
            for(DataFormatModel dataFormat : dataFormats){
                if(file.getName().contains(dataFormat.getFileName().trim())){
                    //**********开始校验数据格式**************
                    String extension = file.getExtension().trim();
                    if(null != extension && extension.equals(dataFormat.getExtension().trim())){
                        DataFormatCheckResultVo resultVo = this.checkHead(file, dataFormat);
                        if(!resultVo.isResult()){
                            return resultVo;
                        }
                    }else {
                        return new DataFormatCheckResultVo(false,"文件["+file.getName()+"]格式有误，应为"+dataFormat.getExtension());
                    }
                    nameFormatFlag = true;
                    break;
                }
            }
            if(!nameFormatFlag){
                return new DataFormatCheckResultVo(false,"文件["+file.getName()+"]命名不规范");
            }

        }
        return new DataFormatCheckResultVo(true,"数据校验通过");
    }

    /**
     * 检查表头
     */
    public DataFormatCheckResultVo checkHead(FileModel file, DataFormatModel format) throws Exception {
        String extension = file.getExtension().trim();
        String headArStr = format.getHeadArr().trim();
        if(extension.equals("csv")){
            InputStreamReader isr = new InputStreamReader(file.getFileInputstream());
            BufferedReader reader = new BufferedReader(isr);
            String head = reader.readLine().trim();//第一行信息，为标题信息
            if(!head.equals(headArStr)){
                return new DataFormatCheckResultVo(false, "文件["+file.getName()+"]数据格式有误");
            }
        }else if(extension.equals("xls") || extension.equals("xlsx")){
            Workbook workbook = WorkbookFactory.create(file.getFileInputstream());
            //获取第一个sheet
            Sheet sheet = workbook.getSheetAt(0);
            //获取标题所在行
            Row row = sheet.getRow(format.getBeginRow()-1);
            //获取最大列数
            int colnum = row.getPhysicalNumberOfCells();
            String[] headArr = headArStr.split(",");
            if(colnum < headArr.length){
                return new DataFormatCheckResultVo(false, "文件["+file.getName()+"]数据列数不够");
            }else {
                for (int i = 0; i < headArr.length; i++) {
                    Cell cell = row.getCell(i);
                    String cellValue = cell.getStringCellValue().trim();
                    if(!cellValue.equals(headArr[i].trim())){
                        return new DataFormatCheckResultVo(false, "文件["+file.getName()+"]的第"+(i+1)+"列数据有误");
                    }
                }
            }
        }
         return new DataFormatCheckResultVo(true,"文件校验通过");
    }

    /**
     * 获取部门下的所有数据格式
     * @param deptId 部门id
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<DataFormatModel> getDataFormatsByDeptid(String deptId) throws Exception {
        return (List<DataFormatModel>) dao.findForList("dataFormat.findFormatsByDeptid", deptId);
    }

    public static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                case Cell.CELL_TYPE_NUMERIC:{
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }
}
