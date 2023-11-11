package com.blue.utils;

import com.google.gson.Gson;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Objects;

public class JsonUtils {
    public static <T> T simpleJsonToObj(String json, Class<T> cls) {
        Gson gson = new Gson();
        if(Objects.isNull(json)) {
            return null;
        }
        T obj = gson.fromJson(json, cls);
        if (Objects.isNull(obj)) {
            return null;
        } else {
            return obj;
        }
    }
    /**
     * 简单对象转Json
     *
     * @param obj
     * @return
     */
    public static String simpleObjToJson(Object obj) {
        if (Objects.isNull(obj)) {
            return "";
        }
        try {
            Gson gson = new Gson();
            return gson.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static <T> String toJson(T t){
        Gson gson = new Gson();
        return gson.toJson(t);
    }

    //json转成其他类
    public static <T> T jsonTo(String json,Class<T> tClass){
        Gson gson = new Gson();
        return gson.fromJson(json,(Type) tClass);
    }

    public static String jsontostring(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder stringBuilder = null;
        String jsonString = null;
// 使用缓冲字符流（直接使用通过request得到的字节流也可以）
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
        stringBuilder = new StringBuilder();
        String str = "";
        while ((str = bufferedReader.readLine()) != null) {
            stringBuilder.append(str);
        }
        bufferedReader.close();
        // 获取处理好的json字符串
        jsonString = stringBuilder.toString();
        return jsonString;
    }
    public static String jsontostring(ServletRequest request, ServletResponse response) throws IOException {
        StringBuilder stringBuilder = null;
        String jsonString = null;
// 使用缓冲字符流（直接使用通过request得到的字节流也可以）
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
        stringBuilder = new StringBuilder();
        String str = "";
        while ((str = bufferedReader.readLine()) != null) {
            stringBuilder.append(str);
        }
        bufferedReader.close();
        // 获取处理好的json字符串
        jsonString = stringBuilder.toString();
        return jsonString;
    }

}
