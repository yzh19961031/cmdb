package com.yzh.cmdb.controller;

import cn.hutool.db.PageResult;
import com.yzh.cmdb.domain.Result;
import com.yzh.cmdb.domain.dto.DynamicInstanceDTO;
import com.yzh.cmdb.domain.dto.InstanceDeleteDTO;
import com.yzh.cmdb.domain.dto.ResourceInstanceQueryDTO;
import com.yzh.cmdb.domain.vo.DynamicInstanceVO;
import com.yzh.cmdb.service.ResourceInstanceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资源实例controller
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@RestController
@RequestMapping(value = "instance")
@AllArgsConstructor
public class ResourceInstanceController {

    private final ResourceInstanceService resourceInstanceService;


    /**
     * 新增实例数据
     *
     * @param dynamicInstanceDTO 实例信息
     * @return res
     */
    @PostMapping("add")
    public Result<Void> add(@RequestBody DynamicInstanceDTO dynamicInstanceDTO) {
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
    public Result<Void> delete(@RequestBody InstanceDeleteDTO deleteDTO) {
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
    public Result<PageResult<DynamicInstanceVO>> list(@RequestBody ResourceInstanceQueryDTO resourceInstanceQueryDTO) {
        return Result.ok(resourceInstanceService.list(resourceInstanceQueryDTO));
    }


    /**
     * 更新实例
     *
     * @param dynamicInstanceDTO 实例详情
     * @return res
     */
    @PostMapping("update")
    public Result<Void> update(@RequestBody DynamicInstanceDTO dynamicInstanceDTO) {
        resourceInstanceService.update(dynamicInstanceDTO);
        return Result.ok();
    }
}
