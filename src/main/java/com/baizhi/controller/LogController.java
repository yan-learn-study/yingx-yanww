package com.baizhi.controller;

import com.baizhi.entity.Log;
import com.baizhi.service.LogService;
import com.baizhi.util.PageObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yww
 * @Description
 * @Date 2020/11/24 20:38
 */
@Controller
@RequestMapping("log")
public class LogController {
    @Autowired
    private LogService service;

    @RequestMapping("queryAllLog")
    @ResponseBody
    public PageObject<Log> queryAllLog(Integer page, Integer rows) {
        return service.queryAllLog(page, rows);
    }
}
