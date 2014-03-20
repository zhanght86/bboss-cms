<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="/sanymbp/common/jsp/taglibs.jsp"%>
<%@page import="com.sany.ems.pending.model.PendingBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>数据列表</title>
<%@ include file="/sanyems/common/header.jsp"%>
</head>
<body>
	<div data-role="page">
		<div class="header">
			<a href="${ctx}/sanyems/showPendingDetail.page?billId=<%=request.getParameter("billId") %>" class="backbtn">
				<span>返回</span>
			</a>
			<h3 class="headtitle">详细信息</h3>
			<div class="headlink">
				<a href="${ctx}/sanyems/showPendingFlow.page?billId=<%=request.getParameter("billId") %>"> <span>审批流程</span> <i></i>
				</a> <a href="${ctx}/sanyems/showPendingDetail.page?billId=<%=request.getParameter("billId") %>"> <span>单据摘要</span> <i></i>
				</a>
			</div>
		</div>
		<!-- /header -->
		<form>
			<div class="detail">
				<div class="tab">
					<ul id="tabul">
						<pg:map requestKey="titleMap">
							<pg:equal expression="{rowid}" value="0">
								<li class="current"><a
									href="javascript:void(0);" id="<pg:mapkey/>"><pg:cell /></a></li>
							</pg:equal>
							<pg:notequal expression="{rowid}" value="0">
							<li><a href="javascript:void(0);" id="<pg:mapkey/>"><pg:cell /></a></li>
							</pg:notequal>
						</pg:map>
					</ul>
					<div class="tabdiv">
						<span></span>
					</div>
				</div>
				<div class="pannle">
					<ol id="olMain">${htmlStr}</ol>
				</div>
			</div>
			<script>
				$(function() {
					$('.pannelol span').each(function() {
						var _this = $(this);
						var _index = $('.pannelol span').index(_this);
						_this.html(_index + 1);
					})
					$('.pannelol:even').addClass('even');
					tabwidth();

					$("#tabul li").children("a").click(function() {
						$.ajax({
							url : "/SanyMBP/sanyems/getPendingDataList.page",
							data:{nameKey:$(this).attr("id")},
							success:function(result){
								$("#olMain").empty();
								$("#olMain").append(result);
							}
						});
					});
				})
				function tabwidth() {
					var tabul = $('#tabul');
					var tabdiv = $('.tabdiv');
					var tabwidth = tabul.children('li').length * 110;
					if (tabwidth>$(document).width()){tabdiv.width(tabwidth);tabul.width(tabwidth)}
					tabul.children('li').click(
							function() {
								var _this = $(this);
								_this.addClass('current').siblings()
										.removeClass('current');
								var _index = tabul.children('li').index(_this);
								tabdiv.children('span').stop(false, false)
										.animate({
											left : _index * 110
										})
							})
				}
			</script>
		</form>
		<div data-role="footer" id="footer" class="footer-docs" data-theme="a" data-position="fixed">
			<p>　&copy; 2012 ISANY</p>
		</div>
	</div>
</body>
</html>