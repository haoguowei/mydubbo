package com.hao.demo.dubbo.ext.chain;

import org.apache.dubbo.common.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * Created by haoguowei. Time 2018/9/7 23:08 Desc
 */
@Component("chainContainer")
public class RedisChainContainer implements ChainContainer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addUrlChain(URL url, String chain) {
        String chainUrl = getForProvider(url);
        logger.info("addUrlChain chainUrl={}, chain={}",chainUrl, chain);
        stringRedisTemplate.opsForValue().set(chainUrl, chain);
    }


    @Override
    public void removeUrlChain(URL url) {
        String chainUrl = getForProvider(url);
        logger.info("removeUrlChain chainUrl={}",chainUrl);
        stringRedisTemplate.delete(chainUrl);
    }


    @Override
    public String getChain(URL url) {
        String chainUrl = getForConsumer(url);
        String chain = stringRedisTemplate.opsForValue().get(chainUrl);
        logger.info("getChain chainUrl={}, chain={}",chainUrl, chain);
        return chain;
    }

    public static String getForProvider(URL url){
        ChainUrl cacheUrl = new ChainUrl();
        cacheUrl.setIp(url.getParameter("bind.ip"));
        cacheUrl.setPort(url.getParameter("bind.port"));
        cacheUrl.setInterfaceName(url.getParameter("interface"));
        cacheUrl.setProtocol(url.getProtocol());
        return cacheUrl.format();
    }

    public static String getForConsumer(URL url){
        ChainUrl cacheUrl = new ChainUrl();
        cacheUrl.setIp(url.getHost());
        cacheUrl.setPort(String.valueOf(url.getPort()));
        cacheUrl.setInterfaceName(url.getParameter("interface"));
        cacheUrl.setProtocol(url.getProtocol());
        return cacheUrl.format();
    }
}
