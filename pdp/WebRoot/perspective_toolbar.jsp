<%@page contentType="text/html;charset=UTF-8"%>
    

<%@page import="com.frameworkset.platform.security.AccessControl"%>

<%     
   AccessControl accesscontroler = AccessControl.getInstance();
   accesscontroler.checkAccess(request,response); 
   String userId = accesscontroler.getUserID();
%> 
<HTML><HEAD><TITLE>三一集团信息管理系统</TITLE>
<link rel="stylesheet" type="text/css" href="sysmanager/css/toolbar.css">
<script language="javascript" src="sysmanager/scripts/toolbar.js"></script>
<script language="javascript" src="sysmanager/common/CommonFunc.js"></script>
 
<META content="MSHTML 6.00.2800.1522" name=GENERATOR></HEAD> 
</head>
<body class="gtoolbarbodymargin">
<div id="globaltoolbar">     
<table width="95%" height="100%" cellpadding=3 cellspacing=0 border=0>   
	<tr>
		<td class="text" nowrap valign="middle" align="center" width="75"
		height=25><a href="javascript:showAbout()" title="三一集团信息管理系统"></a></td>
			<!--height=25><a href="javascript:showAbout()"><img class="normal" src=" " width="75"
			height=25 border="0" title="三一集团信息管理系统"></a></td>-->
			
			
		<td class="text" nowrap valign="middle" align="left" width="200"
			height=25><a href="javascript:showAbout()" title="sp 1.0"></a></td>		
		
		<td class="text" nowrap valign="middle" align="center" width="100%"
			height=25></td>
		<td class="text" nowrap valign="middle" align="right" width="100%"
			height=25><jsp:include page="accountbox.jsp" flush="true"></jsp:include>
		</td>
		<td class="text" nowrap valign="middle" align="center" width="5"
			height=25></td>
	</tr>
	<tr>
		<td colspan="4" bgcolor="#93bee2" width="100%" height=1px></td>
	</tr>
</table>
</div>
<!--<iframe src="sysmanager/popup.jsp?userId=<%=userId%>" id="popup_iframe" name="popup_iframe" width="0" height="0" frameborder="0" border="0"></iframe>-->
<!--<iframe src="" id="popup_iframe1" name="popup_iframe1"  width="0" height="0" frameborder="0" border="0"></iframe>-->
</body>
</html>

