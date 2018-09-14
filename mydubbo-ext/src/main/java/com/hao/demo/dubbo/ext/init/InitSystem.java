package com.hao.demo.dubbo.ext.init;

import com.hao.demo.dubbo.ext.commons.Constants;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


/**
 * Created by haoguowei. Time 2018/9/7 17:53 Desc
 */
@Component
public class InitSystem implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void afterPropertiesSet() throws Exception {
        String env = StringUtils.isBlank(System.getenv(Constants.CHAIN)) ? "local" : System.getenv(Constants.CHAIN);
        System.setProperty(Constants.CHAIN, env);
        logger.info("[mydubbo-ext] Server Inited! Current Service Chain : {}", env);
    }
}
