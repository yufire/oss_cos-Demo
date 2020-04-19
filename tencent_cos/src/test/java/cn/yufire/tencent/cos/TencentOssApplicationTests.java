package cn.yufire.tencent.cos;

import cn.yufire.tencent.cos.service.UploadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class TencentOssApplicationTests {

    @Autowired
    private UploadService uploadService;

    @Test
    public void uploadLocalFile() {
        File file = new File("D:\\123.jpg");
        String upload = uploadService.upload(file);
        System.out.println(upload);
    }

}
