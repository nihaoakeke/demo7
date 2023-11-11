package com.blue.config;

import com.blue.interceptor.IdentityInterceptor;
import com.blue.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.servlet.config.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class WebConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Autowired
    private IdentityInterceptor identityInterceptor;

    /**
     * 解决跨域请求
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
//                .allowedOrigins("*")
                .allowedOriginPatterns("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

//        registry.addResourceHandler("/images/**").addResourceLocations("file:///D:/code/demo7/src/main/resources/images/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///var/www/html/images/");
//        registry.addResourceHandler("/images/**").addResourceLocations("file:///"+"D:/code/demo7/src/main/resources/images/");
    }


//    /**
//     * 异步请求配置
//     * @param configurer
//     */
//    @Override
//    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
//        configurer.setTaskExecutor(new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3)));
//        configurer.setDefaultTimeout(30000);
//    }

//    /**
//     * 配置拦截器、拦截路径
//     * 每次请求到拦截的路径，就会去执行拦截器中的方法
//     * @param configurer
//     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePath = new ArrayList<>();
        //排除拦截，除了注册登录(此时还没token)，其他都拦截
        excludePath.add("/users/login");
        excludePath.add("/users/*");
        excludePath.add("/users/manager");
        excludePath.add("/users/selectLikenickname");
        excludePath.add("/users/selectLikename");
        excludePath.add("/mails/**");
        //登录
        excludePath.add("/users/register");
        //注册
//        excludePath.add("/doc.html");     //swagger
//        excludePath.add("/swagger-ui.html");     //swagger
//        excludePath.add("/swagger-resources/**");     //swagger
//        excludePath.add("/v2/api-docs");     //swagger
//        excludePath.add("/webjars/**");     //swagger
        excludePath.add("/static/**");
        //静态资源
        excludePath.add("/assets/**");
        //静态资源
        excludePath.add("/images/**");

        List<String> patterns1 = new ArrayList<>();
        patterns1.add("/users/**");
        patterns1.add("/chats/**");
        patterns1.add("/blogs/register");
        patterns1.add("/comments/register");
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns(patterns1)
                .excludePathPatterns(excludePath);

        List<String> patterns2 = new ArrayList<>();

        patterns2.add("/blogs/manager/**");
        patterns2.add("/news/manager/**");
        patterns2.add("/comments/manager/**");
        patterns2.add("/users/manager/**");

        registry.addInterceptor(identityInterceptor).addPathPatterns(patterns2).excludePathPatterns(excludePath);






//        registry.addInterceptor(tokenInterceptor).addPathPatterns("/blogs/**");
//        registry.addInterceptor(passwordInterceptor).addPathPatterns("/users/register");
//        WebMvcConfigurer.super.addInterceptors(registry);
//          addInterceptors(registry);
    }
}