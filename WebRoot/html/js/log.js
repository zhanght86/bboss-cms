var logHost ='http://10.0.15.241:8081';//日志接口地址
function pageCounter(appName,moduleName,userName_temp){

		var siteName = appName;//系统名称
		var channelName = moduleName;//模块的名称
		var docrefer = document.referrer;
		var pageURL = window.location.href;
		var userName = userName_temp;//用户名
		var url =  logHost+'/SanyPDP/sanylog/browserCounter.freepage?jsonp_callback=?';
		
		$.getJSON(url, {  pageURL:pageURL,referer:docrefer, siteName:encodeURIComponent(siteName), 
		channelName:encodeURIComponent(channelName),browserUser:encodeURIComponent(userName) },
           function(data) {   
                
           });
	 
	 }


function pageCounterDetail(appName,moduleName,userName_temp,moduleCode_temp){
    var userName = userName_temp;//用户名称
	var siteName = appName;//系统名称
	var channelName = moduleName;//功能路径
	var docrefer = document.referrer;
	var pageURL = window.location.href;
	var moduleCode = moduleCode_temp;//功能编码
	
	var url =  logHost+'/SanyPDP/sanylog/browserCounter.freepage?jsonp_callback=?';
	
	$.getJSON(url, {  pageURL:pageURL,referer:docrefer, siteName:encodeURIComponent(siteName), moduleCode:moduleCode,
	channelName:encodeURIComponent(channelName) ,browserUser:encodeURIComponent(userName)},
       function(data) {   
            
       });
 
 }
function operateCount(appName_temp, moduleName_temp, operContent_temp,operator_temp) {
	var appName = appName_temp; // 站点名称 已有
	var moduleName = moduleName_temp; // 模块名称 已有
	var operContent = operContent_temp; // 操作内容 已有

	var pageURL = window.location.href; // 访问页面url
	var referer = document.referrer; // 访问来源页面url 系统自动设置
    var operator = operator_temp;//操作人
    var url = logHost+'/SanyPDP/sanylog/operateCounter.freepage?jsonp_callback=?'; // 调用的接口地址
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

function operateCountDetail(appName_temp, moduleName_temp, operContent_temp,operator_temp,moduleCode_temp) {
	var appName = appName_temp; // 站点名称 已有
	var moduleName = moduleName_temp; // 模块名称 已有
	var operContent = operContent_temp; // 操作内容 已有
    var operator = operator_temp;//操作人
	var pageURL = window.location.href; // 访问页面url
	var referer = document.referrer; // 访问来源页面url 系统自动设置
	var moduleCode = moduleCode_temp;//功能编码


	var url = logHost+'/SanyPDP/sanylog/operateCounter.freepage?jsonp_callback=?'; // 调用的接口地址
	$.getJSON(url, {
		appName : encodeURIComponent(appName),
		moduleName : encodeURIComponent(moduleName),
		pageURL : pageURL,
		referer : referer,
		operContent : encodeURIComponent(operContent),
		moduleCode:moduleCode,
		operator:encodeURIComponent(operator)
	}, function(data) {
	});
}
