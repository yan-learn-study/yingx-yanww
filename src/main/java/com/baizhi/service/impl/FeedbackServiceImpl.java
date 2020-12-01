package com.baizhi.service.impl;

import com.baizhi.dao.FeedbackMapper;
import com.baizhi.entity.Feedback;
import com.baizhi.service.FeedbackService;
import com.baizhi.util.PageObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yww
 * @Description
 * @Date 2020/11/25 9:06
 */
@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PageObject<Feedback> queryAllFeedback(Integer page, Integer rows) {
        PageObject<Feedback> pageObject = new PageObject<>();
        List<Feedback> feedbacks = feedbackMapper.selectAll();
        Integer count = feedbackMapper.selectCount(new Feedback());
        pageObject.setPage(page.toString());
        pageObject.setTotal(pageObject.getTotalPage(count, rows));
        pageObject.setRecords(count.toString());
        pageObject.setRows(feedbacks);
        return pageObject;
    }
}
