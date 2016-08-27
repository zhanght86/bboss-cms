<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.TemplateStyleManager" %>
<%@ page import="com.frameworkset.platform.cms.templatemanager.TemplateStyleManagerImpl" %>
<%@ page import="com.frameworkset.platform.cms.templatemanager.TemplateStyleInfo" %>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String commenterName = accesscontroler.getUserName();
	
	int successflag = 0;
	String closeWinFlag = "0";         //关闭窗口标志
	
	String action = request.getParameter("action");
	//模板风格处理
	TemplateStyleManager tsm = new TemplateStyleManagerImpl();
	try{
		if("add".equals(action)){
			TemplateStyleInfo tsi = new TemplateStyleInfo();
			tsi.setStyleName(request.getParameter("styleName"));
			tsi.setStyleDesc(request.getParameter("styleDesc"));
			
			if(tsm.addTemplateStyle(tsi)){
				closeWinFlag = "1";
			} else {
				closeWinFlag = "0";
			}
			
			successflag = 1; 
		}else if("delete".equals(action)){
			String[] styleIds = request.getParameter("ids").split(":");
			if(styleIds!=null){
				if(tsm.delTemplateStyles(styleIds)){
					closeWinFlag = "1";
				} else {
					closeWinFlag = "0";
				}
			}
			successflag = 1; 
		}else if("edit".equals(action)){
			String styleId = request.getParameter("styleId");
			TemplateStyleInfo tsi = new TemplateStyleInfo();
			tsi.setStyleName(request.getParameter("styleName"));
			tsi.setStyleDesc(request.getParameter("styleDesc"));
			if(tsm.updateTemplateStyle(styleId,tsi)){
				closeWinFlag = "1" ;
			} else {
				closeWinFlag = "0" ;
			}
			successflag = 1; 
		}else if("saveOrder".equals(action)){
			String strStyleIds = request.getParameter("styleIds");
			String strOrderNums = request.getParameter("orderNum");
			if(tsm.updateTemplateStyleOrder(strStyleIds,strOrderNums)){
				closeWinFlag = "1";
			} else {
				closeWinFlag = "0";
			}
			
			successflag = 1; 
		}
	}catch(Exception d){
		d.printStackTrace();
	}
%>
<%		 
	if(successflag == 1){
%>
		<script language = "javascript">
			if("<%=closeWinFlag%>"=="1"){
				alert("操作成功！");
				window.close();
				window.returnValue="cf";
			} else{
				alert("操作失败！您所添加的数据出了重复！");
			}
		</script>
<%
	}else if(successflag == 0){
%>
		<script language="javascript">
			if("<%=action%>"=="withdrawPub"){
				alert("处于未审状态的文档不能撤销发布，请检查！！");
			} else{
				alert("数据库操作失败！");
				if("<%=action%>"=="add")
					window.close();
			}
		</script>
<%
	}
%>