package com.yzh.cmdb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yzh.cmdb.domain.entity.ResourceValidationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 模型唯一校验
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Mapper
public interface ResourceValidationMapper extends BaseMapper<ResourceValidationEntity> {
}
