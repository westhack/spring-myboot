package com.limaopu.myboot.system.controller.system;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mac
 */
@Slf4j
@RestController
@Api(tags = "系统配置管理接口")
@RequestMapping("/api/v1/system")
@Transactional
public class SystemController {

}
