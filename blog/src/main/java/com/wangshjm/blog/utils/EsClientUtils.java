package com.wangshjm.blog.utils;

import com.alibaba.fastjson.JSONObject;
import com.wangshjm.blog.entity.UserContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class EsClientUtils {
    @Autowired
    private RestHighLevelClient rhlClient;
    @Autowired
    private RestClient restClient;

    private final String INDEX_NAME = "usercontent";
    private final String TYPE_NAME = "article";

    public void saveOrUpdate(String id, String jsonString) {
        IndexRequest request = new IndexRequest(
                INDEX_NAME,
                TYPE_NAME,
                id
        );
        request.source(jsonString, XContentType.JSON);
        IndexResponse response = null;
        try {
            response = rhlClient.index(request);
            if (response.status().getStatus() == HttpStatus.SC_CREATED || response.status().getStatus() == HttpStatus.SC_OK) {
                log.info("index success");
            } else {
                log.error("index error : {}", response.toString());
            }
        } catch (IOException e) {
            log.error("系统异常", e);
        }
    }

    public void delete(String id, String jsonString) {
        DeleteRequest deleteRequest = new DeleteRequest(
                INDEX_NAME,
                TYPE_NAME,
                id
        );
        DeleteResponse response = null;
        try {
            response = rhlClient.delete(deleteRequest);
            if (response.status().getStatus() == HttpStatus.SC_CREATED || response.status().getStatus() == HttpStatus.SC_OK) {
                log.info("delete success");
            } else {
                log.error("delete error : {}", response.toString());
            }
        } catch (IOException e) {
            log.error("系统异常", e);
        }
    }


    public List<UserContent> search(SearchSourceBuilder sourceBuilder) {
        SearchRequest searchRequest = new SearchRequest()
                .indices(INDEX_NAME)
                .types(TYPE_NAME)
                .source(sourceBuilder);
        SearchResponse response = null;
        try {
            response = rhlClient.search(searchRequest);
            if (response.status().getStatus() == HttpStatus.SC_OK) {
                // 提取数据
                return parseSearchResponse(response);
            } else {
                log.error("search false, error : {}", response);
                return null;
            }
        } catch (IOException e) {
            log.error("系统异常", e);
            return null;
        }
    }

    private List<UserContent> parseSearchResponse(SearchResponse response) throws IOException {
        List<UserContent> list = new ArrayList<>();
        response.getHits().forEach(hit -> {
            UserContent userContent = JSONObject.parseObject(hit.getSourceAsString(), UserContent.class);
            list.add(userContent);
        });
        return list;
    }
}
