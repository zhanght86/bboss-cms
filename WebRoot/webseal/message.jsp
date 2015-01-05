<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%String userName = request.getParameter("userName");
 String ip = request.getParameter("ip");
 %>
<%--
	描述：台账主页面
	作者：许石玉
	版本：1.0
	日期：2012-02-08
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript">
function logout()
{
	top.location='../logout.jsp';
	flag = false;
}
</script>
</head>
	<body>
		<div class="mcontent">
			
			<div id="custombackContainer" >
				用户[<%=userName %>]登录失败-IP[<%=ip %>]  <a href="javascript:void" class="zhuxiao" onclick="logout()">注销</a>
			</div>
		</div>
	</body>
</html>
