//package com.blue.utils;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//
//import java.security.Key;
//import java.util.Date;
//
//public class JwtUtil {
//    //设置密钥，密钥随机生成
//    private static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//    //设置过期时间7天
//    private static final long expirationMillis = 1000 * 60 * 60 * 24 * 7;
//
//    //生成token，有用户名和角色参数，可以自行增加参数，在claim中添加，其中subject代表唯一标识，可以用id而不是username
//    public static String generateToken(String username, String role){
//        Date now = new Date();
//        Date expirationTime = new Date(now.getTime() + expirationMillis);
//        return Jwts.builder()
//                .setSubject(username)
//                .claim("role", role)
//                .setIssuedAt(now)
//                .setExpiration(expirationTime)
//                .signWith(secretKey, SignatureAlgorithm.HS256)
//                .compact();
//    }
//    //验证token有效性
//    public static boolean validationToken(String token){
//        try{
//            Jwts.parserBuilder()
//                    .setSigningKey(secretKey)
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (Exception e){
////            e.printStackTrace();
//            return false;
//        }
//    }
//    //解析token，可以从返回的claims中获取token中的信息比如用户名，用户角色
//    public static Claims parseToken(String token){
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}