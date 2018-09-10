package com.hao.demo.dubbo.ext.chain;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.ToString;


/**
 * Created by haoguowei. Time 2018/9/10 16:15 Desc
 */
@Data
@ToString
public class AppUnique implements Serializable {

    private String appName;

    private String ip;

    private String port;

    private List<String> urlUniqueList;

    public String format(){
        return appName + "_" +  ip + "_" + port;
    }

}
