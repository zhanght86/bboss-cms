<%
/**
  *	上传文档相关附件
  */
%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<pg:null actual="${errormessage}">
<SCRIPT LANGUAGE="JavaScript">
	//调用父窗口的结束方法
	var fname = "${fileAllName}";
	parent.UploadLoaded(fname);
</SCRIPT>
</pg:null>

<<pg:notnull actual="${errormessage}">
<SCRIPT LANGUAGE="JavaScript">
	alert("上传文件失败：${errormessage}");
	window.close();
</SCRIPT>
</pg:notnull>

