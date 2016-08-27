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
		td.innerHTML = "*<input type='checkbox' name='ipCtrlChlbx' value='checkbox'><input type='text' readonly name='ipCtrlStart'>&nbsp;到&nbsp;<input type='text' readonly name='ipCtrlEnd'>";
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
	
	String id = request.getParameter("id");
	String docpath = "";
	Title oneTitle = new Title();
	String picPath = "";
	String depart = "";
	String ctime = "";
	String check = "";
	String content = "";

		VoteManager voteMgr  = new VoteManagerImpl();
	    oneTitle = voteMgr.getSurveyBy(Integer.parseInt(id));
	    if(oneTitle!=null)
	    {
	    	picPath = oneTitle.getPicpath();
	    	ctime = oneTitle.getFoundDate();
	    	depart = oneTitle.getDepart_id();
	    	content = oneTitle.getContent();
	    	content = filterStr(content);
	    }
	%>
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
			<input name="channelid" type="hidden" value="433">
			
		<tab:tabContainer id="foo-survey-add" selectedTabPaneId="foo-survey">
			<tab:tabPane id="foo-survey" tabTitle="问卷信息">
			
			<table border="0" cellpadding="0" cellspacing="0" bgcolor="#F3F4F9" >
				<tr>
					<td align="right">
						问卷名称：

					</td>
					<td>
						<input name="titleName" type="text" maxlength="200" style="width:400px;" readonly><font color="red">*问卷标题字数不能超过100字</font>
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
						<input type="text" name="selectGap" size="10" readonly value="<%=oneTitle.getDepart_name()%>"/>	
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
						<input type="text" name="selectGap" size="10" readonly value="<%if(oneTitle.getTimeGap()==-1){out.print("无限");}else{out.print(oneTitle.getTimeGap()+"小时");}%>"/>
					</td>
				</tr>
				<tr>
			    	<td width="15%" align="right">
			    		创建时间：

			    	</td>
			    	<td>
			    	<%
			    	if((ctime==null)|| ctime.equals("")){%>
			    		<input type="text" name="ctime" id="ctime"  value="<%=todayTime%>" class="cms_text" readonly size="30"/>
			    	<%}else{%>
			    		<input type="text" name="ctime" id="ctime"  class="cms_text" readonly size="30"/>
			    	<%}%>
			    	</td>
			    </tr>
			    <tr>
			    	<td width="15%" align="right">
			    		主题图片：
			    	</td>
			    	<td>
			    		<input type="hidden" name="dispicpath" >
			    		<%if((picPath!=null)&&(!picPath.equals(""))){%>
			    		<img src="<%=basePath%>cms/voteManager/images/<%=picPath%>" height="100" width="100">
			    		<%}else{%>
			    		<img src="<%=basePath%>comm/images/nopic.jpg" height="100" width="150">
			    		<%}%>
			    	</td>
			    </tr>
				 <tr>
					<td align="right">
						可投票时间段：

					</td>
					<td  align="left">
						<input type="text" name="timeCtrlStart" id="timeCtrlStart"  class="cms_text" readonly size="20"/>
			    		&nbsp;到&nbsp;
						<input type="text" name="timeCtrlEnd" id="timeCtrlEnd"  class="cms_text" readonly size="20"/>
			    		<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td width="15%" align="right">
						可投票IP段：
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
						
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" name='qstionTbl1' id='qstionTbl1'>
							<tr>
								<td>
									<strong>题目：</strong>
								</td>
								<td>
									<input name="qstionTbl1Qstion" readonly id="qstionTbl1Qstion" type="text" size="80"><font color="red">*字数不能超过100</font>
								</td>
							</tr>
							<tr>
								<td align="right">
									回答方式：

								</td>
								<td>
									<input type="radio" name="qstionTbl1Style" id="qstionTbl1Style" value="0" >
									单选

									<input type="radio" name="qstionTbl1Style" id="qstionTbl1Style" value="1" >
									多选

									<input type="radio" name="qstionTbl1Style" id="qstionTbl1Style" value="2" >
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
		var questionTable = "<table width='100%'  name='"+tblName+"' id='"+tblName+"' ><tr><td ><strong>题目：</strong></td><td ><input name='"+tblName+"Qstion' readonly id='"+tblName+"Qstion' type='text' size='80'></td></tr><tr><td>回答方式：</td><td><input type='radio' name='"+tblName+"Style' id='"+tblName+"Style' value='0'>单选<input type='radio' name='"+tblName+"Style' id='"+tblName+"Style' value='1'>多选 <input type='radio' name='"+tblName+"Style' id='"+tblName+"Style' value='2'>自由回答</td></tr><tr><td>选项：</td><td><input type='button' name='"+tblName+"AddBtn' value='添加选项'  class='cms_button'><input type='button'name='"+tblName+"DelBtn' value='删除选项'  class='cms_button'></td></tr><tr><td>&nbsp;</td><td><table name='"+tblName+"OptinTbl' id='"+tblName+"OptinTbl'><tbody></tbody></table></td></table></td></tr></table>";
		return questionTable;
	}
	
	function getOptionTblScript(tblName){
		var op = "*<input type='checkbox' name='"+tblName+"Chkbx' id='"+tblName+"Chkbx' value='checkbox'><input name='"+tblName+"Option' id='"+tblName+"Option' type='text' size='50'>";
		return op;
	}
	
	document.all("qstionTbl1AddBtn").disabled = true;
	document.all("qstionTbl1DelBtn").disabled = true;
	<%
		
		if (id!=null){
			%>document.all("titleID").value="<%=id%>";<%
			if (oneTitle != null){
				%>document.all("titleName").value = "<%=oneTitle.getName()%>";
				document.all("ctime").value = "<%=oneTitle.getFoundDate()%>";
				//document.all("channelName").value = "<%=oneTitle.getChannelName()%>";
				//document.all("channelid").value = "<%=oneTitle.getChannelID()%>";<%
				if (oneTitle.getIpRepeat()==0){
					%>
					document.all("can_repeat").checked=true;
					<%
				}else
				{
				  %>document.all("can_repeat").checked=false;
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
				if (questions!=null && questions.size() > 0) {
					Question  qstion = (Question)questions.get(0);
					%>document.all("qstionTbl1Qstion").value = "<%=qstion.getTitle()%>";
					checkRadiobox("qstionTbl1Style","<%=qstion.getStyle()%>");<%
					if (qstion.getStyle()!=2 && qstion.getItems().size()>0){
						List items = qstion.getItems();
						Item item = (Item)items.get(0);
						%>document.all("qstionTbl1AddBtn").disabled = false;
						document.all("qstionTbl1DelBtn").disabled = false;
						addOption("qstionTbl1");
						document.all("qstionTbl1Option").value = "<%=item.getOptions()%>";<%
						for (int j=1;j<items.size();j++){
							item = (Item)items.get(j);
							%>addOption("qstionTbl1");
							document.all("qstionTbl1Option")[<%=j%>].value = "<%=item.getOptions()%>";<%
						}
					}
				
					for (int i=1;i<questions.size();i++){
						qstion = (Question)questions.get(i);
						%>addQuestion();
						var tblNm = "qstionTbl"+tableIndex;
						document.all(tblNm+"AddBtn").disabled = true;
						document.all(tblNm+"DelBtn").disabled = true;
						document.all(tblNm+"Qstion").value = "<%=qstion.getTitle()%>";
						checkRadiobox(tblNm+"Style","<%=qstion.getStyle()%>");<%
						if (qstion.getStyle()!=2 && qstion.getItems().size()>0){
							List items = qstion.getItems();
							Item item = (Item)items.get(0);
							%>document.all(tblNm+"AddBtn").disabled = false;
							document.all(tblNm+"DelBtn").disabled = false;
							addOption(tblNm);
							document.all(tblNm+"Option").value = "<%=item.getOptions()%>";<%
							for (int j=1;j<items.size();j++){
								item = (Item)items.get(j);
								%>addOption(tblNm);
								document.all(tblNm+"Option")[<%=j%>].value = "<%=item.getOptions()%>";<%
							}
						}
					}
				}
			}
		}
	%>
		
	</script>
</html>
