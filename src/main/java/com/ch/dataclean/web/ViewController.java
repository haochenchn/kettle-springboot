package com.ch.dataclean.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description:
 * Created by Aaron on 2018/11/20
 */
@Controller
public class ViewController {
    @RequestMapping(value = "/")
    public String index(){
        return "forward:file/toUpload";
    }
}
