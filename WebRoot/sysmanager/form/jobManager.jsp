 
<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 
<html> 
	<head>
		<title></title>
	</head>
	<body>
		<html:form action="/job/jobManager?method=getJobList"focus="orgId">
			jobname : <html:text property="orgId"/><html:errors property="jobname"/><br/>
			<html:submit/><html:reset/>
		</html:form>
	</body>
</html>

