package com.ch.dataclean.common.page;

import com.ch.dataclean.common.exception.BaseException;
import com.ch.dataclean.model.FileModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Created by Aaron on 2018/11/21
 */
public class Page<T> {
    private int pageNum;
    private int pageSize;
    private int startRow;
    private int endRow;
    private long total;
    private int pages;


    //排序
    private String orderBy;
    private List<T> rows;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    /**
     * 分页查询
     * @param sqlSessionTemplate
     * @param sqlMappingStr
     * @param param
     * @param page
     * @return
     */
    public Page<T> queryForPage(SqlSessionTemplate sqlSessionTemplate, String sqlMappingStr, Map param, Page page){

        if(null != this.orderBy && "" != this.orderBy.trim()){
            PageHelper.startPage(page.getPageNum(),page.getPageSize(),this.orderBy);
        }else {
            PageHelper.startPage(page.getPageNum(),page.getPageSize());
        }
        List<T> list = sqlSessionTemplate.selectList(sqlMappingStr, param);
        PageInfo pageInfo = new PageInfo(list);
        page.setPageNum(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        page.setRows(list);
        page.setTotal(pageInfo.getTotal());
        page.setPages(pageInfo.getPages());
        page.setStartRow(pageInfo.getEndRow());
        page.setEndRow(pageInfo.getEndRow());
        return page;
    }
}
