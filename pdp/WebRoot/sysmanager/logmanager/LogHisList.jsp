<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.common.poolman.DBUtil"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    String rootpath = request.getContextPath();
    
    String curUserId = accesscontroler.getUserID();
%>
<html >
      

<head>
<script language="JavaScript" src="<%=request.getContextPath()%>/public/datetime/calender_date.js" ></script>
<script language="JavaScript">
	
	function queryUser(isHis)
	{	
		//查询
		if(LogHisForm.startDate.value>LogHisForm.endDate.value)
		{
			$.dialog.alert('<pg:message code="sany.pdp.sysmanager.log.starttime.endtime.behind"/>');
			return;
		}
		else{
		    //document.all.logGrid.src="logList.jsp?curUserId=<%=curUserId%>"
		    //+"&operUser="+document.all.operUser.value
		    //+"&oper="+document.all.oper.value
		    //+"&type="+document.all.type.value
		    //+"&startDate="+document.all.startDate.value
		    //+"&endDate="+document.all.endDate.value
		    //+"&logModuel="+document.all.logModuel.value
		    //+"&isHis="+isHis;
			//LogForm.action="logList_tab.jsp";
            //LogForm.action="logList_tab.jsp";
			//LogForm.submit();	
			var isRecursion = "0";
		    if(document.LogHisForm.isRecursion.checked){
		    	isRecursion = "1";
		    }
			document.LogHisForm.target="logGridhis";
			document.LogHisForm.action="logList.jsp?isHis=y&isRecursion="+isRecursion;
			document.LogHisForm.submit();
		}
	}
    
    function resetForm(){
        $("#reset").click();
    }
    
    function selectOrg(){
    
    	var url="<%=request.getContextPath()%>/sysmanager/dictmanager/orgSelectTree.jsp?tag1=opOrgid&tag2=opRemark5";
		$.dialog({title:'<pg:message code="sany.pdp.sysmanager.resource.type.org.selected"/>',width:616,height:500,content:'url:'+url}); 
    	
		//var url = "../../sysmanager/dictmanager/orgSelectTree.jsp";
		//var returnVal = window.showModalDialog(url,window,"dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
		//if(returnVal != "" && returnVal.split("^")[0] != ""){
			//document.all.opRemark5.value = returnVal.split("^")[0].split(" ")[1];
			//document.all.opOrgid.value = returnVal.split("^")[0].split(" ")[0];
		//}
	}
	
	function queryState(obj){
		var url="<%=request.getContextPath()%>/sysmanager/logmanager/statLogmodule.jsp?isHis="+obj;
		$.dialog({title:'<pg:message code="sany.pdp.sysmanager.log.module.statistic"/>',width:800,height:600, content:'url:'+url});   
	}
	function logModuel_selectevent(obj)
	{
		var value = obj.value;
		document.getElementById('logModuel').value = encodeURIComponent(value); 
		
		
	}
</script>
</head>
<body>
	<div class="mcontent">
		<div id="searchblock">
			<div  class="search_top">
	    		<div class="right_top"></div>
	    		<div class="left_top"></div>
     		</div>
     		<div class="search_box">
     			<form name="LogHisForm"  method="post">
     				<table width="98.5%" border="0" cellspacing="0" cellpadding="0">
     					<tr>
		      				<td class="left_box"></td>
		      				<td>
		      					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
		      						<tr>
		      							<th><pg:message code="sany.pdp.sysmanager.log.operator.account"/>：</th>
		      							<td><input type="text" name="operUser" value="" class="w120"/></td>
		      							<th><pg:message code="sany.pdp.sysmanager.log.operator.belong.org"/>：</th>
		      							<td>
		      								<input type="text" name="opRemark5" value=""  readonly="true" onclick="selectOrg()" class="w120"/>
											<input type="hidden" name="opOrgid" value="" />
		      							</td>
		      							<th>&nbsp;</th>
		      							<td><input type="checkbox" name="isRecursion" title="<pg:message code='sany.pdp.sysmanager.log.operator.belong.org.available'/>" /><pg:message code="sany.pdp.sysmanager.log.search.suborg.recursion"/></td>
		      						</tr>
		      						<tr>
		      							<th><pg:message code="sany.pdp.sysmanager.log.content"/>：</th>
		      							<td><input type="text" name="oper" value="" class="w120"/></td>
		      							<th><pg:message code="sany.pdp.sysmanager.log.operator.time"/>：</th>
		      							<td>
		      								<input type="text" name="startDate" onclick="WdatePicker()" readonly="true" value="" validator="stringNull" cnname="<pg:message code='sany.pdp.sysmanager.log.search.starttime'/>" maxlength="40" class="w120"/>
						     				<pg:message code="sany.pdp.sysmanager.log.search.to"/>
						     				<input type="text" name="endDate" onclick="WdatePicker()" readonly="true" value="" validator="stringNull" cnname="<pg:message code='sany.pdp.sysmanager.log.search.endtime'/>" maxlength="40" class="w120"/>
		      							</td>
		      						</tr>
		      						<tr>
		      							<th><pg:message code="sany.pdp.sysmanager.log.module"/>：</th>
		      							<td>
		      							<input name="logModuel" id="logModuel" value="" type="hidden"/>
		      								<select name="logModuel_select" class="cms_select"  onChange="logModuel_selectevent(this)" class="w120">
												<option value=""><pg:message code="sany.pdp.sysmanager.log.module.all"/></option>
											<%
											try {
												DBUtil dbUtil = new DBUtil();
												String hsql ="select * from TD_SM_LOGMODULE order by logmodule";
												dbUtil.executeSelect(hsql);
												for(int i = 0; i < dbUtil.size(); i ++)
												{
													%>
													<option value="<%=dbUtil.getString(i,"LOGMODULE")%>"><%=dbUtil.getString(i,"LOGMODULE")%></option>
													<%
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
											%>
											</select>
		      							</td>
		      							<th><pg:message code="sany.pdp.sysmanager.log.source"/>：</th>
		      							<td><input type="text" name="logVisitorial" value="" class="w120"/></td>
		      							<th>&nbsp;</th>
		      							<td>
		      								<a href="javascript:void(0)" class="bt_1" id="search"  onclick="queryUser('y')"><span><pg:message code="sany.pdp.common.operation.search" /></span> </a>
		      								<a href="javascript:void(0)" class="bt_2" id="Submit22" onclick="resetForm()"><span><pg:message code="sany.pdp.common.operation.reset" /></span> </a>
		      								<input type="reset" id="reset" style="display: none" />
		      							</td>
		      						</tr>
		      					</table>
		      				</td>
		      				<td class="right_box"></td>
		      			</tr>	
     				</table>
     			</form>
     		</div>
     		<div class="search_bottom">
				<div class="right_bottom"></div>
				<div class="left_bottom"></div>
			</div>
		</div>
		
		<div class="title_box">
			<div class="rightbtn">
				<a href="javascript:void(0)" class="bt_small" onclick="queryState('n')"><span><pg:message code="sany.pdp.sysmanager.log.module.search"/></span> </a>
			</div>
			<strong><pg:message code="sany.pdp.sysmanager.log.data.list"/></strong>
		</div>
		
			 <iframe src="logList.jsp?isHis=y" name="logGridhis"  width="101.7%" height="430px" frameborder=0 style="display:inline"></iframe>		
	</div>

	</body>
</html>
