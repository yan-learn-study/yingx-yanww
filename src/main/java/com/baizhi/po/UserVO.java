package com.baizhi.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yww
 * @Description
 * @Date 2020/11/26 20:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    private String name;
    private List<UserPO> userPOS;
}
