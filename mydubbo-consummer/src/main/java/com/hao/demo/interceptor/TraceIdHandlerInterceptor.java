package com.hao.demo.interceptor;

import com.hao.demo.dubbo.ext.commons.Constants;

import org.apache.dubbo.rpc.RpcContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by haoguowei. Time 2018/9/4 16:38 Desc
 */
public class TraceIdHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = Constants.TRACE_ID + "-" + new Date().getTime() + "-" + Math.abs(new Random().nextInt());
        RpcContext.getContext().setAttachment(Constants.TRACE_ID, traceId);
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
