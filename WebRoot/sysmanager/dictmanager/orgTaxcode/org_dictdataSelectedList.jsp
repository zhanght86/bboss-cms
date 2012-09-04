<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../include/global1.jsp"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.dictionary.Data" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityConstants" %>
<%@ page import="org.frameworkset.spi.BaseSPIManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.*"%>
<%	
	//权限(可见)关系的数据项列表
		
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	//字典类型ID
	String ids = request.getParameter("did");
	String typeName = request.getParameter("typeName");	
	//机构ID
	String orgId = request.getParameter("orgId");
	String curOrgId = accesscontroler.getChargeOrgId();
	ids = ids==null?"":ids;
	typeName = typeName==null?"":typeName;	
	orgId = orgId==null?"":orgId;
	String dicttype_name = "";
	String did = "";
	//实际上是datavalue 父数据项的值
	String dictdataValue = "";
	String dictdataName = "";
	String data_org = "";
	session.setAttribute("dictTabId", "2");
	Data dtype = null;	
	DictManager dictManager = null;
	if(!"".equals(ids)){
		dictManager = new DictManagerImpl();
		did = ids;
		dtype = dictManager.getDicttypeById(did);
		dicttype_name = dtype.getName();
		data_org = dtype.getData_create_orgid_field();
	}
	if(dtype == null){
		dtype = new Data();
	}	
	
	if("root".equals(dictdataValue)){
	    dictdataValue="0";
	}
	 String showdata = request.getParameter("showdata");
    if(showdata == null || "".equals(showdata)){
    	showdata = "";
    }
    
    boolean isSelfOrg = curOrgId.equals(orgId);
%>
<html>
	<head>
		<title>属性容器</title>
		<link href="<%=rootpath%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">		
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
		<script language="JavaScript" src="../../user/common.js" type="text/javascript"></script>

  
<SCRIPT language="javascript">
	var jsAccessControl = new JSAccessControl("#DAE0E9","#F6F8FB","#F6F8FB");
	var win;
	var featrue = "dialogWidth=600px;dialogHeight=500px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;help=0;dialogLeft="+(screen.availWidth-600)/2+";dialogTop="+(screen.availHeight-500)/2;
	
function queryData(){
	var showdata = document.all.showdata.value;
	var orgId = "<%=orgId%>";
	var path = "org_dictdataSelectedList.jsp?did=<%=did%>&typeName=<%=typeName%>&orgId="+orgId+"&showdata="+showdata+"&identifier=selected";
	dictList.action = path;
	dictList.submit();
	document.location.href=path;
}
<!--为翻页提交做准备-->
function turnPageSumbmitSet(){
    //前提 选择了机构 选择了字典类型.
    var orgId = "";
    var dicttypeId = "";
    if(parent.parent.orgTaxcode){	            
        if(parent.parent.orgTaxcode.document.frames[0]){
            dicttypeId = parent.parent.orgTaxcode.document.frames[0].document.all("selected_typeId").value;
        }
        if(parent.parent.orgTaxcode.document.frames[1]){
            orgId = parent.parent.orgTaxcode.document.frames[1].document.all("orgId").value;
        }
	}
	if(orgId.length==0){
	    //alert("没有选择机构,此次翻页将无法保存数据!");
	    return false;
	}
    if(orgId.length>0 && dicttypeId.length>0){
	    var dicttypeValues = "";
		//var un_dicttypeValues = "";
		var arr = document.getElementsByName("dictdataValue");
	    for(var i=0;i<arr.length;i++){
	        if(arr[i].checked==true){
	            if(dicttypeValues=="") dicttypeValues = arr[i].value;
	            else dicttypeValues = dicttypeValues + "," + arr[i].value;
	        }//else{
	        //    if(un_dicttypeValues=="") un_dicttypeValues = un_dicttypeValues + arr[i].value;
	        //    else un_dicttypeValues = un_dicttypeValues + "," + arr[i].value;
	        //}	                    
	    }
	    if(dicttypeValues==""){
	        return false;
	    }
	    //编码--解码 utf-8
	    document.all.selectedNodeValue.value = encodeURI(dicttypeValues);
	    //document.all.un_selectedNodeValue.value = encodeURI(un_dicttypeValues);
	    document.all.sendOrgId.value = orgId;
	    document.all.sendDicttypeId.value = dicttypeId;	
	    document.all.readOp.value = "del";
	    return true;
    }
}



