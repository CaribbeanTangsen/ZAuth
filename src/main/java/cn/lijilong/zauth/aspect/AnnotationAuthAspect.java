package cn.lijilong.zauth.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import cn.lijilong.zauth.annotation.ZAuthFilter;
import cn.lijilong.zauth.config.ZAuthConfigurationProperties;
import cn.lijilong.zauth.exception.AuthInterceptException;
import cn.lijilong.zauth.service.ZAuthCallBack;
import cn.lijilong.zauth.service.ZAuthService;
import cn.lijilong.zauth.util.ZAuthContextUtil;

import javax.annotation.Resource;

@Aspect
@Slf4j
@Component
public class AnnotationAuthAspect {

    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private ZAuthConfigurationProperties zAuthConfigurationProperties;
    @Resource
    ZAuthService zAuthService;


    @Pointcut("@annotation(cn.lijilong.zauth.annotation.ZAuthFilter)")
    public void zAuthFilter() {
    }

    @Before("zAuthFilter()")
    public void authFilter(JoinPoint joinPoint) throws Throwable {

        boolean showLog = zAuthConfigurationProperties.getLogMode() == ZAuthConfigurationProperties.LogMode.DEBUG;
        String classPathStr = joinPoint.getStaticPart().toLongString();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        ZAuthFilter zAuthFilter = methodSignature.getMethod().getAnnotation(ZAuthFilter.class);
        ZAuthCallBack zAuthCallBack = null;
        try {
            zAuthCallBack = applicationContext.getBean(zAuthFilter.callBack());
        } catch (Exception ignored) {
        }

        if (showLog) {
            log.info(">>> === Method Start === >>> " + classPathStr + " >>> === Auth Info === >>> " + zAuthFilter);
            log.info(">>> === Token === >>> " + ZAuthContextUtil.getToken());
        }

        boolean doFilter = true;

        if (zAuthFilter.any()) {
            if (showLog)
                log.info(">>> === Any Match Success === >>> " + classPathStr);
            return;
        }

        if (zAuthFilter.haveToken()) {
            if (showLog)
                log.info(">>> === Have Token Matching=== >>> " + classPathStr);

            doFilter = zAuthService.haveToken(zAuthCallBack);

            if (!doFilter) {
                if (showLog)
                    log.error(">>> === Have Token Intercept === >>> " + classPathStr);
                throw new AuthInterceptException(">>> === Have Token Intercept === >>> " + classPathStr, true);
            }

            if (showLog)
                log.info(">>> === Have Token Match Success === >>> " + classPathStr);
            return;
        }

        if (zAuthFilter.sign()) {
            if (showLog)
                log.info(">>> === Sign Matching=== >>> " + classPathStr);

            doFilter = zAuthService.sign(zAuthCallBack);

            if (!doFilter) {
                if (showLog)
                    log.error(">>> === Sign Intercept === >>> " + classPathStr);
                if (zAuthFilter.haveAuths().length > 0 && zAuthFilter.haveRoles().length > 0 || zAuthFilter.haveGroups().length > 0 || zAuthFilter.haveUsers().length > 0 || zAuthFilter.excludeAuth().length > 0 || zAuthFilter.excludeGroup().length > 0 || zAuthFilter.excludeRole().length > 0 || zAuthFilter.excludeUser().length > 0) {
                    throw new AuthInterceptException(">>> === Sign Intercept === >>> " + classPathStr, true);
                }
            }

            if (showLog)
                log.info(">>> === Sign Match Success === >>> " + classPathStr);
            return;
        }

        if (zAuthFilter.haveAuths().length > 0) {
            if (showLog)
                log.info(">>> === Have Auth Matching=== >>> " + classPathStr);

            doFilter = zAuthService.haveAuth(zAuthFilter.haveAuths(), zAuthCallBack);

            if (!doFilter) {
                if (showLog)
                    log.error(">>> === Have Auth Intercept === >>> " + classPathStr);
                throw new AuthInterceptException(">>> === Have Auth Intercept === >>> " + classPathStr,false);
            }

            if (showLog)
                log.info(">>> === Have Auth Match Success === >>> " + classPathStr);
        } else if (zAuthFilter.haveRoles().length > 0) {
            if (showLog)
                log.info(">>> === Have Role Matching === >>> " + classPathStr);

            doFilter = zAuthService.haveRole(zAuthFilter.haveRoles(), zAuthCallBack);

            if (!doFilter) {
                if (showLog)
                    log.error(">>> === Have Role Intercept === >>> " + classPathStr);
                throw new AuthInterceptException(">>> === Have Role Intercept === >>> " + classPathStr,false);
            }

            if (showLog)
                log.info(">>> === Have Role Match Success === >>> " + classPathStr);
        } else if (zAuthFilter.haveGroups().length > 0) {
            if (showLog)
                log.info(">>> === Have Group Matching === >>> " + classPathStr);

            doFilter = zAuthService.haveGroup(zAuthFilter.haveGroups(), zAuthCallBack);

            if (!doFilter) {
                if (showLog)
                    log.error(">>> === Have Group Intercept === >>> " + classPathStr);
                throw new AuthInterceptException(">>> === Have Group Intercept === >>> " + classPathStr,false);
            }

            if (showLog)
                log.info(">>> === Have Group Match Success === >>> " + classPathStr);
        } else if (zAuthFilter.haveUsers().length > 0) {
            if (showLog)
                log.info(">>> === Have User Matching === >>> " + classPathStr);

            doFilter = zAuthService.haveUser(zAuthFilter.haveUsers(), zAuthCallBack);

            if (!doFilter) {
                if (showLog)
                    log.error(">>> === Have User Intercept === >>> " + classPathStr);
                throw new AuthInterceptException(">>> === Have User Intercept === >>> " + classPathStr,false);
            }

            if (showLog)
                log.info(">>> === Have User Match Success === >>> " + classPathStr);
        } else if (zAuthFilter.excludeAuth().length > 0) {
            if (showLog)
                log.info(">>> === Exclude Auth Matching === >>> " + classPathStr);

            doFilter = zAuthService.excludeAuth(zAuthFilter.excludeAuth(), zAuthCallBack);

            if (!doFilter) {
                if (showLog)
                    log.error(">>> === Exclude Auth Intercept === >>> " + classPathStr);
                throw new AuthInterceptException(">>>=== Exclude Auth Intercept ===>>> " + classPathStr,false);
            }

            if (showLog)
                log.info(">>> === Exclude Auth Match Success === >>> " + classPathStr);
        } else if (zAuthFilter.excludeRole().length > 0) {
            if (showLog)
                log.info(">>> === Exclude Role Matching === >>> " + classPathStr);

            doFilter = zAuthService.excludeRole(zAuthFilter.excludeRole(), zAuthCallBack);

            if (!doFilter) {
                if (showLog)
                    log.error(">>> === Exclude Role Intercept === >>> " + classPathStr);
                throw new AuthInterceptException(">>> === Exclude Role Intercept === >>> " + classPathStr,false);
            }

            if (showLog)
                log.info(">>> === Exclude Role Match Success === >>> " + classPathStr);
        } else if (zAuthFilter.excludeGroup().length > 0) {
            if (showLog)
                log.info(">>> === Exclude Group Matching === >>> " + classPathStr);

            doFilter = zAuthService.excludeGroup(zAuthFilter.excludeGroup(), zAuthCallBack);

            if (!doFilter) {
                if (showLog)
                    log.error(">>> === Exclude Group Intercept === >>> " + classPathStr);
                throw new AuthInterceptException(">>> === Exclude Group Intercept === >>> " + classPathStr,false);
            }

            if (showLog)
                log.info(">>> === Exclude Group Match Success === >>> " + classPathStr);
        } else if (zAuthFilter.excludeUser().length > 0) {
            if (showLog)
                log.info(">>> === Exclude User Matching === >>> " + classPathStr);

            doFilter = zAuthService.excludeUser(zAuthFilter.excludeUser(), zAuthCallBack);

            if (!doFilter) {
                if (showLog)
                    log.error(">>> === Exclude User Intercept === >>> " + classPathStr);
                throw new AuthInterceptException(">>>=== Exclude User Intercept ===>>> " + classPathStr,false);
            }

            if (showLog)
                log.info(">>> === Exclude User Match Success === >>> " + classPathStr);
        }

        if (showLog)
            log.info(">>> === Method Pass === >>> " + classPathStr);
    }

}
