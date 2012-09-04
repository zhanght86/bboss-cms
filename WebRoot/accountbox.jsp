<%@page contentType="text/html;charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
    AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request,response);
    
    String title = "";
	if(accesscontroler.isAdmin() || accesscontroler.isOrgManager(accesscontroler.getUserAccount())){
		title = "在线用户管理"; 
  	}else{
  		title = "会话信息管理";
  	}
%>
<script language="javascript">


var http_request_account = false;
var flag = true;
//初始化，指定处理的函数，发送请求的函数
function send_request_account(url){

	if(!flag)
	   return ;
	http_request_account = false;
	//开始初始化XMLHttpRequest对象
	if(window.XMLHttpRequest){//Mozilla
		http_request_account = new XMLHttpRequest();
		if(http_request_account.overrideMimeType){//设置MIME类别
			http_request_account.overrideMimeType("text/xml");						
		}
	}
	else if(window.ActiveXObject){//IE
		try{
			http_request_account = new ActiveXObject("Msxml2.XMLHTTP");
		}catch(e){
			try{
				http_request_account = new ActiveXObject("Microsoft.XMLHTTP");							
			}catch(e){
			}
		}
	}
	if(!http_request_account){
		alert("不能创建XMLHttpRequest对象");
		return false;
	}
	http_request_account.onreadystatechange = processRequest_account;
	http_request_account.open("GET",url,true);
	http_request_account.send(null);
}

function processRequest_account(){

	if(http_request_account.readyState == 4){
		
		
		if(http_request_account.status == 200){
		
			var conts  = http_request_account.responseText;
			count_object = document.getElementById("user_counts");
			//alert(conts);
			count_object.innerHTML = "<font color=blue><%=title%></font>&nbsp;";
		}
		else{
			//alert("对不起，服务器错误");
		}
	}
}
function logout()
{
	var msg = "^_^温馨提示：离开系统前请保存已操作的数据";
	if(confirm(msg)){
	    parent.location='logout.jsp';
	    flag = false;
    }
}
function list()
{
   onLine = window.showModalDialog('sysmanager/onLineUser.jsp',window,"dialogWidth:"+(950)+"px;dialogHeight:"+(800)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0;");
   if(onLine == "ok")
   {
   		send_request_account('sysmanager/count.jsp');
   }
}

//window.setInterval("send_request_account('sysmanager/count.jsp')",300000);
</script>
<form name="myform" action="" method="post">		
	<span class="blue1"> <%=accesscontroler.getUserName()%>(<%=accesscontroler.getUserAccount()%>)</span>，欢迎您　<a href="#" class="zhuxiao" onclick="logout()" >注销</a>　 <a class="blue">帮助</a>
</form>

