package com.yzh.cmdb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yzh.cmdb.domain.entity.ResourceInstanceRelationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 实例关联mapper
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Mapper
public interface ResourceInstanceRelationMapper extends BaseMapper<ResourceInstanceRelationEntity> {
}
