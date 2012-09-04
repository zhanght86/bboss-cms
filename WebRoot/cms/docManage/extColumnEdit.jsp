<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.common.poolman.sql.*"%>
<%	
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);	
	String oldColumn = request.getParameter("col_name");
	DocumentExtColumnManager docUtil = new DocumentExtColumnManager();
	String tableName = "td_cms_document";
	String column_name = "";
	String column_type = "";
	String column_length = "";
	String column_comment = "";
	List list = docUtil.getExtColumns();
	for(int i=0;i<list.size();i++){
		ColumnMetaData str = (ColumnMetaData)list.get(i);
		if(str.getColumnName().toLowerCase().equals(oldColumn.toLowerCase())){
            column_name = str.getColumnName().toLowerCase();
            column_type = String.valueOf(str.getTypeName());
			column_length = String.valueOf(str.getColunmSize());
			column_comment = str.getRemarks();
			break;
		}
	}
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<title>修改扩展字段</title>
	<script src="../inc/js/func.js"></script>
	<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
	<style type="text/css">
		.STYLE1 {color: blue}
	</style>
</head>
<body scroll=no leftmargin=0> 
	<div id="divProcessing" style="position:absolute;width:40%;height:20px;z-index:1;
			background-color:#F0f0f0;layer-background-color:#F0f0f0;left:22%;top:80%;display:none;"  class="font">   
	    <marquee direction="left" width="250" behavior="alternate"><span class=STYLE1>正在保存中，请稍等……</span></marquee>
    </div>
	<form action="" method="post" name="editCommentForm">
	<input type="hidden" name="operate_type"/>
	<input type="hidden" name="old_name" value="<%=oldColumn%>"/>	
	<fieldset>
	   <LEGEND align=left>编辑扩展字段</LEGEND>
		<table width="98%" border="0" cellpadding="3" cellspacing="0"  class="Templateedittable">
		  <tr>		  
		    <td align="right" width="30%" height="20" nowrap>
				字段名称
			</td>
			<td align="left" height="20">
				<input type="text" name="column_name" value="<%=column_name.toLowerCase()%>" onblur="checkForm(this)" onafterpaste="checkForm(this)">
				<span style="color:red;">*必填项 以 ext_ 开头</span>
			</td>
		  </tr>		 
		  <tr>
		    <td align="right" height="20" nowrap>
				字段类型		<%=column_type.toLowerCase()%>		
			</td>
			<td align="left" height="20">
				<select name="column_type" id="column_type" style="width:153px" value="<%=column_type.toLowerCase()%>" onchange="prompt(this)">
					<option value="varchar2">varchar2</option>
					<option value="char">char</option>
					<option value="number">number</option>				
					<option value="date">date</option>
					<option value="long">long</option>
				</select>
				<span style="color:red;">*必填项</span>
			</td>
		  </tr>
		  <tr>
		    <td align="right" height="20" nowrap>
				字段长度
			</td>
			<td align="left" height="20">
				<input  type="text" name="column_length" onkeyup="checkForm(this)" value="<%=column_length%>" onafterpaste="checkForm(this)">
				<span id="star" style="color:red;">*必填项 最大长度为4000</span>
			</td>
		  </tr>
		  <tr>
		    <td align="right" height="20" nowrap>
				备注
			</td>
			<td align="left" height="20" rows=5>
				<textarea rows=5 name="column_comment" style="width:100%"><%=column_comment%></textarea>
			</td>
		  </tr>
		  <tr>
		      <td align="center" colspan="2" height="20">
			  		<input name="save" type="button" value="保   存" class="cms_button" onClick="saveit()" />
					<input name="close" type="button" value="关   闭" class="cms_button" onClick="closeit()" />
			  </td>
		  </tr>
		  <tr>
		  </tr>
		</table>
	</fieldset>
	</form>
	<div height=0 width=0 style="display:none">
		<iframe name="editColumnIframe"></iframe>
	</div>
</body>
<SCRIPT LANGUAGE="JavaScript">
<!--
    document.all("column_type").value="<%=column_type.toLowerCase()%>"
	function saveit(){	
		if(document.all("column_name").value.length<=0){
		    alert("请填写必填项!");
		    return false;
		}
		
		if((document.all("column_type").value=="varchar2" ||document.all("column_type").value=="char")){
			if(document.all("column_length").value.length<=0){
				alert("请填写必填项!");
			    return false;
			}
			
			if(document.all("column_length").value<=0){
				alert("字段长度必须是大于0!");
				return false ;
			}
		}
		
		if(!confirm("您确定要提交吗?")) return false;
		else{
			document.all("divProcessing").style.display="";
			document.all("operate_type").value="edit";
		    document.forms[0].action="extColumnOprate_do.jsp"
			document.frames[0].src="extColumnOprate_do.jsp";
			
	        document.forms[0].target="editColumnIframe";
			document.forms[0].submit();
		}
	}
	function closeit(){
		if(confirm("您确定要放弃吗?")){
	        window.close();
		}
	}
	function prompt(e){
		document.all("column_length").value="";
	    if(e.value=="varchar2" ){
	    	document.all("column_length").readOnly=false;
		    document.all("star").innerText="*必填项 最大长度为4000";
		}else if(e.value=="char"){
			document.all("column_length").readOnly=false;
            document.all("star").innerText="*必填项 最大长度为2000";
		}else if(e.value=="number"){
			document.all("column_length").readOnly=false;
            document.all("star").innerText="*必填项 最大长度为38";
		}else {
            if(e.value=="long") {
		        document.all("column_length").value="4000"
		        document.all("star").innerText="一个表中只能包含一个long列";
		    }else{		    
                document.all("star").innerText="";
		    }
		    document.all("column_length").readOnly=true;
		}
	}
	function checkForm(e){
	    if(e.name=="column_length"){
			if(e.value=="") return;
            var format = /^\d*$/;
			if(!format.test(e.value)){
			    alert("只能输数字");
				e.value="";
				e.focus();
			}
			if((document.all("column_type").value=="varchar2" && e.value>4000)||(document.all("column_type").value=="char" && e.value>2000) 
			||(document.all("column_type").value=="long" && e.value>4000)  || (document.all("column_type").value=="number" && e.value>38) ) {
			    alert("超出精度");
				e.value="";
				e.focus();
			}
		}else if(e.name=="column_name"){
			if(e.value=="") return;
            var format = /^ext_[A-Za-z0-9]*$/
			if(!format.test(e.value)){
			    alert("命名格式不对,以 ext_ 开头!");
				e.value="ext_";
				e.focus();
			}
			if(e.value.length>30){
			    alert("超出字段命名最大长度!");
				e.value="";
				e.focus();
			}
		}
	}
	function autoRun(){
	    var column_type = document.all("column_type").value;
	    if(column_type=="date" || column_type=="long"){	
	        if(column_type=="long")   document.all("column_length").value="4000";     
            document.all("column_length").readOnly=true;
            document.all("star").innerText="";
		}
	}
	autoRun()
//-->
</SCRIPT>
</html>