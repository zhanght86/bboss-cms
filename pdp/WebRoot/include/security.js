/* 
jquery安全表单提交插件
 */ 


// the actual Flot code
(function($) {
    function Secutiry() {
        function dosubmit(form,url, target,context)
        {
        	
					var form1 = document.getElementById(form);
					form1.action=url;
					if(target)
						form1.target=target;
					form1.submit();	
			
        }
        
        function dosubmitwithreset(form,url, target,context)
        {
        	
					var form1 = document.getElementById(form);
					form1.action=url;
					if(target)
						form1.target=target;
					form1.submit();	
					form1.reset();
			
        }
        
        function dosubmitwithtoken(form,url, target,context)
        {
        	var tokenurl = context +"/token/getParameterToken.freepage";
        	$.ajax({url:tokenurl, //指定申请令牌的url
				type: "POST",
				success : function(token){//成功申请令牌，token格式为_dt_token_=1518435257
					if(token )//将令牌作为请求参数附加到url中
					{
						if(url.indexOf("?") > 0)
						{
							url = url + "&"+token;
						}
						else
						{
							url = url + "?"+token;
						}
					}
					var form1 = document.getElementById(form);
					form1.action=url;
					if(target)
						form1.target=target;
					form1.submit();	
				}
			});
        }
        
        function dosubmitTokenwithreset(form,url, target,context)
        {
        	var tokenurl = context +"/token/getParameterToken.freepage";
        	$.ajax({url:tokenurl, //指定申请令牌的url
				type: "POST",
				success : function(token){//成功申请令牌，token格式为_dt_token_=1518435257
					if(token )//将令牌作为请求参数附加到url中
					{
						if(url.indexOf("?") > 0)
						{
							url = url + "&"+token;
						}
						else
						{
							url = url + "?"+token;
						}
					}
					var form1 = document.getElementById(form);
					form1.action=url;
					if(target)
						form1.target=target;
					form1.submit();	
					form1.reset();
				}
			});
        }
        var secutiry = this;
        secutiry.dosubmit = dosubmit;
        secutiry.dosubmitwithreset = dosubmitwithreset;
        secutiry.dosubmitwithtoken = dosubmitwithtoken;
        secutiry.dosubmitTokenwithreset = dosubmitTokenwithreset;
       
    }
    $.secutiry =  new Secutiry();
    
})(jQuery);
