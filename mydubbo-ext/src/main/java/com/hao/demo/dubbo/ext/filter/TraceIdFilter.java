package com.hao.demo.dubbo.ext.filter;

import com.hao.demo.dubbo.ext.commons.Constants;

import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by haoguowei. Time 2018/8/31 13:52 Desc
 */
public class TraceIdFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = RpcContext.getContext().getAttachment(Constants.TRACE_ID);
        String serviceChain = RpcContext.getContext().getAttachment(Constants.SERVICE_CHAIN);
        logger.info("[mydubbo-ext] TraceIdFilter.tranceId={};serviceChain={};", traceId, serviceChain);
        return invoker.invoke(invocation);
    }

}
