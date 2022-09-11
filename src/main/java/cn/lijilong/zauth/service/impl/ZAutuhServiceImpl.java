package cn.lijilong.zauth.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.lijilong.zauth.service.*;
import cn.lijilong.zcache.ZCache;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;
import cn.lijilong.zauth.entity.ClientEntity;
import cn.lijilong.zauth.exception.AuthInterceptException;
import cn.lijilong.zauth.util.ZAuthContextUtil;
import cn.lijilong.zauth.util.jwt.JWTUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ZAutuhServiceImpl implements ZAuthService, ZAuthCenter {

    @Resource
    private ZAuthContextUtil zAuthContextUtil;
    @Resource
    AuthorityService authorityService;
    @Resource
    RoleService roleService;
    @Resource
    UserGroupService userGroupService;
    @Resource
    UserService userService;
    @Resource
    ClientService clientService;
    @Resource
    JWTUtil jwtUtil;
    @Resource
    ZCache zCache;


    @Override
    public boolean haveAuth(String[] values, ZAuthCallBack callBack) {
        if (!haveToken(callBack)) {
            return false;
        }
        List<String> auths = new ArrayList<>(getAllAuth(zAuthContextUtil.getUid()));
        for (String value : values) {
            if (auths.contains(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean haveRole(String[] values, ZAuthCallBack callBack) {
        if (!haveToken(callBack)) {
            return false;
        }
        List<String> roles = new ArrayList<>(getAllRole(zAuthContextUtil.getUid()));
        for (String value : values) {
            if (roles.contains(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean haveGroup(String[] values, ZAuthCallBack callBack) {
        if (!haveToken(callBack)) {
            return false;
        }
        List<String> groups = new ArrayList<>(getAllGroup(zAuthContextUtil.getUid()));
        for (String value : values) {
            if (groups.contains(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean haveUser(String[] values, ZAuthCallBack callBack) {
        if (!haveToken(callBack)) {
            return false;
        }
        String uid = String.valueOf(zAuthContextUtil.getUid());
        for (String value : values) {
            if (value.equals(uid)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean excludeAuth(String[] values, ZAuthCallBack callBack) {
        return !haveAuth(values, callBack);
    }

    @Override
    public boolean excludeRole(String[] values, ZAuthCallBack callBack) {
        return !haveRole(values, callBack);
    }

    @Override
    public boolean excludeGroup(String[] values, ZAuthCallBack callBack) {
        return !haveGroup(values, callBack);
    }

    @Override
    public boolean excludeUser(String[] values, ZAuthCallBack callBack) {
        return !haveUser(values, callBack);
    }

    @Override
    public boolean haveToken(ZAuthCallBack callBack) {

        String token = ZAuthContextUtil.getToken();
        if (token == null) {
            throw new AuthInterceptException(">>> === Have Token Intercept === >>> ", true);
        }
        DecodedJWT jwt = jwtUtil.verify(token);
        String redisToken = zCache.getObj("ut-" + jwt.getAudience().get(0), String.class);
        if (redisToken == null) {
            throw new AuthInterceptException(">>> === Have Token Intercept === >>> ", true);
        }
        if (!redisToken.equals(token)) {
            throw new AuthInterceptException(">>> === Have Token Intercept === >>> ", true);
        }
        return true;
    }

    @Override
    public boolean sign(ZAuthCallBack callBack) {
        HttpServletRequest httpServletRequest = ZAuthContextUtil.getRequest();
        String appid = httpServletRequest.getHeader("appid");
        String random = httpServletRequest.getHeader("random");
        String sign = httpServletRequest.getHeader("sign");
        if (appid == null || random == null || sign == null) {
            return false;
        }

        ClientEntity clientEntity = clientService.queryByAppid(appid);
        return SecureUtil.md5(appid + random + clientEntity.getAppSecret()).equals(sign);
    }

    @Override
    public Collection<String> getAllAuth(Long uid) {
        return authorityService.queryByUserId(uid);
    }

    @Override
    public Collection<String> getAllRole(Long uid) {
        return roleService.queryByUserId(uid);
    }

    @Override
    public Collection<String> getAllGroup(Long uid) {
        return userGroupService.queryByUserId(uid);
    }

    @Override
    public String getUserName(Long uid) {
        return userService.queryById(uid).getUserName();
    }

}
