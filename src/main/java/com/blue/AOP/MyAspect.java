package com.blue.AOP;

import com.blue.service.BloagService;
import com.blue.utils.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {
    private final BloagService bloagService;
    private final RedisUtil redisUtil;
    public MyAspect(BloagService bloagService,RedisUtil redisUtil){
        this.bloagService =bloagService;
        this.redisUtil =redisUtil;
    }

    @Pointcut("execution( * com.blue.controller.BlogController.getViewById(..))")
    public void myPointCut1(){}

    @Pointcut("execution( * com.blue.controller.BlogController.OKLikeById(..))")
    public void myPointCut2(){}

    @Pointcut("execution( * com.blue.controller.BlogController.falseLikeById(..))")
    public void myPointCut3(){}

    @Pointcut("execution( * com.blue.controller.CommentController.OKLikeById(..))")
    public void myPointCut4(){}

    @Pointcut("execution( * com.blue.controller.CommentController.falseLikeById(..))")
    public void myPointCut5(){}
    @Pointcut("execution( * com.blue.controller.CommentController.getById(..))")
    public void myPointCut6(){}


    //在这个方法执行后
    @After("myPointCut1()")
    public void doAfter1(JoinPoint joinPoint) throws Throwable {
        Object[] objs=joinPoint.getArgs();
        Integer id=(int) objs[0];
        System.out.println(id);
        System.out.println(id);
        //根据id更新浏览量
//        redisUtil.zIncrementScore("viewNum",id.toString(),1);
        redisUtil.zIncrementScore("blogView",id.toString(),1);
    }

    @After("myPointCut2()")
    public void doAfter2(JoinPoint joinPoint) throws Throwable {
        Object[] objs=joinPoint.getArgs();
        Integer id=(int) objs[0];
        System.out.println("_________________________");
        //根据id更新浏览量
        redisUtil.zIncrementScore("blogLike",id.toString(),1);
    }

    @After("myPointCut3()")
    public void doAfter3(JoinPoint joinPoint) throws Throwable {
        Object[] objs=joinPoint.getArgs();
        Integer id=(int) objs[0];
        System.out.println("_________________________");
        //根据id更新浏览量
        redisUtil.zIncrementScore("blogLike",id.toString(),-1);
    }

    @After("myPointCut4()")
    public void doAfter4(JoinPoint joinPoint) throws Throwable {
        Object[] objs=joinPoint.getArgs();
        Integer id=(int) objs[0];
        System.out.println("_________________________");
        //根据id更新浏览量
//        redisUtil.zIncrementScore("viewNum",id.toString(),1);
        redisUtil.zIncrementScore("cLike",id.toString(),1);
    }

    @After("myPointCut5()")
    public void doAfter5(JoinPoint joinPoint) throws Throwable {
        Object[] objs=joinPoint.getArgs();
        Integer id=(int) objs[0];
        System.out.println("_________________________");
        //根据id更新浏览量
//        redisUtil.zIncrementScore("viewNum",id.toString(),1);
        redisUtil.zIncrementScore("cLike",id.toString(),-1);
    }


}

