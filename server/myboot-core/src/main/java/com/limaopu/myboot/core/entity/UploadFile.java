package com.limaopu.myboot.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.limaopu.myboot.core.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author mac
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_upload_files")
@TableName("sys_upload_files")
@ApiModel(value = "上传文件")
public class UploadFile extends BaseEntity {
    @ApiModelProperty(value = "文件名")
    private String name;

    @ApiModelProperty(value = "文件地址")
    private String url;

    @ApiModelProperty(value = "文件标记")
    private String tag;

    @ApiModelProperty(value = "文件编号")
    private String uuid;

    @ApiModelProperty(value = "类型:local=本地,qiniu=七牛,tencent-cos=腾讯云,aliyun-oss=阿里云")
    private String type;

    @ApiModelProperty(value = "上传用户ID")
    private Long userId;

    @ApiModelProperty(value = "文件MD5")
    private String md5;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;


}
