package com.yzh.cmdb.exception;

import lombok.Getter;

/**
 * 通用异常
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Getter
public class CmdbException extends RuntimeException {
    private final String message;

    public CmdbException(String message) {
        super(message);
        this.message = message;
    }
}
