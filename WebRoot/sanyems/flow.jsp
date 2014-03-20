<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="/sanymbp/common/jsp/taglibs.jsp"%>
<%@page import="com.sany.ems.pending.model.FlowBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>审批流程</title>
<%@ include file="/sanyems/common/header.jsp"%>
</head>
<body>
	<div data-role="page">
		<div class="header">
			<a
				href="${ctx}/sanyems/showPendingDetail.page?billId=<%=request.getParameter("billId") %>"
				class="backbtn"> <span>返回</span>
			</a>
			<h3 class="headtitle">审批流程</h3>
			<div class="headlink">
				<a
					href="${ctx}/sanyems/showPendingDataList.page?billId=<%=request.getParameter("billId") %>">
					<span>详细信息</span> <i></i>
				</a> <a
					href="${ctx}/sanyems/showPendingDetail.page?billId=<%=request.getParameter("billId") %>">
					<span>单据摘要</span> <i></i>
				</a>
			</div>
		</div>
		<div class="flow">
			<form>
				<fieldset data-role="controlgroup">
					<pg:list requestKey="list">
						<div>
							<input name="hidTaskId" type="hidden"
								value="<pg:cell colName="TASK_ID" />" /> <input type="radio"
								name="flow-1" id="flow-<pg:rowid />" value="choice-<pg:rowid />" />
							<label for="flow-<pg:rowid />"> <span class="flowname"><pg:cell
										colName="TASK_LAST_USER_NAME" /></span> <span class="flowtime"><pg:cell
										colName="TASK_LAST_DATETIME" /></span> <span class="flowapproval"><pg:cell
										colName="TASK_RESULT" /></span>
							</label>
						</div>
					</pg:list>
				</fieldset>
				<div data-role="fieldcontain" class="approvaldiv">
					<div class="approvalhis">
						<div class="hiswrapper">
							<a class="hisbtn" href="javascript:void(0);"><span><i>历史记录</i></span></a>
							<div class="hisdialog">
								<div class="hisdialogtop"></div>
								<ul>
									<pg:list requestKey="historyList">
										<li><a href="javascript:void(0);"><pg:cell
													colName="TEXTBOX_VALUE" /></a></li>
									</pg:list>
								</ul>
							</div>
						</div>
					</div>
					<label for="approvaltxt">审批意见: </label>
					<textarea name="approvaltxt" id="approvaltxt" class="approvaltxt"></textarea>
				</div>
			</form>
		</div>
		<div class="footer">
			<ul id="controlUL" class="flowctr">
				<li class="backfirst"><a href="javascript:void(0);"><i></i>否决</a></li>
				<li class="pass"><a href="javascript:void(0);"><i></i>通过</a></li>
				<li class="backlast"><a href="javascript:void(0);"><i></i>驳回</a></li>
			</ul>
		</div>
		<div data-role="footer" id="footer" class="footer-docs" data-theme="a"
			data-position="fixed">
			<p>&copy; 2012 ISANY</p>
		</div>
		<script>
			$(function() {
				$("fieldset").find("input[type='radio']").each(function() {
					$(this).attr("checked", false);
				});

				approvalhistory();
				flowdivheight();
				$("#controlUL li")
						.children("a")
						.click(
								function() {
									var cType = 0;
									switch ($(this).parents("li").attr("class")) {
									case "backfirst":
										cType = "0";
										break;
									case "pass":
										cType = "1";
										break;
									case "backlast":
										cType = "2";
										break;
									}
									var isChecked = validateRadio($("fieldset"));
									if (cType == "2" && !isChecked) {
										alert('请先选择驳回节点');
									}else if(cType == "0" && $("#approvaltxt").val()==""){
										alert('请先输入审批意见');
									} else {
										var checkedObj = getCheckedRadio($("fieldset"));
										if (confirm("确认操作？")) {
											var formTaskId =cType == "2"? checkedObj
													.parents("div")
													.find(
															"input[type='hidden'][name='hidTaskId']")
													.val():"";
											var appText = $("#approvaltxt").val();
											$.ajax({
														url : "/SanyMBP/sanyems/billAuditHandle.page",
														data : {
															auditText : appText,
															privoursTaskId : formTaskId,
															controlType : cType
														},
														success : function(
																result) {
															if (result == "success") {
																location.href = "/SanyMBP/sanyems/showPendingList.page";
															}
														}
													});
										}
									}
								});
			});

			function validateRadio(parentObj) {
				var isChk = false;
				$(parentObj).find("input[type='radio']").each(function() {
					if ($(this).attr("checked") == "checked") {
						isChk = true;
						return;
					}
				});
				return isChk;
			}

			function getCheckedRadio(parentObj) {
				var obj = null;
				$(parentObj).find("input[type='radio']").each(function() {
					if ($(this).attr("checked") == "checked") {
						obj = $(this);
						return;
					}
				});
				return obj;
			}

			function flowdivheight() {
				if ($('body').height() - $('.flow').height() > 115) {
					$('.flow').height($('body').height() - 105)
				}
			}
			function approvalhistory() {
				var dialogbtn = $('a.hisbtn');
				var dialogdiv = $('div.hisdialog');
				var dialoga = $('div.hisdialog li a');
				var t = '';

				dialogbtn.click(function() {
					hisdiashow();
				})
				dialogbtn.hover(function() {
					clearTimeout(t)
				}, function() {
					t = setTimeout('hisdiahide()', 300)
				})
				dialogdiv.hover(function() {
					clearTimeout(t)
				}, function() {
					t = setTimeout('hisdiahide()', 300)
				})
				dialoga.click(function() {
					clearTimeout(t)
					var _html = $(this).html();
					hisdiahide();
					$('#approvaltxt').html(_html);
				})
			}
			function hisdiahide() {
				$('div.hisdialog').hide();
			}
			function hisdiashow() {
				$('div.hisdialog').show();
			}
		</script>
	</div>
</body>
</html>