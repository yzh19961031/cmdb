package com.yzh.cmdb.exception;

import com.yzh.cmdb.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handlerCmdbException(CmdbException e) {
        log.error("Cmdb error", e);
        return Result.error(e.getMessage());
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("参数校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage()).append(",");
        }
        String msg = sb.toString();
        msg = msg.substring(0, msg.length() - 1);
        return Result.ok(msg);
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handlerException(Exception e) {
        log.error("Unknown error", e);
        return Result.error(e.getMessage());
    }
}
