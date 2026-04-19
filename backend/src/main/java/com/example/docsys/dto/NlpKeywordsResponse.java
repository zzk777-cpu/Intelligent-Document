package com.example.docsys.dto;

import lombok.Data;

import java.util.List;

@Data
public class NlpKeywordsResponse {
    private List<String> keywords;
}
