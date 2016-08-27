<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	String channelname = request.getParameter("channelName");
	String siteid = request.getParameter("siteid");
	String channelId = request.getParameter("channelId");
	//System.out.println(channelname);
	//System.out.println(siteid);
	SiteManager siteManager = new SiteManagerImpl();
	String sitename = siteManager.getSiteName(siteid);
				
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>内容管理主框架</title>
<script src="inc/js/func.js"></script>
<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
<script language="javascript" >
 	function checkAll(totalCheck,checkName){	//复选框全部选中
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
	   if(selectAll[0].checked==true){
		   for (var i=0; i<o.length; i++){
	      	  if(!o[i].disabled){
	      	  	o[i].checked=true;
	      	  }
		   }
	   }else{
		   for (var i=0; i<o.length; i++){
	   	  	  o[i].checked=false;
	   	   }
	   }
	}
	//单个选中复选框
	function checkOne(totalCheck,checkName){
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
		var cbs = true;
		for (var i=0;i<o.length;i++){
			if(!o[i].disabled){
				if (o[i].checked==false){
					cbs=false;
				}
			}
		}
		if(cbs){
			selectAll[0].checked=true;
		}else{
			selectAll[0].checked=false;
		}
	}		
	
	function queryUser()
	{	
		//查询
		form1.action="<%=rootpath%>/cms/docManage/garbage_list.jsp?flag=1&siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>"
		form1.submit();	
	}
	
	//还原文档
		function dealRecord(dealType) {
	    var isSelect = false;
	    var outMsg;
	    
	    for (var i=0;i<form1.elements.length;i++) {
			var e = form1.elements[i];
			
			if (e.name == 'ID'){
				if (e.checked){
			       		isSelect=true;
			       		break;
			    }
			}
	    }
	    if (isSelect){
	    	if (dealType==1){
	    		outMsg = "你确定要还原所选文档吗？";
	        	if (confirm(outMsg)){
	        	form1.action="<%=rootpath%>/cms/channelManage/delete_doc.jsp?flag=2&siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>";
				form1.submit();
		 		return true;
			    }
			} 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	   }
  		return false;
	}
		//彻底删除文档
		function deletedoc(dealType) {
	    var isSelect = false;
	    var outMsg;
	    
	    for (var i=0;i<form1.elements.length;i++) {
			var e = form1.elements[i];
			
			if (e.name == 'ID'){
				if (e.checked){
			       		isSelect=true;
			       		break;
			    }
			}
	    }
	    if (isSelect){
	    	if (dealType==1){
	    		outMsg = "你确定要删除所选文档吗？(删除后是不可以再恢复的)。";
	        	if (confirm(outMsg)){
	        	form1.action="<%=rootpath%>/cms/channelManage/delete_doc.jsp?flag=1&siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>&list=1";
				form1.submit();
		 		return true;
			    }
			} 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	   }
  		return false;
	}
