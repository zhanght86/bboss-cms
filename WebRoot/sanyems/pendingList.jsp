<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="/sanymbp/common/jsp/taglibs.jsp"%>
<%@page import="com.sany.ems.pending.model.PendingBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8" http-equiv="Content-Type" content="text/html;">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>EMS移动审批</title>
<%@ include file="/sanyems/common/header.jsp"%>
</head>
<body>
	<div data-role="page">
		<div class="header">
			<a href="${ctx}/sanymbp/index.page" class="backbtn" rel="external"> <span>返回</span>
			</a>
			<h3 class="headtitle">待我审批</h3>
		</div>
		<!-- /header -->
		<div data-role="content">
			<ul data-role="listview">
				<pg:list requestKey="list">
					<li><a href="${ctx}/sanyems/showPendingDetail.page?billId=<pg:cell colName='BILL_ID' />" class="listlink">
							<h3>
								<span class="applyuser"><pg:cell colName="BILL_USER_NAME" /></span>
								<span class="applyid"><pg:cell colName="BILL_NO" /></span>
								<span class="applytime"><pg:cell colName="BILL_SUBMIT_DATETIME" /></span>
							</h3>
							<p class="applyinfo">
								<span>申请事由：</span><pg:cell colName="BILL_REMARK" />
							</p>
					</a></li>
				</pg:list>
			</ul>
		</div>
		<div data-role="footer" id="footer" class="footer-docs" data-theme="a" data-position="fixed">
			<p>　&copy; 2012 ISANY</p>
		</div>
	</div>
</body>
</html>