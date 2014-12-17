<%
/**
 * <p>Title: 子机构排序</p>
 * <p>Description: 子机构排序</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
 <%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%
	String orgId = request.getParameter("orgId");//机构id
	String orgName = request.getParameter("orgName");//机构orgName
%>
<html>
<head>    
  	<title>机构树排序</title>
	<SCRIPT LANGUAGE="JavaScript">
	
	var api = frameElement.api, W = api.opener;
	
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
		//document.all.divProcessing.style.display = "block";
		//document.all.saveButton.disabled = true;
		
		//document.all.button1.disabled = true;
		//document.all.button2.disabled = true;
		//document.all.button3.disabled = true;
		//document.all.button4.disabled = true;
	  	document.forms[0].action =  "sortOrg_do.jsp?orgId=<%=orgId%>&orgIds=" + orgIds;
		document.forms[0].target = "hiddenFrame";
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
	   		W.$.dialog.alert("<pg:message code='sany.pdp.afterchooseoption'/>");
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
	   		W.$.dialog.alert("<pg:message code='sany.pdp.afterchooseoption'/>");
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
	   		W.$.dialog.alert("<pg:message code='sany.pdp.afterchooseoption'/>");
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
	   		W.$.dialog.alert("<pg:message code='sany.pdp.afterchooseoption'/>");
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
	
<body >
<div style="height: 10px">&nbsp;</div>
<form name="OrgJobForm" action="" method="post" >

<table width="100%" border="0" cellpadding="1" cellspacing="1">
  <tr>
	<td align="center" colspan="4">
	</td>
  </tr>
  <tr >
	<td width="20%"> &nbsp;</td>
    <td class="detailcontent" align="center" width="50%">
     <select name="orgId"   style="width:100%" size="18">
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
        <td align="center" class="detailcontent"><a class="bt_2"  onClick="upall()" ><span><pg:message code='sany.pdp.top'/></span></a></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><a class="bt_2"  onClick="up1()" ><span><pg:message code='sany.pdp.up'/></span></a></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center" class="detailcontent"><a class="bt_2"  onClick="down1()" ><span><pg:message code='sany.pdp.down'/></span></a></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center" class="detailcontent"><a class="bt_2"  onClick="downall()" ><span><pg:message code='sany.pdp.bottom'/></span></a></td>
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
	<a class="bt_1" onClick="sortOrg()"><span><pg:message code='sany.pdp.common.confirm'/></span></a> 
	
	</td>
  </tr>		  
  </tr>
  </table>
</form>
<div id=divProcessing style="width:200px;height:30px;position:absolute;left:100px;top:260px;display:none">
	<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
		<tr>
			<td bgcolor=#3A6EA5>
				<marquee align="middle" behavior="alternate" scrollamount="5">
					<font color=#FFFFFF>...处理中...请等待...</font>
				</marquee>
			</td>
		</tr>
	</table>
</div>
</body>
<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>

