package com.hao.demo.dubbo.ext.chain;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;


/**
 * Created by haoguowei. Time 2018/9/7 15:18 Desc
 */
@Data
@ToString
public class UrlUnique implements Serializable {

    private String protocol;

    private String ip;

    private String port;

    private String interfaceName;

    public String format() {
        return protocol + "://" +  ip + ":" + port + "/" + interfaceName;
    }
}
