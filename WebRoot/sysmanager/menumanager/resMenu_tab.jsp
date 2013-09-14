<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld"%>

<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.ResourceAction"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager,com.frameworkset.platform.sysmgrcore.manager.OperManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.framework.MenuItem"%>
<%@ page import="java.util.List,java.util.ArrayList,com.frameworkset.platform.framework.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
	<head>
		<tab:tabConfig />
		<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script language="javascript">
	</script>
	</head>
	<body>
		<sany:menupath menuid="menumanage"/>
		<tab:tabContainer id="foo-menu-container" selectedTabPaneId="foo-menu" skin="sany">
			<tab:tabPane id="foo-menu"  tabTitleCode="sany.pdp.menumanager.menu.view">
				<FORM name="11" method="post">
					<fieldset>
						<legend><pg:message code="sany.pdp.menumanager.menu.info.basic"/></legend>
						<table>
						<pg:in actual="${menuType}" scope="item,module,subSystem">
							<tr>
								<td width="100" height="28" class="detailtitle" wiidth="50%">
									id:
								</td>
								<td class="detailcontent">
									${menu.id}
								</td>
							</tr>
							<pg:in actual="${menuType}" scope="item,module">
								<tr>
								<td width="100" height="28" class="detailtitle" wiidth="50%">
									used:
								</td>
								<td class="detailcontent">
									${menu.used}
								</td>
							</tr>
							</pg:in>
							<tr>
								<td class="detailtitle" height="28">
									name:
								</td>
								<td class="detailcontent">
									<c:forEach var="name" items="${menu.localeNames}" varStatus="status">
										${name.key}:${name.value}${status.index != fn:length(menu.localeNames)-1 ? "," : ""}&nbsp;
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td class="detailtitle" height="28">
									i18n:
								</td>
								<td class="detailcontent">
									<c:forEach var="name" items="${menu.localeNames}" varStatus="status">
										${name.key}${status.index != fn:length(menu.localeNames)-1 ? "," : ""}&nbsp;
									</c:forEach>
								</td>
							</tr>
							<c:if test="${menuType eq 'item' or menuType eq 'module'}">
								<tr>
									<td class="detailtitle" height="28">
										title:
									</td>
									<td class="detailcontent">
										<c:forEach var="title" items="${menu.localeTitles}" varStatus="status">
											${title.key}:${title.value}${status.index != fn:length(menu.localeTitles)-1 ? "," : ""}&nbsp;
										</c:forEach>
									</td>
								</tr>
								<pg:equal actual="${menuType}" value="module">
									<tr>
										<td class="detailtitle" height="28">
											description:
										</td>
										<td class="detailcontent">
											<c:forEach var="description" items="${menu.localeDescriptions}" varStatus="status">
												${description.key}:${description.value}${status.index != fn:length(menu.localeDescriptions)-1 ? "," : ""}&nbsp;
											</c:forEach>
										</td>
									</tr>
									<tr>
										<td class="detailtitle" height="28">
											url:
										</td>
										<td class="detailcontent">
											${menu.url}
										</td>
									</tr>
								</pg:equal>
								<tr>
									<td class="detailtitle" height="28">
										path:
									</td>
									<td class="detailcontent">
										${menu.path}
									</td>
								</tr>
								<tr>
									<td class="detailtitle" height="28">
										parentpath:
									</td>
									<td class="detailcontent">
										${menu.parentPath}
									</td>
								</tr>
								<pg:equal actual="${menuType}" value="item">
									<tr>
										<td class="detailtitle" height="28">
											left:
										</td>
										<td class="detailcontent">
											${menu.left}
										</td>
									</tr>
									<tr>
										<td width="124" height="28" class="detailtitle">
											top:
										</td>
										<td class="detailcontent">
											${menu.top}
										</td>
									</tr>
							</pg:equal>
							</c:if>
							<pg:equal actual="${menuType}" value="subSystem">
								<tr>
									<td width="124" height="28" class="detailtitle">
										template:
									</td>
									<td class="detailcontent">
										${menu.template}
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										module:
									</td>
									<td class="detailcontent">
										${menu.module}
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										logoutredirect:
									</td>
									<td class="detailcontent">
										${menu.logoutredirect}
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										baseuri:
									</td>
									<td class="detailcontent">
										${menu.baseuri}
									</td>
								</tr>
							</pg:equal>
							<tr>
								<td width="124" height="28" class="detailtitle">
									showleftmenu:
								</td>
								<td class="detailcontent">
									${menu.showleftmenu}
								</td>
							</tr>
						</pg:in>
						<c:if test="${menuType eq 'framework'}">
							<tr>
								<td width="124" height="28" class="detailtitle">
									description:
								</td>
								<td class="detailcontent">
									<c:forEach var="description" items="${menu.localeDescriptions}" varStatus="status">
										${description.key}:${description.value}${status.index != fn:length(menu.localeDescriptions)-1 ? "," : ""}&nbsp;
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td width="124" height="28" class="detailtitle">
									i18n:
								</td>
								<td class="detailcontent">
									<c:forEach var="description" items="${menu.localeDescriptions}" varStatus="status">
										${description.key}${status.index != fn:length(menu.localeDescriptions)-1 ? "," : ""}&nbsp;
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td width="124" height="28" class="detailtitle">
									left_width:
								</td>
								<td class="detailcontent">
									${menu.left_width}
								</td>
							</tr>
							<tr>
								<td width="124" height="28" class="detailtitle">
									top_height:
								</td>
								<td class="detailcontent">
									${menu.top_height}
								</td>
							</tr>
							<tr>
								<td width="124" height="28" class="detailtitle">
									showhidden:
								</td>
								<td class="detailcontent">
									${menu.showhidden}
								</td>
							</tr>
							<tr>
								<td width="124" height="28" class="detailtitle">
									showhidden_width:
								</td>
								<td class="detailcontent">
									${menu.showhidden_width}
								</td>
							</tr>
							<tr>
								<td width="124" height="28" class="detailtitle">
									showrootleftmenu:
								</td>
								<td class="detailcontent">
									${menu.showrootleftmenu}
								</td>
							</tr>
							<tr>
								<td width="124" height="28" class="detailtitle">
									messagesource:
								</td>
								<td class="detailcontent">
									${menu.messagesourcefiles}
								</td>
							</tr>
						</c:if>
						</table>
					</fieldset>
					<pg:in actual="${menuType}" scope="framework,subSystem">
						<fieldset>
							<legend><pg:message code="sany.pdp.menumanager.menu.portal"/></legend>
							<table>
								<tr>
									<td height="28" wiidth="40%" class="detailtitle">
										id:
									</td>
									<td class="detailcontent">
										${publicItem.id}
									</td>
								</tr>
								<tr>
									<td height="28" wiidth="40%" class="detailtitle">
										name:
									</td>
									<td class="detailcontent">
										<c:forEach var="name" items="${publicItem.localeNames}" varStatus="status">
											${name.key}:${name.value}${status.index != fn:length(publicItem.localeNames)-1 ? "," : ""}&nbsp;
										</c:forEach>
									</td>
								</tr>
								<tr>
									<td height="28" wiidth="40%" class="detailtitle">
										main:
									</td>
									<td class="detailcontent">
										${publicItem.main}
									</td>
								</tr>
								<tr>
									<td class="detailtitle" height="28" wiidth="40%">
										mouseupimg:
									</td>
									<td class="detailcontent">
										<c:forEach var="mouseupimg" items="${publicItem.localeMouseupimgs}" varStatus="status">
											${mouseupimg.key}:${mouseupimg.value}${status.index != fn:length(publicItem.localeMouseupimgs)-1 ? "," : ""}&nbsp;
										</c:forEach>
									</td>
								</tr>
	
								<tr>
									<td class="detailtitle" height="28">
										mouseoverimg:
									</td>
									<td class="detailcontent">
										<c:forEach var="mouseoverimg" items="${publicItem.localeMouseoverimgs}" varStatus="status">
											${mouseoverimg.key}:${mouseoverimg.value}${status.index != fn:length(publicItem.localeMouseoverimgs)-1 ? "," : ""}&nbsp;
										</c:forEach>
									</td>
								</tr>
								<tr>
									<td class="detailtitle" height="28">
										mouseclickimg:
									</td>
									<td class="detailcontent">
										<c:forEach var="mouseclickimg" items="${publicItem.localeMouseclickimgs}" varStatus="status">
											${mouseclickimg.key}:${mouseclickimg.value}${status.index != fn:length(publicItem.localeMouseclickimgs)-1 ? "," : ""}&nbsp;
										</c:forEach>
									</td>
								</tr>
								<tr>
									<td class="detailtitle" height="28">
										mouseoutimg:
									</td>
									<td class="detailcontent">
										<c:forEach var="mouseoutimg" items="${publicItem.localeMouseoutimgs}" varStatus="status">
											${mouseoutimg.key}:${mouseoutimg.value}${status.index != fn:length(publicItem.localeMouseoutimgs)-1 ? "," : ""}&nbsp;
										</c:forEach>
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										headimg:
									</td>
									<td class="detailcontent">
										<c:forEach var="headimg" items="${publicItem.localeHeadimgs}" varStatus="status">
											${headimg.key}:${headimg.value}${status.index != fn:length(publicItem.localeHeadimgs)-1 ? "," : ""}&nbsp;
										</c:forEach>
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										top:
									</td>
									<td class="detailcontent">
										${publicItem.top}
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										workspacecontent:
									</td>
									<td class="detailcontent">
										${publicItem.workspaceContent}
									</td>
								</tr>
								<pg:map actual="${publicItem.workspacecontentExtendAttribute}">
									<tr>
										<td width="124" height="28" class="detailtitle">
											<pg:mapkey />
										</td>
										<td class="detailcontent">
											
											<pg:cell/>
										</td>
									</tr>
								</pg:map>
								<tr>
								<td width="124" height="28" class="detailtitle" >
								额外的开放权限的url:</td>
								<td class="detailcontent"><pg:list actual="${publicItem.authorResources}">
									<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:cell/>
								</pg:list>
								</td>
								</tr>
							</table>
							
						</fieldset>
						
						<fieldset>
						<legend><pg:message code="sany.pdp.menumanager.menu.subsystem"/></legend>
						<c:forEach var="subSys"  items="${subSystems}">
							<fieldset>
								<legend>${subSys.value.name}</legend>
								<table>
								<tr>
									<td width="124" height="28" class="detailtitle">
										id:
									</td>
									<td class="detailcontent">
										${subSys.value.id}
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										name:
									</td>
									<td class="detailcontent">
										<c:forEach var="name" items="${subSys.value.localeNames}" varStatus="status">
											${name.key}:${name.value}${status.index != fn:length(subSys.value.localeNames)-1 ? "," : ""}&nbsp;
										</c:forEach>
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										i18n:
									</td>
									<td class="detailcontent">
										<c:forEach var="name" items="${subSys.value.localeNames}" varStatus="status">
											${name.key}${status.index != fn:length(subSys.value.localeNames)-1 ? "," : ""}&nbsp;
										</c:forEach>
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										template:
									</td>
									<td class="detailcontent">
										${subSys.value.template}
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										module:
									</td>
									<td class="detailcontent">
										${subSys.value.module}
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										logoutredirect:
									</td>
									<td class="detailcontent">
										${subSys.value.logoutredirect}
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										baseuri:
									</td>
									<td class="detailcontent">
										${subSys.value.baseuri}
									</td>
								</tr>
								</table>
							</fieldset>
						</c:forEach>
						</fieldset>
					</pg:in>
					<pg:equal actual="${menuType}" value="item">
						<fieldset>
						<legend><pg:message code="sany.pdp.menumanager.menu.file"/></legend>
						<table>
							<tr>
								<td class="detailtitle" height="28" wiidth="40%">
									workspacecontent:
								</td>
								<td class="detailcontent">
									${menu.workspaceContent}
								</td>
							</tr>
							<tr>
								<td class="detailtitle" height="28">
									workspacetoolbar:
								</td>
								<td class="detailcontent">
									${menu.workspaceToolbar}
								</td>
							</tr>
							<tr>
								<td class="detailtitle" height="28">
									navigatortoolbar:
								</td>
								<td class="detailcontent">
									${menu.navigatorToolbar}
								</td>
							</tr>
							<tr>
								<td class="detailtitle" height="28">
									navigatorcontent:
								</td>
								<td class="detailcontent">
									${menu.navigatorContent}
								</td>
							</tr>
							<tr>
								<td class="detailtitle" height="28">
									statuscontent:
								</td>
								<td class="detailcontent">
									${menu.statusContent}
								</td>
							</tr>
							<tr>
								<td class="detailtitle" height="28">
									statustoolbar:
								</td>
								<td class="detailcontent">
									${menu.statusToolbar}
								</td>
							</tr>
						</table>
					</fieldset>
					</pg:equal>
					<c:if test="${menuType eq 'item' or menuType eq 'module'}">
						<fieldset>
							<legend><pg:message code="sany.pdp.menumanager.menu.image"/></legend>
							
							<table>
							<tr>
								<td width="124" height="28" class="detailtitle" >
								额外的权限检测的url:</td>
								<td class="detailcontent"><pg:list actual="${menu.authorResources}">
									<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:cell/>
								</pg:list>
								</td>
							</tr>
								<tr>
									<td class="detailtitle" height="28" wiidth="40%">
										mouseupimg:
									</td>
									<td class="detailcontent">
										<c:forEach var="mouseupimg" items="${menu.localeMouseupimgs}" varStatus="status">
											${mouseupimg.key}:${mouseupimg.value}${status.index != fn:length(menu.localeMouseupimgs)-1 ? "," : ""}&nbsp;
										</c:forEach>
									</td>
								</tr>
	
								<tr>
									<td class="detailtitle" height="28">
										mouseoverimg:
									</td>
									<td class="detailcontent">
										<c:forEach var="mouseoverimg" items="${menu.localeMouseoverimgs}" varStatus="status">
											${mouseoverimg.key}:${mouseoverimg.value}${status.index != fn:length(menu.localeMouseoverimgs)-1 ? "," : ""}&nbsp;
										</c:forEach>
									</td>
								</tr>
								<tr>
									<td class="detailtitle" height="28">
										mouseclickimg:
									</td>
									<td class="detailcontent">
										<c:forEach var="mouseclickimg" items="${menu.localeMouseclickimgs}" varStatus="status">
											${mouseclickimg.key}:${mouseclickimg.value}${status.index != fn:length(menu.localeMouseclickimgs)-1 ? "," : ""}&nbsp;
										</c:forEach>
									</td>
								</tr>
								<tr>
									<td class="detailtitle" height="28">
										mouseoutimg:
									</td>
									<td class="detailcontent">
										<c:forEach var="mouseoutimg" items="${menu.localeMouseoutimgs}" varStatus="status">
											${mouseoutimg.key}:${mouseoutimg.value}${status.index != fn:length(menu.localeMouseoutimgs)-1 ? "," : ""}&nbsp;
										</c:forEach>
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										headimg:
									</td>
									<td class="detailcontent">
										<c:forEach var="headimg" items="${menu.localeHeadimgs}" varStatus="status">
											${headimg.key}:${headimg.value}${status.index != fn:length(menu.localeHeadimgs)-1 ? "," : ""}&nbsp;
										</c:forEach>
									</td>
								</tr>
								<pg:equal actual="${menuType}" value="item">
									<tr>
										<td width="124" height="28" class="detailtitle">
											logoimage:
										</td>
										<td class="detailcontent">
											<c:forEach var="logoimage" items="${menu.localLogoimages}" varStatus="status">
												${logoimage.key}:${logoimage.value}${status.index != fn:length(menu.localLogoimages)-1 ? "," : ""}&nbsp;
											</c:forEach>
										</td>
									</tr>
								</pg:equal>
							</table>
						</fieldSet>
					</c:if>
				</FORM>
			</tab:tabPane>


			
		</tab:tabContainer>
	</body>
</html>

