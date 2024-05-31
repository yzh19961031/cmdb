package com.yzh.cmdb.validator;

/**
 * 校验
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
public interface Validator<T> {

    /**
     * 校验
     *
     * @param request 输入
     * @return T OR F
     */
    boolean validate(T request);
}
