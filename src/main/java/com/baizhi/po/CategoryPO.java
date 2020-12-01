package com.baizhi.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author yww
 * @Description
 * @Date 2020/11/25 15:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CategoryPO {
    private String id;
    private String cateName;
    private String levels;
    private String parentId;
    private List<CategoryPO> categoryList;
}
