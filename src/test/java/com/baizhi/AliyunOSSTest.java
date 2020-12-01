package com.baizhi;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutObjectRequest;
import com.baizhi.dao.UserMapper;
import com.baizhi.entity.User;
import com.baizhi.util.AliyunUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author yww
 * @Description
 * @Date 2020/11/23 15:16
 */
@SpringBootTest
public class AliyunOSSTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void testOSS() {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        final String accessKeyId = "LTAI4G4qXW6c5bUGgFJX9wAR";//你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = "85TBIiPiYedJSO7sLRqI7f2qUS4MoW";
        String bucketName = "yingxue-2005";
        String objectName = "光年之外.mp4";
        String localFile = "D:\\素材\\音频素材\\MV\\G.E.M.邓紫棋 - 光年之外.mp4";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new File(localFile));

        // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        // 上传文件。
        ossClient.putObject(putObjectRequest);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Test
    void list() {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
        final String accessKeyId = "LTAI4G4qXW6c5bUGgFJX9wAR";//你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = "85TBIiPiYedJSO7sLRqI7f2qUS4MoW";
        String bucketName = "yingxue-2005";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 列举文件。如果不设置KeyPrefix，则列举存储空间下的所有文件。如果设置KeyPrefix，则列举包含指定前缀的文件。
        ObjectListing result = ossClient.listObjects(bucketName, "video/2020-11-24");
        List<OSSObjectSummary> ossObjectSummaries = result.getObjectSummaries();

        for (OSSObjectSummary obj : ossObjectSummaries) {
            System.out.println("\t" + obj.getKey());
        }

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Test
    void downLoad() throws IOException {

        OSS ossClient = AliyunUtils.getOSS();
        String path = "https://yingxue-2005.oss-cn-beijing.aliyuncs.com/userHeadShow/2020-11-26/1606391464691-02.gif";
        String replace = path.replace("https://yingxue-2005.oss-cn-beijing.aliyuncs.com/", "");
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        ossClient.getObject(new GetObjectRequest("yingxue-2005", replace), new File("E:\\" + replace));
        /*BufferedReader reader = new BufferedReader(new InputStreamReader(object.getObjectContent()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("E:\\"+replace)));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            writer.write(line);
        }
        reader.close();
        writer.close();*/
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Test
    void excel() throws Exception {
        List<User> users = userMapper.selectAll();
        for (User user : users) {
            String replace = user.getHeadShow().replace("https://yingxue-2005.oss-cn-beijing.aliyuncs.com/", "src/main/webapp/");
            user.setHeadShow(replace);
        }
        Workbook sheets = ExcelExportUtil.exportExcel(new ExportParams("应学用户信息", "用户"), User.class, users);
        sheets.write(new FileOutputStream(new File("E:/user.xls")));
        sheets.close();
    }
}
