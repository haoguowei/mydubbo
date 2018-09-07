package com.hao.demo.service;

import com.hao.demo.dubbo.ext.commons.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * Created by haoguowei. Time 2018/8/30 14:26 Desc
 */
@Component("productService")
public class ProductServiceImpl implements ProductService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private UserInfoService userInfoService;


    @Override
    public String getProductName(String s) {
        return System.getProperty(Constants.SERVICE_CHAIN) + ">" + userInfoService.getUserName(s);
    }
}
