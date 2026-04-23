package com.example.docsys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.docsys.client.NlpClient;
import com.example.docsys.dto.*;
import com.example.docsys.entity.DocInfo;
import com.example.docsys.mapper.DocInfoMapper;
import com.example.docsys.service.DocService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocServiceImpl implements DocService {

    // MyBatis-Plus Mapper，负责数据库读写
    private final DocInfoMapper docInfoMapper;
    // NLP 客户端，负责调用 Python 智能服务
    private final NlpClient nlpClient;

    public DocServiceImpl(DocInfoMapper docInfoMapper, NlpClient nlpClient) {
        this.docInfoMapper = docInfoMapper;
        this.nlpClient = nlpClient;
    }

    @Override
    public DocInfo create(DocCreateRequest request) {
        // 1) 先保存原始文档
        DocInfo doc = new DocInfo();
        doc.setTitle(request.getTitle());
        doc.setCourseName(request.getCourseName());
        doc.setDocType(request.getDocType());
        doc.setAuthor(request.getAuthor());
        doc.setContent(request.getContent());
        doc.setCreatedAt(LocalDateTime.now());
        doc.setUpdatedAt(LocalDateTime.now());
        docInfoMapper.insert(doc);
        // 2) 再触发智能分析，补全分类与关键词
        return runIntelligence(doc.getId());
    }

    @Override
    public DocInfo getById(Long id) {
        return docInfoMapper.selectById(id);
    }

    @Override
    public List<DocInfo> search(SearchRequest request) {
        int pageNum = request.getPageNum() == null ? 1 : request.getPageNum();
        int pageSize = request.getPageSize() == null ? 10 : request.getPageSize();
        int offset = (pageNum - 1) * pageSize;

        // 无条件时走普通分页查询，有条件时走动态检索 SQL
        if (!StringUtils.hasText(request.getQ()) && !StringUtils.hasText(request.getCourseName()) && !StringUtils.hasText(request.getDocType())) {
            return docInfoMapper.selectList(new LambdaQueryWrapper<DocInfo>().orderByAsc(DocInfo::getId).last("limit " + offset + "," + pageSize));
        }

        // 走 SQL 检索
        return docInfoMapper.search(request.getQ(), request.getCourseName(), request.getDocType(), offset, pageSize);
    }

    @Override
    public void deleteById(Long id) {
        docInfoMapper.deleteById(id);
    }

    @Override
    public DocInfo runIntelligence(Long docId) {
        DocInfo doc = docInfoMapper.selectById(docId);
        if (doc == null) {
            throw new IllegalArgumentException("文档不存在");
        }

        // 调用分类接口
        NlpClassifyRequest classifyRequest = new NlpClassifyRequest();
        classifyRequest.setDocId(docId);
        classifyRequest.setTitle(doc.getTitle());
        classifyRequest.setContent(doc.getContent());
        NlpClassifyResponse classifyResponse = nlpClient.classify(classifyRequest);
        if (classifyResponse != null) {
            doc.setCategory(classifyResponse.getCategory());
            doc.setCategoryConfidence(classifyResponse.getConfidence());
        }

        // 调用关键词接口
        NlpKeywordsRequest keywordsRequest = new NlpKeywordsRequest();
        keywordsRequest.setDocId(docId);
        keywordsRequest.setContent(doc.getContent());
        keywordsRequest.setTopK(10);
        NlpKeywordsResponse keywordsResponse = nlpClient.keywords(keywordsRequest);
        if (keywordsResponse != null && keywordsResponse.getKeywords() != null) {
            doc.setKeywords(String.join(",", keywordsResponse.getKeywords()));
        }

        // 持久化智能分析结果
        doc.setUpdatedAt(LocalDateTime.now());
        docInfoMapper.updateById(doc);
        return doc;
    }
}