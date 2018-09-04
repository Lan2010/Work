CREATE TABLE `t_params_appoint` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `view_name` varchar(20) DEFAULT NULL COMMENT '参数中文释义，页面展示用名称',
  `dev_param` varchar(30) DEFAULT NULL COMMENT '对应设备参数名',
  `describe` varchar(50) DEFAULT NULL COMMENT '参数描述',
  `rw` varchar(2) DEFAULT 'r' COMMENT 'r：可读，rw：可读写',
  `input_type` varchar(20) DEFAULT NULL COMMENT '输入类型',
  `options` varchar(100) DEFAULT NULL COMMENT '输入类型的分类集',
  `group` varchar(20) DEFAULT NULL COMMENT '所属模块的分组',
  `required` tinyint(1) DEFAULT 0 COMMENT '是否必填',
  `rule` varchar(10) DEFAULT NULL COMMENT '验证规则，例如mac表示是一个mac地址类型的字符串，phone表示是一个手机类型',
  `regExp` varchar(50) DEFAULT NULL COMMENT '正则表达式',
  `regExpTip` varchar(50) DEFAULT NULL COMMENT '正则验证提示，输入提示作用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='设备参数约定规则';

