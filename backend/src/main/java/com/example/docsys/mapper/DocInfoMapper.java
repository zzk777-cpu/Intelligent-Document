package com.example.docsys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.docsys.entity.DocInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DocInfoMapper extends BaseMapper<DocInfo> {

    // 动态检索：关键词匹配标题/正文/关键词，同时支持课程和类型过滤
    @Select({
            "<script>",
            "SELECT * FROM doc_info",
            "WHERE 1=1",
            "<if test='q != null and q != \"\"'>",
            " AND (title LIKE CONCAT('%', #{q}, '%') OR content LIKE CONCAT('%', #{q}, '%') OR keywords LIKE CONCAT('%', #{q}, '%'))",
            "</if>",
            "<if test='courseName != null and courseName != \"\"'>",
            " AND course_name = #{courseName}",
            "</if>",
            "<if test='docType != null and docType != \"\"'>",
            " AND doc_type = #{docType}",
            "</if>",
            "ORDER BY updated_at DESC",
            "LIMIT #{offset}, #{size}",
            "</script>"
    })
    List<DocInfo> search(@Param("q") String q,
                         @Param("courseName") String courseName,
                         @Param("docType") String docType,
                         @Param("offset") Integer offset,
                         @Param("size") Integer size);
}
