package com.baizhi.controller;

import com.baizhi.entity.Feedback;
import com.baizhi.service.FeedbackService;
import com.baizhi.util.PageObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yww
 * @Description
 * @Date 2020/11/25 9:10
 */
@Controller
@RequestMapping("feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService service;

    @ResponseBody
    @RequestMapping("queryAllFeedback")
    public PageObject<Feedback> queryAllFeedback(Integer page, Integer rows) {
        return service.queryAllFeedback(page, rows);
    }
}
