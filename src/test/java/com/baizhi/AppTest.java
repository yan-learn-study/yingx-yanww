package com.baizhi;

import com.baizhi.dao.CategoryMapper;
import com.baizhi.dao.VideoMapper;
import com.baizhi.entity.User;
import com.baizhi.po.CategoryPO;
import com.baizhi.po.VideoPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author yww
 * @Description
 * @Date 2020/11/25 14:55
 */
@SpringBootTest
public class AppTest {
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    void videoTest() {
        List<VideoPO> videoPOS = videoMapper.queryByReleaseTime();
        videoPOS.forEach(videoPO -> System.out.println(videoPO));
    }

    @Test
    void categoryMapperTest() {
        List<CategoryPO> categoryPOS = categoryMapper.selectAllCategory();
        categoryPOS.forEach(categoryPO -> System.out.println(categoryPO));
    }

    @Test
    void categoryMappersTest() {
        List<VideoPO> name = videoMapper.selectByLikeVideoName("火");
        name.forEach(categoryPO -> System.out.println(categoryPO));
    }

    @Test
    void categoryMapperssTest() {
        List<VideoPO> videoPOS = videoMapper.selectByVideoDetail("8d939c7b-c904-4d81-a205-5f7a1a0df836", "336312da-a79f-4d47-bce7-ac8cf613c676", "36");
        videoPOS.forEach(videoPO -> System.out.println(videoPO));
    }

    @Test
    void classTest() {
        Class<User> userClass = User.class;
        /*Annotation[] annotations = userClass.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }*/
        ClassLoader classLoader = userClass.getClassLoader();
        System.out.println(classLoader.toString());
    }
}
