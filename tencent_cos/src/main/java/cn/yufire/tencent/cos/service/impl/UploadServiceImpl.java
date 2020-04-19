package cn.yufire.tencent.cos.service.impl;

import cn.yufire.tencent.cos.service.UploadService;
import cn.yufire.tencent.cos.util.TencentKeyWords;
import cn.yufire.tencent.cos.util.TencentOSSUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class UploadServiceImpl implements UploadService {
    @Override
    public String upload(MultipartFile multipartFile) {

        try {
            TencentOSSUtil.uploadFile(multipartFile, TencentKeyWords.bucketName, multipartFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 拼接访问路径  必须加点否则地址错误！
        return TencentKeyWords.bucketName + "." + TencentKeyWords.url + multipartFile.getName();
    }

    @Override
    public String upload(File file) {
        try {
            TencentOSSUtil.uploadFile(file, TencentKeyWords.bucketName, file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 拼接访问路径  必须加点否则地址错误！
        return TencentKeyWords.bucketName + "." + TencentKeyWords.url + file.getName();
    }
}
