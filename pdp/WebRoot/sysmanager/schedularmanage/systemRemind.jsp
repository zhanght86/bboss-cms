<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.schedularmanage.Schedular"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.SchedularManagerImpl"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<!-- !开始 更新弹出窗口 -->
<script language="JavaScript"> 
window.onload = getMsg; 
window.onresize = resizeDiv; 
window.onerror = function(){} 
//短信提示使用(asilas添加) 
var divTop,divLeft,divWidth,divHeight,docHeight,docWidth,objTimer,i = 1; 
function getMsg() 
{ 
try{ 
divTop = parseInt(document.getElementById("eMeng").style.top,10) 
divLeft = parseInt(document.getElementById("eMeng").style.left,10) 
divHeight = parseInt(document.getElementById("eMeng").offsetHeight,10) 
divWidth = parseInt(document.getElementById("eMeng").offsetWidth,10) 
docWidth = document.body.clientWidth; 
docHeight = document.body.clientHeight; 
document.getElementById("eMeng").style.top =
parseInt(document.body.scrollTop,10) + docHeight + 10;// divHeight 
document.getElementById("eMeng").style.left =
parseInt(document.body.scrollLeft,10) + docWidth - divWidth 
document.getElementById("eMeng").style.visibility="visible" 
objTimer = window.setInterval("moveDiv()",10) 
} 
catch(e){} 
} 

function resizeDiv() 
{ 
i+=1 
if(i>500) closeDiv() 
try{ 
divHeight = parseInt(document.getElementById("eMeng").offsetHeight,10) 
divWidth = parseInt(document.getElementById("eMeng").offsetWidth,10) 
docWidth = document.body.clientWidth; 
docHeight = document.body.clientHeight; 
document.getElementById("eMeng").style.top = docHeight - divHeight +
parseInt(document.body.scrollTop,10) 
document.getElementById("eMeng").style.left = docWidth - divWidth +
parseInt(document.body.scrollLeft,10) 
} 
catch(e){} 
} 

function moveDiv() 
{ 
try 
{ 
if(parseInt(document.getElementById("eMeng").style.top,10) <= (docHeight -
divHeight + parseInt(document.body.scrollTop,10))) 
{ 
window.clearInterval(objTimer) 
objTimer = window.setInterval("resizeDiv()",1) 
} 
divTop = parseInt(document.getElementById("eMeng").style.top,10) 
document.getElementById("eMeng").style.top = divTop - 1 
} 
catch(e){} 
} 
function closeDiv() 
{ 
document.getElementById('eMeng').style.visibility='hidden'; 
if(objTimer) window.clearInterval(objTimer) 
} 
</script>
<div id=eMeng style="BORDER-RIGHT: #455690 1px solid; BORDER-TOP: #a6b4cf
1px solid; Z-INDEX:0; LEFT: 0px; VISIBILITY: hidden; BORDER-LEFT: #a6b4cf
1px solid; WIDTH: 180px; BORDER-BOTTOM: #455690 1px solid; POSITION:
absolute; TOP: 0px; HEIGHT: 116px; BACKGROUND-COLOR: #c9d3f3"> 
<table style="BORDER-TOP: #ffffff 1px solid; BORDER-LEFT: #ffffff 1px
solid" cellspacing=0 cellpadding=0 width="100%" bgcolor=#E7EFF7 border=0>
<tbody>
<tr> 
<td style="FONT-SIZE: 12px; BACKGROUND-IMAGE: url(msgTopBg.gif); COLOR:
#0f2c8c" width=30 height=24></td>
<td width="100%" valign=center style="FONT-WEIGHT: normal; FONT-SIZE: 14px;
BACKGROUND-IMAGE: url(msgTopBg.gif); COLOR: #0855AD; PADDING-TOP:
4px;PADDING-left: 4px"> 
<div align="center">&nbsp;&nbsp;&nbsp;&nbsp;最近更新</div></td>
<td style="BACKGROUND-IMAGE: url(msgTopBg.gif); PADDING-TOP:
2px;PADDING-right:2px" valign=center align=right width=19><span title=关闭
style="CURSOR:
hand;color:#FF8A00;font-size:12px;font-weight:bold;margin-right:4px;"
onClick=closeDiv() >×</span> 
<!-- <IMG title=??±? style="CURSOR: hand" onclick=closeDiv() hspace=3
src="msgClose.jpg"> -->
</td>
</tr>
<tr> 
<td style="PADDING-RIGHT: 1px; BACKGROUND-IMAGE:
url(/oblog/1msgBottomBg.jpg); PADDING-BOTTOM: 1px" colspan=3 height=90
align="center"> 
<div style="BORDER-RIGHT: #9C9E9C 1px solid; PADDING-RIGHT: 13px;
BORDER-TOP: #9C9E9C 1px solid; PADDING-LEFT: 13px; FONT-SIZE: 12px;
PADDING-BOTTOM: 13px; BORDER-LEFT: #9C9E9C 1px solid; WIDTH: 95%; COLOR:
#1f336b; PADDING-TOP: 18px; BORDER-BOTTOM: #9C9E9C 1px solid; HEIGHT:
100%;background-color:white">3??10?? *Flash×÷??* <br> ?í??????????<br>3??15
?? *??????* <br>???????????????? ????13??????<br>3??19?? *????×÷??* <br>????
????×÷??
</div></td>
</tr>
<tr >
<td height=3 colspan=3 ></td>
</tr>
</tbody>
</table>
</div>
<!-- ?á?? ?ü???????°?? -->
