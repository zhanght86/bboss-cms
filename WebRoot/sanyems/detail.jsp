<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="/sanymbp/common/jsp/taglibs.jsp"%>
<%@page import="com.sany.ems.pending.model.PendingBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>详细信息</title>
<%@ include file="/sanyems/common/header.jsp"%>
</head>
<body>
	<div data-role="page">
		<div class="header">
			<a href="${ctx}/sanyems/showPendingList.page;" class="backbtn"> <span>返回</span>
			</a>
			<h3 class="headtitle">单据摘要</h3>
			<div class="headlink">
				<pg:beaninfo requestKey="model">
					<a href="${ctx}/sanyems/showPendingFlow.page?billId=<pg:cell colName='BILL_ID' />" > <span>审批流程</span> <i></i>
					</a>
					<a
						href="${ctx}/sanyems/showPendingDataList.page?billId=<pg:cell colName='BILL_ID' />">
						<span>详细信息</span> <i></i>
					</a>
				</pg:beaninfo>
			</div>
		</div>
		<!-- /header -->

		<div data-role="content">
			<form>
				<div class="docket">
					<pg:beaninfo requestKey="model">
						<ul>
							<li><label for="name">单号:</label><span class="value"><pg:cell
										colName="BILL_NO" /></span></li>
							<li><label for="examinesys">核算体系:</label><span class="value"><pg:cell
										colName="HSS_NAME" /></span></li>
							<li><label for="doctype">单据类型:</label><span class="value"><pg:cell
										colName="BILL_TEMPLATE_NAME" /></span></li>
							<li><label for="approvaltype">审批类型:</label><span
								class="value"><pg:cell colName="AUDIT_TYPE_NAME" /></span></li>
							<li><label for="project">项目:</label><span class="value"><pg:cell
										colName="PROJECT_NAME" /></span></li>
							<li><label for="date">提单日期:</label><span class="value"><pg:cell
										colName="BILL_SUBMIT_DATETIME" /></span></li>
							<li><label for="manager">经办人:</label><span class="value"><pg:cell
										colName="BILL_USER_NAME" /></span></li>
							<li><label for="department">部门:</label><span class="value"><pg:cell
										colName="BILL_DEPARTMENT_NAME" /></span></li>
							<li><label for="budget">是否预算内:</label><span class="value"><pg:cell
										colName="BILL_ISBUDGETNAME" /></span></li>
							<li><label for="docprice">单据金额:</label><span class="value"><pg:cell
										colName="BILL_AMOUNT" /></span></li>
							<li><label for="approvalprice">审核金额:</label><span
								class="value"><pg:cell colName="BILL_ACC_AMOUNT" /></span></li>
							<li><label for="reason">事由:</label><span class="value"><pg:cell
										colName="BILL_REMARK" /></span></li>
							<li><label for="specialapprovaltype">特批类型:</label><span
								class="value"><pg:cell colName="BILL_AUDIT_SPECIAL_NAME" /></span></li>
						</ul>
					</pg:beaninfo>
				</div>
			</form>
			<div data-role="footer" id="footer" class="footer-docs"
				data-theme="a" data-position="fixed">
				<p>　&copy; 2012 ISANY</p>
			</div>
		</div>
</body>
</html>