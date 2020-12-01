package com.baizhi.controller;

import com.baizhi.entity.Admin;
import com.baizhi.service.AdminService;
import com.baizhi.util.ImageCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yww
 * @Description
 * @Date 2020/11/18 19:38
 */
@Controller
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private AdminService service;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    private Logger log = LoggerFactory.getLogger(AdminController.class);

    @RequestMapping("login")
    @ResponseBody
    public Map<String, String> login(Admin admin, String code) {
        log.info("收到的数据：{},验证码：{}", admin, code);
        HashMap<String, String> map = new HashMap<>();
        try {
            service.login(admin, code);
            map.put("message", "登录成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("getImageCode*")
    @ResponseBody
    public void getImageCode() throws IOException {
        String code = ImageCodeUtil.getSecurityCode();
        BufferedImage image = ImageCodeUtil.createImage(code);
        request.getSession().setAttribute("code", code);
        log.info("验证码：{}", code);
        ImageIO.write(image, "png", response.getOutputStream());
    }

    @RequestMapping("exit")
    public String exit() {
        HttpSession session = request.getSession();
        session.removeAttribute("admin");
        session.invalidate();
        return "redirect:/login/login.jsp";
    }
}
