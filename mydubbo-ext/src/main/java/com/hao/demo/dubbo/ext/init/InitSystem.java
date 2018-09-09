package com.hao.demo.dubbo.ext.init;

import com.hao.demo.dubbo.ext.commons.Constants;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


/**
 * Created by haoguowei. Time 2018/9/7 17:53 Desc
 */
@Component
public class InitSystem implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        String env = System.getenv(Constants.SERVICE_CHAIN);
        System.setProperty(Constants.SERVICE_CHAIN, StringUtils.isBlank(env) ? "local" : env);
    }
}
