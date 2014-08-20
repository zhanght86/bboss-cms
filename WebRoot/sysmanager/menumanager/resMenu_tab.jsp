<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<%@ include file="/common/jsp/importtaglib.jsp"%>

<html>
	<head>
		<tab:tabConfig />
		<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
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
									<pg:map actual="${menu.localeNames}">
										<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
									</pg:map>
								</td>
							</tr>
							<tr>
								<td class="detailtitle" height="28">
									i18n:
								</td>
								<td class="detailcontent">
									<pg:map actual="${menu.localeNames}">
										<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
									</pg:map>
								</td>
							</tr>
							<pg:in actual="${menuType}" scope="item,menuType,module">
								<tr>
									<td class="detailtitle" height="28">
										title:
									</td>
									<td class="detailcontent">
										<pg:map actual="${menu.localeTitles}">
											<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
										</pg:map>
									</td>
								</tr>
								<pg:equal actual="${menuType}" value="module">
									<tr>
										<td class="detailtitle" height="28">
											description:
										</td>
										<td class="detailcontent">
											<pg:map actual="${menu.localeDescriptions}">
												<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
											</pg:map>
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
							</pg:in>
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
						<pg:equal actual="${menuType}" value="framework">
							<tr>
								<td width="124" height="28" class="detailtitle">
									description:
								</td>
								<td class="detailcontent">
									<pg:map actual="${menu.localeDescriptions}">
										<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
									</pg:map>
								</td>
							</tr>
							<tr>
								<td width="124" height="28" class="detailtitle">
									i18n:
								</td>
								<td class="detailcontent">
									<pg:map actual="${menu.localeDescriptions}">
										<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
									</pg:map>
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
						</pg:equal>
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
										<pg:map actual="${publicItem.localeNames}">
											<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
										</pg:map>
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
										<pg:map actual="${publicItem.localeMouseupimgs}">
											<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
										</pg:map>
									</td>
								</tr>
	
								<tr>
									<td class="detailtitle" height="28">
										mouseoverimg:
									</td>
									<td class="detailcontent">
										<pg:map actual="${publicItem.localeMouseoverimgs}">
											<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
										</pg:map>
									</td>
								</tr>
								<tr>
									<td class="detailtitle" height="28">
										mouseclickimg:
									</td>
									<td class="detailcontent">
										<pg:map actual="${publicItem.localeMouseclickimgs}">
											<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
										</pg:map>
									</td>
								</tr>
								<tr>
									<td class="detailtitle" height="28">
										mouseoutimg:
									</td>
									<td class="detailcontent">
										<pg:map actual="${publicItem.localeMouseoutimgs}">
											<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
										</pg:map>
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										headimg:
									</td>
									<td class="detailcontent">
										<pg:map actual="${publicItem.localeHeadimgs}">
											<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
										</pg:map>
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
								<td class="detailcontent">
									<pg:list actual="${publicItem.authorResources}">
										<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:cell/>
									</pg:list>
								</td>
								</tr>
							</table>
							
						</fieldset>
						
						<fieldset>
						<legend><pg:message code="sany.pdp.menumanager.menu.subsystem"/></legend>
						<pg:map actual="${subSystems}">
							<fieldset>
								<legend><pg:cell colName="name"/></legend>
								<table>
								<tr>
									<td width="124" height="28" class="detailtitle">
										id:
									</td>
									<td class="detailcontent">
										<pg:cell colName="id"/>
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										name:
									</td>
									<td class="detailcontent">
										<pg:map colName="localeNames">
											<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
										</pg:map>
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										i18n:
									</td>
									<td class="detailcontent">
										<pg:map colName="localeNames">
											<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
										</pg:map>
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										template:
									</td>
									<td class="detailcontent">
										<pg:cell colName="template"/>
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										module:
									</td>
									<td class="detailcontent">
										<pg:cell colName="module"/>
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										logoutredirect:
									</td>
									<td class="detailcontent">
										<pg:cell colName="logoutredirect"/>
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										baseuri:
									</td>
									<td class="detailcontent">
										<pg:cell colName="baseuri"/>
									</td>
								</tr>
								</table>
							</fieldset>
						</pg:map>
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
					<pg:true actual="${menuType eq 'item' or menuType eq 'module'}">
						<fieldset>
							<legend><pg:message code="sany.pdp.menumanager.menu.image"/></legend>
							
							<table>
							<tr>
								<td width="124" height="28" class="detailtitle" >
								额外的权限检测的url:</td>
								<td class="detailcontent">
									<pg:list actual="${menu.authorResources}">
										<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:cell/>
									</pg:list>
								</td>
							</tr>
								<tr>
									<td class="detailtitle" height="28" wiidth="40%">
										mouseupimg:
									</td>
									<td class="detailcontent">
										<pg:map actual="${menu.localeMouseupimgs}">
											<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
										</pg:map>
									</td>
								</tr>
	
								<tr>
									<td class="detailtitle" height="28">
										mouseoverimg:
									</td>
									<td class="detailcontent">
										<pg:map actual="${menu.localeMouseoverimgs}">
											<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
										</pg:map>
									</td>
								</tr>
								<tr>
									<td class="detailtitle" height="28">
										mouseclickimg:
									</td>
									<td class="detailcontent">
										<pg:map actual="${menu.localeMouseclickimgs}">
											<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
										</pg:map>
									</td>
								</tr>
								<tr>
									<td class="detailtitle" height="28">
										mouseoutimg:
									</td>
									<td class="detailcontent">
										<pg:map actual="${menu.localeMouseoutimgs}">
											<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
										</pg:map>
									</td>
								</tr>
								<tr>
									<td width="124" height="28" class="detailtitle">
										headimg:
									</td>
									<td class="detailcontent">
										<pg:map actual="${menu.localeHeadimgs}">
											<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
										</pg:map>
									</td>
								</tr>
								<pg:equal actual="${menuType}" value="item">
									<tr>
										<td width="124" height="28" class="detailtitle">
											logoimage:
										</td>
										<td class="detailcontent">
											<pg:map actual="${menu.localLogoimages}">
												<pg:notequal expression="{rowid}" value="0">,</pg:notequal><pg:mapkey/>:<pg:cell/>&nbsp;
											</pg:map>
										</td>
									</tr>
								</pg:equal>
							</table>
						</fieldSet>
					</pg:true>
				</FORM>
			</tab:tabPane>


			
		</tab:tabContainer>
	</body>
</html>

