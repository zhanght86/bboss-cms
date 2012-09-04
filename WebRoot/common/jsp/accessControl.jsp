
<%
	AccessControl accesscontroler = AccessControl.getAccessControl(request);
	if(accesscontroler == null)
	{
	
		accesscontroler = AccessControl.getInstance();
		if (!accesscontroler.checkAccess(request, response)){
			return;
		}
	}
%>
