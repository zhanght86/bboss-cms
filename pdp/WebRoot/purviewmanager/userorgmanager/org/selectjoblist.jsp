<%
/*
 * <p>Title: 岗位主界面</p>
 * <p>Description: 岗位主界面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2012-06-21
 * @author biaoping.yin
 * @version 1.0
 */
%>

<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<link href="../../../html/stylesheet/common.css" rel="stylesheet" type="text/css" />
<div id="customContent" style="width:110%">
	<pg:pager scope="request"  data="datas" desc="false" isList="false" containerid="dd_1" selector="customContent">
	<pg:param name="jobname"/>
	<select name="allist"  multiple style="width:100%" onDBLclick="addjob()" size="18" title="下拉选择框的右边边框可以左右拖动">
		 <pg:list autosort="false">
			<option value="<pg:cell colName="jobId"/>"><pg:cell colName="jobName"/></option>
		 </pg:list>			
	</select>
   		
	<div class="pages" tyle="width:100%"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index export="110000111"/></div>
    </pg:pager>
</div>
		




