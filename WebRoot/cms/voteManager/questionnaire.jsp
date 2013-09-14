<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.votemanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
  <%!/**
   * 针对HTML的特殊字符转义
   * @param value String
   * @return String
   */
  String filterStr(String value) {
    if (value == null) {
      return (null);
    }
    char content[] = new char[value.length()];
    value.getChars(0, value.length(), content, 0);
    StringBuffer result = new StringBuffer(content.length + 50);
    for (int i = 0; i < content.length; i++) {
      switch (content[i]) {
        case '<':
          result.append("&lt;");
          break;
        case '>':
          result.append("&gt;");
          break;
        case '&':
          result.append("&amp;");
          break;
        case '"':
          result.append("&quot;");
          break;
        case '\'':
          result.append("&#39;");
          break;
        case '\\':
          result.append("\\\\");
          break;
        default:
          result.append(content[i]);
      }
    }
	return (result.toString());
  }
%>  
  	 
 <%
	String path_= request.getContextPath();
    String basePath = request.getScheme() + "://"	+ request.getServerName() + ":" +
    request.getServerPort() + path_+ "/";
         
            String todayTime = null;
            String strOutput = null;
            Calendar calendar = Calendar.getInstance();
            strOutput = "" + calendar.get(1); //年
            int n = calendar.get(2) + 1;
            if (n < 10) //月
                strOutput = strOutput + "-0" + n;
            else
                strOutput = strOutput + "-" + n;
            n = calendar.get(5);
            if (n < 10) //日
                strOutput = strOutput + "-0" + n;
            else
                strOutput = strOutput + "-" + n;

            n = calendar.get(11);
            if (n < 10) //时
                strOutput = strOutput + " 0" + n;
            else
                strOutput = strOutput + " " + n;
            n = calendar.get(12);
            if (n < 10) //分
                strOutput = strOutput + ":0" + n;
            else
                strOutput = strOutput + ":" + n;
            n = calendar.get(13);
            if (n < 10) //秒
                strOutput = strOutput + ":0" + n;
            else
                strOutput = strOutput + ":" + n; 
           todayTime = strOutput;
