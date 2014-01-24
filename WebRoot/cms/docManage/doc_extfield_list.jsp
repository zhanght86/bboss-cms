<%
/**
  *  文档的扩展字段列表
  *
  */
%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*,com.frameworkset.common.poolman.DBUtil"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.*,java.util.List,com.frameworkset.platform.cms.documentmanager.bean.Extvaluescope"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	String id = request.getParameter("id");
	String docid =  request.getParameter("docid");
%>
<html>
	<head>
		<base target=_self><!-- use for submit to self-->
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>.::::::::::文档扩展字段管理::::::::::::::::::::::::::::::::::::::::::::::::::::..</title>
		<script src="../inc/js/func.js"></script>
		<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
		<script type="text/javascript" src="../../public/datetime/calender_date.js" language="javascript"></script>
		<script language="javascript">
 
	//add 扩展字段
	function addextfield()
	{
		openWin("doc_extfield_add.jsp?id=<%=id%>",400,400);
	}
	//del 扩展字段
	function delextfield()
	{
		var all = document.getElementsByName("ID");
		var ids = "";
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				ids = ids + all[i].value + ",";
			}
		}
		if(haveSelect('ID')){
			if(window.confirm("删除扩展字段有可能造成数据丢失\n您确定要删除吗？"))
			{
				openWin("doc_extfield_del_do.jsp?type=2&id=<%=id%>&ids=" + ids,400,500);
				return true;
			}
			else
			{
				return false;
			}
		}else{
			alert("请选择要删除的项！");
			return false;
		}
		
	}
	//选择文件
	var fileFlag = "";
	function choosemedia(cur)
	{
		fileFlag = cur.name;
		openWin("../channelManage/chooseImageFrameset.jsp?fileFlag=file",520,580);
	}
	function setImage(uri){
		try{
		document.all(fileFlag).value  = uri;
		}catch(e){}
	}
	//open文本编辑器
	function opentext(name)
	{
		openWin("doc_extfield_edittextarea.jsp?name=" + name,520,580);
	}
	//open HTML编辑器
	function openHtml(name)
	{
		openWin("doc_extfield_edithtml.jsp?name=" + name,700,580);
	}
	//检测是否输入的是数字
	function checknum(cur)
	{
		if(cur.value.search(/\D/g)!=-1){
			alert("请填写数字!");
			cur.value = "";
			//cur.focus();
			return false;
		}
	}
	//------------------------------------------------------------------------------------------------------------
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
						//alert(http_request.responseText);
					}
					else{
						alert("对不起，服务器错误");
					}
				}
			}

			function changebox(currCheck,docid,fieldid,clobvalue,flag){
			
				//checked
				if(currCheck.checked){
					send_request('saveExtfieldvalue.jsp?docid='+docid+'&fieldid='+fieldid+'&clobvalue='+clobvalue+'&checked=1&flag='+flag);
				}
				//not checked
				else{
					send_request('saveExtfieldvalue.jsp?docid='+docid+'&fieldid='+fieldid+'&clobvalue='+clobvalue+'&checked=0&flag='+flag);
				}

				//setCheck(currCheck,rid,priority);
			}
	</script>
	
	</head>

	<body topmargin="2" rightmargin="0">
	<br>
		<table width="95%" cellpadding="3" cellspacing="0" border="0" class="Datalisttable" id="docextfieldlist">
			<pg:listdata dataInfo="com.frameworkset.platform.cms.customform.tag.DocExtFieldOfSiteOrChlList" keyName="DocExtFieldOfSiteOrChlList" />
			<!--分页显示开始,分页标签初始化-->
			<pg:pager scope="request" data="DocExtFieldOfSiteOrChlList" isList="true">
				<tr class="cms_report_tr">
					<!--设置分页表头-->
					<td width="10%" align="center" height='30'>
						字段名称
					</td>
					<td  width="20%" align="center" height='30'>
						字段label
					</td>
					<td width="20%" align="center" height='30'>
						字段类型
					</td>
					<!--<td width="10%" align="center" height='30'>
						字段最大长度
					</td>-->
					<td width="20%" align="center" height='30'>
						字段内容
					</td>
					<td width="30%" align="center" height='30'>
						字段描述
					</td>
				</tr>

				<pg:param name="id" />
				<pg:param name="type" />
				
				<!--检测当前页面是否有记录-->
				<pg:notify>
					<tr>
						<td colspan=100 align='center' height="18px">
							还没有扩展字段
						</td>
					</tr>
				</pg:notify>

				<!--list标签循环输出每条记录-->
				<pg:list>
					<tr id="<pg:cell colName="fieldId" defaultValue=""/>">
						<input name="fieldId" value="<pg:cell colName="fieldId" defaultValue=""/>" type=hidden>
						<input name="fieldName" value="<pg:cell colName="fieldName" defaultValue=""/>" type=hidden>
						<input name="fieldType" value="<pg:cell colName="fieldType" defaultValue=""/>" type=hidden>
						<td class="tablecells" nowrap="nowrap" width="10%">
							<pg:cell colName="fieldName" defaultValue="" />
						</td>
						<td class="tablecells" nowrap="nowrap" width="10%">
							<pg:cell colName="fieldLable" defaultValue="" />
						</td>
						<td class="tablecells" nowrap="nowrap" width="10%">
							<pg:equal colName="fieldType" value="0">number</pg:equal>
							<pg:equal colName="fieldType" value="1">varchar</pg:equal>
							<pg:equal colName="fieldType" value="2">datetime</pg:equal>
							<pg:equal colName="fieldType" value="3">clob</pg:equal>
							<pg:equal colName="fieldType" value="4">file</pg:equal>
							<pg:equal colName="fieldType" value="5">select</pg:equal>
							<pg:equal colName="fieldType" value="6">radiobox</pg:equal>
							<pg:equal colName="fieldType" value="7">checkbox</pg:equal>
						</td>
						<!--<td class="tablecells" nowrap="nowrap" width="10%">
							<pg:notequal colName="maxlen" value="0">
							<pg:cell colName="maxlen" defaultValue="" /></pg:notequal>
							<pg:equal colName="maxlen" value="0">&nbsp;</pg:equal>
						</td>-->
						<td class="tablecells" nowrap="nowrap" align="left">
							<pg:equal colName="fieldType" value="0">
								<input type="text" name="<pg:cell colName="fieldId" defaultValue=""/>_value" value="<pg:cell colName="numbervalue" defaultValue="" />" onchange="checknum(this);"/>
							</pg:equal>
							<pg:equal colName="fieldType" value="1">
								<input type="text" name="<pg:cell colName="fieldId" defaultValue=""/>_value" value="<pg:cell colName="extfieldvalue" defaultValue="" />" />
							</pg:equal>
							<pg:equal colName="fieldType" value="2">
								<input type="text" name="<pg:cell colName="fieldId" defaultValue=""/>_value" value="<pg:cell colName="datevalue" defaultValue="" />" readonly="true" onClick="return showdate(this)"/>
							</pg:equal>
							<pg:equal colName="fieldType" value="3">
								<!--<textarea name="<pg:cell colName="fieldId" defaultValue=""/>_value" cols="50" rows="5"><pg:cell colName="clobvalue" defaultValue="" /></textarea>
								<br>-->
								<input type="hidden" name="<pg:cell colName="fieldId" defaultValue=""/>_value" value="<pg:cell colName="clobvalue" defaultValue="" />"/>
								<input type="button" name="edittext" value="文本编辑器" onclick="opentext('<pg:cell colName='fieldId' defaultValue=''/>_value');"/>
								<input type="button" name="edithtml" value="HTML编辑器" onclick="openHtml('<pg:cell colName='fieldId' defaultValue=''/>_value');"/>
							</pg:equal>
							<pg:equal colName="fieldType" value="4">
								<input type="text" name="<pg:cell colName="fieldId" defaultValue=""/>_value" value="<pg:cell colName="extfieldvalue" defaultValue="" />" readonly="true" onClick="choosemedia(this)"/>
							</pg:equal>
							<pg:equal colName="fieldType" value="5">
								
								<select name="<pg:cell colName="fieldId" defaultValue=""/>_value" style="margin-center:-100px;width:118px;" id="<pg:cell colName="fieldId" defaultValue=""/>_value">
								<option value="" >--请选择--</option>
						  	  <%
								  List selectlist=null;
								  DocumentManager dmi=new DocumentManagerImpl();
								  Extvaluescope ds=new Extvaluescope();
								  String fieldId = dataSet.getString("fieldId");
								  String clobvalue = dataSet.getString("clobvalue");
								  if(clobvalue==null)
								  {
								  	clobvalue="";
								  }
								 
								  selectlist=dmi.getFieldValueList(id,fieldId,"5");
								  request.setAttribute("selectlist",selectlist);
								  
								
								 
							  %>
							  <pg:list requestKey="selectlist" dataSetName="selectlistds" rowidName="selectlistrd">
							  <option value="<pg:cell colName="id"/>"<pg:equal colName="id" value="<%=clobvalue%>">selected</pg:equal>>
							  <pg:cell colName="value"/>
							  </pg:list>
							</select>
							</pg:equal>
							
							 <pg:equal colName="fieldType" value="6">
							   <%
							   	  List radiolist = null;
								  DocumentManager dmi2=new DocumentManagerImpl();
								 
								  String fieldId2 = dataSet.getString("fieldId");
								  String fieldvalue = dataSet.getString("extfieldvalue");
								
								  if(fieldvalue==null)
								  {
								  	fieldvalue="";
								  }
									radiolist = dmi2.getFieldValueList(id,fieldId2,"6");
							   		request.setAttribute("radiolist",radiolist);
							   %>
							   <form name="<pg:cell colName="fieldId" defaultValue=""/>_radio" method="post" action="">
							   <pg:list requestKey="radiolist" dataSetName="radiolistds" rowidName="radiolistrd">
							   	
							 	<input name="<pg:cell colName="fieldId" defaultValue=""/>_radio"  type="radio"
							 	<pg:equal colName="id" value="<%=fieldvalue%>">checked</pg:equal>
							 	value="<pg:cell colName="id"/>" onclick="changebox(this,'<%=docid%>','<%=fieldId2%>','<pg:cell colName="id"/>','radiobox')">
							 	<pg:cell colName="value"/>
							   </pg:list>
							   </form>
							 </pg:equal>
							 
							 
							  <pg:equal colName="fieldType" value="7">
								<%
							   	  List checkboxlist = null;
								  DocumentManager dmi3=new DocumentManagerImpl();
								 
								  String fieldId3 = dataSet.getString("fieldId");
								  String fieldvalue3 = dataSet.getString("extfieldvalue");
								  if(fieldvalue3==null)
								  {
								  	fieldvalue3="";
								  }
									checkboxlist = dmi3.getFieldValueList(id,fieldId3,"7");
							   		request.setAttribute("checkboxlist",checkboxlist);
							   %>
							   <pg:list requestKey="checkboxlist" dataSetName="checkboxlistds" rowidName="checkboxlistrd">
							 	
								<input name="<pg:cell colName="fieldId" defaultValue=""/>_checkbox" type="checkbox"
								
								<%
						        	DBUtil db = new DBUtil();
						        	String sql ="select FIELDVALUE from TD_CMS_EXTFIELDVALUE where FIELD_ID="+fieldId3+" and DOCUMENT_ID="+docid+"";
						        	db.executeSelect(sql);
						        	String eid = dataSet.getString("id");
						        	if(db.size()>0)
						        	{
						        		for(int i=0;i<db.size();i++)
						        		{
						        			if(eid.equals(db.getString(i,"FIELDVALUE")))
						        			{
						        				out.println("checked");
						        				break;
						        			}
						        		
						        		}
						        	}
						        	
						       	%> 
								value="<pg:cell colName="id"/>"  onclick="changebox(this,'<%=docid%>','<%=fieldId3%>','<pg:cell colName="id"/>','checkbox')">
							   <pg:cell colName="value"/>
							   </pg:list>
							   
							 </pg:equal>
						</td>
						<td class="tablecells" nowrap="nowrap" align="center" width="*">
							<pg:cell colName="fieldDesc" defaultValue="" />
						</td>
					
					</tr>
				</pg:list>
				<tr></tr>
			</pg:pager>
	</table>
	</body>
</html>


