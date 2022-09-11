package cn.lijilong.zauth.config;

import cn.lijilong.zauth.exception.AuthInterceptException;
import cn.lijilong.zauth.util.jwt.JwtVerifyException;
import cn.lijilong.zauth.util.jwt.JwtVerifyExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ExceptionController {


    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public RequestDataDTO<Object> integrityConstraintError(SQLIntegrityConstraintViolationException e) {
        log.error(e.getClass() + " ------ " + e.getSQLState());
        return RequestDataDTO.buildError(-1, e.getMessage());
    }

    @ExceptionHandler(value = AuthInterceptException.class)
    public RequestDataDTO<Object> authInterceptError(AuthInterceptException e) {
        log.warn(e.getClass() + " ^^^ " + e.getMessage());
        return RequestDataDTO.buildError(e.isReLogin() ? 302 : 401, e.getMessage());
    }

    @ExceptionHandler(value = JwtVerifyException.class)
    public RequestDataDTO<Object> JWTVerificationError(JwtVerifyException e) {
        log.warn("JWT Verification Fail! ---> " + e.getJwtVerifyExceptionEnum());
        return RequestDataDTO.buildError(e.getJwtVerifyExceptionEnum() == JwtVerifyExceptionEnum.TIME_OUT ? 407 : 302, e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public RequestDataDTO<Object> error(Exception e) {
        log.error(e.getClass() + " ------ " + e.getMessage());
        return RequestDataDTO.buildError(-1, e.getMessage());
    }


}
