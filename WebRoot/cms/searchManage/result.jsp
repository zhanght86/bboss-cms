<%@ page language="java"  pageEncoding="utf-8" session="false"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ page import="java.io.BufferedReader"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.FileReader"%>
<%@ page import="java.io.FileWriter"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.handler.ContentHandler"%>

<div id="customContent">
<div class="comment_title"><div class="comment_num"><a href="javascript:void"><font color="blue" ><b>${queryString}</b></font>的搜索结果，检索耗时：<span class="red_num" >${searchTime }</span>秒，共检索出<span class="red_num" >
            ${rowcount }</span>条记录</a></div>
          </div>
	<pg:empty actual="${searchhitList}"  >
		<div style="text-align: center;padding-top:10px;">
			<img src="${pageContext.request.contextPath}/html/images/no_data.jpg"/></div>
		</pg:empty> 
	<pg:notempty actual="${searchhitList}">
	
	
          
	
	<table class="comment_content">
	 <pg:list requestKey="searchhitList">
	 	<tr>
            <td valign="top" bgcolor="<pg:cell colName="bgcolor"/>"><b>
              <pg:rowid increament="1"/>
            </b></td>
            <td bgcolor="<pg:cell colName="bgcolor"/>"><a href="<pg:sitedomain/><pg:cell colName="uri"/>" style="cursor:hand" target="_blank" ><pg:cell colName="contentTypeDes"/> &nbsp; <b>
              <pg:cell colName="title" defaultValue="" />
              </b>
                  <pg:cell colName="score" defaultValue="" />%</a> <br />
              描述: <pg:cell colName="content"/> <br />
              <a href="<pg:sitedomain/><pg:cell colName="uri"/>" style="cursor:hand"><font color="black">
                <pg:sitedomain/><pg:cell colName="uri"/>
                &nbsp;
                <pg:cell colName="published" dateformat="yyyy/MM/dd HH:mm:ss" defaultValue="" />
                </font></a> <br />
              <br />
            </td>
          </tr>
		</pg:list>
	</table>
	
    
    </pg:notempty>
</div>		