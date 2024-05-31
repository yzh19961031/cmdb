package com.yzh.cmdb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yzh.cmdb.domain.entity.RelationTypeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 关系类型
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
@Mapper
public interface RelationTypeMapper extends BaseMapper<RelationTypeEntity> {
}
