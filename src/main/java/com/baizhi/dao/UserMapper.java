package com.baizhi.dao;


import com.baizhi.entity.User;
import com.baizhi.po.UserPO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface UserMapper extends Mapper<User> {
    List<UserPO> selectCityBySex(String sex);

    Integer selectCountByMonth(@Param("sex") String sex, @Param("month") Integer month);

}