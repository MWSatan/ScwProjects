package com.XYF;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient  //启用Eureka客户端功能服务功能
@MapperScan("com.XYF.crowd.mapper")  //扫描mapper文件
public class CrowdMainClass {

	public static void main(String[] args) {
		SpringApplication.run(CrowdMainClass.class, args);
	}

}
