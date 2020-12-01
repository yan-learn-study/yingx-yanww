package com.baizhi.service;

import com.baizhi.entity.Feedback;
import com.baizhi.util.PageObject;

/**
 * @author yww
 * @Description
 * @Date 2020/11/25 9:05
 */
public interface FeedbackService {
    PageObject<Feedback> queryAllFeedback(Integer page, Integer rows);
}
