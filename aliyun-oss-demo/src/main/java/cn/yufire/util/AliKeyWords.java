package cn.yufire.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AliKeyWords {

    public static String endpoint = "";
    public static String accessKeyId = "";
    public static String accessKeySecret = "";
    public static String fileDir = "";
    public static String bucketName = "";
    public static String urlPerFix = "";

    /**
     * 地域节点
     */
    @Value("${aliyun.oss.ossEndPoint}")
    private String endpoint_yml;
    /**
     * accessKeyId
     */
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId_yml;
    /**
     * accessKeySecret
     */
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret_yml;
    /**
     * 文件存储目录
     */
    @Value("${aliyun.oss.filedir}")
    private String fileDir_yml;
    /**
     * bucket名称
     */
    @Value("${aliyun.oss.bucket_name}")
    private String bucketName_yml;
    /**
     * 外网访问http头  Bucket 域名
     */
    @Value("${aliyun.oss.http_path}")
    private String urlPerFix_yml;


    /**
     * 利用@PostConstruct将yml中配置的值赋给本地的变量
     */
    @PostConstruct
    public void initParameter() {
        endpoint = this.endpoint_yml;
        accessKeyId = this.accessKeyId_yml;
        accessKeySecret = this.accessKeySecret_yml;
        fileDir = this.fileDir_yml;
        bucketName = this.bucketName_yml;
        urlPerFix = this.urlPerFix_yml;
    }

    @Override
    public String toString() {
        return "AliKeyWords{" +
                "endpoint_yml='" + endpoint_yml + '\'' +
                ", accessKeyId_yml='" + accessKeyId_yml + '\'' +
                ", accessKeySecret_yml='" + accessKeySecret_yml + '\'' +
                ", fileDir_yml='" + fileDir_yml + '\'' +
                ", bucketName_yml='" + bucketName_yml + '\'' +
                ", urlPerFix_yml='" + urlPerFix_yml + '\'' +
                '}';
    }
}
