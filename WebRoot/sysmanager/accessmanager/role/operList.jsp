<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OperManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Operation,com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%
	String resTypeId = request.getParameter("resTypeId");
	String resId = request.getParameter("resId");
	String roleId = (String)session.getAttribute("currRoleId");
	if(resTypeId == null){
		resTypeId = (String)request.getAttribute("resTypeId");
	}
	if(resId == null){
		resId = (String)request.getAttribute("resId");
	}
	
	OperManager operManager = SecurityDatabase.getOperManager();
	ResourceManager resManager = new ResourceManager();

	List list = resManager.getOperations(resTypeId);
	//List list = operManager.getOperList(resTypeId);
	
	//List hasOper = operManager.getOperList(roleId,resId,resTypeId);
	List hasOper = operManager.getOperResRoleList(roleId,resId,resTypeId);
	
	if(list == null){
		list = new ArrayList();
	}	
	request.setAttribute("operList",list);
	if(hasOper == null){
		hasOper = new ArrayList();
	}
	//判断是不是保存后的页面
	String p = (String)request.getAttribute("flag");
	if(p == null){
		p = "0";
	}
%>

<html>
<head>    
  <title>属性容器</title>
  <%@ include file="/include/css.jsp"%>
  <link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
  <link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
<SCRIPT LANGUAGE="JavaScript"> 
function add(){
	var f = <%=p%>;
	
	//var ss=document.Form1.alloper;
	//var len =ss.length;
	//var s = 0;
	//for (var i=0;i<len;i++){
	//	ss[i].disabled=false
    //   if(ss[i].checked){
	//		s = 1;
    //    }
    //}
    if(f == 0){
    	document.forms[0].action="../../../accessmanager/roleManager.do?method=storeOperList&resId=<%=resId%>&resTypeId=<%=resTypeId%>";
    }
    else{ 
    	document.forms[0].action="../accessmanager/roleManager.do?method=storeOperList&resId=<%=resId%>&resTypeId=<%=resTypeId%>";
    }
    document.forms[0].submit();
}

function setCheck(currCheck,priority)
{
   	var o = document.getElementsByName("alloper");
	var prioritylist = document.getElementsByName("priority"); 
	if (currCheck.checked==true && priority.length >1 && (priority.match(/[0-9]/))){
		for (var i=0;i<prioritylist.length;i++){
			var v = prioritylist[i].value;

			if (v.length >1 && (v.match(/[0-9]/)) && priority.substring(0,1) > v.substring(0,1) )
			{
				o[i].checked=true;
				//o[i].disabled=true;
			}
		}  
	
		for (var i=0;i<prioritylist.length;i++){
			var v = prioritylist[i].value;
			if (v.length >1 && (v.match(/[0-9]/)) && priority.substring(1,2) != v.substring(1,2) )
			{
				o[i].checked=false;
				//o[i].disabled=false
			}
		}  
	}	
	if (currCheck.checked==false  && priority.length >1 && (priority.match(/[0-9]/))){
		for (var i=0;i<prioritylist.length;i++){
			var v = prioritylist[i].value;
			if ( v.length >1 && (v.match(/[0-9]/)) && priority.substring(0,1) > v.substring(0,1) )
			{
				if ( o[i].checked==true ){
					//currCheck.checked==true;
					//o[i].disabled=false;
				}
			}
		}  

	}		
}

</SCRIPT>
  <%@ include file="/include/css.jsp"%>
<body class="contentbodymargin">
<div id="contentborder">
<center>
<form name="Form1" action="" method="post" >
<table width="100%" height="22" border="0" cellpadding="0" cellspacing="2" class="table">
      <tr class="tr">
        <td class="td" width="25" height="22" >&nbsp;</td>
        <td class="td" width="100" height="22" >授予操作项</td>
        <td height="22" class="td" >&nbsp;</td>
      </tr>
      <pg:list requestKey="operList" needClear="false">
      <tr class="tr">
        <td width="25" class="td" height="22"><input name="alloper" type="checkbox"
        <%
        	String opId = dataSet.getString(Integer.parseInt(rowid),"id");
        	for(int i = 0; i < hasOper.size(); i ++)
        	{
        		Roleresop op = (Roleresop)hasOper.get(i);
        		if(op.getId().getOpId().equals(opId))
        		{
        			out.println("checked");
        			break;
        		}
        			
        	}
        %>
        value="<pg:cell colName="id"/>" onclick=setCheck(this,'<pg:cell colName="priority" defaultValue=""/>')></td>
        <td width="80" height="22" class="td"><pg:cell colName="name"/></td>
        <td height="22">&nbsp;</td>
        <input name="priority" value="<pg:cell colName="priority" defaultValue=""/>" type="hidden">        
      </tr>
       </pg:list>
    </table>
    <table width="35%" border="0" align="right" cellpadding="0" cellspacing="0" >
        <tr>
          <td><div align="center">
            <input name="mit" type="button" onclick="add()" class="input" value="保存">
          </div></td>
          <td><div align="center">
            <input name="ss" type="reset" class="input" value="重置">
          </div></td>
        </tr>
    </table>
</form>
</center>
</div>
<%@ include file="../../sysMsg.jsp"%>
</body>
</html>

