/*!
 * Front build
 * @Author Gang
 * @Lastest Update 2018-03-06
 */

var gulp = require("gulp"),
	clean = require("gulp-clean"),
	header = require('gulp-header'),

	change = require('gulp-change'),
	htmlreplace = require('gulp-html-replace'),
	// useref = require('gulp-useref'),

	uglify = require('gulp-uglify'),
	cssmini = require('gulp-minify-css'),
	htmlmin = require('gulp-htmlmin'),

	rev = require("gulp-rev"),
	revCollector = require("gulp-rev-collector"),
	
	runSequence = require('run-sequence');

// Stream path config
var path = {
	src: '../srcFront',
	dist: '../src/main/webapp',
	rev: 'node_modules/gulp-rev-manifest'
}

gulp.task('default',function(){
    
		
});

// 更新版本号

var updateVer = function(content){

	var file = content,
		reg = /(version\s=\s')([0-9\.]+)(')/,
		ver = file.match(reg),
		res = '';

	if (ver){

    	var verStr = ver[0];
    	verStr = verStr.replace(reg,'$2');					// 获取版本号
    	// console.log(verStr);

    	if (verStr){
    		var verArr = verStr.split('.');
    		var len = verArr.length;
    		verArr[len-1] = parseInt(verArr[len-1]) + 1;	// 版本号最后一位加1
    		res = verArr.join('.');
    	}
    	// console.log(res);
	}

	if (res){
		file = file.replace(reg, "version = '"+res+"'");
	}
	
    return file;
};

gulp.task('updateVer', function() {

    return gulp.src([path.src + '/*.html',path.src + '/*.jsp'])
        .pipe(change(function(content){
        	return updateVer(content)
        }))
		.pipe(header('\ufeff'))
        .pipe(gulp.dest(path.src))
});

// Clean distribution files
gulp.task('clean',function(){
	
	return gulp.src([path.dist+'/*.jsp',path.dist+'/*.html',path.dist+'/static/',path.dist+'/page/',path.dist+'/module/',path.dist+'/template/'],{read: false})
	    .pipe(clean({force: true}));	
});

// Build jsp
gulp.task('build-jsp',function(){
	
	return gulp.src([path.src+'/*.jsp','!'+path.src+'/index.jsp'])
	    .pipe(header('\ufeff'))
	    .pipe(gulp.dest(path.dist));			
});

// Build jsp
gulp.task('build-index',function(){
	
	return gulp.src([path.src+'/index.jsp'])
	    .pipe(header('\ufeff'))
	    .pipe(gulp.dest(path.dist + '/WEB-INF/viewJsp/'));			
});

// Build html
gulp.task('build-module-html',function(){
	
	return gulp.src([path.src+'/module/**/*.html'])
	    .pipe(header('\ufeff'))
	    .pipe(gulp.dest(path.dist + '/module'));			
});

// Build static - font, images
gulp.task('build-static',function(){
	
	return gulp.src([path.src+'/static/**/images/**/*.*',path.src+'/static/**/fonts/**/*.*',path.src+'/static/**/js/**/*.png',path.src+'/static/**/js/**/*.gif',path.src+'/static/**/js/**/*.jpg',path.src+'/static/**/resource/**/*.*'])
	    .pipe(gulp.dest(path.dist + '/static'));			
});

// Build css
gulp.task('build-css',function(){
	
	return gulp.src([path.src+'/static/**/*.css'])
	    .pipe(header('\ufeff'))
	    .pipe(gulp.dest(path.dist + '/static'));			
});

// Build js
gulp.task('build-js',function(){
	
	return gulp.src([path.src+'/static/**/js/**/*.js'])
	    .pipe(header('\ufeff'))
	    .pipe(gulp.dest(path.dist + '/static'));			
});

gulp.task('build-module-js',function(){
	
	return gulp.src([path.src+'/module/**/*.js'])
	    .pipe(header('\ufeff'))
	    .pipe(gulp.dest(path.dist + '/module'));			
});




