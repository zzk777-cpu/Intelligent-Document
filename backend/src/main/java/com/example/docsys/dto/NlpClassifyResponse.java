package com.example.docsys.dto;

import lombok.Data;

@Data
public class NlpClassifyResponse {
    private String category;
    private Double confidence;
}
