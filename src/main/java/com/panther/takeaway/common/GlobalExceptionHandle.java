package com.panther.takeaway.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常捕获
 */
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandle {

    /**
     * 唯一字段重复异常
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> ExcptionHandle(SQLIntegrityConstraintViolationException ex){

        if(ex.getMessage().contains("Duplicate entry")){
            String[] strs = ex.getMessage().split(" ");
            String message = strs[2];
            return R.error(message+"已存在");
        }

        return R.error("操作失败");
    }

    @ExceptionHandler(CustomExcept.class)
    public R<String> ExcptionHandle(CustomExcept ex){

        return R.error(ex.getMessage());
    }

}
