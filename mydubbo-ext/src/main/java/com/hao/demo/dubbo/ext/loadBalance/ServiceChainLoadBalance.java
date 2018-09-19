package com.hao.demo.dubbo.ext.loadBalance;

import com.hao.demo.dubbo.ext.chain.ChainContainer;
import com.hao.demo.dubbo.ext.commons.Constants;
import com.hao.demo.dubbo.ext.commons.SpringContextUtil;

import org.apache.dubbo.common.utils.CollectionUtils;
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

    public static final String NAME = "chain";

    protected  <T> List<Invoker<T>> filterInvokers(List<Invoker<T>> invokers) {
        ChainContainer chainContainer = (ChainContainer)SpringContextUtil.getBean("chainContainer");

        //获取当前调用链
        String chain = RpcContext.getContext().getAttachment(Constants.CHAIN);
        logger.info("[mydubbo-ext] ServiceChainLoadBalance.currentChain={};invokers.size={};", chain, invokers.size());

        List<Invoker<T>> list = invokers.stream().filter(v -> chainContainer.getChain(v.getUrl()).equals(chain)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)){
            list = invokers.stream().filter(v -> chainContainer.getChain(v.getUrl()).equals(Constants.MASTER)).collect(Collectors.toList());
        }

        RpcContext.getContext().setAttachment(Constants.CHAIN, chain);
        logger.info("[mydubbo-ext] ServiceChainLoadBalance.list.currentChain={}; invoke.urls={};", chain, getEchoUrlInfo(list));
        return list;
    }


    private <T> String getEchoUrlInfo(List<Invoker<T>> invokers) {
        if (CollectionUtils.isEmpty(invokers)) {
            return "";
        }
        StringBuffer sbr = new StringBuffer();
        invokers.forEach(v -> sbr.append(v.getUrl()).append(">><<"));
        return sbr.toString();
    }

}
