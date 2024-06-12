package com.yzh.cmdb.service;


import com.yzh.cmdb.domain.vo.RelationTypeVO;

import java.util.List;

/**
 * 关系类型
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
public interface RelationService {

    /**
     * 获取关系类型列表
     *
     * @return 列表
     */
    List<RelationTypeVO> list();


    /**
     * 新增关系类型
     *
     * @param relationTypeVO 关系类型详情
     */
    void add(RelationTypeVO relationTypeVO);

    /**
     * 删除关系类型
     *
     * @param id 关系类型id
     */
    void delete(Long id);
}
