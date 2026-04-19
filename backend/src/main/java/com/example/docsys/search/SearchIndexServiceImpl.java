package com.example.docsys.search;

import com.example.docsys.entity.DocInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SearchIndexServiceImpl implements SearchIndexService {

    private final DocSearchRepository repository;

    @Value("${app.search.es-enabled:true}")
    private boolean esEnabled;

    public SearchIndexServiceImpl(DocSearchRepository repository) {
        this.repository = repository;
    }

    @Override
    public void upsert(DocInfo docInfo) {
        if (!esEnabled || docInfo == null || docInfo.getId() == null) {
            return;
        }
        try {
            DocSearchDocument doc = new DocSearchDocument();
            doc.setId(docInfo.getId());
            doc.setTitle(docInfo.getTitle());
            doc.setContent(docInfo.getContent());
            doc.setCourseName(docInfo.getCourseName());
            doc.setDocType(docInfo.getDocType());
            doc.setKeywords(docInfo.getKeywords());
            doc.setCategory(docInfo.getCategory());
            repository.save(doc);
        } catch (Exception ignored) {
            // ES 故障时不影响主流程，回退 MySQL 检索
        }
    }

    @Override
    public void delete(Long id) {
        if (!esEnabled || id == null) {
            return;
        }
        try {
            repository.deleteById(id);
        } catch (Exception ignored) {
            // ignore
        }
    }

    @Override
    public List<Long> searchDocIds(String q, String courseName, String docType, Integer offset, Integer size) {
        if (!esEnabled || !StringUtils.hasText(q)) {
            return Collections.emptyList();
        }
        List<DocSearchDocument> candidates;
        try {
            candidates = repository.findByTitleContainingOrContentContainingOrKeywordsContaining(q, q, q);
        } catch (Exception ignored) {
            return Collections.emptyList();
        }

        List<Long> ids = candidates.stream()
                .filter(item -> !StringUtils.hasText(courseName) || Objects.equals(courseName, item.getCourseName()))
                .filter(item -> !StringUtils.hasText(docType) || Objects.equals(docType, item.getDocType()))
                .map(DocSearchDocument::getId)
                .collect(Collectors.toList());

        int safeOffset = Math.max(0, offset == null ? 0 : offset);
        int safeSize = Math.max(1, size == null ? 10 : size);
        if (safeOffset >= ids.size()) {
            return Collections.emptyList();
        }
        int end = Math.min(ids.size(), safeOffset + safeSize);
        return ids.subList(safeOffset, end);
    }
}
