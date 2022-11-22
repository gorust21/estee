package com.gaia.util;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;

import java.io.*;
import java.util.Base64;

@Log4j2
public class FileUtil {

    public static Integer getBaseLength(String base64){
        Integer equalIndex = base64.indexOf("=");//2.找到等号，把等号也去掉
        if (base64.indexOf("=") > 0) {
            base64 = base64.substring(0, equalIndex);
        }
        Integer strLength = base64.length();//3.原来的字符流大小，单位为字节
        Integer size = strLength - (strLength / 8) * 2;//4.计算后得到的文件流大小，单位为字节
        return size;

    }

    public static String getPrintSize(Long size) {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            // 因为如果以MB为单位的话，要保留最后1位小数，
            // 因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
        }
    }

    //图片上传
    public static void upload(String base,String fileName, String pathPrefix) throws IOException {
            //数据库字段
            // 遍历数组
            //logger.info("base:   " + base);
            //fileName = UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
            Base64.Decoder decoder = Base64.getDecoder();
            FileOutputStream write = new FileOutputStream(new File(pathPrefix + fileName));
            byte[] decoderBytes = decoder.decode(base);
            write.write(decoderBytes);
            write.close();

    }

    //删除无效文件
    public static boolean delInvalidFile(String filePath,String fileName){
        try {
            File file = new File(filePath + fileName);
            if (!file.exists() || file.length() == 0) {
                FileUtil.delPicture(filePath, fileName);     //删除无效文件
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    //删除图片
    public static void delPicture(String path, String fileName) {
        File fileTemp = new File(path + fileName);
        // 判断文件是否存在
        boolean falg = false;
        falg = fileTemp.exists();
        if (falg) {
            log.info("temp文件存在");
            File file = new File(path + fileName);
            if (true == file.isFile()) {
                boolean flag = false;
                flag = file.delete();
                if (flag) {
                    log.info("成功删除无效图片文件:" + file.getName());
                }
            }
        } else {
            log.debug("执行失败");
        }
    }

    //压缩图片
    public static void compress(String newPath, String oldPath) throws IOException {
            if (oldPath.equals(""))
                oldPath = newPath;
            Thumbnails.of(newPath)
                    .scale(0.5f)
                    .outputQuality(0.5f)
                    .toFile(oldPath);

    }

    //将文件转换成Byte数组
    public static byte[] getBytesByFile(String pathStr) {
        File file = new File(pathStr);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}