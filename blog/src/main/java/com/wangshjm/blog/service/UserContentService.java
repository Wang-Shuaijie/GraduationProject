package com.wangshjm.blog.service;

import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.entity.UserContent;

import java.util.List;

public interface UserContentService {

    /**
     * 添加文章
     *
     * @param content
     */
    int addContent(UserContent content);

    /**
     * 根据用户id查询文章集合
     *
     * @param uid
     * @return
     */
    List<UserContent> findByUserId(Long uid);


    /**
     * 根据文章id查找文章
     *
     * @param id
     * @return
     */
    UserContent findById(long id);

    /**
     * 更新文章
     *
     * @param content
     */
    int update(UserContent content);

    /**
     * 删除文章
     *
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 根据用户id查询出分类
     *
     * @param uid
     * @return
     */
    List<UserContent> findCategoryByUid(Long uid);

    /**
     * 模糊查询，根据userContent中的参数来查
     * @param userContent
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<UserContent> findAll(UserContent userContent, Integer pageNum, Integer pageSize);

    /**
     * 根据点赞数查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<UserContent> findByUpvote();

    PageInfo<UserContent> findByArchive(String archive, Integer pageNum, Integer pageSize);

    PageInfo<UserContent> findByTag(String tag, Integer pageNum, Integer pageSize);

    PageInfo<UserContent> findRefuseContent(Long uid, Integer pageNum, Integer pageSize);

    /**
     * 查询该分类下的文章数
     * @param categoryName
     * @return
     */
    int countByCategory(String categoryName);

    /**
     * 查询该日期的文章数
     * @param archiveName
     * @return
     */
    int countByArchive(String archiveName);

    int countArticle();

}
