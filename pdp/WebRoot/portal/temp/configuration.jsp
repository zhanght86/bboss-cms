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
	
	String htmlAttributes =
		"border=" + border + "\n" +
		"bordercolor=" + bordercolor + "\n" +
		"frameborder=" + frameborder + "\n" +
		"height-maximized=" + heightMaximized + "\n" +
		"height-normal=" + heightNormal + "\n" +
		"hspace=" + hspace + "\n" +
		"scrolling=" + scrolling + "\n" +
		"vspace=" + vspace + "\n" +
		"width=" + width + "\n";
%>
<form action="<liferay-portlet:actionURL portletConfiguration="true" />" method="post" name="<portlet:namespace />fm">
<input name="<portlet:namespace /><%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />

<table class="lfr-table">
<tr>
	<td>
		<liferay-ui:message key="source-url" />
	</td>
	<td>
	<%
	String readonly = "";
	if(preferences.isReadOnly("src"))
	{
		readonly = "readonly";
	}
	%>
		 
		<input class="lfr-input-text" name="<portlet:namespace />src" type="text" <%=readonly %> value="<%= src %>" />
	</td>
</tr>
<!-- add by jianfeng 20100118 -->
<tr>
	<td>
		<liferay-ui:message key="MAX-source-url" />
	</td>
	<td>
	<%
	readonly = "";
	if(preferences.isReadOnly("MAXsrc"))
	{
		readonly = "readonly";
	}
	%>
		<span id="<portlet:namespace />MAX-context-path-text" style='<%= relative ? "" : "display: none;" %>'>...<%= themeDisplay.getPathContext() %></span> 
		<input class="lfr-input-text" name="<portlet:namespace />MAXsrc" type="text" <%=readonly %> value="<%= MAXsrc %>" />
	</td>
</tr>
<!-- add end -->

<tr>
	<td>
		<liferay-ui:message key="httpcontext" />
	</td>
	<td>
	<%
	readonly = "";
	if(preferences.isReadOnly("httpcontext"))
	{
		readonly = "readonly";
	}
	%>
		<input class="lfr-input-text" name="<portlet:namespace />httpcontext" type="text" <%=readonly %>  value="<%= httpcontext %>"  title="<%=PortalVariableHandler.replaceVariable(httpcontext) %>"/>
	</td>
    
</tr>
<tr>
	<td>
		<liferay-ui:message key="OUTER_FAILED_URL" />
	</td>
	<td>
	<%
	readonly = "";
	if(preferences.isReadOnly("OUTER_FAILED_URL"))
	{
		readonly = "readonly";
	}
	%>
		<input class="lfr-input-text" name="<portlet:namespace />OUTER_FAILED_URL" type="text" <%=readonly %> value="<%= OUTER_FAILED_URL %>" />
	</td>
</tr>

<tr>
	<td>
		<liferay-ui:message key="APP_ID" />
	</td>
	<td>
	<%
	readonly = "";
	if(preferences.isReadOnly("APP_ID"))
	{
		readonly = "readonly";
	}
	%>
		<input class="lfr-input-text" name="<portlet:namespace />APP_ID" type="text" <%=readonly %>  value="<%= APP_ID %>" />
	</td>
</tr>
<tr>
	<td>
		<liferay-ui:message key="SSO_PAGE" />
	</td>
	<td>
	<%
	readonly = "";
	if(preferences.isReadOnly("SSO_PAGE"))
	{
		readonly = "readonly";
	}
	%>
		<input class="lfr-input-text" name="<portlet:namespace />SSO_PAGE" type="text" <%=readonly %>  value="<%= SSO_PAGE %>" />
	</td>
</tr>
<tr>
	<td>
		<liferay-ui:message key="OUTER_FAILED_MESSAGE" />
	</td>
	<td>
	<%
	readonly = "";
	if(preferences.isReadOnly("OUTER_FAILED_MESSAGE"))
	{
		readonly = "readonly";
	}
	%>
		<input class="lfr-input-text" name="<portlet:namespace />OUTER_FAILED_MESSAGE" <%=readonly %>  type="text" value="<%= OUTER_FAILED_MESSAGE %>" />
	</td>
    
</tr>
<tr>
	<td>
		<liferay-ui:message key="form-method" />
	</td>
	<td>
	<%
	readonly = "";
	if(preferences.isReadOnly("formMethod"))
	{
		readonly = "readonly";
	}
	%>
		<select <%=readonly %> name="<portlet:namespace />formMethod">
			<option <%= (formMethod.equals("get")) ? "selected" : "" %> value="get">Get</option>
			<option <%= (formMethod.equals("post")) ? "selected" : "" %> value="post">Post</option>
		</select>
	</td>
</tr>
<tr>
	<td>
		<liferay-ui:message key="user-name" />
	</td>
	<td>
	<%
	readonly = "";
	if(preferences.isReadOnly("userName"))
	{
		readonly = "readonly";
	}
	%>
		<input class="lfr-input-text" <%=readonly %> name="<portlet:namespace />userName" type="text" value="<%= userName %>" />
	</td>
</tr>
<tr>
	<td>
		<liferay-ui:message key="password" />
	</td>
	<td>
	<%
	readonly = "";
	if(preferences.isReadOnly("password"))
	{
		readonly = "readonly";
	}
	%>
		<input class="lfr-input-text" <%=readonly %> name="<portlet:namespace />password" type="text" value="<%= password %>" />
	</td>
</tr>
<tr>
	<td>
		<liferay-ui:message key="hidden-variables" />
	</td>
	<td>
	<%
	readonly = "";
	if(preferences.isReadOnly("hiddenVariables"))
	{
		readonly = "readonly";
	}
	%>
		<input class="lfr-input-text" <%=readonly %> name="<portlet:namespace />hiddenVariables" type="text" value="<%= hiddenVariables %>" />
	</td>
    
</tr>



<tr>
	<td colspan="2">
		<br />
	</td>
</tr>
<tr>
	<td>
		<liferay-ui:message key="html-attributes" />
	</td>
	<td>
	<%
	readonly = "";
	if(preferences.isReadOnly("htmlAttributes"))
	{
		readonly = "readonly";
	}
	%>
		<textarea class="lfr-textarea" <%=readonly %> name="<portlet:namespace />htmlAttributes" wrap="soft" onKeyDown="Liferay.Util.checkTab(this); Liferay.Util.disableEsc();"><%= htmlAttributes %></textarea>
	</td>
</tr>
</table>
<input type="button" value="<liferay-ui:message key="save" />" onClick="submitForm(document.<portlet:namespace />fm);" />

</form>
