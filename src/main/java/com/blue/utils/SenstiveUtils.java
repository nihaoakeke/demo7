package com.blue.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SenstiveUtils {
    private static Map sensitiveWords = null;
    private static List<Character> separatorWords = null;

//    List<String> list, List<Character> separator
    public static void init() {
        List<String> list = new ArrayList<>(); List<Character> separator = new ArrayList<>();
        list.add("sb");
        list.add("傻逼");
        list.add("cnm");
        list.add("傻瓜");
        list.add("你妈");
        list.add("他妈的");
        separator.add(',');
        separator.add('，');
        separator.add('/');
        separator.add('。');
        separator.add(';');
        separator.add('；');
        separator.add(' ');
        separator.add('?');
        separator.add('？');
        separator.add('.');
        separator.add('-');
//        SenstiveUtils.init(list,separator);
        separatorWords = separator;

        if (list != null && !list.isEmpty()) {
            sensitiveWords = new HashMap<>(list.size());
            Map nowMap = null;
            for (String key : list) {
                nowMap = sensitiveWords;
                for (int i = 0; i < key.length(); i++) {
                    char keyChar = key.charAt(i);
                    if (!nowMap.containsKey(keyChar)) {
                        Map newWorMap = new HashMap<>();
                        newWorMap.put("isEnd", "0");
                        nowMap.put(keyChar, newWorMap);
                    }
                    nowMap = (Map) nowMap.get(keyChar);
                    if (i == key.length() - 1) {
                        nowMap.put("isEnd", "1");
                    }
                }
            }
        }

    }
    public static String replaceSensitiveWord(String txt) {
        if (sensitiveWords != null) {
            StringBuilder sb = new StringBuilder(txt);
            for (int i = 0, size = txt.length(); i < size; i++) {
                int length = CheckSensitiveWord(txt, i);
                if (length > 0) {
                    int end = i + length;
                    sb.replace(i, end, star(length));
                    i = end - 1;
                }
            }

            return sb.toString();
        }
        return txt;
    }
    @SuppressWarnings({ "rawtypes" })
    private static int CheckSensitiveWord(String txt, int begin) {
        int temp = 0, match = 0;
        Map nowMap = sensitiveWords;
        for (int i = begin; i < txt.length(); i++) {
            char word = txt.charAt(i);
            if (separatorWords != null && separatorWords.contains(word)) {
                temp++;
                continue;
            }
            nowMap = (Map) nowMap.get(word);
            if (nowMap != null) {
                temp++;
                if ("1".equals(nowMap.get("isEnd").toString())) {
                    match = temp;
                }
            } else{
                break;}
        }
        return match;
    }
    private static String star(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append("*");
        }
        return sb.toString();
    }

}

