package com.wangshjm.blog.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.entity.UserContent;

import java.util.List;

public interface UserContentService {

    /**
     * 查询所有Content按照时间排序并分页
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<UserContent> findAll(Integer pageNum, Integer pageSize);

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
     * 查询所有文章集合
     *
     * @return
     */
    List<UserContent> findAll();

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
     * 根据分类查询所有文章并分类
     *
     * @param category
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<UserContent> findByCategory(String category, Long uid, Integer pageNum, Integer pageSize);

    /**
     * 查找所有不公开文章
     *
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<UserContent> findPersonal(Long uid, Integer pageNum, Integer pageSize);
}
