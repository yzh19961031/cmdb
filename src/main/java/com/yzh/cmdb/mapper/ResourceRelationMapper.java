package com.yzh.cmdb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yzh.cmdb.domain.entity.ResourceRelationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 模型关联mapper
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Mapper
public interface ResourceRelationMapper extends BaseMapper<ResourceRelationEntity> {
}
