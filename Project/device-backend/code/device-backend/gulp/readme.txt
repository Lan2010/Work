
-------------------------------------------------------------------------------------

说明： 

1. 前端发布代码前，请按下面的说明安装

-------------------------------------------------------------------------------------


【前端发布说明】

	前端使用 Gulp 插件发布

	源文件目录：  /srcFront/			(web 根目录)
	目标文件目录：/src/main/webapp/		(java app web 根目录)



【Gulp安装说明】

	安装步骤 ：

	1. 安装 nodejs - http://nodejs.cn/

	2. 安装 gulp - https://www.gulpjs.com.cn/docs/getting-started/

		1). cmd 安装命令： npm install --global gulp

	2. 安装 plugins

		1). cmd 到/gulp/目录下, 执行：npm install

	3. 更新自定义插件

		1). 解压 /gulp/node-modules/node-modules.zip，覆盖 /gulp/node-modules/gulp-rev/,  /gulp/node-modules/gulp-rev-collector/

			覆盖文件：

				(1). /node-modules/gulp-rev/index.js
				(2). /node-modules/gulp-rev-collector/index.js



【Gulp使用说明】
	
    1. cmd c到/gulp/录下，输入命令：

    	1). gulp build 				-  发布 



