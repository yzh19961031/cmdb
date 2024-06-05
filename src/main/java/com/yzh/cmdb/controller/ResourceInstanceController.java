package com.yzh.cmdb.controller;

import com.yzh.cmdb.domain.PageResult;
import com.yzh.cmdb.domain.Result;
import com.yzh.cmdb.domain.dto.DynamicInstanceDTO;
import com.yzh.cmdb.domain.dto.InstanceDeleteDTO;
import com.yzh.cmdb.domain.dto.ResourceInstanceQueryDTO;
import com.yzh.cmdb.domain.vo.DynamicInstanceVO;
import com.yzh.cmdb.service.ResourceInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 资源实例controller
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@RestController
@RequestMapping(value = "instance")
@AllArgsConstructor
@Tag(name = "资源实例")
public class ResourceInstanceController {

    private final ResourceInstanceService resourceInstanceService;


    /**
     * 新增实例数据
     *
     * @param dynamicInstanceDTO 实例信息
     * @return res
     */
    @PostMapping("add")
    @Operation(summary = "新增实例数据")
    public Result<Void> add(@Valid @RequestBody DynamicInstanceDTO dynamicInstanceDTO) {
        resourceInstanceService.add(dynamicInstanceDTO);
        return Result.ok();
    }

    /**
     * 删除
     *
     * @param deleteDTO 删除详情
     * @return res
     */
    @PostMapping("delete")
    @Operation(summary = "删除实例数据")
    public Result<Void> delete(@Valid @RequestBody InstanceDeleteDTO deleteDTO) {
        resourceInstanceService.delete(deleteDTO);
        return Result.ok();
    }


    /**
     * 获取实例列表
     *
     * @param resourceInstanceQueryDTO 查询参数
     * @return 实例列表
     */
    @PostMapping("list")
    @Operation(summary = "获取实例列表")
    public Result<PageResult<DynamicInstanceVO>> list(@Valid @RequestBody ResourceInstanceQueryDTO resourceInstanceQueryDTO) {
        return Result.ok(resourceInstanceService.list(resourceInstanceQueryDTO));
    }


    /**
     * 更新实例
     *
     * @param dynamicInstanceDTO 实例详情
     * @return res
     */
    @PostMapping("update")
    @Operation(summary = "更新实例")
    public Result<Void> update(@Valid @RequestBody DynamicInstanceDTO dynamicInstanceDTO) {
        resourceInstanceService.update(dynamicInstanceDTO);
        return Result.ok();
    }
}
