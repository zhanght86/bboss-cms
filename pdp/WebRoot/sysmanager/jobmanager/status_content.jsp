<%@include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>

<title>状态内容</title>
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/futuretable.css">
</head>
<body class="contentbodymargin">
<div id="contentborder">
<TABLE id=testTable style="behavior:url(../css/FutureTableAct.htc);width:98%; background-color:#FFFFFF;border-collapse:collapse;"
borderColor=#B8B4A3 cellSpacing=0 cellPadding=2  SortType=System basePath="../" showStatus=false dragcolor='gray' slcolor=#CCCCCC
Editable=false Sortable=true HasQueryRow=false sortRowColor="black"  MoveColable=false SetRowNumber=true idColNums=1 nameColNum=2  align=center>
<THEAD>
<TR>
	<td width="24">#</td>
	<td width="160" title="排序">时间</td>
	<td width="100" title="排序">操作人</td>
	<td title="排序">操作</td>
</TR>
</THEAD>
<TBODY>


</TBODY>
   <TFOOT>
    <tr>
    <TD colspan=4 align="right">
	<span id="tableStatus" style="width: 100%; overflow: hidden; font-family: 'Verdana'; font-size: 12"
				title="提示：按住“Shift”或“Ctrl”键多选表格行，使用右击菜单操作选中的记录。">请选择:</span>
	</TD>
   </tr>
  </TFOOT>
</TABLE>
</div>
</body>
</html>

