package cn.yufire.tencent.cos.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TencentKeyWords {

    public static String region = "";
    public static String secretId = "";
    public static String secretKey = "";
    public static String fileDir = "";
    public static String bucketName = "";
    public static String url = "";


    /**
     * 地域节点
     */
    @Value("${tencent.oss.region}")
    private String region_yml;
    /**
     * accessKeyId
     */
    @Value("${tencent.oss.secretId}")
    private String secretId_yml;
    /**
     * accessKeySecret
     */
    @Value("${tencent.oss.secretKey}")
    private String secretKey_yml;
    /**
     * 文件存储目录
     */
    @Value("${tencent.oss.filedir}")
    private String fileDir_yml;
    /**
     * bucket名称
     */
    @Value("${tencent.oss.bucketName}")
    private String bucketName_yml;
    /**
     * 外网访问http头  Bucket 访问域名
     */
    @Value("${tencent.oss.url}")
    private String url_yml;


    /**
     * 利用@PostConstruct将yml中配置的值赋给本地的变量
     */
    @PostConstruct
    public void initParameter() {
        region = this.region_yml;
        secretId = this.secretId_yml;
        secretKey = this.secretKey_yml;
        fileDir = this.fileDir_yml;
        bucketName = this.bucketName_yml;
        url = this.url_yml;
    }

    @Override
    public String toString() {
        return "TencentKeyWords{" +
                "endpoint_yml='" + region + '\'' +
                ", secretId_yml='" + secretId_yml + '\'' +
                ", secretKey_yml='" + secretKey_yml + '\'' +
                ", fileDir_yml='" + fileDir_yml + '\'' +
                ", bucketName_yml='" + bucketName_yml + '\'' +
                ", url_yml='" + url_yml + '\'' +
                '}';
    }
}
