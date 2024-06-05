package com.yzh.cmdb.controller;

import com.yzh.cmdb.domain.Result;
import com.yzh.cmdb.domain.dto.ResourceAttributeDTO;
import com.yzh.cmdb.domain.dto.ResourceAttributeQueryDTO;
import com.yzh.cmdb.domain.vo.ResourceAttributeVO;
import com.yzh.cmdb.service.ResourceAttributeService;
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
 * 模型属性控制器
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@RestController
@RequestMapping(value = "model/attribute")
@AllArgsConstructor
@Tag(name = "模型属性")
public class ResourceAttributeController {
    private final ResourceAttributeService resourceAttributeService;


    /**
     * 添加模型属性
     *
     * @param resourceAttributeDTO 模型属性
     * @return res
     */
    @PostMapping("add")
    @Operation(summary = "添加模型属性")
    public Result<Void> add(@Valid @RequestBody ResourceAttributeDTO resourceAttributeDTO) {
        resourceAttributeService.addAttribute(resourceAttributeDTO);
        return Result.ok();
    }


    /**
     * 删除模型属性
     *
     * @param modelId 模型id
     * @param attrId 属性id
     * @return res
     */
    @PostMapping("delete")
    @Operation(summary = "删除模型属性")
    @Parameters({
            @Parameter(name = "modelId", description = "模型id", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "id", description = "属性id", required = true, in = ParameterIn.QUERY)
    })
    public Result<Void> delete(@RequestParam("modelId") Long modelId, @RequestParam("id") Long attrId) {
        resourceAttributeService.deleteAttribute(modelId, attrId);
        return Result.ok();
    }


    /**
     * 编辑模型属性
     *
     * @param resourceAttributeDTO 模型属性
     * @return res
     */
    @PostMapping("update")
    @Operation(summary = "编辑模型属性")
    public Result<Void> update(@Valid @RequestBody ResourceAttributeDTO resourceAttributeDTO) {
        resourceAttributeService.update(resourceAttributeDTO);
        return Result.ok();
    }


    /**
     * 查询模型属性列表
     *
     * @param resourceAttributeQueryDTO 模型属性列表查询参数
     * @return 模型属性列表
     */
    @PostMapping("list")
    @Operation(summary = "查询模型属性列表")
    public Result<List<ResourceAttributeVO>> list(@Valid @RequestBody ResourceAttributeQueryDTO resourceAttributeQueryDTO) {
        return Result.ok(resourceAttributeService.list(resourceAttributeQueryDTO));
    }


    /**
     * 查看属性详情
     *
     * @param attrId 属性id
     * @return 属性详情
     */
    @GetMapping("detail")
    @Operation(summary = "查看属性详情")
    @Parameter(name = "id", description = "属性id", required = true, in = ParameterIn.QUERY)
    public Result<ResourceAttributeVO> detail(@RequestParam("id") Long attrId) {
        return Result.ok(resourceAttributeService.detail(attrId));
    }

}
