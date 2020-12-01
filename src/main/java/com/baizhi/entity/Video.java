package com.baizhi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "yx_video")
public class Video implements Serializable {
    @Id
    private String id;

    private String title;

    private String intro;

    @Column(name = "cover_path")
    private String coverPath;

    @Column(name = "video_path")
    private String videoPath;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "upload_time")
    private Date uploadTime;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "category_id")
    private String categoryId;

    private String groupId;

    @Transient
    private Integer likeCount;

    @Transient
    private Integer playCount;

    @Transient
    private User user;

    @Transient
    private Category category;

}