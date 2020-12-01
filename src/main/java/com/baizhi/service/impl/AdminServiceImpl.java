package com.baizhi.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baizhi.dao.AdminMapper;
import com.baizhi.entity.Admin;
import com.baizhi.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author yww
 * @Description
 * @Date 2020/11/18 19:35
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper dao;
    @Autowired
    private HttpServletRequest request;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void login(Admin admin, String code) {
        HttpSession session = request.getSession();
        String c = (String) session.getAttribute("code");
        if (!StringUtils.equals(c, code)) throw new RuntimeException("验证码输入错误！！！");
        Admin am = dao.selectByUsername(admin.getUsername());
        if (am == null) throw new RuntimeException("用户名不存在！！！");
        if (!StringUtils.equals(am.getPassword(), admin.getPassword())) throw new RuntimeException("密码输入错误！！！");
        session.setAttribute("admin", am);
    }
}
