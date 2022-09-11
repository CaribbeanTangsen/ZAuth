package cn.lijilong.zauth.util;

import cn.lijilong.zauth.service.ZAuthCenter;
import cn.lijilong.zauth.util.jwt.JWTUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class ZAuthContextUtil {

    @Resource
    JWTUtil jwtUtil;
    @Resource
    ZAuthCenter zAuthCenter;

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static String getToken() {
        HttpServletRequest request = getRequest();
        return request.getHeader("token");
    }

    public String getUserName() {
        return getUserName(getUid());
    }

    public String getUserName(Long uid) {
        return zAuthCenter.getUserName(uid);
    }

    public Long getUid() {
        return Long.valueOf(getJwt().getAudience().get(0));
    }

    public DecodedJWT getJwt() {
        return jwtUtil.verify(getToken());
    }





}
