<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>

		<title>管控指标</title>
		<script language="JavaScript" src="<%=basePath%>/include/pager.js"
			type="text/javascript">
</script>
		<script language="JavaScript"
			src="<%=basePath%>/manageguide/js/checkBox.js"
			type="text/javascript">
</script>

		<link rel="stylesheet" type="text/css"
			href="<%=basePath%>/common/css/button.css">
		<link rel="stylesheet" type="text/css"
			href="<%=basePath%>/common/css/css.css">
		<link rel="stylesheet" type="text/css"
			href="<%=basePath%>/common/css/table.css">
		<link rel="stylesheet" type="text/css"
			href="<%=basePath%>/common/css/input.css">
<style type="text/css">
body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,form,fieldset,input,textarea,blockquote,iframe{
	padding:0; margin:0;
}
table { 
	margin:0 auto;
	padding:0;
}
</style>	
<script type="text/javascript">
  function setcolor(obj){
	obj.style.background ="#E9EEF4" ;
  }
  
  function chucolor(obj){
  	obj.style.background ="";
  }
</script>		
	</head>
	<body>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="Ctable">
			<pg:listdata
				dataInfo="com.frameworkset.platform.integralwarning.scheme.service.SchemeService"
				keyName="SchemeService" />
			<!--分页显示开始,分页标签初始化-->
			<pg:pager maxPageItems="10" scope="request" data="SchemeService"
				isList="false">
				<tr>
					<!--设置分页表头-->
					<td class="c3" align=center>
						<input type="checkbox" name="allcheck" value=""
							onclick='javascript:checkAll("allcheck","onecheck");' />
					</td>
					<td height='20' class="c3">
						方案编号
					</td>
					<td height='20' class="c3">
						方案名称
					</td>
					<td height='20' class="c3">
						开始时间
					</td>
					<td height='20' class="c3">
						结束时间
					</td>
				</tr>
				<pg:param name="tableName" />
				<pg:param name="keyName" />
				<pg:param name="keyType" />
				<pg:param name="db" />

				<!--检测当前页面是否有记录-->
				<pg:notify>
					<tr height='25' class="labeltable_middle_tr_01">
						<td colspan=100 align='center'>
							暂时没有信息
						</td>
					</tr>
				</pg:notify>

				<!--list标签循环输出每条记录-->
				<pg:list>
					<tr onmousemove="setcolor(this)" onmouseout="chucolor(this)"
						>
						<td class="t1" align=center>
							<input type="checkbox" name="onecheck"
								onclick="checkOne('allcheck','onecheck');"
								value="<pg:cell colName="sequence_id" defaultValue=""/>">
						</td>
						<td height='20' align=left class="t1">
							<pg:cell colName="scheme_id" defaultValue="" />
						</td>
						<td height='20' align=left class="t1" title="<pg:cell colName="remark" defaultValue="" />">
						    <pg:cell colName="scheme_name" maxlength="22" replace="..." defaultValue="" />
							
						</td>
						<td height='20' align=left class="t1">
							<pg:cell colName="start_time" defaultValue="" />
						</td>
						<td height='20' align=left class="t1">
							<pg:cell colName="end_time" defaultValue="" />
						</td>

					</tr>
				</pg:list>
				<tr height="30" class="labeltable_middle_tr_01">
					<td colspan=100 align='center'>
						<pg:index />
						<input type="hidden" name="querystring" value="<pg:querystring/>">
					</td>
				</tr>
			</pg:pager>
		</table>
	</body>
</html>

