package cn.yufire.controller;

import cn.yufire.util.AliyunOSSUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
/**
 *
 * <p>
 * Description: 文件上传控制器
 * </p>
 *
 * @author yufire
 * @version v1.0.0
 * @since 2020-04-18 17:54:03
 * @see cn.yufire.controller
 *
 */
@Controller
public class OSSController {


    @GetMapping("/index.html")
    public String index(){
        return "index";
    }

    /**
     * 上传接口
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public Map<String,Object> uploadFile(MultipartFile[] file){
        Map<String, Object> result = AliyunOSSUtil.uploadFile(file, "png");
        return result;
    }

}
