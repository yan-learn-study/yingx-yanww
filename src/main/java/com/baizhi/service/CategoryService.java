package com.baizhi.service;

import com.baizhi.entity.Category;
import com.baizhi.po.CategoryPO;
import com.baizhi.util.PageObject;

import java.util.List;
import java.util.Map;


/**
 * @author yww
 * @Description
 * @Date 2020/11/20 14:51
 */
public interface CategoryService {
    PageObject<Category> queryAllOneLevel(Integer page, Integer rows);

    PageObject<Category> queryAllTwoLevel(Integer page, Integer rows, String id);

    Map<String, Object> saveCategory(Category category);

    Map<String, Object> modifyCategory(Category category);

    Map<String, Object> removeCategory(Category category) throws Exception;

    List<Category> queryAllCategory();

    List<CategoryPO> selectAllCategory();


}