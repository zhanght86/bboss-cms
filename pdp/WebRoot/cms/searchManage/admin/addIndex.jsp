<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.bean.*"%>
<%@ page import="com.frameworkset.platform.util.*" %>
<%@ page import="com.frameworkset.platform.cms.util.CMSUtil" %>
<%@ page import="java.util.List"%>
<%@page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.common.poolman.DBUtil"%>
<%@page import="java.util.Iterator,java.util.List,java.util.Set,com.frameworkset.common.poolman.sql.TableMetaData"%>

<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    
    String siteId = request.getParameter("siteId");
    //flag：0－站内搜索(包括站内频道和整站)；1－站外搜索;3-站群；4-数据库表
    int searchTypeTemp = Integer.parseInt(request.getParameter("flag").trim());
    
    String siteName = CMSUtil.getSiteCacheManager().getSite(siteId).getName();
    
    CMSSearchManager sm = new CMSSearchManager();
    
    //当前站点的整站索引是否存在
    int isAllSiteIndexExist  = sm.isAllSiteIndexExist(siteId);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>::索引信息增加:::</title>
<link href="<%=request.getContextPath()%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/cms/inc/js/func.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/include/pager.js" type="text/javascript"></script>
<script language="javascript">
	function saveIndex(){
		
		//检查合法性
		if(eiditIndexForm.indexName.value.length<=0){
			alert("请输入索引名！");
			return false;
		}
		var searchType = eiditIndexForm.searchType.value;
		if(searchType == "0"){			//站内频道索引
			if((eiditIndexForm.chnlId.value=="")||(eiditIndexForm.chnlName.value=="")){
	        	alert("请您选择频道!");
				return false;
		    }
	    }else if(searchType == "1"){												//站外索引
	    	if((eiditIndexForm.startUrl.value=="")||(eiditIndexForm.startUrl.value.indexOf('.',0)==-1)){
	        	alert("请您输入合法的索引开始点URL!");
				return false;
		    }
			if((eiditIndexForm.domainUrl.value=="")||(eiditIndexForm.domainUrl.value.indexOf('.',0)==-1)){
		        alert("请您输入合法的索引域URL!");
				return false;
		    }
	    //}else if((searchType == "2")){
	    //	if("<%=isAllSiteIndexExist%>" != "-1"){
	    //		alert("对不起，当前站点已经有整站索引，不能重复建！");
	    //		return;
	    //	}
	    }else if((searchType == "3")){
	    	if(eiditIndexForm.siteNames.value==""){
	    		alert("请输入站点名称！");
	    		return;
	    	}
	    }else if((searchType == "4")){
	    	if(eiditIndexForm.db_name.value==""){
	    		alert("请选择数据库！");
	    		return;
	    	}
	    	if(document.tablesFrame.table_name.value==""){
	    		alert("请选择表！");
	    		return;
	    	}
	    	if(eiditIndexForm.primarys.value==""){
	    		alert("请选择标记！");
	    		return;
	    	}
	    	if(eiditIndexForm.content_fields.value==""){
	    		alert("请选择内容域！");
	    		return;
	    	}
	    	if(eiditIndexForm.access_url.value==""){
	    		alert("请输入访问地址！");
	    		return;
	    	}
	    	
	    	//条件和访问地址不能有汉字
	    	var this_wheres = eiditIndexForm.wheres.value;
	    	var this_access_url = eiditIndexForm.access_url.value;
	    	if(isChinese(this_wheres)){
	    		alert("条件有汉字不合法，请重填！");
	    		return;
	    	}
	    	if(isChinese(this_access_url)){
	    		alert("访问地址有汉字不合法，请重填！");
	    		return;
	    	}
	    	
	    }
	    if(eiditIndexForm.day.value==""){
	        alert("请选择索引日期!");
			return false;
	    }
	    if((eiditIndexForm.time.value=="")){
	        alert("请您输入索引开始时间!");
			return false;
	    }
	    if((searchType == "3"))
	    {
	    	eiditIndexForm.action = "indexHandle.jsp?action=add&flag=" + searchType + "&siteNames=" + eiditIndexForm.siteNames.value;
	    }
	    else
	    if((searchType == "4"))
	    {
	    	eiditIndexForm.action = "indexHandle.jsp?action=add&flag=" + searchType + "&table_name=" + document.tablesFrame.table_name.value;
	    }	
	    else
	    {
	    	eiditIndexForm.action = "indexHandle.jsp?action=add&flag=" + searchType;
	    }	
		eiditIndexForm.target = "editIndexF";
		eiditIndexForm.submit();
	}
	
	//判断是否有汉字
	function isChinese(str){ 
		var len;
		var i;
		len = 0;
		for (i=0;i<str.length;i++){
		if (str.charCodeAt(i)>255) return true;
		}
		return false; 
	}
	
	function changeclass(){
		var id;
		id=document.eiditIndexForm.level.options[document.eiditIndexForm.level.selectedIndex].value;
		switch(id)
		{
			case '1':
			document.eiditIndexForm.day.options.length=31;
			document.eiditIndexForm.day.options[0].value='1';
			document.eiditIndexForm.day.options[0].text='每月1号';
			
			document.eiditIndexForm.day.options[1].value='2';
			document.eiditIndexForm.day.options[1].text='每月2号';
			
			document.eiditIndexForm.day.options[2].value='3';
			document.eiditIndexForm.day.options[2].text='每月3号';
			
			document.eiditIndexForm.day.options[3].value='4';
			document.eiditIndexForm.day.options[3].text='每月4号';
			
			document.eiditIndexForm.day.options[4].value='5';
			document.eiditIndexForm.day.options[4].text='每月5号';
			
			document.eiditIndexForm.day.options[5].value='6';
			document.eiditIndexForm.day.options[5].text='每月6号';
			
			document.eiditIndexForm.day.options[6].value='7';
			document.eiditIndexForm.day.options[6].text='每月7号';
			
			document.eiditIndexForm.day.options[7].value='8';
			document.eiditIndexForm.day.options[7].text='每月8号';
			
			document.eiditIndexForm.day.options[8].value='9';
			document.eiditIndexForm.day.options[8].text='每月9号';
			
			document.eiditIndexForm.day.options[9].value='10';
			document.eiditIndexForm.day.options[9].text='每月10号';
			
			document.eiditIndexForm.day.options[10].value='11';
			document.eiditIndexForm.day.options[10].text='每月11号';
			
			document.eiditIndexForm.day.options[11].value='12';
			document.eiditIndexForm.day.options[11].text='每月12号';
			
			document.eiditIndexForm.day.options[12].value='13';
			document.eiditIndexForm.day.options[12].text='每月13号';
			
			document.eiditIndexForm.day.options[13].value='14';
			document.eiditIndexForm.day.options[13].text='每月14号';
			
			document.eiditIndexForm.day.options[14].value='15';
			document.eiditIndexForm.day.options[14].text='每月15号';
			
			document.eiditIndexForm.day.options[15].value='16';
			document.eiditIndexForm.day.options[15].text='每月16号';
			
			document.eiditIndexForm.day.options[16].value='17';
			document.eiditIndexForm.day.options[16].text='每月17号';
			
			document.eiditIndexForm.day.options[17].value='18';
			document.eiditIndexForm.day.options[17].text='每月18号';
			
			document.eiditIndexForm.day.options[18].value='19';
			document.eiditIndexForm.day.options[18].text='每月19号';
			
			document.eiditIndexForm.day.options[19].value='20';
			document.eiditIndexForm.day.options[19].text='每月20号';
			
			document.eiditIndexForm.day.options[20].value='21';
			document.eiditIndexForm.day.options[20].text='每月21号';
			
			document.eiditIndexForm.day.options[21].value='22';
			document.eiditIndexForm.day.options[21].text='每月22号';
			
			document.eiditIndexForm.day.options[22].value='23';
			document.eiditIndexForm.day.options[22].text='每月23号';
			
			document.eiditIndexForm.day.options[23].value='24';
			document.eiditIndexForm.day.options[23].text='每月24号';
			
			document.eiditIndexForm.day.options[24].value='25';
			document.eiditIndexForm.day.options[24].text='每月25号';
			
			document.eiditIndexForm.day.options[25].value='26';
			document.eiditIndexForm.day.options[25].text='每月26号';
			
			document.eiditIndexForm.day.options[26].value='27';
			document.eiditIndexForm.day.options[26].text='每月27号';
			
			document.eiditIndexForm.day.options[27].value='28';
			document.eiditIndexForm.day.options[27].text='每月28号';
			
			document.eiditIndexForm.day.options[28].value='29';
			document.eiditIndexForm.day.options[28].text='每月29号';
			
			document.eiditIndexForm.day.options[29].value='30';
			document.eiditIndexForm.day.options[29].text='每月30号';
			
			document.eiditIndexForm.day.options[30].value='30';
			document.eiditIndexForm.day.options[30].text='每月31号';
			break;
		
		case '0':
			document.eiditIndexForm.day.options.length=7;
			document.eiditIndexForm.day.options[0].value='1';
			document.eiditIndexForm.day.options[0].text='星期日';
			
			document.eiditIndexForm.day.options[1].value='2';
			document.eiditIndexForm.day.options[1].text='星期一';
			
			document.eiditIndexForm.day.options[2].value='3';
			document.eiditIndexForm.day.options[2].text='星期二';
			
			document.eiditIndexForm.day.options[3].value='4';
			document.eiditIndexForm.day.options[3].text='星期三';
			
			document.eiditIndexForm.day.options[4].value='5';
			document.eiditIndexForm.day.options[4].text='星期四';
			
			document.eiditIndexForm.day.options[5].value='6';
			document.eiditIndexForm.day.options[5].text='星期五';
			
			document.eiditIndexForm.day.options[6].value='7';
			document.eiditIndexForm.day.options[6].text='星期六';
			
			break;
		
		case '2':
			document.eiditIndexForm.day.options.length=1;
			document.eiditIndexForm.day.options[0].value='1';
			document.eiditIndexForm.day.options[0].text='每天';
		
		break;
		default:
			document.eiditIndexForm.day.options.length=7;
			document.eiditIndexForm.day.options[0].value='1';
			document.eiditIndexForm.day.options[0].text='星期日';
			
			document.eiditIndexForm.day.options[1].value='2';
			document.eiditIndexForm.day.options[1].text='星期一';
			
			document.eiditIndexForm.day.options[2].value='3';
			document.eiditIndexForm.day.options[2].text='星期二';
			
			document.eiditIndexForm.day.options[3].value='4';
			document.eiditIndexForm.day.options[3].text='星期三';
			
			document.eiditIndexForm.day.options[4].value='5';
			document.eiditIndexForm.day.options[4].text='星期四';
			
			document.eiditIndexForm.day.options[5].value='6';
			document.eiditIndexForm.day.options[5].text='星期五';
			
			document.eiditIndexForm.day.options[6].value='7';
			document.eiditIndexForm.day.options[6].text='星期六';
			break;
		}
	}
	function selectChnl(){
		//传站点名字
		var siteName = document.all.siteName.value;
		if(siteName.length == 0)
		{
			alert("请先选择站点名称！");
			return;
		}
		
		var re = openWin("multi_channel_select_frame.jsp?siteName="+siteName+"&type=''&taskType=chnlIndexPage",400,500);
		if(re != undefined){
			var chnlInfo = re.split(":");
			var chnlNamess = chnlInfo[0].split(";");
			var chnlIdss = chnlInfo[1];
			
			if(chnlIdss.lastIndexOf(",")>0){
				chnlIdss = chnlIdss.substring(0,chnlIdss.lastIndexOf(","));
			}
			var a=chnlNamess[0];
			for(i=1;i<chnlNamess.length-1;i++){
				a = a + "," + chnlNamess[i]
			}
			
			document.all.chnlId.value = chnlIdss;
			document.all.chnlName.value = a;
			//返回选择的多个频道,da.wei
		}
	}
		
	//多站点选择函数
	function selectSite(){
		var sites = window.showModalDialog("../../searchManage/admin/mutisiteselecttree.jsp");
		if(!sites)
		{
			return;
		}
		else
		{	
			document.all.siteNames.value = sites.substring(0,sites.length-1);
		}		
	}
	
	//单站点选择函数
	function selectOneSite(){
		var sites = window.showModalDialog("../../searchManage/admin/singlesiteselecttree.jsp");
		if(!sites)
		{
			return;
		}
		else
		{	
			document.all.siteName.value = sites.substring(0,sites.length-1);
			if(document.all.searchType.value == "2")
			{
				document.all.indexName.value = document.all.siteName.value;
			}
		}		
	}
	
	//数据库的表的列单选函数
	function selectColumn(target){
		var db_name = document.all.db_name.value;
		var table_name = document.tablesFrame.table_name.value;
		if(!(db_name.length==0 || table_name.length==0)){
			var columns = window.showModalDialog("../../searchManage/admin/columnTree.jsp?db_name=" + db_name + "&&table_name=" + table_name);
			if(!columns)
			{
				if(target == "publishtime_field")
				document.all.publishtime_field.value = "";
				return;
			}
			else
			if(columns == "nothing done")
			{
				return;
			}
			else
			{
				//columns = columns.substring(0,columns.length-1);
				thisColumns = columns.substring(0,columns.indexOf("("));
								
				
				if(target == "publishtime_field")
				{
					var timeColumn = columns.substring(columns.indexOf("(")+1, columns.indexOf(")"));
					if(timeColumn == "DATE")
					{
						document.all.publishtime_field.value = thisColumns;
					}
					else
					{
						alert("时间域请选择时间字段！");
						return;
					}					
				}
			}
		}else{
			alert("请先选择数据库和表！");
			return;
		}				
	}
	
	//数据库的表的列多选函数
	function selectColumns(target){
		var db_name = document.all.db_name.value;
		var table_name = document.tablesFrame.table_name.value;
		if(!(db_name.length==0 || table_name.length==0)){
			var columns = "";
			if(target == "content_fields" && document.all.content_types.value == "file"){//内容域如果是文件格式，则是单选
				columns = window.showModalDialog("../../searchManage/admin/columnTree.jsp?db_name=" + db_name + "&&table_name=" + table_name);
			}else{
				columns = window.showModalDialog("../../searchManage/admin/columnsTree.jsp?db_name=" + db_name + "&&table_name=" + table_name);
			}
			if(!columns)
			{
				if(target == "primarys")
				document.all.primarys.value = "";
				if(target == "content_fields")
				document.all.content_fields.value = "";
				if(target == "title_field")
				document.all.title_field.value = "";
				if(target == "description_field")
				document.all.description_field.value = "";
				if(target == "keyword_field")
				document.all.keyword_field.value = "";
				return;
			}
			else
			if(columns == "nothing done")
			{
				return;
			}
			else
			{
				columns = columns.substring(0,columns.length-1);
				//thisColumns = columns.substring(0,columns.indexOf("("));
								
				if(target == "primarys")
				document.all.primarys.value = columns;
				if(target == "content_fields")
				document.all.content_fields.value = columns;
				if(target == "title_field")
				document.all.title_field.value = columns;
				if(target == "description_field")
				document.all.description_field.value = columns;
				if(target == "keyword_field")
				document.all.keyword_field.value = columns;
			}
		}else{
			alert("请先选择数据库和表！");
			return;
		}				
	}
	
	//数据库的表的列选函数，用于内容域
	function contentSelectColumns(){
		var db_name = document.all.db_name.value;
		var table_name = document.tablesFrame.table_name.value;
		if(!(db_name.length==0 || table_name.length==0)){
			var columns = "";
			if(document.all.content_types.value == "file"){//内容域如果是文件路径格式，则是单选
				columns = window.showModalDialog("../../searchManage/admin/columnTree.jsp?db_name=" + db_name + "&&table_name=" + table_name);
				if(!columns)
				{
					document.all.content_fields.value = "";
					return;
				}
				else
				if(columns == "nothing done")
				{
					return;
				}
				else
				{
					document.all.content_fields.value = columns.substring(0, columns.indexOf("("));
				}
			}else if(document.all.content_types.value == "text"){//内容域如果是文本格式，则是多选
				columns = window.showModalDialog("../../searchManage/admin/columnsTree.jsp?db_name=" + db_name + "&&table_name=" + table_name);
				if(!columns)
				{
					document.all.content_fields.value = "";
					return;
				}
				else
				if(columns == "nothing done")
				{
					return;
				}
				else
				{
					columns = columns.substring(0,columns.length-1);
					document.all.content_fields.value = columns;
				}
			}else if(document.all.content_types.value == "blog"){//内容域如果是Blog文件，则是复选
				columns = window.showModalDialog("../../searchManage/admin/contentColumnsTree.jsp?db_name=" + db_name + "&&table_name=" + table_name);
				if(!columns)
				{
					document.all.content_fields.value = "";
					return;
				}
				else
				if(columns == "nothing done")
				{
					return;
				}
				else
				{
					document.all.content_fields.value = columns;
				}
			}
		}else{
			alert("请先选择数据库和表！");
			return;
		}
	}
	
	function changeSearchType(ob){
		if(ob.checked == true){
			if(ob.value == "0"){
				document.all.chnlName.readOnly = false;
				document.all.chnlButton.disabled = false;
				document.all.searchType.value = "0";
			}
			else{
				document.all.chnlName.readOnly = true;
				document.all.chnlButton.disabled = true;
				document.all.searchType.value = "2";
				
				document.all.indexName.readOnly = true;
				document.all.indexName.value = document.all.siteName.value;
			}
		}
	}
	
	function dbChange(){
		var tablesFrame= document.getElementsByName("tablesFrame");
		tablesFrame[0].src = "tableOptions.jsp?db_name=" + document.all.db_name.value;	
	}
