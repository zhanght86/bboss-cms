<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
<tree:tree tree="UserOrg_tree" node="UserOrg_tre.node"
					imageFolder="/sysmanager/images/tree_images/" collapse="true"
					includeRootNode="false" mode="static-dynamic" jquery="true">
					<tree:treedata treetype="com.frameworkset.platform.sanylog.common.LogOrgTree"
						scope="request" rootid="0" rootName="机构树" expandLevel="1"
						showRootHref="false" needObserver="false"
						enablecontextmenu="false" />

				</tree:tree>