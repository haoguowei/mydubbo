FROM java:8
MAINTAINER haoguowei52@163.com

ENV ZK_VERSION 3.4.12

# 下载zookeeper 并解压
RUN curl -sLo /zookeeper-${ZK_VERSION}.tar.gz http://mirror.bit.edu.cn/apache/zookeeper/zookeeper-${ZK_VERSION}/zookeeper-${ZK_VERSION}.tar.gz \
    && tar xf /zookeeper-${ZK_VERSION}.tar.gz -C /  \
    && mv /zookeeper-${ZK_VERSION} /zookeeper \
    && mv /zookeeper/conf/zoo_sample.cfg /zookeeper/conf/zoo.cfg \
    && apt-get clean \
    && rm -rf /zookeeper-${ZK_VERSION}.tar.gz /var/lib/apt/lists/* /var/tmp/*

# 前台运行模式
CMD /zookeeper/bin/zkServer.sh start-foreground
EXPOSE 2181
