package com.baizhi.service.impl;

import com.baizhi.dao.LogMapper;
import com.baizhi.entity.Log;
import com.baizhi.service.LogService;
import com.baizhi.util.PageObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yww
 * @Description
 * @Date 2020/11/24 20:34
 */
@Service
@Transactional
public class LogServiceImpl implements LogService {
    @Autowired
    private LogMapper logMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PageObject<Log> queryAllLog(Integer page, Integer rows) {
        PageObject<Log> pageObject = new PageObject<>();
        List<Log> logs = logMapper.selectAll();
        Integer count = logMapper.selectCount(new Log());
        pageObject.setPage(page.toString());
        pageObject.setTotal(pageObject.getTotalPage(count, rows));
        pageObject.setRecords(count.toString());
        pageObject.setRows(logs);
        return pageObject;
    }
}
