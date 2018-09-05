package com.hao.demo.dubbo.ext.loadBalance;

import com.hao.demo.dubbo.ext.commons.Constants;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * Created by haoguowei. Time 2018/8/30 18:11 Desc
 */
public class CustomLoadBalance implements LoadBalance {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String NAME = "custom";

    private final Random random = new Random();

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        if (invokers == null || invokers.isEmpty()) {
            return null;
        }
        invokers = getCustomInvokers(invokers);
        return doSelect(invokers, url, invocation);
    }


    private <T> List<Invoker<T>> getCustomInvokers(List<Invoker<T>> invokers) {
        String traceId = RpcContext.getContext().getAttachment(Constants.TRACE_ID);
        String serviceChain = RpcContext.getContext().getAttachment(Constants.SERVICE_CHAIN);

        if (StringUtils.isBlank(serviceChain)) {
            serviceChain = System.getenv(Constants.SERVICE_CHAIN);
        }

        logger.info("[mydubbo-ext] CustomLoadBalance.tranceId={};serviceChain={};invokers={};", traceId, serviceChain,invokers.size());

        String finalServiceChain = serviceChain;
        List<Invoker<T>> newList = invokers.stream()
                .filter(v->v.getUrl().getParameter(Constants.SERVICE_CHAIN).equals(finalServiceChain))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(newList)){
            newList = invokers.stream()
                    .filter(v->v.getUrl().getParameter(Constants.SERVICE_CHAIN).equals(Constants.MASTER))
                    .collect(Collectors.toList());
        }

        RpcContext.getContext().setAttachment(Constants.SERVICE_CHAIN, serviceChain);
        return newList;
    }


    static int calculateWarmupWeight(int uptime, int warmup, int weight) {
        int ww = (int) ((float) uptime / ((float) warmup / (float) weight));
        return ww < 1 ? 1 : (ww > weight ? weight : ww);
    }

    protected int getWeight(Invoker<?> invoker, Invocation invocation) {
        int weight = invoker.getUrl().getMethodParameter(invocation.getMethodName(), org.apache.dubbo.common.Constants.WEIGHT_KEY, org.apache.dubbo.common.Constants.DEFAULT_WEIGHT);
        if (weight > 0) {
            long timestamp = invoker.getUrl().getParameter(org.apache.dubbo.common.Constants.REMOTE_TIMESTAMP_KEY, 0L);
            if (timestamp > 0L) {
                int uptime = (int) (System.currentTimeMillis() - timestamp);
                int warmup = invoker.getUrl().getParameter(org.apache.dubbo.common.Constants.WARMUP_KEY, org.apache.dubbo.common.Constants.DEFAULT_WARMUP);
                if (uptime > 0 && uptime < warmup) {
                    weight = calculateWarmupWeight(uptime, warmup, weight);
                }
            }
        }
        return weight;
    }

    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        int length = invokers.size(); // Number of invokers
        int totalWeight = 0; // The sum of weights
        boolean sameWeight = true; // Every invoker has the same weight?
        for (int i = 0; i < length; i++) {
            int weight = getWeight(invokers.get(i), invocation);
            totalWeight += weight; // Sum
            if (sameWeight && i > 0
                    && weight != getWeight(invokers.get(i - 1), invocation)) {
                sameWeight = false;
            }
        }
        if (totalWeight > 0 && !sameWeight) {
            // If (not every invoker has the same weight & at least one invoker's weight>0), select randomly based on totalWeight.
            int offset = random.nextInt(totalWeight);
            // Return a invoker based on the random value.
            for (int i = 0; i < length; i++) {
                offset -= getWeight(invokers.get(i), invocation);
                if (offset < 0) {
                    return invokers.get(i);
                }
            }
        }
        // If all invokers have the same weight value or totalWeight=0, return evenly.
        return invokers.get(random.nextInt(length));
    }
}
