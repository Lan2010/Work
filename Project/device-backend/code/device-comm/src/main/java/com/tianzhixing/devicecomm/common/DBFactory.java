package com.tianzhixing.devicecomm.common;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.SocketOptions;
import com.tianzhixing.devicecomm.config.ScyllaDBConfiguration;

/**
 * create session,the session holds connections to DB
 * @author dev-teng
 * @date 2018年6月28日
 */
public class DBFactory {
	
	public Cluster getCluster(ScyllaDBConfiguration scyllaDBConfiguration) {
		// 认证配置 ,如果有用户名或密码
		// AuthProvider authProvider = new PlainTextAuthProvider("ershixiong",
		// "123456");
		// LoadBalancingPolicy lbp = new TokenAwarePolicy(
		// DCAwareRoundRobinPolicy.builder().withLocalDc("myDC").build()
		// );

		// 读超时或连接超时设置
		SocketOptions so = new SocketOptions().setReadTimeoutMillis(3000).setConnectTimeoutMillis(3000);

		// 连接池配置
		// PoolingOptions poolingOptions = new
		// PoolingOptions().setConnectionsPerHost(HostDistance.LOCAL, 2, 3);
		// 集群在同一个机房用HostDistance.LOCAL 不同的机房用HostDistance.REMOTE 忽略用HostDistance.IGNORED
		PoolingOptions poolingOptions = new PoolingOptions().setMaxRequestsPerConnection(HostDistance.LOCAL, 64)// 每个连接最多允许64个并发请求
				.setCoreConnectionsPerHost(HostDistance.LOCAL, 2)// 和集群里的每个机器都至少有2个连接
				.setMaxConnectionsPerHost(HostDistance.LOCAL, 6);// 和集群里的每个机器都最多有6个连接

		// 查询配置
		// 设置一致性级别ANY(0),ONE(1),TWO(2),THREE(3),QUORUM(4),ALL(5),LOCAL_QUORUM(6),EACH_QUORUM(7),SERIAL(8),LOCAL_SERIAL(9),LOCAL_ONE(10);
		// 可以在每次生成查询statement的时候设置，也可以像这样全局设置
		QueryOptions queryOptions = new QueryOptions().setConsistencyLevel(ConsistencyLevel.ONE);

		// 重试策略

		Builder builder = Cluster.builder();
		Cluster cluster = builder.addContactPoints(scyllaDBConfiguration.getUrl())
				// .withAuthProvider(authProvider)
				// .withLoadBalancingPolicy(lbp)
				.withSocketOptions(so).withPoolingOptions(poolingOptions)
				.withQueryOptions(queryOptions)
				.withPort(Integer.valueOf(scyllaDBConfiguration.getPort()))
				.build();
		return cluster;
	}
}
