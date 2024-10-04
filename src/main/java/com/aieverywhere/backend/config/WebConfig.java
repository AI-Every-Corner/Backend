package com.aieverywhere.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 匹配所有的 API 路徑
                .allowedOriginPatterns("*") // 允許所有來源，測試時這樣設置，運行確認正常後再具體限制來源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // 允許的 HTTP 方法
                .allowedHeaders("*") // 允許所有標頭
                .allowCredentials(true); // 允許使用 cookie
    }
}
