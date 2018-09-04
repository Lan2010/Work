package com.tianzhixing.devicecomm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@MapperScan("com.tianzhixing.devicecomm.mapper")
public class DeviceCommApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeviceCommApplication.class, args);
	}
	
}