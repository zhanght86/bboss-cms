<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityConstants" %>
<%@ page import="org.frameworkset.spi.BaseSPIManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.dictionary.Data" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.*" %>
<%
    AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
    DictManager dictManager = new DictManagerImpl();
    //只获取 授权可见的字典类型列表
    List dicttypes = dictManager.getPartReadChildDicttypeList("0");
%>
<html>
    <title>属性容器</title>
    <head>
		<link href="../../inc/css/cms.css" rel="stylesheet" type="text/css">		
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
		<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="../../user/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
    </head>
    <body>
	    编码种类:
	    <table cellspacing="0" cellpadding="0" border="0" width=98%  class="thin">
			<tr>
				<!--设置分页表头-->								
				<td class="headercolor" >编码代号</td>								
				<td class="headercolor" colspan="2">编码种类名称</td>
			</tr>
			<%
			    for(int i=0;i<dicttypes.size();i++){
			        Data dicttype = (Data)dicttypes.get(i);
			        String dicttypeId = dicttype.getDataId();
			        String dicttypeName = dicttype.getDescription();
			        int is_tree = dicttype.getIsTree();
			        String hrefPath = "org_dictdataList.jsp";
			        if(DictManager.DICTDATA_IS_TREE==is_tree){
			            hrefPath = "org_dictdataTree.jsp";
			        }
			%>
			<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" onclick="getDictdatas('<%=dicttypeId%>','<%=dicttypeName%>','<%=hrefPath%>')">
				<td class="tablecells" nowrap="nowrap">
					<%=dicttypeId%>
				</td>									
				<td class="tablecells" nowrap="nowrap" colspan="2">
					<%=dicttype.getDescription()%>
				</td>
			</tr>
			<%
			    }
			%>
		</table>
		<input type="hidden" name="selected_typeId">
		<input type="hidden" name="selected_typeName">
	</body>
	<script>
	    function getDictdatas(dicttypeId,dicttypeName,hrefPath){
	       document.all.selected_typeId.value = dicttypeId;
	       document.all.selected_typeName.value = dicttypeName;
	       var path = hrefPath + "?did="+dicttypeId+"&typeName="+dicttypeName;
	       if(parent.rightFrame.document.frames[1]){
	           var orgId = parent.rightFrame.document.frames[1].document.all.orgId.value;
	           path = path + "&orgId="+orgId;	           
	       }
	       window.parent.dictDatas.document.location.href= path;
	    }
	</script>
</html>


