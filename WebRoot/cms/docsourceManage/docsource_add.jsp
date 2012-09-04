<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.docsourcemanager.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%
	Docsource docsource= new Docsource();
	docsource.setSRCNAME(request.getParameter("srcname"));
	docsource.setSRCDESC(request.getParameter("srcdesc"));
	docsource.setSRCLINK(request.getParameter("srclink"));
	AccessControl acssCtrl = AccessControl.getInstance();
	acssCtrl.checkAccess(request,response);
	docsource.setCRUSER(Integer.parseInt(acssCtrl.getUserID()));
	
	if(!request.getParameter("srcname").equals("")){
		DocsourceManager dm = new DocsourceManagerImpl();
		boolean b=dm.creatorDsrc(docsource);
		if(b==false){
			%><script language="javascript" >alert("稿源名称有重复！");</script><meta http-equiv='refresh' content='2;url=<%=request.getContextPath()%>/cms/docsourceManage/docsource_creator.jsp'><%
		}else{
			%><script language="javascript" >alert("文档来源添加成功,正在处理您的提交信息，稍后自动返回！");</script><meta http-equiv='refresh' content='2;url=<%=request.getContextPath()%>/cms/docsourceManage/docsource_list.jsp'><%
		}
	}else{
		if(request.getParameter("srcname").equals("") && request.getParameter("cruser").equals("")){
			%><script language="javascript" >alert("文档名称处和创建者处不能为空！");</script><meta http-equiv='refresh' content='2;url=<%=request.getContextPath()%>/cms/docsourceManage/docsource_creator.jsp'><%
		}else{
			if(request.getParameter("srcname").equals("")){
				%><script language="javascript" >alert("文档名称处不能为空！");</script><meta http-equiv='refresh' content='2;url=<%=request.getContextPath()%>/cms/docsourceManage/docsource_creator.jsp'><%
			}else{
				%><script language="javascript" >alert("创建者处不能为空！");</script><meta http-equiv='refresh' content='2;url=<%=request.getContextPath()%>/cms/docsourceManage/docsource_creator.jsp'><%
			}
		}
	}
%>