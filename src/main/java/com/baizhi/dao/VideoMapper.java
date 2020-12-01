package com.baizhi.dao;


import com.baizhi.entity.Video;
import com.baizhi.po.VideoPO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface VideoMapper extends Mapper<Video> {
    List<VideoPO> queryByReleaseTime();

    List<VideoPO> selectByVideoDetail(@Param("userId") String userId, @Param("videoId") String videoId, @Param("cateId") String cateId);

    List<VideoPO> selectByLikeVideoName(@Param("content") String content);

}