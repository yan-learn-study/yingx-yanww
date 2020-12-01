package com.baizhi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author yww
 * @Description
 * @Date 2020/11/27 17:36
 */
@SpringBootTest
public class GoEasyTest {

    @Test
    void testGoEasy() {
        int i = 2147483647;
        i = i + 3;
        System.out.println(i);
    }
}
