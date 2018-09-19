package com.hao.demo.dubbo.ext.chain;

import com.hao.demo.dubbo.ext.pojo.ChainUnique;
import com.hao.demo.dubbo.ext.pojo.ZookeeperNode;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * Created by haoguowei. Time 2018/9/7 23:08 Desc
 *
 * app -> URL -> chain URL -> chain URL -> chain
 */
@Component("chainContainer")
public class ZookeeperChainContainer implements ChainContainer, ZookeeperService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String EXT_PATH = "/ext";

    private String APP_PATH = "";

    private ZooKeeper zk = null;

    @Value("${zookeeper.address}")
    private String zookeeperAddress;


    public static ChainUnique getForConsumer(URL url) {
        ChainUnique cacheUrl = new ChainUnique();
        cacheUrl.setIp(url.getHost());
        cacheUrl.setPort(String.valueOf(url.getPort()));
        cacheUrl.setInterfaceName(url.getParameter("interface"));
        cacheUrl.setProtocol(url.getProtocol());
        return cacheUrl;
    }


    public static ChainUnique getForProvider(URL url) {
        ChainUnique cacheUrl = new ChainUnique();
        cacheUrl.setIp(url.getParameter("bind.ip"));
        cacheUrl.setPort(url.getParameter("bind.port"));
        cacheUrl.setInterfaceName(url.getParameter("interface"));
        cacheUrl.setProtocol(url.getProtocol());
        return cacheUrl;
    }


    @Override
    public synchronized void deleteChains() {
        if (StringUtils.isBlank(APP_PATH)) {
            return;
        }
        try {
            if (zk.exists(APP_PATH, false) != null) {
                try {
                    List<String> list = zk.getChildren(APP_PATH, null);
                    if (CollectionUtils.isNotEmpty(list)) {
                        list.forEach(urlNodeName -> {
                            try {
                                zk.delete(APP_PATH + "/" + urlNodeName, -1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (KeeperException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                zk.delete(APP_PATH, -1);
            }
            logger.info("[mydubbo-ext] zookeeper client deleted APP_PATH : {}", APP_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Map<String, String> getAllChains() {
        Map<String, String> map = new HashMap<>();
        ZookeeperNode zkNode = getNodeTree(EXT_PATH);
        if (CollectionUtils.isNotEmpty(zkNode.getChildren())) {
            zkNode.getChildren().forEach(node -> {
                if (CollectionUtils.isNotEmpty(node.getChildren())) {
                    node.getChildren().forEach(n -> map.put(n.getName(), n.getValue()));
                }
            });
        }
        return map;
    }


    @Override
    public synchronized void putChain(URL url, String chain) {
        try {
            if (zk.exists(EXT_PATH, false) == null) {
                String rootNode = zk.create(EXT_PATH, EXT_PATH.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.info("[mydubbo-ext] zookeeper client created rootNode : {}", rootNode);
            }

            ChainUnique urlUnique = getForProvider(url);
            if (StringUtils.isBlank(APP_PATH)) {
                APP_PATH = EXT_PATH + "/" + url.getParameter("application") + "_" + urlUnique.getIp() + "_" + urlUnique.getPort();
                deleteChains();
            }

            if (zk.exists(APP_PATH, false) == null) {
                String appKeyNode = zk.create(APP_PATH, APP_PATH.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.info("[mydubbo-ext] zookeeper client created appKeyNode : {}", appKeyNode);
            }

            String tmp = APP_PATH + "/" + urlUnique.format();
            if (zk.exists(tmp, false) == null) {
                String urlNode = zk.create(tmp, chain.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                logger.info("[mydubbo-ext] zookeeper client created urlNode : {}", urlNode);
            }

            logger.info("[mydubbo-ext] zookeeper client putChain app={}, key={}, chain={}", APP_PATH, tmp, chain);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getChain(URL url) {
        ChainUnique urlUnique = getForConsumer(url);
        return getAllChains().get(urlUnique.format());
    }


    @PreDestroy
    public void destory() {
        try {
            zk.close();
            logger.info("[mydubbo-ext] zookeeper client closed!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private List<ZookeeperNode> getChilrenNodes(String node) {
        List<ZookeeperNode> children = new ArrayList<>();
        try {
            List<String> list = zk.getChildren(node, null);
            if (CollectionUtils.isEmpty(list)) {
                return children;
            }

            for (String str : list) {
                String tmpNode = node + (node.endsWith("/") ? "" : "/") + str;
                ZookeeperNode tmp = new ZookeeperNode(str, getNodeValue(tmpNode));
                tmp.setChildren(getChilrenNodes(tmpNode));
                children.add(tmp);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return children;
    }


    @Override
    public ZookeeperNode getNodeTree(String rootNode) {
        ZookeeperNode root = new ZookeeperNode(rootNode, getNodeValue(rootNode));
        root.setChildren(getChilrenNodes(rootNode));
        return root;
    }


    private String getNodeValue(String nodePath) {
        try {
            return new String(zk.getData(nodePath, watchedEvent -> logger.info("[mydubbo-ext] zookeeper get node value, nodePath=", nodePath), null));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }


    @PostConstruct
    public void init() {
        try {
            zk = new ZooKeeper(zookeeperAddress, 30000, watchedEvent -> logger.info("[mydubbo-ext] zookeeper connected! zookeeperAddress={}", zookeeperAddress));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
