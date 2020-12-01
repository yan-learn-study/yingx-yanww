package com.baizhi.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author yww
 * @Description
 * @Date 2020/11/25 14:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class VideoPO {
    private String id;//视频id
    private String videoTitle;//视频标题
    private String cover;//视频封面
    private String path;//视频路径
    private Date uploadTime;//视频上传时间
    private String description;//视频描述
    private int likeCount;//视频点赞数
    private String cateName;//所属类别名
    private String userPhoto;//所属用户头像
    private String categoryId;//所属类别id
    private String userId;//所属用户id
    private String userName;//所属用户名字
    private boolean isAttention;//是否关注
    private List<VideoPO> videoList;//

}
