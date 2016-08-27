<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
    AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
    
%>
<html>
    <title>属性容器</title>
    <head>
        <link href="../../inc/css/cms.css" rel="stylesheet" type="text/css">        
        <%@ include file="/include/css.jsp"%>
        <link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
        <link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
        <script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
        <script language="JavaScript" src="../../user/common.js" type="text/javascript"></script>
        <script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
    </head>
    <body class="contentbodymargin" scroll="yes">
        <form name="form1" target="hiddenFrame" method="post">    
		    <table cellspacing="0" cellpadding="0" border="0" class="thin" width=100%>
		        <tr>
		           <td height='50' align="center" colspan="0" >		                 
		               <input type="button" value="回收" class="input" onclick="storeOrgTaxcode()">		               
		            </td>
		        </tr>
		    </table>
		</form>
	</body>
	<iframe height="0" width="0" name="hiddenFrame"></iframe>
	<script>
	    var win;
		var featrue = "dialogWidth=600px;dialogHeight=500px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;help=0;dialogLeft="+(screen.availWidth-600)/2+";dialogTop="+(screen.availHeight-500)/2;
	    function storeOrgTaxcode(){
	        var orgId = "";
	        var dicttypeId = "";
	        var arr = "";
	        var dicttypeValues = "";
	        if(parent.orgTaxcode){	            
	            if(parent.orgTaxcode.document.frames[0]){
	                dicttypeId = parent.orgTaxcode.document.frames[0].document.all("selected_typeId").value;
	            }
	            if(parent.orgTaxcode.document.frames[1]){
	                orgId = parent.orgTaxcode.document.frames[1].document.all("orgId").value;
	            }
	            if(parent.orgTaxcode.document.frames[2]){
	                arr = parent.orgTaxcode.document.frames[2].document.getElementsByName("dictdataValue");
	                for(var i=0;i<arr.length;i++){
	                    if(arr[i].checked==true){
	                        if(dicttypeValues=="") dicttypeValues = dicttypeValues + arr[i].value;
	                        else dicttypeValues = dicttypeValues + "," + arr[i].value;
	                    }	                    
	                }
	            }
	        }
	        //没有选择类型
	        if(dicttypeId==""){
	            alert("请选择编码种类!");
	            return false;
	        }
	        if(orgId==""){
	            alert("请选择税务机关!")
	            return false;
	        }
	        
	        var path = "saveOrgTaxcode.jsp?orgId="+orgId+"&dicttypeId="+dicttypeId+"&dicttypeValues="+dicttypeValues;
	        		
			document.form1.action = path;
			document.form1.submit();
			win = window.showModelessDialog("<%=request.getContextPath()%>/doing.html","",featrue);
	           
	    }
	</script>
	
</html>	
