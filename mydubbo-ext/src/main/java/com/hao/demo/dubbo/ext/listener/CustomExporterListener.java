package com.hao.demo.dubbo.ext.listener;

import org.apache.dubbo.rpc.Exporter;
import org.apache.dubbo.rpc.ExporterListener;
import org.apache.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by haoguowei. Time 2018/9/6 16:40 Desc
 */
public class CustomExporterListener implements ExporterListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void exported(Exporter<?> exporter) throws RpcException {
        logger.info("exported>>>>>>>{}", exporter.getInvoker().getUrl().toString());
    }


    @Override
    public void unexported(Exporter<?> exporter) {
        logger.info("unexported>>>>>>>{}", exporter.getInvoker().getUrl().toString());
    }
}
