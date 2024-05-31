package com.yzh.cmdb.validator;

import java.util.HashSet;
import java.util.Set;


/**
 * 校验规则调用链
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
public class ValidatorChain<T> {

    private Set<Validator<T>> validators = new HashSet<>();

    public ValidatorChain(Set<Validator<T>> validators) {
        this.validators = validators;
    }

    public ValidatorChain() {
    }


    /**
     * 校验方法
     *
     * @param request 输入参数
     * @return T OR F
     */
    public boolean doValidate(T request) {
        for (Validator<T> validator : validators) {
            if (!validator.validate(request)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 添加校验器
     *
     * @param validator 校验器
     */
    public void setNextValidator(Validator<T> validator) {
        validators.add(validator);
    }
}
