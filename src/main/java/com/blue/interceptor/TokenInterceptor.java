package com.blue.interceptor;

import com.blue.utils.RedisUtil;
import com.blue.utils.TokenUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {


    @Autowired private RedisUtil redisUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
        //跨域请求会首先发一个option请求，直接返回正常状态并通过拦截器
        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("Authorization");

        if(token==null)
        {
            JSONObject json=new JSONObject();
            json.put("msg","token verify fail");
            json.put("code","500");
            response.getWriter().append(json.toString());
            System.out.print("message=token过期：解析成功，但是和redis不一致");
            return false;
        }
        String uid = TokenUtils.getUID(token);

        String redistoken = (String) redisUtil.get(uid);

        if (redistoken == null || !redistoken.equals(token)) {
            JSONObject json=new JSONObject();
            json.put("msg","token verify fail");
            json.put("code","500");
            response.getWriter().append(json.toString());
            System.out.print("message=token过期：解析成功，但是和redis不一致");
            return false;
        }
//        String token = null;
//        Cookie[] cookies = request.getCookies();
//        for (Cookie c :
//                cookies) {
//            if (c.getName().equals("token")) {
//                token=c.getValue();
//            }
//        }
        System.out.println(token);
        if (token!=null){
            boolean result= TokenUtils.verify(token);
            if (result){
                System.out.println("通过拦截器");
                return true;
            }
        }

        try {
            JSONObject json=new JSONObject();
            json.put("msg","token verify fail");
            json.put("code","500");
            response.getWriter().append(json.toString());
            System.out.println("认证失败，未通过拦截器");
        } catch (Exception e) {
            return false;
        }
        /**
         * 还可以在此处检验用户存不存在等操作
         */
        return false;
    }
}
