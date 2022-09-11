package cn.lijilong.zauth.annotation;


import cn.lijilong.zauth.service.ZAuthCallBack;
import cn.lijilong.zauth.service.impl.ZAuthCallBackImpl;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ZAuthFilter {

    //开放访问
    boolean any() default false;

    //登陆即可访问
    boolean haveToken() default false;

    //签名访问
    boolean sign() default false;

    //包含权限
    String[] haveAuths() default {};
    //包含角色
    String[] haveRoles() default {};
    //包含组
    String[] haveGroups() default {};
    //包含用户
    String[] haveUsers() default {};

    //排除权限
    String[] excludeAuth() default {};
    //排除角色
    String[] excludeRole() default {};
    //排除组
    String[] excludeGroup() default {};
    //排除用户
    String[] excludeUser() default {};

    //回调函数
    Class<? extends ZAuthCallBack> callBack() default ZAuthCallBackImpl.class;

}
