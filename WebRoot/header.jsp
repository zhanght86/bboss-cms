<%@page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/jsp/importAndTaglib.jsp"%>
<%@page  import="com.frameworkset.platform.framework.MenuHelper"%>
<%@page  import="com.frameworkset.platform.framework.ModuleQueue"%>
<%@page  import="com.frameworkset.platform.framework.Module"%>
<%@page  import="com.frameworkset.platform.framework.Item"%>
<%@page  import="com.frameworkset.platform.framework.ItemQueue"%>
<%@page  import="com.frameworkset.platform.security.AccessControl"%>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>湖南省公安厅信息共享服务平台</title>
<link href="common/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="common/scripts/jquery-1.4.4.min.js"></script>
<!-- <script type="text/javascript" src="common/scripts/menu.js"></script>  -->
<script language="javascript" src="sysmanager/scripts/toolbar.js"></script>
<script language="javascript" src="sysmanager/common/CommonFunc.js"></script>
 
</HEAD> 
<script type="text/javascript">

$(document).ready(function() {
	//让第一个一级菜单默认打开
	var firstItem = $("#mainMenu li:first");	
	showitem(firstItem.attr('id'));
});


function showInit(){
	showitem('dataReuseConfigManage');
}

//缓存二级菜单
var moduleItems = new Array();

/**
 * 显示二级菜单
 * @param moduleId 一级菜单id
 */
function showitem(moduleId) {
	//$("#framesetid", window.parent.document).attr("rows", "103,*");
	
	//一级菜单链接所在li
	var cuerrentMenu = $('#' + moduleId);

	//当前选中的一级菜单切换显示样式
	cuerrentMenu.parent().find("li").removeClass("on");
	cuerrentMenu.parent().find("span").removeClass("nav_01");
	cuerrentMenu.addClass("on");
	cuerrentMenu.find("span").addClass("nav_01");


	//如果本一级菜单没有点击过，通过Ajax从后台获取二级菜单数据，然后通过js缓存
	if (!moduleItems[moduleId]){	
		
		$.ajax({
			type : "post",
			dataType : "json",
			data : {
				"moduleId" : moduleId
			},
			url : "menu/queryMenu.page",
			cache : false,
			async : false,// ajax同步标志修改
			error : function() {
				window.parent.locaction="logout.jsp";				
			},
			success : function(data) {
				//将返回的数据放入js缓存
				moduleItems[moduleId] = data;				
			}
		});
	}

	//将原来显示的tab移除
	$(".bj").children().remove();

	//从js缓存中读取二级菜单信息
	var data = moduleItems[moduleId];
	if (data.length > 0) {
		$.each(data, function(i) {
			
			var span = $("<span style=\"cursor:pointer;\" class=\"func\" id=\""
					+ this.id
					+ "Node"
					+ "\" title=\""
					+ this.name
					+ "\" dataType='iframe' dataLink='"
					+ this.pathU
					+ "' iconImg='images/msn.gif'>"
					+ this.name
					+ "</span>").click(function() {		
											
						//如果二级菜单项被点击，则触发主体内容窗口的addTab函数				
						var name = $(this).attr('title');
						var dataLink = $(this).attr('dataLink');
						addTab(name, dataLink);
					});
				
					var currentdiv = $("<div style=\"width:100px;float:left;margin-left:5px;\"><div style=\"text-align:center;\"><img src=\""
							+ this.imageUrl
							+ "\" width=\"25\" height=\"25\" style=\"cursor:pointer;\" on></div><div style=\"margin-top:1px;text-align:center;color:#000;\" id=\""
							+ this.id + "\"></div></div>");
					currentdiv.find("img").click(function() {
						currentdiv.find("span").click();							
					});
					$(".bj").append(currentdiv);
					$("#" + this.id).append(span);
					
		});
	}		
}

function addTab(name, dataLink){
	//主体内容窗口
	var contentWindow = parent.perspective_main.perspective_content.base_actions_container.base_properties_container.base_properties_content;	//content.jsp
	contentWindow.addTab(name, dataLink);
}
</script>
</head>
<body id="home">
<table width="100%" border="0" cellspacing="0" cellpadding="0" id="bj">
  <tr>
    <td><table border="0" cellspacing="0" cellpadding="0" class="top">
      <tr>
        <td width="315"><img src="common/images/header_03.gif" width="315" height="34"></td>
        <td class="text_right"><table width="20%" border="0" align="right" cellpadding="0" cellspacing="0">
          <tr>
            <!--<td align="center"><a href="javascript:void">&nbsp;</a></td>
            <td align="center"><a href="javascript:void">&nbsp;</a></td>
            <td align="center"><a href="javascript:void">&nbsp;</a></td>
            <td align="center"><a href="javascript:void">&nbsp;</a></td>
            --><td align="center"><jsp:include page="accountbox.jsp" flush="true"></jsp:include></td>
            <td  align="center">&nbsp;</td>
          </tr>
        </table></td>
      </tr>
    </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="header">
        <tr>
          <td valign="top" class="logo">
		  <div class="mainNav" >
		  	<ul id="mainMenu">		 
		  	<%
		  	
			ModuleQueue moduleQueue =(ModuleQueue)request.getAttribute("moduleQueue");
			for(int i=0;i<moduleQueue.size();i++){
				Module module = moduleQueue.getModule(i);
				String moduleName = module.getName();
				String moduleId = module.getId();
				out.println("<li id=\""+moduleId+"\"><span style=\"cursor:pointer;\"><a href=\"javascript:showitem('"+moduleId+"');\" title=\""+moduleName+"\">"+moduleName+"</a></span></li>");
			}
		  	 %> 
		  	
			 </ul>
			<div class="divLeft search_1">
			  <div class="bj" >				  
			  	</div>
			  </div>
		  </div>
		  </td>
        </tr>
      </table></td>
  </tr>
</table>
</body>

</html>

