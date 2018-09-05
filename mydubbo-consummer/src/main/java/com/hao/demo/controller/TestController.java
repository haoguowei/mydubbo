package com.hao.demo.controller;

import com.hao.demo.dubbo.ext.commons.Constants;
import com.hao.demo.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 * Created by haoguowei. Time 2018/9/4 14:11 Desc
 */
@Controller
public class TestController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ProductService productService;

    @RequestMapping("/")
    @ResponseBody
    String home(){
        String res =  System.getenv(Constants.SERVICE_CHAIN) + ">" + productService.getProductName("张三");
        return res;
    }
}
