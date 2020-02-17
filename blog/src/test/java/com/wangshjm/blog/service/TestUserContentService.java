package com.wangshjm.blog.service;

import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.entity.UserContent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserContentService {
    @Autowired
    private UserContentService userContentService;

    @Test
    public void testFindAll(){
        PageInfo<UserContent> pageInfo = userContentService.findAll(null,0,7);
        pageInfo.getList().forEach(list-> System.out.println(list.getNickName()+list.getTitle()));
    }
}
