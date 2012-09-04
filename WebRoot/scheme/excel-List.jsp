<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>导入列表</title>
		<meta http-equiv="pragma" content="no-cache"></meta>
		<meta http-equiv="cache-control" content="no-cache"></meta>
		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/common/css/button.css">
			<link rel="stylesheet" type="text/css"
				href="<%=request.getContextPath()%>/common/css/css.css">
				<link rel="stylesheet" type="text/css"
					href="<%=request.getContextPath()%>/common/css/table.css">
					<link rel="stylesheet" type="text/css"
						href="<%=request.getContextPath()%>/common/css/input.css">
		<script type="text/javascript">
var basePath = '<%=request.getContextPath()%>';
function showSending(url) {
	document.form0.action = url;
	document.form0.submit();
}
function reinitIframe() {
	var iframe = parent.document.getElementById("excelListFrame");
	try {
		var bHeight = iframe.contentWindow.document.body.scrollHeight;
		var dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
		var height = Math.max(bHeight, dHeight);
		iframe.height = height;
	} catch (ex) {
	}
}
window.setInterval("reinitIframe()", 200);

function reloadCardList() {
    var schemeId = '<s:property value="tdPwSchemeTemp.schemeId"/>';
	document.form0.target = "queryListSub";
	document.form0.action = basePath + "/scheme/personal_list.jsp?schemeId="+schemeId+"&date="
			+ new Date().getTime();
	document.form0.submit();
}
</script>
	</head>
	<body>
		<form action="" method="post" name="form0" id="form0">
			<table width="100%" id="listtable" border="0" cellspacing="0"
				cellpadding="0" class="Ctable">
				<thead>
					<tr>
						<td colspan="4" class="taobox22">
							成功导入
							<span style="font-weight: bold;"> <s:property
									value="#request.rightList.size()" /> </span>条记录, 导入失败
							<span style="font-weight: bold; color: red;"> <s:if
									test="#request.typeZero==0">
									<s:property value="#request.typeZero" />
								</s:if> <s:else>
									<s:property value="#request.errorList.size()" />
								</s:else> </span>条记录
						</td>
					</tr>

					<tr>
						<td align="center" style="width: 3%;">
							行号
						</td>
						<td align="center" style="width: 8%;">
							身份证号码
						</td>
					</tr>
					<s:iterator id="kind" value="#request.errorList">
						<s:if test="count==0">
							<tr>
								<td align="center" colspan="10" class="mb">
									&nbsp;
									<span style="color: red;"> <s:property value="%{idCard}" />
									</span>
								</td>
							</tr>
						</s:if>
						<s:else>
							<tr style="cursor: pointer;"
								onmouseout="this.className='tr_hover'"
								onmouseover="this.className='tr_hover_over'">
								<td align="center" class="mb" width="3%">
									&nbsp;
									<s:property value="%{count}" escape="false" />
								</td>
								<td align="center" class="mb" width="8%">
									&nbsp;
									<s:property value="%{idCard}" escape="false" />
								</td>
							</tr>
						</s:else>
					</s:iterator>
			</table>
		</form>
	</body>
</html>
<script type="text/javascript">
<!--
var tabs = document.getElementsByName("tab") ;

 
 
function $() {

    for(var j=0;j<tabs.length;j++){
       var Ptr = tabs[j].getElementsByTagName("tr");
  
      for (i=1;i<Ptr.length+1;i++) { 
         Ptr[i-1].className = (i%2>0)?"t1":"t2"; 
      }
   }
   reloadCardList();
}
window.onload=$;
for(var j=0;j<tabs.length;j++){
  var Ptr = tabs[j].getElementsByTagName("tr");
 
for(var i=0;i<Ptr.length;i++) {
    Ptr[i].onmouseover=function(){
    this.tmpClass=this.className;
    this.className = "t3";
    
    };
    Ptr[i].onmouseout=function(){
    this.className=this.tmpClass;
    };
}
}
//-->
</script>

