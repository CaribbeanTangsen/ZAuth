package cn.lijilong.zauth.util.jwt;


import lombok.Getter;

public class JwtVerifyException extends RuntimeException {

    @Getter
    private final JwtVerifyExceptionEnum JwtVerifyExceptionEnum;

    public JwtVerifyException(JwtVerifyExceptionEnum JwtVerifyExceptionEnum) {
        this.JwtVerifyExceptionEnum = JwtVerifyExceptionEnum;
    }

}
