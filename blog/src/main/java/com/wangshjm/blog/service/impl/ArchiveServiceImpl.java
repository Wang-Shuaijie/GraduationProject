package com.wangshjm.blog.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangshjm.blog.dao.ArchiveMapper;
import com.wangshjm.blog.entity.Archive;
import com.wangshjm.blog.service.ArchiveService;
import com.wangshjm.blog.service.UserContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ArchiveServiceImpl implements ArchiveService {
    @Autowired
    private ArchiveMapper archiveMapper;
    @Autowired
    private UserContentService userContentService;

    @Override
    public JSONObject findArchiveNameAndArticleNum() {
        List<Archive> archiveList = archiveMapper.selectAll();
        JSONArray archivesJsonArray = new JSONArray();
        JSONObject archiveJson;
        for(Archive archive: archiveList){
            archiveJson = new JSONObject();
            archiveJson.put("archiveName",archive.getArchiveName());
            archiveJson.put("archiveArticleNum",userContentService.countByArchive(archive.getArchiveName()));
            archivesJsonArray.add(archiveJson);
        }
        JSONObject returnJson = new JSONObject();
        returnJson.put("result", archivesJsonArray);
        return returnJson;
    }
}