</script>
</head>

<base target="_self">
<body background="../../images/grayline_bg.jpg" scroll="no">
<form action="doc_extfield_add_do.jsp" method="post"  name="eiditIndexForm" id="eiditIndexForm">	
	<br>
	<table width="100%">
	   <tr>
	     <td>
			<table bgcolor=#0066CC cellspacing="1" cellpadding="0" width=100%>
			  <input type="hidden" value="<%=siteId%>" name="siteId">
			  <input type="hidden" value="%>" name="chnlId">
			  <input type="hidden" value="<%=searchTypeTemp%>" name="searchType"> 
			  <tr bgcolor="#FFFFFF">
				<td colspan="2" align="center" class="cms_report_tr">增加索引条目</td>
			  </tr>
			  <tr bgcolor="#FFFFFF">
				<td width="18%" align="right">索引名*</td>
				<td width="82%"><input name="indexName" type="text" value="" size="58"></td>
			  </tr>
			  <%if(searchTypeTemp == 1){%>
			  <tr bgcolor="#FFFFFF">
				<td align="right">索引开始(URL)*</td>
				<td><input name="startUrl" type="text" value="" size="37">（多个用“,”分隔,可省”http://“）</td>
			  </tr>
			  <tr bgcolor="#FFFFFF">
				<td align="right">索引范围(URL)*</td>
				<td><input name="domainUrl" type="text" value=""  size="37">（多个用“,”分隔,可省”http://“）</td>
			  </tr>
			  <%}else if(searchTypeTemp == 0){%>
			  	<tr bgcolor="#FFFFFF">
					<td align="right">站内\整站索引*</td>
					<td>
						<input name="searchTypeRadio" type="radio" value="2" onclick="changeSearchType(this)">&nbsp;整站索引
							
						<input name="searchTypeRadio" type="radio" value="0" onclick="changeSearchType(this)" checked>&nbsp;站内频道索引
					</td>
			  	</tr>
			  	<tr bgcolor="#FFFFFF">
					<td align="right">站点名称*</td>
					<td>
						<input name="siteName" type="text" value="" size="53" readOnly>
						<input type="button" class="cms_button" value="选择" onclick="selectOneSite()" name="siteButton" style="cursor:hand">
					</td>
			  	</tr>
			  	<tr bgcolor="#FFFFFF">
					<td align="right">频道名称*</td>
					<td>
						<input name="chnlName" type="text" value="" size="53" readOnly>
						<input type="button" class="cms_button" value="选择" onclick="selectChnl()" name="chnlButton" style="cursor:hand">
					</td>
			  	</tr>
			  <%}else if(searchTypeTemp == 3){%>
			  	<tr bgcolor="#FFFFFF">
					<td align="right">站点名称*</td>
					<td>
						<input name="siteNames" type="text" value="" size="53" readOnly>
						<input type="button" class="cms_button" value="选择" onclick="selectSite()" name="sitesButton" style="cursor:hand">
					</td>
			  	</tr>
			  <%}else if(searchTypeTemp == 4){%>
			  </table>
			  <table bgcolor=#0066CC cellspacing="1" cellpadding="0" width=100%>
			  	<tr bgcolor="#FFFFFF">
					<td width="18%" align="right">数据库*</td>
					<td width="32%">
						<select name="db_name" class="cms_select" style="width:180" onChange="dbChange()">
							<option value="">请选择数据库</option>
							<%
							java.util.Enumeration pools = com.frameworkset.common.poolman.util.SQLManager.getInstance().getAllPoolnames();
							while(pools.hasMoreElements())
							{
								String pool = (String)pools.nextElement();
							%>					                    
							<option value="<%=pool%>"><%=pool%></option>
							<%}%>
						</select>
					</td>
			  	
					<td width="18%" align="right">表名称*</td>
					<td width="32%">
						<iframe name="tablesFrame" src="tableOptions.jsp" height="20" width="180" scrolling="no" frameborder="0" marginwidth="1" marginheight="1"></iframe>
					</td>
			  	</tr>
			  	<tr bgcolor="#FFFFFF">
					<td align="right">标记(主键)*</td>
					<td>
						<input name="primarys" type="text" value="" size="15" readOnly>
						<input type="button" class="cms_button" value="选择" onclick="selectColumns('primarys')" name="columnButton" style="cursor:hand">
					</td>
					<td align="right">标题域&nbsp;</td>
					<td>
						<input name="title_field" type="text" value="" size="15" readOnly>
						<input type="button" class="cms_button" value="选择" onclick="selectColumns('title_field')" name="myButton" style="cursor:hand">
					</td>
			  	</tr>
			  	<tr bgcolor="#FFFFFF">
					<td align="right">关键字域&nbsp;</td>
					<td>
						<input name="keyword_field" type="text" value="" size="15" readOnly>
						<input type="button" class="cms_button" value="选择" onclick="selectColumns('keyword_field')" name="myButton" style="cursor:hand">
					</td>
					<td align="right">发布时间域&nbsp;</td>
					<td>
						<input name="publishtime_field" type="text" value="" size="15" readOnly>
						<input type="button" class="cms_button" value="选择" onclick="selectColumn('publishtime_field')" name="myButton" style="cursor:hand">
					</td>
			  	</tr>
			  	<tr bgcolor="#FFFFFF">
					<td align="right">描述域&nbsp;</td>
					<td>
						<input name="description_field" type="text" value="" size="15" readOnly>
						<input type="button" class="cms_button" value="选择" onclick="selectColumns('description_field')" name="myButton" style="cursor:hand">
					</td>
					<td align="right">内容域<select name="content_types" class="cms_select" style="width:48" onChange=""><option value="text" selected>文本</option><option value="file">文件路径</option><option value="blog">Blog文件</option></select>*</td>
					<td>
						<input name="content_fields" type="text" value="" size="15" readOnly>
						<input type="button" class="cms_button" value="选择" onclick="contentSelectColumns()" name="myButton" style="cursor:hand">
					</td>
			  	</tr>
			  	
			  	</table>
			  <table bgcolor=#0066CC cellspacing="1" cellpadding="0" width=100%>
			  	<tr bgcolor="#FFFFFF">
					<td width="18%" align="right">条件(where)*</td>
					<td width="82%"><input name="wheres" type="text" value="" size="58"></td>
			  	</tr>
			  	<tr bgcolor="#FFFFFF" height="22">
					<td width="18%" align="right">访问地址*</td>
					<td width="82%"><input name="access_url" type="text" value="" size="58"></td>
			  	</tr>
			  <%}%>
			  <tr bgcolor="#FFFFFF">
				<td align="right">索引频率*</td>
				<td>
					<%
						List listL = null;
						listL = sm.getLDTList(4);
					%>
					<select name="level" onChange="changeclass()" class="cms_select">
						<%= JSPFunc.getOption(listL,"0") %> 
					</select>
			  </tr>
			  <tr bgcolor="#FFFFFF">
				<td align="right">索引日期*</td>
				<td>
					<%
						List listD = null;
						listD = sm.getLDTList(0);
					%>
					<select name="day" class="cms_select">
						<%= JSPFunc.getOption(listD,"0") %>
					</select>
				</td>
			  </tr>
			  <tr bgcolor="#FFFFFF">
				<td align="right">开始时间*</td>
				<td>
					<%
						List listT = null;
						listT = sm.getLDTList(3);
					%>
					<select name="time" class="cms_select">
						<%= JSPFunc.getOption(listT,"0") %>
					</select>
				</td>
			  </tr>
		  </table>
		</td>
      </tr>
  	  <tr>
  	  	<td align="center">
			<input name="修改" type="button" class="cms_button" value="保存" onClick="saveIndex()" style="cursor:hand">
			&nbsp;
			<input name="返回" type="button" class="cms_button" value="返回" onClick="window.close()" style="cursor:hand">
		</td>
	  </tr>
	</table>
</form>
<iframe name="editIndexF" width="0" height="0" style="display:none"></iframe>

</body>
</html>