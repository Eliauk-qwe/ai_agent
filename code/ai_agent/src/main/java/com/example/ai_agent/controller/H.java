package com.example.ai_agent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Swagger 注解
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/health")
@Tag(name = "健康检查接口")
public class H {

    @GetMapping
    @Operation(summary = "健康检查", description = "用于检测服务是否正常运行")
    public String healthCheck() {
        return "ok";
    }
}