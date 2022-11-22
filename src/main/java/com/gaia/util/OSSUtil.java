package com.gaia.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.gaia.base.Constants;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class OSSUtil {

    private static String bucketName = "debt-rents";
    private static String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    private static String accessKeyId = "LTAI4FuUmX8QKDUN8WEBeLAY";
    private static String accessKeySecret = "L2hX1AyTeQ4U2yytupcFeh9chXoqcZ";

    /**
     * byte上传
     *
     * @param //baseCode
     * @param objectName
     * @return
     */
    public String uploadByBaseCode(String baseCode, String objectName) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] content = decoder.decode(baseCode.replace("\r\n", ""));
        //log.debug("base64:" + baseCode.replace("\r\n", ""));
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 上传Byte数组。
        ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content));
        //http://itask.oss-cn-shanghai.aliyuncs.com/avatar/myFirst.jpg?x-oss-process=image
        // 关闭OSSClient。
        ossClient.shutdown();
        return Constants.OSS_PATH + objectName;
    }

    /**
     * 比例缩放（压缩）
     *
     * @param objectName
     */
    public String resize(String objectName) {
        try {
            String style = "?x-oss-process=image/resize,w_100";
            String fileUrl = Constants.OSS_PROCESS_PATH + objectName + style;
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            //新文件名
            String newFile = objectName.substring(0, objectName.length() - 4) + Constants.resize;
            // 上传网络流。
            InputStream inputStream = new URL(fileUrl).openStream();
            //生成文件
            ossClient.putObject(bucketName, newFile, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            return Constants.OSS_PATH + newFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
      /*  // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 缩放
        //String style = "image/resize,m_fixed,w_100,h_100";
        String style = "image/resize,w_100";
        GetObjectRequest request = new GetObjectRequest(bucketName, objectName);
        request.setProcess(style);
        log.debug("objectName:" + objectName);
        String newFile = objectName.substring(0, objectName.length() - 4) + Constants.resize;
        log.debug("objectName:" + newFile);
        ObjectMetadata object = ossClient.getObject(request, new File("example-resize.jpg"));
        // 关闭OSSClient。
        ossClient.shutdown();*/
    }

    /**
     * OSS删除
     *
     * @param objectName
     */
    public void delFile(String objectName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, objectName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 获取OSS文件大小（字节）
     *
     * @param url
     * @throws IOException
     */
    public int getFileLength(String url) {
        try {
            log.warn("图片地址：" + url);
            URLConnection openConnection = new URL(url).openConnection();
            int contentLength = openConnection.getContentLength();
            log.warn("文件大小：" + contentLength);
            return contentLength;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
     /*   try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
            OSSObject ossObject = ossClient.getObject(bucketName, objectName);
            InputStream objectContent = ossObject.getObjectContent();
            int i = objectContent.available();
            ossClient.shutdown();
            return i;
        } catch (OSSException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;*/
    }

    /**
     * 验证文件是否存在
     *
     * @param objectName
     * @return
     */
    public boolean isExist(String objectName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 判断文件是否存在。doesObjectExist还有一个参数isOnlyInOSS，如果为true则忽略302重定向或镜像；如果为false，则考虑302重定向或镜像。
        boolean found = ossClient.doesObjectExist(bucketName, objectName);
        log.debug("OSS文件是否存在-------------"+found);
        // 关闭OSSClient。
        ossClient.shutdown();
        return found;
    }

    public void setHeader(byte[] content) {
        //String content = "Hello OSS";

        // 创建上传文件的元信息，可以通过文件元信息设置HTTP header。
        ObjectMetadata meta = new ObjectMetadata();

        String md5 = BinaryUtil.toBase64String(BinaryUtil.calculateMd5(content));
        // 开启文件内容MD5校验。开启后OSS会把您提供的MD5与文件的MD5比较，不一致则抛出异常。
        meta.setContentMD5(md5);
        // 指定上传的内容类型。内容类型决定浏览器将以什么形式、什么编码读取文件。如果没有指定则根据文件的扩展名生成，如果没有扩展名则为默认值application/octet-stream。
        meta.setContentType("image/jpg");
        // 设置内容被下载时的名称。
        meta.setContentDisposition("attachment; filename=\"DownloadFilename\"");
        // 设置上传文件的长度。如超过此长度，则会被截断，为设置的长度。如不足，则为上传文件的实际长度。
        //meta.setContentLength(content.length());
        // 设置内容被下载时网页的缓存行为。
        meta.setCacheControl("Download Action");
        // 设置内容被下载时的编码格式。
        meta.setContentEncoding("utf-8");
        // 设置header。
        //meta.setHeader("Content-Type", "image/jpeg");
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件。
        PutObjectResult putObjectResult = ossClient.putObject(bucketName, "avatar/myThird.jpg", new ByteArrayInputStream(content), meta);

        // 关闭OSSClient。
        ossClient.shutdown();
    }


    /**
     * 流读取图片
     *
     * @param objectName
     * @param response
     * @throws IOException
     */
    public void readImage(String objectName, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(bucketName, objectName);
        // 读取文件内容。
        //BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
        BufferedInputStream bis = new BufferedInputStream(ossObject.getObjectContent());
        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
        byte[] b = new byte[1024];
        int read;
        while ((read = bis.read(b)) != -1) {
            bos.write(b, 0, read);
        }
        // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
        bis.close();
        bos.flush();
        bos.close();
        ossClient.shutdown();
    }


/*    public   void readImage(String objectName,HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(bucketName, objectName);
        // 读取文件内容。
        log.debug("Object content:");
        //BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
        InputStream in = ossObject.getObjectContent();
        OutputStream os = response.getOutputStream();
        byte[] b = new byte[1024];
        while (in.read(b) != -1) {
            os.write(b);
        }
        in.close();
        os.flush();
        os.close();
        // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
        //reader.close();
        // 关闭OSSClient。
        ossClient.shutdown();
    }*/

}
