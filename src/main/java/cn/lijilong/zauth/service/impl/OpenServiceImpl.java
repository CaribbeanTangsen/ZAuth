package cn.lijilong.zauth.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.lijilong.zauth.config.ZAuthConfigurationProperties;
import cn.lijilong.zauth.exception.*;
import cn.lijilong.zcache.ZCache;
import com.auth0.jwt.interfaces.DecodedJWT;
import cn.lijilong.zauth.dao.AuthorityDao;
import cn.lijilong.zauth.dao.ClientDao;
import cn.lijilong.zauth.dao.RoleDao;
import cn.lijilong.zauth.dao.UserDao;
import cn.lijilong.zauth.dto.LoginDTO;
import cn.lijilong.zauth.dto.LoginStateDTO;
import cn.lijilong.zauth.entity.ClientEntity;
import cn.lijilong.zauth.service.OpenService;
import cn.lijilong.zauth.util.jwt.JWTUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.lijilong.zauth.entity.AuthorityEntity;
import cn.lijilong.zauth.entity.RoleEntity;
import cn.lijilong.zauth.entity.UserEntity;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
public class OpenServiceImpl implements OpenService {

    @Resource
    ClientDao clientDao;
    @Resource
    UserDao userDao;
    @Resource
    JWTUtil jwtUtil;
    @Resource
    ZAuthConfigurationProperties zAuthConfigurationProperties;
    @Resource
    ZCache zCache;
    @Resource
    AuthorityDao authorityDao;
    @Resource
    RoleDao roleDao;

    @Override
    public LoginStateDTO login(LoginDTO loginDTO) {
        ClientEntity clientEntity = clientDao.findByAppId(loginDTO.getAppId()).orElseThrow(() -> new NullEntityException("appid错误！"));
        if (!(loginDTO.getMode().equals("authority_code") || loginDTO.getMode().equals("refToken"))) {
            if (!clientEntity.getAuthMethod().contains(loginDTO.getMode())) {
                throw new NotSupperRequestException("不支持的授权方式！");
            }
        }
        switch (loginDTO.getMode()) {
            case "code":
                return loginForCodeMode(loginDTO);
            case "password":
                return loginForPasswordMode(loginDTO);
            case "agent":
                return loginForAgentMode(loginDTO);
            case "authority_code":
                return loginForCode(loginDTO);
            case "refToken":
                return loginForRefToken(loginDTO);
            default:
                throw new NotSupperRequestException("不支持的授权方式！");
        }
    }

    private LoginStateDTO loginForRefToken(LoginDTO loginDTO) {
        if (loginDTO.getRefToken() == null || loginDTO.getRefToken().equals("")) {
            throw new ArgsNotSupportException("请补全参数！");
        }

        DecodedJWT jwt = jwtUtil.verify(loginDTO.getRefToken());
        Long uid;
        try {
            uid = Long.valueOf(jwt.getAudience().get(0));
        } catch (Exception e) {
            throw new TokenInvalidException("刷新Token无效!");
        }

        Set<String> authorityTags = authorityDao.findAllByUserId(uid).stream().map(AuthorityEntity::getAuthMark).collect(Collectors.toSet());
        Set<String> roleTags = roleDao.findAllByUserId(uid).stream().map(RoleEntity::getTag).collect(Collectors.toSet());
        Map<String, Collection<String>> userInfo = new HashMap<>();
        userInfo.put("authorityTags", new ArrayList<>(authorityTags));
        userInfo.put("roleTags", new ArrayList<>(roleTags));

        UserEntity userEntity = userDao.findById(uid).orElseThrow(() -> new NullEntityException("无效的用户信息！"));

        String token = jwtUtil.makeJwt(String.valueOf(uid), null, zAuthConfigurationProperties.getTokenDefTimeOut(), userInfo);

        if (userEntity.getToken() != null && userEntity.getRepeatOnline()) {
            try {
                jwtUtil.verify(userEntity.getToken());
                token = userEntity.getToken();
            } catch (Exception ignored) { }
        }

        String redisTokenKey = "ut-" + userEntity.getId();
        zCache.setObj(redisTokenKey, token, zAuthConfigurationProperties.getTokenDefTimeOut(), TimeUnit.HOURS);

        userEntity.setToken(token);

        LoginStateDTO loginStateDTO = new LoginStateDTO();

        loginStateDTO.setToken(token);
        loginStateDTO.setState(true);

        return loginStateDTO;
    }

