package com.yzh.cmdb.controller;

import com.yzh.cmdb.domain.Result;
import com.yzh.cmdb.domain.dto.ResourceRelationDTO;
import com.yzh.cmdb.domain.vo.GroupResourceRelationVO;
import com.yzh.cmdb.domain.vo.ResourceModelTopologyVO;
import com.yzh.cmdb.domain.vo.ResourceRelationVO;
import com.yzh.cmdb.service.ResourceRelationService;
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
 * 模型关联控制器
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@RestController
@RequestMapping(value = "model/relation")
@AllArgsConstructor
@Tag(name = "模型关联")
public class ResourceRelationController {

    private final ResourceRelationService resourceRelationService;


    /**
     * 添加模型关联关系
     *
     * @param resourceRelationDTO 模型关联关系
     * @return res
     */
    @PostMapping("add")
    @Operation(summary = "添加模型关联关系")
    public Result<Void> add(@Valid @RequestBody ResourceRelationDTO resourceRelationDTO) {
        resourceRelationService.add(resourceRelationDTO);
        return Result.ok();
    }


    /**
     * 删除模型关联关系
     *
     * @param id 关联关系id
     * @return res
     */
    @PostMapping("delete")
    @Operation(summary = "删除模型关联关系")
    @Parameter(name = "id", description = "模型关联id", required = true, in = ParameterIn.QUERY)
    public Result<Void> delete(@RequestParam("id") Long id) {
        resourceRelationService.delete(id);
        return Result.ok();
    }


    /**
     * 获取指定模型的关联列表
     *
     * @param modelId 模型id
     * @return 关联列表
     */
    @GetMapping("list")
    @Operation(summary = "获取指定模型的关联列表")
    @Parameter(name = "modelId", description = "模型id", required = true, in = ParameterIn.QUERY)
    public Result<GroupResourceRelationVO> list(@RequestParam("modelId") Long modelId) {
        return Result.ok(resourceRelationService.list(modelId));
    }


    /**
     * 生成模型拓扑图
     *
     * @return 拓扑详情
     */
    @GetMapping("recursiveTopology")
    @Operation(summary = "生成模型拓扑图")
    public Result<ResourceModelTopologyVO> recursiveTopology() {
        return Result.ok(resourceRelationService.recursiveTopology());
    }


    /**
     * 获取所有模型关联列表
     *
     * @return 模型关联列表
     */
    @GetMapping("listAll")
    @Operation(summary = "获取所有模型关联列表")
    public Result<List<ResourceRelationVO>> listAll() {
        return Result.ok(resourceRelationService.listAll());
    }
}
