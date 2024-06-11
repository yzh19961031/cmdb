package com.yzh.cmdb.controller;

import com.yzh.cmdb.domain.Result;
import com.yzh.cmdb.domain.dto.ResourceModelDTO;
import com.yzh.cmdb.domain.vo.GroupResourceModelVO;
import com.yzh.cmdb.domain.vo.ResourceModelVO;
import com.yzh.cmdb.service.ResourceModelService;
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
 * 资源模型控制器
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
@RestController
@RequestMapping(value = "model")
@AllArgsConstructor
@Tag(name = "资源模型")
public class ResourceModelController {
    private final ResourceModelService resourceModelService;


    /**
     * 添加资源模型
     *
     * @param resourceModelDTO 模型配置参数
     * @return res
     */
    @PostMapping("add")
    @Operation(summary = "添加资源模型")
    public Result<Void> add(@Valid @RequestBody ResourceModelDTO resourceModelDTO) {
        resourceModelService.add(resourceModelDTO);
        return Result.ok();
    }


    /**
     * 删除指定模型
     *
     * @param id 模型id
     * @return res
     */
    @PostMapping("delete")
    @Operation(summary = "删除指定模型")
    @Parameter(name = "id", description = "模型id", required = true, in = ParameterIn.QUERY)
    public Result<Void> delete(@RequestParam("id") Long id) {
        resourceModelService.delete(id);
        return Result.ok();
    }


    /**
     * 批量删除
     *
     * @param ids 模型id列表
     * @return res
     */
    @PostMapping("batchDelete")
    @Operation(summary = "批量删除")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        resourceModelService.batchDelete(ids);
        return Result.ok();
    }

    /**
     * 更新资源模型
     *
     * @param resourceModelDTO 模型配置参数
     * @return res
     */
    @PostMapping("update")
    @Operation(summary = "更新资源模型")
    public Result<Void> update(@Valid @RequestBody ResourceModelDTO resourceModelDTO) {
        resourceModelService.update(resourceModelDTO);
        return Result.ok();
    }

    /**
     * 获取资源模型分组列表
     *
     * @param name 名称
     * @return 资源模型分组列表
     */
    @GetMapping("list")
    @Operation(summary = "获取资源模型分组列表")
    @Parameters({@Parameter(name = "name", description = "名称", in = ParameterIn.QUERY)})
    public Result<List<GroupResourceModelVO>> list(@RequestParam(name = "name", required = false) String name) {
        return Result.ok(resourceModelService.list(name));
    }


    /**
     * 获取所有的模型列表
     *
     * @return 模型列表
     */
    @GetMapping("listAll")
    @Operation(summary = "获取所有的模型列表")
    public Result<List<ResourceModelVO>> listAll() {
        return Result.ok(resourceModelService.listAll());
    }


    /**
     * 获取模型详情
     *
     * @param id 模型id
     * @return 模型详情
     */
    @GetMapping("detail")
    @Operation(summary = "获取模型详情")
    @Parameter(name = "id", description = "模型id", required = true, in = ParameterIn.QUERY)
    public Result<ResourceModelDTO> detail(@RequestParam("id") Long id) {
        return Result.ok(resourceModelService.detail(id));
    }
}
