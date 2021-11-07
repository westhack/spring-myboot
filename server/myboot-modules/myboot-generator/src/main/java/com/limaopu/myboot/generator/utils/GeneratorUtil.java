package com.limaopu.myboot.generator.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.db.Db;
import cn.hutool.db.ds.simple.SimpleDataSource;
import com.baomidou.mybatisplus.annotation.TableField;
import com.limaopu.myboot.core.common.exception.MyBootException;
import com.limaopu.myboot.generator.bean.Entity;
import com.limaopu.myboot.generator.bean.Item;
import com.limaopu.myboot.generator.vo.AutoCodeResultVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 代码生成器 Mybatis-Plus
 */
@Slf4j
public class GeneratorUtil {

    /**
     * 实体类名
     * 建议仅需修改
     */
    public String CLASS_NAME = "Apis";

    /**
     * 类说明描述
     * 建议仅需修改
     */
    public String DESCRIPTION = "测试";

    /**
     * 作者名
     * 建议仅需修改
     */
    public String AUTHOR = "myBoot";

    /**
     * 是否生成树形结构相关接口
     * 建议仅需修改
     */
    public Boolean IS_TREE = false;

    /**
     * 数据库表名前缀
     * 下方请根据需要修改
     */
    public String TABLE_PRE = "sys_";

    /**
     * 主键类型
     */
    public String PRIMARY_KEY_TYPE = "String";

    /**
     * 生成模块路径
     * (文件自动生成至该模块下)
     */
    public String MODULE = "/myboot-modules/myboot-system";

    /**
     * 模块包路径
     * （下方包路径拼接使用）
     */
    public String MODULE_PACKAGE; // "base"

    /**
     * 实体类对应包
     * (文件自动生成至该包下)
     */
    public String ENTITY_PACKAGE = "com.limaopu.myboot." + MODULE_PACKAGE + ".entity";

    /**
     * dao对应包
     * (文件自动生成至该包下)
     */
    public String DAO_PACKAGE = "com.limaopu.myboot." + MODULE_PACKAGE + ".dao";

    /**
     * mapper对应包 【注意修改后需到com.limaopu.myboot.core.config.mybatisplus.MybatisPlusConfig配置你的mapper路径扫描】
     * (文件自动生成至该包下)
     */
    public String MAPPER_PACKAGE = "com.limaopu.myboot." + MODULE_PACKAGE + ".mapper";

    /**
     * service对应包
     * (文件自动生成至该包下)
     */
    public String SERVICE_PACKAGE = "com.limaopu.myboot." + MODULE_PACKAGE + ".service";

    /**
     * serviceImpl对应包
     * (文件自动生成至该包下)
     */
    public String SERVICE_IMPL_PACKAGE = "com.limaopu.myboot." + MODULE_PACKAGE + ".serviceimpl";

    /**
     * controller对应包
     * (文件自动生成至该包下)
     */
    public String CONTROLLER_PACKAGE = "com.limaopu.myboot." + MODULE_PACKAGE + ".controller";

    /**
     * 路径前缀
     */
    public String SYS_PATH = System.getProperty("user.dir") + MODULE + "/src/main/java/";
    public String WEB_PATH = "";

    public Entity entity = new Entity();

    /**
     * 运行该主函数即可生成代码
     *
     * @throws IOException
     */
    public AutoCodeResultVo generator(boolean preview, boolean zip) throws IOException {

        // 模板路径
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("/btl/");
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);

        // 生成代码
        AutoCodeResultVo res = generateCode(gt, preview, zip);

        // 读取你的实体类中的字段，补充生成条件构造分页查询代码【需自行复制控制台打印输出的代码自行覆盖】
        // generatePlus(gt);

        // 根据类名删除生成的代码
        // deleteCode(className);

