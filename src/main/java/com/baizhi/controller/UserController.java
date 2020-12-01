package com.baizhi.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baizhi.entity.User;
import com.baizhi.po.UserVO;
import com.baizhi.service.UserService;
import com.baizhi.util.PageObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author yww
 * @Description
 * @Date 2020/11/19 14:31
 */
@Controller
@RequestMapping("user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService service;


    @RequestMapping("queryUserByPage")
    @ResponseBody
    public PageObject<User> queryUserByPage(Integer page, Integer rows) {
        PageObject<User> pageObject = service.queryUserByPage(page, rows);
        return pageObject;
    }

    @RequestMapping("modifyUserStatus")
    @ResponseBody
    public Map<String, String> modifyUserStatus(User user) {
        log.info("修改用户状态收集到的数据:{}", user);
        HashMap<String, String> map = new HashMap<>();
        try {
            service.modifyUserStatus(user);
            map.put("message", "修改成功！");
            map.put("status", "200");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "修改失败！");
            map.put("status", "201");
        }
        return map;
    }

    @RequestMapping("editUser")
    @ResponseBody
    public Map<String, Object> editUser(String oper, User user) {
        log.info("添加用户信息收集到的数据:{}", user);
        HashMap<String, Object> map = new HashMap<>();
        if (oper.equals("add")) {
            try {
                service.saveUser(user);
                map.put("message", "添加成功！");
                map.put("success", true);
                map.put("rows", user);
                map.put("status", "200");
            } catch (Exception e) {
                e.printStackTrace();
                map.put("message", "添加失败！");
                map.put("success", false);
                map.put("status", "201");
            }
        }
        return map;
    }

    @RequestMapping("upHeadShow")
    @ResponseBody
    public void upHeadShow(MultipartFile headShow, String id) throws Exception {
        log.info("上传文件名：{}", headShow.getOriginalFilename());
        service.upHeadShow(headShow, id);
    }

    @RequestMapping("aliyun")
    @ResponseBody
    public Map<String, Object> aliyun(String phoneNumbers) {
        log.info("收到的手机号码：{}", phoneNumbers);
        HashMap<String, Object> map = new HashMap<>();
        try {
            SendSmsResponse response = service.aliyun(phoneNumbers);
            map.put("status", response.getMessage());
            map.put("message", "发送成功");
        } catch (ClientException e) {
            e.printStackTrace();
            map.put("message", "发送失败，请稍后再试！");
        }
        return map;
    }

    @RequestMapping("exportExcel")
    @ResponseBody
    public HashMap<String, Object> exportExcel() {
        HashMap<String, Object> map = new HashMap<>();
        try {
            service.exportExcel();
            map.put("status", 200);
            map.put("message", "导出成功，请在E盘查看");
        } catch (IOException e) {
            e.printStackTrace();
            map.put("status", 201);
            map.put("message", "导出失败，请稍后再试");
        }
        return map;
    }

    @RequestMapping("queryCityBySex")
    @ResponseBody
    public HashMap<String, Object> queryCityBySex() {
        HashMap<String, Object> map = new HashMap<>();
        try {
            List<UserVO> userVOS = service.queryCityBySex();
            map.put("status", 200);
            map.put("message", "查询成功");
            map.put("rows", userVOS);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 201);
            map.put("message", "查询失败");

        }
        return map;
    }

    @RequestMapping("queryCountByMonth")
    @ResponseBody
    public Map<String, Object> queryCountByMonth() {
        return service.queryCountByMonth();
    }

}
