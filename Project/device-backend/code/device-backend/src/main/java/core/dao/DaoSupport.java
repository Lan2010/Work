package core.dao;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import core.component.DBFactory;

/**
 * ScyllaDB核心支持类
 * @author dev-teng
 * @date 2018年7月19日
 */
@Component
public class DaoSupport{
	@Autowired
	private ScyllaDBConfiguration scyllaDBConfiguration;
	
	private Cluster cluster;
	
	@PostConstruct
	public void setCluster() {
		this.cluster = DBFactory.getCluster(scyllaDBConfiguration);
		
	}

	public Cluster getCluster() {
		return this.cluster;
	}

	public Session getSession() {
		notNull(getCluster(),"Cluster is null");
		return getCluster().connect(scyllaDBConfiguration.getKeyspace());
	}
	
	public void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}
}
