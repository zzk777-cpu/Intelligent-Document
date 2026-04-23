package com.example.docsys.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface DocSearchRepository extends ElasticsearchRepository<DocSearchDocument, Long> {

    // ✅ 原有方法（保留）
    List<DocSearchDocument> findByTitleContainingOrContentContainingOrKeywordsContaining(String title, String content, String keywords);

    // ✅ 新增：按分类搜索
    List<DocSearchDocument> findByCategoryContaining(String category);

    // ✅ 新增：组合搜索（标题、内容、关键词、分类）
    List<DocSearchDocument> findByTitleContainingOrContentContainingOrKeywordsContainingOrCategoryContaining(String title, String content, String keywords, String category);
}