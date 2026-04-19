package com.example.docsys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@MapperScan("com.example.docsys.mapper")
@EnableElasticsearchRepositories(basePackages = "com.example.docsys.search")
public class CourseDocSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseDocSystemApplication.class, args);
    }
}
