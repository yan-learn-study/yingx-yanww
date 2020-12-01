package com.baizhi.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "yx_user")
public class User implements Serializable {
    @Id
    @Excel(name = "用户Id")
    private String id;

    @Excel(name = "用户名")
    private String username;

    @Excel(name = "用户手机号码")
    private String mobile;

    @Excel(name = "用户签名")
    private String sign;

    @Excel(name = "用户头像", type = 2)
    @Column(name = "head_show")
    private String headShow;

    @Excel(name = "用户状态")
    private String status;

    @Excel(name = "用户注册时间", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "reg_time")
    private Date regTime;

    @Column(name = "sex")
    private String sex;

    @Column(name = "city")
    private String city;


}