package com.baizhi.util;

import com.alibaba.fastjson.JSON;
import io.goeasy.GoEasy;

/**
 * @author yww
 * @Description
 * @Date 2020/11/27 17:46
 */
public class GoEasyUtils {

    public static void goEasy(Object object) {
        GoEasy goEasy = new GoEasy("http://rest-hangzhou.goeasy.io", "BC-cf8d6c8f2ae84dcc93eaf76dc7a99702");
        goEasy.publish("test-channel", JSON.toJSONString(object));
    }
}