</script>
<style type="text/css">
<!--
body {
	background-color: #ffffff;
}
.STYLE1 {color: #0000FF}
.STYLE2 {color: #000099}
.style3 {
	font-size: 14px;
	font-weight: bold;
	color: #3300FF;
}
-->
</style></head>

<body topmargin="3" rightmargin="0">
<form id="form1" name="form1" method="post" action="">
<table width="100%" border="0"   cellpadding="3" align="center" cellspacing="0">
  <tr>
    <td height="82" valign=top background="../images/pagebar.jpg" style="background-repeat:no-repeat">
    <table width="100%" border="0">
      <tr>
        <td height="23">&nbsp;&nbsp;&nbsp;&nbsp; 当前位置:<%=sitename%>站点  >>  <%=channelname%>频道</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
	   <tr>
       <td >
	   
	      <div align="right"><a href="<%=rootpath%>/cms/channelManage/channel_doc_list.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>&title=null&doclevel=null&status=null&doclevel=null&status=null&flag=2">返回</a>|  <a href="/creatorcms/cms/main.jsp">欢迎页</a></div></td>
       </tr>
    </table>
    </td>
  </tr>
<tr>
    <td  height="483" valign=top background="images/tile.jpg" bgcolor="#FFFFFF">
  <table class="mhwz_table" width="98%" align=center><tr><td> 
  <table width="100%" border="0" align="center" class="">
         				
         				
         				
         				<tr >
         					
           				    <td width="11%" height='30' align="center" valign='middle'>文档标题：</td>
       					    <td width="11%" height='30' align="center" valign='middle'>
       					   		 <div align="left">
       					   		   <input type="text" size="10" name="title" >
			   		      </div></td>
           					<td width="11%"  height='30' align="center" valign='middle'>发稿人：</td>
           					<td width="11%"  height='30' align="left" valign='middle'>
           					 	 <input type="text" size="10" name="userid" >
							</td>
           					<td width="11%"  height='30' align="center" valign='middle'>文档类型:</td>
           					<td width="39%"  height='30' align="center" valign='middle'>
	           					 <div align="left">
	           					   <select name="doctype">
           					         <option value=""></option>
           					         <option value="0">普通文档</option>
           					         <option value="1">外部链接</option>
           					         <option value="2">外部文件</option>
       					           </select>
   					          </div></td>
           					<td width="1%"  height='30' align="center"valign='middle'>&nbsp;	
           					</td>
           					<td width="5%"  height='30' align="left"valign='middle'>&nbsp;
           					</td>
         				</tr>
						
         				<tr >
         					
           				    <td height='30'valign='middle' align="center">文档状态：</td>
           					<td height='30'valign='middle' align="left">
           					<select name="status">
           					  <option value=""></option>
           					  <option value="1">新稿</option>
           					  <option value="2">已审</option>
           					  <option value="3">返工</option>
           					  <option value="4">已发</option>
           					  <option value="5">已回收</option>
           					  <option value="6">已归档</option>
                             </select></td>
           					<td  height='30'valign='middle' align="center">优先级：</td>
           					<td  height='30'valign='middle' align="left">
           					
           						 <select name="doclevel">
	           					  <option value=""></option>
	           					  <option value="0">普通</option>
	           					  <option value="1">高级</option>
	           					  <option value="2">最高级</option>
           					  </select>
           					</td>
           					<td  height='30' colspan="4" align="center"valign='middle'>
           						 <div align="right">
        					       <input name="search" type="button" class="cms_button" value="查询" onClick="queryUser()">
<!--        					       <input type="button" value="还原所选文档" class="cms_button"-->
<!--									onclick="javascript:dealRecord(1); return false;">-->
<!--									 <input type="button" value="删除所选文档" class="cms_button"-->
<!--									onclick="javascript:deletedoc(1); return false;">		-->
<!--         					       </div>-->
       					  </td>
       					</tr>
						<tr>
         					<td  height='30' colspan="2" align="left" valign='middle' class="cms_title_blue"> <%=channelname%>_的回收站列表
         					</td>
       					    <td  height='30' colspan="6" align="left" valign='middle'>
							  <a  style="cursor:hand" onClick="javascript:dealRecord(1); return false;"><u><img src="../images/del.gif">还原所选文档</u></a>
         					  <a  style="cursor:hand" onClick="javascript:deletedoc(1); return false;"><u><img src="../images/del.gif">删除所选文档</u></a>         					
							</td>
   					    </tr>
   			  </table>
     			</td></tr></table>    			
         			
         				
						<table  width="100%" border="1" align=center cellpadding="3" cellspacing="0" class="Datalisttable">
         				<pg:listdata dataInfo="Doc_GarbageList" keyName="Doc_GarbageList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="10" scope="request" data="Doc_GarbageList" isList="false">
							<tr class="cms_report_tr">
								<!--设置分页表头-->
								
								 <td width="5%" align=center>
								  	<input class="checkbox" type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">
								 </td>
								<td   width="11%" >文档类型</td>
								
								<td   width="40%" >文档标题</td>
								<td   width="8%" >所属频道</td>
								<td   width="8%" >状态</td>								
								<td   width="10%" >发稿人</td>
								<td   width="8%" >作者</td>
								<td   width="10%" >发稿日期</td>
							</tr>
					
							<pg:param name="title" />
							<pg:param name="doclevel" />
							<pg:param name="status" />
							<pg:param name="userid" />
							<pg:param name="doctype" />
							<!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center'> 该回收站暂时没有文档 </td>
								</tr>
							</pg:notify>

							<!--list标签循环输出每条记录-->
							<pg:list>
								
								<tr class="cms_data_tr">
									<td class="tablecells" align=center><input class="checkbox" onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="document_id" defaultValue=""/>"></td>					

									<td  class="tablecells" align=center>
										<pg:equal colName="doctype" value="0"><div align="center"><img  title="HTML文档" src="../images/doc_html.gif" width="20" height="20"></div>
										</pg:equal>
										<pg:equal colName="doctype" value="1"><div align="center"><img  title="外部链接类型文档" src="../images/doc_link.gif" width="20" height="20"></div>
										</pg:equal>
										<pg:equal colName="doctype" value="2"><div align="center"><img title="下载文件类型" src="../images/doc_downloadfile.gif" width="15" height="16"></div>
										</pg:equal>
										
									</td>
								
									<td class="tablecells" nowrap="nowrap">
										<pg:equal colName="doctype" value="0"><a href=/creatorcms/cms/docManage/doc_gather_update.jsp?docid=<pg:cell colName="document_id" defaultValue=""/>&siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>><pg:cell colName="subtitle" defaultValue="" /></a>
										</pg:equal>
										<pg:equal colName="doctype" value="1"><a href=/creatorcms/cms/docManage/doc_gather_links_update.jsp?docid=<pg:cell colName="document_id" defaultValue=""/>&siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>><pg:cell colName="subtitle" defaultValue="" /></a>
										</pg:equal>
										<pg:equal colName="doctype" value="2"><a href=/creatorcms/cms/docManage/doc_gather_filedownload_update.jsp?docid=<pg:cell colName="document_id" defaultValue=""/>&siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>><pg:cell colName="subtitle" defaultValue="" /></a>
										</pg:equal>
									</td>
									<td class="tablecells" nowrap="nowrap" width="16%">
										<%=channelname%>
									</td>
									<td class="tablecells" nowrap="nowrap" width="10%">
										
										<pg:equal colName="status" value="1">新稿</pg:equal>
										<pg:equal colName="status" value="2">已审</pg:equal>
										<pg:equal colName="status" value="3">返工</pg:equal>
										<pg:equal colName="status" value="4">已发</pg:equal>
										<pg:equal colName="status" value="5">已回收</pg:equal>
										<pg:equal colName="status" value="6">已归档</pg:equal>
									</td>
									
									<td class="tablecells" nowrap="nowrap" width="10%">
										<pg:cell colName="username" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap" width="10%">
										<pg:cell colName="author" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap" width="10%">
										<pg:cell colName="createTime" defaultValue="" />
									</td>
								</tr>
							</pg:list>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=10 align='center'>
									<pg:index />
								</td>
								
					      </tr>
							<input name="queryString" value="<pg:querystring/>" type="hidden">
						</pg:pager>

	    </table>
 </td></tr>
</table> </form>

</body>
</html>
