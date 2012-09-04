<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OperManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="java.util.*, com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Operation,com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%
	String resTypeId = request.getParameter("resTypeId");
	
	
	String jobid ="";
	String resId = request.getParameter("resId");	//这个是orgid+":"+jobId
	String[] tmp = resId.split(":");				//获得jobId
	if(tmp.length == 2){
		 jobid = tmp[1];
	}
	String orgId = request.getParameter("orgId");
	//根据机构和岗位取用户列表
	UserManager userManager = SecurityDatabase.getUserManager();
	Job job = new Job();
    job.setJobId(jobid);
    Organization org=new Organization();
    org.setOrgId(orgId);
	List userlist = null;
	userlist =userManager.getUserList(org,job);
	request.setAttribute("userlist", userlist);
	
	
	
	
	
	String roleId = (String)session.getAttribute("currRoleId");//角色Id

	if(resTypeId == null){
		resTypeId = (String)request.getAttribute("resTypeId");
	}
	if(resId == null){
		resId = (String)request.getAttribute("resId");
	}
	
	OperManager operManager = SecurityDatabase.getOperManager();
	ResourceManager resManager = new ResourceManager();

	List list = resManager.getOperations(resTypeId);
	
	List hasOper = operManager.getOperResRoleList(roleId,resId,resTypeId);
	
	if(list == null){
		list = new ArrayList();
	}	
	request.setAttribute("operList",list);//操作列表
	if(hasOper == null){
		hasOper = new ArrayList();
	}
	//判断是不是保存后的页面
	String p = (String)request.getAttribute("flag");
	if(p == null){
		p = "0";
	}
%>

<html>
<head>    
  <title>属性容器</title>
  <%@ include file="/include/css.jsp"%>
  <link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
  <link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
<SCRIPT LANGUAGE="JavaScript">

function add(){
	
	var len=document.all("userIds").options.length;
	
	if(len == 0){
		alert("该岗位下没有用户！");
		return;
	}
	var selected = 0;	//判断是不是选中用户
	for (var i=0;i<len;i++){	      
         if(document.all("userIds").options[i].selected)
         {	
         	selected = 1;
         	}
         }
	var f = <%=p%>;
	if(selected == 0){
		alert("请选择用户");
		return;
	}
	if(f == 0){
    	document.forms[0].action="../../../accessmanager/roleManager.do?method=storeRoleResOpList&resId=<%=resId%>&resTypeId=<%=resTypeId%>&orgId=<%=orgId%>";
    }
    else{ 
    	document.forms[0].action="../accessmanager/roleManager.do?method=storeRoleResOpList&resId=<%=resId%>&resTypeId=<%=resTypeId%>&orgId=<%=orgId%>";
    }
    document.forms[0].submit();
}

