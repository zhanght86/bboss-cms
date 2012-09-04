<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<%
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Session_cache_limiter", "private");
		String basePath = request.getContextPath();
	%>
	<head>
		<title>方案管理</title>
		<script language="JavaScript"
			src="<%=request.getContextPath()%>/scheme/js/user-frame.js">
		</script>
		<link rel="stylesheet" type="text/css"
			href="<%=basePath%>/common/css/button.css">
			<link rel="stylesheet" type="text/css"
				href="<%=basePath%>/common/css/css.css">
				<link rel="stylesheet" type="text/css"
					href="<%=basePath%>/common/css/table.css">
					<link rel="stylesheet" type="text/css"
						href="<%=basePath%>/common/css/input.css">
		<script type="text/javascript">
var basePath = "<%=request.getContextPath()%>";
</script>
	</head>
	<body>
		<form action="" method="post" id="formDownLoad" name="formDownLoad">
		</form>
		<s:form name="excelForm" id="excelForm" action="" method="POST"
			theme="simple" enctype="multipart/form-data">
			<table width="100%" id="listtable" border="0" cellspacing="0"
				cellpadding="0" class="Ctable">
				<tr>
					<td width="50%">
						<table width="100%" id="listtable" border="0" cellspacing="0"
							cellpadding="0" class="Ctable">
							<tr>
								<td colspan="4" class="taobox22">
									人员维护列表-Excel导入
								</td>
							</tr>
							<tr>
								<td width="20%" class="c2" align="right" nowrap="nowrap">
									Excel路径
								</td>
								<td width="80%" class="table_block_td" align="left"
									nowrap="nowrap">

									<input type="file" name="upLoadFile" class="input_out"
										id="upLoadFile" size="25" />

								</td>
							</tr>
							<tr>
								<td width="3%" colspan="2" align="center" nowrap="nowrap">
									<span style="text-align: center;"> 
									<input type="button" name="addScheme2" onclick="download_card()" class="button" 
											value="模板下载" />
									 <input name="Submit2222" type="button" onclick="subMit()" class="button" value="导入Excel" />
									</span>
								</td>
							</tr>
						</table>
					</td>
                    <input type="hidden" name="tdPwSchemeTemp.schemeId" value='<%=request.getParameter("schemeId") %>'/>
				</tr>
				<tr>
					<td width="50%">
						<iframe src="" name="excelListFrame" id="excelListFrame"
							border="0" style="margin-top: 12px;" frameborder="0"
							framespacing="0" marginheight="0" marginwidth="0" 
							width="100%" />
					</td>
				</tr>
			</table>
		</s:form>

	</body>
</html>

