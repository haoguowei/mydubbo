package com.hao.demo.service;

import com.hao.demo.dubbo.ext.commons.Constants;

import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Component;


/**
 * Created by haoguowei. Time 2018/8/30 14:27 Desc
 */
@Component("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Override
    public String getUserName(String name) {
        String traceId = RpcContext.getContext().getAttachment(Constants.TRACE_ID);
        return System.getenv(Constants.SERVICE_CHAIN) + ">" + name + "==>" + traceId;
    }
}
