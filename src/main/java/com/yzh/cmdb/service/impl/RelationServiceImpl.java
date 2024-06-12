package com.yzh.cmdb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yzh.cmdb.domain.entity.RelationTypeEntity;
import com.yzh.cmdb.domain.entity.ResourceRelationEntity;
import com.yzh.cmdb.domain.vo.RelationTypeVO;
import com.yzh.cmdb.exception.CmdbException;
import com.yzh.cmdb.mapper.RelationTypeMapper;
import com.yzh.cmdb.mapper.ResourceRelationMapper;
import com.yzh.cmdb.service.RelationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private ResourceRelationMapper resourceRelationMapper;


    @Override
    public List<RelationTypeVO> list() {
        return relationTypeMapper.selectList(null)
                .stream()
                .map(relationTypeEntity -> {
                    RelationTypeVO relationTypeVO = new RelationTypeVO();
                    BeanUtils.copyProperties(relationTypeEntity, relationTypeVO);
                    return relationTypeVO;
                }).collect(Collectors.toList());
    }

    @Override
    public void add(RelationTypeVO relationTypeVO) {
        String name = relationTypeVO.getName();
        boolean exists = relationTypeMapper.exists(new LambdaQueryWrapper<RelationTypeEntity>().eq(RelationTypeEntity::getName, name));
        if (exists) {
            throw new CmdbException("关系类型【" + name + " 】已存在！");
        }
        RelationTypeEntity relationTypeEntity = new RelationTypeEntity();
        BeanUtils.copyProperties(relationTypeVO, relationTypeEntity);
        relationTypeMapper.insert(relationTypeEntity);
    }

    @Override
    public void delete(Long id) {
        boolean exists = resourceRelationMapper.exists(new LambdaQueryWrapper<ResourceRelationEntity>().eq(ResourceRelationEntity::getRelationTypeId, id));
        if (exists) {
            throw new CmdbException("关系类型正在被使用！！！");
        }
        relationTypeMapper.deleteById(id);
    }
}
