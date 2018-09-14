package com.hao.demo.interceptor;

import com.hao.demo.dubbo.ext.commons.Constants;

import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by haoguowei. Time 2018/9/4 16:38 Desc
 */
public class ChainHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String chain = StringUtils.isBlank(request.getParameter("chain")) ? System.getProperty(Constants.SERVICE_CHAIN) : request.getParameter("chain");
        RpcContext.getContext().setAttachment(Constants.SERVICE_CHAIN, chain);
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
