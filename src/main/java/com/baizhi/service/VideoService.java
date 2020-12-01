package com.baizhi.service;

import com.baizhi.entity.Video;
import com.baizhi.po.VideoPO;
import com.baizhi.util.PageObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yww
 * @Description
 * @Date 2020/11/23 19:35
 */
public interface VideoService {
    PageObject<Video> queryVideoByPage(Integer page, Integer rows);

    Map<String, Object> saveVideo(Video video);

    Map<String, Object> modifyVideo(Video video);

    Map<String, Object> removeVideo(Video video);

    void uploadVideo(MultipartFile videoPath, String id) throws IOException;

    List<VideoPO> queryByReleaseTime();

    List<VideoPO> queryByVideoDetail(String userId, String videoId, String cateId);

    List<VideoPO> queryByLikeVideoName(String content);
}
