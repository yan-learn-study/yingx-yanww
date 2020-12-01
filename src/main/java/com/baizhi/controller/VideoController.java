package com.baizhi.controller;

import com.baizhi.entity.Video;
import com.baizhi.service.VideoService;
import com.baizhi.util.PageObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yww
 * @Description
 * @Date 2020/11/23 19:42
 */
@Controller
@RequestMapping("video")
public class VideoController {
    private static final Logger log = LoggerFactory.getLogger(VideoController.class);
    @Autowired
    private VideoService service;

    @RequestMapping("queryVideoByPage")
    @ResponseBody
    public PageObject<Video> queryVideoByPage(Integer page, Integer rows) {
        return service.queryVideoByPage(page, rows);
    }

    @RequestMapping("editVideo")
    @ResponseBody
    public Map<String, Object> editVideo(String oper, Video video) {
        Map<String, Object> map = null;
        if (oper.equals("add")) map = service.saveVideo(video);
        else if (oper.equals("edit")) map = service.modifyVideo(video);
        else if (oper.equals("del")) map = service.removeVideo(video);
        return map;
    }

    @RequestMapping("uploadVideo")
    @ResponseBody
    public Map<String, Object> uploadVideo(MultipartFile videoPath, String id) {
        log.info("上传视频的名字：{},收集的id,{}", videoPath.getOriginalFilename(), id);
        HashMap<String, Object> map = new HashMap<>();
        try {
            service.uploadVideo(videoPath, id);
            map.put("status", 200);
            map.put("message", "上传成功!");
        } catch (IOException e) {
            e.printStackTrace();
            map.put("status", 201);
            map.put("message", "上传失败!");
        }
        return map;
    }
}
