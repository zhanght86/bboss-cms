<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<html>
<head>    
 <title></title>

<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<script language="JavaScript">


	var jsAccessControl = new JSAccessControl("#ff0000","#ffffff","#eeeeee");
	function goBack(){		
		document.location.href="../schedularmanage/daySchedular.jsp";
	}
	function validateOnlyCheck(checkName)
	{
		 var o = document.all.item(checkName);
		 for (var i=0,j=0; o && o.length && i<o.length; i++){
	   	  	if(o[i].checked)
	   	  	{
	   	  		j++;
	   	   	}
	   	   	if(j == 2)
	   	   	{
	   	   		return false;
	   	   	}
	   	   }
		 return true;
	}
	
	function validatecheck(checkName)
	{
			 var o = document.all.item(checkName);
		for (var i=0; o && o.length && i<o.length; i++){
	   	  	  if(o[i].checked)
	   	  	  	return true;
	   	   }
	   	   if(o && !o.length)
			{
				if(o.checked)
					return true;
			}
			return false;
	}
	function modify(checkName){
		if(!validatecheck(checkName))
		{
			alert("请选择要修改的日程");
			return;
		}		
		if(!validateOnlyCheck(checkName))
		{
			alert("一次只能选择一个日程进行修改");
			return;
		}
		
		document.forms[0].action="../schedularmanage/schManager.do?method=getModifySchedular&path=latest";
		document.forms[0].submit();
	}
	function view(checkName){	
		if(!validatecheck(checkName))
		{
			alert("请选择要查看的日程");
			return;
		}	
		if(!validateOnlyCheck(checkName))
		{
			alert("一次只能选择一个日程进行修改");
			return;
		}
		
		document.forms[0].action="../schedularmanage/schManager.do?method=getSchedularAndRemind&path=latest";
		document.forms[0].submit();
	}
	function history(checkName){
		if(!validatecheck(checkName))
		{
			alert("请选择要放入历史的日程");
			return;
		}
		document.forms[0].action="../schedularmanage/schManager.do?method=history&path=latest";
		document.forms[0].submit();
	}
	function checkAll(totalCheck,checkName){	//复选框全部选中
	   var selectAll = document.all.item(totalCheck);
	   
	   var o = document.all.item(checkName);
	   
	   if(selectAll.checked==true){
		   for (var i=0; o && o.length && i<o.length; i++){
	      	  if(!o[i].disabled){
	      	  	o[i].checked=true;
	      	  }
		   }
		   if(o && !o.length && !o.checked)
			{
				o.checked=true;
			}
		   
	   }else{
		   for (var i=0; o && o.length && i<o.length; i++){
	   	  	  o[i].checked=false;
	   	   }
	   	   if(o && !o.length && !o.checked)
			{
				o.checked=false;
			}
	   	   
	   }
	}
	//单个选中复选框
	function checkOne(totalCheck,checkName){
	   var selectAll = document.all.item(totalCheck);
	   var o = document.all.item(checkName);
		var cbs = true;
		
		for (var i=0;o && o.length && i < o.length;i++){
			if(!o[i].disabled){
				if (o[i].checked==false){
					cbs=false;
				}
			}
		}
		if(o && !o.length && !o.checked)
		{
			cbs = false;
		}
		if(cbs){
			selectAll.checked=true;
		}
		else{
			selectAll.checked=false;
		}
	}	
	
	function dealRecord(checkName,dealType) {
	    if(!validatecheck(checkName))
		{
			alert("请选择要删除的日程");
			return false;
		}
	    
	    	if (dealType==1){
	    		outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
	        	if (confirm(outMsg)){
		        	Org.action="../schedularmanage/schManager.do?method=deleteSchedular&path=latest";
					Org.submit();
			 		return true;
				}
			} 
	    return false;
	}
</script>
<body class="contentbodymargin" scrolling="no">
<div id="contentborder" align="center">
<FORM name="Org" action="" method="post">
		
	<TABLE width="100%" cellpadding="1" cellspacing="1" bordercolor="#EEEEEE" class="thin">
	<!--分页显示开始,分页标签初始化--><TR><TD height="30" class="detailtitle" align="center" colspan="8"><B>近期日程列表</B></TD></TR>
		<pg:listdata dataInfo="LatestSchedularList" keyName="LatestSchedularList" />
		
		<pg:pager maxPageItems="15" scope="request" data="LatestSchedularList" isList="false">
			      	<td class="headercolor" width="10">
				  	<input type="checkBox" name="checkBox" onClick="checkAll('checkBox','ID')">
				  	</td>
			      	<td class="headercolor">主题</td>
			      	<td class="headercolor">地点</td>
			      	<td class="headercolor">日程开始时间</td>
			      	<td class="headercolor">日程结束时间</td>
			      	<td class="headercolor">日程类型</td>
			     	<pg:list>								
							<tr class="labeltable_middle_tr_01" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'">	      				
							<td class="tablecells" width="5%" nowrap="nowrap">
									<input type="checkBox" name="ID" onClick="checkOne('checkBox','ID')" value="<pg:cell colName="schedularID" defaultValue=""/>" >
									</td>
									<td class="tablecells" width="20%" nowrap="nowrap">
									
									<pg:equal colName="topic" value="">无主题</pg:equal>
										<pg:cell colName="topic" defaultValue="无主题" />
									</td>
									<td class="tablecells" width="20%" nowrap="nowrap">
									
									<pg:equal colName="place" value="">无地点</pg:equal>
									<pg:cell colName="place" defaultValue="无地点" />
									</td>
									<td class="tablecells" width="20%" nowrap="nowrap" >
									
									<pg:equal colName="beginTime" value="">未安排</pg:equal>
										<pg:cell colName="beginTime" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="未安排" />
									</td>
									<td class="tablecells" width="20%" nowrap="nowrap">
									
									<pg:equal colName="endTime" value="">未安排</pg:equal>
										<pg:cell colName="endTime" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="未安排" />
									</td>									
									
									<td class="tablecells" width="15%" nowrap="nowrap">
										<pg:cell colName="type" defaultValue="无" />
									</td>
								</tr>
							</pg:list>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=6 align='center' nowrap="nowrap">
									<pg:index />
								<input type="submit" class="input" value="删除日程" 
								onclick="javascript:dealRecord('ID',1); return false;">
								<INPUT type="button" class="input" name="viewSD" value="查看日程" onclick="view('ID')"> 
								<INPUT type="button" class="input" name="modifySD" value="修改日程" onclick="modify('ID')"> 
								
								</td>							
								</tr>
							<input name="queryString" value="<pg:querystring/>" type="hidden">
						
			   			   	      
		</pg:pager>
		
	</TABLE>	
	
 </FORM>  
 </div>
 	<%@include file="../sysMsg.jsp" %>
</body>

</html>

