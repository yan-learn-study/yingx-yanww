package com.baizhi.service;

import com.baizhi.entity.Log;
import com.baizhi.util.PageObject;

/**
 * @author yww
 * @Description
 * @Date 2020/11/24 20:34
 */
public interface LogService {

    PageObject<Log> queryAllLog(Integer page, Integer rows);
}
