package com.baizhi.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author yww
 * @Description
 * @Date 2020/11/12 19:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PageObject<T> implements Serializable {
    private String page;//当前页
    private Integer total;//总页数
    private String records;//总条数
    private List<T> rows;//内容

    public Integer getTotalPage(Integer count, Integer rows) {
        return (count % rows == 0) ? count / rows : count / rows + 1;
    }
}
