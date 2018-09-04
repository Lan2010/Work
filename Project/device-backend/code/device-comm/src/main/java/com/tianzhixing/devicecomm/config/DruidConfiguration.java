package com.tianzhixing.devicecomm.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 加载druid数据库连接池的配置
 * @author dev-teng
 * @date 2018年6月27日
 */
@Configuration
public class DruidConfiguration {

	@Bean(destroyMethod = "close", initMethod = "init")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource druidDataSource() {
		DruidDataSource druidDataSource = new DruidDataSource();
		return druidDataSource;
	}

}
