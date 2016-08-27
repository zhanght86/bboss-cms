<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="com.frameworkset.platform.cms.votemanager.*"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>修改投票项目</title>

		<link rel="stylesheet" type="text/css" href="STYLE.CSS">
	</head>

	<script type="text/javascript" src="/public/datetime/calender_date.js" language="javascript"></script>

	<body topmargin="1" rightmargin="1" scroll=no leftmargin="1">

		<html:form method="post" action="voteAction.do">
			<html:hidden property="itemString" />
			<html:hidden property="titleId" />
			<html:hidden property="actionType" />
			<html:hidden property="info" />


			<table  border="1" cellpadding="0" align="center" cellspacing="0" class="query_box">
				<tr>
					<td width="100%" height="18">
						<P align="center">
							<b>修改投票项目 
						</P>
					</td>
				</tr>
				<tr>
					<td height="25">
						主题：
						<html:text property="title" size="38" />
					</td>
				</tr>
				<tr>
					<td height="25">
						方式：
						<html:radio property="style" value="radiostyle" />
						单选&nbsp;
						<html:radio property="style" value="checkboxstyle" />
						多选
					</td>
				</tr>
				<tr>
					<td height="25">
						开始时间：
						<html:text property="startDate" readonly="true" />
						<input name="button" type="button" onClick="showdate(document.all('startDate'))" value="..." />
						<!--					<html:text property="startDate" readonly="true" onfocus="return showdate(document.all('startDate'))"/>-->
					</td>

				</tr>
				<tr>
					<td height="25">
						结束时间：
						<html:text property="toDate" readonly="true" />
						<input name="button" type="button" onClick="showdate(document.all('toDate'))" value="..." />
					</td>
				</tr>
				<TR><TD>
				
				<table id="tbl" name="tbl">
				
				<script language="javascript">
					var items = document.voteForm.itemString.value.split(";");
					for (var i=0;i<items.length-1;i++){
				      var option=items[i].split(",");
				      document.write("<tr><td>选项：<input type='checkbox' value='" + option[0] + "' id='ckbxValues' name='ckbxValues'><input type='text' name='options'id='options' value='" + option[1] + "'></td></tr>");
				    
				   }
				</script>
				
				</table>
			    </td></tr>
				<tr><td>
				</td></tr>
				<tr>
					<td height="25">
						设为当前调查项目？
						<html:radio property="active" value="1" />
						是
						<html:radio property="active" value="0" />
						否
					</td>
				</tr>
				<tr bordercolor="#eeeee" bgcolor="#336699">
					<td height="25" align="center">
						<input type="button" value="删除选项" name="delete" onclick="return deleteFnc();">
						<input type="button" value="新增选项" name="new" onclick="return additem();">
						<input type="button" value="保存" name="save" onclick="return saveIt();">
						<input type="reset" value="取消" name="cancel">
					</td>
				</tr>
			</table>
		</html:form>
		
		
<script language="javascript">

function  additem()
{
	var etable = document.all("tbl");
	var row1 = etable.insertRow();
	var td1 = document.createElement("td");
	td1.innerHTML = "选项：<input type='checkbox' value='' name='ckbxValues' ><input type='text' name='options' value=''>";
	row1.appendChild(td1);
}

function deleteFnc(){

	var haha = document.getElementsByName("ckbxValues");
	for (var i=0;i<haha.length;i++){
		if (haha[i].checked){
			var tbody1= haha[i].parentElement.parentElement.parentElement;
			tbody1.removeChild(haha[i].parentElement.parentElement);
		}
	}
	//return window.open('<%=request.getContextPath()%>/voteAction.do?actionType=delete');
}

function saveIt(){
	
	var ids = document.getElementsByName("options");
	document.voteForm.itemString.value = "";
	for(var i=0;i<ids.length;i++){
		document.voteForm.itemString.value += ids[i].value+";";
	}
	document.voteForm.actionType.value='save';
	document.voteForm.submit();
}

if (document.voteForm.info.value!=""){
	alert(document.voteForm.info.value);
	//window.parent.location.reload();
	//window.close();
}  
</script>
	</body>
</html>
