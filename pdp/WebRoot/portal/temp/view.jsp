<%
/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
%>

<%@ include file="/init.jsp" %>
<%@ page import="com.liferay.portlet.iframe.action.SSOUserMapping" %>
<%@ page import="com.liferay.portlet.iframe.action.UserMapping" %>
<%@ page import="com.liferay.portal.util.PortalVariableHandler" %>

<%
//replace the variable of httpcontext
httpcontext =  PortalVariableHandler.replaceVariable(httpcontext);
%>

<% 
	//System.out.println("creator view.jsp============iframe plugins");
	SSOUserMapping userMapping = new SSOUserMapping();
	String formaction = null;
	String iframeHeight = heightNormal;

	if (windowState.equals(WindowState.MAXIMIZED)) {
		iframeHeight = heightMaximized;
		//add by jianfeng 20100118
			if(MAXsrc != null && !MAXsrc.trim().equals(""))
				src = MAXsrc;
		// add end	
		
	}
	//应用中单点登录页面的上下文，不配置的情况是 %app_home%/sso/sso.jsp
	if(SSO_PAGE == null || SSO_PAGE.trim().equals(""))
		SSO_PAGE = "sso/sso.jsp";
	
	//modify by jianfeng 20091216	
	String orgId = "";	
	if (themeDisplay.getScopeOrganization() != null){
		orgId = String.valueOf(themeDisplay.getScopeOrganization().getOrganizationId());
	}
	//modify end
	
	//add by jianfeng 20091028	
	//src = PortalUtil.appendCompanyAndOrgParama(scopeGroupId, src);
	//add end
	//如果源URL配置的不是上下文，则iframe直接就是源URL地址
	if(src.startsWith("http://") || src.startsWith("https://"))
	{
		formaction = src;
%>
	<iframe border="<%=border %>" 
		bordercolor="<%=bordercolor %>" 
		frameborder="<%=frameborder %>" 
		height="<%=iframeHeight %>" 
		hspace="<%=hspace %>" 
		name="<portlet:namespace />iframe_liferay" 
		scrolling="<%=scrolling %>" 
		src="<%=formaction %>" 
		vspace="<%=vspace %>" 
		width="<%=width %>"></iframe>
<%
		return;
	}
	else if(httpcontext == null || httpcontext.trim().equals("") )
	{
		formaction = SSO_PAGE;
	}
	else
	{		
		src = PortalUtil.getURL(httpcontext ,src);
		formaction = PortalUtil.getURL(httpcontext ,SSO_PAGE);
		
	}
	String userNameKey = StringPool.BLANK;
	String userNameValue = StringPool.BLANK;
	UserMapping userMap = null;
	
	int pos = userName.indexOf("=");
	if (pos != -1) {
		userNameKey = userName.substring(0, pos);
		userNameValue = userName.substring(pos + 1, userName.length());
	}
	else
	{
		if(userName != null && !userName.equals(""))
			userNameKey = userName;
		else
			userNameKey = "OUT_USER_ACCOUNT_KEY";
		long userid = PortalUtil.getUserId(request);
		userNameValue = PortalUtil.getUserName(userid,"guest","screenname");
		
		userMap = userMapping.getUserMapping("portal",userNameValue,userMapping.getUniqAppID(httpcontext,APP_ID));
		if(userMap != null)
			userNameValue = userMap.getB_USER_NAME();
		
	}
	
	String passwordKey = StringPool.BLANK;
	String passwordValue = StringPool.BLANK;
	
	pos = password.indexOf("=");
	if (pos != -1) {
		passwordKey = password.substring(0, pos);
		passwordValue = password.substring(pos + 1, password.length());
	}
	else
	{
		if(password != null && !password.equals(""))
			passwordKey = password;
		else
			passwordKey = "OUTER_USER_PASSWORD_KEY";
		if(userMap != null)
		{
			passwordValue = userMap.getB_USER_PASSWORD();
		}
		else
		{
			long userid = PortalUtil.getUserId(request);
			passwordValue = PortalUtil.getUserPassword(request);
		}
	}
	
	//System.out.println("userNameValue = " + userNameValue);
	//System.out.println("passwordValue = " + passwordValue);
%>
<form action="<%=formaction %>" method="<%=formMethod %>" name="<portlet:namespace />fm" target="<portlet:namespace />iframe_liferay">
<%
	String[] hiddenVariablesArray = StringTool.split(hiddenVariables, StringPool.SEMICOLON);
for (int i = 0; i < hiddenVariablesArray.length; i++) {
	String hiddenKey = StringPool.BLANK;
	String hiddenValue = StringPool.BLANK;

	int pos2 = hiddenVariablesArray[i].indexOf(StringPool.EQUAL);

	if (pos2 != -1) {
		hiddenKey = hiddenVariablesArray[i].substring(0, pos2);
		hiddenValue = hiddenVariablesArray[i].substring(pos2 + 1, hiddenVariablesArray[i].length());
	}
%>

<input name="<%= hiddenKey %>" type="hidden" value="<%= hiddenValue %>" />
<%} %>

<!-- add by jianfeng 20091217 -->
<input name="portal_companyId" type="hidden" value="<%= company.getCompanyId()%>" />
<input name="portal_orgId" type="hidden" value="<%= orgId %>" />
<!-- add end -->
<input name="<%= userNameKey %>" type="hidden" value="<%= userNameValue %>" />
<input name="OUTER_URL" type="hidden" value="<%= src %>" />
<%if(OUTER_FAILED_URL != null && !OUTER_FAILED_URL.trim().equals(""))
	{
	%> 
	
	<input name="OUTER_FAILED_URL" type="hidden" value="<%= PortalUtil.getURL(httpcontext ,OUTER_FAILED_URL) %>" />
	<%}
	else
	{
	 %>
		<input name="OUTER_FAILED_URL" type="hidden" value="<%= PortalUtil.getURL(httpcontext , "/sso/ssofailed.jsp") %>" />
	<%
	}
	if(OUTER_FAILED_MESSAGE != null && !OUTER_FAILED_MESSAGE.trim().equals(""))
	{
	
	%> 

	<input name="OUTER_FAILED_MESSAGE" type="hidden" value="<%= OUTER_FAILED_MESSAGE %>" />
	
	
	<%} %>
	<input name="<%= passwordKey %>" type="hidden" value="<%= passwordValue %>" />
	<input name="subsystem_id" type="hidden" value="<%= APP_ID %>" />

</form>
		<iframe border="<%=border %>" 
		bordercolor="<%=bordercolor %>" 
		frameborder="<%=frameborder %>" 
		height="<%=iframeHeight %>" 
		hspace="<%=hspace %>" 
		name="<portlet:namespace />iframe_liferay" 
		scrolling="<%=scrolling %>" 
		src="#" 
		vspace="<%=vspace %>" 
		width="<%=width %>"></iframe>
<script language="javascript">
<portlet:namespace />fm.submit();
</script>

