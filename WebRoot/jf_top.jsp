<%@ page session="true" language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.*"%>
<%@ page import="com.frameworkset.platform.tools.TimeHelper"%>
<%@ page import="com.frameworkset.platform.integralwarning.assistjudge.dao.AssistJudeDao"%>
<%@ page import="com.frameworkset.platform.integralwarning.assistjudge.dao.AssistJudeDaoImpl"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
	
	Calendar   cal   =   Calendar.getInstance();
    int   year   =   cal.get(Calendar.YEAR);
    int   month   =   cal.get(Calendar.MONTH)   +   1;
    int   day   =   cal.get(Calendar.DATE); 
    //String theDay = TimeHelper.getCurrentDateHasDelay(TimeHelper.JAVA_DATE_FORAMTER_1); 
	AssistJudeDao jude = new AssistJudeDaoImpl(); 
	request.setAttribute("list",jude.getTopEntityList());
%>	
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>�ޱ����ĵ�</title>
<link href="common/css/jf.css" rel="stylesheet" type="text/css" /> 
<link href="common/css/grxx.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/common/scripts/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/include/swfobject.js"></script>
<script language="JavaScript" src="FusionCharts/JSClass/FusionCharts.js"></script>
<style type="text/css">
#jd_shadow{z-index:999;position:absolute;background-color:#000000;filter:alpha(opacity=25);-moz-opacity:0.25;opacity:0.25;}
#jd_dialog{z-index:1000;position:absolute;}
#jd_dialog_s{position:absolute;top:5px;left:5px;background-color:#000000;filter:alpha(opacity=40);-moz-opacity:0.4;opacity:0.4;}
#jd_dialog_m{position:absolute;}
</style>
<script type="text/javascript">
	var gDialogImgFolder = "<%=basePath%>/common/scripts/dialog/img";
</script>
<script type="text/javascript" src="<%=basePath%>/common/scripts/dialog/jquery_dialog.js"></script>
<script type="text/javascript">
//$(document).ready(function() {
//	var url = "<%=basePath%>/warningmodel/chart1.action";
//	swfobject.embedSWF("include/open-flash-chart.swf",
//					   "chart1",
//					   "312",
//					   "178",
//					   "9.0.0",
//					   "expressInstall.swf", 
//					   {"data-file" : url}
					 //  {"data-file" : "data-files/x-axis-steps-zero-check.txt"}
//		   			  );
//});
function zjfankui(){
		window.open('<%=basePath%>/zjfk/zjfk!openPage.action', 740, 540);
	}
	function closeWindow() {
		JqueryDialog.Close();
	}
	function closeOpen(){
		window.close();
	}
function t(){
	var topImg = ['jf_03.jpg','jf_03_2.jpg','jf_03_3.jpg','jf_03_4.jpg','jf_03_5.jpg','jf_03_6.jpg','jf_03_7.jpg'];
	var topElement = document.getElementsByName("top");
	for(var i =0;i<topElement.length;i++){
		topElement[i].src="images/"+topImg[i];
	}
}
function showWindow(idV,cardno) {
	var wHeight = window.screen.height-100;
	var wWidth = window.screen.width-20;
	//��ȡ��Ļ�ֱ���
	var param = 'height=' + wHeight + ',' + 'width=' + wWidth
			+ ',top=0, left=50,status=no,toolbar=no,menubar=no,location=no,resizable=yes,scrollbars=yes';
	var milliseconds = new Date();
	window.open('openInit.action?summryId='+idV+'&cardno='+cardno+"&dateflag="+milliseconds.getTime(), '_blank', param);
		//JqueryDialog.Open('��ϸ��Ϣ', 'assistjudge_main.jsp?cardno='+cardno+'&modelId='+idV+'&batchNo='+batch_no, 920, 520);
	}

