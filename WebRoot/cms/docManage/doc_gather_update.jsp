<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.common.poolman.DBUtil"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.templatemanager.*
,com.frameworkset.platform.cms.container.Template,com.frameworkset.platform.cms.documentmanager.bean.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.cms.channelmanager.*,com.frameworkset.platform.cms.util.*"%>
<%@page import="com.frameworkset.platform.cms.driver.htmlconverter.*"%>
<%@page import="com.frameworkset.platform.cms.driver.i18n.*"%>
<%@page import="java.util.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl,java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.TemplateInfo" %>
<%@ page import="com.frameworkset.platform.cms.container.Template" %>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>


<%
String rootpath  = request.getContextPath();
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	String userid = accesscontroler.getUserID();
   
	String docid = request.getParameter("docid");
	String channelname = request.getParameter("channelName");
	String siteid = request.getParameter("siteid");
	String channelId = request.getParameter("channelId");
	
	DocumentManager dmi = new DocumentManagerImpl();
	ChannelManager cm = new ChannelManagerImpl();
	SiteManager siteManager = new SiteManagerImpl();
	TemplateManager tm = new TemplateManagerImpl();
	//时间
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	java.util.Date currentTime = new java.util.Date(); 
	String riqi = formatter.format(currentTime); 
	 //在返工文档编辑时传来rebound，在审核时传来audit，其他出来为空(返工文档是有任务id的)
   	String flag =  request.getParameter("flag");
   	int taskid = 0;
   	String taskidStr="";
   	if(flag!=null && flag.equals("rebound")){
   		taskid = dmi.hasTask(Integer.parseInt(userid),Integer.parseInt(docid),2);
   	}
   	else if(flag!=null && flag.equals("audit")){
   		taskid = dmi.hasTask(Integer.parseInt(userid),Integer.parseInt(docid),1);
   		taskidStr = request.getParameter("taskidStr");
   	}

	Document document = dmi.getDoc(docid);

	String status = String.valueOf(document.getStatus());
	
	String sitename = siteManager.getSiteInfo(siteid).getName();
	String docsource = String.valueOf(document.getDocsource_id());
	String tid = String.valueOf(document.getDetailtemplate_id());
	Template t = tm.getTemplateInfo(tid);
	String tName = "";
	if(t!=null) tName = t.getName();
	String docLevelId = String.valueOf(document.getDoc_level());//文档级别
	//图片，附件等的保存路径 docpath
	String sitedir = siteManager.getSiteInfo(siteid).getSiteDir();//频道相对路径
	String relativePath = cm.getChannelInfo(request.getParameter("channelId")).getChannelPath();//站点相对路径
	String docpath = sitedir + "/_webprj/" + relativePath + "/content_files";

	//处理文档内容
	CmsLinkProcessor processor = new CmsLinkProcessor(request,relativePath,sitedir);
	processor.setHandletype(CmsLinkProcessor.PROCESS_READCONTENT);
	try {
		String content = processor.process(document.getContent(),CmsEncoder.ENCODING_UTF_8);
		document.setContent(content);
	} catch (Exception e) {
		e.printStackTrace();
	}

	//get文档的相关文档的信息
	String partId = "";
	String partName = "";
	String partChlName = "";
	String[] doclists =  dmi.getDocRelatedString(Integer.parseInt(docid));
	for(int i=0;i<doclists.length;i++)
	{
		String[] tem = doclists[i].split("№");
		partId += tem[0] + "№";
		partName += tem[1] + "№";
		partChlName += tem[2] + "№";
	}
	//get文档的相关附件的信息
	String attId = "";
	String attDesc = "";
	String attUrl = "";
	String ofilename = "";
	String[] atts = dmi.getAllRelationOfDocument2String(Integer.parseInt(docid),2);
	for(int i=0;i<atts.length;i++)
	{
		String[] tem = atts[i].split("№");//对应关系
		attId += tem[0] + "№";
		attDesc += tem[4] + "№";
		attUrl += tem[2] + "№";
		ofilename += tem[5] + "№";
	}
	//get文档的相关图片的信息
	String attId2 = "";
	String attDesc2 = "";
	String attUrl2 = "";
	String ofilename2 = "";
	String[] atts2 = dmi.getAllRelationOfDocument2String(Integer.parseInt(docid),3);
	for(int i=0;i<atts2.length;i++)
	{//System.out.println(atts2[i]);
		String[] tem = atts2[i].split("№");//对应关系
		attId2 += tem[0] + "№";
		attDesc2 += tem[4] + "№";
		attUrl2 += tem[2] + "№";
		ofilename2 += tem[5] + "№";
	}
	//文档来源开始
   String idstr="";
   String srcnamestr="";
   String orasrcnamestr="";
	List dslist=null;
	DocumentSource ds=new DocumentSource();
	dslist=dmi.getDocSourceList();
   if(dslist.size()>0)
   {
   
    for(int i=0;i<dslist.size();i++)
    {
       ds=(DocumentSource)dslist.get(i);
        if(docsource.equals(String.valueOf(ds.getDocsource_id())))
        {
        orasrcnamestr=ds.getSrcname();
        }
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
	//文档来源结束
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<pg:config enablecontextmenu="false" enabletree="false"/>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/common/scripts/esbCommon.js"></script>
<link href="<%=request.getContextPath()%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/cms/inc/js/func.js"></script>
<script src="<%=request.getContextPath()%>/cms/inc/js/killerror.js"></script>
<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
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
.STYLE8 {color: #0000FF}

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
	//W.modifyQueryData();
	api.close();
}
	//字符转换
	function HTMLEncodeCMS(text){
		//text = text.replace(/"/g, "&quot;");
		return text;
	}
	//选择图片新闻的概览图片
	var fileFlag;
	function chooseImage(){
		fileFlag = "pic";
		openWin("<%=request.getContextPath()%>/cms/channelManage/chooseImageFrameset.jsp?fileFlag=picOrFlash",700,580);
	}
	function setImage(uri){
		if(fileFlag == "pic")
			document.all("picpath").value  = uri;
		if(fileFlag == "media")
			document.all("mediapath").value  = uri;
		if(fileFlag == "newpic")
			document.all("newpicpath").value  = uri;
	}
	//选择多媒体文件
	function choosemedia()
	{
		fileFlag = "media";
		openWin("<%=request.getContextPath()%>/cms/channelManage/chooseImageFrameset.jsp?fileFlag=media",620,580);
	}
	//选择new图标文件
	function choosenewpic()
	{
		fileFlag = "newpic";
		openWin("<%=request.getContextPath()%>/cms/channelManage/chooseImageFrameset.jsp?fileFlag=pic",620,580);
	}
	//预览模板
	function preview()
	{
		var tplId = myform.detailtemplate_id.value;
		window.open("../templateManage/template_previewbyid.jsp?tplId=" + tplId + "&siteId=<%=siteid%>");
	}
	
	//站点窗口输入参数校验
	function saveform(closeFlag){
		if(closeFlag != 5)
		{
			if(!confirm('您确定修改文档吗？')){
				return;
			}
		}
		if(!validateForm(myform))
		{
			return;
		}
		var inputsource=document.all.inputdocsource.value;
		//编辑器保持在设计模式
		eWebEditor1.setMode('EDIT');
		
		if(myform.title.value==""){
			alert("请输入文档标题!");
			myform.title.focus();
			return false;
		}
		if(myform.subtitle.value==""){
			alert("请输入显示标题!");
			myform.subtitle.focus();
			return false;
		}
		var seq = myform.seq.value;
		if(seq!=""&&seq.search("^[0-9]*[0-9][0-9]*$")!=0)
		{
		 alert("请输入数字类型排序号");
		 document.all.auditorDiv.innerHTML="";
			document.all.publisherDiv.innerHTML="";
			myform.seq.focus();
		 return false;
		}
		if(seq>=999999)
		{
		  alert("请确定排序号为小于999999的数字");	
          document.all.auditorDiv.innerHTML="";
          document.all.publisherDiv.innerHTML="";
			myform.seq.focus();
		  return false;
		}
		if(myform.keywords.value=="")
		{
			alert("请输入关键词!");
			document.all.auditorDiv.innerHTML="";
			document.all.publisherDiv.innerHTML="";
			myform.keywords.focus();
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
			if(document.all.docsource_id.value.length<1)
		    {
				document.all.isnewdocsource.value="1";
			}
			//取相关文档列表
			var arr = new Array();
			arr = docxz.document.getElementsByName("docid");
			for(var i=0;i<arr.length;i++){
				myform.doclist.value += arr[i].value +"№";
			}
			//取相关附件列表
			var arr2 = new Array();
			var arr3 = new Array();
			var arr4 = new Array();
			arr2 = docattachlist.document.getElementsByName("description");
			arr3 = docattachlist.document.getElementsByName("originalFilename");
			arr4 = docattachlist.document.getElementsByName("url");
			for(var i=0;i<arr2.length;i++)
			{
				myform.description.value += arr2[i].value + "№";
				myform.originalFilename.value += arr3[i].value + "№";
				myform.url.value += arr4[i].value + "№";
			}
			//取相关图片列表
			var arr22 = new Array();
			var arr32 = new Array();
			var arr42 = new Array();
			arr22 = docattachlist2.document.getElementsByName("description");
			arr32 = docattachlist2.document.getElementsByName("originalFilename");
			arr42 = docattachlist2.document.getElementsByName("url");
			for(var i=0;i<arr22.length;i++)
			{
				myform.description2.value += arr22[i].value + "№";
				myform.originalFilename2.value += arr32[i].value + "№";
				myform.url2.value += arr42[i].value + "№";
			}
			var tempcontent = eWebEditor1.eWebEditor.document.body.innerHTML;
			setPageTagNone("eWebEditor1");
			myform.content.value = HTMLEncodeCMS(eWebEditor1.eWebEditor.document.body.innerHTML);
			
			if(myform.content.value=="")
			{
				alert("请输入文档内容!");
				return false;
			}
			if(document.all.selecttpl[1].checked)
				myform.parentDetailTpl.value = "1";
			else
				myform.parentDetailTpl.value = "0";

			//扩展字段
			//var extfieldvalues = docextfielslist.document.getElementsByName("extfielvalue");
			var fieldis = docextfielslist.document.getElementsByName("fieldId");
			var fldtypes = docextfielslist.document.getElementsByName("fieldType");
			var fldnames = docextfielslist.document.getElementsByName("fieldName");
			myform.extfieldvalues.value = "";
			myform.fieldids.value = "";
			myform.extfieldtypes.value = "";
			myform.extfieldnames.value = "";
			for(var ef=0;ef<fieldis.length;ef++)
			{
				
				var tmp = new Array();
				tmp = docextfielslist.document.getElementsByName(fieldis[ef].value+"_value");
				//alert(tmp.length);
				if (tmp.length<=0)
					continue;
					
				myform.extfieldvalues.value += tmp[0].value + "№";
				myform.fieldids.value += fieldis[ef].value + "№";
				myform.extfieldtypes.value += fldtypes[ef].value + "№";
				myform.extfieldnames.value += fldnames[ef].value + "№";
			}
			//特殊要求(便于当值为空时java 的split（）方法)
			myform.extfieldvalues.value += "null№";

			myform.method="post";
			
			//新加的专题报道
			var specialobj=specialreportlist.document.getElementsByName("ID");
			var specialsite='';//专题报道对应的站点串
			var specialchannelid='';//专题报道对应的频道id串
			for(var specialsize=0;specialsize<specialobj.length;specialsize++)
			{
			var specialarr=specialobj[specialsize].value.split('&');
			if(specialsite=='')
			  {
			     specialsite=specialarr[0];
			  }
			  else
			  {
			   specialsite=specialsite+'&'+specialarr[0];
			  }
			  if(specialchannelid=='')
			  {
			     specialchannelid=specialarr[1];
			  }
			  else
			  {
			   specialchannelid=specialchannelid+'&'+specialarr[1];
			  }
			}
			myform.specialsiteid.value=specialsite;
			myform.sepcialchannelid.value=specialchannelid;
			
			var url = "${pageContext.request.contextPath}/document/updateDocument.page?closeFlag=" + closeFlag+"&detailtemplate_id="+myform.detailtemplate_id.value;
			if(closeFlag == 100)
				url =url +'&taskid=<%=taskid%>';
			if(closeFlag==5)
			{
				url = "${pageContext.request.contextPath}/document/previewDoc.page";
			}
			$.ajax({
				   type: "POST",
					url : url,
					data :formToJson("#myform"),
					dataType : 'json',
					async:false,
					beforeSend: function(XMLHttpRequest){
							
				      		blockUI();	
				      		XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				      		eWebEditor1.setHTML(tempcontent);

							 	
						},
					success : function(responseText){
						//去掉遮罩
						unblockUI();
						if(closeFlag!=5)
						{
							if(responseText=="success"){
								
								{							
								
									$.dialog.alert("操作文档成功",function(){	
											W.modifyQueryData();
											api.close();
									},api);
								}
							}else{
								$.dialog.alert("操作文档出错",function(){},api);
							}
						}
						else
						{
							if(responseText=="fail"){
								
								$.dialog.alert("预览文档出错，可能当前频道未设置细览模板或者模板文件不存在",function(){},api);
											 
																					
							}else{
								//$.dialog({ title:"新增文档-<%=channelname%>",width:800,height:600, content:'url:'+responseText,lock: false,maxState:true});
								window.open(responseText);
							}
						}
						
						
						
					}
				  });
		}
	}
	//返回
	function back() {
		closewin();
	}

	//焦点放在第一个text
	function init(){
		var ptpl = "<%=document.getParentDetailTpl()%>";
		myform.title.focus();
		document.all.tab1.style.color = "#FF6600";
		if(ptpl==0)
		{
			document.all.selecttpl[0].checked = true;
		}
		if(ptpl==1)
		{
			document.all.selecttpl[1].checked = true;
			document.all.btnTemplateList.disabled = true;
		}
	}
	//光标是停在文本框文字的最后
	function focusend() 
	{ 
		var e = event.srcElement; 
		var r =e.createTextRange(); 
		r.moveStart("character",e.value.length); 
		r.collapse(true); 
		r.select(); 
	}
	//tab切换
	function tabClick(i){
		if(i==1){
			document.all.docxz.style.display = "none";
			document.all.docattachlist.style.display = "none";
			document.all.docattachlist2.style.display = "none";
			document.all.docextfielslist.style.display = "none";
			document.all.eWebEditor1.style.display = "";
			document.all.tab1.style.color = "#FF6600";
			document.all.tab2.style.color = "";
			document.all.tab3.style.color = "";
			document.all.tab4.style.color = "";
			document.all.tab5.style.color = "";
			document.all.tab6.style.color = "";
		}
		if(i==2){
			document.activeElement.blur();
			//myform.title.focus();//解决iframe焦点问题
			document.all.eWebEditor1.style.display = "none";
			document.all.docattachlist.style.display = "none";
			document.all.docattachlist2.style.display = "none";
			document.all.docextfielslist.style.display = "none";
			document.all.docxz.style.display = "";
			document.all.docxz.style.width = "95%";
			document.all.docxz.style.height = "300px";
			document.all.tab1.style.color = "";
			document.all.tab2.style.color = "#FF6600";
			document.all.tab3.style.color = "";
			document.all.tab4.style.color = "";
			document.all.tab5.style.color = "";
			document.all.tab6.style.color = "";
		}
		if(i==3){
			document.activeElement.blur();
			//myform.title.focus();//解决iframe焦点问题
			document.all.eWebEditor1.style.display = "none";
			document.all.docxz.style.display = "none";
			document.all.docattachlist2.style.display = "none";
			document.all.docextfielslist.style.display = "none";
			document.all.docattachlist.style.display = "";
			document.all.docattachlist.style.width = "95%";
			document.all.docattachlist.style.height = "300px";
			document.all.tab1.style.color = "";
			document.all.tab2.style.color = "";
			document.all.tab3.style.color = "#FF6600";
			document.all.tab4.style.color = "";
			document.all.tab5.style.color = "";
			document.all.tab6.style.color = "";
		}
		if(i==4){
			document.activeElement.blur();
			//myform.title.focus();//解决iframe焦点问题
			document.all.eWebEditor1.style.display = "none";
			document.all.docxz.style.display = "none";
			document.all.docattachlist.style.display = "none";
			document.all.docextfielslist.style.display = "none";
			document.all.docattachlist2.style.display = "";
			document.all.docattachlist2.style.width = "95%";
			document.all.docattachlist2.style.height = "300px";
			document.all.tab1.style.color = "";
			document.all.tab2.style.color = "";
			document.all.tab3.style.color = "";
			document.all.tab4.style.color = "#FF6600";
			document.all.tab5.style.color = "";
			document.all.tab6.style.color = "";
		}
		if(i==5){
			document.activeElement.blur();
			//myform.title.focus();//解决iframe焦点问题
			document.all.eWebEditor1.style.display = "none";
			document.all.docxz.style.display = "none";
			document.all.docattachlist.style.display = "none";
			document.all.docattachlist2.style.display = "none";
			document.all.docextfielslist.style.display = "";
			document.all.docextfielslist.style.width = "95%";
			document.all.docextfielslist.style.height = "300px";
			document.all.tab1.style.color = "";
			document.all.tab2.style.color = "";
			document.all.tab3.style.color = "";
			document.all.tab4.style.color = "";
			document.all.tab5.style.color = "#FF6600";
			document.all.tab6.style.color = "";
		}
		if(i==6){
			document.activeElement.blur();
			//myform.title.focus();//解决iframe焦点问题
			document.all.eWebEditor1.style.display = "none";
			document.all.docxz.style.display = "none";
			document.all.docattachlist.style.display = "none";
			document.all.docattachlist2.style.display = "none";
			document.all.docextfielslist.style.display = "none";
			document.all.specialreportlist.style.display = "";
			document.all.specialreportlist.style.width = "95%";
			document.all.specialreportlist.style.height = "300px";
			document.all.tab1.style.color = "";
			document.all.tab2.style.color = "";
			document.all.tab3.style.color = "";
			document.all.tab4.style.color = "";
			document.all.tab5.style.color = "";
			document.all.tab6.style.color = "#FF6600";
		}
	}
	//初始化相关文档table表的数据
	function initRelatedDoc()
	{
		var part_id = "<%=partId%>";
		var part_name = "<%=CMSUtil.filterStr(partName)%>";
		var part_ChlName = "<%=CMSUtil.filterStr(partChlName)%>";
		
		if(part_id!=""&&part_name!="")
		{
			var ids = new Array();
			var names = new Array();
			var chlnames = new Array();
			ids = part_id.split("№");
			names = part_name.split("№");
			chlnames = part_ChlName.split("№");
			var tableatt = docxz.document.all("chndoclist");
			for(var i=0;i<ids.length-1;i++)
			{
				var row1 = tableatt.insertRow(i+1);
				var td1 = docxz.document.createElement("td");
				var td2 = docxz.document.createElement("td");
				var td3 = docxz.document.createElement("td");

				td1.innerHTML="<input type='checkBox' name='id' value="+(i+1)+" hideFocus=true onclick='checkOne(id)'/>";		 
				var docid = "<input type='hidden' name='docid' value='"+ids[i]+"' />";
				//var channelId = "<input type='hidden' name='channelId' value='"+obj[1]+"' />";
				td2.innerHTML = docid + names[i];
				td3.innerHTML = chlnames[i];
				row1.appendChild(td1);
				row1.appendChild(td2);
				row1.appendChild(td3);
			}
		}
	}
	//初始化相关附件table表的数据
	function initAtt()
	{
		var att_id = "<%=attId%>";
		var att_desc = "<%=attDesc%>";
		var att_url = "<%=attUrl%>";
		var att_ofilename = "<%=ofilename%>";
		
		if(att_id!=""&&att_url!="")
		{
			var ids = new Array();
			var descs = new Array();
			var urls = new Array();
			var ofilenames = new Array();

			ids = att_id.split("№");
			descs = att_desc.split("№");
			urls = att_url.split("№");
			ofilenames = att_ofilename.split("№");

			var tableatt = docattachlist.document.all("doc_attach_list");
			for(var i=0;i<ids.length-1;i++)
			{
				var row1 = tableatt.insertRow(i+1);
				var td1 = docattachlist.document.createElement("td");
				var td2 = docattachlist.document.createElement("td");
				var td3 = docattachlist.document.createElement("td");

				td1.innerHTML="<input type='checkBox' name='id' value="+ids[i]+" hideFocus=true onclick='checkOne(id)'/>";		 
				td2.innerHTML = "<input type='text' name='description' value='"+descs[i]+"'/>" +
					"<input type='hidden' name='originalFilename' value='"+ofilenames[i]+"'/>"
					+ "<input type='hidden' name='url' value='"+ urls[i]+"'/>";
				td3.innerHTML = "<a href='#' onclick=downloadattrs('../siteResource/<%=docpath%>/"+urls[i]+"')>"+urls[i]+"</a>";
				row1.appendChild(td1);
				row1.appendChild(td2);
				row1.appendChild(td3);
			}
		}
	}
	//初始化相关图片table表的数据
	function initAttPic()
	{
		var att_id = "<%=attId2%>";
		var att_desc = "<%=attDesc2%>";
		var att_url = "<%=attUrl2%>";
		var att_ofilename = "<%=ofilename2%>";
		
		if(att_id!=""&&att_url!="")
		{
			var ids = new Array();
			var descs = new Array();
			var urls = new Array();
			var ofilenames = new Array();

			ids = att_id.split("№");
			descs = att_desc.split("№");
			urls = att_url.split("№");
			ofilenames = att_ofilename.split("№");

			var tableatt = docattachlist2.document.all("doc_attach_list");
			for(var i=0;i<ids.length-1;i++)
			{
				var row1 = tableatt.insertRow(i+1);
				var td1 = docattachlist2.document.createElement("td");
				var td2 = docattachlist2.document.createElement("td");
				var td3 = docattachlist2.document.createElement("td");

				td1.innerHTML="<input type='checkBox' name='id' value="+ids[i]+" hideFocus=true onclick='checkOne(id)'/>";		 
				td2.innerHTML = "<input type='text' name='description' value='"+descs[i]+"'/>" +
					"<input type='hidden' name='originalFilename' value='"+ofilenames[i]+"'/>"
					+ "<input type='hidden' name='url' value='"+ urls[i]+"'/>";
				//td3.innerHTML = "<a href='../siteResource/<%=docpath%>/"+urls[i]+"'>"+urls[i]+"</a>";
				td3.innerHTML = "<a href='#' onclick=downloadattrs('<%=request.getContextPath()%>/cms/siteResource/<%=docpath%>/"+urls[i]+"')>"+urls[i]+"</a>";
				row1.appendChild(td1);
				row1.appendChild(td2);
				row1.appendChild(td3);
			}
		}
	}
	//初始化相关table
	function document.onreadystatechange()
	{
		if (document.readyState!="complete") 
			return;
		initRelatedDoc();
		initAtt();
		initAttPic();
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
	function publishPreview(){
		myform.method="post";
		myform.action="<%=request.getContextPath()%>/cms/docManage/previewPubDocHandle.jsp?docId=<%=docid%>";
		myform.submit();
	}
	function seeAdvice(channelId,docid,taskid){
		openWin("<%=rootpath%>/cms/docManage/see_audit_advice.jsp?channelId=<%=channelId%>&docid=<%=docid%>&taskid=<%=taskid%>" ,400,550);
	}
	function delive(){
		//saveform("0");
		//flag为1表示新稿文档的称颂,为2表示返工文档的呈送
		//openWin("<%=rootpath%>/cms/docManage/doc_AuditorList.jsp?flag=2&channelId=<%=channelId%>&docid=<%=docid%>&taskid=<%=taskid%>");
		//var str = window.dialogArguments.location.href;
		//var end = str.indexOf("?");
		//var strArray;
		//if(end != -1)
		//	strArray= str.slice(0,end);
		//else
		//	strArray = str;
		//window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
		//window.close();
		showModalDialog("<%=rootpath%>/cms/docManage/doc_AuditorList.jsp?flag=44&channelId=<%=channelId%>&docid=<%=docid%>&taskid=<%=taskid%>",window,"dialogWidth:400px;dialogHeight:500px;help:no;scroll:auto;status:no");
	}
	function agree(){
		var re;
		//参数auditFlag为审核意见，0表不同意，1表同意
		re = openWin("<%=rootpath%>/cms/docManage/audit_add_comment.jsp?idStr=<%=taskidStr%>&auditFlag=1",400,550);
		if(re == "cf"){ 
			//var str = window.dialogArguments.location.href;
			//var end = str.indexOf("?");
			//	var strArray;
			//	if(end != -1)
			//		strArray= str.slice(0,end);
			//	else
			//		strArray = str;
			//window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
			//window.close();
			W.modifyQueryData();
			api.close();
		}
	}
	function disagree(){
		var re;
		//var addComment = window.confirm("要增加审核意见吗？");
		//if(addComment==false){
		//	re = "cf";
		//	myform.method="post";
		//	//参数auditFlag为审核意见，0表不同意，1表同意
		//	myform.action = "<%=rootpath%>/cms/docManage/auditDocHandle.jsp?idStr=<%=taskidStr%>&auditFlag=0";
		//	myform.submit();
		//}
		//else{
			re = openWin("<%=rootpath%>/cms/docManage/audit_add_comment.jsp?idStr=<%=taskidStr%>&auditFlag=0",400,550);
		//}
		if(re == "cf"){
			//var str = window.dialogArguments.location.href;
			//var end = str.indexOf("?");
			//	var strArray;
			//	if(end != -1)
			//		strArray= str.slice(0,end);
			//	else
			//		strArray = str;
			//window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
			//window.close();	
			W.modifyQueryData();
			api.close();
		}
	}
	function changSelect(o)
	{
	   document.all.inputdocsource.value=document.all.docsource_id.options(document.all.docsource_id.selectedIndex).text;
	   document.all.inputdocsource.focus();
	   document.all.inputdocsource.select();
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
	//有关文档来源查询
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
if(initsearch=='true'){
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
initdiv();
}
}
function initdiv()
{
//初试化div
var t=document.getElementById("inputdocsource").value;
if(t.length<1){
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
  changdiv();
}
}
function divHide(d) { 
var d=document.getElementById("id1");
if(d.style.display=='block')
 d.style.display="none";
} 
function changdiv()
{
initsearch='false';
if(document.all.ischecksource.value!='1'){
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
   var count_1=0;
   for(var i=0;i<myarray.length;i++)
   {
   if(myarray[i].match(patternstr)!=null)
   {
	 sr=sr+' <tr style="cursor:hand" onClick=\"selectvalue(\''+myarray[i]+'\',\''+myarrayid[i]+'\')\"><td bgcolor=#FFFFFF height=22>&nbsp;'+myarray[i]+'</td></tr>';
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
//-->
</script>
<script language="javascript">

</script>
<!--下面的代码 使得按 F5会刷新modal页面-->
<base target="_self">
<body  bottommargin="0"   bgcolor="#F3F4F9" onkeydown="if(event.keyCode==116){reload.click()}" rightmargin="0" leftmargin="0" topmargin="0" onLoad="init()">
<script language="javascript">
	document.write("<a id='reload' href='" + document.location.href + "' style='display:none'>reload...</a>");
</script>
<!--上面的代码 使得按 F5会刷新modal页面-->
	<form target="updategather" name="myform" id="myform" >
		<input name="userid" value="<%=document.getUser_id()%>" type=hidden>
		<input name="doctype" value="0" type=hidden>
	  	<input name="docid" value="<%=docid%>" type=hidden>
	  	<input name="siteid" value="<%=siteid%>" type=hidden>
	  	<input name="channelName" value="<%=channelname%>" type=hidden>
	  	<input name="channelId" value="<%=channelId%>" type=hidden>
	  	<input name="isnewdocsource" value="0" type="hidden"><!--是否新稿源-->
		<input name="filepaths" value="" type=hidden><!--文件的新增时间，作为文件名，拼成一个串-->
		<input name="otherfilepaths" value="" type=hidden><!--用于维护更新remote图片的状态-->
		<input name="ofilepaths" value="" type=hidden><!--原本的url-->
		<div id="auditorDiv" name="auditorDiv" style="display:none;"></div><!--文档返工送审存放审核人-->
		<input type="hidden" id="docsource_id" name="docsource_id" value="<%=docsource%>"><input type="hidden" id="ischecksource">
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	    	<tr>
	            <td height="50" colspan="3" valign="top" background="<%=request.getContextPath()%>/cms/images/top_tile_short.jpg">
		            <table width="100%" border="0" cellpadding="0" cellspacing="0">
		                <tr>
		                	<td width="200"><img src="<%=request.getContextPath()%>/cms/images/logo.jpg" width="200" height="50"></td>
		                    <td width="53"><img src="<%=request.getContextPath()%>/cms/images/Gather_top_LeftDot.jpg" width="174" height="50"></td>
		                </tr>
		            </table>
	            </td>
	        </tr>
						            
            <tr>
	            <td height="18" colspan="3">
		            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		            	<tr background="<%=request.getContextPath()%>/cms/images/Gather_Content_topbg.gif" >
		                	<td width="4%" height="35" align="right" background="<%=request.getContextPath()%>/cms/images/Gather_Content_topbg.gif">
		                  		<img src="<%=request.getContextPath()%>/cms/images/ico_point.gif" width="25" height="25">
		                  	</td>
		                  	<td colspan="3" background="<%=request.getContextPath()%>/cms/images/Gather_Content_topbg.gif" class="cms_title_blue">
			                  	<table width="100%" border="0" cellpadding="0" cellspacing="0">
				                    <tr>
				                        <td width="100" height="35" class="cms_title_blue">文档信息填写</td>
				                    	<td>
				                    	<DIV style="text-align:left">
				                    	(<span class="red_star">*</span>为必填项<img src="<%=request.getContextPath()%>/cms/images/tan.gif" width="11" height="11">)
				                    	</div>
				                    	</td>
				                	</tr>
		                      	</table>
	                      	</td>
	                  	</tr>
		                <tr>
		                	<td height="26">&nbsp;</td>
		                	<td height="23" colspan="3">
		                		<table border="0" cellpadding="3" cellspacing="0">
		                    		<tr>
		                      			<td><strong>文档类型:</strong> </td>
		                      			<td>
		                      				<img src="<%=request.getContextPath()%>/cms/images/doc_html.gif" width="20" height="20">								                      			</td>
								        <td>普通文档</td>
							        </tr>
							    </table>

		                    </td>
		                </tr>
		                <tr>
		                	<td height="26">&nbsp;</td>
		                  	<td width="6%" height="23">
		                  		<strong>文档标题:</strong>
		                  	</td>
		                    <td width="43%">
		                    	<span class="cms_title_blue">
		                    		<label>
		                    			<input name="title" type="text" class="cms_text" size="60" value="<%=CMSUtil.filterStr(document.getTitle())%>" onFocus="focusend()">
		                    		</label>
		                  		</span>
		                  		<span class="STYLE7">
		                  			<label><span class="red_star">*</span></label>
		                  		</span>
		                  	</td>
		                  	<td height="23">
			                  	<span><strong>标题颜色:</strong></span>
		                  		<span>
		                      		<input name="titlecolor"  value="<%=document.getTitlecolor()%>" onClick="openWin('<%=request.getContextPath()%>/cms/inc/js/selcolor.htm?',271, 311);"  readonly type="text" class="cms_text" size="8" style="color:<%=document.getTitlecolor()%>" maxlength="50">
	                  		  	</span>
	                  		</td>
		                </tr>
		                <tr>
		                	<td height="26">&nbsp;</td>
		                  	<td height="23">
	                  	    <strong>显示标题:</strong></td>
		                  	<td height="23">
		                  		<input name="subtitle" type="text" class="cms_text" size="60" value="<%=CMSUtil.filterStr(document.getSubtitle())%>" >
						  		<span class="STYLE7">
			                  		<label><span class="red_star">*</span></label>
		                  		</span>
		                  	</td>
		                  	<td height="23">
			                  	<label><strong>文档来源:</strong></label>
		                    	<span class="cms_title_blue">
		                    	<input type="text" style="width:120px;height:21px;font-size:10pt;" id="inputdocsource" name="inputdocsource" onmouseover="initdiv1()" onpropertychange="changdiv()" onmouseout="divhidden()" value="<%=orasrcnamestr%>">
		                    	<div id="id1" STYLE="position:absolute;display:none;z-index:100;background: #fff; width=100px;height=300px;overflow-y:scroll;" onmouseover="divShow()" onmouseout="divhidden()" ></div><span class="red_star">*</span>
							</span>
		                    </td>
		                </tr>
		                <tr>
		                	<td height="26">&nbsp;</td>
		                	<td height="23"><strong>副 标 题:</strong></td>
							<td height="23">
								<input name="secondtitle" type="text" class="cms_text" value="<%=document.getSecondtitle()%>" size="60">
							</td>
		                  	<td height="23">
		                  		<strong>编稿时间:</strong>
	                  			<span class="black">
	                  				<span class="cms_title_blue">
										<input name="docwtime" class="cms_text" type="text" size="23" validator="datetime"  onpropertychange="ordertime.value=this.value" cnname="编稿时间"  value="<%=riqi%>" >
										<input type="button" class="Channel_5wordsBtn" value="时间" onClick="showdatetime(document.all('docwtime'))">
									</span>
								</span>
		                    </td>
		                </tr>
		                <tr>
						    <td height="26">&nbsp;</td>
						    <td height="26">文档内部分类</td>
						    <td height="26"><span class="cms_title_blue">
							<select id="doc_class" name="doc_class"  >
							<%
							List<DocClass> docClasses = CMSUtil.getDocClassList(siteid);
							for(DocClass docClass:docClasses)
							{
							%>
								<option value="<%= docClass.getClass_name()%>" 
								<% if(document.getDoc_class()!= null && document.getDoc_class().equals(docClass.getClass_name())){
									out.print("selected");
								}%>><%= docClass.getClass_name()%></option>
							<%} %>
							</select><span class="red_star">*</span>
							</span></td>
						    <td height="23"> 
							    <strong>排序时间:</strong>
							    <span class="black">
									<span class="cms_title_blue">
									<%
									    String ordertime = "";
									    if(document.getOrdertime() != null) {
									        ordertime = formatter.format(document.getOrdertime());
									    }else if(document.getDocwtime() != null){
									        ordertime = formatter.format(document.getDocwtime());
									    }
									%>
										<input name="ordertime" type="text" class="cms_text" value="<%=ordertime%>" validator="datetime" cnname="排序时间" size="23">
									</span>
								</span>
								<input type="button" class="Channel_5wordsBtn" value="时间" onClick="showdatetime(document.all('ordertime'))">
							</td>  
						</tr>
						<tr>
		                	<td height="26">&nbsp;</td>
		                  	<td height="23"><strong>发布模板:</strong></td>
		                  	<td height="23">
								<table border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td>
											<table>
												<tr>
													<td>
													<span class="cms_title_blue">
														<input type="radio" name="selecttpl" value="0"
														onclick="changtpl(0)">
														<span class="cms_title_blue">
															<input name="detailtemplate_id" id="detailtemplate_id" type="hidden" value="<%=tid%>" />
															<input name="tName" id="tName" type="text" style="width:130px" value="<%=tName%>"  class="cms_text" disabled="disabled">
															<input name="btnTemplateList" id="btnTemplateList" type="button" class="Channel_5wordsBtn" onclick="templateChoice()" value="列表"  />
														</span>
												  </td>
												</tr>
												<tr>
													<td>
													<input type="radio" name="selecttpl" value="1" onClick="changtpl(1)">
													<input type="hidden" name="parentDetailTpl"/>
													<%
													ChannelManager chnl = new ChannelManagerImpl();
													Template tp = chnl.getDetailTemplateOfChannel(channelId);
													String tname = (tp==null?"还没有设置":tp.getName());
													%>
													继承所属频道的细览模板(<span class="red_star"><%=tname%></span>)
													</td>
												</tr>
											</table>
										</td>
										<td>
											<table>
												<tr>
													<td>
													<a href="#" onClick="preview()" ><u>预览</u></a>
													</td>
												</tr>
											</table>
										</td>
									</tr>
							  </table>
							</td>
							<td>
								<span class="black">
		                  		    <strong><%if(channelname.equals("领导讲话")||channelname.equals("领导活动")){out.print("领导名称:");}else{out.print("作　　者:");}%></strong><span class="cms_title_blue">
			                  			<input name="author" type="text" class="cms_text" size="17" value="<%=document.getAuthor()%>">
		                  		</span></span>
								&nbsp;&nbsp;<strong>级别:</strong>
								<span class="cms_title_blue">
									<select name="doc_level" class="cms_select">
										<%
										  List doclevellist = null;
										  DocumentManager impl = new DocumentManagerImpl();
										  doclevellist = impl.getDocLevelList();
										  request.setAttribute("doclevellist",doclevellist);
										%>
										<pg:list requestKey="doclevellist">
											<option value="<pg:cell colName="id"/>"<pg:equal colName="id" value="<%=docLevelId%>">selected</pg:equal>>
											<pg:cell colName="name"/> 
										</pg:list>
									</select>
								&nbsp; 
							  </span>
					  	  </td>
		                </tr>
						<tr>
							<td height="26">&nbsp;</td>
							<td height="23" >
								<strong>主题图片:</strong></td>
							<td height="23">
								<input type="text" name="picpath" value="<%=document.getPicPath()%>" class="cms_text"  size="30"/>
								<input type="button" name="addpic" class="Channel_7wordsBtn" onClick="chooseImage()" value="选择图片或flash"/>
							</td>
							<td height="23"> 
								<strong>发布名称:</strong>
							    <input type="text" name="publishfilename" value="<%=document.getPublishfilename()%>"  class="cms_text"  size="30"/>
							</td>
						</tr>
						<tr>
					    <td height="26">&nbsp;</td>
					    <td><strong>多媒体文件:</strong></td>
					    <td>
					    	<input type="text" name="mediapath" value="<%=document.getMediapath()%>" class="cms_text"  size="30"/>
					  		<input type="button" class="Channel_5wordsBtn" name="addmedia" onClick="choosemedia()" value="选择文件"/>
					    </td>
					    <td><strong>排 序 号:</strong>
						    <input type="text" name="seq" value="<%if(document.getSeq()==999999){out.print("");}else{out.print(document.getSeq());}%>" class="cms_text" size="10"/></td>
					</tr>
					    
					    <tr>
						    <td height="26">&nbsp;</td>
						    <td colspan="3">
						    	<strong>是否标记为new:</strong>&nbsp;&nbsp;
						    	<input type="checkbox" hideFocus name="isnew" value="1" <%if(document.getIsNew()==1)out.print("checked");%> class="cms_text"/>&nbsp;&nbsp;
						    	<input type="text" name="newpicpath" value="<%=document.getNewPicPath()%>" class="cms_text" readonly size="30"/>
						  		<input type="button" class="Channel_5wordsBtn" name="default" onClick="document.all('newpicpath').value='image/new.gif'" value="使用默认值"/>
						  		<input type="button" class="Channel_7wordsBtn" name="addnewpic" onClick="choosenewpic()" value="选择new图标"/>
						    </td>
						</tr>
		                <tr>
		                	<td height="26">&nbsp;</td>
		                  	<td height="23" colspan="3">
		                  		<table width="100%" border="0" cellpadding="3" cellspacing="0">
		                    		<tr>
				                    	<td width="7%"><strong>摘要内容:</strong></td>
				                      	<td>
			                      			<textarea name="docabstract" cols="70" rows="5" class="cms_textarea"><%=document.getDocabstract()%></textarea>
			                      		</td>
			                      	</tr>
		                  		</table>
		                  	</td>
		                </tr>
		                <tr>
		                	<td height="26">&nbsp;</td>
		                  	<td height="23" colspan="3">
		                  		<table width="100%" border="0" cellpadding="3" cellspacing="0">
		                    		<tr>
				                    	<td width="7%"><strong>关 键 词:</strong></td>
				                      	<td>
				                      		<textarea name="keywords" cols="70" rows="5" class="cms_textarea"><%=document.getKeywords()%></textarea>
				                      		<span class="red_star">*<span>(<span class="red_star">多个关键词之间用逗号“,”分隔</span>)
			                      		</td>
			                      	</tr>
		                  		</table>
		                  	</td>
		                </tr>
		                <tr>
		                	<td height="26">&nbsp;</td>
		                  	<td height="23" colspan="2">
								<img align=absMiddle border=0 height=12 src="<%=request.getContextPath()%>/cms/images/text-edit.gif" width=14 >
								<a id=tab1 href="#" onclick=tabClick(1) ><u>[内容编辑]</u></a>&nbsp;&nbsp; 
								<img align=absMiddle border=0 height=12 src="<%=request.getContextPath()%>/cms/images/text-correlated.gif" width=14 >
								<a id=tab2 href="#" onclick=tabClick(2) ><u>[相关文档]</u></a>&nbsp;&nbsp;
								<img align=absMiddle border=0 height=12 src="<%=request.getContextPath()%>/cms/images/text-addons.gif" width=14 >
								<a id=tab3 href="#" onclick=tabClick(3) ><u>[文档附件]</u></a>&nbsp;&nbsp;
								<img align=absMiddle border=0 height=12 src="<%=request.getContextPath()%>/cms/images/text-img.gif" width=14 >
								<a id=tab4 href="#" onclick=tabClick(4) ><u>[文档图片]</u></a>&nbsp;&nbsp;
								<img align=absMiddle border=0 height=15 src="<%=request.getContextPath()%>/cms/images/text-text.gif" width=15 >
								<a id=tab5 href="#" onclick=tabClick(5) ><u>[扩展字段]</u></a>
								<img align=absMiddle border=0 height=15 src="<%=request.getContextPath()%>/cms/images/text-text.gif" width=15 >
								<a id=tab6 href="#" onclick=tabClick(6) ><u>[专题报道]</u></a>
		                  	</td>
		                    <td width="30%" height="23">
				<div align="center">
				<%if(flag!=null && flag.equals("rebound")){%>
					<input name="button" type="button" onClick="seeAdvice();" class="cms_button"  value="查看意见">
					<input name="button" type="button" onClick="delive();" class="cms_button"  value="送审">
				<%}else if(flag!=null && flag.equals("audit")){%>
					<input name="button" type="button" onClick="agree();" class="cms_button"  value="通过">
					<input name="button" type="button" onClick="disagree();" class="cms_button"  value="退回">
				<%}else{%>
					<input name="button" type="button" onClick="saveform(1);" class="cms_button"  value="保存">
					<input type="button" value="发布预览" class="cms_button" onClick="saveform(5);">
					<%
					}
					%>
				
					<input type="button" value="返回" class="cms_button" onClick="back()">
				</div>
			</td>
		                </tr>
            	  </table>
            	</td>
            </tr>
            <tr>
				<td  width="95%" height="22" colspan="3"  align="center" valign="top">
					<div>
						<input type="hidden" name="content" value="<%=CMSUtil.filterStr(document.getContent())%>">
						<input type="hidden" name="pics">
						<input type="hidden" name="flashs">
						<input type="hidden" name="medias">
						<input type="hidden" name="files"><br/>
						 <iframe id="eWebEditor1" src="<%=request.getContextPath()%>/cms/editor/eWebEditor48/ewebeditor.htm?id=content&style=coolblue&cusdir=<%=docpath%>" frameborder="0" scrolling="no" width="95%" height="460">
						</iframe>
					</div>
					<div>
						<input type="hidden" name="doclist">
						<br/><iframe id="docxz" src="<%=request.getContextPath()%>/cms/channelManage/chn_doc_xzlist.jsp?flag=1&siteid=<%=siteid%>" frameborder="0" scrolling="auto" width="0%" height="0%">
						</iframe>
					</div>
					<div>
						<input type="hidden" name="originalFilename">
						<input type="hidden" name="description">
						<input type="hidden" name="url">
						<iframe id="docattachlist" src="<%=request.getContextPath()%>/cms/docManage/doc_attach_list.jsp?type=2&docpath=<%=docpath%>" frameborder="0" scrolling="auto" width="0%" height="0%">
						</iframe>
					</div>
					<div>
						<input type="hidden" name="originalFilename2">
						<input type="hidden" name="description2">
						<input type="hidden" name="url2">
						<iframe id="docattachlist2" src="<%=request.getContextPath()%>/cms/docManage/doc_attach_list.jsp?type=3&docpath=<%=docpath%>" frameborder="0" scrolling="auto" width="0%" height="0%">
						</iframe>
					</div>
					<div>
						<input type="hidden" name="fieldids">
						<input type="hidden" name="extfieldvalues">
						<input type="hidden" name="extfieldtypes">
						<input type="hidden" name="extfieldnames">
						<iframe id="docextfielslist" src="<%=request.getContextPath()%>/cms/docManage/doc_extfield_list.jsp?cusdir=<%=docpath%>&sitedir=<%=sitedir %>&relativePath=<%=relativePath %>&type=2&id=<%=channelId%>&docid=<%=docid%>" frameborder="0" scrolling="auto" width="0%" height="0%">
						</iframe>
					</div>
					<!-- 所属专题报道-->
					<div>
						<INPUT type="hidden" name="specialsiteid"><!-- 专题站点id-->
						<INPUT type="hidden" name="sepcialchannelid"><!-- 专题频道id-->
						<iframe id="specialreportlist" src="<%=request.getContextPath()%>/cms/docManage/special_report_list.jsp?type=2&docpath=<%=docpath%>&channelid=<%=channelId%>&siteid=<%=siteid%>" frameborder="0" scrolling="auto" width="0%" height="0%">
						</iframe>
					</div>
				</td>
            </tr>						            					            	          
		  <tr>
			<td>&nbsp;</td>
		  </tr>
          <!--来源于别的网站才有父站点-->
          <tr>
              <td height="11" colspan="3" background="<%=request.getContextPath()%>/cms/images/bottom_gray.jpg">&nbsp;</td>
          </tr>
      	</table>
    	<script type="text/javascript">
  	  		//document.all.inputdocsource.value=document.all.docsource_id.options(document.all.docsource_id.selectedIndex).text;
  		</script> 
	</form>
	<div style="display:none">
		<iframe name="updategather" width="0" height="0"></iframe>
	</div> 
</body>
</html>