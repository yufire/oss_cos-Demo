package cn.yufire.tencent.cos.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.exception.MultiObjectDeleteException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * Description:  腾讯云 cos操作工具类
 * </p>
 *
 * @author yufire
 * @version v1.0.0
 * @see cn.yufire.tencent.cos.util
 * @since 2020-04-19 14:10:31
 */
public class TencentOSSUtil {
    private static final Logger logger = LoggerFactory.getLogger(TencentOSSUtil.class);

    private static COSClient cosClient;

    static {
        // 初始化 cos客户端
        cosClient = getCosClint();
    }

    /**
     * 获取 cos客户端
     *
     * @return
     */
    public static COSClient getCosClint() {
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(TencentKeyWords.secretId, TencentKeyWords.secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        Region region = new Region(TencentKeyWords.region);
        // 加载初始化配置
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 返回 cos 客户端。
        return new COSClient(cred, clientConfig);
    }

    /**
     * 上传 MultipartFile 文件
     *
     * @param file       上传的文件
     * @param bucketName bucketName
     * @param key        文件名
     * @return
     * @throws Exception
     */
    public static boolean uploadFile(MultipartFile file, String bucketName, String key) throws IOException {
        try {
            File file1 = MultipartFileToFileConvert.multipartFileToFile(file);
            PutObjectResult putObjectResult = cosClient.putObject(bucketName, key, file1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            cosClient.shutdown();
        }

        return true;
    }

    /**
     * 上传 File 文件
     *
     * @param file       上传的文件
     * @param bucketName bucketName
     * @param key        文件名
     * @return
     * @throws Exception
     */
    public static boolean uploadFile(File file, String bucketName, String key) throws IOException {
        try {
            PutObjectResult putObjectResult = cosClient.putObject(bucketName, key, file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            cosClient.shutdown();
        }

        return true;
    }

    /**
     * 获取所有的 bucket 列表
     *
     * @return
     */
    public static List<Map<String, String>> getBucketList() {
        // 获取指定地域的所有bucket列表
        List<Bucket> buckets = cosClient.listBuckets();
        // 数据集合
        List<Map<String, String>> result = new ArrayList<>();
        for (Bucket bucketElement : buckets) {
            HashMap<String, String> resultMap = new HashMap<>();
            // bucket 名称
            String bucketName = bucketElement.getName();
            // bucket 地域
            String bucketLocation = bucketElement.getLocation();
            resultMap.put("bucketName", bucketName);
            resultMap.put("bucketLocation", bucketLocation);
            resultMap.put("bucketCreateDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bucketElement.getCreationDate()));
            result.add(resultMap);
        }
        cosClient.shutdown();
        return result;
    }

    /**
     * 新建bucket
     *
     * @param bucketName 命名规则 ：BucketName-AppId
     * @param permission bucket权限 CannedAccessControlList枚举类型
     * @return
     */
    public static boolean createBucket(String bucketName, CannedAccessControlList permission) {

        try {
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            // 设置 bucket 的权限为 Private(私有读写), 其他可选有公有读私有写, 公有读写
            createBucketRequest.setCannedAcl(permission);
            Bucket bucket = cosClient.createBucket(createBucketRequest);
            System.out.println(bucket);
        } catch (CosClientException e) {
            e.printStackTrace();
            return false;
        } finally {
            cosClient.shutdown();
        }
        return true;

    }

    /**
     * 删除 bucket
     *
     * @param bucketName
     * @return
     */
    public static boolean delBucket(String bucketName) {
        try {
            cosClient.deleteBucket(bucketName);
        } catch (CosClientException e) {
            e.printStackTrace();
            return false;
        } finally {
            cosClient.shutdown();
        }
        return true;
    }

    /**
     * 修改bucket权限
     *
     * @param bucketName
     * @return
     */
    public static boolean updateBucketPermission(String bucketName, CannedAccessControlList permission) {
        try {
            cosClient.setBucketAcl(bucketName, permission);
        } catch (CosClientException e) {
            e.printStackTrace();
            return false;
        } finally {
            cosClient.shutdown();
        }
        return true;
    }

    /**
     * 删除文件 和 批量删除文件
     *
     * @param bucketName bucketName
     * @param keys       要删除的文件名称数组
     * @return
     */
    public static boolean delObjects(String bucketName, String... keys) {
        if (keys.length == 1) {
            cosClient.deleteObject(bucketName, keys[0]);
        } else if (keys.length > 1) {
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
            // 设置要删除的key列表, 最多一次删除1000个
            ArrayList<DeleteObjectsRequest.KeyVersion> keyList = new ArrayList<DeleteObjectsRequest.KeyVersion>();
            for (String key : keys) {
                // 传入要删除的文件名
                keyList.add(new DeleteObjectsRequest.KeyVersion(key));
            }
            deleteObjectsRequest.setKeys(keyList);

            // 批量删除文件
            try {
                DeleteObjectsResult deleteObjectsResult = cosClient.deleteObjects(deleteObjectsRequest);
                List<DeleteObjectsResult.DeletedObject> deleteObjectResultArray = deleteObjectsResult.getDeletedObjects();
            } catch (MultiObjectDeleteException mde) { // 如果部分删除成功部分失败, 返回MultiObjectDeleteException
                List<DeleteObjectsResult.DeletedObject> deleteObjects = mde.getDeletedObjects();
                List<MultiObjectDeleteException.DeleteError> deleteErrors = mde.getErrors();
            } catch (CosServiceException e) { // 如果是其他错误，例如参数错误， 身份验证不过等会抛出 CosServiceException
                e.printStackTrace();
                throw e;
            } catch (CosClientException e) { // 如果是客户端错误，例如连接不上COS
                e.printStackTrace();
                throw e;
            }

        } else {
            logger.error("传入的keys有问题！");
            return false;
        }
        cosClient.shutdown();
        return true;
    }

    /**
     * 查询 bucket里的文件
     *
     * @param bucketName bucketName
     * @param perFix     文件夹名称 默认为空
     * @param isDeliter  文件分隔符 默认列出当前目录下的object  如果为true则显示所有的 objects
     * @return
     */
    public static List<Map<String, String>> getObjectList(String bucketName, String perFix, boolean isDeliter) {
        // 返回的数据集合
        List<Map<String, String>> results = new ArrayList<>();
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        // 设置bucket名称
        listObjectsRequest.setBucketName(bucketName);
        // prefix表示列出的object的key以prefix开始
        listObjectsRequest.setPrefix(perFix);
        // deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
        listObjectsRequest.setDelimiter(isDeliter ? "" : "/");
        // 设置最大遍历出多少个对象, 一次listobject最大支持1000
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing = null;
        do {
            try {
                objectListing = cosClient.listObjects(listObjectsRequest);
            } catch (CosServiceException e) {
                e.printStackTrace();
            } catch (CosClientException e) {
                e.printStackTrace();
            }
            // common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
            List<String> commonPrefixs = objectListing.getCommonPrefixes();

            // object summary表示所有列出的object列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                // 返回的数据集合
                HashMap<String, String> resultMap = new HashMap<>();
                // 文件的路径key
                String key = cosObjectSummary.getKey();
                // 文件的etag
                String etag = cosObjectSummary.getETag();
                // 文件的长度
                long fileSize = cosObjectSummary.getSize();
                // 文件的存储类型
                String storageClasses = cosObjectSummary.getStorageClass();
                Date lastModified = cosObjectSummary.getLastModified();
                resultMap.put("key", key);
                resultMap.put("fileSize", String.valueOf(fileSize));
                resultMap.put("etag", etag);
                resultMap.put("storageClasses", storageClasses);
                resultMap.put("lastModified", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastModified));
                results.add(resultMap);
            }

            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());
        cosClient.shutdown();
        return results;
    }


}
