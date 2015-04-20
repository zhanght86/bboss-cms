<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>

<%@ include file="/common/jsp/importAndTaglib.jsp"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld"%>

	<%--
	描述：桌面背景自定义列表信息设置
	作者：qian.wang
	版本：1.0
	日期：2011-09-20
	 --%>	
<%
String path = request.getContextPath();
%>


  <div id="customContent">
     <table>
       <tr>
       <td colspan="1">自定义背景图片信息列表:</td>
       <td colspan="4" align="right">
	    	<input type="button" class="button" value="批量保存" onclick="saveListBackGrounds()" />
	    </td></tr>
    </table>
    <pg:pager scope="request"  data="datas" isList="false" containerid="custombackContainer" selector="customContent">
    <form id="listForm" name="listForm">
    <table class="Ctable">
       <tr>
       		<td class="c3">序号</td>
       		<td class="c3">自定义名称</td>
       		<td class="c3">图片路径名称</td>
       		<td class="c3">上传时间</td>
       		<td class="c3">操作</td>
       </tr>
      <pg:list>
       <tr>
         <td>
         <input type="hidden" name="userid" value='<pg:cell colName="userid"/>'/>
         <input type="hidden" name="subsystem" value='<pg:cell colName="subsystem"/>'/>
         <input type="hidden" name="filename" value='<pg:cell colName="filename"/>'/>
         <pg:rowid increament="1" offset="false"/>
         </td>
         <td><input type="text" class="cn_name" name="cn_name" id='<pg:rowid/>' value='<pg:cell colName="cn_name"/>'/></td>
         <td><pg:cell colName="filename"/></td>
         <td><pg:cell colName="creatdate" dateformat="yyyy-MM-dd hh:mm:ss"/></td>
         <td><a  href="javascript:void(0)" onclick="deleteCustom('<pg:cell colName="filename"/>')">删除</a>&nbsp;&nbsp;<a  href="javascript:void(0)" onclick="saveCustom('<pg:cell colName="filename"/>','<pg:rowid/>')">保存</a></td>
       </tr>
    </pg:list>
       <tr>
			<td height="20" class="pagefoot" colspan="7" align="center"
				class="pagefoot">
				<pg:index />
				<input id="querystring" type="hidden"
					value='<pg:querystring/>' />
			</td>
		</tr>
    </table>
    </form>
    </pg:pager>
  	</div>							


