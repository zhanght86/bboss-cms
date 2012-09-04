<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.frameworkset.platform.cms.addressmanager.*"%>
<%
	String[] addressid = request.getParameterValues("checkBoxOne");
	boolean flag = false;
	if (addressid != null) {
		flag = new AddressManagerImpl().deleteAddress(addressid);
	}
	if(flag){
%>
		<script language="javascript" >
			alert("删除成功!");
			top.sreachFrame.document.all.form1.action = "address_list.jsp?isAll=yes";
			top.sreachFrame.document.all.form1.submit();
		</script>
<%
	}else{
%>
		<script language="javascript" >
			alert("删除失败!");
		</script>
<%
	}
%>	