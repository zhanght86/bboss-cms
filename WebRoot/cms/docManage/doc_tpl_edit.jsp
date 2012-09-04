<%@ page contentType="text/html; charset=UTF-8" language="java" 
import="java.util.Date,java.text.SimpleDateFormat,java.util.StringTokenizer"%>
<%@page import="com.frameworkset.common.poolman.DBUtil"%>
<%@page import="com.frameworkset.platform.security.AccessControl,java.util.List"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.templatemanager.*
,com.frameworkset.platform.cms.container.Template,com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.bean.*"%>
<jsp:useBean id="date" scope="page" class="net.fiyu.edit.TimeStamp"/>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>

<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    
    com.frameworkset.platform.cms.CMSManager cmsManager  = new com.frameworkset.platform.cms.CMSManager();
	cmsManager.init(request,session,response,accesscontroler);
	
    String docTplId = request.getParameter("docTplId");
    String siteid = cmsManager.getSiteID();
    DocTemplate docTPL = null;
    if(docTplId!=null){
	    DocumentManager dmi = new DocumentManagerImpl();
	    docTPL = dmi.getDocTPL(docTplId); 
    } 
%>
<%!/**
   * 针对HTML的特殊字符转义
   * @param value String
   * @return String
   */
  String filterStr(String value) {
    if (value == null) {
      return (null);
    }
    char content[] = new char[value.length()];
    value.getChars(0, value.length(), content, 0);
    StringBuffer result = new StringBuffer(content.length + 50);
    for (int i = 0; i < content.length; i++) {
      switch (content[i]) {
        case '<':
          result.append("&lt;");
          break;
        case '>':
          result.append("&gt;");
          break;
        case '&':
          result.append("&amp;");
          break;
        case '"':
          result.append("&quot;");
          break;
        case '\'':
          result.append("&#39;");
          break;
        case '\\':
          result.append("\\\\");
          break;
        default:
          result.append(content[i]);
      }
    }
	return (result.toString());
  }
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<link href="<%=request.getContextPath()%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/cms/inc/js/func.js"></script>
<style type="text/css">
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>文档采集模板编辑：</title>
</head>
<script language="javascript">
	//字符转换
	function HTMLEncode(text){
		//text = text.replace(/"/g, "&quot;");
		return text;
	}
	//静态分页置标
	/**将图片转换成分页置标**/
	function setPageTagNone(){
		var arPageTags = eWebEditor1.eWebEditor.document.all("_CMSPage");
		if(arPageTags == null)return;
		if(arPageTags.length){
			for(var i =arPageTags.length-1; i>=0; i--){
				var objPageTag = arPageTags[i];
				if(objPageTag == null)continue;

				var pageTag = eWebEditor1.eWebEditor.document.createElement("CMS_PAGE_SEPARATOR");		
				//pageTag.innerHTML = "TRS静态分页置标";
				objPageTag.replaceNode(pageTag);
			}
		}else{
			var pageTag = eWebEditor1.eWebEditor.document.createElement("CMS_PAGE_SEPARATOR");		
			//pageTag.innerHTML = "TRS静态分页置标";
			arPageTags.replaceNode(pageTag);
		}

	}
	//将编辑器中的代码保存为文档模板
	function saveTPL(){
		if(document.all.docTplName.value == ""){
			alert("请输入文档模板名！");
			return;
		}
		if(document.all.channelIds.value == ""){
			alert("请选择所属频道！");
			return;
		}
		var tempcontent = eWebEditor1.eWebEditor.document.body.innerHTML;
		setPageTagNone();
		myform.content.value = HTMLEncode(eWebEditor1.eWebEditor.document.body.innerHTML);
		eWebEditor1.eWebEditor.document.body.innerHTML = tempcontent;
		if(myform.content.value==""){
			alert("请输入文档模板内容!");
			return false;
		}	
		myform.action = "doc_tpl_handle.jsp?action=add";
		myform.target = "docTplFrame"; 
		myform.submit();
	}
	function selChnl(){
		var reVlaue = openWin("multi_channel_select_frame.jsp?type=single&siteid=<%=siteid%>",400,500);	
		if(reVlaue != undefined){
			document.all.channelNames.value = reVlaue.split(":")[0];
			document.all.channelIds.value = reVlaue.split(":")[1];
		}
	}
	function updateTPL(){
		if(document.all.docTplName.value == ""){
			alert("请输入文档模板名！");
			return;
		}
		if(document.all.channelIds.value == ""){
			alert("请选择所属频道！");
			return;
		}
		var tempcontent = eWebEditor1.eWebEditor.document.body.innerHTML;
		setPageTagNone();
		myform.content.value = HTMLEncode(eWebEditor1.eWebEditor.document.body.innerHTML);
		eWebEditor1.eWebEditor.document.body.innerHTML = tempcontent;
		if(myform.content.value==""){
			alert("请输入文档模板内容!");
			return false;
		}	
		myform.action = "doc_tpl_handle.jsp?action=update";
		myform.target = "docTplFrame"; 
		myform.submit();
	}
</script>
<body background="../images/grayline_bg.jpg">
<form target="addgather" name="myform" method="post">
	<input type="hidden" name="docTplId" value=<%=docTplId==null?"":docTplId%>>		
	<table width="100%" height="100%">
		<tr>
			<td width="10%" align="right">文档模板名称：</td>
			<td width="70%" align="left">
				<input name="docTplName" type="text" class="cms_text" value=<%=docTplId==null?"":filterStr(docTPL.getTplName())%>>
				<span style="color: #FF0000">*</span>
			</td>
		</tr>
		<tr>
            <td align="right">所属频道：</td>
            <td align="left">
                <input name="channelNames" type="text" size="60"  class="cms_text" disabled="disabled" value=<%=docTplId==null?"":docTPL.getChnlName()%>>
                <input name="channelIds" type="hidden" size="16"  class="cms_text" value=<%=docTplId==null?"":docTPL.getChnlId()+""%>>
                 <input type="hidden" name="channelId" value=<%=docTplId==null?"":docTPL.getChnlId()+""%>><!-- eWebeditor中引入模板中找不到channelId-->	
               
                <input name="chnlSel" type="button" onClick="selChnl()" value="...">  
                <span style="color: #FF0000">*</span>              
            </td>				
		</tr>
		<tr>
			<td align="right">文档模板描述：</td>
			<td align="left"><textarea name="docTplDescription" rows="2" cols="100"><%=docTplId==null?"":docTPL.getDescription()%></textarea></td>
		</tr>
		<tr>
			<td colspan="2" align="right">
				<%if(docTplId==null){%>
				<input name="button" type="button" onClick="saveTPL()" class="cms_button"  value="保存">
				<%}else{%>
				<input name="button" type="button" onClick="updateTPL()" class="cms_button"  value="更新">
				<%}%>
				&nbsp;
				<input name="button" type="button" onClick="window.close()" class="cms_button"  value="返回">
			</td>
		</tr>
		<tr>
			<td  width="100%" height="100%"  align="left" valign="top" colspan="2">
				<div>
					<input type="hidden" name="content" value="<%=docTplId==null?"":filterStr(docTPL.getTplCode())%>">
					<iframe id="eWebEditor1" src="<%=request.getContextPath()%>/cms/editor/eWebeditor/eWebEditor.jsp?id=content&style=standard" frameborder="0" scrolling="no" width="100%" height="420"></iframe>
				</div>
			</td>
		</tr>
		<!--来源于别的网站才有父站点-->
  </table> 
</form>
<iframe name="docTplFrame" width="0" height="0"></iframe>
</body>
</html>