package com.hao.demo.dubbo.ext.chain;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;


/**
 * Created by haoguowei. Time 2018/9/7 23:08 Desc
 *
 * app -> URL -> chain URL -> chain URL -> chain
 */
@Component("chainContainer")
public class ZookeeperChainContainer implements ChainContainer, ZookeeperService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String rootPath = "/ext";

    private String appKey = "";

    private ZooKeeper zk = null;


    public static UrlUnique getForConsumer(URL url) {
        UrlUnique cacheUrl = new UrlUnique();
        cacheUrl.setIp(url.getHost());
        cacheUrl.setPort(String.valueOf(url.getPort()));
        cacheUrl.setInterfaceName(url.getParameter("interface"));
        cacheUrl.setProtocol(url.getProtocol());
        return cacheUrl;
    }


    public static UrlUnique getForProvider(URL url) {
        UrlUnique cacheUrl = new UrlUnique();
        cacheUrl.setIp(url.getParameter("bind.ip"));
        cacheUrl.setPort(url.getParameter("bind.port"));
        cacheUrl.setInterfaceName(url.getParameter("interface"));
        cacheUrl.setProtocol(url.getProtocol());
        return cacheUrl;
    }


    @Override
    public synchronized void deleteChains() {
        if (StringUtils.isBlank(appKey)) {
            return;
        }
        try {
            if (zk.exists(appKey, false) != null) {
                try {
                    List<String> list = zk.getChildren(appKey, null);
                    if (CollectionUtils.isNotEmpty(list)) {
                        list.forEach(str -> {
                            try {
                                zk.delete(appKey + "/" + str, -1);
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


                zk.delete(appKey, -1);
            }
            logger.info("RedisChainContainer.deleteChains appKey={}", appKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Map<String, String> getAllChains() {
        Map<String, String> map = new HashMap<>();
        ZkNode zkNode = getNodeTree(rootPath);
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
            if (zk.exists(rootPath, false) == null) {
                String rootNode = zk.create(rootPath, rootPath.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.info("createed rootNode={}", rootNode);
            }

            UrlUnique urlUnique = getForProvider(url);
            if (StringUtils.isBlank(appKey)) {
                appKey = rootPath + "/" + url.getParameter("application") + "_" + urlUnique.getIp() + "_" + urlUnique.getPort();
                deleteChains();
            }

            if (zk.exists(appKey, false) == null) {
                String appKeyNode = zk.create(appKey, appKey.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.info("createed appKeyNode={}", appKeyNode);
            }

            String tmp = appKey + "/" + urlUnique.format();
            if (zk.exists(tmp, false) == null) {
                String urlNode = zk.create(tmp, chain.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                logger.info("createed urlNode={}", urlNode);
            }

            logger.info("ZookeeperChainContainer.putChain app={}, key={}, chain={}", appKey, tmp, chain);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getChain(URL url) {
        UrlUnique urlUnique = getForConsumer(url);
        Map<String, String> map = getAllChains();
        return map.get(urlUnique.format());
    }


    private List<ZkNode> getChilrenNodes(String node) {
        List<ZkNode> children = new ArrayList<>();
        try {
            List<String> list = zk.getChildren(node, null);
            if (CollectionUtils.isEmpty(list)) {
                return children;
            }

            for (String str : list) {
                String tmpNode = node + (node.endsWith("/") ? "" : "/") + str;
                ZkNode tmp = new ZkNode(str, getValue(tmpNode));
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
    public ZkNode getNodeTree(String rootNode) {
        ZkNode root = new ZkNode(rootNode, getValue(rootNode));
        root.setChildren(getChilrenNodes(rootNode));
        return root;
    }


    private String getValue(String path) {
        try {
            return new String(zk.getData(path, watchedEvent -> logger.info("zookeeper get path value, path=", path), null));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }


    @PostConstruct
    public void init() {
        if (zk == null) {
            try {
                zk = new ZooKeeper("127.0.0.1:2181", 30000, watchedEvent -> logger.info("zookeeper connected!"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
