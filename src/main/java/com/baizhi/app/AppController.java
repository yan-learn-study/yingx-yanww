package com.baizhi.app;

import com.aliyuncs.exceptions.ClientException;
import com.baizhi.common.CommonResult;
import com.baizhi.po.CategoryPO;
import com.baizhi.po.VideoPO;
import com.baizhi.service.CategoryService;
import com.baizhi.service.UserService;
import com.baizhi.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yww
 * @Description
 * @Date 2020/11/25 14:19
 */
@RestController
@RequestMapping("app")
public class AppController {
    @Autowired
    private UserService userService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("getPhoneCode")
    public CommonResult getPhoneCode(String phone) {
        try {
            userService.aliyun(phone);
            return CommonResult.success("验证码发送成功", phone);
        } catch (ClientException e) {
            return CommonResult.error("验证码发送失败", phone);
        }
    }

    @RequestMapping("queryByReleaseTime")
    public CommonResult queryByReleaseTime() {
        try {
            List<VideoPO> videoPOS = videoService.queryByReleaseTime();
            return CommonResult.success("查询成功", videoPOS);
        } catch (Exception e) {
            return CommonResult.error("查询失败");
        }

    }

    @RequestMapping("queryAllCategory")
    public CommonResult queryAllCategory() {
        try {
            List<CategoryPO> categories = categoryService.selectAllCategory();
            return CommonResult.success("查询成功", categories);
        } catch (Exception e) {
            return CommonResult.error("查询失败");
        }

    }

    @RequestMapping("queryByLikeVideoName")
    public CommonResult queryByLikeVideoName(String content) {
        try {
            List<VideoPO> videoPOS = videoService.queryByLikeVideoName(content);
            return CommonResult.success("查询成功", videoPOS);
        } catch (Exception e) {
            return CommonResult.error("查询失败");
        }
    }

    @RequestMapping("queryByVideoDetail")
    public CommonResult queryByVideoDetail(String userId, String videoId, String cateId) {
        try {
            List<VideoPO> videoPOS = videoService.queryByVideoDetail(userId, videoId, cateId);
            return CommonResult.success("查询成功", videoPOS);
        } catch (Exception e) {
            return CommonResult.error("查询失败");
        }
    }
}
