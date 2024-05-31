package com.yzh.cmdb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * cmdb启动类
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
@SpringBootApplication
@MapperScan("com.yzh.cmdb.mapper")
public class CmdbApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmdbApplication.class, args);
    }
}
