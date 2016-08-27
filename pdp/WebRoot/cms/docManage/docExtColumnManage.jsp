<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.sql.*,java.util.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.common.poolman.sql.*"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/tabpane-taglib.tld" prefix="tab"%>
<%  
    AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);  
    ServletContext context =session.getServletContext();   
    String realPath = context.getRealPath("/");   
	DocumentExtColumnManager docUtil = new DocumentExtColumnManager();
	String tableName = "td_cms_document"; 
	List fixlist = docUtil.getFixColumns(); 
    List extlist = docUtil.getExtColumns();   
%>
<html>
    <head>
	    <link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	    <script src="../inc/js/func.js"></script>
		<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
		<script src="../inc/js/func.js"></script>
		<style type="text/css">	
			body {
				margin-left: 0px;
				margin-top: 0px;
				margin-right: 0px;
				margin-bottom: 0px;
			}
		</style>
		<tab:tabConfig/>
		<title></title>
	</head>
	<body topmargin="1" rightmargin="1" scroll=auto leftmargin="1" onLoad="setFocus();">
	<tab:tabContainer id="foo-channel-add" selectedTabPaneId="base-column">
		<tab:tabPane id="base-column" tabTitle="基本字段">
			<table width="100%" border="0" cellpadding="5" cellspacing="0" class="Datalisttable">
				<tr class="cms_report_tr">					
					<td class="cms_title_blue">字段名</td>
					<td class="cms_title_blue">字段类型</td>
					<td class="cms_title_blue">类型长度</td>
					<td class="cms_title_blue">注释</td>
				</tr>
				<%
				for(int i=0;i<fixlist.size();i++){
					ColumnMetaData str = (ColumnMetaData)fixlist.get(i);
                    String datatype = str.getTypeName().toString(); 
                    int size = str.getColunmSize();
                    if("long".equalsIgnoreCase(datatype)) size = 4000;
                    //datatype = datatype.substring(datatype.indexOf("[")+1,datatype.length()-1);
				%>
				<tr>					
					<td><%=str.getColumnName().toLowerCase()%></td>
					<td><%=datatype%></td>
					<td><%=size%></td>
					<td><%=str.getRemarks()%></td>
				</tr>    
				<%
				}
				%>        
			</table>
		</tab:tabPane>
		<tab:tabPane id="ext-column" tabTitle="扩展字段">
		     <form name="tagOperate" target="taglist" method="POST">
			 <input type="hidden" name="operate_type"/>
				 <table id="tag_attach_list" width="100%" border="1" align=center cellpadding="3" cellspacing="0" bordercolor="#B7CBE4"  class="Datalisttable" id="docListTable">
					<tr>
						<td height='25' colspan="9" background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7">
							<div  class="DocumentOperT">扩展字段操作</div>
							<a style="cursor:hand" onClick="addColmun()">
								<div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">新增</div>
							</a>
							<a style="cursor:hand" onClick="editColmun()">
								<div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">编辑</div>
							</a>
							<a style="cursor:hand" onClick="delColmun()">
								<div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">删除</div>
							</a>
						</td>
					</tr> 
				</table>
			
                <table width="100%" border="0" cellpadding="5" cellspacing="0" class="Datalisttable">
				<tr class="cms_report_tr">
					<td><input type="checkbox" id="col_name" name="col_name" value="" class="checkbox" onclick="selectall()"></td>
					<td class="cms_title_blue">字段名</td>
					<td class="cms_title_blue">字段类型</td>
					<td class="cms_title_blue">类型长度</td>
					<td class="cms_title_blue">注释</td>
				</tr>
				<%
				int max_length = 35;
				for(int i=0;i<extlist.size();i++){
					ColumnMetaData str = (ColumnMetaData)extlist.get(i);
                    String memo = str.getRemarks();
                    String datatype = str.getTypeName().toString();
                    int size = str.getColunmSize();
                    if("long".equalsIgnoreCase(datatype)) size = 4000;
                    //datatype = datatype.substring(datatype.indexOf("[")+1,datatype.length()-1); 
					if(str.getRemarks()!=null && str.getRemarks().length()>max_length) memo = memo.substring(0,max_length)+"...";
				%>
				<tr> 
					<td><input type="checkbox" id="col_name" name="col_name" value="<%=str.getColumnName().toLowerCase()%>"></td>
					<td><%=str.getColumnName().toLowerCase()%></td>
                    <td><%=datatype%></td>
                    <td><%=size%></td>
                    <td><%=memo%></td>
				</tr>    
				<%
					
				}
				%>        
			</table>
			</form>
		</tab:tabPane>
		</tab:tabContainer>
		<iframe name="taglist" width="0" height="0"></iframe>
	</body>
	<SCRIPT LANGUAGE="JavaScript">
	<!--
	var checkflag = false;    
    //全选中复选框
	function selectall(){
		var o = document.getElementsByName("col_name");
		if(checkflag==false){
			for (var i=0; i<o.length; i++){
				if(!o[i].disabled){
						o[i].checked=true;
				}
			}
			checkflag = true;
		}else{
			for (var i=0; i<o.length; i++){
				if(!o[i].disabled){
						o[i].checked=false;
				}
			}
			checkflag = false;
		}
	}
	//单个选中复选框
	function checkOne(id){
		var o = document.getElementsByName("col_name");
		for (var i=0;i<o.length;i++){
			if(!o[i].disabled){
				if (o[i].checked==false){
					cbs=false;
				}
			}
		}
	}
	
	//模态窗口打开模式 新增标签
	function addColmun(){
		var url = "extColumnAdd.jsp";
        var swidth = "500";
        var sheight = "300";
        var returnVal;
		returnVal = openWin(url,swidth,sheight);
        if(returnVal=="True"){
            window.location.reload();  
        }
	}
	//模态窗口打开模式 编辑标签
	function editColmun(){
		var url = "extColumnEdit.jsp";
        var swidth = "500";
        var sheight = "300";
        var returnVal;
		var o = document.getElementsByName("col_name");
        var flag  = false;
		var count = 0;
		var col_name = "";
        for (var i=0; i<o.length; i++){
            if(!o[i].disabled){
                if(o[i].checked==true) {
                    flag = true;
					col_name = o[i].value;
					count ++;
                }
            }
        }
		if(count>1){
		    alert("每次只能编辑一条记录!");
			return false;
		}
        if(!flag){
            alert("请选择要编辑的记录!");
        }else{
			url = url + "?col_name="+col_name;
            returnVal = openWin(url,swidth,sheight);
        }
		
        if(returnVal=="True"){
            window.location.reload();  
        }
	}
    //删除标签
    function delColmun(){
        var o = document.getElementsByName("col_name");
        var flag  = false;
        for (var i=0; i<o.length; i++){
            if(!o[i].disabled){
                if(o[i].checked==true) {
                    flag = true; 
                }
            }
        }
        if(!flag){
            alert("请选择要删除的记录!");
        }else{
            if(confirm("您确定要删除该记录吗?")){
				document.tagOperate.operate_type.value="delete";
                document.frames[0].src = "extColumnOprate_do.jsp";
                document.tagOperate.target = "taglist";
                document.tagOperate.action = "extColumnOprate_do.jsp";
                document.tagOperate.submit();
            }
        }
        
    }
	function setFocus(){
	//document.all.channelForm.channelName.focus();
}

function onNameChange(value){
	document.all.channelForm.displayName.value = value;
}

    
    
	//-->
	</SCRIPT>
</html>


