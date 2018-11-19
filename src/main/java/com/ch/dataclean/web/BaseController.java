package com.ch.dataclean.web;

import com.alibaba.fastjson.JSONObject;

/**
 * Description:
 * Created by Aaron on 2018/11/19
 */
public class BaseController {

    public JSONObject success(){
        JSONObject obj = new JSONObject();
        obj.put("state", "1");
        return obj;
    }

    public JSONObject error(){
        JSONObject jo = new JSONObject();
        jo.put("state", "0");
        return jo;
    }

    public JSONObject success(JSONObject jo){
        jo.put("state", "1");
        return jo;
    }

    public JSONObject data(JSONObject jo, Object obj){
        jo.put("data", obj);
        return jo;
    }
}
