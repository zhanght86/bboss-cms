<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" %>
<%
for(int i = 0; i < 1000; i ++)
{
	System.out.println("i=" + i);
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CMS 防止二次提交模板处理页面</title>
</head>
<script language="javascript">

			parent.win.alert("操作成功！");
			parent.enable();
			parent.closewin();
</script>

</body>
</html>
