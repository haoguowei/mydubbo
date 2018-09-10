package com.hao.demo.dubbo.ext.chain;

import org.apache.dubbo.common.URL;


/**
 * 存储url 和 chain的容器服务
 * Created by haoguowei. Time 2018/9/7 22:43 Desc
 */
public interface ChainContainer {

    /**
     * 添加url的chain信息
     * @param url
     * @param chain
     */
    void putUrlWithChain(URL url, String chain);

    /**
     * 移除chain信息
     * @param url
     */
    void removeUrlWithChain(URL url);

    /**
     * 获取chain信息
     * @param url
     * @return
     */
    String getChain(URL url);
}
