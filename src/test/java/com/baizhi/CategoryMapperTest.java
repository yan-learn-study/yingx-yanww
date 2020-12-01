package com.baizhi;

import com.baizhi.dao.CategoryMapper;
import com.baizhi.entity.Category;
import com.baizhi.example.CategoryExample;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.mapper.entity.Example;


@SpringBootTest
class CategoryMapperTest {

    @Autowired
    CategoryMapper dao;

    @Test
    public void testSelect() {
        Example o = new Example(Category.class);
        o.createCriteria().andEqualTo("level", "1");
        dao.selectByExample(o).forEach(category -> System.out.println(category));

    }

    @Test
    public void testSelectAll() {
        CategoryExample example = new CategoryExample();
        example.createCriteria().andLevelEqualTo("2").andParentIdEqualTo("1");
        dao.selectByExample(example).forEach(category -> System.out.println(category));
    }

}
