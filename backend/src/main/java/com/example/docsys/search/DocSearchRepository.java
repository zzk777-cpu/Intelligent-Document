package com.example.docsys.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface DocSearchRepository extends ElasticsearchRepository<DocSearchDocument, Long> {
    List<DocSearchDocument> findByTitleContainingOrContentContainingOrKeywordsContaining(String title,
                                                                                           String content,
                                                                                           String keywords);
}
