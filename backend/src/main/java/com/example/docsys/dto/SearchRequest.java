package com.example.docsys.dto;

import lombok.Data;

@Data
public class SearchRequest {
    private String q;
    private String courseName;
    private String docType;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
