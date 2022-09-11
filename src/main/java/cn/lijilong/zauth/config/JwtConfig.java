package cn.lijilong.zauth.config;

import cn.lijilong.zauth.util.jwt.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class JwtConfig {

    @Resource
    ZAuthConfigurationProperties zAuthConfigurationProperties;

    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil(zAuthConfigurationProperties.getJwtSecret(), zAuthConfigurationProperties.getJwtIssuer(), zAuthConfigurationProperties.getJwtDefTimeOut());
    }

}
