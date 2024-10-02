package com.aieverywhere.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允許所有路徑
                .allowedOriginPatterns("*") // 允許來自指定域名的請求
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允許的 HTTP 方法
                .allowedHeaders("*") // 允許的請求標頭
                .allowCredentials(true); // 允許攜帶憑證（如 Cookies）
    }
}
