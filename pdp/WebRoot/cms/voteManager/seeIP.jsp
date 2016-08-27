<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>

<html>
	<head>
		<title>问题IP记录查看</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css"/>
		<style type="text/css">
		body {
			background-color: #ffffff;
		}
		.STYLE1 {color: #0000FF}
		.STYLE2 {color: #000099}
		.style3 {
			font-size: 14px;
			font-weight: bold;
			color: #3300FF;
		}
		.operStyle{
		width:17;
		height:16;
		}
</style>
     </head>
     <script language="JavaScript" src="common.js" type="text/javascript"></script>
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
     <SCRIPT language="javascript">
   
	function returnListVote(){
  if( window.opener && window.opener.refreshSelf ){
        window.opener.refreshSelf();
        window.opener.focus();
  }
  window.close();
}

   </SCRIPT>
    <body topmargin="1" rightmargin="1" scroll=auto leftmargin="1">
     <div id="divProcessing" style="DISPLAY: none; Z-INDEX: 1; LEFT: 22%; WIDTH: 40%; POSITION: absolute; TOP: 80%; HEIGHT: 20px; BACKGROUND-COLOR: #f0f0f0; layer-background-color: #F0f0f0"  class="font"> 
	</div> 
	<form>
		<table cellspacing="1" cellpadding="0" border="0" bordercolor="#eeeeee" width=100% class="thin" align="center"><!--分页显示开始,分页标签初始化-->
         				<pg:listdata dataInfo="Iplist" keyName="Iplist" />
						
						<pg:pager maxPageItems="20" scope="request" data="Iplist" isList="false">
							<!--检测当前页面是否有记录--><!--list标签循环输出每条记录-->
							<TR class="cms_report_tr">
								<!--设置分页表头-->
	                            
								<TD class="headercolor" width="20%"> 问题名称</TD>	
								<TD class="headercolor" width="30%"> 回答人IP</TD>
								<TD class="headercolor" width="30%"> 回答时间</TD>	
												
							</TR>
							<pg:param name="qid"/>		
							<pg:param name="qtitle" />	
							<pg:param name="whoIP" />	
							<pg:param name="when" />
								
							<pg:list>	
							<tr class="cms_data_tr" >
								
								<td class="tablecells" nowrap="true" width="20%" title="<pg:cell colName="qtitle" defaultValue=""/>">
										<pg:cell colName="qtitle" defaultValue="" maxlength="15" replace="..."/>
								</td>
								<td class="tablecells" nowrap="true" width="30%">
										<pg:cell colName="whoIp" defaultValue="" />
								</td>
								<td class="tablecells" nowrap="true" width="30%">
										<pg:cell colName="when" defaultValue="" />
								</td>							
							</tr>
							</pg:list>
						<tr class="labeltable_middle_tr_01">
						<td colspan=10 ><div class="Data_List_Table_Bottom"> 
							 共
							<pg:rowcount />
							 条记录
							<pg:index />				
							</div>  
						</td>
					    </tr>
					</pg:pager>
				  </table>
				  <table bgcolor="#336699" border="0" cellPadding="0" cellSpacing="0" width="100%">
			<tbody>
				<tr>
					<td>
						<br>
					</td>
				</tr>
				<tr>


					<td class="c2" color="#000000"></td>
					<td align="right" class="c1" width="50%" color="#000000">
						<font color="#FFFFFF">│<a href="javascript:window.returnListVote();"><font color="#FFFFFF"> 关闭窗口</font></a><font color="#FFFFFF">│</font></font>
					</td>
				</tr>
			</tbody>
		</table>
				</form></body>
</html>