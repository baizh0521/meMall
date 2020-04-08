// ======================================
// Project Name:meddb-starter
// Package Name:com.kingyee.nhblood
// File Name:DefineConfiguration.java
// Create Date:2019年10月16日  16:28
// ======================================
package com.kingyee.me;

import com.kingyee.me.common.security.AdminInterceptor;
import com.kingyee.me.interceptor.UserInfoInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * mvc配置
 *
 * @author 李旭光
 * @version 2019年10月16日  16:28
 */
@Configuration
public class DefineConfiguration implements WebMvcConfigurer {
    //后台用户拦截器
    private final AdminInterceptor adminInterceptor;
    //前台用户拦截器
    private final UserInfoInterceptor userInfoInterceptor;

    public DefineConfiguration(AdminInterceptor adminInterceptor,UserInfoInterceptor userInfoInterceptor ) {
        this.adminInterceptor = adminInterceptor;
        this.userInfoInterceptor = userInfoInterceptor;
    }

    @Bean
    public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
        return new DeviceResolverHandlerInterceptor();
    }

    @Bean
    public DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver() {
        return new DeviceHandlerMethodArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(deviceHandlerMethodArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(deviceResolverHandlerInterceptor());
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin", "/admin/**")
                .excludePathPatterns("/admin/login", "/admin/logout");
        registry.addInterceptor(userInfoInterceptor)
                .addPathPatterns("/","/web","/web/**")
                .excludePathPatterns("/web/adv");
    }

}