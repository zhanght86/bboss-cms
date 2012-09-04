<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityConstants" %>
<%@ page import="org.frameworkset.spi.BaseSPIManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.dictionary.Data" %>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator,
				com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>
<%@ page import="java.util.*" %>
<%
    //常用 机构编码关系-------------------------------------------
    AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
    DictManager dictManager = new DictManagerImpl();
    List dicttypes = dictManager.getUsualRelationDicttypeList("0");
    OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
    List list = orgAdministrator.getManagerOrgsOfUserByID(accesscontroler.getUserID());
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

	    <table cellspacing="0" cellpadding="0" border="0" width=98%  class="thin">
			<tr>
				<!--设置分页表头-->					
				<td align="center"><font size=2><strong>字典类型:</strong></font>&nbsp;&nbsp;
					<select name="dictType" onChange="getDictdatas(this)">
						<option selected>--请选择字典类型--</option>
						<%
						if(list.size() > 0 || accesscontroler.isAdmin()){
						    for(int i=0;i<dicttypes.size();i++){
						        Data dicttype = (Data)dicttypes.get(i);
						        String dicttypeId = dicttype.getDataId();
						        String dicttypeName = dicttype.getDescription();
						        int is_tree = dicttype.getIsTree();
						        String hrefPath = "org_dictdataListFrame.jsp";
						        if(DictManager.DICTDATA_IS_TREE==is_tree){
						            hrefPath = "org_dictdataTreeFrame.jsp";
						        }
						%>
						<option value="<%=hrefPath%>"><%=dicttypeId%> <%=dicttypeName%></option>
						<%	}}%>
					</select>



				</td>
			</tr>



		</table>
		<input type="hidden" name="selected_typeId">
		<input type="hidden" name="selected_typeName">
	</body>
	<script>
	    var selectedTr = null;
	    function getDictdatas(obj){
	       if(obj.selectedIndex==0){
	       	  return;
	       }
	       var idName = obj.options[obj.selectedIndex].text;
	       var idNames = idName.split(" ");
	       var dicttypeId = idNames[0];
	       var dicttypeName = idNames[1];
	       var hrefPath = obj.value;
	       document.all.selected_typeId.value = dicttypeId;
	       document.all.selected_typeName.value = dicttypeName;
	       var path = hrefPath + "?did="+dicttypeId+"&typeName="+dicttypeName;
	       if(parent.orgTaxcode.document.frames[1]){
	           var orgId = parent.orgTaxcode.document.frames[1].document.all.orgId.value;
	           path = path + "&orgId="+orgId;	           
	       }
	       window.parent.dictDatas.document.location.href= path;
	       
	       //选中
	       //obj.style.background='#191970';   //把点到的那一行变希望的颜色; 
           //obj.style.color = 'white'  
		   //if(selectedTr!=null){
		   //    selectedTr.style.removeAttribute("backgroundColor");  
		   //    selectedTr.style.color = 'black'  ;
		   //}
		   //if(selectedTr==obj)   selectedTr=null;//加上此句，以控制点击变白，再点击反灰   
		   //else   selectedTr=obj; 
	    }
	</script>
</html>
