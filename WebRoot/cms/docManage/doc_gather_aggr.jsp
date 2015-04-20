<%@ page contentType="text/html; charset=UTF-8" language="java" 
import="java.util.List,java.util.Date,java.text.SimpleDateFormat,java.util.StringTokenizer"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.templatemanager.*
,com.frameworkset.platform.cms.container.Template,com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@page import="com.frameworkset.common.poolman.DBUtil"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    String userid = accesscontroler.getUserID();
	//当前时间
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	java.util.Date currentTime = new java.util.Date(); 
	String riqi = formatter.format(currentTime); 

	String channelname = request.getParameter("channelName");
	String siteid = request.getParameter("siteid");
	String channelId = request.getParameter("channelId");
	
	ChannelManager cm = new ChannelManagerImpl();
	//获取所选频道的细缆模板ID
	String selecttemplateid="-1";
	
	Template selecttemplate = cm.getDetailTemplateOfChannel(channelId);
	if(selecttemplate != null)
	{
		selecttemplateid = String.valueOf(selecttemplate.getTemplateId());
	}
	
	SiteManager siteManager = new SiteManagerImpl();
	String sitename = siteManager.getSiteInfo(siteid).getName();
	
	//文档来源的加载js数组
    String idstr="";
    String srcnamestr="";
	List dslist=null;
	DocumentManager dmi=new DocumentManagerImpl();
	DocumentSource ds=new DocumentSource();
	dslist=dmi.getDocSourceList();
   if(dslist.size()>0)
   {
	    for(int i=0;i<dslist.size();i++)
	    {
	       ds=(DocumentSource)dslist.get(i);
	        if(idstr!="")
	        {
	        idstr=idstr+","+ds.getDocsource_id();
	        srcnamestr=srcnamestr+"&"+ds.getSrcname();
	        }else
	        {
	        idstr=String.valueOf(ds.getDocsource_id());
	        srcnamestr=ds.getSrcname();
	        }
	    }
   }
	
	
	String orgName = "" ;
    if(accesscontroler.getChargeOrg() != null)
    {
    	orgName = accesscontroler.getChargeOrg().getRemark5();
    }
			
	//获取上一次文档的信息如：文档来源 
	String docSourceName = orgName;
	String docSourceId = "";

   try{
	   DBUtil db = new DBUtil();
	   //String sql = "select a.DOCSOURCE_ID,a.SRCNAME from TD_CMS_DOCSOURCE a , TD_CMS_DOCUMENT b where a.DOCSOURCE_ID = b.DOCSOURCE_ID and rownum =1 order by b.Document_Id desc" ;
	   String sql = "select a.DOCSOURCE_ID,a.SRCNAME from TD_CMS_DOCSOURCE a where a.srcname='"+orgName+"'";
	   db.executeSelect(sql) ;
	   if(db.size()>0){
	   		docSourceId = db.getString(0,0);
	   		docSourceName = db.getString(0,1); 
	   }
	}catch(Exception e){}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<pg:config enablecontextmenu="false" enabletree="false"/>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/common/scripts/esbCommon.js"></script>
