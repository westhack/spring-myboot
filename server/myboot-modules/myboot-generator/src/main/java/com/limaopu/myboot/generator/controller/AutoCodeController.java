package com.limaopu.myboot.generator.controller;

import cn.hutool.core.io.FileUtil;
import com.google.gson.Gson;
import com.limaopu.myboot.generator.dao.AutoCodeHistoryDao;
import com.limaopu.myboot.generator.entity.AutoCodeHistory;
import com.limaopu.myboot.generator.mapper.AutoCodeHistoryMapper;
import com.limaopu.myboot.core.common.utils.ResultUtil;
import com.limaopu.myboot.core.common.vo.Result;
import com.limaopu.myboot.generator.service.AutoCodeHistoryService;
import com.limaopu.myboot.generator.utils.GeneratorUtil;
import com.limaopu.myboot.generator.vo.AutoCodeResultVo;
import com.limaopu.myboot.generator.vo.AutoCodeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author mac
 */
@Slf4j
@RestController
@Api(tags = "代码生成")
@RequestMapping("/api/generator/autoCode")
public class AutoCodeController {
    @Autowired
    AutoCodeHistoryDao autoCodeHistoryDao;

    @Autowired
    AutoCodeHistoryMapper autoCodeHistoryMapper;

    @Autowired
    private AutoCodeHistoryService autoCodeHistoryService;

    @Value("${spring.datasource.name}")
    private String dbName;

    @RequestMapping(value = "/getModules", method = RequestMethod.GET)
    @ApiOperation(value = "获取模块列表")
    public Result<Object> getModules() {

        List<HashMap<String, String>> items = new ArrayList<>();

        String basePath = System.getProperty("user.dir") + File.separator + "myboot-modules";

        File[] ls = FileUtil.ls(basePath);

        Arrays.stream(ls).forEach(e -> {
            if (e.isDirectory()) {
                HashMap<String, String> n = new HashMap<String, String>();
                n.put("title", e.getName());
                n.put("name", e.getName());
                items.add(n);
            }
        });

        HashMap<String, Object> ret = new HashMap<String, Object>();
        ret.put("items", items);

        return ResultUtil.data(ret);
    }

    @RequestMapping(value = "/getDatabases", method = RequestMethod.GET)
    @ApiOperation(value = "获取数据库")
    public Result<Object> getDatabases() {

        List<HashMap<String, String>> databases = autoCodeHistoryMapper.getDatabases();

        databases.stream().forEach(db -> {
            db.put("label", db.get("database"));
            db.put("value", db.get("database"));
        });

        HashMap<String, Object> ret = new HashMap<String, Object>();
        ret.put("items", databases);
        ret.put("dbName", this.dbName);

        return ResultUtil.data(ret);
    }

    @RequestMapping(value = "/getTables", method = RequestMethod.GET)
    @ApiOperation(value = "获取数据库表")
    public Result<Object> getTables(@RequestParam String dbName) {
        List<HashMap<String, String>> tables = autoCodeHistoryMapper.getTables(dbName);

        tables.stream().forEach(db -> {
            db.put("description", db.get("tableComment"));
            db.put("label", db.get("tableName"));
            db.put("value", db.get("tableName"));
        });

        HashMap<String, Object> ret = new HashMap<String, Object>();
        ret.put("items", tables);

        return ResultUtil.data(ret);
    }

    @RequestMapping(value = "/getColumns", method = RequestMethod.GET)
    @ApiOperation(value = "获取数据表字段")
    public Result<Object> getColumns(@RequestParam(required = true) String dbName, @RequestParam(required = true) String tableName) {

        List<HashMap<String, String>> columns = autoCodeHistoryMapper.getColumns(dbName, tableName);

        HashMap<String, Object> ret = new HashMap<String, Object>();

        columns.stream().forEach(e -> {
            String dataType = e.get("dataType");
            if ("varchar".equals(dataType) || "char".equals(dataType) || "text".equals(dataType) || "longtext".equals(dataType)) {
                e.put("fieldType", "String");
            } else if ("int".equals(dataType) || "tinyint".equals(dataType)) {
                e.put("fieldType", "Integer");
            } else if ("bigint".equals(dataType)) {
                e.put("fieldType", "Long");
            } else if ("decimal".equals(dataType)) {
                e.put("fieldType", "BigDecimal");
            } else if ("datetime".equals(dataType)) {
                e.put("fieldType", "Date");
            } else {
                e.put("fieldType", "String");
            }
        });


        ret.put("items", columns);

        return ResultUtil.data(ret);
    }

