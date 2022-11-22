package com.gaia.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;


import java.util.Date;

public class JWTUtil {

    public static final String TOKEN_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    private static final long EXPIRE_TIME = 30 * 60 * 1000;

    private static final String SECRET = "jJfWeA2WkR9OLr";

    /**
     * 生成签名,30分钟后过期
     *
     * @param name 名称
     * @return 加密后的token
     */
    public static String sign(String name) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC512(SECRET);
        String token = JWT.create()
                .withClaim("name", name)
                //.withSubject(name) //信息直接放在这里也行
                .withExpiresAt(date)
                .sign(algorithm);
        return token;
    }

    /**
     * 校验token是否正确
     *
     * @param token 令牌
     * @return 是否正确
     */
    public static boolean verify(String token) {
        try{
            String name = getName(token);
            Algorithm algorithm = Algorithm.HMAC512(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("name", name)
                    //.withSubject(name)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            jwt.getToken();
            return true;
        } catch (Exception e){
            return false;
        }
    }

    /**
     * 获得token中的信息
     *
     * @return token中包含的名称
     */
    public static String getName(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("name").asString();
        }catch(Exception e){
            return null;
        }
    }

}
