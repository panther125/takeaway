package com.panther.takeaway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan  // 扫描web组件
@EnableTransactionManagement
@EnableCaching
public class application {

    public static void main(String[] args) {
        SpringApplication.run(application.class,args);
    }

}
