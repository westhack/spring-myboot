package com.limaopu.myboot.base.controller.storage;


import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.limaopu.myboot.core.base.BaseController;
import com.limaopu.myboot.core.base.BaseService;
import com.limaopu.myboot.core.common.constant.CommonConstant;
import com.limaopu.myboot.core.common.utils.*;
import com.limaopu.myboot.core.common.vo.PageResultVo;
import com.limaopu.myboot.core.common.vo.PageVo;
import com.limaopu.myboot.core.common.vo.Result;
import cn.hutool.core.util.StrUtil;
import com.limaopu.myboot.core.common.vo.SortOrderVo;
import com.limaopu.myboot.core.entity.Config;
import com.limaopu.myboot.core.entity.UploadFile;
import com.limaopu.myboot.core.entity.User;
import com.limaopu.myboot.core.service.UploadFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author mac
 */
@Slf4j
@RestController
@Api(tags = "文件上传接口")
@RequestMapping("/api/v1/storage/upload")
@Transactional
public class UploadController extends BaseController<UploadFile, Long> {

    @Value("${myboot.maxUploadFile}")
    private Integer maxUploadFile;

    @Value("${myboot.oss-type}")
    private String ossType = "local";

    @Autowired
    private QiniuUtil qiniuUtil;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private UploadUtil uploadUtil;

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    SecurityUtil securityUtil;

    @Override
    public BaseService<UploadFile, Long> getService() {
        return uploadFileService;
    }

    @RequestMapping(value = "/file", method = RequestMethod.POST)
    @ApiOperation(value = "文件上传")
    public Result<Object> upload(@RequestParam(required = false) MultipartFile file, @RequestParam(required = false) String base64) {

        if (file.getSize() > maxUploadFile * 1024 * 1024) {
            return ResultUtil.error("文件大小过大，不能超过" + maxUploadFile + "MB");
        }

        if (StrUtil.isNotBlank(base64)) {
            // base64上传
            file = Base64DecodeMultipartFile.base64Convert(base64);
        }

        if (ossType.equals(CommonConstant.UPLOAD_QINIU)) {
            return qiniu(file);
        } else {
            return loacl(file);
        }
    }

    private Result<Object> loacl(MultipartFile file) {
        User user = securityUtil.getUser();
        Long userId = 0L;
        if (user != null) {
            userId = user.getId();
        }

        String result;
        try {
            UploadFile uploadFile = uploadUtil.upload(file);
            if (StrUtil.isBlank(uploadUtil.getFileUrlPrefix())) {
                result = httpUtil.getDomain() + "/api/storage/upload/view/" + uploadFile.getUuid();
            } else {
                result = httpUtil.getDomain() + "/api/storage/upload/view/" + uploadFile.getUuid();
                // result = uploadUtil.getFileUrlPrefix() + "/" + uploadFile.getUrl().toString().replace("\\", "/");
            }
            uploadFile.setType(CommonConstant.UPLOAD_LOCAL);
            uploadFile.setUserId(userId);
            uploadFileService.save(uploadFile);

            uploadFile.setUrl(result);

            HashMap<String, Object> map = new HashMap(16);
            map.put("file", uploadFile);

            return ResultUtil.data(map);

        } catch (Exception e2) {
            log.error(e2.toString());

            return new ResultUtil<Object>().setErrorMsg(e2.toString());
        }
    }

    private Result<Object> qiniu(MultipartFile file) {
        User user = securityUtil.getUser();
        Long userId = 0L;
        if (user != null) {
            userId = user.getId();
        }

        String result;
        String fileName = CommonUtil.renamePic(file.getOriginalFilename());
        try {
            InputStream inputStream = file.getInputStream();
            // 上传七牛云服务器
            result = qiniuUtil.qiniuInputStreamUpload(inputStream, fileName);

            UploadFile uploadFile = uploadUtil.getFileInfo(file);
            uploadFile.setUrl(result);
            uploadFile.setType(CommonConstant.UPLOAD_QINIU);
            uploadFile.setUserId(userId);

            uploadFileService.save(uploadFile);

            HashMap<String, Object> map = new HashMap(16);
            map.put("file", uploadFile);

            return ResultUtil.data(map);
        } catch (Exception e) {
            log.error(e.toString());
            // 七牛失败 走本地
            return loacl(file);
        }
    }

    @RequestMapping(value = "/view/{id}")
    public void view(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {

        try {
            UploadFile fileMessage = uploadFileService.getByUiid(id);

            String fileName = new String(fileMessage.getName().getBytes(), "ISO-8859-1");
            File fil = getFile(fileMessage);

            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            response.setHeader("file-name", URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Content-Length", fil.length() + "");
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("ETag", "md5-" + fileMessage.getMd5());

            InputStream inputStream = new FileInputStream(fil);

            OutputStream os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            // 这里主要关闭。
            os.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            log.error("", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("", e);
            e.printStackTrace();
        }

    }

    private File getFile(UploadFile fileinfo) {
        String userDir = System.getProperty("user.dir");
        File file = new File(userDir, fileinfo.getUrl());
        if (!file.exists()) {
            File src = new File(fileinfo.getUrl());
            if (src.exists()) {
                FileUtil.copyFile(src, file);
            }
        }
        return file;
    }

    @RequestMapping(value = "/getUserFileList", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "用户上传文件列表")
    public Result<Object> getUserFileList(@RequestBody(required = false) PageVo page) {

        User currUser = securityUtil.getCurrUser();

        QueryWrapper<UploadFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", currUser.getId());

        SortOrderVo sortOrder = this.sortOrder;
        if (sortOrder == null) {
            sortOrder = this.getSortOrder();
        }

        if (sortOrder != null) {
            if (sortOrder.getOrder().equals("asc")) {
                queryWrapper.orderByAsc(sortOrder.getColumn());
            } else {
                queryWrapper.orderByDesc(sortOrder.getColumn());
            }
        }

        getService().getQueryWrapper(queryWrapper, page, true, null);

        PageResultVo<UploadFile> search = getService().search(page, queryWrapper);

        return ResultUtil.data(search);
    }
}