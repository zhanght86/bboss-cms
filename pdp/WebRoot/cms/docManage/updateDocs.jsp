<%@page import="org.frameworkset.util.DataFormatUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@page import="com.frameworkset.common.poolman.PreparedDBUtil"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*,java.util.List"%>
<jsp:useBean id="date" scope="page" class="net.fiyu.edit.TimeStamp"/>
<%
	String docidStr = request.getParameter("docidStr");	
	String channelname = request.getParameter("channelName");
	//当前时间
	java.text.SimpleDateFormat formatter = DataFormatUtil.getSimpleDateFormat(request, "yyyy-MM-dd HH:mm:ss");//new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	java.util.Date currentTime = new java.util.Date(); 
	String riqi = formatter.format(currentTime); 
	//文档来源的加载js数组
   String idstr="";
   String srcnamestr="";
	List<DocumentSource> dslist=null;
	DocumentManager dmi=new DocumentManagerImpl();
	DocumentSource ds= null;
	dslist=dmi.getDocSourceList();
	 String docSourceName = "";
	   String docSourceId = "";
   if(dslist.size()>0)
   {
	   ds = dslist.get(0);
	   docSourceName = ds.getSrcname();
	   docSourceId = ds.getDocsource_id()+"";
	   
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
	//获取上一次文档的信息如：文档来源 
  
   
   try{
	   /**PreparedDBUtil db = new PreparedDBUtil();
	   String sql = "select a.DOCSOURCE_ID,a.SRCNAME from TD_CMS_DOCSOURCE a , TD_CMS_DOCUMENT b where a.DOCSOURCE_ID = b.DOCSOURCE_ID  order by b.Document_Id desc" ;
	   db.executeSelect(sql) ;
	   if(db.size()>0){
	   		docSourceId = db.getString(0,0);
	   		docSourceName = db.getString(0,1); 
	   }*/
	   
	}catch(Exception e){}
%>
<html>

<head>    
  <title>文档转发：：</title>
  <script language="JavaScript" src="changeView.js" type="text/javascript"></script>
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sysmanager/css/treeview.css">
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sysmanager/css/windows.css">
  <link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
  <script src="<%=request.getContextPath()%>/cms/inc/js/func.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/public/datetime/calender.js" language="javascript"></script>
  <script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
  <script type="text/JavaScript">
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
	//选择new图标文件
	function choosenewpic()
	{
		fileFlag = "newpic";
		openWin("<%=request.getContextPath()%>/cms/channelManage/chooseImageFrameset.jsp?fileFlag=pic",620,580);
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
	function saveForm()
	{
	   var str = "";
	   var str_1 = "";
	   var str_2 = "";
	   var str_3 = "";
	   var str_4 = "";
	   var str_5 = "";
	   var str_6 = "";
	   var str_7 = "";
	   var str_8 = "";
	   if(haveSelect('ID'))
	   {
	       var ch = document.getElementsByName('ID');   
		   for (var i=0;i<ch.length;i++) {
			if (ch[i].checked){

				if(ch[i].value==1)
				{
					str_1 = "true";
				}
				
				if(ch[i].value==2)
				{
					str_2 = "true";
				}
				if(ch[i].value==3)
				{
					str_3 = "true";
				}
				if(ch[i].value==4)
				{
					str_4 = "true";
				}
				if(ch[i].value==5)
				{
					str_5 = "true";
				}
				if(ch[i].value==6)
				{
					str_6 = "true";
				}
				if(ch[i].value==7)
				{
					str_7 = "true";
				}
				if(ch[i].value==8)
				{
					str_8 = "true";
				}
				
				if(str!="")
				{
				  str=str+","+ch[i].value;
				}
				else
				{
				  str=ch[i].value;
				}
				
			}
		  }
		}
		else
		{
			alert('请至少选择一项！');
			return false;
		}
		if(str_2=="true")
		{
			var inputsource=document.all.inputdocsource.value;
			if(inputsource==null||inputsource=="")
			{
			  alert('请选择或者填写文档来源');
			  document.all.inputdocsource.focus();
			  return false;
			}
			else
			{
				//判定是否新加的文档来源，是设置
			    if(document.all.docsource_id.value.length<1)
			    {
					document.all.isnewdocsource.value="1";
				}	
			}
		}
		if(str_8=="true")
		{
			if(document.all.keywords.value.length<1)
			{
				alert("请填写关键字！");
				return false;
			}
		}
		myform.action = "updateDocs_do.jsp?docidStr=<%=docidStr%>&flagStr="+str;
		myform.target = "update";
		myform.submit();
		
	} 
	function setImage(uri){
		if(fileFlag == "pic")
			document.all("picpath").value  = uri;
		if(fileFlag == "media")
			document.all("mediapath").value  = uri;
		if(fileFlag == "newpic")
			document.all("newpicpath").value  = uri;
	}    
	
	function checkAll(totalCheck,checkName){	//复选框全部选中
			   var selectAll = document.getElementsByName(totalCheck);
			   var o = document.getElementsByName(checkName);
			   if(selectAll[0].checked==true){
				   for (var i=0; i<o.length; i++){
			      	  if(!o[i].disabled){
			      	  	o[i].checked=true;
			      	  }
				   }
			   }else{
				   for (var i=0; i<o.length; i++){
			   	  	  o[i].checked=false;
			   	   }
			   }
			}
			//单个选中复选框
			function checkOne(totalCheck,checkName){
			   var selectAll = document.getElementsByName(totalCheck);
			   var o = document.getElementsByName(checkName);
				var cbs = true;
				for (var i=0;i<o.length;i++){
					if(!o[i].disabled){
						if (o[i].checked==false){
							cbs=false;
						}
					}
				}
				if(cbs){
					selectAll[0].checked=true;
				}else{
					selectAll[0].checked=false;
				}
			}
			//判断是否有选择
			function haveSelect(elName){
				var isSelect = false;
				var ch = document.getElementsByName(elName);
				for (var i=0;i<ch.length;i++) {
					if (ch[i].checked){
						isSelect=true;
						break;
					}
				}
				return isSelect;
			}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
//-->
</script> 
</head>
<body class="contentbodymargin" scrolling="no">
<form name="myform" method="post">
<input name="isnewdocsource" value="0" type="hidden"><!--是否新稿源-->
<input type="hidden" id="docsource_id" name="docsource_id" value="<%=docSourceId%>"><input type="hidden" id="ischecksource">
<div id="contentborder">
<table class="table" width="100%" border="0" cellpadding="0" cellspacing="1">
  <tr>
  	<td width="3%"><input   class="checkbox" type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')"></td>
  	<td style="color:red">如果要操作批量修改，则必须选中所要修改的项目</td>
  </tr>
  <tr  align="left">
  	<td width="3%">
  		<input   class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="1">
  	</td>
    <td height="23" width="*">
		<span><strong>标题颜色:</strong></span>
		<span>
			<input name="titlecolor" onClick="openWin('/cms/inc/js/selcolor.htm?',271,311);"  readonly="true" type="text" class="cms_text" value="#000000" size="8" maxlength="50">
		</span>												
	</td>				  
  </tr> 
  <tr>
  	<td width="3%">
  		<input   class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="2">
  	</td>
  	<td height="23">
		<label><strong>文档来源:</strong></label>
		<span class="cms_title_blue">
		<input type="text" style="width:120px;height:21px;font-size:10pt;" id="inputdocsource" name="inputdocsource" onmouseover="initdiv1()" onpropertychange="changdiv()" onmouseout="divhidden()" value="<%=docSourceName%>">
	    	<div id="id1" STYLE="position:absolute;display:none;z-index:100;background: #fff; overflow-y:scroll; width:100px; height:300px" onmouseover="divShow()" onmouseout="divhidden()" ></div>
		</span>
	</td>
  </tr> 
  <tr>
  	<td width="3%">
  		<input   class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="3">
  	</td>
  	<td height="23"> 
	    <strong>编稿时间:</strong>
	    <span class="black">
			<span class="cms_title_blue">
				<input name="docwtime" type="text" class="cms_text"  validator="datetime" cnname="编稿时间" value="<%=riqi%>" size="22">
			</span>
		</span>
		<input type="button" class="Channel_5wordsBtn" value="时间" onClick="showdatetime(document.all('docwtime'))">
	</td>		
  </tr>
  <tr>
  	<td width="3%">
  		<input   class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="4">
  	</td>
  	<td height="23"> 
	    <strong>排序时间:</strong>
	    <span class="black">
			<span class="cms_title_blue">
				<input name="ordertime" type="text" class="cms_text" validator="datetime" cnname="排序时间" value="<%=riqi%>" size="22">
			</span>
		</span>
		<input type="button" class="Channel_5wordsBtn" value="时间" onClick="showdatetime(document.all('ordertime'))">
	</td>
  </tr>
  <tr>
  	<td width="3%">
  		<input   class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="5">
  	</td>
  	<td>
		<span class="black">
		    <strong><%if(channelname.equals("领导讲话")||channelname.equals("领导活动")){out.print("领导名称:");}else{out.print("作　　者:");}%></strong>
		    <span class="cms_title_blue">
				<input name="author" type="text" class="cms_text" size="17">
			</span>
		</span>
	</td>
  </tr>
  
  <tr>
  	<td width="3%">
  		<input   class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="6">
  	</td>
  	<td><strong>排 序 号:</strong><span class="black">
			<span class="cms_title_blue">
				<input name="seq" type="text" class="cms_text"  value="" size="10">
			</span>
		</span></td>
  </tr>
  <tr>
  	<td width="3%">
  		<input   class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="7">
  	</td>
    <td>
    	<strong>是否标记为new:</strong>&nbsp;&nbsp;
    	<input type="checkbox" hideFocus name="isnew" value="1" class="cms_text"/>&nbsp;&nbsp;
    	<input type="text" name="newpicpath" value="" class="cms_text" readonly size="30"/>
  		<input type="button" class="Channel_5wordsBtn" name="default" onClick="document.all('newpicpath').value='image/new.gif'" value="使用默认值"/>
  		<input type="button" class="Channel_7wordsBtn" name="addnewpic" onClick="choosenewpic()" value="选择new图标"/>
    </td>
  </tr>
  <tr>
  	<td width="3%">
  		<input   class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="8">
  	</td>
  	<td height="23" colspan="3">
		<table width="100%" border="0" cellspacing="0">
        	<tr>
            	<td width="15%"><strong>关 键 词:</strong></td>
            	<td>
            	<textarea name="keywords" cols="70" rows="5" class="cms_textarea"></textarea>
            	<span class="red_star">*<span>(<span class="red_star">多个关键词之间用逗号“,”分隔</span>)
            	</td>
        	</tr>
        </table>
    </td>
  </tr>
  <tr><td height="10" colspan="2">&nbsp;</td></tr>
  <tr><td align="center" colspan="2"><input type="button" name="update" onclick="saveForm();" value="修改"/><input type="button" name="return" onclick="window.close();" value="返回"/></td></tr>
</table>
</div>
</form>
</body>
<iframe name="update" width="0" height="0"></iframe>
</html>