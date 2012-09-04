<%
/*
 * <p>Title: 岗位新增页面</p>
 * <p>Description: 岗位新增页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-18
 * @author liangbing.tao
 * @version 1.0
 */
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
%>
<html>
	<head>
		<title>新增岗位</title>
		<style type="text/css">
			<!--
			.style1 {color: #CC0000}
			-->
		</style>
		<SCRIPT language="JavaScript" SRC="../include/validateForm.js"></SCRIPT>
		<script type="text/javascript" src="../../include/jquery-1.4.2.min.js"></script>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
		<SCRIPT LANGUAGE="JavaScript"> 
			var api = frameElement.api, W = api.opener;
			function trim(string)
			{
			  var temp="";
			  string = ''+string;
			  splitstring = string.split(" ");
			  for(i=0;i<splitstring.length;i++)
			  {
			    temp += splitstring[i];
			  } 
			  return temp;
			}
					
			function savejob() 
			{
				var form = document.forms[0];
				var jobname=form.jobname.value;
				var jobNumber=form.jobNumber.value;
				var jobRank=form.jobRank.value;
				var jobAmount=form.jobAmount.value;
				var jobFunction=form.jobFunction.value;
				var jobDesc=form.jobdesc.value;
				var jobCondition=form.jobCondition.value;
				
				if (trim(jobname).length == 0 )
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmnage.input.job.name'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				    return false;
			    }
				
				if (trim(jobNumber).length ==0)
					{
						W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.input.job.number'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
						return false;
					}
			    
			    //if (trim(jobNumber).length == 0 )
			    //{
				//    alert("请录入岗位编号！"); 
				//    return false;
			   // }  
			   // if(jobname.search(/[\\\/\|:\*\?<>"']/g)!=-1)
			   // {
				//	alert("岗位名称不能有\\/|:*?<>\"'!等特殊字符");
				//	form.jobname.focus();
					//return;
				//}
			   
			   var reg = /^[A-Za-z0-9\u4e00-\u9fa5]+$/;
			   if(!reg.test(jobname))
				   {
				   	   W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.name.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					   return false;
				   }
			   
				var reg_Number =  /^[a-zA-Z]\w*$/;
				if(!reg_Number.test(jobNumber))
					{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.number.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					form.jobNumber.focus();
					return false;
					}
				if(jobRank == "" || jobRank.length<1 || jobRank.replace(/\s/g,"") == "")
				{
				
				}else{
					if(!reg.test(jobRank))
					{
						W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.level.invali'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
						form.jobRank.focus();
						return;
					}
				}
				if(jobAmount == "" || jobAmount.length<1 || jobAmount.replace(/\s/g,"") == "")   
				{
				
				}else{
					if(!reg.test(jobAmount))
					{
						W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.establishment.invalid'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
						form.jobAmount.focus();
						return;
					}
				}
				if(jobname.length>100)
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.name.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return;
				}
				if(jobNumber.length>100)
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.number.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return;
				}
				if(jobRank.length>100)
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmange.job.level.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return;
				}
				if(jobAmount.length>100)
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.establishment.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return;
				}
				if(jobFunction.length>200)
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.duty.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return;
				}
				if(jobDesc.length>200)
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.description.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return;
				}
				if(jobCondition.length>200)
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.jobmanage.job.condition.long'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return;
				}
			    
				var form = document.forms[0];
				
				//document.all.divProcessing.style.display = "block";
				
				//document.all.Submit.disabled = true;
				//document.all.Reset.disabled = true;
				//document.all.BackButton.disabled = true;
				
			  	form.action = "addjob_do.jsp";
				form.target = "hiddenFrame";
				form.submit();				
			}
			
			function doreset() {
				$("#reset").click();
			}
		</SCRIPT>
	</head>
	<body class="contentbodymargin" scroll="no">
		<div id="" align="center">
			<form action="" method="post">  
				<table width="100%" height="223" border="0" align="center" cellpadding="0" cellspacing="1" class="table2">
				  <tr>
				    <th> <pg:message code="sany.pdp.jobmanage.job.name"/>： <span class="style1">*</span></th>
				    <td width="" class="detailcontent">
				    	<input type="text" name="jobname" size="40" maxlength="20"/>
				    </td>
				  </tr>
				  <tr>
				     <th><pg:message code="sany.pdp.jobmanage.job.number"/>：  <span class="style1">*</span></th>
				    <td class="detailcontent">
				    	<input type="text" name="jobNumber" size="40" maxlength="20"/>
				    </td>
				  </tr>
				   <tr>
				     <th> <pg:message code="sany.pdp.jobmanage.job.level"/>： </td>
				    <td class="detailcontent">
				    	<input type="text" name="jobRank" size="40" maxlength="20"/>
				    </td>
				  </tr>
				   <tr>
				     <th> <pg:message code="sany.pdp.jobmanage.job.establishment"/>： </th>
				    <td class="detailcontent">
				    	<input type="text" name="jobAmount" size="40" maxlength="20"/>
				    </td>
				  </tr>
				  <tr>
				    <th> <pg:message code="sany.pdp.jobmanage.job.duty"/>： </th>
				    <td class="detailcontent">
				    	<textarea name="jobFunction" cols="50" rows="4"/></textarea>
				    </td>
				  </tr>
				  <tr>
				     <th> <pg:message code="sany.pdp.jobmanage.job.description"/>： </th>
				    <td class="detailcontent">
				    	<textarea name="jobdesc" cols="50" rows="4"/></textarea>
				    </td>
				  </tr>
				  <tr>
				     <th> <pg:message code="sany.pdp.jobmanage.job.condition"/>： </th>
				    <td class="detailcontent">
				    	<textarea name="jobCondition" cols="50" rows="4"/></textarea>
				    </td>
				   </tr>
				</table>
				<div align="center">
					 <a class="bt_1" onClick="savejob()"><span><pg:message code="sany.pdp.common.operation.save"/></span></a>
					 <a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
					 <input type="reset" id="reset" style="display:none"/>
				</div>
			</form>
		</div>
		
		<div id=divProcessing style="width:200px;height:30px;position:absolute;left:200px;top:260px;display:none">
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
		<iframe name="hiddenFrame" width="0" height="0"></iframe>
	</body>
</html>

