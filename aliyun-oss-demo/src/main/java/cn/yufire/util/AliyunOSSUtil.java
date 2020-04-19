package cn.yufire.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * Description: 阿里云oss对象存储工具类
 * </p>
 *
 * @author yufire
 * @version v1.0.0
 * @see cn.yufire.util
 * @since 2020-04-18 16:29:46
 */
@Component
public class AliyunOSSUtil {

    private static final Logger logger = LoggerFactory.getLogger(AliyunOSSUtil.class);

    /** ====================================变量赋值 从yml读取配置 Begin==================================== */
    /** ====================================变量赋值 从yml读取配置 Begin==================================== */
    /**
     * 获取 OSS 对象
     *
     * @return
     */
    public static OSSClient getOssClient() {
        return new OSSClient(AliKeyWords.endpoint, AliKeyWords.accessKeyId, AliKeyWords.accessKeySecret);
    }

    /**
     * ====================================实现上传 Begin====================================
     */

    /**
     * 文件上传实现
     */
    public static Map<String, Object> uploadFile(MultipartFile[] multipartFile, String suffix) {
       HashMap<String, Object> result = new HashMap<>();
        int i = 0;
        for (MultipartFile file : multipartFile) {
            // 拼接出完整的文件名 格式： uuid + 时间戳 + 文件后缀
            // uuid + 时间戳 是为了保证文件名不重复
            String fileName = UUID.randomUUID().toString() + System.currentTimeMillis() + (suffix.contains(".") ? suffix : "." + suffix);
            uploadFile(getOssClient(), file, AliKeyWords.bucketName, AliKeyWords.fileDir == null ? "/" + fileName : AliKeyWords.fileDir + fileName);
            result.put("url",AliKeyWords.urlPerFix+AliKeyWords.fileDir + fileName);
            result.put("fileSize",file.getSize()/1024);
            result.put("uploadTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            result.put("fileNum",i++);
        }
        return result;
    }



    /** ====================================一系列的方法 Begin==================================== */

    /**
     * @param ossClient  oss客户端
     * @param url        URL
     * @param bucketName bucket名称
     * @param objectName 上传文件目录和（包括文件名）例如“test/index.html”
     * @return void        返回类型
     * @throws
     * @Title: uploadByNetworkStream
     * @Description: 通过网络流上传文件
     */
    public static void uploadByNetworkStream(OSSClient ossClient, URL url, String bucketName, String objectName) {
        try {
            InputStream inputStream = url.openStream();
            ossClient.putObject(bucketName, objectName, inputStream);
            ossClient.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * @param ossClient   oss客户端
     * @param inputStream 输入流
     * @param bucketName  bucket名称
     * @param objectName  上传文件目录和（包括文件名） 例如“test/a.jpg”
     * @return void        返回类型
     * @throws
     * @Title: uploadFile
     * @Description: 通过输入流上传文件
     */
    public static boolean uploadFile(OSSClient ossClient, InputStream inputStream, String bucketName,
                                     String objectName) {

        try {
            if (!existFileToThisMethod(ossClient, bucketName, objectName)) {
                ossClient.putObject(bucketName, objectName, inputStream);
            } else {
                logger.error("这个文件已经存在! " + objectName.split("\\.")[0]);
                logger.error("This file already exists! " + objectName.split("\\.")[0]);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }

        return true;
    }

    /**
     * @param ossClient
     * @param multipartFile
     * @param bucketName
     * @param objectName
     * @Title uploadFile
     * @Description 通过 MultipartFile 上传文件
     */
    public static boolean uploadFile(OSSClient ossClient, MultipartFile multipartFile, String bucketName,
                                     String objectName) {
        try {
            if (!existFileToThisMethod(ossClient, bucketName, objectName)) {
                InputStream inputStream = multipartFile.getInputStream();
                ossClient.putObject(bucketName, objectName, inputStream);
            } else {
                logger.error("这个文件已经存在! " + objectName.split("\\.")[0]);
                logger.error("This file already exists! " + objectName.split("\\.")[0]);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return true;
    }

    /**
     * @param ossClient  oss客户端
     * @param file       上传的文件
     * @param bucketName bucket名称
     * @param objectName 上传文件目录和（包括文件名） 例如“test/a.jpg”
     * @return void        返回类型
     * @throws
     * @Title: uploadFile
     * @Description: 通过file上传文件
     */
    public static boolean uploadFile(OSSClient ossClient, File file, String bucketName, String objectName) {

        try {
            if (!existFileToThisMethod(ossClient, bucketName, objectName)) {
                ossClient.putObject(bucketName, objectName, file);
            } else {
                logger.error("这个文件已经存在! " + objectName.split("\\.")[0]);
                logger.error("This file already exists ! " + objectName.split("\\.")[0]);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return true;
    }


    /**
     * @param ossClient  oss客户端
     * @param bucketName bucket名称
     * @param key        文件路径/名称，例如“test/a.txt”
     * @return void            返回类型
     * @throws
     * @Title: deleteFile
     * @Description: 根据key删除oss服务器上的文件
     */
    public static void deleteFile(OSSClient ossClient, String bucketName, String key) {
        ossClient.deleteObject(bucketName, key);
    }

    /**
     * @param ossClient  oss客户端
     * @param bucketName bucket名称
     * @param key        文件路径和名称
     * @return InputStream    文件输入流
     * @throws
     * @Title: getInputStreamByOSS
     * @Description:根据key获取服务器上的文件的输入流
     */
    public static InputStream getInputStreamByOSS(OSSClient ossClient, String bucketName, String key) {
        InputStream content = null;
        try {
            OSSObject ossObj = ossClient.getObject(bucketName, key);
            content = ossObj.getObjectContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * @param ossClient  oss客户端
     * @param bucketName bucket名称
     * @return List<String>  文件路径和大小集合
     * @throws
     * @Title: getBucketAllFiles
     * @Description: 查询某个bucket里面的所有文件
     */
    public static List<String> getBucketAllFiles(OSSClient ossClient, String bucketName) {
        List<String> results = new ArrayList<String>();
        try {
            // ossClient.listObjects返回ObjectListing实例，包含此次listObject请求的返回结果。
            ObjectListing objectListing = ossClient.listObjects(bucketName);
            // objectListing.getObjectSummaries获取所有文件的描述信息。
            for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                results.add(objectSummary.getKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * 本类所有方法使用 判断文件是否存在
     *
     * @param ossClient
     * @param bucketName
     * @param fileName
     * @return
     */
    public static boolean existFileToThisMethod(OSSClient ossClient, String bucketName, String fileName) {
        boolean existFlag = ossClient.doesObjectExist(bucketName, fileName);
        return existFlag;
    }

    /**
     * 判断文件是否存在
     *
     * @param ossClient
     * @param bucketName
     * @param fileName
     * @return
     */
    public static boolean existFile(OSSClient ossClient, String bucketName, String fileName) {
        boolean existFlag = ossClient.doesObjectExist(bucketName, fileName);
        ossClient.shutdown();
        return existFlag;
    }

    /**
     * 判断 bucket是否存在
     *
     * @param bucketName
     * @return
     */
    public static boolean existBucket(OSSClient ossClient, String bucketName) {
        boolean exist = ossClient.doesBucketExist(bucketName);
        ossClient.shutdown();
        return exist;
    }

    /** ====================================一系列的方法 End ==================================== */
}
