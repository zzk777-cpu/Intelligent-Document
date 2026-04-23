package com.example.docsys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("doc_info")
public class DocInfo {
    @TableId
    private Long id;
    private String title;
    private String courseName;
    private String docType;
    private String author;
    private String fileUrl;
    private String content;
    private String category;
    private Double categoryConfidence;
    private String keywords;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
