package core.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import core.pojo.ParamsAppoint;

public interface ParamsAppointMapper {
	/**
	 * 获取设备可配置参数信息
	 * 
	 * @return
	 * @throws SQLException
	 */
	@Select("select * from `t_params_appoint`")
	@Results({ @Result(property = "viewName", column = "view_name"),
			@Result(property = "devParam", column = "dev_param"),
			@Result(property = "inputType", column = "input_type"),
			@Result(property = "regExp", column = "reg_exp"),
			@Result(property = "regExpTip", column = "reg_exp_tip"),
			@Result(property = "devAlias", column = "dev_alias")
	})
	public List<ParamsAppoint> getParamsAppoint() throws SQLException;
}
