package cn.yufire.tencent.cos.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * <p>
 * Description: 文件上传 业务接口
 * </p>
 *
 * @author yufire
 * @version v1.0.0
 * @see cn.yufire.tencent.cos.service
 * @since 2020-04-19 15:51:11
 */
public interface UploadService {

    String upload(MultipartFile multipartFile);
    String upload(File file);

}
