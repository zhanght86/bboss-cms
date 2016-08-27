<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.common.poolman.DBUtil"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.templatemanager.*
,com.frameworkset.platform.cms.container.Template,com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl,java.util.List"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	String docid = request.getParameter("docid");
	String channelname = request.getParameter("channelName");
	String siteid = request.getParameter("siteid");
	String channelId = request.getParameter("channelId");
	
	DocumentManager dmi = new DocumentManagerImpl();
	Document document = dmi.getDoc(docid);
	String status = String.valueOf(document.getStatus());
	SiteManager siteManager = new SiteManagerImpl();
	String sitename = siteManager.getSiteName(siteid);
	String tid = String.valueOf(document.getDetailtemplate_id());
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
	if(!confirm('您确定要修改文档吗？')){
	return;
	}
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
			myform.action="update_document.jsp";
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
	
</script>
<!--下面的代码 使得按 F5会刷新modal页面-->
<base target="_self">
<body  bottommargin="0"  background="../images/grayline_bg.jpg" onkeydown="if(event.keyCode==116){reload.click()}" rightmargin="0" leftmargin="0" topmargin="0" onload="init();">
<a id="reload" href="doc_gather_filedownload.jsp" style="display:none">reload...</a>
<!--上面的代码 使得按 F5会刷新modal页面-->
<form target="updatefiledownload" action="update_document.jsp" method="post"  name="myform">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<input type="hidden" name="content">
    <input name="doctype" value="2" type=hidden>
    <input name="docid" value="<%=docid%>" type=hidden>
   	<input name="siteid" value="<%=siteid%>" type=hidden>
  	<input name="channelName" value="<%=channelname%>" type=hidden>
  	<input name="channelId" value="<%=channelId%>" type=hidden>
	<!--来源于别的网站才有父站点-->
    <tr >
      <td height="22" style="padding-top:3px"><table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
          <td height="2" bgcolor="D7D7D7"></td>
        </tr>
        <tr>
          <td bgcolor="D7D7D7"><table width="99%" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
            
            <!--来源于别的网站才有父站点-->
            
            <tr >
              <td height="47" colspan="3" valign="top" background="../images/toolbar_common_func_template.jpg"><table width="96%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="28">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 内容编辑 [<%=sitename%> 站点  :  <%=channelname%> 频道]</td>
                </tr>
                <tr>
                  <td valign="middle" style="padding-bottom:0px">&nbsp;</td>
                </tr>
              </table></td>
              </tr>
            
            
            <tr>
              <td height="18" colspan="3"><table width="98%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="4%" height="27"><img src="../images/ico_point.gif" width="25" height="25"></td>
                  <td colspan="2" class="cms_title_blue"><table width="100%" border="0">
                      <tr>
                        <td class="cms_title_blue">文档信息填写</td>
                        <td><div align="right"><span class="red_star">*</span> 为必填项<span class="red_star"><img src="../images/tan.gif" width="11" height="11"> </span></div></td>
                      </tr>
                    </table></td>
                </tr>
                <tr>
                  <td height="23">&nbsp;</td>
                  <td height="23" colspan="2"><table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td width="9%">文档类型:</td>
                      <td width="4%"><div align="center"><img src="../images/doc_downloadfile.gif" width="15" height="16">&nbsp;</div></td>
                      <td width="32%">外部文件</td>
                      <td width="30%">&nbsp;</td>
                    </tr>
                  </table>
                    <label>&nbsp;</label></td>
                </tr>
                <tr>
                  <td height="23">&nbsp;</td>
                  <td height="23" colspan="2">文档标题:<span class="cms_title_blue">
                    <label>
                    <input name="title" type="text" class="cms_text" size="60" value="<%=document.getTitle()%>">
                    </label>
                  </span><span class="STYLE7">
                  <label><span class="red_star">*</span></label>
                  </span><span class="cms_title_blue">
				  <label></label>
                  </span><span>&nbsp;&nbsp; 标题颜色</span><span >:
                  <input name="titlecolor"  value="<%=document.getTitlecolor()%>"  onClick="window.open('../inc/js/selcolor.htm?','window','height=271, width=411, top=0, left=580, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no, status=no');"  readonly type="text" class="cms_text" style="color:<%=document.getTitlecolor()%>" size="7" maxlength="50">
				  </span><span class="black"><!--span class="red_star">*</span>&nbsp;权重:
				  <input name="documentprior" type="text" class="cms_text" value="100" size="3" value="<%=document.getDocumentprior()%>">
				  </span--></td>
                </tr>
                <tr>
                  <td height="23">&nbsp;</td>
                  <td height="23" colspan="2">显示标题:
                    <input name="subtitle" type="text" class="cms_text" size="60" value="<%=document.getSubtitle()%>">
					<label><span class="red_star">*</span></label>
					<label>&nbsp;&nbsp; 编稿时间: <span class="black"><span class="cms_title_blue">
					<input name="docwtime" type="text" class="cms_text" value="2006/12/11 12:55:05" size="20" value="<%=document.getCreateTime()%>">
					</span><span class="STYLE7"><span class="red_star">*</span></span></span></label></td>
                </tr>
                
                <tr>
                  <td height="23">&nbsp;</td>
                  <td height="23" colspan="2"><table width="100%" border="0" cellspacing="0">
                    <tr>
                      <td>发布模板:<span class="cms_title_blue">
                    <select name="detailtemplate_id" class="cms_select">
                        <%
                          List list=null;
					      TemplateManagerImpl tmi=new TemplateManagerImpl();
					      Template template=new Template();
						  list=tmi.getTemplateInfoList(2);
						  request.setAttribute("tpllist",list);
						%>
						  <pg:list requestKey="tpllist">
						  	  
							  <option value="<pg:cell colName="templateid"/>" <pg:equal colName="templateid" value="<%=tid%>">selected</pg:equal>>
							  <pg:cell colName="name"/>
							  </option>
						  </pg:list>
                    </select>
					&nbsp; </span>预览 | 编辑 </td>
                      </tr>
                  </table></td>
                </tr>
                
                <tr>
                  <td height="23">&nbsp;</td>
                  <td width="73%" height="23">下载文件:
                    <label>
                    <input name="docurl" type="text"  class="cms_file" size="50" value="<%=document.getLinkfile()%>">
