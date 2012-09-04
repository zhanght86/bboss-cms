<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.dictionary.DictManager"%>
<%@page import="com.frameworkset.platform.dictionary.DictManagerImpl"%>
<%@page import="java.util.List"%>
<%@page import="com.frameworkset.dictionary.Item"%>

<%
	String dtypeId = request.getParameter("dtypeId");
	String coulumnName = request.getParameter("columnName"); 
	String selectedId = request.getParameter("selectedId")==null?"":request.getParameter("selectedId");
	//System.out.println("selectedId = " + selectedId);
	DictManager dictManager = new DictManagerImpl();
	List dictdatas = dictManager.getDictdataList(dtypeId);
%>
<html>
<head></head>
<script language="javascript">
function dictHiddenValue(obj){
	var parentColumn = "<%=coulumnName%>";
	var dictvalue = obj.options[obj.selectedIndex].value;
	parent.document.all.item(parentColumn).value = dictvalue; 
}

window.onload = function initPage(){
    var selectedId = "<%=selectedId%>";
    var coulumnName = "<%=coulumnName%>";
    if(selectedId!=""){
    	document.all.item(coulumnName).value = selectedId; 
    }
}
</script>
<body>
<table>
<tr>
<td>
<select name="<%=coulumnName %>" onchange="dictHiddenValue(this);">
	<option value="">--请选择--</option>
	<% 
		for(int i = 0; dictdatas != null && i < dictdatas.size(); i++){
			Item dictdata = (Item)dictdatas.get(i);
	%>
	<option value="<%=dictdata.getItemId() %>"><%=dictdata.getItemId() %> <%=dictdata.getName() %></option>
	<% 
		}
	%>
</select>
</td>
</tr>
</table>
</body>
</html>
