<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.util.*" %>
<%@page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@page import="com.frameworkset.platform.cms.channelmanager.*" %>
<%@page import="com.frameworkset.platform.cms.flowmanager.*" %>
<%@page import="com.frameworkset.common.poolman.DBUtil" %>
<%@page import="java.util.*" %>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String channelId = request.getParameter("channelId");
	if(channelId == null || channelId.trim().length()==0){
	%>
		<script language="javascript" type="text/javascript">
			alert("频道id为空,无法编辑频道.");
			self.location.href = "about:blank";
		</script>
	<%
		return;
	}
	ChannelManager cm = new ChannelManagerImpl();
	Channel channel = cm.getChannelInfo(channelId) ;

	String channelName = channel.getName();
	int workflowFromParent = channel.workflowIsFromParent();
	long parentId = channel.getParentChannelId();
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<script src="../inc/js/func.js"></script>
	<title>修改频道流程</title>
<style type="text/css">
	.topDIV {
		background-image: url(../images/toolbar_common_func_site.jpg);
		height: 82px;
		padding-top: 7px;
		padding-left: 55px;

	body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
</style>
<script language="javascript">

//站点窗口输入参数校验
function checkForm(){
	var siteForm = document.all.siteForm;
	document.all.siteForm.submit();
}

function changeWorkFlowType(type){
	if(0==type){
		document.all.siteForm.workFlow.disabled = false;
	}else{
		document.all.siteForm.workFlow.disabled = true;
	}
}

</script>
</head>
<body background="../images/grayline_bg.jpg">
<form id="siteForm" name="siteForm"  target="exeman" action="changeWorkflow_do.jsp" method="post">
	<input name="channelId" type="hidden" id="siteId" value="<%=channelId%>">
	<table width="98%" border="1" cellpadding="0" cellspacing="0" align="center" bordercolorlight="#FFFFFF" >
		<tr>
			<td colspan="2" class="topDIV"  align="left" valign="top">修改频道流程</td>
		</tr>
		<tr>
			<td width="100" height="24" align="right" nowrap>频道中文名称:</td>
			<td height="24">
				<%=(channelName==null?"&nbsp;":channelName)%>
			</td>
		</tr>
		<tr>
			<td height="24" rowspan="2" align="right" nowrap>工作流程:</td>
			<td height="24">
				<input type="radio" name="workFlowFrom" value="0" <%=0==workflowFromParent?"checked":""%> onclick="changeWorkFlowType(0)">
				<%
				String wfName = "";
				String wfId = "";
				if(channelId!=null && !"".equals(channelId)){
					List workflowInfo = cm.getFlowInfo(channelId);
					if(workflowInfo!=null && workflowInfo.size()>=2){
						if(workflowInfo.get(0)!=null && workflowInfo.get(1)!=null){
							wfId = workflowInfo.get(0).toString();
							wfName = workflowInfo.get(1).toString();
						}
					}
				}
				FlowManager fm = new FlowManagerImpl();
				List list = fm.getFlowList();
				%>
				<select id="workFlow" name="workFlow" class="cms_select" style="width:130px"
					<%=(workflowFromParent==0?"":"disabled")%>>
					<%= JSPFunc.getOption(list,"".equals(wfId)?"1":wfId) %>
				</select>
			</td>
		</tr>
		<tr>
			<%
			wfName = "";
			wfId = "";
			
			if(parentId>0){
				List workflowInfo = cm.getFlowInfo(""+parentId);
				if(workflowInfo!=null && workflowInfo.size()>=2){
					if(workflowInfo.get(0)!=null && workflowInfo.get(1)!=null){
						wfId = workflowInfo.get(0).toString();
						wfName = workflowInfo.get(1).toString();
					}
				}
			}else{
				SiteManager sm = new SiteManagerImpl();
				try {
					List workflowInfo = sm.getFlowInfo(""+channel.getSiteId());
					if(workflowInfo!=null && workflowInfo.size()>=2){
						if(workflowInfo.get(0)!=null && workflowInfo.get(1)!=null){
							wfId = workflowInfo.get(0).toString();
							wfName = workflowInfo.get(1).toString();
						}
					}					
				} catch (SiteManagerException e) {
					e.printStackTrace();
					throw new ChannelManagerException("该频道没有父频道,在尝试取该频道的站点的流程时发生异常.异常信息为:"+ e.getMessage());
				}
			}			
			%>
			<td height="24">
				<input type="radio" name="workFlowFrom" value="1" <%=0!=workflowFromParent?"checked":""%> onclick="changeWorkFlowType(1)">
				继承父站点或父频道的流程设置(<span style="color: #FF0000"><%=wfName%></span>)
			</td>
		</tr>
	</table>
	<br>
	<div style="text-align:center">
		<INPUT name="button" type="button" class="cms_button" onClick="checkForm()"  value="保存">
		&nbsp;&nbsp;&nbsp;
		<INPUT name="button" type="button" class="cms_button" onClick="window.close()"  value="关闭">
	</div>
</form>
<iframe name="exeman" width="0" height="0" style="display:none"></iframe>
</body>
</html>