function openjudge(objvalue){
    if(objvalue=="1"){
        var url="http://10.142.55.89:8080/qbpt/base/commonForward.action?jh=160015&menuid=560101&yjjb=1";
    	window.open(url,'_blank','menubar=no,status=no,toolbar=no,width=800,height=600'); 
     }else if(objvalue=="2"){
       var url="http://10.142.55.89:8080/qbpt/base/commonForward.action?jh=160015&menuid=560101&yjjb=2";
       window.open(url,'_blank','menubar=no,status=no,toolbar=no,width=800,height=600'); 
     }else if(objvalue=="3"){
      var url="http://10.142.55.89:8080/qbpt/base/commonForward.action?jh=160015&menuid=560101&yjjb=3";
       window.open(url,'_blank','menubar=no,status=no,toolbar=no,width=800,height=600'); 
     }else if(objvalue=="4"){
     	var url="http://10.142.55.89:8080/qbpt/base/commonForward.action?jh=160015&link=ckyjqs&bh=43310010070000022905";
        window.open(url,'_blank','menubar=no,status=no,toolbar=no,width=800,height=600'); 
     }
}
function djs(time,obj,inttime){
var timearry = time.split(":");
var timeO = timearry[0];
var mino= timearry[1];
if(parseInt(mino,10)==0 && parseInt(timeO,10)>=0){
timeO = parseInt(timeO,10)-1;
mino = 60-1;
}else if(parseInt(timeO,10)>=0&&parseInt(mino,10)>0){
mino = parseInt(mino,10)-1;
}
if(parseInt(timeO,10)==0&& parseInt(mino,10)==0){
obj.html("00:00");
if(inttime!=0){
clearInterval(inttime);
}

}
obj.html(chagenum(timeO)+":"+chagenum(mino));
}
function chagenum(num){
if((num+"").length<2){
return "0"+num;
}
return num+"";
}

</script>
</head>

<body valign="top">
<table width="100%" align="center">
  <tr>
    <td valign="top">
    <table width="100%" border="0" cellspacing="0">
      <tr>
        <td width="48%" valign="top"><table width="100%" border="0" align="center" cellspacing="0">
          <tr class="jfxl">
            <td class="c2" >���칲����Ԥ��20��</td>
          </tr>
          <tr>
			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="center" class="yjcolor1" style="cursor: pointer;" onclick="openjudge(1)  ">��ɫԤ����10��</td>
    <td width="10"></td>
    <td align="center" class="yjcolor2" style="cursor: pointer;"  onclick="openjudge(2)">��ɫԤ����5��</td>
    <td width="10"></td>
    <td align="center" class="yjcolor3">��ɫԤ����0��</td>
    <td width="10"></td>
    <td align="center" class="yjcolor4" style="cursor: pointer;"  onclick="openjudge(3)">��ɫԤ����5��</td>
    <td>&nbsp;</td>
  </tr>