    private LoginStateDTO loginForCode(LoginDTO loginDTO) {
        if (loginDTO.getCode() == null || loginDTO.getAppId() == null || loginDTO.getClientSecret() == null) {
            throw new ArgsNotSupportException("请补全参数！");
        }

        String codeKey = "uc-" + loginDTO.getCode();
        Long uid = zCache.getObj(codeKey, Long.class);
        if (uid == null) {
            throw new ArgsNotSupportException("错误的code！");
        }

        zCache.delete(codeKey);

        ClientEntity clientEntity = clientDao.findByAppId(loginDTO.getAppId()).orElseThrow(() -> new NullEntityException("无效的客户端信息！"));
        if (!clientEntity.getAppSecret().equals(loginDTO.getClientSecret())) {
            throw new ArgsNotSupportException("无效的客户端信息！");
        }

        UserEntity userEntity = userDao.findById(uid).orElseThrow(() -> new NullEntityException("无效的用户信息！"));

        if (!userEntity.getEnable()) {
            throw new UserException("用户已被禁用！");
        }
        if (userEntity.getTimeoutDate() != null && userEntity.getTimeoutDate() < System.currentTimeMillis()) {
            throw new UserException("用户已过期！");
        }

        //获取用户角色权限相关信息
        Set<String> authorityTags = authorityDao.findAllByUserId(userEntity.getId()).stream().map(AuthorityEntity::getAuthMark).collect(Collectors.toSet());
        Set<String> roleTags = roleDao.findAllByUserId(userEntity.getId()).stream().map(RoleEntity::getTag).collect(Collectors.toSet());
        Map<String, Collection<String>> userInfo = new HashMap<>();
        userInfo.put("authorityTags", new ArrayList<>(authorityTags));
        userInfo.put("roleTags", new ArrayList<>(roleTags));


        //生成token
        // TODO: 2022/5/24 传入权限角色信息
        String token = jwtUtil.makeJwt(String.valueOf(userEntity.getId()), null, zAuthConfigurationProperties.getTokenDefTimeOut(), userInfo);
        String refToken = jwtUtil.makeJwt(String.valueOf(userEntity.getId()), null, zAuthConfigurationProperties.getRefTokenDefTimeOut(), null);
        if (userEntity.getToken() != null && userEntity.getRepeatOnline()) {
            try {
                jwtUtil.verify(userEntity.getToken());
                token = userEntity.getToken();
            } catch (Exception ignored) { }
            if (userEntity.getRefreshToken() != null) {
                try {
                    jwtUtil.verify(userEntity.getRefreshToken());
                    refToken = userEntity.getRefreshToken();
                }catch (Exception ignored){ }
            }
        }

        //同步到redis
        String redisTokenKey = "ut-" + userEntity.getId();
        zCache.setObj(redisTokenKey, token, zAuthConfigurationProperties.getTokenDefTimeOut(), TimeUnit.HOURS);

        userEntity.setLastLogin(System.currentTimeMillis());
        userEntity.setToken(token);
        userEntity.setRefreshToken(refToken);
        LoginStateDTO loginStateDTO = new LoginStateDTO();

        loginStateDTO.setToken(token);
        loginStateDTO.setRefToken(refToken);
        loginStateDTO.setState(true);

        return loginStateDTO;

    }

