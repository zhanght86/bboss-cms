
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(request,response);
	
	session.setAttribute("roleTabId", "1");
	
	//当前用户是否拥有超级管理员权限与部门管理员权限
	boolean isAdminOrOrgManager = false;
	//是否是管理员
	boolean isOrgManager = new OrgAdministratorImpl().isOrgManager(accessControl.getUserID());
	boolean isAdmin = accessControl.isAdmin();
	if(isAdmin || isOrgManager){
		isAdminOrOrgManager = true;
	}	
%>
<html>
	<head>
		<title>属性容器</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/treeview.css">		
		<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
		<script type="text/javascript">
			/**查询出来的节点集合**/
			var a = document.getElementsByTagName("a");
			/**保存定位到的元素集合**/
			var eleArray = new Array();
			/*模糊查询下一条记录*/
			var next = 0;
			
			function blurryQuary(){
				var count = 0;
				var blurryValue = document.all("blurryValue").value;
				if(blurryValue == ""){
					alert("查找内容不能为空!");
					return ;
				}
				for(var i = 0; i < a.length; i++){
				// && !(a[i].name.indexOf("type") != -1)
					if(a[i].name != "" && a[i].name != "0" && !(a[i].name.indexOf("type") != -1)
							 && !(a[i].name.indexOf("type") != -1)){
						document.all(a[i].name).parentElement.childNodes[0].style.backgroundColor="";
						if(a[i].innerText.indexOf(blurryValue) != -1){
							eleArray[count] = a[i].name;
							count ++;
						}
					}
				}
				if(eleArray.length > 0){
				/**保存所有父节点信息**/
				    var parentEle = new Array();
					var node = document.all("div_"+eleArray[0]);
					if(node.id == "div_parent_0"){
						document.all(eleArray[0]).parentElement.childNodes[0].style.backgroundColor="cornflowerblue";
						document.all(eleArray[0]).parentElement.focus();
						document.all.queryBtn.style.display="none";
				 		document.all.nextBtn.style.display="";
				 		next ++;
			 		}else{
			 			for(var i = 0; i < 100; i++){
					      	node = node.parentElement;
					      /**父节点有ID表示存在**/
					    	if( node.id != "" && node.id != "div_parent_0" ){
					     		parentEle[i] = node.id;
					    	}else{
					     		/**退出循环**/
					     		break;
					    	}
			 			}
			 		}
			 		
			 		for( var i = parentEle.length-1; i >=0 ; i --){
			 			if(Element.visible( parentEle[i])){
				   		
				   		}else{
				   		//alert(parentEle[i].substring(11,parentEle[i]));
				   		   document.all('icon_'+parentEle[i].substring(11,parentEle[i].length)).click();
				   		}
			
				   		/**判断是否到达最后一个**/ 
				   		if( i == 0 ){
				   			document.all(eleArray[0]).parentElement.childNodes[0].style.backgroundColor="cornflowerblue";
							document.all(eleArray[0]).parentElement.focus();
							document.all.queryBtn.style.display="none";
					 		document.all.nextBtn.style.display="";
					 		next ++;
				   		}
			 		}
			 		
				}else{
					onBlurryQueryChange();
					alert("没有找到您需要的相关信息！");
					return;
				}
				
			}
			
			/**文本属性改变**/
			function onBlurryQueryChange(){
			 count = 0;
			 eleArray.length = 0;
			 document.all.queryBtn.style.display="";
			 document.all.nextBtn.style.display="none";
			}
			
			function nextNodes(){
				if(eleArray.length > next){
					document.all(eleArray[next-1]).parentElement.childNodes[0].style.backgroundColor="";
					
					var parentEle = new Array();
					var node = document.all("div_"+eleArray[next]);
					if(node.id == "div_parent_0"){
						document.all(eleArray[next]).parentElement.childNodes[0].style.backgroundColor="cornflowerblue";
						document.all(eleArray[next]).parentElement.focus();
						document.all.queryBtn.style.display="none";
				 		document.all.nextBtn.style.display="";
				 		next ++;
			 		}else{
			 			for(var i = 0; i < 100; i++){
					      	node = node.parentElement;
					      /**父节点有ID表示存在**/
					    	if( node.id != "" && node.id != "div_parent_0" ){
					     		parentEle[i] = node.id;
					    	}else{
					     		/**退出循环**/
					     		break;
					    	}
			 			}
			 		}
			 		
			 		for( var i = parentEle.length-1; i >=0 ; i --){
			 			if(Element.visible( parentEle[i])){
				   		
				   		}else{
				   		   document.all('icon_'+parentEle[i].substring(11,parentEle[i].length)).click();
				   		}
			
				   		/**判断是否到达最后一个**/ 
				   		if( i == 0 ){
				   			document.all(eleArray[next]).parentElement.childNodes[0].style.backgroundColor="cornflowerblue";
							document.all(eleArray[next]).parentElement.focus();
							document.all.queryBtn.style.display="none";
					 		document.all.nextBtn.style.display="";
					 		next ++;
				   		}
			 		}
			 		
				}else{
					alert("查询完成！");
					onBlurryQueryChange();
					next = 0;
				}
			}
			
			function enterKeydowngo(){ 
				if(window.event.keyCode == 13){
					if(document.all.queryBtn.style.display==""){
						blurryQuary();
					}else{
						nextNodes();
					}
				}
			}
		
		</script>
	</head>
	<body class="contentbodymargin" scroll="no">
	<%if(isAdminOrOrgManager){ %>
		
		
				<form name="OrgJobForm" action="" target="hiddenFrame" method="post">
				<div align="center">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table">
						<tr>
							<td>
								<div align="center">
									<a href="roleQuery.jsp" target="base_properties_content">角色查询</a>
									&nbsp;&nbsp;
									<%
									ResourceManager resManager = new ResourceManager();
									String resId = resManager.getGlobalResourceid(AccessControl.ROLE_RESOURCE);
									if (resId != null && !resId.equals("") && accessControl.checkPermission(resId,
                       							"add", AccessControl.ROLE_RESOURCE))
									{
									%>
									<a href="newRole.jsp" target="base_properties_content">新增角色</a>
									<%}%>
								</div>
							</td>
						</tr>
						  <tr>
						<td align="left">
						<input name="blurryValue" type="text" onpropertychange="onBlurryQueryChange()" onkeydown="enterKeydowngo()"/>
						</td>
					</tr>
					<tr>
					<td align="right">
						<input type="button" name="queryBtn" value="查询" onclick="blurryQuary()"  class="input">
						<input style="display:none" name="nextBtn" type="button" value="查找下一个" onclick="nextNodes()" class="input">
						</td>
					</tr>
					</table>
				</div>
				<div id="contentborder" style="width=100%;height:89%;overflow:auto">
					<table >					
						<tr>
							<td>
								<tree:tree tree="role_tree"
				    	           node="role_tree.node"
				    	           imageFolder="../../images/tree_images/"
				    	           collapse="true"
				    			   includeRootNode="true"    			   
				    			   href="../../accessmanager/role/properties_toolbar.jsp"    			   
				    			   target="base_properties_toolbar"
				    			   mode="static-dynamic"
				    			   >  				
				    			 <tree:treedata treetype="RoleTree"
		    	                   scope="request"
		    	                   rootid="0"  
		    	                   rootName="角色"
		    	                   expandLevel="1"
		    	                   showRootHref="false"
		    	                   needObserver="false"
		    	                   />
				    			 </tree:tree>
							</td>
						</tr>
					</table>	
					</div>				
				</form>
		<%}else{ %>
		<div align="center">没有权限！</div>
		<%} %>
		<iframe name="hiddenFrame" width="0" height="0"></iframe>
	</body>
</html>
