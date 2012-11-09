<%@ page session="true" language="java"
	contentType="text/html; charset=utf-8"%>
<html>
<body>

<table>
<tr><td><%String dirq = "/opt/tool/apache-tomcat-6.0.32/webapps/SanyIPP/cms/siteResource/sanyIPP/_template/reportDetail.html";
		String dirb = "/opt/tool/apache-tomcat-6.0.32/webapps/SanyIPP/cms/siteResource/sanyIPP/_template/reportsList.html";
		
		java.io.File f = new java.io.File(dirq);
		java.io.File f1 = new java.io.File(dirb);
		out.println(dirq + "<br/>" +f.lastModified() );
		out.println(dirb + "<br/>" +f1.lastModified() );
		out.println(f.lastModified() == f1.lastModified());%></td></tr>
</table>

</body>
</html>