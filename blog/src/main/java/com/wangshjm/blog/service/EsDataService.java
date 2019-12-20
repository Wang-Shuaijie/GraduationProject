package com.wangshjm.blog.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.entity.UserContent;
import com.wangshjm.blog.utils.EsClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EsDataService {
    @Autowired
    private EsClientUtils esClientUtils;

    public void saveOrUpdate(UserContent userContent) {
        esClientUtils.saveOrUpdate(String.valueOf(userContent.getId()), JSONObject.toJSONString(userContent));
    }

    public void delete(UserContent userContent) {
        esClientUtils.delete(String.valueOf(userContent.getId()), JSONObject.toJSONString(userContent));
    }

    public PageInfo<UserContent> search(String keyWord, Integer pageNum, Integer pageSize) {
        //处理特殊字符
        keyWord = QueryParser.escape(keyWord);

        //初始化高亮对象
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");//高亮title
        highlightBuilder.field("content");//高亮内容
        highlightBuilder.preTags("<span style='color:red'>").postTags("</span>");//高亮标签

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchQuery("title", keyWord).analyzer("ik_smart"))
                        .should(QueryBuilders.matchQuery("content", keyWord).analyzer("ik_smart"))
                )
                .highlighter(highlightBuilder)
                .from((pageNum - 1) * pageSize)
                .size(pageSize);
        List<UserContent> list = esClientUtils.search(sourceBuilder);
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<UserContent> pageInfo = new PageInfo<UserContent>(list);
        return pageInfo;
    }
}
