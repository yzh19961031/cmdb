package com.yzh.cmdb.service;

import com.yzh.cmdb.domain.entity.RelationTypeEntity;

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
    List<RelationTypeEntity> list();
}
