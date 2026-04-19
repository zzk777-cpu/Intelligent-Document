package com.example.docsys.dto;

import lombok.Data;

@Data
public class NlpKeywordsRequest {
    private Long docId;
    private String content;
    private Integer topK = 10;
}
