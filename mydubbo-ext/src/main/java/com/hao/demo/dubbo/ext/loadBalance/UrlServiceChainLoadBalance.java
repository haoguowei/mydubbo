package com.hao.demo.dubbo.ext.loadBalance;

import com.hao.demo.dubbo.ext.commons.Constants;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by haoguowei. Time 2018/9/10 12:00 Desc
 */
public class UrlServiceChainLoadBalance extends AbstractChainLoadBalance {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String NAME = "urlServiceChain";

    protected <T> List<Invoker<T>> filterInvokers(List<Invoker<T>> invokers) {
        String serviceChain = RpcContext.getContext().getAttachment(Constants.CHAIN);
        if (StringUtils.isBlank(serviceChain)) {
            serviceChain = System.getenv(Constants.CHAIN);
        }

        logger.info("[mydubbo-ext] UrlServiceChainLoadBalance.serviceChain={};invokers={};", serviceChain,invokers.size());

        String finalServiceChain = serviceChain;
        List<Invoker<T>> newList = invokers.stream()
                .filter(v->getServiceChain(v.getUrl()).equals(finalServiceChain))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(newList)){
            newList = invokers.stream()
                    .filter(v->getServiceChain(v.getUrl()).equals(Constants.MASTER))
                    .collect(Collectors.toList());
        }

        RpcContext.getContext().setAttachment(Constants.CHAIN, serviceChain);
        return newList;
    }

    private String getServiceChain(URL url){
        return StringUtils.isBlank(url.getParameter(Constants.CHAIN)) ? "" : url.getParameter(Constants.CHAIN);
    }

}
