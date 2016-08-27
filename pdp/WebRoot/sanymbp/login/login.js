//页面加载加入
jQuery(document).ready(function() { 
	//页面输入框校验
	$("#loginform").validate();
	//提交登录
	$("#loginsub").click( function () { 
		$("#loginform").submit(); 
	}); 
});
