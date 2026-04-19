package com.example.docsys.client;

import com.example.docsys.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NlpClient {

    private final RestTemplate restTemplate;

    @Value("${app.nlp.base-url}")
    private String baseUrl;

    public NlpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public NlpClassifyResponse classify(NlpClassifyRequest request) {
        // 调用 Python 服务分类接口
        return restTemplate.postForObject(baseUrl + "/nlp/classify", request, NlpClassifyResponse.class);
    }

    public NlpKeywordsResponse keywords(NlpKeywordsRequest request) {
        // 调用 Python 服务关键词接口
        return restTemplate.postForObject(baseUrl + "/nlp/keywords", request, NlpKeywordsResponse.class);
    }
}
