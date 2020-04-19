package cn.yufire;

import cn.yufire.util.AliKeyWords;
import cn.yufire.util.AliyunOSSUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class AliyunOssApplicationTests {

    @Autowired
    private AliKeyWords aliKeyWords;

    @Test
    void contextLoads() {

        boolean yufire1 = AliyunOSSUtil.uploadFile(AliyunOSSUtil.getOssClient(), new File("C:\\Users\\28678\\Desktop\\图片\\IMG_20190509_121358.jpg.jpg"), "yufire", "yufire.png");

        boolean yufire = AliyunOSSUtil.existFile(AliyunOSSUtil.getOssClient(), "yufire", "yufire.png");
        System.out.println(yufire);

    }

}
