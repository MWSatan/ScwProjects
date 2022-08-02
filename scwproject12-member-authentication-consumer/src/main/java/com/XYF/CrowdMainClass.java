package com.XYF;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*
Eureka Client不要在SpringBoot启动类上标注@EnableEurekaClient注解也可以向注册中心注册的原因:

eureka.client.enabled这个属性的值为true才会初始化这个类（默认值为true，只有手动赋值为false，就不会初始化这个类了）。
 */

/*@EnableEurekaClient
@EnableDiscoveryClient //启动发现服务注解,当前版本可以不写，后面以防万一都写上比较好\
*/
@EnableFeignClients //启用feign客户端功能
@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class CrowdMainClass {

	public static void main(String[] args) {
		SpringApplication.run(CrowdMainClass.class, args);
	}

}
