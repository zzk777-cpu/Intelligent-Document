package com.example.docsys.dto;

import lombok.Data;

@Data
public class NlpClassifyRequest {
    private Long docId;
    private String title;
    private String content;
}
