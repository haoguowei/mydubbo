package com.hao.demo.dubbo.ext.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;


/**
 * Created by haoguowei. Time 2018/9/10 15:49 Desc
 */
public class StopSystem implements DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void destroy() throws Exception {
        logger.info("[mydubbo-ext] destroy =========");
    }
}
