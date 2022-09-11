package cn.lijilong.zauth.interceptor;

import cn.lijilong.zauth.annotation.ZAuthFilter;
import cn.lijilong.zauth.config.ZAuthConfigurationProperties;
import cn.lijilong.zauth.exception.AuthInterceptException;
import cn.lijilong.zauth.service.ZAuthCallBack;
import cn.lijilong.zauth.service.ZAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;


@Component
@Slf4j
public class ZAuthInterceptor implements HandlerInterceptor {

    @Resource
    private ZAuthConfigurationProperties zAuthConfigurationProperties;
    @Resource
    ZAuthService zAuthService;
    @Resource
    ZAuthCallBack zAuthCallBack;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        boolean showLog = zAuthConfigurationProperties.getLogMode() == ZAuthConfigurationProperties.LogMode.DEBUG;
        HandlerMethod method;
        try {
            method = (HandlerMethod) handler;
        } catch (ClassCastException classCastException) {
            if (handler instanceof ParameterizableViewController || handler instanceof ResourceHttpRequestHandler) {
                return true;
            }
            if (showLog) {
                log.info(">>> === API Guard === Intercept === >>>");
            }
            throw new AuthInterceptException(">>> === API Guard === Intercept === >>>",true);
        }
        boolean haveZAuthAnnotation = false;
        for (Annotation annotation : method.getMethod().getAnnotations()) {
            if (annotation instanceof ZAuthFilter) {
                haveZAuthAnnotation = true;
                ZAuthFilter zAuthFilter = (ZAuthFilter) annotation;
                if (zAuthFilter.any()) {
                    if (showLog) {
                        log.info(">>> === API Guard === Method === " + handler + " === >>>");
                        log.info(">>> === API Guard === Any Request Pass === >>> ");
                    }
                    return true;
                }
            }
        }

        if (zAuthConfigurationProperties.isApiGuard()) {
            if (!haveZAuthAnnotation) {
                if (showLog) {
                    log.info(">>> === API Guard === Method === " + handler + " === >>>");
                }
                if (zAuthService.sign(zAuthCallBack) || zAuthService.haveToken(zAuthCallBack)) {
                    if (showLog) {
                        log.info(">>> === API Guard === Match Success === >>>");
                    }
                    return true;
                } else {
                    if (showLog) {
                        log.info(">>> === API Guard === Intercept === >>>");
                    }
                    throw new AuthInterceptException(">>> === API Guard === Intercept === >>>", true);
                }
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
