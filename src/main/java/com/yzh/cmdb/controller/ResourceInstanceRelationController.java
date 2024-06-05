package com.yzh.cmdb.controller;

import com.yzh.cmdb.domain.Result;
import com.yzh.cmdb.domain.dto.ResourceInstanceRelationDTO;
import com.yzh.cmdb.domain.vo.ListResourceInstanceRelationVO;
import com.yzh.cmdb.domain.vo.ResourceInstanceTopologyVO;
import com.yzh.cmdb.service.ResourceInstanceRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
 * 实例关联控制器
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@RestController
@RequestMapping(value = "instance/relation")
@AllArgsConstructor
@Tag(name = "实例关联")
public class ResourceInstanceRelationController {

    private final ResourceInstanceRelationService resourceInstanceRelationService;


    /**
     * 添加实例关联关系
     *
     * @param resourceInstanceRelationDTO 实例关联详情
     */
    @PostMapping("add")
    @Operation(summary = "添加实例关联关系")
    public Result<Void> add(@Valid @RequestBody ResourceInstanceRelationDTO resourceInstanceRelationDTO) {
        resourceInstanceRelationService.add(resourceInstanceRelationDTO);
        return Result.ok();
    }


    /**
     * 批量添加实例关联关系
     *
     * @param resourceInstanceRelationDTOList 实例关联详情列表
     */
    @PostMapping("batchAdd")
    @Operation(summary = "批量添加实例关联关系")
    public Result<Void> batchAdd(@Valid @RequestBody List<ResourceInstanceRelationDTO> resourceInstanceRelationDTOList) {
        resourceInstanceRelationService.batchAdd(resourceInstanceRelationDTOList);
        return Result.ok();
    }


    /**
     * 删除实例关联关系
     *
     * @param id 关联关系id
     */
    @PostMapping("delete")
    @Operation(summary = "删除实例关联关系")
    @Parameter(name = "id", description = "实例关联id", required = true, in = ParameterIn.QUERY)
    public Result<Void> delete(@RequestParam("id") Long id) {
        resourceInstanceRelationService.delete(id);
        return Result.ok();
    }


    /**
     * 生成指定实例的拓扑图
     *
     * @param modelId 模型id
     * @param instanceId 实例id
     * @return 拓扑详情
     */
    @GetMapping("recursiveTopology")
    @Operation(summary = "生成指定实例的拓扑图")
    @Parameters({
            @Parameter(name = "modelId", description = "模型id", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "instanceId", description = "实例id", required = true, in = ParameterIn.QUERY)
    })
    public Result<ResourceInstanceTopologyVO> recursiveTopology(@RequestParam("modelId") Long modelId, @RequestParam("instanceId") String instanceId) {
        return Result.ok(resourceInstanceRelationService.recursiveTopology(modelId, instanceId));
    }


    /**
     * 获取指定实例列表
     *
     * @param modelId 模型id
     * @param instanceId 实例id
     * @return 实例列表
     */
    @GetMapping("list")
    @Operation(summary = "获取指定实例列表")
    @Parameters({
            @Parameter(name = "modelId", description = "模型id", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "instanceId", description = "实例id", required = true, in = ParameterIn.QUERY)
    })
    public Result<ListResourceInstanceRelationVO> list(@RequestParam("modelId") Long modelId, @RequestParam("instanceId") String instanceId) {
        return Result.ok(resourceInstanceRelationService.list(modelId, instanceId));
    }

}
