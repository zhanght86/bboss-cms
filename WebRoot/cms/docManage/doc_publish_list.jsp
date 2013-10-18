<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.*"%>
<%@ page import="com.frameworkset.platform.cms.util.CMSUtil,com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String curUserid = accesscontroler.getUserID();
	request.setAttribute("curUserid",curUserid);
	String docTitle = request.getParameter("docTitle");
	String docChannel = request.getParameter("docChannel");
	String docFlow = request.getParameter("docFlow");
	String flag = request.getParameter("flag");
	String submitter = request.getParameter("submitter");
	String subTime =  request.getParameter("subTime");
	//是否设有模板
	String detailTemplateFlag = request.getParameter("detailTemplateFlag");
    //外部注入发布管理
    String uuid = CMSUtil.getUUID();
    PublishMonitor monitor = PublishMonitor.createPublishMonitor();
    session.setAttribute(uuid,monitor);
%>
<html>
<head>
	<title>
		已审文档列表
	</title>
	<link  href="../inc/css/cms.css" rel="stylesheet" type="text/css"></link>
	<script language="javascript" src="../inc/js/func.js"></script>
	<script src="${pageContext.request.contextPath}/include/jquery-1.4.2.min.js"></script>
		<script src="${pageContext.request.contextPath}/include/security.js"></script>
	<script language="javascript" src=src="../../sysmanager/include/pager.js" type="text/javascript"></script>
	<script language="javascript">
		//批量发布
		function batchPublish(){
			if(haveSelect("ID")){
				form1.action = "publishDocHandle.jsp?flag=1";
				form1.target = "pubIframe";
                currentPageUrl();
				//form1.submit();
                var path = "doc_publish_info.jsp?uuid=<%=uuid%>";
                var featrue = "dialogHeight:310px;dialogWidth:450px";
                winOpen = window.showModelessDialog(path,window,featrue);
				//document.all.divProcessing.style.display="";
			}else{
				alert("请至少选择一篇文档");
			}
		}
		//发布单个文档
		function publish(docId){
			form1.action = "publishDocHandle.jsp?flag=3&docId="+docId;
			form1.target = "pubIframe";
			//form1.submit();
            currentPageUrl();
            var path = "doc_publish_info.jsp?uuid=<%=uuid%>";
            var featrue = "dialogHeight:310px;dialogWidth:450px";
            winOpen = window.showModelessDialog(path,window,featrue);
			//document.all.divProcessing.style.display="";
		}
		function garbage(){
			//将选中的文档id拼成字符串
			var all = document.getElementsByName("ID");
			var docidStr="";
			for(var i=0;i<all.length;i++){
				if(all[i].checked == true){
					docidStr = docidStr + all[i].value + ";";
				}
			}
			if(haveSelect('ID')){
				//form1.action="docGarbageHandle.jsp?docidStr="+docidStr;
				//form1.target="pubIframe";
				//form1.submit();
				$.secutiry.dosubmit("form1",
						"docGarbageHandle.jsp?docidStr="+docidStr,
								"pubIframe",
								"${pageContext.request.contextPath}");
			}else{
		    	alert("请至少要选择一篇文档！");
		   	}
		}
		//预览要发布的文档
		function preview(docId){
			//form1.action="previewPubDocHandle.jsp?docId="+docId;
			//form1.target="pubIframe";
			//form1.submit();
			
			$.secutiry.dosubmit("form1",
					"previewPubDocHandle.jsp?docId="+docId,
							"pubIframe",
							"${pageContext.request.contextPath}");
		}
		//退回，给提交人发布人一个返工文档的任务
		function withdraw(docId,taskid){
			//form1.action="pubWithdrawHandle.jsp?docId="+docId+"&taskId="+taskid;
			//form1.target="pubIframe";
			//form1.submit();
			$.secutiry.dosubmit("form1",
					"pubWithdrawHandle.jsp?docId="+docId+"&taskId="+taskid,
							"pubIframe",
							"${pageContext.request.contextPath}");
		}
        function closeSubWindow(){ 
            winOpen.close();
            document.all("cleanSession").src = "../cleanSession.jsp?uuid=<%=uuid%>";
       
        }
        //保存当前页面URL 以便刷新
        function currentPageUrl(){
            var str = document.location.href;
            var end = str.indexOf("?");
            var strArray;
            if(end != -1)
                strArray= str.slice(0,end);
            else
                strArray = str; 
           document.all("url").value = strArray+"?" + document.all.queryString.value;
        }
        //调用发布主页面的函数
	    function lastRefreshSubPage(){        
	        winOpen.updateMsg();
	    }
	    //调用发布主页面的函数
	    //url 为空的时候 直接alert(msg)
	    //url 不为空的时候 confirm()选择msg
	    function alertFun(msg,url){
	        winOpen.alertMsg(msg,url);
	    }
	</script>
	<style type="text/css">
		body {
			background-color: #ffffff;
		}
		.STYLE1 {color: blue}
	</style>
</head>
<body topmargin="2" rightmargin="1" scroll=no leftmargin="1">
	<div id="divProcessing" style="position:absolute;width:40%;height:20px;z-index:1;
			background-color:#F0f0f0;layer-background-color:#F0f0f0;left:22%;top:80%;display:none;"  class="font">   
	    <marquee direction="left" width="250" behavior="alternate"><span class=STYLE1>正在发布中，请稍等……</span></marquee>
    </div>
    <form name="form1" id="form1" action="" method="post" >
   
        <input type="hidden" name="uuid" value="<%=uuid%>">
        <input type="hidden" name="isRecordValue">
        <input type="hidden" name="recursionPublish">
        <input type="hidden" name="clearCache">
        <input type="hidden" name="url">
		<table width="100%" border="1"  cellpadding="3" cellspacing="0" bordercolor="#B7CBE4" class="Datalisttable">
		 <tr>
			<td height="25" colspan=8 background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7"><div  class="DocumentOperT">文档操作：</div><a style="cursor:hand" onClick="batchPublish()"><div  class="DocumentOper"><img 
					class="operStyle" src="../images/folder.gif">批量发布</div></a> 
