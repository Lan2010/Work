﻿define(["jquery","Global","Menu","Validate","DTP"],function(n,s,a,t,e){"use strict";var i,o,r=function(){o=i.validate(),n("#btnSubmit").on("click",function(){if(o.form()){s.$page.msg("loading",{text:"添加中"});var a="/api/user/add",t=i.serialize();n.ajax({url:s.path+a,type:"post",data:t,dataType:"json",success:function(t){s.checkJson(t)&&(s.$page.msg("success",{text:"添加成功",time:2e3}),window.history.back())},error:function(t,e){s.$page.msg("error",{text:a+" - "+t.status+" - "+t.statusText+" - "+e})}})}}),i.on("submit",function(){return!1})};return{onSearch:function(t,e){},onLoad:function(t,e){a.active("user/list"),s.$page.addClass("page-gray"),i=n("#formAdd"),r()}}});