<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.LogManagerImpl"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.common.poolman.DBUtil"%>
<%@page import="java.util.Date"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    //当前的时间
	String riqi ;		
	String curUserId = accesscontroler.getUserID();
	java.text.SimpleDateFormat   df=new   java.text.SimpleDateFormat("yyyy-MM-dd");
    Date currentDate = new Date(); 
    /**当前时间*/ 
    riqi = df.format(currentDate);
    
    String back = request.getParameter("back");
    String days = request.getParameter("days");
    boolean state = false;
    if(back != null && "1".equals(back)){
    	
    	new LogManagerImpl().backupLog(days);
    	state = true;
    }
    
	
String rootpath = request.getContextPath();
    /*
    String operUser=request.getParameter("operUser");    
    String type=request.getParameter("type"); 
    //String logModuel=request.getParameter("logModuel");       
    String oper=request.getParameter("oper");     
	String startDate=request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
    operUser = operUser==null?"":operUser;
    type = type==null?"":type;
    oper = oper==null?"":oper;
    startDate = startDate==null?"":startDate;
    endDate = endDate==null?"":endDate;
	if(startDate==null || startDate==""){
		//startDate = riqi;
	}
	if(endDate==null || endDate==""){
		//endDate = riqi;
	}
   */
    
%>
<html >
      

<head>
		<title>日志管理</title>
<tab:tabConfig/>		
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="javascript" src="../scripts/selectTime.js"></script>

