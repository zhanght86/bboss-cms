<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
String beginTime = request.getParameter("beginTime");
			String endTime = request.getParameter("endTime");
%>
<html>
	<head>
		<title></title>

		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript">

</script>
	</head>
	<body class="contentbodymargin" scrolling="no">
		<div id="contentborder" align="center">
			<TABLE width="100%" cellpadding="1" cellspacing="1" bordercolor="#EEEEEE" class="thin">
				<!--分页显示开始,分页标签初始化-->
				<TR>
					<TD height="100" class="detailtitle" align="center" colspan="8">
						<B>冲突日程列表</B>
					</TD>
				</TR>
				<pg:listdata dataInfo="ImpactSchedularList" keyName="ImpactSchedularList" />
				
				<pg:pager maxPageItems="4" scope="request" data="ImpactSchedularList" isList="false">
				<pg:param name="beginTime"/>
				<pg:param name="endTime"/>
					<Td class="headercolor">
						主题
					</Td>
					<Td class="headercolor">
						重要性
					</Td>
					<Td class="headercolor">
						日程开始时间
					</Td>
					<Td class="headercolor">
						日程结束时间
					</td>
					<td class="headercolor">
						是否公事
					</td>
					<td class="headercolor">
						地点
					</td>
					<td class="headercolor">
						日程类型
					</td>
					<pg:notify>
						
					</pg:notify>
					<pg:list>
						<tr class="labeltable_middle_tr_01" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'">
							<td class="tablecells" nowrap="nowrap">
								<pg:equal colName="topic" value="">无主题</pg:equal>
								 <pg:cell colName="topic" defaultValue="无主题" />
							</td>
							<td class="tablecells" nowrap="nowrap">
								<pg:equal colName="essentiality" value="0">重要</pg:equal>
								 <pg:equal colName="essentiality" value="1">一般</pg:equal>
								 <pg:equal colName="essentiality" value="2">不重要</pg:equal>
							</td>
							<td class="tablecells" nowrap="nowrap">

								<pg:equal colName="beginTime" value="">未安排</pg:equal>
								 <pg:cell colName="beginTime" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="未安排" />
							</td>
							<td class="tablecells" nowrap="nowrap">

								<pg:equal colName="endTime" value="">未安排</pg:equal>
								 <pg:cell colName="endTime" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="未安排" />
							</td>
							<td class="tablecells" nowrap="nowrap">

								<pg:equal colName="isPublicAffair" value="0">公事</pg:equal>
								 <pg:equal colName="isPublicAffair" value="1">私事</pg:equal>
								
							</td>
							<td class="tablecells" nowrap="nowrap">

								<pg:equal colName="place" value="">无地点</pg:equal>
								 <pg:cell colName="place" defaultValue="无地点" />

							</td>
							<td class="tablecells" nowrap="nowrap">
								<pg:equal colName="type" value="">类型不确定</pg:equal>
								 <pg:cell colName="type" defaultValue="无" />
							</td>
						</tr>
					</pg:list>
					<tr height="18px" class="labeltable_middle_tr_01">
						<td colspan=7 align='center' nowrap="nowrap">
							<pg:index />
						</td>
					</tr>
					<input name="queryString" value="<pg:querystring/>" type="hidden">
				<TR></TR><TR height="18px" class="labeltable_middle_tr_01">
							<TD colspan="100" align="center">
								该时间段内没有冲突日程
							</TD>
						</TR></pg:pager>
			</TABLE>
		</div>
	</body>

</html>

