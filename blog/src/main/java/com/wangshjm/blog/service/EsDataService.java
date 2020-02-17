package com.wangshjm.blog.service;

import com.alibaba.fastjson.JSONObject;
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

    public void delete(String id) {
        esClientUtils.delete(id);
    }

    public PageInfo<UserContent> search(String keyWord, Integer pageNum, Integer pageSize) {
        //处理特殊字符
        keyWord = QueryParser.escape(keyWord);
        //初始化高亮对象
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("nickName");
        highlightBuilder.field("title");//高亮title
//        highlightBuilder.field("content");//高亮内容
        //下面这两项,如果你要高亮如文字内容等有很多字的字段,必须配置,不然会导致高亮不全,文章内容缺失等
        highlightBuilder.fragmentSize(800000); //最大高亮分片数
        highlightBuilder.numOfFragments(0); //从第一个分片获取高亮片段

        highlightBuilder.preTags("<span style=\"color:#F56C6C\">").postTags("</span>");//高亮标签

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchQuery("nickName", keyWord))
                        .should(QueryBuilders.matchQuery("title", keyWord))
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
