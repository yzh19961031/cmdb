package com.yzh.cmdb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yzh.cmdb.domain.dto.ResourceGroupDTO;
import com.yzh.cmdb.domain.entity.ResourceGroupEntity;
import com.yzh.cmdb.domain.entity.ResourceModelEntity;
import com.yzh.cmdb.exception.CmdbException;
import com.yzh.cmdb.mapper.ResourceGroupMapper;
import com.yzh.cmdb.mapper.ResourceModelMapper;
import com.yzh.cmdb.service.ResourceGroupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 资源分组service
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ResourceGroupServiceImpl implements ResourceGroupService {
    @Resource
    private ResourceGroupMapper resourceGroupMapper;

    @Resource
    private ResourceModelMapper resourceModelMapper;


    @Override
    public void add(ResourceGroupDTO resourceGroupDTO) {
        this.checkResourceGroup(resourceGroupDTO, true);
        ResourceGroupEntity resourceGroupEntity = new ResourceGroupEntity();
        BeanUtils.copyProperties(resourceGroupDTO, resourceGroupEntity);
        resourceGroupMapper.insert(resourceGroupEntity);
    }

    @Override
    public void delete(Long id) {
        boolean exist = resourceModelMapper.exists(new LambdaQueryWrapper<ResourceModelEntity>().eq(ResourceModelEntity::getGroupId, id));
        if (exist) {
            throw new CmdbException("该分组内有数据，不可删除！！！");
        }
        resourceGroupMapper.deleteById(id);
    }

    @Override
    public void update(ResourceGroupDTO resourceGroupDTO) {
        this.checkResourceGroup(resourceGroupDTO, false);
        ResourceGroupEntity resourceGroupEntity = new ResourceGroupEntity();
        BeanUtils.copyProperties(resourceGroupDTO, resourceGroupEntity);
        resourceGroupMapper.updateById(resourceGroupEntity);
    }

    @Override
    public List<ResourceGroupDTO> list() {
        return resourceGroupMapper.selectList(null).stream().map(resourceGroupEntity -> {
            ResourceGroupDTO resourceGroupDTO = new ResourceGroupDTO();
            BeanUtils.copyProperties(resourceGroupEntity, resourceGroupDTO);
            return resourceGroupDTO;
        }).collect(Collectors.toList());
    }

    // 基础校验
    private void checkResourceGroup(ResourceGroupDTO resourceGroupDTO, boolean isNew) {
        ResourceGroupDTO groupDTO = Objects.requireNonNull(resourceGroupDTO, "分组信息不能为空");
        String name = groupDTO.getName();
        if (isNew) {
            boolean exists = resourceGroupMapper.exists(new LambdaQueryWrapper<ResourceGroupEntity>().eq(ResourceGroupEntity::getName, name));
            if (exists) {
                throw new IllegalArgumentException("分组名称【" + name + "】已经存在");
            }
        } else {
            ResourceGroupEntity resourceGroupEntity = resourceGroupMapper.selectById(resourceGroupDTO.getId());
            String originalName = resourceGroupEntity.getName();
            if (!StringUtils.equals(originalName, name)) {
                boolean exists = resourceGroupMapper.exists(new LambdaQueryWrapper<ResourceGroupEntity>().eq(ResourceGroupEntity::getName, name));
                if (exists) {
                    throw new IllegalArgumentException("分组名称【" + name + "】已经存在");
                }
            }
        }
    }
}
