package com.hao.demo.dubbo.ext.init;

import com.hao.demo.dubbo.ext.commons.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


/**
 * Created by haoguowei. Time 2018/9/4 17:22 Desc
 */
@Component
public class InitProperty implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("[mydubbo-ext] InitProperty.current service chain : {};", System.getenv(Constants.SERVICE_CHAIN));
    }
}
