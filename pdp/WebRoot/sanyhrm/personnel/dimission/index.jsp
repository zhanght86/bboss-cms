<%@ page session="true" language="java"
	contentType="text/html; charset=utf-8"%>
<%@ include file="/sanymbp/common/jsp/taglibs.jsp"%>

  <cm:request setAttribute="pagePath" value="/sanyhrm/personnel/dimission/index"/>
 <cm:request setAttribute="h1_title" value="待办信息：调岗流程"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>三一集团人力资源管理系统:${h1_title}</title>
<%@ include file="/sanymbp/common/jsp/head.jsp"%>
</head>
<body>
	<div data-role="page" class="type-interior">
		<div data-role="header" data-position="fixed" class="h1_1">
			<%@ include file="/sanymbp/common/jsp/top.jsp"%>
			<ul class="navbar1">
				<li class="li1"><a data-ajax="false"
					href="${ctx}/sanyhrm/personnel/dimission/index.page?taskId=${param.taskId}"><span>申请内容</span></a></li>
				<li class="li2"><a data-ajax="false"
					href="${ctx}/sanyhrm/personnel/dimission/auditing.page?taskId=${param.taskId}"><span>流程信息</span></a></li>
			</ul>
		</div>
		<div></div>
		<div data-role="content" class="div3">
		<pg:beaninfo requestKey="dimissionTask">
			<dl>
				<dt>基本信息：</dt>
				<dd>
					<span class="span1">工号：</span><pg:cell colName="PERNR"/>
				</dd>
				<dd>
					<span class="span1">姓名：</span><pg:cell colName="PERNR_NA_"/>
				</dd>
				<dd>
					<span class="span1">手机号码：</span><pg:cell colName="USRID_3"/>
				</dd>
				<dd>
					<span class="span1">部门：</span><pg:cell colName="orgName"/>
				</dd>
				<dd>
					<span class="span1">行政级别：</span><pg:cell colName="POSTY_NA"/>
				</dd>
				<dd>
					<span class="span1">待遇级别：</span><pg:cell colName="POSNC_NA"/>
				</dd>

				<dd>
					<span class="span1">关键岗位类别：</span><pg:cell colName="POSSC"/>
				</dd>
				<dd>
					<span class="span1">岗位体系：</span><pg:cell colName="POST_NA"/>
				</dd>
				<dd>
					<span class="span1">域账号：</span><pg:cell colName="USRID_5"/>
				</dd>
				<dd>
					<span class="span1">流程提交日期：</span><pg:cell colName="CREATETIME_NA_"/>
				</dd>
				<dd>
					<span class="span1">当前处理人：</span><pg:cell colName="DISPOSE_NAME"/>
				</dd>
				<dd>
					<span class="span1">岗位：</span><pg:cell colName="GW"/>
				</dd>

				<dt>调岗信息</dt>
				<dd>
					<span class="span1">流程类型：</span><pg:cell colName="WORKFLOW_NA_"/>
				</dd>
				<dd>
					<span class="span1">入司时间：</span><pg:cell colName="DAT01_NA_"/>
				</dd>
				<dd>
					<span class="span1">合同期限：</span>
					
						<pg:upper colName="WORK_YEAR_" value="100">无期限</pg:upper>
						<pg:equalandlower colName="WORK_YEAR_" value="100"><pg:cell colName="WORK_YEAR_"/>年</pg:equalandlower>
					
				</dd>
				<dd>
					<span class="span1">合同到期时间：</span><pg:cell colName="WORK_ENDDA"/>
				</dd>
				<dd>
					<span class="span1">情况说明：</span><pg:cell colName="INSTANCE_DE_"/>
				</dd>
				<dd>
					<span class="span1">已上传附件：</span>
					<pg:list colName="fileList">
						<br /><a href="<pg:cell colName="FILEPATH"/>" target="new"><pg:cell colName="FILENAME"/></a>
					</pg:list>
				</dd>
			</dl>
			</pg:beaninfo>
		</div>
		<%@ include file="/sanymbp/common/jsp/footer.jsp"%>
	</div>
</body>
</html>