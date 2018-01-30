package com.qming.question2answer.configuration;

import com.qming.question2answer.interceptor.LoginRequireInterceptor;
import com.qming.question2answer.interceptor.PassPortInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-24
 * Time: 20:29
 */
@Component
public class Q2aWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassPortInterceptor passPortInterceptor;
    @Autowired
    LoginRequireInterceptor loginRequireInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(passPortInterceptor);
        interceptorRegistry.addInterceptor(loginRequireInterceptor).addPathPatterns("/user/*");
        super.addInterceptors(interceptorRegistry);
    }
}
