<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,java.util.*" %>

<% 
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	UserManager userManager = SecurityDatabase.getUserManager();
	String cursessionId = (String)session.getAttribute(AccessControl.SESSIONID_CACHE_KEY);
	String curserverip = (String)session.getAttribute(AccessControl.SERVER_IP_KEY);
	String curserverport = (String)session.getAttribute(AccessControl.SERVER_PORT_KEY);
	String ips = (String)session.getAttribute(AccessControl.REMOTEADDR_CACHE_KEY);
	String macaddr = (String)session.getAttribute(AccessControl.MACADDR_CACHE_KEY);
	String DNSName = (String)session.getAttribute(AccessControl.MACHINENAME_CACHE_KEY);
	String curUserName = accesscontroler.getUserAccount(); 
	String[] selectUserName = request.getParameterValues("checkBoxOne");
	
	String[] userAccounts = request.getParameterValues("userAccounts");
	StringBuffer nothingPopedom = new StringBuffer();
	String[] selectPopedom = null;
	List deleteUsers = new ArrayList();
	List refuseUsers = new ArrayList();
	if(!accesscontroler.isAdmin() && accesscontroler.isOrgManager(accesscontroler.getUserAccount())){
		String[] selectPopedomStr = userManager.getCuruserAdministrableDeleteUser(accesscontroler.getUserID(),userAccounts);
		
		for(int i = 0; i < selectUserName.length; i++){
		    boolean flag = false;
			for(int j = 0; selectPopedomStr != null && j < selectPopedomStr.length; j++){
				if(selectUserName[i].startsWith(selectPopedomStr[j]))
				{
					flag = true;
					deleteUsers.add(selectUserName[i]);
					break;
				}
			}
			if(!flag){
				refuseUsers.add(selectUserName[i]);
				nothingPopedom.append(selectUserName[i]).append("\\n");
			}
		}
		if(deleteUsers.size() > 0){
			selectPopedom = new String[deleteUsers.size()];
			for(int i = 0; i < deleteUsers.size(); i++){
				selectPopedom[i] = (String)deleteUsers.get(i);
			}
		}
		
		
	}else{
		selectPopedom = selectUserName;
	}
	String delState = request.getParameter("delState");
	boolean state = false;
	if(delState.equals("selectUser")){
		if(selectPopedom == null){
			state = true;
		}else{
			state = AccessControl.removeUsers(selectPopedom,curUserName);
		}
	}else if(delState.equals("allUser")){
		state = AccessControl.removeAllUser();
	}else if(delState.equals("allUserExcludeSelf")){
		state = AccessControl.removeAllUserExcludeSelf(curUserName,cursessionId, ips,macaddr,DNSName,curserverip,curserverport);
	}
	if(state){
%>
	<script language="Javascript">
		<%
			String popedom = nothingPopedom.toString(); 
			if(popedom != null && !popedom.equals("")){
		%>
			alert("对以下用户没有清理权限:\n<%=popedom%>");
		<%
			}else{
		%>
			alert("操作成功！");
		<%
			}
		%>
		parent.resetwindow();
	</script>
<%
	}else{
%>
	<script language="Javascript">
		alert("操作失败！");
	</script>
<%
	}
%>
