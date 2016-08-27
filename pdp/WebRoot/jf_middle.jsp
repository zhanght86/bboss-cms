<%@ page session="true" language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
	
	Calendar   cal   =   Calendar.getInstance();
    int   year   =   cal.get(Calendar.YEAR);
    int   month   =   cal.get(Calendar.MONTH)   +   1;
    int   day   =   cal.get(Calendar.DATE); 
    String theMonth = TimeHelper.getCurrentDateHasDelay(TimeHelper.JAVA_DATE_FORAMTER_3); 
    String theDay = TimeHelper.getCurrentDateHasDelay(TimeHelper.JAVA_DATE_FORAMTER_1); 
	
%>	
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>无标题文档</title>
<link href="common/css/jf.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="common/scripts/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="include/swfobject.js"></script>
<script language="JavaScript" src="FusionChartsNew/JSClass/FusionCharts.js"></script>
<script type="text/javascript" language="javascript">

/*$(document).ready(function() {
	var url = "<%=basePath%>/warningmodel/chart2.action";
	swfobject.embedSWF("include/open-flash-chart.swf",
					   "chart",
					   "435",
					   "201",
					   "9.0.0",
					   "expressInstall.swf", 
					   {"data-file" : url}
					   
		   			  );
});*/
</script>
<%@ page import="java.util.*"%>
<%@ page import="com.frameworkset.platform.tools.TimeHelper"%>
</head>
<body valign="top">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td>
    <table width="98%" border="0" align="left" cellspacing="0" class="tox1">
      <tr>
        <td align="center" valign="top"><table width="100%" border="0" cellspacing="0">
          <tr>
            <td width="14%" align="center" bgcolor="#43a9ea" class="t4 lmbg" >积分模型情况</td>
            <td width="86%" bgcolor="#f0f4f5" class="lmbg2">&nbsp;</td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td align="center" valign="top"><table width="100%">
          <tr>
            <td width="50%"><table width="98%" border="0" cellspacing="0">
              <tr>
                <td height="26" align="left" valign="bottom" >积分模型有效情况（<%=theMonth+"-01" %>到<%=theDay%>）</td>
              </tr>
              <tr>
                <td width="50%" valign="top"><table width="99%" border="0" align="center" cellspacing="0" class="jfxl2">
                    <tr>
                      <td><table width="99%" border="0" align="center" cellspacing="0" class="tox">
                          <tr class="jfxl3">
                            <td width="32%" align="left">积分模型名称</td>
                            <td width="16%" align="center">产生预警数</td>
                            <td width="17%" align="center">有效预警数</td>
                            <td width="10%" align="center">有效比</td>
                            <td width="10%" align="center">查看</td>
                          </tr>
                          <tr class="jfxl4">
                            <td align="left">涉毒人员积分模型</td>
                            <td align="center">40</td>
                            <td align="center">20</td>
                            <td align="center">50%</td>
                            <td align="center"><img src="images/jf_16.jpg" width="12" height="12" /></td>
                          </tr>
                          <tr class="jfxl4">
                            <td align="left">制贩毒人员积分模型</td>
                            <td align="center">100</td>
                            <td align="center">20</td>
                            <td align="center">20%</td>
                            <td align="center"><img src="images/jf_19.jpg" width="12" height="12" /></td>
                          </tr>
                          <tr class="jfxl4">
                            <td align="left">重大刑事犯罪前科积分模型</td>
                            <td align="center">45</td>
                            <td align="center">9</td>
                            <td align="center">20%</td>
                            <td align="center"><img src="images/jf_16.jpg" width="12" height="12" /></td>
                          </tr>
                          <tr class="jfxl4">
                            <td align="left">重大经济犯罪前科积分模型</td>
                            <td align="center">10</td>
                            <td align="center">5</td>
                            <td align="center">50%</td>
                            <td align="center"><img src="images/jf_16.jpg" width="12" height="12" /></td>
                          </tr>

                      </table></td>
                    </tr>
                </table></td>
              </tr>
            </table></td>
            <td valign="top"><table width="85%" border="0" align="center" cellspacing="0">
              <tr>
                <!-- <td width="82%" height="26" align="left" valign="bottom" >积分模型有效情况对比图</td>
                <td width="18%" align="left" valign="bottom"></td> -->
              </tr>
              
              <tr>
                <!-- <td colspan="2" align="left" valign="top">
                	<div id="chart"></div>
                </td> -->
                <td colspan="2" align="center" valign="top">
                   <div id="chartdiv" align="center">FusionCharts. </div>
     			   <script type="text/javascript">
			   			var chart = new FusionCharts("FusionChartsNew/Charts/MSColumn2D_A.swf", "ChartId", "435", "220", "0", "0");
			   			chart.setDataURL("FusionChartsNew/Gallery/Data/MSColumn2D_a.xml");		   
			   			chart.render("chartdiv");
					</script> 
				</td> 
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

