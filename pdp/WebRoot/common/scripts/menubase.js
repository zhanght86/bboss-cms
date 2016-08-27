var sany_old_menuid = "anchor_publicitem";

function navto_sany_MenuItem(tokenurl,menuId,url,target,options,title)
{
	var ppup = options?options.popup:false;
	var winopen = options?options.winopen:false;
	var maxstate = options ?(options.maxstate === true || options.maxstate === false?options.maxstate :true):true;
	var width = options && options.width ?options.width:777;
	var height = options && options.height ?options.height:500;
	
	//pageCounterDetail(appName_log,url.split('?')[0],userName_log,"");//参数：1.系统名称，2.功能的url，3.用户名，4.功能编码
	if(!ppup)
	{
		$('#'+target).attr("src",url);
	}
	else
	{
		if(!winopen)
		{
			$.dialog({ maxState:maxstate,title:title,width:width ,height:height, content:'url:'+url,lock: true});
		}
		else
		{
			window.open(url, "");
		}
	}
	
	
	if(sany_old_menuid == "anchor_"+menuId)
	{
		
		
		
		return;
	}
	else
	{
		$('#'+sany_old_menuid).attr("class","");
		sany_old_menuid = 	"anchor_"+menuId;
		$('#'+sany_old_menuid).attr("class","select");
		
	}	
}

function navto_sany_MenuItem_window(tokenurl,menutitle,menuId,menupath,contextpath,target)
{

	if(sany_old_menuid == "anchor_"+menuId)
	{
		//$.dialog({ title:menutitle,width:740,height:560, content:'url:'+url,lock: true});
		window.open(url);
		return;
	}
	else
	{
		$('#'+sany_old_menuid).attr("class","");
		sany_old_menuid = 	"anchor_"+menuId;
		$('#'+sany_old_menuid).attr("class","select");
		//$.dialog({ title:menutitle,width:740,height:560, content:'url:'+url,lock: true});
		window.open(url);	
	}	
	 	
			
}

function leftnavto_sany_MenuItem(tokenurl,url,target,options,title)
{
	var ppup = options?options.popup:false;
	var winopen = options?options.winopen:false;
	var maxstate = options ?(options.maxstate === true || options.maxstate === false?options.maxstate :true):true;
	var width = options && options.width ?options.width:777;
	var height = options && options.height ?options.height:500;
	//pageCounterDetail(appName_log,url.split('?')[0],userName_log,"");//参数：1.系统名称，2.功能的url，3.用户名，4.功能编码
	//"../token/getToken.freepage"
	if(!ppup)
	{
		$('#'+target).attr("src",url);//打开带令牌的url对应的页面
	}
	else
	{
		if(!winopen)
		{
			$.dialog({ title:title,width:width,height:height, content:'url:'+url,lock: true,maxState:maxstate});
		}
		else
		{
			window.open(url, "");
		}
		 
	}
}
//以下代码是拿到用户名和系统名称
var js_log = document.getElementsByTagName("script");  
var userName_log ;
var appName_log ;
for (var i = 0; i < js_log.length; i++) {  
    if (js_log[i].src.indexOf("menu.js") >= 0) {  
        var arraytemp = new Array();  
        arraytemp = js_log[i].src.split('?');  
        arraytemp = arraytemp[1].split('&');
        var param_0 = arraytemp[0].split('=');
        var param_1 = arraytemp[1].split('=');
        
        appName_log = param_0[1];
        userName_log = param_1[1];
        if(arraytemp.length == 3)
        {
	        var param_2 = arraytemp[2].split('=');
	        sany_old_menuid = "anchor_"+param_2[1];
        }
    }  
}  