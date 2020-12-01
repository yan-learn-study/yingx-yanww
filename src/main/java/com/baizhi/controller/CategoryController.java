package com.baizhi.controller;

import com.baizhi.entity.Category;
import com.baizhi.service.CategoryService;
import com.baizhi.util.PageObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @author yww
 * @Description
 * @Date 2020/11/20 14:54
 */
@Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService service;
    @Autowired
    private HttpServletResponse response;


    @ResponseBody
    @RequestMapping("queryAllOneLevel")
    public PageObject<Category> queryAllOneLevel(Integer page, Integer rows) {
        return service.queryAllOneLevel(page, rows);
    }

    @ResponseBody
    @RequestMapping("editCategory")
    public Map<String, Object> editCategory(Category category, String oper) throws Exception {
        Map<String, Object> map = null;
        if ("add".equals(oper)) map = service.saveCategory(category);
        else if ("edit".equals(oper)) map = service.modifyCategory(category);
        else if ("del".equals(oper)) map = service.removeCategory(category);
        return map;

    }

    @ResponseBody
    @RequestMapping("queryAllTwoLevel")
    public PageObject<Category> queryAllTwoLevel(Integer page, Integer rows, String id) {
        return service.queryAllTwoLevel(page, rows, id);
    }

    @ResponseBody
    @RequestMapping("queryAllCategory")
    public void queryAllCategory() throws IOException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<select>");
        List<Category> categories = service.queryAllCategory();
        categories.forEach(category -> buffer.append("<option value='" + category.getId() + "'>" + category.getName() + "</option>"));
        buffer.append("<select>");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.getWriter().print(buffer);
    }
}
