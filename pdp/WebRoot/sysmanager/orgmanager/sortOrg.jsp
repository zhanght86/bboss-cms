<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%
	String orgId = request.getParameter("orgId");//机构id
	String orgName = request.getParameter("orgName");//机构orgName
%>
<html>
<head>    
  	<title>机构排序</title>
	<SCRIPT LANGUAGE="JavaScript">
	//排序
	function sortOrg()
	{
		var len = document.all("orgId").options.length;
		var orgIds = "";
		for (var i=0;i<len;i++)
		{
			orgIds += document.all("orgId").options[i].value + ",";
		}
		if(orgIds == "")
		{
			return false;
		}
		document.forms[0].action = "sortOrg_do.jsp?orgId=<%=orgId%>&orgIds=" + orgIds;
		document.forms[0].target = "sortOrgiframe";
		document.forms[0].submit();
	}

	function up1() {
		var len=document.all("orgId").options.length;
		var isselected = 0;
		for (var i=0;i<len;i++){
			var op=document.all("orgId").options[i];
	   		if(op.selected){   			
	   			isselected += 1;
	   		}
	   	}
	   	if(isselected != 1){
	   		alert("请先选择一个机构再进行此操作");
	   		return;
	   	}
		for (var i=0;i<len;i++){
			var op=document.all("orgId").options[i];
	   		if(op.selected){   			
	   			var tmp = new Option(op.text,op.value);
	   			if(i>0){
	   				var dest = document.all("orgId").options[i-1];
	   				document.all("orgId").options[i-1] = tmp;
	   				document.all("orgId").options[i] = dest;
	   				document.all("orgId").options[i-1].selected=true;
					var num = i - 1;
					//document.all("orgId").selectedIndex = num;
					setTimeout("indexChange(" + num +");",100);
	   			}
	   		}
	    }
	}
	function upall() {
		var len=document.all("orgId").options.length;	
		var isselected = 0;
		for (var i=0;i<len;i++){
			var op=document.all("orgId").options[i];
	   		if(op.selected){   			
	   			isselected += 1;
	   		}
	   	}
	   	if(isselected != 1){
	   		alert("请先选择一个机构再进行此操作");
	   		return;
	   	}
		for (var i=0;i<len;i++){
			var op=document.all("orgId").options[i];
	   		if(op.selected){   			
	   			var tmp = new Option(op.text,op.value);
	   			op.selected = false;   			
	   			var j=i;   			
	   			for(;j>=1;j--){     				
	   				var atmp =  document.all("orgId").options[j-1];   				
	   				var btmp = new Option(atmp.text,atmp.value);   				
	   				document.all("orgId").options[j] = btmp;
	   			}   			
	   			document.all("orgId").options[0] = tmp;
	   			break;	
	   		}
	    }  
	    document.all("orgId").options[0].selected=true;
		document.all("orgId").selectedIndex = 0;
	}
	
	function down1() {
		var len=document.all("orgId").options.length;
		var isselected = 0;
		for (var i=0;i<len;i++){
			var op=document.all("orgId").options[i];
	   		if(op.selected){   			
	   			isselected += 1;
	   		}
	   	}
	   	if(isselected != 1){
	   		alert("请先选择一个机构再进行此操作");
	   		return;
	   	}
		var flag = 0;
		for (var i=0;i<len;i++){
			var op=document.all("orgId").options[i];
	   		if(op.selected){   			
	   			var tmp = new Option(op.text,op.value);   			
	   			op.selected=false;
	   			if(i == len-1){
	   				flag = len-2;
	   			}
	   			else {
	   				flag = i;
	   			}
	   			if(i<len-1){
	   				var dest = document.all("orgId").options[i+1];
	   				var orgId2 = tmp.value;	   				
	   				var orgId = document.all("orgId").value;
	   				var orgId1 = dest.value;
	   				var jobSn = i;
	   				document.all("orgId").options[i+1] = tmp;
	   				document.all("orgId").options[i] = dest;
	   			}
	   		}
	    }
	    document.all("orgId").options[flag+1].selected=true;
		var num = flag + 1;
		setTimeout("indexChange(" + num +");",100);
		//document.all("orgId").selectedIndex = num;
	}

	
	function downall() {
		var len=document.all("orgId").options.length;
		var isselected = 0;
		for (var i=0;i<len;i++){
			var op=document.all("orgId").options[i];
	   		if(op.selected){   			
	   			isselected += 1;
	   		}
	   	}
	   	if(isselected != 1){
	   		alert("请先选择一个机构再进行此操作");
	   		return;
	   	}	
		for (var i=0;i<len;i++){
			var op=document.all("orgId").options[i];
	   		if(op.selected){   			
	   			var tmp = new Option(op.text,op.value);
	   			op.selected = false;   			
	   			var j=i;   			
	   			for(;j<len-1;j++){     				
	   				var atmp =  document.all("orgId").options[j+1];   				
	   				var btmp = new Option(atmp.text,atmp.value);   				
	   				document.all("orgId").options[j] = btmp;   				
	   			}   			
	   			document.all("orgId").options[len-1] = tmp;	
				break;
	   		}
	    }
	    document.all("orgId").options[len-1].selected=true;
		var num = len-1;
		setTimeout("indexChange(" + num +");",100);
		//document.all("orgId").selectedIndex = num;
	}
	function indexChange(num)
	{
		document.OrgJobForm.orgId.selectedIndex = num;
	}
	</SCRIPT>
   <%@ include file="/include/css.jsp"%>
   <link rel="stylesheet" type="text/css" href="../css/contentpage.css">
   <link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
   <%@ include file="/include/css.jsp"%>
   <link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
   <link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
	
<body >
<form name="OrgJobForm" action="" method="post" >

<table width="100%" border="0" cellpadding="1" cellspacing="1">
  <tr>
	<td align="center" colspan="4">
	<%=orgName%>
	</td>
  </tr>
  <tr >
	<td width="20%"> &nbsp;</td>
    <td class="detailcontent" align="center" width="50%">
     <select name="orgId"  multiple style="width:100%" size="18">
	  <%
	  List orglist = OrgCacheManager.getInstance().getSubOrganizations(orgId);
	  request.setAttribute("orglist",orglist);
	  %>
	  <pg:list requestKey="orglist">
		<option value="<pg:cell colName="orgId"/>"><pg:cell colName="remark5"/></option>
	  </pg:list>			
	 </select>				
					
	</td>
	<td>
	<table align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center" class="detailcontent"><input name="button1" type="button" class="input" value="到顶部" onClick="upall()"></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button2" type="button" class="input" value="向上移" onClick="up1()"></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center" class="detailcontent"><input name="button3" type="button" class="input" value="向下移" onClick="down1()"></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center" class="detailcontent"><input name="button4" type="button" class="input" value="到底部" onClick="downall()"></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
    </table>
	
	</td>
  <tr>
  	<td>&nbsp;</td>
  </tr>	
  <tr>
    <td align="center" colspan="4">
	<input name="butn" type="button" class="input" value="确定" onClick="sortOrg();">
	<input name="butn" type="button" class="input" value="关闭" onClick="window.close();">
	</td>
  </tr>		  
  </tr>
  </table>
</form>
<iframe name="sortOrgiframe" width="0" height="0">
</iframe>
</body>
</html>

