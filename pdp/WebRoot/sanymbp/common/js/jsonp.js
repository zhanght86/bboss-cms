/* * 跨域访问 * */
var jsonp={     
	getJSON:function(options){
		$.ajax({ 
            async:false, 
            url: options.url,  // 跨域URL
            type: 'GET', 
            dataType: 'jsonp', 
            jsonp: 'jsoncallback', //默认callback
            data: options.data, 
            timeout: 5000, 
            success: function (json) { //客户端jquery预先定义好的callback函数，成功获取跨域服务器上的json数据后，会动态执行这个callback函数 
            	options.success(json)
                if(json.actionErrors.length!=0){ 
                	 //    alert(json.actionErrors); 
                	options.error(json.actionErrors)
                } 
            }, 
            error: function(xhr){ 
                //jsonp 方式此方法不被触发
                //请求出错处理 
                //alert("请求出错(请检查相关度网络状况.)"); 
            	options.error("请求出错(请检查相关度网络状况.)")
            } 
        });
	}
}
