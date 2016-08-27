<%@page import="com.frameworkset.platform.cms.util.CMSUtil"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="java.util.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor"%>
<%@page import="com.frameworkset.platform.cms.driver.publish.impl.APPPublish"%>
<%
    AccessControl accesscontroler = AccessControl.getAccessControl();
    String uuid = CMSUtil.getUUID();
    String refresh = request.getParameter("refresh");
    refresh = refresh==null?"":refresh;
    //发布的重要参数
    //flag 发布类型
    //docid 文档ID
    String flag = request.getParameter("flag");
    flag = flag==null?"":flag;
    String docId = request.getParameter("docId");
    docId = docId==null?"":docId;
    
    response.setHeader("Cache-Control", "no-cache"); 
    response.setHeader("Pragma", "no-cache"); 
    response.setDateHeader("Expires", -1);  
    response.setDateHeader("max-age", 0);    
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>发布文档</title>
<style type="text/css">
<!--
.STYLE7 {color:#003398; font-size: 12px;}
#Layer1 {
    position:absolute;
    left:0px;
    top:0px;
    width:113px;
    height:127px;
    z-index:1;
}
.STYLE8 {color: #0000FF}
.STYLE1 {color: blue}
-->
</style>
</head>

<body background="../images/grayline_bg.jpg">
<form name="form1" action="" method="post">
<table width="98%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" align="center">
    <tr>
        <td width="4%" height="20" align="right">
            <img src="../images/ico_point.gif" width="25" height="25">
        </td>
        <td height="20" class="cms_title_blue">
                文档发布
        </td>
    </tr>
</table>
<table width="98%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" align="center">
    <tr>
        <td width="100" height="24" align="right" nowrap>是否记录日志:</td>
        <td height="24">
            <input hideFocus type="checkbox" id="isRecordMsg1" name="isRecordMsg1" value="false" onclick="document.all.isRecordMsg2.checked=false"  checked/>不记录
            <input hideFocus type="checkbox" id="isRecordMsg2" name="isRecordMsg2" value="true" onclick="document.all.isRecordMsg1.checked=false"/>记录
            <span id="infoMsg" style="display:none;">
                <INPUT name="button" id="showinfo" type="button" class="cms_button" onClick="showMsg()" >
                <!-- <INPUT  type="button" class="cms_button" value="刷新信息" onClick="updateMsg()" > -->
            </span>
        </td>
    </tr> 
    <tr>
        <td width="100" height="24" align="right" nowrap>是否递归发布:</td>
        <td height="24">
        	<input hideFocus type="checkbox" id="recursionPublishYes" name="recursionPublishYes" value="true" onclick="document.all.recursionPublishNo.checked=false;document.all.recursionPublish.value=true"  checked/>是
            <input hideFocus type="checkbox" id="recursionPublishNo" name="recursionPublishNo" value="false" onclick="document.all.recursionPublishYes.checked=false;document.all.recursionPublish.value=false"/>否
            <input type="hidden" id="recursionPublish" name="recursionPublish" value="true"/>
        </td>
    </tr>
    <tr>
    	<td width="100" height="24" align="right" nowrap>是否清除缓存:</td>
        <td height="24">
        	<input hideFocus type="checkbox" id="clearCacheNo" name="clearCacheNo" onclick="document.all.clearCacheYes.checked=false;document.all.clearCache.value=false" value="false" checked/>不清除
			<input hideFocus type="checkbox" id="clearCacheYes" name="clearCacheYes" value="true" onclick="document.all.clearCacheNo.checked=false;document.all.clearCache.value=true" />清除
			<input id="clearCache" name="clearCache" type="hidden" value="false" />
		</td>
    </tr>
</table>
<br>
</form>
<div  style="text-align:center;">
    <INPUT name="button" id="publishButton" type="button" class="cms_button" onClick="subform()"  value="发布">
    &nbsp;&nbsp;&nbsp;
    <INPUT name="button" id="closeButton" type="button" class="cms_button" onClick="window.close()"  value="关闭">
        
</div>
<div id="waiting_marquee" style="position:absolute;width:40%;height:20px;z-index:1;background-color:#F0f0f0;layer-background-color:#F0f0f0;left:22%;top:80%;display:none;"  class="font">   
    <marquee direction="left" width="250" behavior="alternate"><span class=STYLE1>正在发布中，请稍等……</span></marquee>
</div>



<iframe name="publishFrame" src="#" width="2" height="2"  ></iframe>
    <div id="waiting" style="text-align:center;display:none">    
        <span>
            <textarea name="publish_info" style="width:400" rows="8"></textarea>
        </span>        
    </div>
    <div id="linkInfo" style="text-align:center">
    </div>
    <div id="sp_button" style="text-align:center">
    </div>
</body>

<script language="javascript">

    window.onunload = function setReturnStr(){        
        if("<%=refresh%>"!="false"){
            //alert("refresh parent window")
            var url = window.dialogArguments.document.all("url").value;
            //刷新父页面
            window.dialogArguments.document.location.href = url;
        }else{
            //alert("close parent window");            
            window.returnValue = "close";
        }
        //清空session
       // var path = "../cleanSession.jsp?uuid=<%=uuid%>";
       // window.dialogArguments.document.all("cleanSession").src = path;
    }
    function subform(){
        if(document.getElementById("publishButton"))
            document.getElementById("publishButton").disabled="disabled";
        if(document.getElementById("isRecordMsg1"))
            document.getElementById("isRecordMsg1").disabled="disabled";
        if(document.getElementById("isRecordMsg2"))
            document.getElementById("isRecordMsg2").disabled="disabled";
        if(document.getElementById("clearCacheYes"))
			document.getElementById("clearCacheYes").disabled="disabled";
		if(document.getElementById("clearCacheNo"))
			document.getElementById("clearCacheNo").disabled="disabled";  
		if(document.getElementById("recursionPublishYes"))
			document.getElementById("recursionPublishYes").disabled="disabled";
		if(document.getElementById("recursionPublishNo"))
			document.getElementById("recursionPublishNo").disabled="disabled";  
        
        var isRecordValue = "";
        if(document.all("isRecordMsg1").checked) 
            isRecordValue = document.all("isRecordMsg1").value;
        if(document.all("isRecordMsg2").checked)
            isRecordValue = document.all("isRecordMsg2").value;
            
        window.dialogArguments.document.all("isRecordValue").value = isRecordValue;
        window.dialogArguments.document.all("uuid").value = "<%=uuid%>";
        
        //增加是否递归发布和是否清除缓存
        window.dialogArguments.document.all("recursionPublish").value = document.all("recursionPublish").value;
        window.dialogArguments.document.all("clearCache").value =document.all("clearCache").value;
        
        
        //提交父窗口
        window.dialogArguments.document.form1.submit();        
        //if(isRecordValue == "true"){
        //    updateMsg();
        //}
        
        var isRecordValue = "";
        if(document.all("isRecordMsg1").checked) 
            isRecordValue = document.all("isRecordMsg1").value;
        if(document.all("isRecordMsg2").checked)
            isRecordValue = document.all("isRecordMsg2").value;
        
        if(isRecordValue=="true"){    
            document.all.infoMsg.style.display = "";
            document.all("showinfo").value = "查看日志"
           
        }else{
            document.all.waiting_marquee.style.display = "";
        }
        document.all.waiting_marquee.style.display = "";
    }
    //被list页面回调, 显示最新记录的发布日志内容
    function updateMsg(multipub,pageUrl){
   
        document.all("publish_info").doScroll('down');
        document.all("info").src="publish_info.jsp?uuid=<%=uuid%>";
         if(pageUrl != null && pageUrl.length > 0){
            document.all("waiting_marquee").style.display = "none";
             var isRecordValue = window.dialogArguments.document.all("isRecordValue").value;             
             try{
                 if(isRecordValue=="true"){
                     var infomsg = "<a href='"+pageUrl+"'>发布文档:"+pageUrl+"</a>";
                     if(multipub != null && multipub=="mutipublish" ){
                         infomsg = "批量发布,请逐一查看发布文档"
                     }
                     document.all("linkInfo").innerHTML = infomsg;
                     alert("恭喜，发布成功！"); 
                 }  
                  setTimeout("clearTimer()",1000);  
                 //window.clearInterval(timer1);
                 
             }catch(err){
             	 setTimeout("clearTimer()",1000);  
                 alert(err.description);
             }   
         }
    }
    //被list页面回调, 显示发布成功的提示
    //url 为空的时候 直接alert(msg)
    //url 不为空的时候 confirm()选择msg
    function alertMsg(msg,url){
        if(url != ""){
	        con = window.confirm(msg);
	        if(con){
	            window.open(url);
	        } 
        }else{
            alert(msg);
        }
    }
    function showMsg(){
        if(document.all("showinfo").value == "查看日志") 
            document.all("showinfo").value = "隐藏日志";
        else document.all("showinfo").value = "查看日志";
        
        if(document.getElementById("waiting")){
            if(document.getElementById("waiting").style.display==""){
                document.getElementById("waiting").style.display="none";
            }else{
                document.getElementById("waiting").style.display="";
            }
        }
    }
    
    
</script>
</html>

