package com.hao.demo.dubbo.ext.init;

import com.hao.demo.dubbo.ext.chain.ChainContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * Created by haoguowei. Time 2018/9/10 15:49 Desc
 */
@Component
public class DestroySystem implements DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ChainContainer chainContainer;

    @Override
    public void destroy() throws Exception {
        logger.info("========= DestroySystem =========");
        chainContainer.deleteChains();
    }
}
