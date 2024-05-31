package com.yzh.cmdb.domain;


import lombok.Data;

import java.util.List;

/**
 * 分页结果
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class PageResult<T> {
    private List<T> list;
    private Long total;
}
