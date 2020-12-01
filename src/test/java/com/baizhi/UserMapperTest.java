package com.baizhi;

import com.baizhi.dao.AdminMapper;
import com.baizhi.dao.UserMapper;
import com.baizhi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserMapperTest {

    @Autowired
    UserMapper dao;
    @Autowired
    AdminMapper adminMapper;

    @Test
    void contextLoads() {
        System.out.println(adminMapper.selectByUsername("admin"));
    }

    @Test
    void test() {
        User user = new User();
        user.setId("0296f8d5-b3c4-47a3-9c25-92d2be61fe86");
        user.setStatus("5");
        dao.updateByPrimaryKeySelective(user);
    }

    @Test
    void testInsert() {
        Integer integer = dao.selectCountByMonth("男", 1);
        System.out.println(integer);
    }

    @Test
    void testReg() {
    }
}
