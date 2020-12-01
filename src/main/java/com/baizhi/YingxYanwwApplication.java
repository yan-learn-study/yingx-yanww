package com.baizhi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@tk.mybatis.spring.annotation.MapperScan("com.baizhi.dao")
@SpringBootApplication
public class YingxYanwwApplication {

    public static void main(String[] args) {
        SpringApplication.run(YingxYanwwApplication.class, args);
    }

}