    private LoginStateDTO loginForCodeMode(LoginDTO loginDTO) {

        UserEntity userEntity = verifyUser(loginDTO);

        LoginStateDTO loginStateDTO = new LoginStateDTO();

        String code = IdUtil.fastSimpleUUID();
        String redisCodeKey = "uc-" + code;
        zCache.setObj(redisCodeKey, userEntity.getId(), 1, TimeUnit.MINUTES);

        loginStateDTO.setState(true);
        loginStateDTO.setCode(code);
        loginStateDTO.setRedirectUri(loginDTO.getRedirectUri() + "?code=" + code);

        return loginStateDTO;
    }

    private LoginStateDTO loginForPasswordMode(LoginDTO loginDTO) {

        UserEntity userEntity = verifyUser(loginDTO);


        //获取用户角色权限相关信息
        Set<String> authorityTags = authorityDao.findAllByUserId(userEntity.getId()).stream().map(AuthorityEntity::getAuthMark).collect(Collectors.toSet());
        Set<String> roleTags = roleDao.findAllByUserId(userEntity.getId()).stream().map(RoleEntity::getTag).collect(Collectors.toSet());
        Map<String, Collection<String>> userInfo = new HashMap<>();
        userInfo.put("authorityTags", new ArrayList<>(authorityTags));
        userInfo.put("roleTags", new ArrayList<>(roleTags));

        //生成token
        // TODO: 2022/5/24 传入权限角色信息
        String token = jwtUtil.makeJwt(String.valueOf(userEntity.getId()), null, zAuthConfigurationProperties.getTokenDefTimeOut(), userInfo);
        String refToken = jwtUtil.makeJwt(String.valueOf(userEntity.getId()), null, zAuthConfigurationProperties.getRefTokenDefTimeOut(), null);
        if (userEntity.getToken() != null && userEntity.getRepeatOnline()) {
            try {
                jwtUtil.verify(userEntity.getToken());
                token = userEntity.getToken();
            } catch (Exception ignored) { }
            if (userEntity.getRefreshToken() != null) {
                try {
                    jwtUtil.verify(userEntity.getRefreshToken());
                    refToken = userEntity.getRefreshToken();
                }catch (Exception ignored){ }
            }
        }

        //同步到redis
        String redisTokenKey = "ut-" + userEntity.getId();
        zCache.setObj(redisTokenKey, token, zAuthConfigurationProperties.getTokenDefTimeOut(), TimeUnit.HOURS);


        userEntity.setLastLogin(System.currentTimeMillis());
        userEntity.setToken(token);
        userEntity.setRefreshToken(refToken);
        LoginStateDTO loginStateDTO = new LoginStateDTO();

        loginStateDTO.setToken(token);
        loginStateDTO.setRefToken(refToken);
        loginStateDTO.setRedirectUri(loginDTO.getRedirectUri());
        loginStateDTO.setState(true);

        return loginStateDTO;
    }

    private LoginStateDTO loginForAgentMode(LoginDTO loginDTO) {
        loginDTO.setRedirectUri(null);
        return loginForPasswordMode(loginDTO);
    }

    private UserEntity verifyUser(LoginDTO loginDTO) {
        //判断用户合法性
        List<UserEntity> users = userDao.findAllByEmailOrPhoneOOrName(loginDTO.getUsername());
        if (users == null || users.size() != 1) {
            throw new UserException("用户名或密码错误！");
        }
        UserEntity userEntity = users.get(0);

        if (!userEntity.getAuthMethod().contains(loginDTO.getMode())) {
            throw new NotSupperRequestException("未授权的登陆方式！");
        }
        if (!userEntity.getEnable()) {
            throw new UserException("用户已被禁用！");
        }
        if (userEntity.getTimeoutDate() != null && userEntity.getTimeoutDate() < System.currentTimeMillis()) {
            throw new UserException("用户已过期！");
        }
        if (!SecureUtil.md5(loginDTO.getPassword() + ":" + userEntity.getSalt()).equals(userEntity.getPassword())) {
            throw new UserException("用户名或密码错误！");
        }
        return userEntity;
    }
}
