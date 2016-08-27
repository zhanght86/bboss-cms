<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%//从form1表单选项个数中获取n值，如果没有就给一个默认值4
			String n = request.getParameter("n");
			if (n == null) {
				n = "4";
			}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>添加投票项目</title>
<link rel="stylesheet" type="text/css" href="STYLE.CSS">
</head>

<body>
<table width="100%" border="1" align="center" cellpadding="0"
	cellspacing="1" class="query_box" >

	<tr>
		<td width="746" height="349" >
		<form name="form1" method="post" action="addaction.jsp">
		<table width="70%" height="224" border="1" align="center"
			cellpadding="0" cellspacing="0">
			<tr>
				<td width="100%" height="18">
									<P align="center">
										<h5>新增投票项
									</P>
								</td>
			</tr>
			<tr>
				<td height="25" >选项个数： <input type="text" name="n"
					size="6" onchange="location.href='addVote.jsp?n='+form1.n.value;"
					value="<%=n%>">&nbsp; <!?-过onchange方法实现改变选项个数值，并传给上面的变量n，从而动态地改变选项个数-->
				<input type="button" value="确 定" name="B3"></td>
			</tr>
			<tr>
				<td height="25" >主题： <input type="text"
					name="title" size="38"></td>
			</tr>
			<tr>
				<td height="25" >方式： <input type="radio"
					value="radio" name="style" checked>单选&nbsp; <input type="radio"
					value="checkbox" name="style">多选</td>
			</tr>
			<tr>
				<td height="25" >开始时间： <select size="1"
					name="startyear">
					<option value="2005" selected>2005</option>
					<%for (int i = 6; i < 10; i++) {
				out.println("<option value=200" + i + ">200" + i + "</option>");
			}

			%>
				</select>年<select size="1" name="startmonth">
					<option value="1" selected>1</option>
					<%for (int i = 2; i < 13; i++) {
				out.println("<option value=" + i + ">" + i + "</option>");
			}

			%>
				</select>月<select size="1" name="startday">
					<option value="1" selected>1</option>
					<%for (int i = 2; i < 32; i++) {
				out.println("<option value=" + i + ">" + i + "</option>");
			}

			%>
				</select>日&nbsp;&nbsp;</td>
			</tr>
			<tr>
				<td height="25" >结束时间： <select size="1"
					name="endyear">
					<option value="2005" selected>2005</option>
					<%for (int i = 6; i < 10; i++) {
				out.println("<option value=200" + i + ">200" + i + "</option>");
			}

			%>
				</select>年<select size="1" name="endmonth">
					<option value="1" selected>1</option>
					<%for (int i = 2; i < 13; i++) {
				out.println("<option value=" + i + ">" + i + "</option>");
			}

			%>
				</select>月<select size="1" name="endday">
					<option value="1" selected>1</option>
					<%for (int i = 2; i < 32; i++) {
				out.println("<option value=" + i + ">" + i + "</option>");
			}

			%>
				</select>日&nbsp;&nbsp;</td>
			</tr>
			<%//根据n值列出选项
			for (int i = 1; i <= Integer.parseInt(n); i++) {
%>
			<tr>
				<td height="25" >选项<%=i%>：<input type="text"
					name="option<%=i%>" value=""></td>
			</tr>
			<%}

		%>
			<tr>
				<td height="25" >设为当前调查项目？ <input type="radio"
					name="active" value="1" checked>是 <input name="active" type="radio"
					value="0">否</td>
			</tr>
			<tr>
				<td height="25" align="center" ><input type="submit" value="提交" name="B1">&nbsp;&nbsp;&nbsp;&nbsp; 
					<input type="reset" value="取消" name="B2"></td>
			</tr>
		</table>
		</form>
		</td>
	</tr>
</table>
	<a href=votelist.jsp>>>返回</a>
</body>
</html>
