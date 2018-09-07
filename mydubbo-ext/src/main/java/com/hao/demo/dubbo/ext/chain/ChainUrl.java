package com.hao.demo.dubbo.ext.chain;

import java.io.Serializable;


/**
 * Created by haoguowei. Time 2018/9/7 15:18 Desc
 */
public class ChainUrl implements Serializable {

    private String protocol;

    private String ip;

    private String port;

    private String interfaceName;

    public String format() {
        return protocol + "://" +  ip + ":" + port + "/" + interfaceName;
    }


    public String getProtocol() {
        return protocol;
    }


    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }


    public String getIp() {
        return ip;
    }


    public void setIp(String ip) {
        this.ip = ip;
    }


    public String getPort() {
        return port;
    }


    public void setPort(String port) {
        this.port = port;
    }


    public String getInterfaceName() {
        return interfaceName;
    }


    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }
}