</table>
          </tr>
          <tr>
            <td valign="top"><table width="99%" border="0" align="center" cellspacing="0" class="jfxl2">
              <tr>
                <td>���й������·�Ԥ��5����ʡ����Ԥ������Ԥ����Ϣ20��<br />
                  Ŀǰ�ѽ�����в��·�ָ���Ԥ��4�������д�ǩ��2��������2�����Ѵ������0����<br />
                  ����16���辡�����д���</td>
              </tr>
            </table></td>
          </tr>
          <tr>
            <td valign="top">&nbsp;</td>
          </tr>
        </table>
          <table width="100%" border="0" align="right" cellspacing="0">
            <tr class="jfxl">
              <td width="91%"  class="c2">�ص��ע��Ա</td>
              <td width="9%" class="c1" ><a href="javascript:void(0);">���&gt;&gt;</a></td>
            </tr>
            <tr>
              <td colspan="2" ><table width="99%" border="0" align="center" cellspacing="0" class="tox">
                <tr class="jfxl3">
                  <td width="8%" align="center">����</td>
                  <td width="14%" align="center">����</td>
                  <!--<td width="22%" align="center">���֤��</td>-->
                  <td width="8%" align="center">���</td>
                  <td width="16%" align="center">��Ա����</td>
                  <td align="center" width="16%">���ý���</td>
                  <td width="15%" align="center">����</td>
                </tr>
                <s:iterator value="#request.list">
                		<tr class="jfxl4"> 
		                  <td align="center"><img src="images/jf_03.jpg" width="34" height="20" name="top"/></td>
		                  <td align="center"><s:property value="xm"/></td>
		                  <!--<td align="center"><s:property value="cardno"/></td>-->
		                  <td align="center"><s:property value="summary_value"/></td>
		                  <td align="center"><s:property value="mc"/></td>
    <s:if test="warn_name==''"><td class="yjcolor18" align="center" valign="top"><s:property value="cljy"/></td></s:if>
    <s:if test="warn_name=='��Ԥ��'">
      <td  class="yjcolor18" align="left" valign="top"><span style="margin-top:0px;">&nbsp;&nbsp;&nbsp;&nbsp;��Ԥ��</span></td></s:if>
    <s:if test="warn_name=='��ɫԤ��'"><td class="yjcolor16" align="left" valign="top">&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="cljy"/></td></s:if>
    <s:if test="warn_name=='��ɫԤ��'"><td class="yjcolor11" align="left" valign="top">&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="cljy"/></td></s:if>
    <s:if test="warn_name=='��ɫԤ��'"><td class="yjcolor13" align="left" valign="top">&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="cljy"/></td></s:if>
    <s:if test="warn_name=='��ɫԤ��'"><td class="yjcolor14" align="left" valign="top">&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="cljy"/></td></s:if>

		                  <td align="center"><img src="images/jf_07.jpg" width="22" height="23" /><a href="javascript:showWindow('<s:property value="id" />','<s:property value="cardno" />')"">����</a></td>
		                </tr>
                </s:iterator>
                
              
              </table></td>
            </tr>
          </table></td>
        <td width="52%" valign="top"><table width="92%" border="0" align="center" cellspacing="0" class="tox1">
          <tr>
            <td align="center" valign="top"><table width="98%" border="0" cellspacing="0">
              <tr>
                <td align="left" class="t1">����ǩ�ճ�ʱԤ����Ϣ2��</td>
              </tr>
              <tr>
                <td valign="top"><table width="80%" align="center" class="tox">
                  <tr>
                    <td class="t2">��ɫԤ��</td>
                    <td class="t2">��xx </td>
                    <td class="t2">ץ��</td>
                  <td class="t3" id="tdtime1">10:00</td>
                  <td class="t3"><button type="button" class="button5" onclick="openjudge(4)">ǩ��</button></td>
                  </tr>
                  <tr>
                    <td class="t2">��ɫԤ��</td>
                    <td class="t2">��xx </td>
                    <td class="t2">ץ��</td>
                    <td class="t3" id="tdtime2">08:30</td>
                    <td class="t3"><button type="button" class="button5" onclick="openjudge(4)">ǩ��</button></td>
                  </tr>
                  
                </table></td>
              </tr>
              <tr>
                <td height="1" bgcolor="#C5D2DA"></td>
              </tr>
              <tr>
                <td align="left"><span class="t1">����������ʱԤ����Ϣ2��</span></td>
              </tr>
              <tr>
                <td><table width="80%" align="center" class="tox">
                  <tr>
                    <td class="t2">��ɫԤ��</td>
                    <td class="t2">��xx </td>
                    <td class="t2">ץ��</td>
                   <td class="t3" id="tdtime3">10:50</td>
                   <td class="t3"><button type="button" class="button5" onclick="zjfankui()">����</button></td>
                  </tr>
                  <tr>
                    <td class="t2">��ɫԤ��</td>
                    <td class="t2">��xx </td>
                    <td class="t2">ץ��</td>
                    <td class="t3" id="tdtime4">08:00</td>
                    <td class="t3"><button type="button" class="button5" onclick="zjfankui()">����</button></td>
                  </tr>
                </table></td>
              </tr> 
            </table></td>
          </tr>
        </table>
          <table width="92%" border="0" align="center" cellspacing="0" class="tox1">
            <tr>
              <td align="center" valign="top"><table width="98%" border="0" cellspacing="0">
                  <tr>
                    <td align="left" class="t1">��ɫԤ���ȶ�ͼ</td>
                  </tr>
                  <tr>
                    <td valign="top" aling='center'>
                    <!--  <div id="chart1"></div>-->
                     <div id="chartdiv" align="center">FusionCharts. </div>
     			   	<script type="text/javascript">
			   			var chart = new FusionCharts("FusionCharts/Charts/Pie3D.swf", "ChartId", "420", "200", "0", "0");
			   			chart.setDataURL("FusionCharts/Gallery/Data/Pie3D_M.xml");		   
			   			chart.render("chartdiv");
					</script> 
                    <!--<img src="images/jf_09.jpg" width="312" height="151" />
                    --></td>
                  </tr>
              </table></td>
            </tr>
          </table></td>
      </tr>
    </table>
    </td>
  </tr>
</table>
</body>
</html>
<script type="text/javascript">
var inttime1 =0;
var inttime2 =0;
var inttime3 =0;
var inttime4 =0;
function time1ds(){
var tdtime1 = $("#tdtime1");
djs(tdtime1.html(),tdtime1,inttime1);
}
function time2ds(){
var tdtime2 = $("#tdtime2");
djs(tdtime2.html(),tdtime2,inttime2);
}
function time3ds(){
var tdtime3 = $("#tdtime3");
djs(tdtime3.html(),tdtime3,inttime3);
}
function time4ds(){
var tdtime4 = $("#tdtime4");
djs(tdtime4.html(),tdtime4,inttime4);
}
if(inttime1==0){
inttime1 = setInterval("time1ds()", 1000);
}
if(inttime2==0){
inttime2 = setInterval("time2ds()", 1000);
}
if(inttime3==0){
inttime3 = setInterval("time3ds()", 1000);
}
if(inttime4==0){
inttime4 = setInterval("time4ds()", 1000);
}
t();
</script>


