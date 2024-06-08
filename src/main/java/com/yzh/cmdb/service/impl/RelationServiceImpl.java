package com.yzh.cmdb.service.impl;

import com.yzh.cmdb.domain.vo.RelationTypeVO;
import com.yzh.cmdb.mapper.RelationTypeMapper;
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
}
