package com.limaopu.myboot;

import com.limaopu.myboot.core.common.utils.ResultUtil;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author mac
 */
@SpringBootApplication
// 启用JPA审计
@EnableJpaAuditing
// 启用缓存
@EnableCaching
// 启用异步
@EnableAsync
// 启用自带定时任务
@EnableScheduling
// 启用Admin监控
@EnableAdminServer
@RestController
public class MyBootApplication {

    public static void main(String[] args) {
        MyBootApplication.args = args;
        context = SpringApplication.run(MyBootApplication.class, args);
    }

    private static ConfigurableApplicationContext context;

    private static String[] args;

    @PostMapping("/api/v1/system/reloadSystem")
    public Object reloadSystem() {

        ExecutorService threadPool = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1), new ThreadPoolExecutor.DiscardOldestPolicy());
        threadPool.execute(() -> {
             context.close();
             context = SpringApplication.run(MyBootApplication.class, args);
        });

        threadPool.shutdown();

        return ResultUtil.success("系统重启成功");
    }
}
