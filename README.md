# mydubbo

##一个dubbo测试环境隔离的demo

1. 修改了dubbo源码的ServiceConfig#export#doExportUrlsFor1Protocol
  
        map.put("service-chain", System.getenv("service-chain"));
   
2. 注释掉 AbstractClusterInvoker#doSelect

        //if (invokers.size() == 1)
          //  return invokers.get(0);
            
3. mydubbo-ext自定义customBalance


##docker运行
1. 启动zkcloud
2. 打包
    
        mvn clean install

3. 修改zk配置，将jar包cp到doc下
4. 运行docker

        docker-compose up