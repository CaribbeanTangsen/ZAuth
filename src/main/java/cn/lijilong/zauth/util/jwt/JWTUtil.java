package cn.lijilong.zauth.util.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

public class JWTUtil {

    private final Algorithm algorithmHS;
    private final String issuer;
    private final int defaultTimeOut;

    public JWTUtil(String secret, String issuer,int defaultTimeOut) {
        this.issuer = issuer;
        this.defaultTimeOut = defaultTimeOut;
        algorithmHS = Algorithm.HMAC256(secret);
    }

    /**
     * @param aud     颁发给
     * @param nbf     生效日期
     * @param timeOut 过期时间(小时)
     * @param map     自定义参数
     * @return jwt
     */
    public String makeJwt(String aud, Long nbf, Integer timeOut, Map<String, ?> map) {
        JWTCreator.Builder builder = JWT.create()
                .withIssuer(issuer)
                .withAudience(aud)
                .withExpiresAt(new Date());
        if (nbf != null) {
            builder.withNotBefore(new Date(nbf));
        }

        builder.withExpiresAt(new Date(System.currentTimeMillis() + (timeOut == null ? defaultTimeOut : timeOut) * 60 * 60 * 1000));

        if (map != null) {
            builder.withPayload(map);
        }
        return builder.sign(algorithmHS);
    }


    /**
     * @param jwt jwt字符串
     * @return 解析结果
     * @throws JwtVerifyException 错误信息
     */

    public DecodedJWT verify(String jwt) throws JwtVerifyException {
        try {
            JWTVerifier verifier = JWT.require(algorithmHS)
                    .withIssuer(issuer)
                    .build();
            return verifier.verify(jwt);
        } catch (JWTVerificationException jwtVerificationException) {
            JwtVerifyExceptionEnum jwtVerifyExceptionEnum;
            if (jwtVerificationException.getClass().equals(TokenExpiredException.class)) {
                jwtVerifyExceptionEnum = JwtVerifyExceptionEnum.TIME_OUT;
            } else {
                jwtVerifyExceptionEnum = JwtVerifyExceptionEnum.VERIFY_FAIL;
            }
            throw new JwtVerifyException(jwtVerifyExceptionEnum);
        }

    }
}
