package com.limaopu.myboot.core.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.limaopu.myboot.core.common.exception.MyBootException;
import com.limaopu.myboot.core.entity.UploadFile;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

/**
 * 文件上传工具类
 */
@Slf4j
@Data
@Component
public class UploadUtil {

    /**
     * 按照当日创建文件夹
     */
    @Value("${myboot.upload.isDayType}")
    private boolean isDayType;
    /**
     * 自定义文件路径
     */
    @Value("${myboot.upload.uploadPath}")
    private String uploadPath;

    @Value("${myboot.upload.fileUrlPrefix}")
    private String fileUrlPrefix;

    @Value("${myboot.upload.maxFileSize}")
    private int maxFileSize = 10 * 1024 * 1024;

    @Value("${myboot.upload.allowFiles}")
    private String allowFiles = "bmp,jpg,png,gif,jpeg";

    public UploadUtil() {

    }


    /**
     * 文件上传
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public String uploadOne(MultipartFile multipartFile) throws IOException {
        // 参数检验
        if (multipartFile == null) {
            throw new MyBootException("文件不能为空");
        }
        // 文件限制10M
        long size = multipartFile.getSize();
        if (size > maxFileSize) {
            throw new MyBootException("length exceeds limit of 10M");
        }
        String folder = uploadPath + File.separator;
        if (!FileUtil.exist(folder)) {
            FileUtil.mkdir(folder);
        }
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmssSSS")) + multipartFile.getOriginalFilename();
        String path = folder + fileName;
        File file = new File(path);
        if (FileUtil.exist(file)) {
            throw new MyBootException("文件已存在");
        }
        File file1 = FileUtil.writeBytes(multipartFile.getBytes(), path);
        if (file1.length() < 0) {
            throw new MyBootException("文件上传失败");
        }
        return fileName;
    }


    public String getUploadPath() {
        return uploadPath;
    }

    public UploadFile upload(MultipartFile multipartFile) throws IOException {

        if (isNull(multipartFile)) {
            throw new MyBootException("上传数据/地址获取异常");
        }

        UploadFile uploadFile = new UploadFile();
        File file = fileNameStyle(multipartFile, uploadFile);

        return uploadFile;
    }

    /**
     * 格式化文件名 默认采用UUID
     *
     * @return
     */
    public File fileNameStyle(MultipartFile multipartFile, UploadFile uploadFile) throws IOException {

        String curr = multipartFile.getOriginalFilename();
        int suffixLen = curr.lastIndexOf(".");
        boolean flag = false;
        int index = -1;
        String suffix = curr.substring(suffixLen - 1); // 文件后缀
        String uuid = IdUtil.simpleUUID().toUpperCase();
        if ("blob".equals(curr)) {
            flag = true;
            index = 0;
            curr = uuid + suffix;
        } else if (suffixLen == -1) {
            throw new MyBootException("文件获取异常");
        }

        if (!flag) {
            suffix = curr.substring(suffixLen, curr.length());
            index = Arrays.binarySearch(allowFiles.split(","),
                    suffix.replace(".", ""));
            curr = uuid + suffix;
        }

        uploadFile.setTag(suffix.substring(1));
        uploadFile.setName(multipartFile.getOriginalFilename());

        String today= DateUtil.today().replace("-", File.separator );
        String dir = uploadPath + File.separator + today;

        String userDir = System.getProperty("user.dir");
        if (!FileUtil.isDirectory(userDir + dir)) {
            FileUtil.mkdir(userDir + dir);
        }

        //image 情况
        String path = dir + File.separator + curr;

        File file = new File(userDir + path);
        if (FileUtil.exist(file)) {
            throw new MyBootException("文件已存在");
        }

        log.info("========> 文件路径 " + userDir + path);

        File file1 = FileUtil.writeBytes(multipartFile.getBytes(), userDir + path);
        if (file1.length() < 0) {
            throw new MyBootException("文件上传失败");
        }

        Date date = DateUtil.date(System.currentTimeMillis());

        uploadFile.setFileSize(multipartFile.getSize());
        uploadFile.setMd5(SecureUtil.md5().digestHex(multipartFile.getInputStream()));
        uploadFile.setCreatedAt(date);
        uploadFile.setUuid(uuid);
        uploadFile.setUrl(path);

        return file;
    }

    public UploadFile getFileInfo(MultipartFile multipartFile) throws IOException {

        UploadFile uploadFile = new UploadFile();

        String originalFilename = multipartFile.getOriginalFilename();

        int suffixLen = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(suffixLen - 1);
        String uuid = IdUtil.simpleUUID().toUpperCase();
        Date date = DateUtil.date(System.currentTimeMillis());

        uploadFile.setUuid(uuid);
        uploadFile.setTag(suffix.substring(1));
        uploadFile.setName(multipartFile.getOriginalFilename());
        uploadFile.setFileSize(multipartFile.getSize());
        uploadFile.setMd5(SecureUtil.md5().digestHex(multipartFile.getInputStream()));
        uploadFile.setCreatedAt(date);
        uploadFile.setUuid(uuid);

        return uploadFile;
    }

    private boolean isNull(MultipartFile multipartFile) {
        if (null != multipartFile) {
            return false;
        }

        return true;
    }

}
