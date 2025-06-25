package com.casbin.casbin_test.config;

import com.casbin.casbin_test.interceptor.UserAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserAuthenticationInterceptor userAuthenticationInterceptor;

    @Autowired
    public WebMvcConfig(UserAuthenticationInterceptor userAuthenticationInterceptor) {
        this.userAuthenticationInterceptor = userAuthenticationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userAuthenticationInterceptor)
                .addPathPatterns("/api/**");
    }
}
