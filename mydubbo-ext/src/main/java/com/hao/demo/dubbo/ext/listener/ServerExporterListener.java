package com.hao.demo.dubbo.ext.listener;

import com.hao.demo.dubbo.ext.chain.ChainContainer;
import com.hao.demo.dubbo.ext.commons.Constants;
import com.hao.demo.dubbo.ext.init.SpringContextUtil;

import org.apache.dubbo.rpc.Exporter;
import org.apache.dubbo.rpc.ExporterListener;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.stereotype.Component;


/**
 * Created by haoguowei. Time 2018/9/6 16:40 Desc
 */
@Component
public class ServerExporterListener implements ExporterListener {

    @Override
    public void exported(Exporter<?> exporter) throws RpcException {
        ChainContainer chainContainer = (ChainContainer)SpringContextUtil.getBean("chainContainer");
        chainContainer.addUrlChain(exporter.getInvoker().getUrl(), System.getProperty(Constants.SERVICE_CHAIN));
    }


    @Override
    public void unexported(Exporter<?> exporter) {
        ChainContainer chainContainer = (ChainContainer)SpringContextUtil.getBean("chainContainer");
        chainContainer.removeUrlChain(exporter.getInvoker().getUrl());
    }


}
