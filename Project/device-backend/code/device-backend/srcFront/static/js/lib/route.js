/*!
 *	[定义模块 - 路由模块]
 *
 *	@ Version v1.0
 *  @ Author Gang
 *  @ Latest Update 2018-02-02
 */


define(function(){

    'use strict';

    /**
     *  定义路由对象
     */
    var Route = {

        extend: function(options){
            if (Object.prototype.toString.call(options) === '[object Object]'){
                for (var key in options){
                    var copy = options[key];
                    if (this === options[key]){
                        continue;
                    }
                    this[key] = copy;
                }
            }
            return this;
        },

        oldPath: '',
        newPath: '',
        routes: {},

        onlyHashing: false,

        /**
         *  相同路径更新Hash，不触发Route
         */
        updateHash: function(params){

            if (!params){ return; }

            var that = this,
                url = window.location.href.split('#'),
                path = url[1];

            if (path){

                path = path.split('?');
                var oldParams = path[1] ? path[1] : '';

                params = $.param(params);

                if (oldParams != params){
                    // console.log('[Route][updateHash]' + params);
                    that.onlyHashing = true;
                    window.location.href = url[0] + '#' + path[0] + (params ? '?' + params : '');
                }
            }

        },

        /**
         *  监听路由
         */
        start: function(){

            var that = this;

            if ('onhashchange' in window && (document.documentMode === undefined || document.documentMode > 7)) {
                if (window.history === true) {
                    setTimeout(function() {
                        window.onpopstate = onRoute;
                    }, 300);
                }
                else {
                    window.onhashchange = onRoute;
                }
                window.mode = 'modern';
            } else {
                console.log('[Auth System] Sorry, your browser doesn\'t support route');
            }

            // Init
            onRoute();
        }
    };

    /**
     *  处理路由变化 - 引自backbone
     *  @param route
     *  @returns {RegExp}
     */
    var getRegExp = function(route){

        var optionalParam = /\((.*?)\)/g,
            namedParam    = /(\(\?)?:\w+/g,
            splatParam    = /\*\w+/g,
            escapeRegExp  = /[\-{}\[\]+?.,\\\^$|#\s]/g;

        route = route.replace(escapeRegExp, '\\$&')
                    .replace(optionalParam, '(?:$1)?')
                    .replace(namedParam, function(match, optional) {
                        return optional ? match : '([^/?]+)';
                    })
                    .replace(splatParam, '([^?]*?)');

        return new RegExp('^' + route + '(?:\\?([\\s\\S]*))?$');
    };


    /**
     *  匹配路由，触发回调
     */
    var onRoute = function(e){

        if (Route.onlyHashing){
            Route.onlyHashing = false;
            return;
        }

        var oldURL = e && e.oldURL || '',
            newURL = e && e.newURL || window.location.href,
            oldPath = /.*#/.test(oldURL) ? oldURL.replace(/.*#/, '') : '',
            newPath = /.*#/.test(newURL) ? newURL.replace(/.*#/, '') : '',
            handler = newPath ? Route.routes['*'] : '',
            result = [];  

        for (var path in Route.routes) {
            if (path == '*'){ continue; }
            var reg = getRegExp(path), res = reg.exec(newPath);
            if(res && res.length > 1){
                handler = Route.routes[path];                
                result = res.slice(1);
            }   
        }

        oldPath = oldPath ? oldPath.split('?')[0] : oldPath;
        newPath = newPath ? newPath.split('?')[0] : newPath;

        // console.log('-------------------------------');
        // console.log('[Route][' + handler + '] newPath - ' + newPath + ' | oldPath - ' + oldPath);
        handler && Route[handler] && Route[handler].call(Route,result,newPath,oldPath);
    };

    return Route;

});