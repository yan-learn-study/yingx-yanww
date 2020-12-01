package com.baizhi.service;

import com.baizhi.entity.Admin;

/**
 * @author yww
 * @Description
 * @Date 2020/11/18 19:32
 */
public interface AdminService {
    void login(Admin admin, String code);
}
