package com.baizhi.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author yww
 * @Description
 * @Date 2020/12/05 14:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class VideoEs {
    private String id;
    private String title;
    private String intro;
    private String coverPath;
    private String videoPath;
    private Date uploadTime;
}
