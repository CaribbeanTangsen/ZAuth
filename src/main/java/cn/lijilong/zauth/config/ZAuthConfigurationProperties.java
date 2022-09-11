package cn.lijilong.zauth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = ZAuthConfigurationProperties.CONFIG_PREFIX)
public class ZAuthConfigurationProperties {

    public static final String CONFIG_PREFIX = "zauth.config";

    //jwt密钥
    private String jwtSecret;
    //jwt颁发者
    private String jwtIssuer;
    //jwt有效时间
    private int JwtDefTimeOut = 1;

    //token过期时间
    private int tokenDefTimeOut = 1;
    //刷新token过期时间
    private int refTokenDefTimeOut = 48;
    //API守卫
    private boolean apiGuard = true;

    //日志模式
    private LogMode logMode = LogMode.DEBUG;

    public enum LogMode {
        DEBUG,PRODUCE
    }

}


