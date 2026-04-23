package com.example.docsys.controller;

import com.example.docsys.common.ApiResponse;
import com.example.docsys.dto.DocCreateRequest;
import com.example.docsys.dto.SearchRequest;
import com.example.docsys.entity.DocInfo;
import com.example.docsys.service.DocService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/docs")
public class DocController {

    // 文档业务服务，负责文档增删查与智能分析流程
    private final DocService docService;

    public DocController(DocService docService) {
        this.docService = docService;
    }

    @PostMapping
    public ApiResponse<DocInfo> create(@RequestBody @Valid DocCreateRequest request) {
        // 创建文档后会自动调用 NLP 服务进行分类与关键词提取
        return ApiResponse.ok(docService.create(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<DocInfo> detail(@PathVariable Long id) {
        return ApiResponse.ok(docService.getById(id));
    }

    @PostMapping("/search")
    public ApiResponse<List<DocInfo>> search(@RequestBody SearchRequest request) {
        // 支持按关键词、课程名、文档类型进行组合检索
        return ApiResponse.ok(docService.search(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        docService.deleteById(id);
        return ApiResponse.ok(null);
    }
    @PostMapping("/{id}/intelligence")
    public ApiResponse<DocInfo> intelligence(@PathVariable Long id) {
        // 手动重跑智能分析（适用于模型升级后重新计算）
        return ApiResponse.ok(docService.runIntelligence(id));
    }
}
