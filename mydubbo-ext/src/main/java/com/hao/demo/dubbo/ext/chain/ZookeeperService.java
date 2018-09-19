package com.hao.demo.dubbo.ext.chain;

import com.hao.demo.dubbo.ext.pojo.ZookeeperNode;


/**
 * Created by haoguowei. Time 2018/9/18 17:18 Desc
 */
public interface ZookeeperService {

    ZookeeperNode getNodeTree(String rootNode);

}
