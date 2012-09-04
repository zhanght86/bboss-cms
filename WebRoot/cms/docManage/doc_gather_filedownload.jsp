<%@ page contentType="text/html; charset=UTF-8" language="java" 
import="java.util.List,java.util.Date,java.text.SimpleDateFormat,java.util.StringTokenizer"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.templatemanager.*,com.frameworkset.platform.cms.container.Template"%>
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
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
	java.util.Date currentTime = new java.util.Date(); 
	String riqi = formatter.format(currentTime);
	
	
	String channelname = request.getParameter("channelName");
	String siteid = request.getParameter("siteid");
	String channelId = request.getParameter("channelId");
	
	SiteManager siteManager = new SiteManagerImpl();
	String sitename = siteManager.getSiteName(siteid);
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
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

-->
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CMS 文档管理</title>
</head>
<script language="javascript">
	//站点窗口输入参数校验
	function saveform(){
		if(myform.title.value==""){
			alert("请输入文档标题!");
			myform.title.focus();
			return false;
		}
		if(myform.subtitle.value==""){
			alert("请输入显示标题!");
			myform.subtitle.focus();
			return false;
		}else{
			myform.method="post";
			myform.action="add_document.jsp";
			myform.submit();
		}
	}
	//返回
	function back() {
		window.close();
	}
	//焦点放在第一个text
	function init(){
		myform.title.focus();
	}
	function isnum()
	{
	  var k = window.event.keyCode;
	  if (k < 48 || k > 57){
	     alert("你输入的不是数字！")
	     window.event.keyCode = 0 ;}
	}	
</script>
<!--下面的代码 使得按 F5会刷新modal页面-->
<base target="_self">
<body  bottommargin="0"  bgcolor="#F3F4F9"  onkeydown="if(event.keyCode==116){reload.click()}" rightmargin="0" leftmargin="0" topmargin="0" onLoad="init()">
<a id="reload" href="doc_gather_filedownload.jsp" style="display:none">reload...</a>
<!--上面的代码 使得按 F5会刷新modal页面-->
<form target="addfiledownload"  action="add_document.jsp" method="post"  name="myform">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
            
            <!--来源于别的网站才有父站点-->
            
            <tr >
              <td valign="top" background="../images/top_tile_short.jpg">
			  <table width="100%" border="0" cellpadding="0" cellspacing="0">
					            			<tr>
					                  			<td width="200"><img src="../images/logo.jpg" width="200" height="50" /></td>
					                		    <td>内容采集 [<%=sitename%> 站点  :  <%=channelname%> 频道]</td>
					            			    <td width="53"><img src="../images/Gather_top_LeftDot.jpg" width="174" height="50" /></td>
					            			</tr>
           		</table>
			  </td>
            </tr>
            
            
            <tr>
              <td height="18"><table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="4%" height="35" align="right" background="../images/top_tile_short.jpg"><img src="../images/ico_point.gif" width="25" height="25"></td>
                  <td width="96%" colspan="2" background="../images/top_tile_short.jpg" class="cms_title_blue"><table width="100%" border="0" cellpadding="3">
                      <tr>
                        <td class="cms_title_blue">文档信息填写</td>
                        <td><div align="right"><span class="red_star">*</span> 为必填项<span class="red_star"><img src="../images/tan.gif" width="11" height="11"> </span></div></td>
                      </tr>
                    </table></td>
                </tr>
                <tr>
                  <td height="26">
                  <label>&nbsp;</label></td>
                  <td height="23" colspan="2"><table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td width="9%"><strong>文档类型:</strong></td>
                      <td width="3%"><div align="center"><img src="../images/doc_html.gif" width="20" height="20"></div></td>
                      <td width="9%"><a href="doc_gather.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>">普通文档</a></td>
                      <td width="3%"><div align="center"><img src="../images/doc_link.gif" width="20" height="20"></div></td>
                      <td width="10%"><a href="doc_gather_links.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>">外部链接</a></td>
                      <td width="4%"><div align="center"><img src="../images/doc_downloadfile.gif" width="15" height="16">&nbsp;</div></td>
                      <td width="32%"><a href="doc_gather_filedownload.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>">外部文件</a></td>
                      <td width="30%">&nbsp;</td>
                    </tr>
                  </table></td>
                </tr>
                <tr>
                  <td height="26">&nbsp;</td>
                  <td height="23" colspan="2"><strong>文档标题:</strong><span class="cms_title_blue">
	                    <label>
	                    <input name="title" type="text" class="cms_text" size="60">
	                    </label>
	                  </span><span class="STYLE7">
	                  <label><span class="red_star">*</span></label>
	                  </span><span class="cms_title_blue">
						<label></label>
	                  </span><span>&nbsp;&nbsp;<strong>标题颜色:</strong>
                      <input name="titlecolor"   onClick="window.open('../inc/js/selcolor.htm?','window','height=271, width=411, top=0, left=580, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no, status=no');"  readonly type="text" class="cms_text" value="#000000" size="7" maxlength="50">
						</span>					</td>
                </tr>
                <tr>
                  <td height="26">&nbsp;</td>
                  <td height="23" colspan="2"><strong>显示标题:</strong>
                    <input name="subtitle" type="text" class="cms_text" size="60">
						<label><span class="red_star">*</span></label>
						<label>&nbsp;&nbsp; <strong>编稿时间:</strong> <span class="black"><span class="cms_title_blue">
						<input name="docwtime" type="text" class="cms_text" value="<%=riqi%>" size="20">
				  </span><span class="STYLE7"><span class="red_star">*</span></span></span></label>				 </td>
                </tr>
                
                <tr>
                  <td height="26">&nbsp;</td>
                  <td height="23" colspan="2"><table width="100%" border="0" cellspacing="0">
                    <tr>
                      <td height="23"><strong>发布模板:</strong><span class="cms_title_blue">
                       <select name="detailtemplate_id" class="cms_select">
                      
                        <%
                          List list=null;
					      TemplateManagerImpl tmi=new TemplateManagerImpl();
					      Template template=new Template();
						  list=tmi.getTemplateInfoList(2);
						  request.setAttribute("tpllist",list);
						%>
						  <pg:list requestKey="tpllist">
						  	  
							  <option value="<pg:cell colName="templateid"/>"><pg:cell colName="name"/></option>
						  </pg:list>
                    </select>
					&nbsp; </span>预览 | 编辑					</td>
                   </tr>
                  </table></td>
                </tr>
                <tr>
                  <td height="26">&nbsp;</td>
                  <td height="23" colspan="2"><strong>下载文件:</strong>
                    <input name="docurl" type="file"  class="cms_file" size="55">
                    <label>&nbsp;<span class="red_star">*</span>&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;</label>                    <div align="right">&nbsp;&nbsp; 
                    </div></td>
                  </tr>
              </table></td>
              </tr>
          
          
            <tr>
              <td height="22"  align="center" valign="top">
              	 <INPUT name="button" type="button" class="cms_button"  value="保存" onClick="saveform();">&nbsp;
            <input type="button" value="返回" class="cms_button"
								onclick="back()">            </tr>
            
            
            <!--来源于别的网站才有父站点-->
          </table>   
</form>
<div style="display:none">
	<iframe name="addfiledownload" width="0" height="0"></iframe>
</div> 
</body>
</html>