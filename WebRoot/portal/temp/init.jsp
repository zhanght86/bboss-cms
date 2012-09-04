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

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>

<%@ page import="com.liferay.portal.kernel.util.Constants" %>
<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %>
<%@ page import="com.liferay.portal.kernel.util.StringPool" %>
<%@ page import="com.liferay.portal.kernel.util.Validator" %>
<%@ page import="com.liferay.portlet.PortletPreferencesFactoryUtil" %>
<%@ page import="com.liferay.portal.util.PortalUtil" %>
<%@ page import="com.liferay.portal.kernel.util.StringUtil" %>

<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page import="javax.portlet.WindowState" %>

<!-- add by jianfeng 20100118 -->
<liferay-theme:defineObjects />
<!-- add end -->

<portlet:defineObjects />

<%
//System.out.println("creator init.jsp============iframe plugins");
PortletPreferences preferences = renderRequest.getPreferences();

WindowState windowState = null;
if (renderRequest != null) {
	windowState = renderRequest.getWindowState();
}
else if (resourceRequest != null) {
	windowState = resourceRequest.getWindowState();
}

String portletResource = ParamUtil.getString(request, "portletResource");

if (Validator.isNotNull(portletResource)) {
	preferences = PortletPreferencesFactoryUtil.getPortletSetup(request, portletResource);
}



String MAXsrc = preferences.getValue("MAXsrc", StringPool.BLANK);
boolean relative = GetterUtil.getBoolean(preferences.getValue("relative", StringPool.BLANK));
/**
boolean auth = GetterUtil.getBoolean(preferences.getValue("auth", StringPool.BLANK));
String authType = preferences.getValue("auth-type", StringPool.BLANK);
*/

//源URL
String src = preferences.getValue("src", StringPool.BLANK);
//httpcontext
String httpcontext = preferences.getValue("httpcontext", StringPool.BLANK);
//OUTER_FAILED_URL
String OUTER_FAILED_URL = preferences.getValue("OUTER_FAILED_URL", StringPool.BLANK);
//APP_ID
String APP_ID = preferences.getValue("APP_ID", StringPool.BLANK);
//SSO_PAGE
String SSO_PAGE = preferences.getValue("SSO_PAGE", StringPool.BLANK);
//OUTER_FAILED_MESSAGE
String OUTER_FAILED_MESSAGE = preferences.getValue("OUTER_FAILED_MESSAGE", StringPool.BLANK);
//表单提交方法  post，get
String formMethod = preferences.getValue("form-method", StringPool.BLANK);
//用户名称
String userName = preferences.getValue("user-name", StringPool.BLANK);
//密码
String password = preferences.getValue("password", StringPool.BLANK);
//隐藏的变量
String hiddenVariables = preferences.getValue("hidden-variables", StringPool.BLANK);
//html属性
String border = preferences.getValue("border", "0");
String bordercolor = preferences.getValue("bordercolor", "#000000");
String frameborder = preferences.getValue("frameborder", "0");
String heightMaximized = preferences.getValue("height-maximized", "600");
String heightNormal = preferences.getValue("height-normal", "300");
String hspace = preferences.getValue("hspace", "0");
String scrolling = preferences.getValue("scrolling", "auto");
String vspace = preferences.getValue("vspace", "0");
String width = preferences.getValue("width", "100%");



%>
