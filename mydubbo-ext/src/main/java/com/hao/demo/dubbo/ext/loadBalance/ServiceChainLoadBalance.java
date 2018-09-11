package com.hao.demo.dubbo.ext.loadBalance;

import com.hao.demo.dubbo.ext.chain.ChainContainer;
import com.hao.demo.dubbo.ext.commons.Constants;
import com.hao.demo.dubbo.ext.init.SpringContextUtil;

import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by haoguowei. Time 2018/8/30 18:11 Desc
 */
public class ServiceChainLoadBalance extends AbstractChainLoadBalance {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String NAME = "serviceChain";

    protected  <T> List<Invoker<T>> filterInvokers(List<Invoker<T>> invokers) {
        String requestChain = RpcContext.getContext().getAttachment(Constants.SERVICE_CHAIN);
        String currentRequestChain = StringUtils.isBlank(requestChain) ? System.getProperty(Constants.SERVICE_CHAIN) : requestChain;
        logger.info("[mydubbo-ext] ServiceChainLoadBalance.currentRequestChain={};invokers.size={};", currentRequestChain,invokers.size());

        ChainContainer chainContainer = (ChainContainer)SpringContextUtil.getBean("chainContainer");

        List<Invoker<T>> list = invokers.stream().filter(v -> chainContainer.getChain(v.getUrl()).equals(currentRequestChain)).collect(Collectors.toList());
        logger.info("[mydubbo-ext] ServiceChainLoadBalance.list.size={};invokers.size={};", list.size(),invokers.size());

        if (CollectionUtils.isEmpty(list)){
            list = invokers.stream().filter(v -> chainContainer.getChain(v.getUrl()).equals(Constants.MASTER)).collect(Collectors.toList());
        }


        RpcContext.getContext().setAttachment(Constants.SERVICE_CHAIN, currentRequestChain);
        return list;
    }


}
