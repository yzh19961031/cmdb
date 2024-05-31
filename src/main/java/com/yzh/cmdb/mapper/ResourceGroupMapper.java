package com.yzh.cmdb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yzh.cmdb.domain.entity.ResourceGroupEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资源分组mapper
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
@Mapper
public interface ResourceGroupMapper extends BaseMapper<ResourceGroupEntity> {
}
