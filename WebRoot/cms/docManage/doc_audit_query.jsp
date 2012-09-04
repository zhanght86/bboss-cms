<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@page import="com.frameworkset.platform.cms.flowmanager.*" %>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*,com.frameworkset.platform.cms.documentmanager.*,com.frameworkset.platform.cms.channelmanager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String userid = accesscontroler.getUserID();
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>内容管理主框架</title>
		<script src="../inc/js/func.js"></script>
		<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="../../sysmanager/scripts/selectTime.js" type="text/javascript"></script>
		<script type="text/javascript" src="../../public/datetime/calender.js" language="javascript"></script>
		<script type="text/javascript" src="../../public/datetime/calender_date.js" language="javascript"></script>
		<script language="javascript">
			function subQuery(){
				form1.action = "doc_audit_list.jsp?flag=query";
				form1.target = "auditDocListF";
				form1.submit();
			}
			function dispalyAll(){
				form1.action = "doc_audit_list.jsp?flag=all";
				form1.target = "auditDocListF";
				form1.submit();
				form1.reset();
			}
			function selChnl(){
				var reVlaue = openWin("multi_channel_select_frame.jsp?taskType=audit&siteid=",400,500);	
				if(reVlaue != undefined){
					document.all.channelNames.value = reVlaue.split(":")[0];
					document.all.channelIds.value = reVlaue.split(":")[1];
				}
			}
		</script>
		<style type="text/css">
		body {
			background-color: #ffffff;
		}
		</style>
	</head>
	<body bgcolor="#FFFFFF" leftmargin="1" topmargin="0" rightmargin="1" scroll=no>
	  <form id="form1" name="form1" method="post" action="" >
	    <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="5" valign="top" bgcolor="#ffffff"><img src="../images/querybox_left.gif" width="5" height="62"></td>
            <td  style="background:url(../images/querybox_bg.gif) repeat-x top"><table width="100%" border="0" align="center" cellpadding="3" cellspacing="0"  class="query_table">
              <tr>
                <td height="60%" colspan="4">            
              <td width="95">              </tr>
              <tr>
				<td height="23" colspan="7">&nbsp; 当前位置:个人工作台 &nbsp;>> &nbsp;待审文档</td>
			  </tr>
              <tr>
                <td width="10%" height="30" align="right">文档标题：</td>
                <td width="14%" align="left"><input name="docTitle" type="text" size="16"  class="cms_text"></td>
                <td width="9%" align="right">送审人：</td>
                <td width="15%" align="left"><input name="submitter" type="text" size="16"  class="cms_text"></td>
                <td align="9%">送审时间：</td>
                <td width="20%" align="left"><input name="subTime" type="text" size="16"  class="cms_text">
                <input name="button" type="button" onClick="showdate(document.all('subTime'))" value="..."/>                </td>
              	<td width="23%" rowspan="2" valign="bottom"><input name="subButton1" type="button" class="cms_button" value="查询" onClick="subQuery()">
                  &nbsp;
                <input name="subButton2" type="button" class="cms_button" value="查询所有" onClick="dispalyAll()"></td>
              </tr>
              <tr>
                 <td align="right">所属频道：</td>
                 <td align="left" colspan=5>
                 	<input name="channelNames" type="text" size="60"  class="cms_text" disabled="disabled">
                    <input name="channelIds" type="hidden" size="16"  class="cms_text" >
                <input name="chnlSel" type="button" onClick="selChnl()" value="...">                </td>
           	  </tr>
            </table></td>
            <td width="6" valign="top" bgcolor="#ffffff"><img src="../images/querybox_right.gif" width="6" height="62"></td>
          </tr>
        </table>
	  </form>
</body>
</html>