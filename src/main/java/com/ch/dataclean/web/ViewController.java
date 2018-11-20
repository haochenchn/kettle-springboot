package com.ch.dataclean.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description:
 * Created by Aaron on 2018/11/20
 */
@Controller
@RequestMapping("/view")
public class ViewController {
    @RequestMapping(value = "/index1")
    public String toIndex1(){
        return "uploadStatus";
    }
}
