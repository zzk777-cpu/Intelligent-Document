package com.example.docsys.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DocCreateRequest {
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotBlank(message = "课程名不能为空")
    private String courseName;
    private String docType;
    private String author;
    @NotBlank(message = "内容不能为空")
    private String content;
}
