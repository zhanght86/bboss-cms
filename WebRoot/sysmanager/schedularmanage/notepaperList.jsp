<%@include file="../include/global1.jsp"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@include file="../base/scripts/panes.jsp"%>
<%@taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.SchedularManagerImpl"%>
<html>
	<head>
		<title>便笺列表</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="../scripts/common.js" type="text/javascript"></script>
		<script language="JavaScript">
		var jsAccessControl = new JSAccessControl("#ff0000","#ffffff","#eeeeee");
		function validatecheck(checkName)
		{
		    var o = document.all.item(checkName);
		    for (var i=0; o && o.length && i<o.length; i++)
	    	{
				if(o[i].checked)
					return true;
			}
			if(o && !o.length)
		    {
			    if(o.checked)
			    return true;
		    }
		   return false;
		}
		function validateOnlyCheck(checkName)
		{
	    	var o = document.all.item(checkName);
	    	for (var i=0,j=0; o && o.length && i<o.length; i++)
	    	{
			   	if(o[i].checked)
			   	{
			   		j++;
			   	}
			   	if(j == 2)
			   	{
			   		return false;
			   	}
			}
			return true;
		}
		
		function add(){    
			//修改 By xiling.Gong 2008.3.10
			var strContent=document.all.item("content").value;
			if(strContent==""){
				alert("请填写便笺内容");
				return;
			}
			if(lengthC(strContent)>1024){
				if(!confirm("最大字符数 1024 个，是否截掉多余字符?")){
					document.all.item("content").focus();
					return;
				}
			}
			strContent=subStrC(strContent,1024);
			document.all.item("content").value=strContent;
			//xiling.Gong修改结束--------------
			
			SchedularForm.action="../schedularmanage/schManager.do?method=addNotepaper";
			SchedularForm.submit();		
		}
		
		function getNotepaper(checkName)
		{
			if(!validatecheck(checkName))
			{
				alert("请选择要修改的便笺");
				return;
			}
			if(!validateOnlyCheck(checkName))
			{
				alert("一次只能选择一个便笺进行修改");
				return;
			}	
			SchedularForm.action="../schedularmanage/schManager.do?method=getNotepaper";
			SchedularForm.submit();
		}
		function checkAll(totalCheck,checkName)
		{	//复选框全部选中
		   var selectAll = document.all.item(totalCheck);
		   
		   var o = document.all.item(checkName);
		   
		   if(selectAll.checked==true){
			   for (var i=0; o && o.length && i<o.length; i++)
			   {
		      	  if(!o[i].disabled)
		      	  {
		      	  	o[i].checked=true;
		      	  }
			   }
			   if(o && !o.length && !o.checked)
			   {
			   		o.checked=true;
			   }
			   
		   }else{
			   for (var i=0; o && o.length && i<o.length; i++){
		   	  	  o[i].checked=false;
		   	   }
		   	   if(o && !o.length && !o.checked)
				{
					o.checked=false;
				}
		   	   
		   }
		}
		//单个选中复选框
		function checkOne(totalCheck,checkName){
		   var selectAll = document.all.item(totalCheck);
		   var o = document.all.item(checkName);
		   var cbs = true;
		   for (var i=0;o && o.length && i < o.length;i++){
				if(!o[i].disabled){
					if (o[i].checked==false){
						cbs=false;
					}
				}
			}
			if(o && !o.length && !o.checked)
			{
				cbs = false;
			}
			if(cbs == true){
				selectAll.checked=true;
			}else{
				selectAll.checked=false;
			}
		}	
		
		function dealRecord(checkName,dealType) {
		    if(!validatecheck(checkName))
			{
				alert("请选择要删除的便笺");
				return false;
			}
		    	if (dealType==1){
		    		outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
		        	if (confirm(outMsg)){
			        	SchedularForm.action="../schedularmanage/schManager.do?method=deleteNotepaper";
						SchedularForm.submit();
				 		return true;
					}
				} 
		    return false;
		}
		
		function actionOnmouseover(e){	
			e.style.backgroundColor = "#8080FF";
		}
	
		function actionOnmouseup(e){
			e.style.backgroundColor = "#BEC0C2";
		}
		
		//添加 By  xiling.Gong 2008.3.10-----------------------------------
		function lengthC(str){
		// 该函数用来获取中英文混合字符串的实际长度
		// 中文字符长度为2，英文字符长度为1
			var i=0;              // 当前扫描指针位置
			var n=0;              // 当前已得到的长度
			for(i=0;i<=str.length-1 ;i++ ){	
				n+=((str.charCodeAt(i)>255)?2:1);
			}
			return n;
		}
	    function subStrC(str,n){
		//该函数用来从包含中文的字符串中截取从第1个字符开始向后长度为n的子字符串
		//其中长度是指 中文字符长度为2 英文字符长度为1 的所有字符的长度总和
			var i;					  //当前扫描指针位置
			var j=0;			      //已获得的字符长度
			var retStr="";            //将要返回的子串
			if(lengthC(str)<=n){
				return str;			  //原样返回
			}
			for(i=0;i<=n-1,j<n;i++){
				if(str.charCodeAt(i)>255){
					j+=2;
					if(j>n){	      //若加入当前字符,长度将大于限定长度，舍弃
						return retStr;
					}else{
						retStr=retStr+str.charAt(i);
					}
				}else{
					j++;
					retStr=retStr+str.charAt(i);
				}
			}
			return retStr;
		}
		//xiling.Gong添加结束---------------------------------------------
