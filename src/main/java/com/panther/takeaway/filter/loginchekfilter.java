package com.panther.takeaway.filter;

import com.alibaba.fastjson.JSON;
import com.panther.takeaway.common.BaseContent;
import com.panther.takeaway.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class loginchekfilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request =  (HttpServletRequest) servletRequest;
        HttpServletResponse response =  (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        // 放行地址
        String[] urls = new String[]{
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**",
            "/common/**",
            "/user/sendMsg",
            "/user/login",
            "/doc.html",
            "/webjars/**",
            "/swagger-resources",
            "/v2/api-docs"
        };
        // 匹配路径
        boolean exit = checkUrl(urls, requestURI);

        //是否处理
        if(exit){
            // 直接放行地址
            filterChain.doFilter(request,response);
            return;
        }
        //判断session是否登录
        if(request.getSession().getAttribute("employee") != null){

            Long empId = (Long)request.getSession().getAttribute("employee");
            BaseContent.setCurrentID(empId);
            filterChain.doFilter(request,response);
            return;
        }
        //移动端判断session是否登录
        if(request.getSession().getAttribute("user") != null){

            Long userId = (Long)request.getSession().getAttribute("user");
            BaseContent.setCurrentID(userId);
            filterChain.doFilter(request,response);
            return;
        }

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }


    public boolean checkUrl(String[] urls,String request){
        boolean match;
        for(String temp : urls){
            match = PATH_MATCHER.match(temp, request);
            if(match){
                return true;
            }
        }
        return false;
    }
}
