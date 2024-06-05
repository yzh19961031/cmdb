package com.yzh.cmdb.controller;

import com.yzh.cmdb.domain.Result;
import com.yzh.cmdb.domain.entity.RelationTypeEntity;
import com.yzh.cmdb.service.RelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 关系类型控制器
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
@RestController
@RequestMapping(value = "relation")
@AllArgsConstructor
@Tag(name = "关系类型")
public class RelationController {
    private final RelationService relationService;


    /**
     * 获取所有的关系类型
     *
     * @return 关系类型列表
     */
    @GetMapping("list")
    @Operation(summary = "获取所有的关系类型列表")
    public Result<List<RelationTypeEntity>> list() {
        return Result.ok(relationService.list());
    }
}
