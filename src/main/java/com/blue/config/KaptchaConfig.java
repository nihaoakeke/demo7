package com.blue.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha getDefaultKaptcha(){
        com.google.code.kaptcha.impl.DefaultKaptcha defaultKaptcha = new com.google.code.kaptcha.impl.DefaultKaptcha();

        //就是设置各种的配置属性
        Properties properties = new Properties();
        //无边框
        properties.put("kaptcha.border", "no");
        //黑色
        properties.put("kaptcha.textproducer.font.color", "black");
        //宽度
        properties.put("kaptcha.image.width", "150");
        //高度
        properties.put("kaptcha.image.height", "40");
        //字体大小
        properties.put("kaptcha.textproducer.font.size", "30");
        //设置关键字为key
        properties.put("kaptcha.session.key", "verifyCode");
        //字符间隔距离
        properties.put("kaptcha.textproducer.char.space", "5");
        Config config = new Config(properties);
        //装载配置信息
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
