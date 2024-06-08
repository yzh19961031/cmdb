package com.yzh.cmdb.exception;

import com.yzh.cmdb.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@ControllerAdvice
@Order(-1)
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CmdbException.class)
    @ResponseBody
    public Result<Void> handlerCmdbException(CmdbException e) {
        log.error("Cmdb error", e);
        return Result.error(e.getMessage());
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("参数校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage()).append(",");
        }
        String msg = sb.toString();
        msg = msg.substring(0, msg.length() - 1);
        return Result.error(msg);
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Void> handlerException(Exception e) {
        log.error("Unknown error", e);
        return Result.error(e.getMessage());
    }
}
