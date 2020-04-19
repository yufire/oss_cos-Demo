# OSS-COS 的操作demo

## 注意事项

使用该demo之前：

- 注册您的  腾讯云 或者 阿里云 账号
- 开通 oss(阿里云) 或 cos(腾讯云)  对象存储服务
- 获取  SecretId  和  SecretKey  
- 创建 bucket 权限为公共读
- 把获取到的 参数填入 项目的 application.yml文件中



## 概述

使用阿里云oss 腾讯云 cos -商业化组件实现 `上传` ,`下载`,`删除`,`查看` 的demo



## 操作流程图



![阿里云OSS操作流程图](https://yufire.oss-cn-shanghai.aliyuncs.com/image/2a65eb26-3d44-4e1d-b94b-5e5710aa3dba1587212154718.png)

## 需要环境

- 开发工具：Intellij IDEA 、MyEclipse 等...
- Java SDK：jdk1.8
- 所需框架：SpringBoot 2.2.6 RELEASE

## 项目所需依赖

```xml
<dependency>
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-sdk-oss</artifactId>
    <version>2.8.2</version>
</dependency>
<dependency>
    <groupId>com.qcloud</groupId>
    <artifactId>cos_api</artifactId>
    <version>5.6.19</version>
</dependency>
```

# 相关链接

[项目访问路径](http://localhost:8090)

[阿里云注册链接]( https://account.aliyun.com/register/qr_register.htm?spm=5176.12825654.amxosvpfn.22.e9392c4aasLmf3&oauth_callback=https%3A%2F%2Fwww.aliyun.com%2F&aly_as=JW-ZXuST6 )

[腾讯云注册链接]( https://cloud.tencent.com/register )

# 腾讯云 cos 对象存储  新用户免费 40G空间

[地址]( https://console.cloud.tencent.com/ )

![1587284269210](https://yufire.oss-cn-shanghai.aliyuncs.com/image/d68fc395-121d-400f-9658-9ec92fe06d5f1587284498535.png)

# 阿里云 对象存储 很便宜

[开通地址]( https://www.aliyun.com/product/oss?spm=5176.10695662.1112155.1.2f2d36b9Q8eXoM )

![1587284421176](https://yufire.oss-cn-shanghai.aliyuncs.com/image/cf2b389d-f658-4b50-8994-2ee73369d5411587284515250.png)

# demo主页效果

![主页效果](https://yufire.oss-cn-shanghai.aliyuncs.com/image/036cd9f9-514c-4ca0-80ae-81ef4c5af2601587284541946.png)