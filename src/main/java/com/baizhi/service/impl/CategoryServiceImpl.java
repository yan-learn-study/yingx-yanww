package com.baizhi.service.impl;

import com.baizhi.annocation.AddCache;
import com.baizhi.annocation.ClearCache;
import com.baizhi.annocation.LogAnnocation;
import com.baizhi.dao.CategoryMapper;
import com.baizhi.dao.VideoMapper;
import com.baizhi.entity.Category;
import com.baizhi.example.CategoryExample;
import com.baizhi.example.VideoExample;
import com.baizhi.po.CategoryPO;
import com.baizhi.service.CategoryService;
import com.baizhi.util.PageObject;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * @author yww
 * @Description
 * @Date 2020/11/20 14:52
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper dao;
    @Autowired
    private VideoMapper videoMapper;

    /**
     * @Description: 查询所有一级类别
     */
    @Override
    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PageObject<Category> queryAllOneLevel(Integer page, Integer rows) {
        PageObject<Category> pageObject = new PageObject<>();
        Example example = new Example(Category.class);
        example.createCriteria().andEqualTo("level", "1");
        //查询内容
        List<Category> categories = dao.selectByExampleAndRowBounds(example, new RowBounds((page - 1) * rows, rows));
        //查询内容总条数
        Integer totalCount = dao.selectCountByExample(example);
        //查询内容总页数
        Integer totalPage = pageObject.getTotalPage(totalCount, rows);
        pageObject.setPage(page.toString())
                .setTotal(totalPage)
                .setRecords(totalCount.toString())
                .setRows(categories);
        return pageObject;
    }

    /**
     * @Description: 查询所有二级类别
     */
    @Override
    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PageObject<Category> queryAllTwoLevel(Integer page, Integer rows, String id) {
        PageObject<Category> pageObject = new PageObject<>();
        CategoryExample example = new CategoryExample();
        example.createCriteria().andLevelEqualTo("2").andParentIdEqualTo(id);
        //查询内容
        List<Category> categories = dao.selectByExampleAndRowBounds(example, new RowBounds((page - 1) * rows, rows));
        //查询内容总条数
        Integer totalCount = dao.selectCountByExample(example);
        //查询内容总页数
        Integer totalPage = pageObject.getTotalPage(totalCount, rows);
        pageObject.setPage(page.toString())
                .setTotal(totalPage)
                .setRecords(totalCount.toString())
                .setRows(categories);
        return pageObject;
    }

    /**
     * @Description: 添加类别
     */
    @Override
    @ClearCache
    @LogAnnocation("添加类别")
    public Map<String, Object> saveCategory(Category category) {
        HashMap<String, Object> hashMap = new HashMap<>();
        category.setId(UUID.randomUUID().toString());
        category.setLevel("1");
        if (category.getParentId() != null) category.setLevel("2");
        try {
            dao.updateByPrimaryKeySelective(category);
            hashMap.put("status", 200);
            hashMap.put("message", "添加成功！");
            hashMap.put("rows", category);
        } catch (Exception e) {
            e.printStackTrace();
            hashMap.put("status", 201);
            hashMap.put("message", "添加失败！");
        }
        return hashMap;
    }

    /**
     * @Description: 修改类别
     */
    @Override
    @ClearCache
    @LogAnnocation("修改类别")
    public Map<String, Object> modifyCategory(Category category) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            dao.updateByPrimaryKeySelective(category);
            hashMap.put("status", 200);
            hashMap.put("message", "修改成功！");
            hashMap.put("rows", category);
        } catch (Exception e) {
            e.printStackTrace();
            hashMap.put("status", 201);
            hashMap.put("message", "修改失败！");
        }
        return hashMap;
    }

    @Override
    @ClearCache
    @LogAnnocation("删除类别")
    public Map<String, Object> removeCategory(Category category) throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        Category selectOne = dao.selectOne(category);
        //一级类别的处理
        if ("1".equals(selectOne.getLevel())) {
            CategoryExample example = new CategoryExample();
            example.createCriteria().andLevelEqualTo("2").andParentIdEqualTo(category.getId());
            if (dao.selectCountByExample(example) == 0) {
                dao.deleteByPrimaryKey(category.getId());
                hashMap.put("status", 200);
                hashMap.put("message", "删除成功！");
            } else {
                hashMap.put("status", 201);
                hashMap.put("message", "删除失败，请检查当前一级类别下是否有二级类别存在！");
                throw new RuntimeException("删除失败，请检查当前一级类别下是否有二级类别存在！");
            }
            //二级类别的处理
        } else {
            VideoExample videoExample = new VideoExample();
            videoExample.createCriteria().andCategoryIdEqualTo(category.getId());
            if (videoMapper.selectCountByExample(videoExample) == 0) {
                dao.deleteByPrimaryKey(category.getId());
                hashMap.put("status", 200);
                hashMap.put("message", "删除成功！");
            } else {
                hashMap.put("status", 201);
                hashMap.put("message", "删除失败，请检查当前二级类别下是否有视频存在！");
                throw new RuntimeException("删除失败，请检查当前二级类别下是否有视频存在！");
            }
        }
        return hashMap;
    }

    /**
     * @Description: 查询所有二级类别并返回List
     */
    @Override
    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Category> queryAllCategory() {
        CategoryExample example = new CategoryExample();
        example.createCriteria().andLevelEqualTo("2");
        List<Category> categories = dao.selectByExample(example);
        return categories;
    }

    /**
     * 查询所有类别并返回List<CategoryPO>
     */
    @Override
    @AddCache
    public List<CategoryPO> selectAllCategory() {
        return dao.selectAllCategory();
    }

}
