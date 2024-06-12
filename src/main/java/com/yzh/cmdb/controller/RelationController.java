package com.yzh.cmdb.controller;

import com.yzh.cmdb.domain.Result;
import com.yzh.cmdb.domain.vo.RelationTypeVO;
import com.yzh.cmdb.service.RelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
    public Result<List<RelationTypeVO>> list() {
        return Result.ok(relationService.list());
    }

    /**
     * 新增关系类型
     *
     * @param relationTypeVO 关系类型详情
     * @return res
     */
    @PostMapping("add")
    @Operation(summary = "新增关系类型")
    public Result<Void> add(@Valid @RequestBody RelationTypeVO relationTypeVO) {
        relationService.add(relationTypeVO);
        return Result.ok();
    }

    /**
     * 删除关系类型
     *
     * @param id 关系类型id
     * @return res
     */
    @PostMapping("delete")
    @Operation(summary = "删除关系类型")
    @Parameter(name = "id", description = "关系类型id", required = true, in = ParameterIn.QUERY)
    public Result<Void> delete(@RequestParam("id") Long id) {
        relationService.delete(id);
        return Result.ok();
    }
}
