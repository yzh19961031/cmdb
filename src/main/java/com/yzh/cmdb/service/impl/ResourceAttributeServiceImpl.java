package com.yzh.cmdb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yzh.cmdb.domain.dto.ColumnDefinitionDTO;
import com.yzh.cmdb.domain.dto.ResourceAttributeDTO;
import com.yzh.cmdb.domain.dto.ResourceAttributeQueryDTO;
import com.yzh.cmdb.domain.entity.ResourceAttributeEntity;
import com.yzh.cmdb.domain.entity.ResourceModelEntity;
import com.yzh.cmdb.domain.entity.ResourceValidationEntity;
import com.yzh.cmdb.domain.vo.ResourceAttributeVO;
import com.yzh.cmdb.enums.ModelUpdateTypeEnum;
import com.yzh.cmdb.enums.ResourceAttributeEnum;
import com.yzh.cmdb.event.UpdateModelEvent;
import com.yzh.cmdb.exception.CmdbException;
import com.yzh.cmdb.mapper.ResourceAttributeMapper;
import com.yzh.cmdb.mapper.ResourceModelMapper;
import com.yzh.cmdb.mapper.ResourceValidationMapper;
import com.yzh.cmdb.service.ResourceAttributeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 模型属性service
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Service
public class ResourceAttributeServiceImpl implements ResourceAttributeService {

    @Resource
    private ResourceModelMapper resourceModelMapper;

    @Resource
    private ResourceAttributeMapper resourceAttributeMapper;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private ResourceValidationMapper resourceValidationMapper;

    @Override
    public void addAttribute(ResourceAttributeDTO resourceAttributeDTO) {
        this.checkResourceAttribute(resourceAttributeDTO);
        Long modelId = resourceAttributeDTO.getModelId();
        // 添加属性
        ResourceAttributeEntity resourceAttributeEntity = new ResourceAttributeEntity();
        BeanUtils.copyProperties(resourceAttributeDTO, resourceAttributeEntity);
        resourceAttributeEntity.setModelId(modelId);
        resourceAttributeMapper.insert(resourceAttributeEntity);
        ResourceModelEntity resourceModelEntity = resourceModelMapper.selectById(modelId);
        List<ColumnDefinitionDTO> columnDefinitionDTOList = Collections.singletonList(generateColumnDefinitionDTO(resourceAttributeDTO));
        // 发布模型变更事件
        applicationEventPublisher.publishEvent(new UpdateModelEvent(this, resourceModelEntity.getTableName(), ModelUpdateTypeEnum.ADD_COLUMN, columnDefinitionDTOList));
    }


    @Override
    public void deleteAttribute(Long modelId, Long attrId) {
        ResourceAttributeEntity resourceAttributeEntity = resourceAttributeMapper.selectById(attrId);
        // 判断属性是否在唯一校验规则里面
        List<ResourceValidationEntity> resourceValidationEntities = resourceValidationMapper.selectList(new LambdaQueryWrapper<ResourceValidationEntity>().eq(ResourceValidationEntity::getModelId, modelId));
        if (CollectionUtils.isNotEmpty(resourceValidationEntities)) {
            resourceValidationEntities.forEach(resourceValidationEntity -> {
                String validateColumns = resourceValidationEntity.getValidateColumns();
                HashSet<String> set = new HashSet<>(Arrays.asList(validateColumns.split(",")));
                if (set.contains(resourceAttributeEntity.getIdentifier())) {
                    throw new CmdbException("该属性是唯一校验属性，无法删除！！！");
                }
            });
        }
        ResourceModelEntity resourceModelEntity = resourceModelMapper.selectById(modelId);
        resourceAttributeMapper.deleteById(attrId);
        UpdateModelEvent updateModelEvent = new UpdateModelEvent(this, resourceModelEntity.getTableName(), ModelUpdateTypeEnum.DELETE_COLUMN,
                Collections.singletonList(new ColumnDefinitionDTO().setColumnName(resourceAttributeEntity.getIdentifier())));
        applicationEventPublisher.publishEvent(updateModelEvent);
    }

