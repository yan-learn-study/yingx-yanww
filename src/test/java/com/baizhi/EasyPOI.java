package com.baizhi;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baizhi.dao.UserMapper;
import com.baizhi.entity.User;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author yww
 * @Description
 * @Date 2020/11/26 18:17
 */
@SpringBootTest
public class EasyPOI {
    @Autowired
    private UserMapper userMapper;

    @Test
    void userMapperTest() throws Exception {
        List<User> users = userMapper.selectAll();
        users.forEach(user -> user.setHeadShow("src/main/webapp/bootstrap/img/01.jpg"));
        Workbook sheets = ExcelExportUtil.exportExcel(new ExportParams("应学用户信息", "用户"), User.class, users);
        sheets.write(new FileOutputStream(new File("E:/user.xls")));
        sheets.close();
    }
}