<link href="<%=request.getContextPath()%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/cms/inc/js/func.js"></script>
<script src="<%=request.getContextPath()%>/cms/inc/js/killerror.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/datetime/calender.js" language="javascript"></script>
<style type="text/css">
	<!--
	.STYLE7 {color:#003398; font-size: 12px;}
	#Layer1 {
		position:absolute;
		left:0px;
		top:0px;
		width:113px;
		height:127px;
		z-index:1;
	}
	.STYLE9 {color: #FF6600}
	.STYLE1 {color: red}
	-->
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CMS 文档管理</title>
</head>
<script language="javascript">
var api = frameElement.api, W = api.opener;
	//动画窗口对象
	var win = null;
	//关闭动画窗口
	function closewin()
	{
		win.close();
	}
	//选择new图标文件
	function setImage(uri){
		document.all("newpicpath").value  = uri;
	}
	//选择new图标文件
	function choosenewpic()
	{
		openWin("<%=request.getContextPath()%>/cms/channelManage/chooseImageFrameset.jsp?fileFlag=pic",620,580);
	}
	//站点窗口输入参数校验
	function saveform(closeFlag){
	    var inputsource=document.all.inputdocsource.value;
		if(myform.title.value==""){
			alert("请输入文档标题!");
			myform.title.focus();
			return false;
		}
		if(inputsource==null||inputsource=="")
		{
			alert('请选择或者填写文档来源');
		  	document.all.inputdocsource.focus();
		  	return false;
		}
		else{
			//判定是否新加的文档来源，是设置
			//if(inputsource!=document.all.docsource_id.options(document.all.docsource_id.selectedIndex).text)
			//document.all.isnewdocsource.value="1";
			
			 if(document.all.docsource_id.value.length<1)
		    {
				document.all.isnewdocsource.value="1";
			}
			
			if(document.all.selecttpl[1].checked)
				myform.parentDetailTpl.value = "1";
			else
				myform.parentDetailTpl.value = "0";
			myform.subtitle.value = myform.title.value;
			//取相关文档列表
			var arr = docxz.document.getElementsByName("docid");
			var arr2 = docxz.document.getElementsByName("aggrtitle");
			for(var i=0;i<arr.length;i++)
			{
				myform.doclist.value += arr[i].value +"№";
				myform.titles.value += arr2[i].value +"№";
			}
			
			 var url = "${pageContext.request.contextPath}/document/addDocument.page?flag=" + closeFlag;
				
				$.ajax({
					   type: "POST",
						url : url,
						data :formToJson("#myform"),
						dataType : 'json',
						async:false,
						beforeSend: function(XMLHttpRequest){
								
					      		blockUI();	
					      		XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					      				 	
							},
						success : function(responseText){
							//去掉遮罩
							unblockUI();
							if(responseText=="success"){
								
								$.dialog.alert("操作文档成功",function(){	
										W.modifyQueryData();
										api.close();
								},api);													
							}else{
								$.dialog.alert("操作文档出错",function(){},api);
							}
							
							
							
							
						}
					  });
			
			//myform.method="post";
			//myform.target = "addaggr";
			//myform.action="<%=request.getContextPath()%>/cms/docManage/add_document.jsp?flag=" + closeFlag;
			//win = window.open("<%=request.getContextPath()%>/cms/doing.html","doinghtml","height="+(screen.availHeight-200)+",width="+(screen.availWidth-300)+",top=100,left=150,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no");
			//myform.submit();
			//var buttons = document.getElementsByTagName("input");
			//for(var i=0;i<buttons.length;i++)
			//{
			//	buttons[i].disabled = true;
			//}
		}
	}
	function tabClick(i){
		if(i==2){
			document.all.docxz.style.display = "";
			document.all.docxz.style.width = "95%";
			document.all.docxz.style.height = "300px";
			document.all.tab2.style.color = "#FF6600";
		}
	}
	//返回
	function back() {
		window.close();
	}

	//焦点放在第一个text
	function init(){
		changtpl(1);
		myform.title.focus();
	}
	
	function isnum()
	{
	  var k = window.event.keyCode;
	  if (k < 48 || k > 57){
	     alert("你输入的不是数字！")
	     window.event.keyCode = 0 ;}
	}
	function changSelect(o)
	{
	   document.all.inputdocsource.value=document.all.docsource_id.options(document.all.docsource_id.selectedIndex).text;
	   document.all.inputdocsource.focus();
	   document.all.inputdocsource.select();
	}
	//响应rodio事件
	function changtpl(type)
	{
		if(type==0){
			myform.btnTemplateList.disabled = false;
			myform.tName.disabled = false;
		}else{
			myform.btnTemplateList.disabled = true;
			myform.tName.disabled = true;
		}
	}
	//预览模板
	function preview()
	{
		var tplId = document.all.detailtemplate_id.value;
		window.open("<%=request.getContextPath()%>/cms/templateManage/template_previewbyid.jsp?tplId=" + tplId + "&siteId=<%=siteid%>");
	}	
	function subpublish(){
		showModalDialog("doc_PublisherList.jsp?flag=4&channelId=<%=channelId%>",window,"dialogWidth:400px;dialogHeight:500px;help:no;scroll:auto;status:no");
	}
	function publish(){
		document.all.divProcessing.style.display="";
		saveform(12);
	}
	function deliver(){
		showModalDialog("doc_AuditorList.jsp?flag=4&channelId=<%=channelId%>",window,"dialogWidth:400px;dialogHeight:500px;help:no;scroll:auto;status:no");
	}
	function templateChoice(){
		var channelName = "<%=channelname%>";
		var w = showModalDialog("../channelManage/channel_templateSet_frameset.jsp?siteId="+
		"<%=siteid%>&channelId=<%=channelId%>&channelName="+encodeURIComponent(channelName)+"&detailTemplateName="+myform.tName.value+
		"&dtId="+myform.detailtemplate_id.value+"&type=2&action=search&isSite=doc","setChannelOutlineTemplateId",
		"dialogWidth:800px;dialogHeight:505px;help:no;scroll:yes;status:no");
		//alert(w[0]+"    "+w[1]);
		if(w!=undefined && w.length>0)
		{
			myform.detailtemplate_id.value=w[0];
			myform.tName.value=w[1];
		}
	}
	
	//处理文档来源
	var myarray=new Array();
	var myarrayid=new Array();
	var srcnameadd='<%=srcnamestr%>';
	var srcnameidadd='<%=idstr%>'
	myarray=srcnameadd.split('&');
	myarrayid=srcnameidadd.split(',');
	function selectvalue(selectstr,selectid)
	{
		
	  document.getElementById("inputdocsource").value=selectstr;
	  document.all.isnewdocsource.value="0";
	  document.all.ischecksource.value='1';
	  document.all.docsource_id.value=selectid;
	  
	  divhidden();
	  
	}
	function divShow() { 
	var tt=document.getElementById("inputdocsource");
	var  ttop    =  tt.offsetTop;          //TT控件的定位点高  
	var  thei    =  tt.clientHeight;    //TT控件本身的高  
	var  tleft  =  tt.offsetLeft;        //TT控件的定位点宽 
	 var d=document.getElementById("id1");
	while  (tt  =  tt.offsetParent){ttop+=tt.offsetTop;  tleft+=tt.offsetLeft;}
	 d.style.display="block";
	 d.style.pixelLeft=tleft+1;
	 d.style.pixelTop=ttop+thei+4;
	 
	} 
	function divhidden()
	{
	
	var mydiv1=document.getElementById("id1");
	mydiv1.style.display="none";
	
	}
	var initsearch='true';
	function initdiv1()
	{
		if(initsearch=='true')
		{
			var mydiv1=document.getElementById("id1");
			var str='<table width=120 border=0 cellpadding=1 cellspacing=1 bgcolor=#006699>';
			var sr='';
			 for(var i=0;i<myarray.length;i++)
			{
			 sr=sr+' <tr style="cursor:hand" onClick=\"selectvalue(\''+myarray[i]+'\',\''+myarrayid[i]+'\')\"><td bgcolor=#FFFFFF height=22>&nbsp;'+myarray[i]+'</td></tr>';
			}
			str=str+sr+'</table>';
			mydiv1.innerHTML=str;
			divShow();
		}
		else
		{
			initdiv();
		}
	}
	
	function initdiv()
	{
		//初试化div
		var t=document.getElementById("inputdocsource").value;
		if(t.length<1)
		{
			var mydiv1=document.getElementById("id1");
			mydiv1.style.height="300px";
			var str='<table width=120 border=0 cellpadding=1 cellspacing=1 bgcolor=#006699>';
			var sr='';
		    for(var i=0;i<myarray.length;i++)
			{
			 sr=sr+' <tr style="cursor:hand" onClick=\"selectvalue(\''+myarray[i]+'\',\''+myarrayid[i]+'\')\"><td bgcolor=#FFFFFF height=22>&nbsp;'+myarray[i]+'</td></tr>';
			}
			str=str+sr+'</table>';
			mydiv1.innerHTML=str;
			divShow();
		}
		else
		{
		  changdiv();
		}
	}
	
	function divHide(d) 
	{ 
		var d=document.getElementById("id1");
		if(d.style.display=='block')
	 		d.style.display="none";
	} 
	
	function changdiv()
	{
		initsearch='false';
		if(document.all.ischecksource.value!='1')
		{
			document.all.ischecksource.value='0';
			document.all.docsource_id.value="";
		}
		var t=document.getElementById("inputdocsource").value;
		if(t.length<1)
		{
		   initdiv();
		}
		else
		{
		//添加行
		   divShow();
		   var patternstr=t;
		   var mydiv=document.getElementById("id1");
		   var sr='';
		   var divstr='';
		   var count=0;
		   var count_1 = 0;
		   for(var i=0;i<myarray.length;i++)
		   {
			   if(myarray[i].match(patternstr)!=null)
			   {
				 sr=sr+' <tr style="cursor:hand"  onClick=\"selectvalue(\''+myarray[i]+'\',\''+myarrayid[i]+'\')\"><td bgcolor=#FFFFFF height=22>&nbsp;'+myarray[i]+'</td></tr>';
				 count=1;
				 count_1++;
			   }
		   }
		   if(count==1)
		   {
		     divstr='<table width=120 border=0 cellpadding=1 cellspacing=1 bgcolor=#006699>'+sr+'</table>'; 
		   }
		   else
		   {
		    divstr='<table width=120 border=0 cellpadding=1 cellspacing=1 bgcolor=#006699><tr><td bgcolor=#FFFFFF height=22>&nbsp;没有稿源</td></tr></table> ';
		   }
		   mydiv.style.height=count_1*24+"px";
		   mydiv.innerHTML=divstr;
		}
	}  
</script>
<!--下面的代码 使得按 F5会刷新modal页面-->
<base target="_self">
<body  bottommargin="0" bgcolor="#F3F4F9" onkeydown="if(event.keyCode==116){reload.click()}" rightmargin="0" leftmargin="0" topmargin="0" onLoad="init()">
<script language="javascript">
	document.write("<a id='reload' href='" + document.location.href + "' style='display:none'>reload...</a>");
</script>
<!--上面的代码 使得按 F5会刷新modal页面-->
<!--发布跑马灯 -->
<div id="divProcessing" style="position:absolute;width:40%;height:20px;z-index:1;
			background-color:#F0f0f0;layer-background-color:#F0f0f0;left:22%;top:80%;display:none;"  class="font">   
	    <marquee direction="left" width="250" behavior="alternate"><span class=STYLE1>正在发布中，请稍等……</span></marquee>
</div>

<form target="addlinks" name="myform" id="myform">
	<div id="auditorDiv" name="auditorDiv" style="display:none;"></div>
	<div id="publisherDiv" name="publisherDiv" style="display:none;"></div>
	<input name="doctype" value="3" type=hidden><!--聚合文档-->
	<input name="userid" value="<%=userid%>" type=hidden>
  	<input name="channelId" value="<%=channelId%>" type=hidden>
	<input name="siteid" value="<%=siteid%>" type=hidden>
  	<input name="channelName" value="<%=channelname%>" type=hidden>
	<input type="hidden" name="titlecolor" /><!--标题颜色-->
	<input name="subtitle" type="hidden"/><!--显示标题-->
	<input name="author" value="不详" type="hidden"/><!--作者-->
	<input name="isnewdocsource" value="0" type="hidden"><!--是否新稿源-->
	<!--input name="detailtemplate_id" type="hidden"/><!--发布模板-->
	<input name="content" type="hidden"/><!--content-->
	<!-- <input type="hidden" name="parentDetailTpl" value="1"/> -->
	<input type="hidden" id="ischecksource">
	<input type="hidden" id="docsource_id" name="docsource_id" value="<%=docSourceId%>">
	<table width="99%" border="0" align="center" cellpadding="0" cellspacing="0">
				            	<tr>
					              	<td height="47" colspan="3" valign="top" background="<%=request.getContextPath()%>/cms/images/top_tile_short.jpg">
						              	<table width="100%" border="0" cellpadding="0" cellspacing="0">
							                <tr>
							                  <td width="200"><img src="<%=request.getContextPath()%>/cms/images/logo.jpg"></td>
							                  <td>内容采集 [<%=sitename%> 站点  :  <%=channelname%> 频道]</td>
							                </tr>
						              	</table>					              	</td>
				              	</tr>
					            <tr>
						            <td height="18" colspan="3">
							            <table width="100%" border="0" cellpadding="0" cellspacing="0">
							            	<tr background="<%=request.getContextPath()%>/cms/images/Gather_Content_topbg.gif">
								                <td width="4%" height="35" align="right">
								                	<img src="<%=request.getContextPath()%>/cms/images/ico_point.gif" width="25" height="25">								                </td>
								                <td colspan="2" class="cms_title_blue">
								                	<table width="100%" border="0" cellpadding="3">
								                    	<tr>
									                        <td class="cms_title_blue" width="100">文档信息填写</td>
									                        <td>
										                        (
										                        <span class="red_star">*</span>
										                           为必填项
										                        <span class="red_star">
										                        <img src="<%=request.getContextPath()%>/cms/images/tan.gif" width="11" height="11">
										                        </span>
										                        )									                        
										                    </td>
									                    </tr>
								                    </table>								                
								                </td>
							                </tr>
							                <tr>
							                	<td height="26">
						                      <label>&nbsp;</label></td>
							                  	<td height="23" colspan="2">
								                  	<table border="0" cellpadding="0" cellspacing="0">
									                    <tr>
									                      <td><strong>文档类型:</strong></td>
									                      <td>
										                      <div align="center">
										                      	<img src="<%=request.getContextPath()%>/cms/images/doc_html.gif" width="20" height="20">
										                      </div>									                      </td>
									                      <td>
									                      	<a href="<%=request.getContextPath()%>/cms/docManage/doc_gather.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>">普通文档</a>									                      </td>
									                      <td>
										                      <div align="center">
										                      	<img src="<%=request.getContextPath()%>/cms/images/doc_link.gif" width="20" height="20">										                      
										                      </div>									      
														  </td>
									                      <td>
									                      	<a href="<%=request.getContextPath()%>/cms/docManage/doc_gather_links.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>">外部链接</a>									                      </td>
									                      <td width="3%">
																<div align="center">
																	<img src="<%=request.getContextPath()%>/cms/images/doc_aggr.gif" width="20" height="20">																
																</div>														  
														  </td>
														  <td>
														    <a href="<%=request.getContextPath()%>/cms/docManage/doc_gather_aggr.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>"><font color="#FF6600">聚合文档</font></a>															
														  </td>
														</tr>
							                  	  </table>							                    
							                  	</td>
							                </tr>
							                <tr>
							                	<td height="26">&nbsp;</td>
												<td width="6%" height="23" align="right"><strong>链接标题:</strong></td>
							                  	<td width="90%" height="23" colspan="2">
							                  		<span class="cms_title_blue">
									                    <label>
									                    <input name="title" type="text" class="cms_text" size="60">
									                    </label>
							                  	  </span>
							                  		<span class="STYLE7">
							                  			<label>
							                  				<span class="red_star">*</span>							                  			
							                  			</label>
							                  		</span>
													&nbsp;&nbsp;&nbsp;&nbsp;
													
													<!-- 
													<strong>文档来源:</strong>
							                    	 <span class="black" >
							                    		<span class="STYLE7">
							                    			<span class="red_star">
							                    			   
							                    				  <span class="cms_title_blue">
							                    				<input type="text" style="width:100px;height:21px;font-size:10pt;" id="inputdocsource" name="inputdocsource"><span style="width:18px;border:0px solid red;">
							                    				<select name="docsource_id" style="margin-left:-100px;width:118px;" onChange="changSelect(this)" id="docsource_id">
							                     						<%
												                        //  List dslist=null;
																	     // DocumentManager dmi=new DocumentManagerImpl();
																	     // DocumentSource ds=new DocumentSource();
																		 // dslist=dmi.getDocSourceList();
																		 // request.setAttribute("dslist",dslist);
																		%>
													  					<pg:list requestKey="dslist">
													  	  					<option value="<pg:cell colName="docsource_id"/>"><pg:cell colName="srcname"/>
													  	  					</option>
													  					</pg:list>
							                    					</select>
							                    					</span>
		                   				   						</span>							                    			
		                   				  					 </span>						                    		
		                   				  				 </span>							                    	
		                   				   			</span>	
		                   				  			 -->	
		                   				  			 
		                   				  			 <label>
														<strong>文档来源:</strong>
													</label>
													<span class="cms_title_blue"> 
														<input type="text" style="width:120px;height:21px;font-size:10pt;" id="inputdocsource" name="inputdocsource" onmouseover="initdiv1()"
																 onpropertychange="changdiv()" onmouseout="divhidden()" value="<%=docSourceName%>">
														<div id="id1" STYLE="position:absolute;display:none;z-index:100;background: #fff; overflow-y:scroll; width:100px; height:300px" 
																	onmouseover="divShow()" onmouseout="divhidden()"></div>
													    <span class="red_star">*</span> 
													</span>									
		                   				   </td>
							                </tr>							               
							                <tr>
							                	<td height="26">&nbsp;</td>
												<td height="23" align="right"><strong>编稿时间:</strong></td>
							                  	<td height="23" colspan="2">
													
							                  		<label>
														 <span class="black">
											  				<span class="cms_title_blue">
																<input name="docwtime" type="text" class="cms_text" value="<%=riqi%>" size="22" onpropertychange="ordertime.value=this.value">&nbsp;
																<input type="button" class="Channel_5wordsBtn" value="时间" onClick="showdatetime(document.all('docwtime'))"></span>
															</span>
													</label>
													&nbsp;&nbsp;&nbsp;&nbsp;
													<span class="cms_title_blue">
												级别:
														<select name="doc_level" class="cms_select">
															<%
															  List doclevellist = null;
															  DocumentManager impl = new DocumentManagerImpl();
															  doclevellist = impl.getDocLevelList();
															  request.setAttribute("doclevellist",doclevellist);
															%>
															<pg:list requestKey="doclevellist">
																<option value="<pg:cell colName="id"/>">
																<pg:cell colName="name"/> 
															</pg:list>
														</select>
													</span>
											  </td>
							                </tr>
							                <tr>
							                	<td height="26">&nbsp;</td>
												<td height="23" align="right"><strong>排序时间:</strong></td>
							                  	<td height="23" colspan="2">
							                  		<span class="black">
														<span class="cms_title_blue">
															<input name="ordertime" type="text" class="cms_text" value="<%=riqi%>" size="22">
														</span>
													</span>
													<input type="button" class="Channel_5wordsBtn" value="时间" onClick="showdatetime(document.all('ordertime'))">										
		                   				   		</td>
							                </tr>
							                <tr height="23">
							                <td>&nbsp;</td>
							                <td colspan="3"><strong>发布模板:</strong>
							                <span class="cms_title_blue">
												<input type="radio" name="selecttpl" value="0"  onClick="changtpl(0)">
												<input name="detailtemplate_id" id="detailtemplate_id" type="hidden" value="" />
												<input name="tName" id="tName" type="text" style="width:130px" value=""  class="cms_text" readonly />
												<input name="btnTemplateList" id="btnTemplateList" type="button" class="Channel_5wordsBtn" onclick="templateChoice()" value="选 择"  />
											</span>		
							               
							                
							                <input type="radio" name="selecttpl" value="1" checked="true"  onClick="changtpl(1)">
																		<input type="hidden" name="parentDetailTpl"/>
																		<%
																		ChannelManager chnl = new ChannelManagerImpl();
																		Template tp = chnl.getDetailTemplateOfChannel(channelId);
																		String tname = (tp==null?"还没有设置":tp.getName());
																		%>
																		继承所属频道的细览模板(<span class="red_star"><%=tname%></span>)	  <a href="javascript:void(0)" onClick="preview()" ><u>预览</u></a>	
							                </td>
							                </tr>
											<tr>
												<td height="26">&nbsp;</td>
												<td colspan="3">
													<strong>是否标记为new:</strong>&nbsp;&nbsp;
													<input type="checkbox" hideFocus name="isnew" value="1" class="cms_text"/>&nbsp;&nbsp;
													<input type="text" name="newpicpath" value="" class="cms_text" readonly size="30"/>
													<input type="button" class="Channel_5wordsBtn" name="default" onClick="document.all('newpicpath').value='image/new.gif'" value="使用默认值"/>
													<input type="button" class="Channel_7wordsBtn" name="addnewpic" onClick="choosenewpic()" value="选择new图标"/>
												</td>
											</tr>
							                <tr>
												<td height="26">&nbsp;												</td>
												<td height="23" align="right"><strong>摘要内容:</strong></td>
											    <td colspan="2"><textarea name="docabstract" cols="70" rows="5" class="cms_textarea"></textarea></td>
										    </tr>
											<tr>
												<td height="26">&nbsp;												</td>
												<td height="23" align="right"><strong>关 键 词:</strong></td>
											    <td colspan="2"><textarea name="keywords" cols="70" rows="5" class="cms_textarea"></textarea>
												(<span class="red_star">多个关键词之间用逗号“,”分隔</span>)
												</td>
										    </tr>
					        	    </table>
								    </td>
					            </tr>
								
								<tr>
									<td  width="95%" height="22" colspan="3"  align="center" valign="top">
										<div>
											<input type="hidden" name="doclist">
											<input type="hidden" name="titles">
											<br/><iframe id="docxz" src="<%=request.getContextPath()%>/cms/channelManage/chn_doc_xzlist.jsp?flag=11&siteid=<%=siteid%>" frameborder="0" scrolling="auto" width="95%" height="300">
											</iframe>
										</div>									</td>
								</tr>
								<tr>
									<td  height="23">
										<div align="center">
											<INPUT name="button" type="button" onClick="saveform(5)" class="cms_button"  value="保存">
											<%
											if(impl.canTransition(Integer.parseInt(channelId),1,2)!=-1){%>
											<input type="button" value="送审" class="cms_button" onClick="deliver();">
											<%}
											if(impl.canTransition(Integer.parseInt(channelId),1,11)!=-1){%>
											<input type="button" value="提交发布" class="cms_button" onClick="subpublish();">
											<%}
											if(impl.canTransition(Integer.parseInt(channelId),1,5)!=-1){%>
											<input type="button" value="立即发布" class="cms_button" onClick="publish();">
											<%}%>
											&nbsp;<input type="button" value="返回" class="cms_button" onClick="back()">
										</div>									</td> 
								</tr>
								<tr><td colspan="4">&nbsp;</td></tr>
								<tr><td colspan="4">&nbsp;</td></tr>
			            		<!--来源于别的网站才有父站点-->
  </table>  
  <script type="text/javascript">
  //document.all.inputdocsource.value=document.all.docsource_id.options(document.all.docsource_id.selectedIndex).text;
  </script> 
</form>
<div style="display:none">
	<iframe name="addaggr" width="0" height="0"></iframe>
</div> 
</body>
</html>