</script>
	<!-- <body  oncontextmenu="return false;" scrolling="no">-->
	<body class="contentbodymargin" scroll="no">
		<div id="contentborder" align="center">
			<FORM name="SchedularForm" action="" method="post">
				<table width="100%" border="0" cellpadding="0" cellspacing="2" class="thin">
					<tr>
						<td height="23" class="detailtitle" width="40%" align="center">
							<strong> 便笺内容(最多输入1024个字符)</strong>
						</td>
						<td></td>
					</tr>
					<tr>
						<td height="23" width="29%">
							<textarea cols="40" name="content"  rows="6"></textarea>
						</td>
					</tr>
					<tr>
						<td>
							<input type="button" class="input" name="addNote" value="保存" onClick="add()">
						</td>
					</tr>
				</table>
				<TABLE width="100%" cellpadding="1" cellspacing="1" bordercolor="#EEEEEE" class="thin">
					<!--分页显示开始,分页标签初始化-->
					<TR>
						<TD height="30" class="detailtitle" align="center" colspan="3">
							<B>便笺列表</B>
						</TD>
					</TR>
					<pg:listdata dataInfo="NotepaperList" keyName="NotepaperList" />
					<pg:pager maxPageItems="10" scope="request" data="NotepaperList" isList="false">
						<td class="headercolor">
							<input type="checkBox" name="checkBox" onClick="checkAll('checkBox','ID')">
						</td>
						<Td class="headercolor">
							最后修改时间
						</Td>
						<Td class="headercolor">
							内容
						</Td>
						<pg:list>
							<tr class="" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'">
								<td class="tablecells" width="5%" nowrap="nowrap">
									<input type="checkBox" name="ID" onClick="checkOne('checkBox','ID')" value="<pg:cell colName="notepaparID" defaultValue=""/>">
								</td>
								<td class="tablecells" width="20%" nowrap="nowrap">
									<pg:equal colName="time" dateformat="yyyy-MM-dd HH:mm:ss" value=""></pg:equal>
									<pg:cell colName="time" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="" />
								</td>
								<td class="tablecells" width="90%" >
									<pg:equal colName="content" value=""> 便笺无内容
									</pg:equal>
									<pg:cell colName="content" defaultValue="" />
								</td>
							</tr>
						</pg:list>
						<tr height="18px" class="labeltable_middle_tr_01">
							<td colspan=5 align='center' nowrap="nowrap">
								<pg:index />
								<input type="button" class="input" name = "delete" value="删除便笺" onclick="javascript:dealRecord('ID',1); return false;">
								<INPUT type="button" class="input" name="modifynote" value="修改便笺" onclick="getNotepaper('ID')">

							</td>
						</tr>
						<input name="queryString" value="<pg:querystring/>" type="hidden">
					</pg:pager>
				</TABLE>
			</FORM>
		</div>
		<%@include file="../sysMsg.jsp" %>
	</body>
</html>

