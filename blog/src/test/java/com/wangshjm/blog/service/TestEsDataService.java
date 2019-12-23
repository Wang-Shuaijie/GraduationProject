package com.wangshjm.blog.service;

import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.entity.UserContent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestEsDataService {
    @Autowired
    private EsDataService esDataService;
    @Autowired
    private UserContentService userContentService;

    @Test
    public void testSave(){
        List<UserContent> list = userContentService.findByUserId(5L);
        for(UserContent content: list){
            esDataService.saveOrUpdate(content);
        }
    }

    @Test
    public void search(){
        String keyWord="java";
        PageInfo<UserContent> pageInfo = esDataService.search(keyWord,1,7);
        for(UserContent userContent: pageInfo.getList()){
            System.out.println(userContent.getTitle());
        }
    }
}
