package core.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import core.dao.DaoSupport;
import core.dao.DevOnlineLogDao;

@Repository
public class DevOnlineLogDaoImpl implements DevOnlineLogDao {

	@Autowired
	private DaoSupport daoSupport;

	@Override
	public Integer countToday(Map<String, Object> map) {
		Session session = null;
		try {
			session = daoSupport.getSession();
			// String cql = "select * from dev_online_log";

			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			Long today = c.getTimeInMillis();
			ResultSet resultSet = session.execute(QueryBuilder.select("dev_number", "online_status", "record_time")
					.from("dev_online_log")
					.where(QueryBuilder.eq("online_status", map.get("online_status")))//筛选在线情况
					.and(QueryBuilder.gt("record_time", new Date(today)))//筛选大于今日0时0分0秒的数据
					.allowFiltering());
			List<Row> rows = resultSet.all();
			return rows.size();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null)
				session.close();
		}
	}

}
