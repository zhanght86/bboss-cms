<%     
  response.setHeader("Cache-Control", "no-cache"); 
  response.setHeader("Pragma", "no-cache"); 
  response.setDateHeader("Expires", -1);  
  response.setDateHeader("max-age", 0); 
%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<% 
	//是否是历史模块统计查询   y     n
	String isHis = request.getParameter("isHis");
	//System.out.println("isHis = " + isHis);
	
	//start query condition
	//String opRemark5 = request.getParameter("opRemark5");
	//if(opRemark5 == null){
	//	opRemark5 = "";
	//}
	//String opOrgid = request.getParameter("opOrgid");
	//if(opOrgid == null){
	//	opOrgid = "";
	//}
	
	//String startDate = request.getParameter("startDate");
	//if(startDate == null){
	//	startDate = "";
	//}
	//String endDate = request.getParameter("endDate");
	//if(endDate == null){
	//	endDate = "";
	//}
	
	//String logVisitorial = request.getParameter("logVisitorial");
	//if(logVisitorial == null){
	//	logVisitorial = "";
	//}
	
	//String isRecursion = request.getParameter("isRecursion");
	//if(isRecursion == null){
	//	isRecursion = "0";
	//}
	//end query condition
%>
<html>
<head>
<title>模块日志数统计</title>
	<base target="_self"/>
	<script language="JavaScript" src="<%=request.getContextPath()%>/public/datetime/calender_date.js" ></script>
	
	<script type="text/javascript" langugae="Javascript">
	
	var api = frameElement.api, W = api.opener;
	var api2 = api;
	
	function selectOrg(){
		
		var url="<%=request.getContextPath()%>/sysmanager/dictmanager/orgSelectTree.jsp?tag1=opOrgid&tag2=opRemark5";
		W.$.dialog({title:'<pg:message code="sany.pdp.sysmanager.resource.type.org.selected"/>',width:616,height:500,content:'url:'+url,parent:api,currentwindow:this}); 
				 	 
		//var url = "../../sysmanager/dictmanager/orgSelectTree.jsp";
		//var returnVal = window.showModalDialog(url,window,"dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
		
		//if(typeof(returnVal) != "undefined" && returnVal != "" && returnVal.split("^")[0] != ""){
		//	document.all.opRemark5.value = returnVal.split("^")[0].split(" ")[1];
		//	document.all.opOrgid.value = returnVal.split("^")[0].split(" ")[0];
		//}
	}
	
	function queryModuleStat()
	{	
		//查询
		if(statForm.startDate.value>statForm.endDate.value)
		{
			W.$.dialog.alert('<pg:message code="sany.pdp.sysmanager.log.starttime.endtime.behind"/>');
			return;
		}
		else{
			var isRecursion = "0";
		    if(document.statForm.isRecursion.checked){
		    	isRecursion = "1";
		    }
		    var opOrgid = document.all.opOrgid.value;1
		    var startDate = document.all.startDate.value;
		    var endDate = document.all.endDate.value;
		    var logVisitorial = escape(escape(document.all.logVisitorial.value));
		    
		    var tablesFrame = document.getElementsByName("statIframe");
		    //document.statForm.target = "hiddenIframe";
			//document.statForm.action = "statLogmodule.jsp?isHis=<%=isHis%>";
			//resetwindow();
			var roleIdValue = document.all("statIframe").value;
  			tablesFrame[0].src = "statLogmodule_iframe.jsp?isHis=<%=isHis %>&isRecursion="+isRecursion
  				+"&opOrgid="+opOrgid+"&startDate="+startDate+"&endDate="+endDate
  				+"&logVisitorial="+logVisitorial;
		}
	}
	function resetwindow()
	{
		document.statForm.action = window.location.href;	
		document.statForm.target = "";
		
		document.statForm.submit();
		
	}
	
	function resetValue(){
        document.statForm.startDate.value = "";
        document.statForm.endDate.value = "";
        document.statForm.opRemark5.value = "";
        document.statForm.opOrgid.value = "";
        document.statForm.logVisitorial.value = "";
        document.statForm.isRecursion.checked = false;
    }
	
	</script>
</head>

<body>
<div class="mcontent">
	<div style="height: 10px">&nbsp;</div>
	<div id="searchblock">
		<div  class="search_top">
    		<div class="right_top"></div>
    		<div class="left_top"></div>
    	</div>
    	<div class="search_box">
    		<form name="statForm" method="post" >
    			<table width="98%" border="0" cellspacing="0" cellpadding="0">
    				<tr>
      					<td class="left_box"></td>
      					<td>
      						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
      							<tr>
      								<th><pg:message code="sany.pdp.sysmanager.log.operator.belong.org"/>：</th>
      								<td>
      									<input type="text" name="opRemark5" value="" onclick="selectOrg()" class="w120"/>
      									<input type="hidden" name="opOrgid" value="" />
      									&nbsp;
      									<input type="checkbox" name="isRecursion" title="<pg:message code='sany.pdp.sysmanager.log.operator.belong.org.available'/>"/><pg:message code="sany.pdp.sysmanager.log.search.suborg.recursion"/>
      								</td>
      								<th><pg:message code="sany.pdp.sysmanager.log.operator.time"/>：</th>
      								<td>
      									<input type="text" name="startDate" onclick="showdate(document.all('startDate'))" readonly="true"  validator="stringNull" cnname="<pg:message code='sany.pdp.sysmanager.log.search.starttime'/>" maxlength="40" class="w120"/>
			    						 <pg:message code="sany.pdp.sysmanager.log.search.to"/>
			     						<input type="text" name="endDate" onclick="showdate(document.all('endDate'))" readonly="true"  validator="stringNull" cnname="<pg:message code='sany.pdp.sysmanager.log.search.endtime'/>" maxlength="40" class="w120"/>
      								</td>
      							</tr>
      							<tr>
      								<th><pg:message code="sany.pdp.sysmanager.log.source"/>：</th>
      								<td>
      									<input type="text" name="logVisitorial"  class="w120"/>
      								</td>
      								<th>&nbsp;</th>
      								<td align="right">
      									<a href="javascript:void(0)" class="bt_1" id="search"  onclick="queryModuleStat()"><span><pg:message code="sany.pdp.common.operation.search" /></span> </a>
			      						<a href="javascript:void(0)" class="bt_2" id="Submit22" onclick="resetValue()"><span><pg:message code="sany.pdp.common.operation.reset" /></span> </a>
      									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      								</td>
      							</tr>
      						</table>
      					</td>
      					<td class="right_box"></td>
		      		</td>
    			</table>
    		</form>
    	</div>
    	<div class="search_bottom">
			<div class="right_bottom"></div>
			<div class="left_bottom"></div>
		</div>
	</div>
	
	<iframe src="statLogmodule_iframe.jsp?isHis=<%=isHis %>" name="statIframe" width="102.5%" height="100%"  frameborder="0"></iframe>
</div>
<iframe name="hiddenFrame" width="0" height="0"></iframe>
</body>
</html>
