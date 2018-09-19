package com.hao.demo.dubbo.ext.pojo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.ToString;


/**
 * Created by haoguowei. Time 2018/9/18 16:55 Desc
 */
@Data
@ToString
public class ZookeeperNode implements Serializable {

    private String name;

    private String value;

    private List<ZookeeperNode> children;


    public ZookeeperNode(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
