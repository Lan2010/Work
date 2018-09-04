package com.tianzhixing.devicecomm.dao.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.tianzhixing.devicecomm.common.DaoSupport;
import com.tianzhixing.devicecomm.dao.DevOnlineLogDao;
import com.tianzhixing.devicecomm.util.SnowflakeIdWorker;

@Service
public class DevOnlineLogDaoImpl  implements DevOnlineLogDao {
	//private static SnowflakeIdWorker snowflakeIdWorker =  new SnowflakeIdWorker(0, 0);
	
	@Autowired
	private DaoSupport daoSupport;
	
	@Override
	public void insertLog(Map<String, Object> map) {
		Session session = null;
		try {
			session = daoSupport.getSession();
			//String cql = "insert into dev_online_log (record_id,dev_number,online_status,record_time) values (?,?,?,?,?)";
			Insert insert = QueryBuilder.insertInto("dev_online_log")
				.values(new String[]{"dev_number","online_status","record_time"}, new Object[]{map.get("devNum"),map.get("status"),map.get("time")});
			session.execute(insert);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(session!=null)
				session.close();
		}
	}

}
