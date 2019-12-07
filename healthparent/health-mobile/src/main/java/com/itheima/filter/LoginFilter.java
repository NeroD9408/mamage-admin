package com.itheima.filter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginFilter implements Filter {
    // 需要定义系统页面访问中可放行的连接
    private List<String> list = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //list.add("/login.html");

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //从cookie中获取当前登录的用户标志
        //获取指定cookie
        Cookie[] cookies = request.getCookies();
        Cookie login_member_telephone = getCookieByName(cookies, "login_member_telephone");
        //判断cookie是否存在
        if (login_member_telephone != null){
            //若无，放行
            filterChain.doFilter(request,response);
            return;
        }
        response.sendRedirect("/login.html");
    }
    //根据手机号查询需要的cookie
    private Cookie getCookieByName(Cookie[] cookies, String cookieName) {
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())){
                    //找到指定的cookie
                    return cookie;
                }
            }
        }
        return null;
    }

    @Override
    public void destroy() {

    }
}