<!--				<a style="cursor:hand" onclick="garbage()"><div  class="DocumentOper"><img -->
<!--					class="operStyle" src="../images/trash_small.gif">回收</u></a>  -->			</td>
		 </tr>
		<pg:listdata dataInfo="PublishDocList" keyName="PublishDocList" />
				<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="10" scope="request" data="PublishDocList" isList="false">
		<tr class="cms_report_tr">
			<td height="30" align="center" width="2%">
		  <input class="checkbox" type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')"></td>
			<td height="30" width="8%">文档类型</td>
			<td height="30" width="35%">文档标题</td>
			<td height="30" width="14%">所属站点</td>
			<td height="30" width="8%">所属频道</td>
			<td height="30" width="8%">提交人</td>
			<td height="30" width="15%">提交时间</td>
			<td height="30" width="10%">流程</td>
		</tr>
		
		<pg:param name="docTitle" />
		<pg:param name="docChannel" />
		<pg:param name="docFlow" />
		<pg:param name="submitter" />
		<pg:param name="subTime" />
		<pg:param name="curUserid" />
		<pg:param name="flag" />
		<pg:param name="detailTemplateFlag" />	
		
		<!--检测当前页面是否有记录-->
		<pg:notify>
			<tr class="labeltable_middle_tr_01">
				<td colspan=100 align='center'  height="18px">
					暂时没有待发文档				</td>
			</tr>
		</pg:notify>
		<%
			ContextMenu contextmenu = new ContextMenuImpl();
		%>
		<!--list标签循环输出每条记录-->
		<pg:list>
		<%	
			int taskid = dataSet.getInt("taskid");
			int docid = dataSet.getInt("docid");							
			
			Menu menu = new Menu();
			menu.setIdentity("doc"+docid);
								
			Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
			menuitem1.setName("发布");
			menuitem1.setLink("javascript:publish('" + docid + "')");
			menuitem1.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_fb.gif");
			menu.addContextMenuItem(menuitem1);

			Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
			menuitem2.setName("退回");
			menuitem2.setLink("javascript:withdraw('" + docid + "','" + taskid + "')");
			menuitem2.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_back.gif");
			menu.addContextMenuItem(menuitem2);						

			contextmenu.addContextMenu(menu);
		%>
		<tr class="cms_data_tr"  onMouseOver="high(this)" onMouseOut="unhigh(this)">
			<input name="docid" value="<pg:cell colName="docid" defaultValue=""/>" type=hidden>
			<td class="tablecells"  height="25" align="center">
				<input  class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" 
					value="<pg:cell colName="taskid" defaultValue=""/>:<pg:cell colName="docid" defaultValue=""/>:<pg:cell colName="docSiteid" defaultValue=""/>:<pg:cell colName="docChannelid" defaultValue=""/>">			</td>
			<td class="tablecells" align=center height="30">
				<pg:equal colName="docTpye" value="0">
				<div align="center">
					<img title="HTML文档" src="../images/doc_html.gif" width="20" height="20">				</div>
				</pg:equal>
				<pg:equal colName="docTpye" value="1">
				<div align="center">
					<img title="外部链接类型文档" src="../images/doc_link.gif" width="20" height="20">				</div>
				</pg:equal>
				<pg:equal colName="docTpye" value="2">
				<div align="center">
					<img title="下载文件类型" src="../images/doc_downloadfile.gif" width="15" height="16">				</div>
				</pg:equal>
				<pg:equal colName="docTpye" value="3">
				<div align="center">
					<img title="聚合文件类型" src="../images/doc_aggr.gif" width="15" height="16">				</div>
				</pg:equal>			</td>
			<td id="doc<%=docid%>" class="tablecells" align="left" height="25" style="cursor:hand" onClick="viewDoc(<pg:cell colName="docid" defaultValue=""/>,form1,'pubIframe')">
				<pg:cell colName="docName" defaultValue="" />
			</td>
			<td class="tablecells" align="left" height="25">
				<pg:cell colName="docSiteName" defaultValue="" />			</td>
			<td class="tablecells" align="left" height="25">
				<pg:cell colName="docChannelName" defaultValue="" />			</td>
			<td class="tablecells" align="left" height="25">
				<pg:cell colName="submitUserName" defaultValue="" />			</td>
			<td class="tablecells" align="left" height="25">
				<pg:cell colName="submitTime" dateformat="yyyy/MM/dd HH:mm:ss" defaultValue="" />			</td>
			<td height="25" align="left"  class="tablecells">
				<pg:cell colName="docFlowName" defaultValue="" />			</td>
		</tr>
		</pg:list>
		<%
			request.setAttribute("opdoc",contextmenu);
		%>
		<pg:contextmenu context="opdoc" enablecontextmenu="true" scope="request"/>
		<tr class="labeltable_middle_tr_01">
			<td colspan=10><div class="Data_List_Table_Bottom">
				共
				<pg:rowcount />
				条记录
		  <pg:index /></div></td>
		</tr>
	  <input id="queryString" name="queryString" value="<pg:querystring/>" type="hidden">
	  </pg:pager>
	</table>
  </form>
  <div width=0 height=0 style="display:none">
  	<iframe name="pubIframe"></iframe>    
  </div>
  <iframe name="cleanSession" id="cleanSession"  width="0" height="0"  style="display:none" ></iframe>
</body>
</html>