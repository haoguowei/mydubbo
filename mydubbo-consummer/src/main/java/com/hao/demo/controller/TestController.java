package com.hao.demo.controller;

import com.hao.demo.dubbo.ext.chain.ChainContainer;
import com.hao.demo.dubbo.ext.chain.ZookeeperService;
import com.hao.demo.dubbo.ext.commons.Constants;
import com.hao.demo.dubbo.ext.pojo.ZookeeperNode;
import com.hao.demo.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import javax.annotation.Resource;


/**
 * Created by haoguowei. Time 2018/9/4 14:11 Desc
 */
@Controller
public class TestController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ProductService productService;

    @Resource
    private ChainContainer chainContainer;

    @Autowired
    private ZookeeperService zookeeperService;

    @RequestMapping("/")
    @ResponseBody
    String home(){
        String name = "刘德华";
        String res = System.getProperty(Constants.CHAIN) + ">" + productService.getProductName(name);
        return res;
    }


    @RequestMapping("/view")
    @ResponseBody
    Map<String, String> view() {
        return chainContainer.getAllChains();
    }


    @RequestMapping("/zookeeper")
    @ResponseBody
    ZookeeperNode zookeeper() {
        return zookeeperService.getNodeTree("/");
    }
}