    @Override
    public void update(ResourceAttributeDTO resourceAttributeDTO) {
        // 编辑模型值编辑名称 校验规则等 不影响实例的表结构
        ResourceAttributeEntity resourceAttributeEntity = new ResourceAttributeEntity();
        BeanUtils.copyProperties(resourceAttributeDTO, resourceAttributeEntity);
        resourceAttributeMapper.updateById(resourceAttributeEntity);
    }

    @Override
    public List<ResourceAttributeVO> list(ResourceAttributeQueryDTO resourceAttributeQueryDTO) {
        List<ResourceAttributeEntity> resourceAttributeEntityList = resourceAttributeMapper.selectList(new LambdaQueryWrapper<ResourceAttributeEntity>()
                .eq(ResourceAttributeEntity::getModelId, resourceAttributeQueryDTO.getModelId())
                .like(StringUtils.isNotEmpty(resourceAttributeQueryDTO.getName()), ResourceAttributeEntity::getName, resourceAttributeQueryDTO.getName())
                .like(StringUtils.isNotEmpty(resourceAttributeQueryDTO.getIdentifier()), ResourceAttributeEntity::getIdentifier, resourceAttributeQueryDTO.getIdentifier()));
        return resourceAttributeEntityList.stream().map(resourceAttributeEntity -> {
            ResourceAttributeVO resourceAttributeVO = new ResourceAttributeVO();
            BeanUtils.copyProperties(resourceAttributeEntity, resourceAttributeVO);
            return resourceAttributeVO;
        }).collect(Collectors.toList());
    }

    @Override
    public ResourceAttributeVO detail(Long attrId) {
        ResourceAttributeEntity resourceAttributeEntity = resourceAttributeMapper.selectById(attrId);
        ResourceAttributeVO resourceAttributeVO = new ResourceAttributeVO();
        BeanUtils.copyProperties(resourceAttributeEntity, resourceAttributeVO);
        return resourceAttributeVO;
    }



    private void checkResourceAttribute(ResourceAttributeDTO resourceAttributeDTO) {
        ResourceAttributeDTO attributeDTO = Objects.requireNonNull(resourceAttributeDTO, "属性参数不能为空");
        String name = attributeDTO.getName();

        Long modelId = attributeDTO.getModelId();
        boolean existsName = resourceAttributeMapper.exists(new LambdaQueryWrapper<ResourceAttributeEntity>()
                .eq(ResourceAttributeEntity::getModelId, modelId)
                .eq(ResourceAttributeEntity::getName, name));
        if (existsName) {
            throw new IllegalArgumentException("属性名称重复");
        }

        String identifier = attributeDTO.getIdentifier();
        boolean existsIdentifier = resourceAttributeMapper.exists(new LambdaQueryWrapper<ResourceAttributeEntity>()
                .eq(ResourceAttributeEntity::getModelId, modelId)
                .eq(ResourceAttributeEntity::getIdentifier, identifier));
        if (existsIdentifier) {
            throw new IllegalArgumentException("属性标识重复");
        }
    }


    /**
     * 根据模型属性生成对应的数据库的列
     *
     * @param resourceAttributeDTO 模型属性
     * @return 数据库列定义
     */
    private ColumnDefinitionDTO generateColumnDefinitionDTO(ResourceAttributeDTO resourceAttributeDTO) {
        Integer type = resourceAttributeDTO.getType();
        ResourceAttributeEnum resourceAttributeEnum = ResourceAttributeEnum.getByValue(type);
        String identifier = resourceAttributeDTO.getIdentifier();
        ColumnDefinitionDTO columnDefinitionDTO = new ColumnDefinitionDTO().setColumnName(identifier);
        switch (resourceAttributeEnum) {
            case INPUT:
            case PASSWORD:
            case ENUMS:
                columnDefinitionDTO.setColumnType("VARCHAR(512)");
                break;
            case DATE:
                columnDefinitionDTO.setColumnType("TIMESTAMP");
                break;
            case INTEGER:
                columnDefinitionDTO.setColumnType("BIGINT");
                break;
            case FLOAT:
                columnDefinitionDTO.setColumnType("DECIMAL(16, 2)");
                break;
            default:
                throw new IllegalArgumentException("非法的属性参数类型！！！");
        }
        return columnDefinitionDTO;
    }
}
