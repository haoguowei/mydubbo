package com.hao.demo.interceptor;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * Created by haoguowei. Time 2018/9/4 16:41 Desc
 */
@SpringBootConfiguration
public class CustomMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TraceIdHandlerInterceptor());
        super.addInterceptors(registry);
    }
}
