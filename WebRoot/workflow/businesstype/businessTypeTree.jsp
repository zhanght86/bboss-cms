<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

			<tree:tree tree="TreeWithContextMenu" node="TreeWithContextMenu.node"
				imageFolder="../../sysmanager/images/tree_images/" collapse="true"
				includeRootNode="false" 
				mode="static-dynamic" jquery="true">
				<!--
		    			   		树的展开和折叠时  保持页面的参数
		    			   		uprecursive="true" 
		    			   		partuprecursive="false"
		    			   		recursive="true"
		    			   -->
			
				<!-- 指定树的数据加载器和根节点信息
		                   		treetype-数据加载器的实现类，这里是test.tree.TestTree
		                   		scope 数据加载器对象的存储范围，一般是request级别
		                   		
		                   		指定根节点的信息：
		                   		rootid 根节点的id
		                   		rootName 根节点的名称
		                   		
		                   		expandLevel 默认展开多少级
		                   		enablecontextmenu 是否启用右键菜单，true启用，false不启用
		                    -->


				<tree:treedata
					treetype="com.sany.workflow.util.BusinessTypeTree"
					scope="request" rootid="0" rootName="业务类别" expandLevel="1"
					showRootHref="false"
					enablecontextmenu="false" />

			</tree:tree>
	