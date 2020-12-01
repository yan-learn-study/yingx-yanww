package com.baizhi.dao;


import com.baizhi.entity.Category;
import com.baizhi.po.CategoryPO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category> {
    List<CategoryPO> selectAllCategory();
}