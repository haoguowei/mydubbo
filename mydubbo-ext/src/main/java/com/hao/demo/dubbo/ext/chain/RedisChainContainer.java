package com.hao.demo.dubbo.ext.chain;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;


/**
 * Created by haoguowei. Time 2018/9/7 23:08 Desc
 *
 * app -> URL -> chain
 *        URL -> chain
 *        URL -> chain
 *
 */
@Component("redisChainContainer")
public class RedisChainContainer implements ChainContainer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private String app_prefix = "app_";

    private String appKey = "";


    public static UrlUnique getForConsumer(URL url) {
        UrlUnique cacheUrl = new UrlUnique();
        cacheUrl.setIp(url.getHost());
        cacheUrl.setPort(String.valueOf(url.getPort()));
        cacheUrl.setInterfaceName(url.getParameter("interface"));
        cacheUrl.setProtocol(url.getProtocol());
        return cacheUrl;
    }


    public static UrlUnique getForProvider(URL url) {
        UrlUnique cacheUrl = new UrlUnique();
        cacheUrl.setIp(url.getParameter("bind.ip"));
        cacheUrl.setPort(url.getParameter("bind.port"));
        cacheUrl.setInterfaceName(url.getParameter("interface"));
        cacheUrl.setProtocol(url.getProtocol());
        return cacheUrl;
    }


    @Override
    public synchronized void deleteChains() {
        stringRedisTemplate.delete(appKey);
        logger.info("RedisChainContainer.deleteChains appKey={}", appKey);
    }


    @Override
    public Map<String, String> getAllChains() {
        Map<String, String> resultMap = new HashMap<>();
        Set<String> keys = stringRedisTemplate.keys(app_prefix + "*");
        if (CollectionUtils.isNotEmpty(keys)) {
            keys.forEach(k -> {
                Map<Object, Object> tmp = stringRedisTemplate.opsForHash().entries(k);
                if (tmp != null && tmp.size() > 0) {
                    tmp.forEach((x, y) -> resultMap.put(String.valueOf(x), String.valueOf(y)));
                }
            });
        }
        logger.info("RedisChainContainer.getAllChains={}", resultMap);
        return resultMap;
    }


    @Override
    public synchronized void putChain(URL url, String chain) {
        UrlUnique urlUnique = getForProvider(url);

        if (StringUtils.isBlank(appKey)) {
            appKey = app_prefix + url.getParameter("application") + "_" + urlUnique.getIp() + ":" + urlUnique.getPort();
            deleteChains();
        }

        stringRedisTemplate.opsForHash().put(appKey, urlUnique.format(), chain);
        logger.info("RedisChainContainer.putChain appKey={}, urlHashKey={}, chain={}", appKey, urlUnique.format(), chain);
    }


    @Override
    public String getChain(URL url) {
        UrlUnique urlUnique = getForConsumer(url);
        Map<String, String> chainsMap = getAllChains();
        return chainsMap.get(urlUnique.format());
    }
}
