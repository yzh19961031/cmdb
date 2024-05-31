package com.yzh.cmdb.controller;

import com.yzh.cmdb.domain.Result;
import com.yzh.cmdb.domain.dto.ResourceValidationDTO;
import com.yzh.cmdb.domain.vo.ResourceValidationVO;
import com.yzh.cmdb.service.ResourceValidationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 模型唯一校验controller
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@RestController
@RequestMapping(value = "model/validation")
@AllArgsConstructor
public class ResourceValidationController {

    private final ResourceValidationService resourceValidationService;


    /**
     * 添加唯一校验
     *
     * @param resourceValidationDTO 唯一校验属性
     * @return res
     */
    @PostMapping("add")
    public Result<Void> add(@RequestBody ResourceValidationDTO resourceValidationDTO) {
        resourceValidationService.add(resourceValidationDTO);
        return Result.ok();
    }

    /**
     * 删除唯一校验
     *
     * @param id 唯一校验id
     * @return res
     */
    @PostMapping("delete")
    public Result<Void> delete(@RequestParam("id") Long id) {
        resourceValidationService.delete(id);
        return Result.ok();
    }


    /**
     * 获取指定模型的唯一校验列表
     *
     * @param modelId 模型id
     * @return 唯一校验列表
     */
    @GetMapping("list")
    public Result<List<ResourceValidationVO>> list(@RequestParam("modelId") Long modelId) {
        return Result.ok(resourceValidationService.list(modelId));
    }
}
