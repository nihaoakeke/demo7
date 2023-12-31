package com.blue.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.blue.domain.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class TokenUtils {

    //token到期时间7天
    private static final long EXPIRE_TIME= 7*24*60*60*1000;
    //密钥盐
    private static final String TOKEN_SECRET="ljdyaishijin**3nkjnj??";

    /**
     * 生成token
     * @param user
     * @return
     */
    public static String sign(User user,String ip){

        System.out.println(user.getUId());
        System.out.println(user.getUFlag());
        String token=null;
        System.out.println("jingguole----------->>>>>>>>>>>>>>>>>>");
        try {
            Date expireAt=new Date(System.currentTimeMillis()+EXPIRE_TIME);
            token = JWT.create()
                    //发行人
//                    .withIssuer("auth0")
                    .withIssuer("Authorization")
                    //存放数据
                    .withClaim("username",user.getUname())
                    .withClaim("uid",user.getUId().toString())
                    .withClaim("uflag",user.getUFlag().toString())
                    .withClaim("uip",ip.toString())
                    //过期时间
                    .withExpiresAt(expireAt)
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (IllegalArgumentException | JWTCreationException je) {

        }
        return token;
    }


    /**
     * token验证
     * @param token
     * @return
     */
    public static Boolean verify(String token){

        try {
            //创建token验证器 //withIssuer("auth0")
            JWTVerifier jwtVerifier= JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("Authorization").build();
            DecodedJWT decodedJWT=jwtVerifier.verify(token);
            System.out.println("认证通过：");
            System.out.println("username: " + decodedJWT.getClaim("username").asString());
            System.out.println("uid"+decodedJWT.getClaim("uid").asString());
            System.out.println("过期时间：      " + decodedJWT.getExpiresAt());
        } catch (IllegalArgumentException | JWTVerificationException e) {
            //抛出错误即为验证不通过
            return false;
        }
        return true;
    }

    public static String getUID(String token)
    {
        JWTVerifier jwtVerifier= JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("Authorization").build();
        DecodedJWT decodedJWT=jwtVerifier.verify(token);
        return decodedJWT.getClaim("uid").asString();
    }
    public static String getuFlag(String token)
    {
        JWTVerifier jwtVerifier= JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("Authorization").build();
        DecodedJWT decodedJWT=jwtVerifier.verify(token);
        return decodedJWT.getClaim("uflag").asString();
    }
    public static String getuIp(String token)
    {
        JWTVerifier jwtVerifier= JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("Authorization").build();
        DecodedJWT decodedJWT=jwtVerifier.verify(token);
        return decodedJWT.getClaim("uIp").asString();
    }



//    public String getToken(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        for (Cookie c :
//                cookies) {
//            if (c.getName().equals("token")) {
//                return c.getValue();
//            }
//        }
//        return null;
//
////        JWTVerifier jwtVerifier= JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("Authorization").build();
////        DecodedJWT decodedJWT=jwtVerifier.verify(token);
////        return decodedJWT.getClaim("uId").asString();
//
//    }
//
//    public static HttpServletRequest getRequest() {
//        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
//                .getRequestAttributes();
//        return requestAttributes == null ? null : requestAttributes.getRequest();
//    }




}