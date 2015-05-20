<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>

<tree:tree tree="BusinessDemoTree" node="BusinessDemoTree.node"
	imageFolder="../../sysmanager/images/tree_images/" collapse="true"
	includeRootNode="false" mode="static-dynamic" jquery="false">
	<tree:checkbox name="processkey"/>
	<tree:treedata treetype="com.sany.workflow.demo.util.BusinessDemoTree"
	scope="request" rootid="0" rootName="业务类型流程树" expandLevel="1"
	showRootHref="false" needObserver="false" enablecontextmenu="false" />
	
</tree:tree>