<script language="JavaScript" src="<%=request.getContextPath()%>/public/datetime/calender_date.js" ></script>
<script language="JavaScript" src="../../sysmanager/scripts/selectTime.js" type="text/javascript"></script>
<script type="text/javascript" src="../../public/datetime/calender.js" language="javascript"></script>
<script type="text/javascript" src="../../public/datetime/calender_date.js" language="javascript"></script>
<script language="JavaScript">
	var state = <%=state%>;
	if(state){
		<%if("0".equals(days)){%>
		$.dialog.alert('<pg:message code="sany.pdp.sysmanager.log.backup.all.success"/>');
		<%}else{%>
		$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.name" arguments="<%=days%>"/>');
		<%}%>
	}

	var jsAccessControl = new JSAccessControl("#DAE0E9","#F6F8FB","#F6F8FB");
	function getLogInfo(e,logId){
	jsAccessControl.setBackColor(e);
	document.all("selectId").value = logId;
	}
	
	function queryUser(isHis)
	{	
		//查询
		if(LogForm.startDate.value>LogForm.endDate.value)
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
		    //+"&isHis="+isHis
		    //+"&opOrgid="+document.all.opOrgid.value;
		    var isRecursion = "0";
		    if(document.LogForm.isRecursion.checked){
		    	isRecursion = "1";
		    }
		    document.LogForm.target="logGrid"
		    document.LogForm.action="logList.jsp?isHis=n&isRecursion="+isRecursion;
		    document.LogForm.submit();
		    
			//LogForm.action="logList_tab.jsp";
            //LogForm.action="logList_tab.jsp";
			//LogForm.submit();	
		}
	}
    
    function resetForm(){
        $("#reset").click();
    }
    
    function backUp(){
    	var days = document.all.beforDay.value;
    	if(days.length == 0){
    		days = 0;
    	}else{
	    	if(days.search(/[^0-9]/g) != -1){
	    		$.dialog.alert('<pg:message code="sany.pdp.sysmanager.log.backup.day.num.int"/>');
	    		return;
	    	}
    	}
    	document.LogForm.target = "_self";
    	document.LogForm.action = "logList_tab.jsp?back=1&days="+days;
    	document.LogForm.submit();
    	
    }
    
    function selectOrg(){
    	
    	var url="<%=request.getContextPath()%>/sysmanager/dictmanager/orgSelectTree.jsp?tag1=opOrgid&tag2=opRemark5";
		$.dialog({title:'<pg:message code="sany.pdp.sysmanager.resource.type.org.selected"/>',width:616,height:500,content:'url:'+url}); 
    	
    	/*
		var url = "../../sysmanager/dictmanager/orgSelectTree.jsp";
		var returnVal = window.showModalDialog(url,window,"dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
		if(returnVal != "" && returnVal.split("^")[0] != ""){
			document.all.opRemark5.value = returnVal.split("^")[0].split(" ")[1];
			document.all.opOrgid.value = returnVal.split("^")[0].split(" ")[0];
		}
		*/
	}
	
	function isValue(){
		var val = document.LogForm.opRemark5.value;
		if(val != ""){
			document.LogForm.isRecursion.disabled = false;
		}else{
			document.LogForm.isRecursion.disabled = true;
		}
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
<body>
<sany:menupath menuid="logquery"/>
<tab:tabContainer id="log-bar-container" selectedTabPaneId="loglist"  skin="sany">
	<tab:tabPane id="loglist"  tabTitleCode="sany.pdp.sysmanager.log.online">
		
		<div class="mcontent">
			<div id="searchblock">
				<div  class="search_top">
		    		<div class="right_top"></div>
		    		<div class="left_top"></div>
      			</div>
      			<div class="search_box">
      				<form name="LogForm" action=""  method="post">
      					<table width="98.5%" border="0" cellspacing="0" cellpadding="0">
      						<tr>
		      					<td class="left_box"></td>
		      					<td>
		      						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
		      							<tr>
		      								<th><pg:message code="sany.pdp.sysmanager.log.operator.account"/>：</th>
											<td><input type="text" name="operUser" value="" class="w120" /></td>
		      								<th><pg:message code="sany.pdp.sysmanager.log.operator.belong.org"/>：</th>
		      								<td>
		      									<input type="text" name="opRemark5" value=""  readonly="false" onclick="selectOrg()" class="w120" />
		      									<input type="hidden" name="opOrgid" value=""  >
		      									&nbsp;
		      									<input type="checkbox" name="isRecursion" title="<pg:message code='sany.pdp.sysmanager.log.operator.belong.org.available'/>"><pg:message code="sany.pdp.sysmanager.log.search.suborg.recursion"/>
		      								</td>
		      							</tr>
		      							<tr>
		      								<th><pg:message code="sany.pdp.sysmanager.log.content"/>：</th>
		      								<td><input type="text" name="oper" value=""  class="w120" /></td>
		      								<th><pg:message code="sany.pdp.sysmanager.log.operator.time"/>：</th>
		      								<td>
		      									<input type="text" name="startDate" onclick="WdatePicker()" readonly="true" value="" validator="stringNull" cnname="<pg:message code='sany.pdp.sysmanager.log.search.starttime'/>" maxlength="40" class="w120" />
		      									<pg:message code="sany.pdp.sysmanager.log.search.to"/>
		      									<input type="text" name="endDate" onclick="WdatePicker()" readonly="true" value="" validator="stringNull" cnname="<pg:message code='sany.pdp.sysmanager.log.search.endtime'/>" maxlength="40" class="w120" />
		      								</td>
		      							</tr>
		      							<tr>
		      								<th><pg:message code="sany.pdp.sysmanager.log.module"/>：</th>
		      								<td>
		      									<input name="logModuel" id="logModuel" value="" type="hidden"/>
		      									<select name="logModuel_select" class="cms_select"  onChange="logModuel_selectevent(this)"  class="w120" >
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
		      								<td><input type="text" name="logVisitorial" value="" style="width:85%" class="w120" /></td>
		      							</tr>
		      							<%if(accesscontroler.isAdmin()){ %>
		      							<tr>
		      								<th><pg:message code="sany.pdp.sysmanager.log.backup.day.few"/>：</th>
		      								<td>
		      									<input name="beforDay" type="text" value="0" class="w120" />&nbsp;&nbsp;
		      									<a href="javascript:void(0)" class="bt_1" id="but"  onclick="backUp()" title="<pg:message code='sany.pdp.sysmanager.log.backup.day.default'/>"><span><pg:message code="sany.pdp.sysmanager.log.data.backup"/></span> </a>
		      									&nbsp;&nbsp;<font color="red"><pg:message code="sany.pdp.sysmanager.log.backup.day.default"/></font>
		      								</td>
		      								<th></th>
		      								<td>
		      									<a href="javascript:void(0)" class="bt_1" id="search"  onclick="queryUser('n')"><span><pg:message code="sany.pdp.common.operation.search" /></span> </a>
			      								<a href="javascript:void(0)" class="bt_2" id="Submit22" onclick="resetForm()"><span><pg:message code="sany.pdp.common.operation.reset" /></span> </a>
		      									<input type="reset" id="reset" style="display: none" />
		      								</td>
		      							</tr>
		      							<%} %>
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
					<a href="javascript:void(0)" class="bt_small" onclick="queryState('y')"><span><pg:message code="sany.pdp.sysmanager.log.module.search"/></span> </a>
				</div>
				<strong><pg:message code="sany.pdp.sysmanager.log.data.list"/></strong>
			</div>
			
			<iframe src="logList.jsp?isHis=n" name="logGrid"  width="101.7%"  height="505px" frameborder="0" style="display:inline"></iframe>
		</div>	
		
	</tab:tabPane>
	
	<tab:tabPane id="log-his"  lazeload="true"  tabTitleCode="sany.pdp.sysmanager.log.history">
		
		<tab:iframe id="loghis" src="LogHisList.jsp" frameborder="0" width="100%" height="100%" scrolling="no"/>
			
	</tab:tabPane>
<!--模块是否填写日志控制页面------------------------------------------------------------------>
<%if(accesscontroler.isAdmin()){ %>
	<tab:tabPane id="log-module"  lazeload="true"  tabTitleCode="sany.pdp.sysmanager.log.config">
		
		<tab:iframe id="logmodule" src="LogModuleList.jsp" frameborder="0" width="100%" height="505px"/>
			
	</tab:tabPane>
<%} %>
</tab:tabContainer>
<iframe name="hiddenFrame" width="0" height="0"></iframe>
</body>
<script>

</script>
</html>

