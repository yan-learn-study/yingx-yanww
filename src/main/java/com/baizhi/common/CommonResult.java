package com.baizhi.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author yww
 * @Description
 * @Date 2020/11/25 14:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommonResult {
    private String status = "100";
    private String message = "SUCCESS";
    private Object data;

    /**
     * SUCCESS
     */
    public static CommonResult success(String status, String message, Object data) {
        CommonResult result = new CommonResult();
        result.setStatus(status).setMessage(message).setData(data);
        return result;
    }

    public static CommonResult success(String message, Object data) {
        CommonResult result = new CommonResult();
        result.setMessage(message).setData(data);
        return result;
    }

    public static CommonResult success(Object data) {
        CommonResult result = new CommonResult();
        result.setData(data);
        return result;
    }


    /**
     * ERROR
     */
    public static CommonResult error(String status, String message, Object data) {
        CommonResult result = new CommonResult();
        result.setStatus(status).setMessage(message).setData(data);
        return result;
    }

    public static CommonResult error(String message, Object data) {
        CommonResult result = new CommonResult();
        result.setMessage(message).setData(data);
        return result;
    }

    public static CommonResult error(Object data) {
        CommonResult result = new CommonResult();
        result.setMessage("ERROR").setData(data);
        return result;
    }
}
