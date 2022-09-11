package cn.lijilong.zauth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import cn.lijilong.zauth.interceptor.ZAuthInterceptor;

import javax.annotation.Resource;

@Component
@Slf4j
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    ZAuthInterceptor zAuthInterceptor;
    @Resource
    ZAuthConfigurationProperties zAuthConfigurationProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (!zAuthConfigurationProperties.isApiGuard()) {
            log.warn("ZAuth API Guard Is Not Enable, Your API Port Is Danger!");
        }
        registry.addInterceptor(zAuthInterceptor);
    }
}
