
//获取系统待办
function getPending(user,ctx){
	// 添加random参数，防止json数据参不变的情况下，直接返回的页面缓存结果。
	var datas = {		
		random : Math.random()
	};   
	_url = ctx +'/sanymbp/getHrmPending.page'
	$.ajax({
		url:_url,
		type:'POST',
		dataType:'json',
		data:datas ,
		error:function(){
			alert('错误请求！');
		},
		success:function(json){
			if(json.hrmTaskCount!=''){
				$("#numhrm").html(json.hrmTaskCount);
				$("#pendhrm").show();
			}
			
		}
	});
	
	_url = ctx +'/sanymbp/getGspPending.page'
	$.ajax({
		url:_url,
		type:'POST',
		dataType:'json',
		data:datas ,
		error:function(){
			alert('错误请求！');
		},
		success:function(json){
			
			if(json.gspTaskCount!=''){
				$("#numgsp").html(json.gspTaskCount);
				$("#pendgsp").show();
			}
			
		}
	});
	
	_url = ctx +'/sanymbp/getEMSPending.page'
	$.ajax({
		url:_url,
		type:'POST',
		dataType:'json',
		data:datas ,
		error:function(){
			alert('错误请求！');
		},
		success:function(json){
			
			if(json.emsTaskCount!=''){
				$("#numems").html(json.emsTaskCount);
				$("#pendems").show();
			}
		}
	});
	
	//$.getJSON("http://10.8.131.138:8080/mbp/sanymbp/getPending.page?jsoncallback=?", data, 
	//function(json){ 
	//	if(json.hrmTaskCount!=''){
			//alert("hrmTaskCount: " + json.hrmTaskCount+" error " +json.error);
	//	}
	//});
}