package com.JIMS.integration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private InternalApiKeyInterceptor apiKeyInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                    "http://localhost:*",
                  //  "http://10.10.94.*:*",
                    //"http://10.10.94.112:*",
                    "https://jims.jssl.in",
                    "https://jimstest.jssl.in"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Set-Cookie")
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/jssl/user/userlogin")
                .excludePathPatterns("/jssl/user/userlogout")
                .excludePathPatterns("/jssl/user/resetpassword")
                .excludePathPatterns("/jssl/user/forgotpassword")
                .excludePathPatterns("/jssl/user/otpvalidation")
                .excludePathPatterns("/jssl/user/verifyotp")
                .excludePathPatterns("/jssl/user/refresh")   // ← NEW
                .excludePathPatterns("/api/proxy/secondary-login")
                .excludePathPatterns("/api/captcha-image");
    }
}