package com.example.docsys.search;

import com.example.docsys.entity.DocInfo;

import java.util.List;

public interface SearchIndexService {
    void upsert(DocInfo docInfo);

    void delete(Long id);

    void batchUpsert(List<DocInfo> docInfos);

    List<Long> searchDocIds(String q, String courseName, String docType, Integer offset, Integer size);
}