<!--                    <input name="docurl" type="file"  class="cms_file" size="20">-->
                    </label>
                    <label>&nbsp;<span class="red_star">*</span>&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;</label></td>
                  <td width="23%" height="23"><div align="right">&nbsp;&nbsp; 
                  </div></td>
                </tr>
                <tr>
	               <td height="23">&nbsp;</td>
	               <%if ((accesscontroler.getUserName()!=null)&& accesscontroler.getUserName().equals("系统管理员")){%>
					
					 <!--td  height="23">文档状态:
						<select name="status" class="cms_select">
	                      <option value="1" <%if(status.equals("1")){out.print("selected");}%>>新稿</option>
	                      <option value="2" <%if(status.equals("2")){out.print("selected");}%>>已审</option>
	                      <option value="3" <%if(status.equals("3")){out.print("selected");}%>>返工</option>
	                      <option value="4" <%if(status.equals("4")){out.print("selected");}%>>已发</option>
	                      <option value="5" <%if(status.equals("5")){out.print("selected");}%>>已回收</option>
	                      <option value="6" <%if(status.equals("6")){out.print("selected");}%>>已归档</option>
	                    </select>
	                </td-->
			    	<%}%>
            </tr>
              </table></td>
              </tr>
            
        	
            
            <tr>
              <td  width="766" height="22" colspan="3"  align="center" valign="top">
              	 <INPUT name="button" type="button" class="cms_button"  value="修改" onclick="saveform();">
                  <input type="button" value="返回" class="cms_button"
								onclick="back()">
                </tr>
            
            <tr>
              <td height="22" colspan="3"><table width="98%" border="0">
                
                <tr>
                  <td width="11%">&nbsp;</td>
                  <td width="89%">&nbsp;</td>
                </tr>
              </table></td>
              </tr>
            
            
            <!--来源于别的网站才有父站点-->
            
            <tr>
              <td height="11" colspan="3" background="../images/bottom_gray.jpg">&nbsp;</td>
              </tr>
          </table></td>
        </tr>
        <tr>
          <td height="3" bgcolor="D7D7D7"></td>
        </tr>
      </table></td>
    </tr>
    
    
  <!--来源于别的网站才有父站点-->
</table>   
</form>
<div style="display:none">
	<iframe name="updatefiledownload" width="0" height="0"></iframe>
</div>
</body>
</html>