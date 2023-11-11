//package com.blue.controller;
//
//import com.blue.domain.Code;
//import com.blue.domain.Result;
//import com.blue.exception.BusinessException;
//import com.blue.exception.SystemException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class ProjectExceptionAdvice {
//    @ExceptionHandler(SystemException.class)
//    public Result doSystemException(SystemException ex){
//        return new Result(ex.getCode(),null,ex.getMessage());
//    }
//
//    @ExceptionHandler(BusinessException.class)
//    public Result doBusinessException(BusinessException ex){
//        return new Result(ex.getCode(),null,ex.getMessage());
//    }
//
//    @ExceptionHandler(Exception.class)
//    public Result doOtherException(Exception ex){
//        return new Result(Code.SYSTEM_UNKNOW_ERR,null,"系统繁忙，请稍后再试！");
//    }
//}
