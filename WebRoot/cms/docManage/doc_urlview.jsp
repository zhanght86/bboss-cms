<%
	/**
	 *  发布文档的浏览
	 *
	 */
%>

<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.cms.container.ContainerImpl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.DocumentManager"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl"%>
<%
	String siteid = request.getParameter("siteid");
	String channelid = request.getParameter("channelid");
	String docid = request.getParameter("docid");

	String urlstr = "";
	String docType = "";
	try
	{
		DocumentManager docManager = new DocumentManagerImpl();
		docType = docManager.getDocType(Integer.parseInt(docid)) + "";
		
		urlstr = ContainerImpl.getDocumentUrl(request,response,siteid,channelid,docid);
	}
	catch(Exception e)
	{
	}

%>
<SCRIPT LANGUAGE="JavaScript">
	var urlstr = "<%=urlstr%>";
	var docType = "<%=docType%>";
	
	function send_request(url){
		http_request = false;
		//开始初始化XMLHttpRequest对象
		if(window.XMLHttpRequest){//Mozilla
			http_request = new XMLHttpRequest();
			if(http_request.overrideMimeType){//设置MIME类别
				http_request.overrideMimeType("text/xml");						
			}
		}
		else if(window.ActiveXObject){//IE
			try{
				http_request = new ActiveXObject("Msxml2.XMLHTTP");
			}catch(e){
				try{
					http_request = new ActiveXObject("Microsoft.XMLHTTP");							
				}catch(e){
				}
			}
		}
		if(!http_request){
			alert("不能创建XMLHttpRequest对象");
			return false;
		}
		//http_request.onreadystatechange = processRequest;
		http_request.open("GET",url,false);
		http_request.send();
		if(http_request.readyState == 4){
		
			if(http_request.status == 200){
				return true;
			}
			else{
				return false;
			}
		}
	}
	
	if(docType == "0")
	{
		if(urlstr == "" || !send_request(urlstr))
		{
			alert("文档还未发布，请先发布文档后再浏览！");
			window.close();
		}
		else
		{
			document.location.href = urlstr;
		}
	}
	else if(docType == "1")
	{
		document.location.href = urlstr;
	}
	
	
</SCRIPT>