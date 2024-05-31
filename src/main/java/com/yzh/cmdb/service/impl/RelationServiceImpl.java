package com.yzh.cmdb.service.impl;

import com.yzh.cmdb.domain.entity.RelationTypeEntity;
import com.yzh.cmdb.mapper.RelationTypeMapper;
import com.yzh.cmdb.service.RelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 关系类型service
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
@Service
public class RelationServiceImpl implements RelationService {

    @Resource
    private RelationTypeMapper relationTypeMapper;


    @Override
    public List<RelationTypeEntity> list() {
        return relationTypeMapper.selectList(null);
    }
}
