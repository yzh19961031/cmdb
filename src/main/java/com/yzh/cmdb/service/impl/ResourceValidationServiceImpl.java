package com.yzh.cmdb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yzh.cmdb.domain.dto.ResourceValidationDTO;
import com.yzh.cmdb.domain.entity.ResourceAttributeEntity;
import com.yzh.cmdb.domain.entity.ResourceValidationEntity;
import com.yzh.cmdb.domain.vo.ResourceValidationVO;
import com.yzh.cmdb.exception.CmdbException;
import com.yzh.cmdb.mapper.ResourceAttributeMapper;
import com.yzh.cmdb.mapper.ResourceValidationMapper;
import com.yzh.cmdb.service.ResourceValidationService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 模型唯一校验service
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Service
public class ResourceValidationServiceImpl implements ResourceValidationService {


    @Resource
    private ResourceValidationMapper resourceValidationMapper;

    @Resource
    private ResourceAttributeMapper resourceAttributeMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(ResourceValidationDTO resourceValidationDTO) {
        this.checkResourceValidation(resourceValidationDTO);
        ResourceValidationEntity resourceValidationEntity = new ResourceValidationEntity();
        String validateColumns = Optional.ofNullable(resourceValidationDTO.getValidateColumns())
                .orElseThrow(() -> new IllegalArgumentException("唯一校验参数非法！！！"));
        Long modelId = resourceValidationDTO.getModelId();
        resourceValidationEntity.setValidateColumns(validateColumns);
        resourceValidationEntity.setModelId(modelId);
        resourceValidationMapper.insert(resourceValidationEntity);
    }


    @Override
    public void delete(Long id) {
        // 实例名称默认不给删除
        ResourceValidationEntity resourceValidationEntity = resourceValidationMapper.selectById(id);
        String validateColumns = resourceValidationEntity.getValidateColumns();
        if (StringUtils.equals(validateColumns, "instance_name")) {
            throw new CmdbException("默认实例名称唯一校验无法删除！！！");
        }
        resourceValidationMapper.deleteById(id);
    }

    @Override
    public List<ResourceValidationVO> list(Long modelId) {
        List<ResourceValidationEntity> resourceValidationEntities = resourceValidationMapper.selectList(new LambdaQueryWrapper<ResourceValidationEntity>().eq(ResourceValidationEntity::getModelId, modelId));
        return resourceValidationEntities
                .stream()
                .map(resourceValidationEntity -> {
                    List<ResourceAttributeEntity> resourceAttributeList = getResourceAttributeList(resourceValidationEntity.getValidateColumns().split(","), modelId);
                    String displayName = resourceAttributeList.stream().map(ResourceAttributeEntity::getIdentifier).collect(Collectors.joining("+"));
                    List<Long> attributeList = resourceAttributeList.stream().map(ResourceAttributeEntity::getId).collect(Collectors.toList());
                    return ResourceValidationVO
                            .builder()
                            .id(resourceValidationEntity.getId())
                            .displayName(displayName)
                            .attributeList(attributeList)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<ResourceAttributeEntity> getResourceAttributeList(String[] strings, Long modelId) {
        return Arrays.stream(strings)
                .map(key -> resourceAttributeMapper.selectOne(new LambdaQueryWrapper<ResourceAttributeEntity>()
                        .eq(ResourceAttributeEntity::getIdentifier, key)
                        .eq(ResourceAttributeEntity::getModelId, modelId)))
                .collect(Collectors.toList());
    }


    private void checkResourceValidation(ResourceValidationDTO resourceValidationDTO) {
        Objects.requireNonNull(resourceValidationDTO, "参数不能为空！！！");
        String validateColumns = Objects.requireNonNull(resourceValidationDTO.getValidateColumns(), "唯一校验项不能为空！！！");
        Long modelId = resourceValidationDTO.getModelId();
        List<ResourceValidationEntity> resourceValidationEntities = resourceValidationMapper.selectList(new LambdaQueryWrapper<ResourceValidationEntity>()
                .eq(ResourceValidationEntity::getModelId, modelId));
        if (CollectionUtils.isEmpty(resourceValidationEntities)) {
            return;
        }

        Set<String> inputColumnsSet = new HashSet<>(Arrays.asList(validateColumns.split(",")));
        for (ResourceValidationEntity entity : resourceValidationEntities) {
            Set<String> entityColumnsSet = new HashSet<>(Arrays.asList(entity.getValidateColumns().split(",")));
            if (entityColumnsSet.equals(inputColumnsSet)) {
                throw new CmdbException("唯一校验已存在！！！");
            }
        }
    }
}