// Compress css
gulp.task('compress-css',function(){
							
	return gulp.src([path.dist + '/static/**/*.css',,'!' + path.dist + '/static/**/*.min.css'])
	    .pipe(cssmini())
	    .pipe(header('\ufeff'))
	    .pipe(gulp.dest(path.dist + '/static'));
});

// Compress js
gulp.task('compress-js',function(){
							
	return gulp.src([path.dist + '/static/**/*.js','!' + path.dist + '/static/js/**/*.min.js'])
	    // .pipe(uglify({preserveComments:'some',output:{ascii_only:true}}))
	    .pipe(uglify())
	    .pipe(header('\ufeff'))
	    .pipe(gulp.dest(path.dist + '/static'));
});

// Compress module js
gulp.task('compress-module-js',function(){
							
	return gulp.src([path.dist + '/module/**/*.js','!' + path.dist + '/module/**/*.min.js'])
	    // .pipe(uglify({preserveComments:'some',output:{ascii_only:true}}))
	    .pipe(uglify())
	    .pipe(header('\ufeff'))
	    .pipe(gulp.dest(path.dist + '/module'));
});


// Build rev list - Css
gulp.task('build-css-rev',function(){

    return gulp.src(path.dist+'/static/**/*.css')
		.pipe(rev())
		.pipe(rev.manifest())
		.pipe(gulp.dest(path.rev + '/css'))
});

// Build rev list - js
gulp.task('build-js-rev',function(){

    return gulp.src(path.dist+'/static/js/**/*.js')
		.pipe(rev())
		.pipe(rev.manifest())
		.pipe(gulp.dest(path.rev + '/js'))
});


// Revision css for jsp
gulp.task('rev-css-for-jsp',function(){
	
	return gulp.src([path.rev+'/css/*.json',path.dist+'/*.jsp'])
	    .pipe(revCollector({
		    replaceReved: true				   
		}))
		.pipe(header('\ufeff'))
		.pipe(gulp.dest(path.dist)) // Replace targets
});

// Revision css for jsp
gulp.task('rev-css-for-jsp-views',function(){
	
	return gulp.src([path.rev+'/css/*.json',path.dist+'/WEB-INF/views/*.jsp'])
	    .pipe(revCollector({
		    replaceReved: true				   
		}))
		.pipe(header('\ufeff'))
		.pipe(gulp.dest(path.dist+'/WEB-INF/views')) // Replace targets
});

// Revision js for jsp
gulp.task('rev-js-for-jsp',function(){
	
	return gulp.src([path.rev+'/js/*.json',path.dist+'/*.jsp'])
	    .pipe(revCollector({
		    replaceReved: true				   
		}))
		.pipe(header('\ufeff'))
		.pipe(gulp.dest(path.dist)) // Replace targets
});

// Revision js for jsp
gulp.task('rev-js-for-jsp-views',function(){
	
	return gulp.src([path.rev+'/js/*.json',path.dist+'/WEB-INF/views/*.jsp'])
	    .pipe(revCollector({
		    replaceReved: true				   
		}))
		.pipe(header('\ufeff'))
		.pipe(gulp.dest(path.dist+'/WEB-INF/views')) // Replace targets
});


// Build all
gulp.task('build', function (done) {
    condition = false;
    runSequence(
         'clean',
         'updateVer',
         'build-jsp',
         'build-index',
         'build-module-html',
         'build-static',		// images, fonts
         'build-css',
		 'build-js',
		 'build-module-js',

		 'compress-css',
		 'compress-js',
		 'compress-module-js',

		 'build-css-rev',
		 'build-js-rev',
		 'rev-css-for-jsp',
		 'rev-css-for-jsp-views',
		 'rev-js-for-jsp',
		 'rev-js-for-jsp-views',
    done);
});
