<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
    //常用关系维护-----------------------------------------------
    AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
    
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
            <input type="hidden" name="selectedNodeValue"> 
            <input type="hidden" name="un_selectedNodeValue"> 
            <input type="hidden" name="sendOrgId"> 
            <input type="hidden" name="sendDicttypeId"> 
		    <table cellspacing="0" cellpadding="0" border="0" class="thin" width=100%>
		        <tr>
		           <td height='50' align="center" colspan="0" >		                 
		               <input type="button" value="保存" class="input" onclick="storeOrgTaxcode()">		               
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
	        var un_dicttypeValues = "";
	        if(parent.parent.orgTaxcode){	            
	            if(parent.parent.orgTaxcode.document.frames[0]){
	                dicttypeId = parent.parent.orgTaxcode.document.frames[0].document.all("selected_typeId").value;
	            }
	            if(parent.parent.orgTaxcode.document.frames[1]){
	                orgId = parent.parent.orgTaxcode.document.frames[1].document.all("orgId").value;
	            }
	            if(parent.orgDictdataTree.document.frames[0]){
	                arr = parent.orgDictdataTree.document.frames[0].document.getElementsByName("dictdataValue");
	                for(var i=0;i<arr.length;i++){
	                	var strt = "";
						var isDeploy = "false";
						if(i != 0){
							strt = "icon_"+dicttypeId+":"+arr[i].value;
							if(parent.orgDictdataTree.document.frames[0].$(strt)){
								isDeploy = parent.orgDictdataTree.document.frames[0].$(strt).firsted;
							}else{
								isDeploy = false;
							}
						}
	                    if(i != 0 && arr[i].checked==true){
	                        if(dicttypeValues=="") dicttypeValues = dicttypeValues + arr[i].value + ":" + isDeploy;
	                        else dicttypeValues = dicttypeValues + "," + arr[i].value + ":" + isDeploy;
	                    }else if(i != 0){
	                        if(un_dicttypeValues=="") un_dicttypeValues = un_dicttypeValues + arr[i].value + ":" + isDeploy;
	                        else un_dicttypeValues = un_dicttypeValues + "," + arr[i].value + ":" + isDeploy;
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
	        
	        var path = "saveOrgTaxcode.jsp?op=direct";
	        document.all.selectedNodeValue.value = dicttypeValues;
	        document.all.un_selectedNodeValue.value = un_dicttypeValues;
	        alert(dicttypeValues);
	        alert(un_dicttypeValues);
	        document.all.sendOrgId.value = orgId;
	        document.all.sendDicttypeId.value = dicttypeId;		
			document.form1.action = path;
			win = window.showModelessDialog("../doing.jsp","",featrue);
			document.form1.submit();
			
	           
	    }
	    
	    function aftersave(){
	        win.close();
	        alert("操作成功!");	
	    }
	</script>
	
</html>	