function setCheck(currCheck,priority)
{
   	var o = document.getElementsByName("alloper");
	var prioritylist = document.getElementsByName("priority"); 
	if (currCheck.checked==true && priority.length >1 && (priority.match(/[0-9]/))){
		for (var i=0;i<prioritylist.length;i++){
			var v = prioritylist[i].value;

			if (v.length >1 && (v.match(/[0-9]/)) && priority.substring(0,1) > v.substring(0,1) )
			{
				o[i].checked=true;
				//o[i].disabled=true;
			}
		}  
	
		for (var i=0;i<prioritylist.length;i++){
			var v = prioritylist[i].value;
			if (v.length >1 && (v.match(/[0-9]/)) && priority.substring(1,2) != v.substring(1,2) )
			{
				o[i].checked=false;
				//o[i].disabled=false
			}
		}  
	}	
	if (currCheck.checked==false  && priority.length >1 && (priority.match(/[0-9]/))){
		for (var i=0;i<prioritylist.length;i++){
			var v = prioritylist[i].value;
			if ( v.length >1 && (v.match(/[0-9]/)) && priority.substring(0,1) > v.substring(0,1) )
			{
				if ( o[i].checked==true ){
					//currCheck.checked==true;
					//o[i].disabled=false;
				}
			}
		}  

	}
	}
	var http_request = false;
			//初始化，指定处理的函数，发送请求的函数
			function send_request(url){
				http_request = false;
				//开始初始化XMLHttpRequest对象
				if(window.XMLHttpRequest){//Mozilla
					http_request = new XMLHttpRequest();
					if(http_request.overrideMimeType){//设置MIME类别
						http_request.overrideMimeType("text/xml");						
					}
				}
				else if(window.ActiveXObject){//IE
					try{
						http_request = new ActiveXObject("Msxml2.XMLHTTP");
					}catch(e){
						try{
							http_request = new ActiveXObject("Microsoft.XMLHTTP");							
						}catch(e){
						}
					}
				}
				if(!http_request){
					alert("不能创建XMLHttpRequest对象");
					return false;
				}
				http_request.onreadystatechange = processRequest;
				http_request.open("GET",url,true);
				http_request.send(null);
			}
			
			function processRequest(){
				if(http_request.readyState == 4){
					if(http_request.status == 200){
						var ops = http_request.responseText;
						alert(ops);
						var tmp = ops.split(";");				
						//alert("tmp:"+tmp);
						
						var alloper = document.getElementsByName("alloper"); 
						for (var k=0;k<alloper.length;k++) {
							alloper[k].checked=false;
						}
						
						for (var i=0;i<alloper.length;i++) {
							for (var j=0;j < tmp.length;j++) {
							
							
								if(tmp[j] == alloper[i].value){
							
									alloper[j].checked=true;
									break;
								}
							}
						}
						
					}
					//else{
					//	alert("对不起，服务器错误");
					//}
				}
			}
			
			function changebox(){
			    var userid;
				var len=document.all("userIds").options.length;
				var selected = 0;	//判断是不是选中用户
				for (var i=0;i<len;i++){	      
        		if(document.all("userIds").options[i].selected)
		         {	
		         	selected = 1;
		         	userid=document.all("userIds").options[i].value;
		         	}
		         }
		        var resId="<%=resId%>"+":"+userid;			
				var roleId= "<%=roleId%>";
				var restypeId ="user";
				send_request('saveRoleresop.jsp?resId='+resId+'&resTypeId='+restypeId+'&roleId='+roleId);
			}
			


</SCRIPT>
  <%@ include file="/include/css.jsp"%>
<body class="contentbodymargin">
<form  method="post">
<div id="contentborder">  <table width="391" border="0" align="left">
    <tr>
      <td width="126"><div align="center">&#29992;&#25143;&#21015;&#34920;</div></td>
      <td width="249" rowspan="2" valign="top">
        <table width="100%" height="22" border="0" cellpadding="0" cellspacing="2" class="table">
          <tr class="tr">
            <td class="td" width="25" height="22" >&nbsp;</td>
            <td class="td" width="100" height="22" >授予操作项</td>
            <td height="22" class="td" >&nbsp;</td>
          </tr>
          <pg:list requestKey="operList" needClear="false">
          <tr class="tr">
            <td width="25" class="td" height="22"><input name="alloper" type="checkbox"
        <%
        	String opId = dataSet.getString(Integer.parseInt(rowid),"id");
        	for(int i = 0; i < hasOper.size(); i ++)
        	{
        		Roleresop op = (Roleresop)hasOper.get(i);
        		if(op.getId().getOpId().equals(opId))
        		{
        			out.println("checked");
        			break;
        		}
        			
        	}
        %>
        value="<pg:cell colName="id"/>" onclick=setCheck(this,'<pg:cell colName="priority" defaultValue=""/>')></td>
            <td width="80" height="22" class="td"><pg:cell colName="name"/></td>
            <td height="22">&nbsp;</td>
            <input name="priority" value="<pg:cell colName="priority" defaultValue=""/>
      " type="hidden"> </tr>
          </pg:list>
        </table>
        <table width="35%" border="0" align="right" cellpadding="0" cellspacing="0" >
          <tr>
            <td><div align="center">
                <input name="mit" type="button" onclick="add()" class="input" value="保存">
            </div></td>
            <td><div align="center">
                <input name="ss" type="reset" class="input" value="重置">
            </div></td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td height="151" valign="top">
       	<select class="select" name="userIds" onchange="changebox()"  style="width:100%;height:100%" size="18" >
			<pg:list requestKey="userlist">
				<option value="<pg:cell colName="userId"/>">
					<pg:cell colName="userName" />
				</option>
			</pg:list>
	  </select>
      </td>
    </tr>
  </table>
  <pg:beaninfo requestKey="userRoleForm">
		<input type="hidden" name="uid" value="<pg:cell colName="userId"  defaultValue=""/>" />
 </pg:beaninfo>
</div>
</form>
<%@ include file="../../sysMsg.jsp"%>
</body>
</html>

