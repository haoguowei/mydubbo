package com.hao.demo.dubbo.ext.chain;

import org.apache.dubbo.common.URL;

import java.util.Map;


/**
 * 存储url 和 chain的容器服务
 * Created by haoguowei. Time 2018/9/7 22:43 Desc
 */
public interface ChainContainer {

    /**
     * 移除chain信息
     */
    void deleteChains();

    /**
     * 获取所有保存的chain信息
     *
     * @return
     */
    Map<String, String> getAllChains();

    /**
     * 添加url的chain信息
     * @param url
     * @param chain
     */
    void putChain(URL url, String chain);

    /**
     * 获取chain信息
     * @param url
     * @return
     */
    String getChain(URL url);
}
