package com.example.docsys.service;

import com.example.docsys.dto.DocCreateRequest;
import com.example.docsys.dto.SearchRequest;
import com.example.docsys.entity.DocInfo;

import java.util.List;

public interface DocService {
    DocInfo create(DocCreateRequest request);
    DocInfo getById(Long id);
    List<DocInfo> search(SearchRequest request);
    void deleteById(Long id);
    DocInfo runIntelligence(Long docId);
}
