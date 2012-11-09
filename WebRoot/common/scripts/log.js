function pageCounter(appName,moduleName,userName_temp){

		var siteName = appName;
		var channelName = moduleName;//模块的名称
		var docrefer = document.referrer;
		var pageURL = window.location.href;
		var userName = userName_temp
		var url =  'http://127.0.0.1:8888/SanyPDP/sanylog/browserCounter.freepage?jsonp_callback=?';
		
		$.getJSON(url, {  pageURL:pageURL,referer:docrefer, siteName:encodeURIComponent(siteName), 
		channelName:encodeURIComponent(channelName),browserUser:encodeURIComponent(userName) },
           function(data) {   
                
           });
	 
	 }


function pageCounterDetail(appName,moduleName,operContent_temp,moduleCode_temp,modulePath_temp){

	var siteName = appName;
	var channelName = moduleName;//模块的名称
	var docrefer = document.referrer;
	var pageURL = window.location.href;
	var moduleCode = moduleCode_temp;
	var modulePath = modulePath_temp;
	var url =  'http://10.0.48.56:8081/SanyPDP/sanylog/browserCounter.freepage?jsonp_callback=?';
	
	$.getJSON(url, {  pageURL:pageURL,referer:docrefer, siteName:encodeURIComponent(siteName), moduleCode:moduleCode,modulePath:encodeURIComponent(modulePath),
	channelName:encodeURIComponent(channelName) },
       function(data) {   
            
       });
 
 }
function operateCount(appName_temp, moduleName_temp, operContent_temp,operator_temp) {
	var appName = appName_temp; // 站点名称 已有
	var moduleName = moduleName_temp; // 模块名称 已有
	var operContent = operContent_temp; // 操作内容 已有

	var pageURL = window.location.href; // 访问页面url
	var referer = document.referrer; // 访问来源页面url 系统自动设置
    var operator = operator_temp
    var url = 'http://127.0.0.1:8888/SanyPDP/sanylog/operateCounter.freepage?jsonp_callback=?'; // 调用的接口地址
	$.getJSON(url, {
		appName : encodeURIComponent(appName),
		moduleName : encodeURIComponent(moduleName),
		pageURL : pageURL,
		referer : referer,
		operContent : encodeURIComponent(operContent),
		operator:encodeURIComponent(operator)
	}, function(data) {
	});
}

function operateCountDetail(appName_temp, moduleName_temp, operContent_temp,moduleCode_temp,modulePath_temp) {
	var appName = appName_temp; // 站点名称 已有
	var moduleName = moduleName_temp; // 模块名称 已有
	var operContent = operContent_temp; // 操作内容 已有

	var pageURL = window.location.href; // 访问页面url
	var referer = document.referrer; // 访问来源页面url 系统自动设置
	var moduleCode = moduleCode_temp;
	var modulePath = modulePath_temp;

	var url = 'http://10.0.48.56:8081/SanyPDP/sanylog/operateCounter.freepage?jsonp_callback=?'; // 调用的接口地址
	$.getJSON(url, {
		appName : encodeURIComponent(appName),
		moduleName : encodeURIComponent(moduleName),
		pageURL : pageURL,
		referer : referer,
		operContent : encodeURIComponent(operContent),
		moduleCode:moduleCode,
		modulePath:encodeURIComponent(modulePath)
	}, function(data) {
	});
}
