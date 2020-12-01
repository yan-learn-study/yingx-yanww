package com.baizhi.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectListing;
import com.baizhi.annocation.LogAnnocation;
import com.baizhi.dao.VideoMapper;
import com.baizhi.entity.Admin;
import com.baizhi.entity.Video;
import com.baizhi.po.VideoPO;
import com.baizhi.service.VideoService;
import com.baizhi.util.AliyunUtils;
import com.baizhi.util.PageObject;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yww
 * @Description
 * @Date 2020/11/23 19:36
 */
@Service
@Transactional
public class VideoServiceImpl implements VideoService {
    private static final Logger log = LoggerFactory.getLogger(VideoServiceImpl.class);
    @Autowired
    private VideoMapper dao;
    @Autowired
    private HttpServletRequest request;

    /**
     * @Description: 分页查询视频数据
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public PageObject<Video> queryVideoByPage(Integer page, Integer rows) {
        PageObject<Video> pageObject = new PageObject<>();
        List<Video> videos = dao.selectByRowBounds(new Video(), new RowBounds((page - 1) * rows, rows));
        Integer count = dao.selectCount(new Video());
        pageObject.setPage(page.toString())
                .setTotal(pageObject.getTotalPage(count, rows))
                .setRows(videos).setRecords(count.toString());
        return pageObject;
    }

    /**
     * @Description: 添加视频数据
     */
    @Override
    @LogAnnocation("添加视频信息")
    public Map<String, Object> saveVideo(Video video) {
        log.info("业务层添加视频收集的数据：{}", video);
        HashMap<String, Object> map = new HashMap<>();
        //补全数据
        video.setId(UUID.randomUUID().toString());
        video.setUploadTime(new Date());
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        video.setUserId(admin.getId());
        video.setVideoPath(null);
        try {
            dao.insertSelective(video);
            map.put("rows", video);
            map.put("status", 200);
            map.put("message", "添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 201);
            map.put("message", "添加失败！");
        }
        return map;
    }

    /**
     * @Description: 修改视频数据
     */
    @LogAnnocation("修改视频信息")
    @Override
    public Map<String, Object> modifyVideo(Video video) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            log.info("修改视频信息video：{}", video);
            video.setVideoPath(null);
            dao.updateByPrimaryKeySelective(video);
            map.put("rows", video);
            map.put("status", 200);
            map.put("message", "修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 201);
            map.put("message", "修改失败！");
        }
        return map;
    }

    /**
     * @Description: 删除视频数据
     */
    @Override
    @LogAnnocation("删除视频信息")
    public Map<String, Object> removeVideo(Video video) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            deleteFile(video);
            //从数据库中删除
            dao.deleteByPrimaryKey(video.getId());
            map.put("status", 200);
            map.put("message", "删除成功！");

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 201);
            map.put("message", "删除失败！");
        }
        return map;
    }

    /**
     * @Description: 删除文件
     */
    private void deleteFile(Video video) {
        Video selectOne = dao.selectOne(video);
        log.info("删除视频文件查出的video:{}", selectOne);
        String videoPath = selectOne.getVideoPath();
        //获取oss对象
        OSS oss = AliyunUtils.getOSS();
        //删除文件
        if (videoPath != null && !videoPath.contains("C:\\fakepath\\")) {
            String url = selectOne.getVideoPath().replace("https://yingxue-2005.oss-cn-beijing.aliyuncs.com/", "");
            log.info("objectName:{}", url);
            oss.deleteObject("yingxue-2005", url);
            String dateDir = url.split(String.valueOf(url.charAt(url.lastIndexOf("/"))))[1];
            ObjectListing listObjects = oss.listObjects("yingxue-2005", dateDir);
            //删除日期文件夹
            if (listObjects.getObjectSummaries().size() == 0) oss.deleteObject("yingxue-2005", dateDir);
        }
        oss.shutdown();
    }

    /**
     * @Description: 上传视频文件
     */
    @Override
    public void uploadVideo(MultipartFile videoPath, String id) throws IOException {
        if (!videoPath.isEmpty() && videoPath != null && videoPath.getOriginalFilename() != "") {
            //新文件名字
            String newName = new Date().getTime() + "-" + videoPath.getOriginalFilename();
            String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            //上传到服务器
            String objectName = "video/" + format + "/" + newName;
            log.info("视频ID：{}", id);
            log.info("服务器的文件路径：{}", objectName);
            OSS oss = AliyunUtils.getOSS();
            oss.putObject("yingxue-2005", objectName, new ByteArrayInputStream(videoPath.getBytes()));
            //删除原来的视频文件
            Video video = new Video();
            video.setId(id);
            deleteFile(video);
            //视频文件网络路径
            String path = "https://yingxue-2005.oss-cn-beijing.aliyuncs.com/" + objectName;
            String imgUrl = path + "?x-oss-process=video/snapshot,t_5000,f_jpg,w_0,h_0,m_fast,ar_auto";
            video.setVideoPath(path).setCoverPath(imgUrl);
            dao.updateByPrimaryKeySelective(video);
        }
    }

    @Override
    public List<VideoPO> queryByReleaseTime() {
        List<VideoPO> videoPOS = dao.queryByReleaseTime();
        return videoPOS;
    }

    @Override
    public List<VideoPO> queryByVideoDetail(String userId, String videoId, String cateId) {
        if (userId == null) userId = "d82f9c83-ff6e-45c4-b52a-efddd8b4fe8d";
        if (cateId == null) cateId = "36";
        return dao.selectByVideoDetail(userId, videoId, cateId);
    }

    @Override
    public List<VideoPO> queryByLikeVideoName(String content) {
        List<VideoPO> videoPOS = dao.selectByLikeVideoName(content);
        return videoPOS;
    }

}