<!-- 结束 -->
</SCRIPT>		
	<body>
		<form name="dictList" action="pageSaveandDel.jsp" method="post" target="hiddenFrame">
		    <!--为翻页提交做准备-->
		    <input type="hidden" name="selectedNodeValue"> 
            <input type="hidden" name="sendOrgId"> 
            <input type="hidden" name="sendDicttypeId">
            <input type="hidden" name="readOp">
            <input type="hidden" name="dictdataValues">
           
            <!-- 结束 -->	
		    <br/>
			<p align="center"><font size=2><strong>已设置项</strong></font>
			<br/>
			
			<table cellspacing="1" cellpadding="0" border="0" width=100%  class="thin">
			<tr align="right" style="text-align:left">
				<td colspan="3" align="right" style="text-align:left">数据项：<input name="showdata" value="<%=showdata%>" type="text" />
					<input name="querySub" type="button" class="input" value="查询" onclick="queryData()" />
					<input name="retSub" type="button" class="input" value="重置" onclick="document.all.showdata.value='';" />
					
				</td>
			</tr>
				<pg:listdata dataInfo="com.frameworkset.platform.dictionary.tag.UserDictListSelected" keyName="DictList" />
				<!--分页显示开始,分页标签初始化-->
				<!--为翻页提交做准备 form="dictList" promotion="false" commitevent="turnPageSumbmitSet()" -->
				<pg:pager form="dictList" maxPageItems="10" scope="request" promotion="true"  commitevent="turnPageSumbmitSet()" data="DictList"  isList="false">
				
				    <tr>
						<!--设置分页表头-->
						<td class="headercolor" >
							<input type="checkBox"  name="checkBoxAll" onClick="checkAll('checkBoxAll','dictdataValue')">
						</td>						
						<td class="headercolor" >数据项</td>	
						<%if(!data_org.equals("") && data_org != null){%>
						<td class="headercolor">所属机构</td>	
						<%}%>
					<tr>					
					<pg:param name="did" />
					<pg:param name="typeName" />
					<pg:param name="orgId" />
					<pg:param name="identifier" />
					<!--检测当前页面是否有记录-->
					<pg:notify>
						<tr height="18px" class="labeltable_middle_tr_01">
							<td colspan=100 align='center'>
								暂时没有数据
							</td>
						</tr>
					</pg:notify>
					<!--list标签循环输出每条记录-->
					<pg:list>
					<% 
						String org_id = dataSet.getString("orgId");
						String c = "";
						String disabled = "";
						if(!accesscontroler.isAdmin() && isSelfOrg && !curOrgId.equals(org_id) ){
							c = "0";
							disabled = "disabled";
						}
					%>
						<tr>							
							<td class="tablecells" style="width:20%" nowrap="nowrap" style="text-align:center">
							    <pg:equal colName="dataValidate" value="1">
								    <input type="checkbox" name="dictdataValue<%=c %>" <%=disabled %>   value="<pg:cell colName='value' defaultValue='' />:<pg:cell colName='name' defaultValue='' />" />
								</pg:equal>
								<pg:equal colName="dataValidate" value="0">
								    <input type="checkbox" name="dictdataValue" disabled="true" value="<pg:cell colName='value' defaultValue='' />:<pg:cell colName='name' defaultValue='' />" />
								</pg:equal>
								<pg:null colName="dataValidate" >
									<input type="checkbox" name="dictdataValue"  value="<pg:cell colName='value' defaultValue='' />:<pg:cell colName='name' defaultValue='' />" />
								</pg:null>
							</td>									
							<td class="tablecells" style="width:80%;text-align:left"  nowrap="nowrap">								
								<pg:cell colName="name" defaultValue="" />
							</td>
							<%if(!data_org.equals("") && data_org != null){%>
							<td class="tablecells" style="width:80%;text-align:left"  nowrap="nowrap" >								
								<pg:cell colName="dataOrg" defaultValue="" />
							</td>
							<%}%>
						</tr>
					</pg:list>
					<input name="dictTypeId" value="<%=did%>" type="hidden">
				    <tr height="30" class="labeltable_middle_tr_01">
						<td colspan=100 align='center'><pg:index />
						<input type="hidden" name="querystring" value="<pg:querystring/>"></td>
					</tr>
					<tr height="30" class="labeltable_middle_tr_01">
					<td  colspan=100 align='center'>
					<input name="delRead" type="button" class="input" value="删除选中已设置项" onClick="delReadUsal()" />
					<input name="delAllRead" type="button" class="input" value="删除全部已设置项" onClick="delAllReadUsal()" />
					</td>
					</tr>
                
			</pg:pager>
		  </table>
		</form>
		<iframe height="0" width="0" name="hiddenFrame"></iframe>
	</body>
	<script language="javascript">
	function delReadUsal(){
		var dictdataValues = "";
		var obj=document.getElementsByName("dictdataValue");
		if(obj.length==0){
			alert("没有可删除的数据项！");
			return;
		}
		var n = 0;
		var dicttypeId = "";
		var orgId = "";
	    if(parent.parent.orgTaxcode){	            
	        if(parent.parent.orgTaxcode.document.frames[0]){
	            dicttypeId = parent.parent.orgTaxcode.document.frames[0].document.all("selected_typeId").value;
	        }
	        if(parent.parent.orgTaxcode.document.frames[1]){
	            orgId = parent.parent.orgTaxcode.document.frames[1].document.all("orgId").value;
	        }
		}
		for(var i=0;i< obj.length;i++){
		    if(obj[i].checked){
		    	if(dictdataValues == ""){
		    		dictdataValues = obj[i].value;
		    	}else{
		    		dictdataValues += "," + obj[i].value;
		    	}
		    	n++;
		    }
		}
		if(n == 0){
			alert("没有选择要删除的已设置项！");
		}else{
			document.all.dictdataValues.value = dictdataValues;
			dictList.action = "delOrgTaxcodeRead.jsp?dicttypeId="+dicttypeId+"&orgId="+orgId+"&state=read";
			dictList.submit();
		}
	}
	
	function delAllReadUsal(){
		var obj=document.getElementsByName("dictdataValue");
		if(obj.length==0){
			alert("没有要删除的已设置项数据！");
			return;	
		}
		var dicttypeId = "";
		var orgId = "";
	    if(parent.parent.orgTaxcode){	            
	        if(parent.parent.orgTaxcode.document.frames[0]){
	            dicttypeId = parent.parent.orgTaxcode.document.frames[0].document.all("selected_typeId").value;
	        }
	        if(parent.parent.orgTaxcode.document.frames[1]){
	            orgId = parent.parent.orgTaxcode.document.frames[1].document.all("orgId").value;
	        }
		}
		dictList.action = "delOrgTaxcodeRead.jsp?identifier=selected&dicttypeId="+dicttypeId+"&orgId="+orgId+"&state=allRead";
		dictList.submit();
	}
	</script>
</html>


