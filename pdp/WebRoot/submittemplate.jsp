<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" %>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CMS 防止二次提交模板</title>
</head>
<script language="javascript">
	//动画窗口对象
	var win = null;
	//关闭动画窗口
	function closewin()
	{
		win.close();
	}
	
	function unable()
	{
		var buttons = document.getElementsByTagName("input");
		for(var i=0;i<buttons.length;i++)
		{

			buttons[i].disabled = true;
		}
		
	}
	function enable()
	{
		var buttons = document.getElementsByTagName("input");
		for(var i=0;i<buttons.length;i++)
		{
			buttons[i].disabled = false;
		}
	}
	function save()
	{
		unable();
		document.forms[0].action = "handle.jsp";
		document.forms[0].submit();
		win = window.open("<%=request.getContextPath()%>/doing.html","doinghtml","height="+(screen.availHeight-200)+",width="+(screen.availWidth-300)+",top=100,left=150,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no");
		//win = window.showModalDialog("<%=request.getContextPath()%>/cms/doing.html","height="+(screen.availHeight-200)+",width="+(screen.availWidth-300)+",top=100,left=150,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no");
		
	}
	
</script>
<body>
<TABLE>
<form name="test" method="post" target="handle">

<tr>
		<td  width="95%" height="22"  align="center" valign="top">
  				<input type="button" value="提交" name="save1" onclick="save()">
  				<input type="text" value="名称" name="name" >
  				  				<input type="text" value="名称1" name="name1" >
		</tr>

</TABLE>

</from>
<iframe width="0" height="0" name="handle" ></iframe>
</body>
</html>