%>

		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>无标题文档</title>
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<META HTTP-EQUIV="pragma" CONTENT="no-cache">
		<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
		<META HTTP-EQUIV="expires" CONTENT="Mon, 23 Jan 1978 20:52:30 GMT">
		
	<tab:tabConfig/>
		<script  src="<%=basePath%>public/datetime/calender.js" language="javascript"></script>
		<script  src="<%=basePath%>public/datetime/calender_date.js" language="javascript"></script>	
		<link href="<%=basePath%>cms/inc/css/cms.css" rel="stylesheet" type="text/css">
		<script src="<%=basePath%>cms/inc/js/func.js"></script>
		<script language="javascript">
	    //字符转换

    function HTMLEncode(text){
		//text = text.replace(/"/g, "&quot;");
		return text;
	}
	 function init() {
   
    //编辑器保持在设计模式
    eWebEditor1.setMode('EDIT');
    eWebEditor1.eWebEditor.document.body.innerHTML=form1.content.value;
    }
	var tableNames = "qstionTbl1;";
	var tableIndex = 1;
	
	function addQuestion(){
		var tbl = document.all("qstTable");
		var row = tbl.insertRow();
		var td = document.createElement("td");
		tableIndex ++;
		td.innerHTML = getQstionTblScript("qstionTbl"+tableIndex);
		tableNames += "qstionTbl"+tableIndex+";";
		row.appendChild(td);
		document.all("qstionTbl"+tableIndex+"AddBtn").disabled = true;
		document.all("qstionTbl"+tableIndex+"DelBtn").disabled = true;
	}
	
	function delQstion(tblName){
		var re = new RegExp(tblName+";","g");
		tableNames = tableNames.replace(re,"");
		var tbody = document.all(tblName).parentElement.parentElement.parentElement;
		tbody.removeChild(document.all(tblName).parentElement.parentElement)
	}
	
	function addOption(tblName){
		var tbl = document.all(tblName+"OptinTbl");
		var row = tbl.insertRow();
		var td = document.createElement("td");
		td.innerHTML = getOptionTblScript(tblName);
		row.appendChild(td);
	}
	
	function delOption(tblName){
		var chckbxs = document.getElementsByName(tblName+"Chkbx");
		for (var i=0;i<chckbxs.length;i++){
			if (chckbxs[i].checked){
				var tbody1= chckbxs[i].parentElement.parentElement.parentElement;
				tbody1.removeChild(chckbxs[i].parentElement.parentElement);
				i--;
			}
		}
	}
	
	function clearOption(tblName){
		var chckbxs = document.getElementsByName(tblName+"Chkbx");
		for (var i=0;i<chckbxs.length;i++){
				var tbody1= chckbxs[i].parentElement.parentElement.parentElement;
				tbody1.removeChild(chckbxs[i].parentElement.parentElement);
				i--;
		}
		document.all(tblName+"AddBtn").disabled = "true";
		document.all(tblName+"DelBtn").disabled = "true";
	}
	
	function changeStyle(tblName){
		if (document.all(tblName+"AddBtn").disabled){
			document.all(tblName+"AddBtn").disabled = false;
			document.all(tblName+"DelBtn").disabled = false;
			addOption(tblName);
		}
	}
	
	function addIpCtrl(){
		var tbl = document.all("ipTable");
		var row = tbl.insertRow();
		var td = document.createElement("td");
		td.innerHTML = "*<input type='checkbox' name='ipCtrlChlbx' value='checkbox'><input type='text' name='ipCtrlStart'>&nbsp;到&nbsp;<input type='text' name='ipCtrlEnd'>";
		row.appendChild(td);
	}
	
	function delIpCtrl(){
		var chckbxs = document.getElementsByName("ipCtrlChlbx");
		for (var i=0;i<chckbxs.length;i++){
			if(chckbxs[i].checked){
				var tbody1= chckbxs[i].parentElement.parentElement.parentElement;
				tbody1.removeChild(chckbxs[i].parentElement.parentElement);
				i--;
			}
		}
	}
	
	function save(){
		if(document.all("titleName").value.replace(/ /gi,'')==""){
			alert("问卷名称不能为空！");
		//	document.all("titleName").focus();
			return;
		}
		if(document.all("titleName").value.length>100)
		{
		  alert("问卷名称字数不能超过100！");
		  return false;
		}
		
		if(document.all("channelName").value.length < 1){
			alert("所属频道不能为空！");
		//	document.all("channelName").focus();
			return;
		}
		if(document.all("depart_id").value==""){
			alert("所属部门不能为空！");
			return;
		}
		document.all("ipStartString").value = "";
		document.all("ipEndString").value = "";
		
		//ip constrain
		var ipCtrlStartArray = document.getElementsByName("ipCtrlStart");
		var ipCtrlEndArray = document.getElementsByName("ipCtrlEnd");
		for (var i=0;i<ipCtrlStartArray.length;i++){
			if (!checkIP(ipCtrlStartArray[i].value)){
				alert("IP地址格式不对!!，只能输入数字和“.”,格式为XXX.XXX.XXX.XXX 例如：192.168.0.1");
				//ipCtrlStartArray[i].focus();
				return ;
			}
			if (ipCtrlStartArray[i].value > ipCtrlEndArray[i].value){
				alert("起始IP应小于终止IP！");
			//	ipCtrlStartArray[i].focus();
				return;
			}
			document.all("ipStartString").value += ipCtrlStartArray[i].value+";";
		}
		for (var i=0;i<ipCtrlEndArray.length;i++){
			if (!checkIP(ipCtrlEndArray[i].value)){
				alert("IP地址格式不对!!，只能输入数字和“.”,格式为XXX.XXX.XXX.XXX 例如：192.168.0.1");
			//	ipCtrlEndArray[i].focus();
				return ;
			}
			document.all("ipEndString").value += ipCtrlEndArray[i].value+";";
		}
		
		//time constrain
		//var timeCtrlStartArray = document.getElementsByName("timeCtrlStart");
		//var timeCtrlEndArray = document.getElementsByName("timeCtrlEnd");
		document.all("timeStartString").value = "";
		//for (var i=0;i<timeCtrlStartArray.length;i++){
			if (document.all("timeCtrlStart").value==""){
				alert("时间段限制不能为空！");
			//	timeCtrlStartArray[i].focus();
				return;
			}
			if (document.all("timeCtrlEnd").value==""){
				alert("时间段限制不能为空！");
			//	timeCtrlEndArray[i].focus();
				return;
			}
			
			if (document.all("timeCtrlStart").value > document.all("timeCtrlEnd").value){
				alert("起始时间应小于终止时间！");
			//	timeCtrlStartArray[i].focus();
				return;
			}
			var todayTime = "<%=todayTime%>";
			if(document.all("timeCtrlEnd").value<todayTime.substring(0,10))
			{
			  alert("时间段不应小于当前时间！");
			  return false;
			}
			document.all("timeStartString").value = document.all("timeCtrlStart").value+";";
			
		//}
		//for (var i=0;i<timeCtrlEndArray.length;i++){
			document.all("timeEndString").value = document.all("timeCtrlEnd").value+";";
		//}
		 eWebEditor1.setMode('EDIT');
         form1.content.value=HTMLEncode(eWebEditor1.eWebEditor.document.body.innerHTML);
         if(form1.content.value.lenght<1)
         {
           alert("问卷描述不能为空！");
           return false;
         }
		//construct questioin and options string. 
		//Format is like <questioin?style?option1;option2;><questioin?style?option1;option2;><questioin?style?option1;option2;>
		var questionString = ""
		var tableNameArray = tableNames.split(";");
		for (var i=0;i<tableNameArray.length-1;i++){
			var tblName = tableNameArray[i];
			//get question
			var qstion = document.all(tblName+"Qstion").value.replace(/\?/gi,'');
			var qstion_id = document.all(tblName+"Qstion_id").value;
			var voteCount = document.all(tblName+"VoteCount").value;
			if (qstion.replace(/ /gi,'')==""){
				alert("请填写问题！");
			//	document.all(tblName+"Qstion").focus();
				return;
			}
			if(document.all(tblName+"Qstion").value.length>100)
			{
			  alert("问题字数不能超过100！");
			  return false;
			}
			//get style
			var styleChckbx =  document.all(tblName+"Style");
			for (var j=0;j<styleChckbx.length;j++){
				if(styleChckbx[j].checked == true){				
					styleChckbx = styleChckbx[j].value;
					break;
				}
			}
			if (styleChckbx[1]!=null){
				alert("请指定回答方式！");
			//	styleChckbx[0].focus();
				return;
			}
			//get options
			
			var optionString = "";
			var countString = "";
			var option_id = "";
			if(styleChckbx!=2)
			{
			var optionText = document.getElementsByName(tblName+"Option");
			var countText = document.getElementsByName(tblName+"Count");
			var option_idText = document.getElementsByName(tblName+"Option_id");
			if (styleChckbx!=2 && (optionText==null||optionText.length==0)){
				alert("请填入选项信息！");
				return;
			}
			for (var j=0;j<optionText.length;j++){
				if (optionText[j].value.replace(/ /gi,'')==""){
					alert("请填入选项信息！");
				//	optionText[j].focus();
					return;
				}
				optionString += optionText[j].value
								.replace(/;/gi,',')
								.replace(/\>/gi,'》')
								.replace(/\</gi,'《')
								.replace(/'/gi,'`')+";";
				option_id += option_idText[j].value + ";";
				countString += countText[j].value + ";";
			}
			}
			qstion = qstion.replace(/\>/gi,'》').replace(/\</gi,'《');
			questionString += "<"+qstion+"'"+qstion_id+"'"+voteCount+"'"+styleChckbx+"'"+optionString+"'"+option_id+"'"+countString+">";
		}
		document.all("questionString").value= questionString;
		document.all("actionType").value= "save";
		document.form1.target = "biaoge";
		document.form1.submit();
	}
	
	function checkIP(ip){
		var format = /(\d+)\.(\d+)\.(\d+)\.(\d+)/;
		if (ip.replace(format,"").length>0){
			return false;
		}
		
		var array = ip.split(".");
		if (array.length != 4)
			return false;
			
		for (var i=0;i<array.length;i++)
			if (array[i]>255 || array[i]<0)
				return false;
				
		return true;
	}
	
	function chkTimeGap(){
		var repeatChkbx= document.all("can_repeat");
		if (repeatChkbx.checked){
			document.all("selectGap").disabled = false;
		}else{
			document.all("selectGap").options[0].selected = true;
			document.all("selectGap").disabled = true;
		}
	}
	
	function del(){
		if (confirm("是否需要删除该问卷？")){
			document.all("actionType").value= "delete";
			document.form1.submit();
		}
	}
	function chooseImage(){
		openWin("<%=basePath%>cms/voteManager/chooseImageFrameset.jsp",520,580);
	}
	function setImage(uri){
			document.all("picpath").value  = uri;
	}
	</script>
	</head>
	<body style="background-color: #F3F4F9" onload="init()">
	<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(request,response);
	CMSManager cmsM = new CMSManager();
	cmsM.init(request,session,response,accessControl);
	int siteID = Integer.parseInt(cmsM.getSiteID());
	
	String docPath = "site"+siteID+"/_webprj/content_files/votemanager"; 
	
	String qid = request.getParameter("qid");
	String id = request.getParameter("id");
	String docpath = "";
	Title oneTitle = new Title();
	String picPath = "";
	String depart = "";
	String ctime = "";
	String check = "";
	String content = "";
	if (id==null&&qid!=null){
		VoteManager voteMgr  = new VoteManagerImpl();
		id = String.valueOf(voteMgr.getTitleIDBy(qid));
	}
	if(id!=null)
	{
		VoteManager voteMgr  = new VoteManagerImpl();
	    oneTitle = voteMgr.getSurveyBy(Integer.parseInt(id));
	    if(oneTitle!=null)
	    {
	    	picPath = oneTitle.getPicpath();
	    	System.out.println(picPath);
	    	ctime = oneTitle.getFoundDate().substring(0,10);
	    	depart = oneTitle.getDepart_id();
	    	content = oneTitle.getContent();
	    	content = filterStr(content);
	    }
	}
	String channel_id_hidden = "";
	String sql = "select channel_id from td_cms_channel where DISPLAY_NAME='"+request.getParameter("channel")+"'";
	
	%>
	<pg:pager scope="request" statement="<%=sql%>" title="分页" dbname="bspf" isList="true">				
			<pg:notify>当前没有记录</pg:notify>
			<pg:list>
             <% 
			 channel_id_hidden = dataSet.getString("channel_id");
			 %>
			</pg:list>
	</pg:pager>
	
	
	<base target="_self">
		<form name="form1" method="post" action="questionnaire_do.jsp" target="biaoge">
		
			<input name="ipStartString" type="hidden" value="">
			<input name="titleID" type="hidden" value="">
			<input name="ipEndString" type="hidden" value="">
			<input name="timeStartString" type="hidden" value="">
			<input name="timeEndString" type="hidden" value="">
			<input name="questionString" type="hidden" value="">
			<input name="actionType" type="hidden" value="">
			<input name="origQidString" type="hidden" value="">
			<input name="channelid" type="hidden" value="<%=channel_id_hidden%>">
			<table width="100%">
				<tr>
					<td>
						<div align="right">
							<input type="button" name="保存" value="保存" onclick="return save()"  class="cms_button">
							<% if(request.getParameter("qid")!=null||request.getParameter("id")!=null){
							 %><input type="button" name="删除" value="删除" onclick="return del();"  class="cms_button"><%
							}%>
							<input type="button" name="取消" value="取消" onclick="return window.close()"  class="cms_button">
						</div>
					</td>
				</tr>
			</table>
		<tab:tabContainer id="foo-survey-add" selectedTabPaneId="foo-survey">
			<tab:tabPane id="foo-survey" tabTitle="问卷信息">
			
			<table border="0" cellpadding="0" cellspacing="0" bgcolor="#F3F4F9" >
				<tr>
					<td align="right">
						问卷名称：
					</td>
					<td>
						<input name="titleName" type="text" maxlength="200" style="width:400px;"><font color="red">*问卷标题字数不能超过100字</font>
					</td>
				</tr>
				<!-- <tr>
					<td align="right">
						问卷描述：

					</td>
					<td>
						<textarea id="content" name="content" cols="63" rows="10"></textarea><font color="red">*问卷描述字数不能超过500字</font>
					</td>
				</tr>-->
				<!-- <tr>
					<td align="right">
						所属频道：

					</td>
					<td>
						<input name="channelName" id="channelName" type="text" style="width:150px;" readonly="true" onclick="return selChannel()"/><font color="red">*请用鼠标左键点击</font>
					</td>
				</tr>
				-->
				<input name="channelName" type="hidden" value="网上调查">
				<tr>
					<td align="right">
						所属部门：
					</td>
					<td>
							
												<!--分页显示开始,分页标签初始化-->
												
												<select name="depart_id">
												<OPTION value="">--请选择网上调查部门--</OPTION><font color="#FF0000">*</font>
												<pg:list statement="select id,disposedep from TD_COMM_EMAIL_DISPOSEDEP" dbname="bspf">
												<%
													String ID = dataSet.getString("id");
													String disposedep1=dataSet.getString("disposedep");
													if(depart.equals(ID))
													{
														check = "selected";
													}
													else
													{
														check="";
													}
												%>
												<option value='<%=ID%>' <%=check%>>
												<%=disposedep1%>
												</option>
												</pg:list>
												</select><font color="red">*</font>
					            			
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
					    <input type="checkbox" name="can_repeat" value="0"  onClick="return chkTimeGap();">
						相同IP不可重复投票 时间间隔
						<select name="selectGap" disabled>
							<option value="-1">
								无限
							</option>
							<option value="1">
								1
							</option>
							<option value="2">
								2
							</option>
							<option value="3">
								3
							</option>
							<option value="4">
								4
							</option>
							<option value="5">
								5
							</option>
							<option value="6">
								6
							</option>
							<option value="7">
								7
							</option>
							<option value="8">
								8
							</option>
							<option value="9">
								9
							</option>
							<option value="10">
								10
							</option>
							<option value="11">
								11
							</option>
							<option value="12">
								12
							</option>
							<option value="13">
								13
							</option>
							<option value="14">
								14
							</option>
							<option value="15">
								15
							</option>
							<option value="16">
								16
							</option>
							<option value="17">
								17
							</option>
							<option value="18">
								18
							</option>
							<option value="19">
								19
							</option>
							<option value="20">
								20
							</option>
							<option value="21">
								21
							</option>
							<option value="22">
								22
							</option>
							<option value="23">
								23
							</option>
							<option value="24">
								24
							</option>
						</select>小时
					</td>
				</tr>
				<tr>
			    	<td width="15%" align="right">
			    		创建时间：
			    	</td>
			    	<td>
			    	<%
			    	if(ctime.equals("")||(ctime==null)){%>
			    		<input type="text" name="ctime" id="ctime"  value="<%=todayTime%>" class="cms_text" readonly size="30"/>
			    	<%}else{%>
			    		<input type="text" name="ctime" id="ctime"  class="cms_text" readonly size="30"/>
			    	<%}%>
			    		<input name="button" type="button" onClick="showdatetime(document.all('ctime'))" value="..."/>
			    	</td>
			    </tr>
			    <%if(ctime.length()<10){%>
			    <tr>
					<td width="15%" align="right">
						选择图片：

					</td>
					<td>
						<input type="hidden" name="hiddenPath" value="<%=picPath%>">
						<input type="text" name="picpath" id="picpath"  class="cms_text" readonly size="30"/>
						<input type="button" name="addpic" onClick="chooseImage()" value="选择图片"/>
					</td>
				</tr>
				<%}else{%>
			    <tr>
			    	<td width="15%" align="right">
			    		主题图片：
			    	</td>
			    	<td>
			    		<input type="hidden" name="dispicpath" >
			    		<%if((picPath!=null)&&(!picPath.equals(""))){%>
			    		<img src="<%=basePath%>cms/voteManager/images/<%=picPath%>" height="100" width="100">
			    		<%}else{%>
			    		<img src="<%=basePath%>comm_front/images/nopic.jpg" height="100" width="150">
			    		<%}%>
			    	</td>
			    </tr>
			   
				<tr>
					<td width="15%" align="right">
						更改图片：
					</td>
					<td>
						<input type="hidden" name="hiddenPath" value="<%=picPath%>">
						<input type="text" name="picpath" id="picpath"  class="cms_text" readonly size="30"/>
						<input type="button" name="addpic" onClick="chooseImage()" value="选择图片"/>
					</td>
				</tr>
				 <%}%>
				 <tr>
					<td align="right">
						可投票时间段：

					</td>
					<td  align="left">
						<input type="text" name="timeCtrlStart" id="timeCtrlStart"  class="cms_text" readonly size="20"/>
			    		<input name="button" type="button" onClick="showdate(document.all('timeCtrlStart'))" value="..."/>&nbsp;到&nbsp;
						<input type="text" name="timeCtrlEnd" id="timeCtrlEnd"  class="cms_text" readonly size="20"/>
			    		<input name="button" type="button" onClick="showdate(document.all('timeCtrlEnd'))" value="..."/><font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right">
						可投票IP段：
					</td>
					<td align="left">
						<input type="button" name="Submit3" value="+" onclick="return addIpCtrl()" class="cms_button">
						<input type="button" name="Submit22" value="-" onclick="return delIpCtrl()" class="cms_button">
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						<table name="ipTable" id="ipTable">
							<tbody>
								
							</tbody>
						</table>
					</td>
				</tr>
				<tr><td align="right">
						问卷描述：
					</td>
					<td colspan="3"></td>
					</tr>
					<input type="hidden" name="content" value="<%=content%>"/>
					<input type="hidden" name="pics"/>
					<input type="hidden" name="flashs"/>
					<input type="hidden" name="medias"/>
					<input type="hidden" name="files"/>
					<tr>
					 <td colspan="4">
					  <iframe id="eWebEditor1" src="<%=request.getContextPath()%>/cms/editor/eWebEditor48/ewebeditor.htm?id=content&style=coolblue&cusdir=<%=docPath%>" frameborder="0" scrolling="no" width="95%" height="460"></iframe>
					 </td>
		            </tr>
				
					</table>
			</tab:tabPane>
			
		<tab:tabPane id="bar-question" tabTitle="题目信息">
			<table width="100%" border="0" id="qstTable" name="qstTable" cellpadding="0" cellspacing="0" bgcolor="#F3F4F9" >
				<tr>
					<td>
						<input type="button" name="Submit4" value="添加题目" onclick="return addQuestion()"  class="cms_button">
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" name='qstionTbl1' id='qstionTbl1'>
							<tr>
								<td align="right">
									<strong>题目：</strong>

								</td>
								<td>
									<input name="qstionTbl1Qstion" id="qstionTbl1Qstion" type="text" size="80"><input name="qstionTbl1Qstion_id" id="qstionTbl1Qstion_id" type="hidden" size=5><input name="qstionTbl1VoteCount" id="qstionTbl1VoteCount" type="hidden" size="5"><font color="red">*字数不能超过100</font>
								</td>
							</tr>
							<tr>
								<td align="right">
									回答方式：
								</td>
								<td>
									<input type="radio" name="qstionTbl1Style" id="qstionTbl1Style" value="0" onClick="return changeStyle('qstionTbl1')">
									单选
									<input type="radio" name="qstionTbl1Style" id="qstionTbl1Style" value="1" onClick="return changeStyle('qstionTbl1')">
									多选
									<input type="radio" name="qstionTbl1Style" id="qstionTbl1Style" value="2" onClick="return clearOption('qstionTbl1')">
									自由回答
								</td>
							</tr>
							<tr>
								<td>
									选项：
								</td>
								<td>
									<input type="button" name="qstionTbl1AddBtn" value="添加选项" onclick="return addOption('qstionTbl1')"  class="cms_button">
									<input type="button" name="qstionTbl1DelBtn" value="删除选项" onclick="return delOption('qstionTbl1')"  class="cms_button">
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
								<td>
									<table id="qstionTbl1OptinTbl" name="qstionTbl1OptinTbl">
										<tbody></tbody>
									</table>
								</td>
						</table>
					</td>
				</tr>
			</table>
			<table cellpadding="0" cellspacing="0" bgcolor="#F3F4F9" >
				<tr>
					<td>
						<input type="button" name="Submit4" value="添加题目" onclick="return addQuestion()"  class="cms_button">
					</td>
				</tr>
			</table>
			
			</tab:tabPane>
		</tab:tabContainer>
			
			
		</form>
		</base>
		<IFRAME id="biaoge" name="biaoge" width=0 height=0></IFRAME>
	</body>
	<script language="javascript">
	function selectValue(selCtrl,v){
		selCtrl = document.all(selCtrl);
		for(var i=0;i<selCtrl.options.length;i++){
			if (selCtrl.options[i].value == v){
				selCtrl.options[i].selected = true;
				return;
			}
		}
	}
	
	function checkRadiobox(radiobox,v){
		radiobox = document.getElementsByName(radiobox);
		for(var i=0;i<radiobox.length;i++){
			if (radiobox[i].value == v){
				radiobox[i].checked = true;
				return;
			}
		}
	}
	
	function getQstionTblScript(tblName){
		var questionTable = "<table width='100%'  name='"+tblName+"' id='"+tblName+"' ><tr><td ><strong>题目：</strong></td><td ><input name='"+tblName+"Qstion' id='"+tblName+"Qstion' type='text' size='80'><input name='"+tblName+"Qstion_id' id='"+tblName+"Qstion_id' type='hidden' value='0' size='5'><input name='"+tblName+"VoteCount' id='"+tblName+"VoteCount' type='hidden' value='0' size='5'><input type='button' name='Submit42' value='删除' onclick=\"return delQstion('"+tblName+"')\"  class='cms_button'></td></tr><tr><td>回答方式：</td><td><input type='radio' name='"+tblName+"Style' id='"+tblName+"Style' value='0' onClick='return changeStyle(\""+tblName+"\")'>单选<input type='radio' name='"+tblName+"Style' id='"+tblName+"Style' value='1' onClick='return changeStyle(\""+tblName+"\")'>多选 <input type='radio' name='"+tblName+"Style' id='"+tblName+"Style' value='2' onClick='return clearOption(\""+tblName+"\")'>自由回答</td></tr><tr><td>选项：</td><td><input type='button' name='"+tblName+"AddBtn' value='添加选项' onclick='return addOption(\""+tblName+"\")'  class='cms_button'><input type='button'name='"+tblName+"DelBtn' value='删除选项' onclick='return delOption(\""+tblName+"\")'  class='cms_button'></td></tr><tr><td>&nbsp;</td><td><table name='"+tblName+"OptinTbl' id='"+tblName+"OptinTbl'><tbody></tbody></table></td></table></td></tr></table>";
		return questionTable;
	}
	
	function getOptionTblScript(tblName){
		var op = "*<input type='checkbox' name='"+tblName+"Chkbx' id='"+tblName+"Chkbx' value='checkbox'><input name='"+tblName+"Option' id='"+tblName+"Option' type='text' size='50'><input name='"+tblName+"Option_id' id='"+tblName+"Option_id' type='hidden' value='0' size='5'><input name='"+tblName+"Count' id='"+tblName+"Count' type='hidden' value='0' size='5'>";
		return op;
	}
	
	document.all("qstionTbl1AddBtn").disabled = true;
	document.all("qstionTbl1DelBtn").disabled = true;
	<%
		
		if (id!=null){
			%>document.all("titleID").value="<%=id%>";<%
			if (oneTitle != null){
				%>document.all("titleName").value = "<%=oneTitle.getName()%>";
				document.all("ctime").value = "<%=oneTitle.getFoundDate().substring(0,10)%>";
				//document.all("channelName").value = "<%=oneTitle.getChannelName()%>";
				//document.all("channelid").value = "<%=oneTitle.getChannelID()%>";<%
				
				if (oneTitle.getIpRepeat()==0){
					%>
					document.all("can_repeat").checked=true;
					chkTimeGap();
					selectValue("selectGap","<%=oneTitle.getTimeGap()%>");
					<%
				}else
				{
				  %>
				  document.all("can_repeat").checked=false;
					chkTimeGap();
					//document.all("selectGap").disable=false;
					<%
				}
				
				List ipCtrls = oneTitle.getIpCtrls();
				if (ipCtrls!=null&&ipCtrls.size()>0){
					for (int i=0;i<ipCtrls.size();i++){
						IpCtrl ip = (IpCtrl)ipCtrls.get(i);
						%>addIpCtrl();<%
						if (i>0){
							%>document.all("ipCtrlStart")[<%=i%>].value = "<%=ip.getIpStart()%>";
							document.all("ipCtrlEnd")[<%=i%>].value = "<%=ip.getIpEnd()%>";<%
						}
						else{
							%>document.all("ipCtrlStart").value = "<%=ip.getIpStart()%>";
							document.all("ipCtrlEnd").value = "<%=ip.getIpEnd()%>";<%
						}
					}
				}
				
				
				List timeCtrls = oneTitle.getTimeCtrls();
				if (timeCtrls!=null&&timeCtrls.size()>0){
						TimeCtrl time = (TimeCtrl)timeCtrls.get(0);
						%>
							document.all("timeCtrlStart").value = "<%=time.getTimeStart()%>";
							document.all("timeCtrlEnd").value = "<%=time.getTimeEnd()%>";<%
				}
				
				List questions = oneTitle.getQuestions();
				Question  qstion = (Question)questions.get(0);
				%>document.all("qstionTbl1Qstion").value = "<%=qstion.getTitle()%>";
				checkRadiobox("qstionTbl1Style","<%=qstion.getStyle()%>");
				document.all("qstionTbl1VoteCount").value = "<%=qstion.getVotecount()%>";
				document.all("qstionTbl1Qstion_id").value="<%=qstion.getId()%>";<%
				if (qstion.getStyle()!=2 && qstion.getItems().size()>0){
					List items = qstion.getItems();
					Item item = (Item)items.get(0);
					%>document.all("qstionTbl1AddBtn").disabled = false;
					document.all("qstionTbl1DelBtn").disabled = false;
					addOption("qstionTbl1");
					document.all("qstionTbl1Option").value = "<%=item.getOptions()%>";
					document.all("qstionTbl1Count").value = "<%=item.getCount()%>";
					document.all("qstionTbl1Option_id").value = "<%=item.getId()%>";
					<%
					for (int j=1;j<items.size();j++){
						item = (Item)items.get(j);
						%>addOption("qstionTbl1");
						document.all("qstionTbl1Option")[<%=j%>].value = "<%=item.getOptions()%>";
						document.all("qstionTbl1Count")[<%=j%>].value = "<%=item.getCount()%>";
						document.all("qstionTbl1Option_id")[<%=j%>].value = "<%=item.getId()%>";<%
					}
				}
				
				for (int i=1;i<questions.size();i++){
					qstion = (Question)questions.get(i);
					%>addQuestion();
					var tblNm = "qstionTbl"+tableIndex;
					document.all(tblNm+"AddBtn").disabled = true;
					document.all(tblNm+"DelBtn").disabled = true;
					document.all(tblNm+"Qstion").value = "<%=qstion.getTitle()%>";
					checkRadiobox(tblNm+"Style","<%=qstion.getStyle()%>");
					document.all(tblNm+"VoteCount").value="<%=qstion.getVotecount()%>";
					document.all(tblNm+"Qstion_id").value="<%=qstion.getId()%>";<%
					if (qstion.getStyle()!=2 && qstion.getItems().size()>0){
						List items = qstion.getItems();
						Item item = (Item)items.get(0);
						%>document.all(tblNm+"AddBtn").disabled = false;
						document.all(tblNm+"DelBtn").disabled = false;
						addOption(tblNm);
						document.all(tblNm+"Option").value = "<%=item.getOptions()%>";
						document.all(tblNm+"Count").value = "<%=item.getCount()%>";
						document.all(tblNm+"Option_id").value = "<%=item.getId()%>";
						<%
						for (int j=1;j<items.size();j++){
							item = (Item)items.get(j);
							%>addOption(tblNm);
							document.all(tblNm+"Option")[<%=j%>].value = "<%=item.getOptions()%>";
							document.all(tblNm+"Count")[<%=j%>].value = "<%=item.getCount()%>";
							document.all(tblNm+"Option_id")[<%=j%>].value = "<%=item.getId()%>";<%
						}
					}
				}
			}
		}
	%>
	
	</script>
</html>
