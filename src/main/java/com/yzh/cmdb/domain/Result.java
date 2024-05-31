package com.yzh.cmdb.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -4728716490482583561L;

    private static final Integer SUCCESS_CODE = 0;
    private static final Integer FAILED_CODE = 1;

    private Integer code;
    private T data;
    private String msg;


    public static Result<Void> ok() {
        Result<Void> result = new Result<>();
        result.setCode(SUCCESS_CODE);
        return result;
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setCode(SUCCESS_CODE);
        result.setData(data);
        return result;
    }

    public static Result<Void> error(String msg) {
        Result<Void> result = new Result<>();
        result.setCode(FAILED_CODE);
        result.setMsg(msg);
        return result;
    }
}

