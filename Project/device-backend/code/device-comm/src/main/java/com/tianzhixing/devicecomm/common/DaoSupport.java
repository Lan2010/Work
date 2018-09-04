package com.tianzhixing.devicecomm.common;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.tianzhixing.devicecomm.config.ScyllaDBConfiguration;

@Component
public class DaoSupport{
	@Autowired
	private ScyllaDBConfiguration scyllaDBConfiguration;
	
	private Cluster cluster;
	
	@PostConstruct
	public void setCluster() {
		this.cluster = new DBFactory().getCluster(scyllaDBConfiguration);
		
	}

	public Cluster getCluster() {
		return this.cluster;
	}

	public Session getSession() {
		return getCluster().connect(scyllaDBConfiguration.getKeyspace());
	}
	
	public void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}
}
