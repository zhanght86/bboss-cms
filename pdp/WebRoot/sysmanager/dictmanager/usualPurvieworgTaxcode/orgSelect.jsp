<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.*,
				 com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
				 com.frameworkset.platform.sysmgrcore.manager.OrgManager,
				 com.frameworkset.platform.sysmgrcore.entity.Organization" %>
<%
    AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	List list = new ArrayList();
	list = orgManager.getAllOrgList("");
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
    <body class="contentbodymargin" scroll="yes">
        <form name="form1">    
		    <table cellspacing="0" cellpadding="0" border="0" class="thin" width=100%>
		        <tr>
		           <td height='40' align="center" colspan="0" >
		                 税务机关:&nbsp;&nbsp;
		                 <select name="orgId" onchange="changeOrg()" style="width:60%">
		                 	<option value="">--请选择税务机关--</option>
		                 	<%
		                 	Organization organization = null;
		                 	for(int i = 0; i < list.size(); i++){
								organization = new Organization();
								organization = (Organization)list.get(i);
								String orgId = organization.getOrgId();
								if (accesscontroler.checkPermission(orgId,
	                            	AccessControl.WRITE_PERMISSION, AccessControl.ORGUNIT_RESOURCE)){
		                 	%>
		                 	<option value="<%=orgId%>"><%=organization.getOrgnumber()%> <%=organization.getRemark5()==null?organization.getOrgName():organization.getRemark5()%></option>
		                 	<%}}%>
		                 </select>
		               <!--  <input type="text" style="width:60%" name="orgName"> -->
		               <!-- input type="button" value="选择" class="input" onclick="selectOrg()" -->		               
		            </td>
		        </tr>
		    </table>
		</form>
	</body>
	<script>
	    function selectOrg(){
	        var orgId = document.all("orgId").value;
	        var path = "org_tree.jsp?orgId="+orgId;
	        var featrue = "dialogWidth=600px;dialogHeight=500px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;help=0;dialogLeft="+(screen.availWidth-600)/2+";dialogTop="+(screen.availHeight-500)/2;
	        win = window.showModalDialog(path,window,featrue);
	        if(win=="ok"){
	            if(window.parent.document.frames[2]){
 	                var old_path = window.parent.document.frames[2].location.href;
 	                var page_name = old_path.substring(0,old_path.indexOf("?"));
 	                var new_orgId = document.all("orgId").value;
 	                var dicttypeId = "";
 	                var dicttypeName = "";
 	                if(parent.document.frames[0]){
 	                    dicttypeId = parent.document.frames[0].document.all("selected_typeId").value;
 	                    dicttypeName = parent.document.frames[0].document.all("selected_typeName").value;
 	                }
 	                if(page_name!=""){
	 	                var new_path = page_name + "?did="+dicttypeId+"&orgId="+ new_orgId+"&typeName="+dicttypeName;
	 	                window.parent.document.frames[2].location.href = new_path;
 	                }
	            }
	        }
	    }
	    
	    function window.onhelp(){
	        selectOrg();
		    return false;
	    }
	    
	    function changeOrg(){
	    	var orgId = document.all("orgId").value;
	    	if(window.parent.document.frames[2]){
				var old_path = window.parent.document.frames[2].location.href;
 	            var page_name = old_path.substring(0,old_path.indexOf("?"));
 	            var dicttypeId = "";
 	            var dicttypeName = "";
 	            if(parent.document.frames[0]){
 	               dicttypeId = parent.document.frames[0].document.all("selected_typeId").value;
 	               dicttypeName = parent.document.frames[0].document.all("selected_typeName").value;
 	            }
 	            if(page_name!=""){
	 	           var new_path = page_name + "?did="+dicttypeId+"&orgId="+ orgId+"&typeName="+dicttypeName;
	 	           window.parent.document.frames[2].location.href = new_path;
 	            }
 	         }
	    }
	</script>
</html>

