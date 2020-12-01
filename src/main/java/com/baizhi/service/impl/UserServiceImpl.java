package com.baizhi.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.aliyun.oss.OSS;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baizhi.annocation.LogAnnocation;
import com.baizhi.dao.UserMapper;
import com.baizhi.entity.User;
import com.baizhi.po.UserPO;
import com.baizhi.po.UserVO;
import com.baizhi.service.UserService;
import com.baizhi.util.*;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yww
 * @Description
 * @Date 2020/11/19 14:41
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserMapper dao;
    @Autowired
    private HttpServletRequest request;

    /**
     * @Description:分页查询
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PageObject<User> queryUserByPage(Integer page, Integer rows) {
        log.info("业务层收到的参数:page:{},rows:{}", page, rows);
        PageObject<User> pageObject = new PageObject<>();//返回的分页对象
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);//分页对象
        List<User> users = dao.selectByRowBounds(new User(), rowBounds);
        log.info("分页查询User数组:{}", users);
        Integer count = dao.selectCount(new User());//总条数
        Integer pageTotal = pageObject.getTotalPage(count, rows);
        //总页数
        pageObject.setPage(page.toString()).setTotal(pageTotal).setRecords(count.toString()).setRows(users);
        return pageObject;
    }

    /**
     * @Description:修改用户状态
     */
    @LogAnnocation("修改用户状态")
    @Override
    public void modifyUserStatus(User user) {
        if (user.getStatus().equals("1")) user.setStatus("0");
        else user.setStatus("1");
        dao.updateByPrimaryKeySelective(user);
    }

    /**
     * @Description: 添加用户信息
     */
    @Override
    @LogAnnocation("添加用户")
    public void saveUser(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setRegTime(new Date());
        user.setHeadShow("http://yingxue-2005.oss-cn-beijing.aliyuncs.com/userHeadShow/2020-11-23/1606130419188-yx-icon.png");
        dao.insert(user);
        Map<String, Object> map = queryCountByMonth();
        GoEasyUtils.goEasy(map);
    }

    /**
     * @Description: 上传用户头像
     */
    @Override
    public void upHeadShow(MultipartFile headShow, String id) throws IOException {
        log.info("接受的文件名：{}", headShow.getOriginalFilename());
        if (!headShow.isEmpty() && headShow != null && headShow.getOriginalFilename() != "") {
            //新文件名
            String newName = new Date().getTime() + "-" + headShow.getOriginalFilename();
            //创建时间文件夹
            String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            //保存在服务器上的文件名
            String objectName = "userHeadShow" + "/" + format + "/" + newName;
            OSS oss = AliyunUtils.getOSS();
            oss.putObject("yingxue-2005", objectName, new ByteArrayInputStream(headShow.getBytes()));
            oss.shutdown();
            String realPath = request.getSession().getServletContext().getRealPath("/userHeadShow");
            File dateDir = new File(realPath + "/" + format);
            if (!dateDir.exists()) dateDir.mkdir();
            headShow.transferTo(new File(dateDir, newName));
            //头像网络路径
            String path = "https://yingxue-2005.oss-cn-beijing.aliyuncs.com/" + objectName;
            //修改用户头像路径
            User user = new User();
            user.setId(id);
            user.setHeadShow(path);
            log.info("修改后的id:{}", id);
            log.info("修改后的路径:{}", user.getHeadShow());
            dao.updateByPrimaryKeySelective(user);
        }
    }

    /**
     * @Description: 阿里云短信服务
     */
    @Override
    public SendSmsResponse aliyun(String phoneNumbers) throws ClientException {
        String signName = "小燕商城";
        String templateCode = "SMS_205610732";
        String templateParam = ImageCodeUtil.getSecurityCode();
        SendSmsResponse response = AliyunUtils.aliyunSMS(phoneNumbers, signName, templateCode, templateParam);
        return response;
    }

    /**
     * 导出用户信息
     */
    @Override
    public void exportExcel() throws IOException {
        List<User> users = dao.selectAll();
        for (User user : users) {
            String replace = user.getHeadShow().replace("https://yingxue-2005.oss-cn-beijing.aliyuncs.com/", "src/main/webapp/");
            user.setHeadShow(replace);
        }
        Workbook sheets = ExcelExportUtil.exportExcel(new ExportParams("应学App用户信息", "用户信息表"), User.class, users);
        sheets.write(new FileOutputStream(new File("E:/userList.xls")));
        sheets.close();
    }

    @Override
    public List<UserVO> queryCityBySex() {
        List<UserPO> boys = dao.selectCityBySex("男");
        List<UserPO> girls = dao.selectCityBySex("女");
        log.info("boys{}", boys);
        log.info("girls{}", girls);
        UserVO userVOBoys = new UserVO();
        userVOBoys.setName("男");
        userVOBoys.setUserPOS(boys);
        UserVO userVOGirls = new UserVO();
        userVOGirls.setName("女");
        userVOGirls.setUserPOS(girls);
        List<UserVO> userVOS = Arrays.asList(userVOBoys, userVOGirls);
        return userVOS;
    }

    @Override
    public Map<String, Object> queryCountByMonth() {
        //返回结果
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Integer> boysCount = new ArrayList<>();
        ArrayList<Integer> girlsCount = new ArrayList<>();
        ArrayList<Integer> months = MonthUtils.getMonths();
        for (Integer month : months) {
            boysCount.add(dao.selectCountByMonth("男", month));
        }
        for (Integer month : months) {
            girlsCount.add(dao.selectCountByMonth("女", month));
        }
        map.put("months", months);
        map.put("boysCount", boysCount);
        map.put("girlsCount", girlsCount);
        return map;
    }


}