    @RequestMapping(value = "/preview", method = RequestMethod.POST)
    @ApiOperation(value = "生成预览")
    public Result<Object> preview(@RequestBody AutoCodeVo autoCodeVo) throws IOException {

        GeneratorUtil generatorUtil = new GeneratorUtil();

        String[] strings = autoCodeVo.getModuleName().split("-");
        String MODULE = strings[1];

        String sysPath = System.getProperty("user.dir");
        File file = new File(sysPath);

        generatorUtil.TABLE_PRE = "";
        generatorUtil.CLASS_NAME = autoCodeVo.getClassName();
        generatorUtil.DESCRIPTION = autoCodeVo.getDescription();
        generatorUtil.MODULE = "/myboot-modules/" + autoCodeVo.getModuleName();
        generatorUtil.MODULE_PACKAGE = MODULE;
        generatorUtil.AUTHOR = "myBoot";
        generatorUtil.PRIMARY_KEY_TYPE = autoCodeVo.getPrimaryKeyType();

        generatorUtil.WEB_PATH = file.getParent() + "/backend-ui/src/";
        generatorUtil.SYS_PATH = sysPath + generatorUtil.MODULE + "/src/main/java/";

        generatorUtil.entity.setEntityFields(autoCodeVo.getFields());
        generatorUtil.entity.setRowKey(autoCodeVo.getRowKey());
        generatorUtil.entity.setModulePackage(generatorUtil.MODULE_PACKAGE);

        generatorUtil.ENTITY_PACKAGE = "com.limaopu.myboot." + generatorUtil.MODULE_PACKAGE + ".entity";
        generatorUtil.DAO_PACKAGE = "com.limaopu.myboot." + generatorUtil.MODULE_PACKAGE + ".dao";
        generatorUtil.MAPPER_PACKAGE = "com.limaopu.myboot." + generatorUtil.MODULE_PACKAGE + ".mapper";
        generatorUtil.SERVICE_PACKAGE = "com.limaopu.myboot." + generatorUtil.MODULE_PACKAGE + ".service";
        generatorUtil.SERVICE_IMPL_PACKAGE = "com.limaopu.myboot." + generatorUtil.MODULE_PACKAGE + ".serviceimpl";
        generatorUtil.CONTROLLER_PACKAGE = "com.limaopu.myboot." + generatorUtil.MODULE_PACKAGE + ".controller";

        AutoCodeResultVo autoCodeResultVo = generatorUtil.generator(true, false);

        HashMap<String, Object> map = new HashMap<>();
        map.put("autoCode", autoCodeResultVo.getAutoCode());

        return ResultUtil.data(map);
    }

    @RequestMapping(value = "/createTemp", method = RequestMethod.POST)
    @ApiOperation(value = "生成")
    public void createTemp(@RequestBody AutoCodeVo autoCodeVo, HttpServletResponse response) throws IOException {

        GeneratorUtil generatorUtil = new GeneratorUtil();

        String[] strings = autoCodeVo.getModuleName().split("-");
        String MODULE = strings[1];

        String sysPath = System.getProperty("user.dir");
        File file = new File(sysPath);

        generatorUtil.TABLE_PRE = "";
        generatorUtil.CLASS_NAME = autoCodeVo.getClassName();
        generatorUtil.DESCRIPTION = autoCodeVo.getDescription();
        generatorUtil.MODULE = "/myboot-modules/" + autoCodeVo.getModuleName();
        generatorUtil.MODULE_PACKAGE = MODULE;
        generatorUtil.AUTHOR = "myBoot";
        generatorUtil.PRIMARY_KEY_TYPE = autoCodeVo.getPrimaryKeyType();

        generatorUtil.WEB_PATH = file.getParent() + "/backend-ui/src/";
        generatorUtil.SYS_PATH = sysPath + generatorUtil.MODULE + "/src/main/java/";

        generatorUtil.entity.setEntityFields(autoCodeVo.getFields());
        generatorUtil.entity.setRowKey(autoCodeVo.getRowKey());
        generatorUtil.entity.setModulePackage(generatorUtil.MODULE_PACKAGE);

        generatorUtil.ENTITY_PACKAGE = "com.limaopu.myboot." + generatorUtil.MODULE_PACKAGE + ".entity";
        generatorUtil.DAO_PACKAGE = "com.limaopu.myboot." + generatorUtil.MODULE_PACKAGE + ".dao";
        generatorUtil.MAPPER_PACKAGE = "com.limaopu.myboot." + generatorUtil.MODULE_PACKAGE + ".mapper";
        generatorUtil.SERVICE_PACKAGE = "com.limaopu.myboot." + generatorUtil.MODULE_PACKAGE + ".service";
        generatorUtil.SERVICE_IMPL_PACKAGE = "com.limaopu.myboot." + generatorUtil.MODULE_PACKAGE + ".serviceimpl";
        generatorUtil.CONTROLLER_PACKAGE = "com.limaopu.myboot." + generatorUtil.MODULE_PACKAGE + ".controller";

        boolean moveFile = autoCodeVo.getAutoMoveFile();
        AutoCodeResultVo autoCodeResultVo = generatorUtil.generator(false, !moveFile);

        String autocodeFileName = System.getProperty("user.dir") + "/autocode.zip";

        if (!autoCodeVo.getAutoMoveFile()) {
            outFile(response, autocodeFileName);
        } else {
            AutoCodeHistory autoCodeHistory = new AutoCodeHistory();
            autoCodeHistory.setClassName(autoCodeVo.getClassName());
            autoCodeHistory.setDescription(autoCodeVo.getDescription());
            autoCodeHistory.setTableName(autoCodeVo.getTableName());
            autoCodeHistory.setFlag(0);

            String jsonStr = new Gson().toJson(autoCodeVo);
            autoCodeHistory.setRequestMeta(jsonStr);

            StringBuilder stringBuilder = new StringBuilder();
            autoCodeResultVo.getAutoCodePath().stream().forEach(e -> {
                stringBuilder.append(e + ";");
            });

            String autoCodeJson = new Gson().toJson(autoCodeResultVo.getAutoCode());
            autoCodeHistory.setAutoCode(autoCodeJson);

            autoCodeHistory.setAutoCodePath(stringBuilder.toString());

            autoCodeHistoryService.save(autoCodeHistory);
        }
        FileUtil.del(autocodeFileName);
    }

    private void outFile(HttpServletResponse response, String autocodeFile) {
        try {

            String fileName = new String("autocode.zip".getBytes(), "ISO-8859-1");
            File fil = new File(autocodeFile);

            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            response.setHeader("file-name", URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Content-Length", fil.length() + "");
            response.setHeader("Accept-Ranges", "bytes");
            // response.setHeader("ETag", "md5-" + fil.getMd5());

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
}
