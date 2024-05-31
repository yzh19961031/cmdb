package com.yzh.cmdb.controller;

import com.yzh.cmdb.domain.Result;
import com.yzh.cmdb.domain.dto.ResourceRelationDTO;
import com.yzh.cmdb.domain.vo.GroupResourceRelationVO;
import com.yzh.cmdb.domain.vo.ResourceModelTopologyVO;
import com.yzh.cmdb.service.ResourceRelationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模型关联控制器
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@RestController
@RequestMapping(value = "model/relation")
@AllArgsConstructor
public class ResourceRelationController {

    private final ResourceRelationService resourceRelationService;


    /**
     * 添加模型关联关系
     *
     * @param resourceRelationDTO 模型关联关系
     * @return res
     */
    @PostMapping("add")
    public Result<Void> add(@RequestBody ResourceRelationDTO resourceRelationDTO) {
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
    public Result<GroupResourceRelationVO> list(@RequestParam("modelId") Long modelId) {
        return Result.ok(resourceRelationService.list(modelId));
    }


    /**
     * 生成模型拓扑图
     *
     * @return 拓扑详情
     */
    @GetMapping("recursiveTopology")
    public Result<ResourceModelTopologyVO> recursiveTopology() {
        return Result.ok(resourceRelationService.recursiveTopology());
    }
}
