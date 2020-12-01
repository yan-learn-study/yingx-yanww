package com.baizhi.service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baizhi.entity.User;
import com.baizhi.po.UserVO;
import com.baizhi.util.PageObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @author yww
 * @Description
 * @Date 2020/11/19 14:30
 */
public interface UserService {

    PageObject<User> queryUserByPage(Integer page, Integer rows);

    void modifyUserStatus(User user);

    void saveUser(User user);

    void upHeadShow(MultipartFile headShow, String id) throws Exception;

    SendSmsResponse aliyun(String phoneNumbers) throws ClientException;

    void exportExcel() throws IOException;

    List<UserVO> queryCityBySex();

    Map<String, Object> queryCountByMonth();
}
