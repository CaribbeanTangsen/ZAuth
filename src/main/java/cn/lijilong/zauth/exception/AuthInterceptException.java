package cn.lijilong.zauth.exception;

import lombok.Getter;

@Getter
public class AuthInterceptException extends RuntimeException{

    private boolean reLogin;

    public AuthInterceptException(String msg, boolean reLogin) {
        super(msg);
        this.reLogin = reLogin;
    }

}