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
        
5. 访问

        http://localhost:8080
        
        http://localhost:28080
       
        http://localhost:38080
 
        
##服务调用情况

    1. master S1 > master S2 > master S3
    
    2. dev S1 > dev S2 > dev S3
    
    3. test S1 > master S2 > test S3
