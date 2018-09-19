package com.hao.demo.dubbo.ext.pojo;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;


/**
 * Created by haoguowei. Time 2018/9/7 15:18 Desc
 */
@Data
@ToString
public class ChainUnique implements Serializable {

    private String protocol;

    private String ip;

    private String port;

    private String interfaceName;

    public String format() {
        return protocol + "_" + ip + "_" + port + "_" + interfaceName;
    }
}
