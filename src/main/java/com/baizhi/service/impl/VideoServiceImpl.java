package com.baizhi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectListing;
import com.baizhi.annocation.LogAnnocation;
import com.baizhi.dao.VideoMapper;
import com.baizhi.entity.Admin;
import com.baizhi.entity.Video;
import com.baizhi.po.VideoEs;
import com.baizhi.po.VideoPO;
import com.baizhi.service.VideoService;
import com.baizhi.util.AliyunUtils;
import com.baizhi.util.PageObject;
import org.apache.ibatis.session.RowBounds;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
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
import java.text.ParseException;
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
    @Autowired
    private RestHighLevelClient client;

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
            //添加索引
            VideoEs videoEs = new VideoEs();
            videoEs.setId(video.getId()).setIntro(video.getIntro()).setTitle(video.getTitle()).setUploadTime(video.getUploadTime());
            IndexRequest indexRequest = new IndexRequest("videos", "video", video.getId());
            String viodeJSON = JSONObject.toJSONStringWithDateFormat(videoEs, "yyyy-MM-dd");
            indexRequest.source(viodeJSON, XContentType.JSON);
            IndexResponse index = client.index(indexRequest, RequestOptions.DEFAULT);
            log.info("添加索引状态：{}" + index.status());
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
            //数据库修改
            video.setVideoPath(null);
            dao.updateByPrimaryKeySelective(video);
            //索引修改
            UpdateRequest updateRequest = new UpdateRequest("videos", "video", video.getId());
            String videoJson = JSONObject.toJSONStringWithDateFormat(video, "yyyy-MM-dd");
            updateRequest.doc(videoJson, XContentType.JSON);
            UpdateResponse update = client.update(updateRequest);
            log.info("修改索引基本信息{}", update.status());
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
            //删除索引
            DeleteRequest deleteRequest = new DeleteRequest("videos", "video", video.getId());
            DeleteResponse delete = client.delete(deleteRequest, RequestOptions.DEFAULT);
            log.info("删除索引状态：{}" + delete.status());
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
            //修改索引
            UpdateRequest request = new UpdateRequest("videos", "video", id);
            String jsonVIdeoEs = JSONObject.toJSONStringWithDateFormat(video, "yyyy-MM-dd");
            request.doc(jsonVIdeoEs, XContentType.JSON);
            UpdateResponse update = client.update(request, RequestOptions.DEFAULT);
            log.info("修改索引路径信息状态：{}", update.status());
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

    @Override
    public List<VideoEs> searchVideo(String content) throws IOException, ParseException {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        //fuzzy查询
        FuzzyQueryBuilder queryBuilder = QueryBuilders.fuzzyQuery("title", content);
        //分词查询
        QueryStringQueryBuilder stringQueryBuilder = QueryBuilders.queryStringQuery(content);
        stringQueryBuilder.field("title").field("intro");
        //前缀查询
        PrefixQueryBuilder prefixQuery = QueryBuilders.prefixQuery("title", content);

        query.should(queryBuilder)
                .should(stringQueryBuilder)
                .should(prefixQuery)
                .should(QueryBuilders.wildcardQuery("title", "*" + content + "*"))//通配符查询
                .should(QueryBuilders.termsQuery("title", content))//关键字查询
                .should(QueryBuilders.multiMatchQuery(content, "title", "intro"));//多字段查询
        //高亮操作
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title").field("intro")
                .requireFieldMatch(false)
                .preTags("<font color='red'>")
                .postTags("</font>");

        sourceBuilder.query(query)
                .highlighter(highlightBuilder);
        searchRequest.indices("videos").types("video").source(sourceBuilder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);

        //返回结果
        List<VideoEs> videos = new ArrayList<>();
        SearchHits hits = search.getHits();
        log.info("查询总条数：{}", hits.getTotalHits());
        log.info("查询总条数中分数最高的成绩：{}", hits.getMaxScore());
        for (SearchHit hit : hits.getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            Map<String, Object> source = hit.getSourceAsMap();
            VideoEs videoEs = new VideoEs();
            videoEs.setId(hit.getId());
            videoEs.setTitle(source.get("title").toString());
            videoEs.setIntro(source.get("intro").toString());
            videoEs.setCoverPath(source.get("coverPath").toString());
            videoEs.setVideoPath(source.get("videoPath").toString());
            String data = source.get("uploadTime").toString();
            Date parse = new SimpleDateFormat("yyyy-MM-dd").parse(data);
            videoEs.setUploadTime(parse);
            if (highlightFields != null) {
                if (highlightFields.containsKey("title"))
                    videoEs.setTitle(highlightFields.get("title").fragments()[0].toString());
                if (highlightFields.containsKey("intro"))
                    videoEs.setIntro(highlightFields.get("intro").fragments()[0].toString());
            }
            videos.add(videoEs);
        }
        log.info("查询状态：{}", search.status());
        return videos;
    }
}