        return res;
    }

    /**
     * 生成代码
     *
     * @param gt
     * @param preview
     * @throws IOException
     */
    private AutoCodeResultVo generateCode(GroupTemplate gt, boolean preview, boolean zip) throws IOException {
        AutoCodeResultVo autoCodeResultVo = new AutoCodeResultVo();
        LinkedHashMap<String, String> autoCodeRes = new LinkedHashMap<>();

        Template entityTemplate = gt.getTemplate("entity.btl");
        Template mapperTemplate = gt.getTemplate("mapper.btl");
        Template daoTemplate = gt.getTemplate("dao.btl");
        Template serviceTemplate = gt.getTemplate("service.btl");
        Template serviceImplTemplate = gt.getTemplate("serviceImpl.btl");
        Template controllerTemplate = gt.getTemplate("controller.btl");
        Template mapperXmlTemplate = gt.getTemplate("mapperXml.btl");
        Template webRouterTemplate = gt.getTemplate("web/router.btl");
        Template webApiTemplate = gt.getTemplate("web/api.btl");
        Template webTableTemplate = gt.getTemplate("web/table.btl");
        Template webFormTemplate = gt.getTemplate("web/form.btl");
        if (Boolean.TRUE.equals(IS_TREE)) {
            controllerTemplate = gt.getTemplate("treeMpController.btl");
        }

        entity.setEntityPackage(ENTITY_PACKAGE);
        entity.setDaoPackage(DAO_PACKAGE);
        entity.setMapperPackage(MAPPER_PACKAGE);
        entity.setServicePackage(SERVICE_PACKAGE);
        entity.setServiceImplPackage(SERVICE_IMPL_PACKAGE);
        entity.setControllerPackage(CONTROLLER_PACKAGE);
        entity.setAuthor(AUTHOR);
        entity.setClassName(name(CLASS_NAME, true));
        entity.setTableName(TABLE_PRE + camel2Underline(CLASS_NAME));
        entity.setClassNameLowerCase(name(CLASS_NAME, false));
        entity.setDescription(DESCRIPTION);
        entity.setPrimaryKeyType(PRIMARY_KEY_TYPE);
        entity.setIsTree(IS_TREE);

        OutputStream out = null;

        // 生成实体类代码
        final String varEntity = "entity";
        entityTemplate.binding(varEntity, entity);
        String entityResult = entityTemplate.render();
//        log.info(entityResult);
        // 创建文件
        String entityFileUrl = SYS_PATH + dotToLine(ENTITY_PACKAGE) + "/" + name(CLASS_NAME, true) + ".java";
        File entityFile = new File(entityFileUrl);
        File entityDir = entityFile.getParentFile();
        if (!preview && !entityDir.exists()) {
            entityDir.mkdirs();
        }
        if (!preview && !zip && !entityFile.exists() && entityFile.createNewFile()) {
            // 若文件存在则不重新生成
            out = new FileOutputStream(entityFile);
            entityTemplate.renderTo(out);
        }

        // 生成mapper代码
        mapperTemplate.binding(varEntity, entity);
        String mapperResult = mapperTemplate.render();
//        log.info(mapperResult);
        // 创建文件
        String mapperFileUrl = SYS_PATH + dotToLine(MAPPER_PACKAGE) + "/" + name(CLASS_NAME, true) + "Mapper.java";
        File mapperFile = new File(mapperFileUrl);
        File mapperDir = mapperFile.getParentFile();
        if (!preview && !mapperDir.exists()) {
            mapperDir.mkdirs();
        }
        if (!preview && !zip && !mapperFile.exists() && mapperFile.createNewFile()) {
            // 若文件存在则不重新生成
            out = new FileOutputStream(mapperFile);
            mapperTemplate.renderTo(out);
        }

        // 生成dao代码
        daoTemplate.binding(varEntity, entity);
        String daoResult = daoTemplate.render();
//        log.info(daoResult);
        // 创建文件
        String daoFileUrl = SYS_PATH + dotToLine(DAO_PACKAGE) + "/" + name(CLASS_NAME, true) + "Dao.java";
        File daoFile = new File(daoFileUrl);
        File daoDir = daoFile.getParentFile();
        if (!preview && !daoDir.exists()) {
            daoDir.mkdirs();
        }
        if (!preview && !zip && !daoFile.exists() && daoFile.createNewFile()) {
            // 若文件存在则不重新生成
            out = new FileOutputStream(daoFile);
            daoTemplate.renderTo(out);
        }


        // 生成service代码
        serviceTemplate.binding(varEntity, entity);
        String serviceResult = serviceTemplate.render();
//        log.info(serviceResult);
        // 创建文件
        String serviceFileUrl = SYS_PATH + dotToLine(SERVICE_PACKAGE) + "/" + name(CLASS_NAME, true) + "Service.java";
        File serviceFile = new File(serviceFileUrl);
        File serviceDir = serviceFile.getParentFile();
        if (!preview && !serviceDir.exists()) {
            serviceDir.mkdirs();
        }
        if (!preview && !zip && !serviceFile.exists() && serviceFile.createNewFile()) {
            // 若文件存在则不重新生成
            out = new FileOutputStream(serviceFile);
            serviceTemplate.renderTo(out);
        }

        // 生成serviceImpl代码
        serviceImplTemplate.binding(varEntity, entity);
        String serviceImplResult = serviceImplTemplate.render();
//        log.info(serviceImplResult);
        // 创建文件
        String serviceImplFileUrl = SYS_PATH + dotToLine(SERVICE_IMPL_PACKAGE) + "/" + name(CLASS_NAME, true) + "ServiceImpl.java";
        File serviceImplFile = new File(serviceImplFileUrl);
        File serviceImplDir = serviceImplFile.getParentFile();
        if (!preview && !serviceImplDir.exists()) {
            serviceImplDir.mkdirs();
        }
        if (!preview && !zip && !serviceImplFile.exists() && serviceImplFile.createNewFile()) {
            // 若文件存在则不重新生成
            out = new FileOutputStream(serviceImplFile);
            serviceImplTemplate.renderTo(out);
        }

        // 生成controller代码
        controllerTemplate.binding(varEntity, entity);
        String controllerResult = controllerTemplate.render();
//        log.info(controllerResult);
        // 创建文件
        String controllerFileUrl = SYS_PATH + dotToLine(CONTROLLER_PACKAGE) + "/" + name(CLASS_NAME, true) + "Controller.java";
        File controllerFile = new File(controllerFileUrl);
        File controllerDir = controllerFile.getParentFile();
        if (!preview && !controllerDir.exists()) {
            controllerDir.mkdirs();
        }
        if (!preview && !zip && !controllerFile.exists() && controllerFile.createNewFile()) {
            // 若文件存在则不重新生成
            out = new FileOutputStream(controllerFile);
            controllerTemplate.renderTo(out);
        }

        // 生成mapperXml代码
        mapperXmlTemplate.binding(varEntity, entity);
        String mapperXmlResult = mapperXmlTemplate.render();
//        log.info(mapperXmlResult);
        // 创建文件
        String mapperXmlFileUrl = System.getProperty("user.dir") + MODULE + "/src/main/resources/mapper/" + name(CLASS_NAME, true) + "Mapper.xml";
        File mapperXmlFile = new File(mapperXmlFileUrl);
        File mapperXmlDir = mapperXmlFile.getParentFile();
        if (!preview && !mapperXmlDir.exists()) {
            mapperXmlDir.mkdirs();
        }
        if (!preview && !zip && !mapperXmlFile.exists() && mapperXmlFile.createNewFile()) {
            // 若文件存在则不重新生成
            out = new FileOutputStream(mapperXmlFile);
            mapperXmlTemplate.renderTo(out);
        }


        // 生成web api代码
        webApiTemplate.binding(varEntity, entity);
        String webApiResult = webApiTemplate.render();
//        log.info(webApiResult);
        // 创建文件
        String webApiFileUrl = WEB_PATH + "modules/" + MODULE_PACKAGE + "/api/" + name(CLASS_NAME, false) + ".js";
        File webApiFile = new File(webApiFileUrl);
        File webApiDir = webApiFile.getParentFile();
        if (!preview && !webApiDir.exists()) {
            webApiDir.mkdirs();
        }
        if (!preview && !zip && !webApiFile.exists() && webApiFile.createNewFile()) {
            // 若文件存在则不重新生成
            out = new FileOutputStream(webApiFile);
            webApiTemplate.renderTo(out);
        }

        webFormTemplate.binding(varEntity, entity);
        String webFormResult = webFormTemplate.render();
//        log.info(webFromResult);
        // 创建文件
        String webFormFileUrl = WEB_PATH + "modules/" + MODULE_PACKAGE + "/views/" + name(CLASS_NAME, false) + "/form.js";
        File webFormFile = new File(webFormFileUrl);
        File webFormDir = webFormFile.getParentFile();
        if (!preview && !webFormDir.exists()) {
            webFormDir.mkdirs();
        }
        if (!preview && !zip && !webFormFile.exists() && webFormFile.createNewFile()) {
            // 若文件存在则不重新生成
            out = new FileOutputStream(webFormFile);
            webFormTemplate.renderTo(out);
        }

        webTableTemplate.binding(varEntity, entity);
        String webTableResult = webTableTemplate.render();
//        log.info(webTableResult);
        // 创建文件
        String webTableFileUrl = WEB_PATH + "modules/" + MODULE_PACKAGE + "/views/" + name(CLASS_NAME, false) + "/table.vue";
        File webTableFile = new File(webTableFileUrl);
        File webTableDir = webTableFile.getParentFile();
        if (!preview && !webTableDir.exists()) {
            webTableDir.mkdirs();
        }
        if (!preview && !zip && !webTableFile.exists() && webTableFile.createNewFile()) {
            // 若文件存在则不重新生成
            out = new FileOutputStream(webTableFile);
            webTableTemplate.renderTo(out);
        }


        webRouterTemplate.binding(varEntity, entity);
        String webRouterResult = webRouterTemplate.render();
//        log.info(webRouterResult);
        // 创建文件
        String webRouterFileUrl = WEB_PATH + "modules/" + MODULE_PACKAGE + "/router/" + name(CLASS_NAME, false) + ".router.config.js";
        File webRouterFile = new File(webRouterFileUrl);
        File webRouterDir = webRouterFile.getParentFile();
        if (!preview && !webRouterDir.exists()) {
            webRouterDir.mkdirs();
        }
        if (!preview && !zip && !webRouterFile.exists() && webRouterFile.createNewFile()) {
            // 若文件存在则不重新生成
            out = new FileOutputStream(webRouterFile);
            webRouterTemplate.renderTo(out);
        }

        autoCodeRes.put("server-" + name(CLASS_NAME, true) + ".java", entityResult);
        autoCodeRes.put("server-" + name(CLASS_NAME, true) + "Dao.java", daoResult);
        autoCodeRes.put("server-" + name(CLASS_NAME, true) + "Mapper.java", mapperResult);
        autoCodeRes.put("server-" + name(CLASS_NAME, true) + "Service.java", serviceResult);
        autoCodeRes.put("server-" + name(CLASS_NAME, true) + "ServiceImpl.java", serviceImplResult);
        autoCodeRes.put("server-" + name(CLASS_NAME, true) + "Controller.java", controllerResult);
        autoCodeRes.put("server-" + name(CLASS_NAME, true) + "Mapper.xml", mapperXmlResult);
        autoCodeRes.put("web-api.js", webApiResult);
        autoCodeRes.put("web-form.js", webFormResult);
        autoCodeRes.put("web-table.vue", webTableResult);
        autoCodeRes.put("web-router.js", webRouterResult);

        autoCodeResultVo.setAutoCode(autoCodeRes);

        ArrayList<String> autoPaths = new ArrayList<String>();
        autoPaths.add(entityFileUrl);
        autoPaths.add(daoFileUrl);
        autoPaths.add(mapperFileUrl);
        autoPaths.add(serviceFileUrl);
        autoPaths.add(serviceImplFileUrl);
        autoPaths.add(controllerFileUrl);
        autoPaths.add(mapperXmlFileUrl);
        autoPaths.add(webApiFileUrl);
        autoPaths.add(webFormFileUrl);
        autoPaths.add(webTableFileUrl);
        autoPaths.add(webRouterFileUrl);
        autoPaths.add(System.getProperty("user.dir"));

        autoCodeResultVo.setAutoCodePath(autoPaths);
        if (zip) {
            String tmpDir = System.getProperty("user.dir") + "/autocode/";
            FileUtil.mkdir(tmpDir);

            autoCodeRes.keySet().stream().forEach(e -> {
                File tmpFile = new File(tmpDir + e);
                File tmpPDir = tmpFile.getParentFile();
                if (!tmpPDir.exists()) {
                    tmpPDir.mkdirs();
                }
                try {
                    log.info("======>" + tmpDir + e);
                    if (!tmpFile.exists() && tmpFile.createNewFile()) {
                        // 若文件存在则不重新生成
                        FileOutputStream o = new FileOutputStream(tmpFile);
                        String str = autoCodeRes.get(e);
                        o.write(str.getBytes(StandardCharsets.UTF_8));
                        o.flush();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            ZipUtil.zip(tmpDir, System.getProperty("user.dir") + "/autocode.zip", true);
            FileUtil.del(tmpDir);
        }

        if (out != null) {
            out.close();
        }
        //log.info("生成代码成功！", autoCodeRes);

        return autoCodeResultVo;
    }

    /**
     * 删除指定类代码
     *
     * @param className
     * @throws IOException
     */
    public void deleteCode(String className) throws IOException {

        String entityFileUrl = SYS_PATH + dotToLine(ENTITY_PACKAGE) + "/" + name(className, true) + ".java";
        Files.delete(Paths.get(entityFileUrl));

        String daoFileUrl = SYS_PATH + dotToLine(DAO_PACKAGE) + "/" + name(className, true) + "Mapper.java";
        Files.delete(Paths.get(daoFileUrl));

        String serviceFileUrl = SYS_PATH + dotToLine(SERVICE_PACKAGE) + "/" + name(className, true) + "Service.java";
        Files.delete(Paths.get(serviceFileUrl));

        String serviceImplFileUrl = SYS_PATH + dotToLine(SERVICE_IMPL_PACKAGE) + "/" + name(className, true) + "ServiceImpl.java";
        Files.delete(Paths.get(serviceImplFileUrl));

        String controllerFileUrl = SYS_PATH + dotToLine(CONTROLLER_PACKAGE) + "/" + name(className, true) + "Controller.java";
        Files.delete(Paths.get(controllerFileUrl));

        String mapperXmlFileUrl = System.getProperty("user.dir") + MODULE + "/src/main/resources/mapper/" + name(className, true) + "Mapper.xml";
        Files.delete(Paths.get(mapperXmlFileUrl));

        log.info("删除代码完毕！");
    }

    private void generatePlus(GroupTemplate gt) {

        try {
            generateMPlus(gt);
        } catch (Exception e) {
            System.out.println("请确保实体类存在并且【已编译生成的模块代码】，记得完善填入字段后再生成条件构造代码哦！");
        }
    }

    private void generateMPlus(GroupTemplate gt) throws Exception {

        Template plusTemplate = gt.getTemplate("mplus.btl");

        Entity entity = new Entity();

        entity.setClassName(name(CLASS_NAME, true));
        entity.setClassNameLowerCase(name(CLASS_NAME, false));
        List<Item> items = new ArrayList<>();

        String path = ENTITY_PACKAGE + "." + name(CLASS_NAME, true);
        Class<Object> c = (Class<Object>) Class.forName(path);
        Object obj = c.getDeclaredConstructor().newInstance();
        java.lang.reflect.Field[] fields = obj.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {

            java.lang.reflect.Field field = fields[i];
            field.setAccessible(true);
            // 字段名
            String fieldName = field.getName();
            String fieldType = field.getType().getName();
            // 白名单
            if ("serialVersionUID".equals(fieldName)) {
                continue;
            }
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null && !tableField.exist()) {
                continue;
            }

            // 获得字段注解
            ApiModelProperty myFieldAnnotation = field.getAnnotation(ApiModelProperty.class);
            String fieldNameCN = fieldName;
            if (myFieldAnnotation != null) {
                fieldNameCN = myFieldAnnotation.value();
            }
            fieldNameCN = StrUtil.isBlank(fieldNameCN) ? fieldName : fieldNameCN;

            if (fieldType.startsWith("java.lang.")) {
                fieldType = StrUtil.subAfter(fieldType, "java.lang.", false);
            }

            Item item = new Item();
            item.setType(fieldType);
            item.setUpperName(name(fieldName, true));
            item.setLowerName(name(fieldName, false));
            item.setLineName(camel2Underline(fieldName));
            item.setTitle(fieldNameCN);

            items.add(item);
        }

        // 绑定参数
        plusTemplate.binding("entity", entity);
        plusTemplate.binding("items", items);
        String result = plusTemplate.render();

        System.out.println("=================================================================================");
        System.out.println("=====生成条件构造代码Plus成功！请根据需要自行复制添加以下代码至控制层方法Controller中======");
        System.out.println("=================================条件构造代码起始线=================================");

        //System.out.println(result);

        System.out.println("=================================条件构造代码终止线=================================");
        System.out.println("【代码方法添加位置：" + CONTROLLER_PACKAGE + "." + CLASS_NAME + "Controller.java】");
        System.out.println("【若未读取到字段请主动编译下生成的模块代码】");
    }


    private void getFields() throws SQLException {
        DataSource ds = new SimpleDataSource("jdbc:mysql://localhost:3306/dbName", "root", "123456");
        Db.use(ds).execute("", null);
    }


    /**
     * 点转斜线
     *
     * @param str
     * @return
     */
    public static String dotToLine(String str) {
        return str.replace(".", "/");
    }

    /**
     * 驼峰法转下划线
     */
    public static String camel2Underline(String str) {

        if (StrUtil.isBlank(str)) {
            return "";
        }
        if (str.length() == 1) {
            return str.toLowerCase();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                sb.append("_" + Character.toLowerCase(str.charAt(i)));
            } else {
                sb.append(str.charAt(i));
            }
        }
        return (str.charAt(0) + sb.toString()).toLowerCase();
    }

    /**
     * 首字母是否大小写
     *
     * @param name
     * @param isFirstUpper
     * @return
     */
    public static String name(String name, boolean isFirstUpper) {

        if (StrUtil.isBlank(name)) {
            throw new MyBootException("name不能为空");
        }

        if (name.length() == 1) {
            if (isFirstUpper) {
                return name.toUpperCase();
            } else {
                return name.toLowerCase();
            }
        }

        StringBuilder sb = new StringBuilder();
        if (isFirstUpper) {
            sb.append(Character.toUpperCase(name.charAt(0)));
        } else {
            sb.append(Character.toLowerCase(name.charAt(0)));
        }
        sb.append(name.substring(1));
        return sb.toString();
    }
}
