package com.yzh.cmdb.validator.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.yzh.cmdb.domain.dto.DynamicInstanceDTO;
import com.yzh.cmdb.domain.entity.ResourceModelEntity;
import com.yzh.cmdb.domain.entity.ResourceValidationEntity;
import com.yzh.cmdb.mapper.ResourceModelMapper;
import com.yzh.cmdb.utils.SQLGenerator;
import com.yzh.cmdb.validator.Validator;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * 唯一校验实现
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
public class UniqueValidator implements Validator<DynamicInstanceDTO> {
    private final ResourceModelMapper resourceModelMapper = SpringUtil.getBean(ResourceModelMapper.class);
    private final JdbcTemplate jdbcTemplate = SpringUtil.getBean(JdbcTemplate.class);

    private final ResourceValidationEntity resourceValidationEntity;

    public UniqueValidator(ResourceValidationEntity resourceValidationEntity) {
        this.resourceValidationEntity = resourceValidationEntity;
    }

    @Override
    public boolean validate(DynamicInstanceDTO request) {
        Long modelId = request.getModelId();
        LinkedHashMap<String, Object> data = request.getData();
        // 获取唯一校验
        String validateColumns = resourceValidationEntity.getValidateColumns();
        ResourceModelEntity resourceModelEntity = resourceModelMapper.selectById(modelId);
        List<String> selectColumnNames = Arrays.asList(validateColumns.split(","));
        String sql = SQLGenerator.generateSelectCountSQL(resourceModelEntity.getTableName(), selectColumnNames);
        List<Object> params = new ArrayList<>();
        selectColumnNames.forEach(column -> params.add(data.getOrDefault(column, "")));
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());
        return Objects.nonNull(count) && count < 1;
    }
}
