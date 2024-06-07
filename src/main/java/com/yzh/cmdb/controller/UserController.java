package com.yzh.cmdb.controller;

import cn.hutool.core.lang.UUID;
import com.yzh.cmdb.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供一个简单的登录接口
 * 暂时就先提供一个mock
 *
 * @author yuanzhihao
 * @since 2024/6/6
 */
@RestController
@RequestMapping(value = "user")
@AllArgsConstructor
@Tag(name = "用户管理")
public class UserController {

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return token
     */
    @PostMapping("login")
    @Operation(summary = "用户登录")
    public Result<String> login(@RequestBody LoginRequest loginRequest) {
        return Result.ok(UUID.fastUUID().toString());
    }


    /**
     * 用户登出
     *
     * @return res
     */
    @PostMapping("logout")
    @Operation(summary = "用户登出")
    public Result<Void> logout() {
        return Result.ok();
    }


    /**
     * 获取用户信息
     *
     * @param token token
     * @return 用户信息
     */
    @GetMapping("info")
    @Operation(summary = "获取用户信息")
    public Result<UserInfo> info(@RequestParam("token") String token) {
        UserInfo userInfo = UserInfo.builder()
                .avatar("https://avatars.githubusercontent.com/u/33096440")
                .introduction("I am a super administrator")
                .name("Yuandupier")
                .build();
        return Result.ok(userInfo);
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class UserInfo {
        String introduction;
        String avatar;
        String name;
    }

    @Data
    private static class LoginRequest {
        @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin")
        private String username;

        @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
        private String password;
    }
}
