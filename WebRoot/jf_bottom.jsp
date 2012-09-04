<%@ page session="true" language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="com.frameworkset.common.poolman.SQLExecutor"%>
<head>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>	
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>无标题文档</title>
<link href="common/css/jf.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/common/scripts/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/include/swfobject.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	var url = "<%=basePath%>/warningmodel/chart3.action";
	swfobject.embedSWF("include/open-flash-chart.swf",
					   "chart3",
					   "435",
					   "198",
					   "9.0.0",
					   "expressInstall.swf", 
					   {"data-file" : url}
					 //  {"data-file" : "data-files/x-axis-steps-zero-check.txt"}
		   			  );
});
</script>
</head>

<body>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td>
    <table width="98%" border="0" align="left" cellspacing="0" class="tox1">
      <tr>
        <td align="center" valign="top"><table width="100%" border="0" cellspacing="0">
            <tr>
               <td width="14%" align="center" bgcolor="#43a9ea"  class="t4 lmbg" >积分预警趋势情况</td>
              <td width="86%" bgcolor="#f0f4f5" class="lmbg2">&nbsp;</td>
            </tr>
        </table></td>
      </tr>
      <tr>
        <td align="center" valign="top"><table width="100%">
            <tr>
              <td width="50%"><table width="98%" border="0" cellspacing="0">
                  <tr>
                    <%
                    DecimalFormat df1 = new DecimalFormat("##.00%"); 
                    Calendar   cal   =   Calendar.getInstance();
                    //int   year   =   cal.get(Calendar.YEAR);
                    int   month   =   cal.get(Calendar.MONTH)   +   1;
                    int   day   =   cal.get(Calendar.DATE);  
                    //String theMonth = month<10?"0"+month:String.valueOf(month);
                    //String theLastMonth = month-1<10?"0"+(month-1):String.valueOf(month-1);
                    String year = "2011";
                    String theMonth = "02";
                    String theLastMonth = "01";
                    
                	String map1 = SQLExecutor.queryField("select count(type) count from TD_TEST_WARNING where type=? and to_char(warn_time,'yyyy-mm')=?", "1",year+"-"+theMonth);
                	String map2 = SQLExecutor.queryField("select count(type) count from TD_TEST_WARNING where type=? and to_char(warn_time,'yyyy-mm')=?", "2",year+"-"+theMonth);
                	String map3 = SQLExecutor.queryField("select count(type) count from TD_TEST_WARNING where type=? and to_char(warn_time,'yyyy-mm')=?", "3",year+"-"+theMonth);
                	String map4 = SQLExecutor.queryField("select count(type) count from TD_TEST_WARNING where type=? and to_char(warn_time,'yyyy-mm')=?", "4",year+"-"+theMonth);
                    
                	String map11 =  SQLExecutor.queryField("select count(type) count from TD_TEST_WARNING where type=? and to_char(warn_time,'yyyy-mm')=?", "1",year+"-"+theLastMonth);
                	String map22 =  SQLExecutor.queryField("select count(type) count from TD_TEST_WARNING where type=? and to_char(warn_time,'yyyy-mm')=?","2",year+"-"+theLastMonth);
                	String map33 =  SQLExecutor.queryField("select count(type) count from TD_TEST_WARNING where type=? and to_char(warn_time,'yyyy-mm')=?", "3",year+"-"+theLastMonth);
                	String map44 = SQLExecutor.queryField("select count(type) count from TD_TEST_WARNING where type=? and to_char(warn_time,'yyyy-mm')=?", "4",year+"-"+theLastMonth);
                    
                	double warn1 = Double.parseDouble(map1);
                	double warn11 = Double.parseDouble(map11);
                	double warn2 = Double.parseDouble(map2);
                	double warn22 = Double.parseDouble(map22);
                	double warn3 = Double.parseDouble(map3);
                	double warn33 = Double.parseDouble(map33);
                	double warn4 = Double.parseDouble(map4);
                	double warn44 = Double.parseDouble(map44);
                    %>
                    <td height="26" align="left" valign="bottom">分色预警情况（<%=month%>月）</td>
                  </tr>
                  <tr>
                    <td width="50%" valign="top"><table width="99%" border="0" align="center" cellspacing="0" class="jfxl2">
                        <tr>
                          <td><table width="99%" border="0" align="center" cellspacing="0" class="tox">
                              <tr class="jfxl3">
                                <td width="17%" align="center">预计类型</td>
                                <td width="16%" align="center">本月累计</td>
                                <td width="17%" align="center">上月同期</td>
                                <td width="15%" align="center">比上月同期</td>
                                <td width="10%" align="center">查看</td>
                              </tr>
                              <tr class="jfxl4">
                                <td align="center">红色预警</td>
                                <td align="center"><%=(int)(Double.parseDouble(map1))%></td>
                                <td align="center"><%=(int)(Double.parseDouble(map11))%></td>
                                <td align="center">
                                	<%if((int)(Double.parseDouble(map11))==0){
                                	 %>
                                	 &nbsp; 	
                                	<%}else{%>
                                	<%=df1.format((warn1-warn11)/warn11)%>	
                                	<%} %>
                                </td>
                                <td align="center"><img src="images/jf_16.jpg" width="12" height="12" /></td>
                              </tr>
                              <tr class="jfxl4">
                                <td align="center">橙色预警</td>
                                <td align="center"><%=(int)(Double.parseDouble(map2))%></td>
                                <td align="center"><%=(int)(Double.parseDouble(map22))%></td>
                                <td align="center">
									<%if((int)(Double.parseDouble(map22))==0){
                                	 %>
                                	 &nbsp; 	
                                	<%}else{%>
									<%=df1.format((warn2-warn22)/warn22)%>	
                                	<%} %>
								</td>
                                <td align="center"><img src="images/jf_19.jpg" width="12" height="12" /></td>
                              </tr>
                              <tr class="jfxl4">
                                <td align="center">黄色预警</td>
                                <td align="center"><%=(int)(Double.parseDouble(map3))%></td>
                                <td align="center"><%=(int)(Double.parseDouble(map33))%></td>
                                <td align="center">
                                	<%if((int)(Double.parseDouble(map33))==0){
                                	 %>
                                	 &nbsp; 	
                                	<%}else{%>
                                	<%=df1.format((warn3-warn33)/warn33)%>	
                                	<%} %>
                                </td>
                                <td align="center"><img src="images/jf_16.jpg" width="12" height="12" /></td>
                              </tr>
                              <tr class="jfxl4">
                                <td align="center">蓝色预警</td>
                                <td align="center"><%=(int)(Double.parseDouble(map4))%></td>
                                <td align="center"><%=(int)(Double.parseDouble(map44))%></td>
                                <td align="center">
                                	<%if((int)(Double.parseDouble(map44))==0){
                                	 %>
                                	 &nbsp; 	
                                	<%}else{%>
                                	<%=df1.format((warn4-warn44)/warn44)%>	
                                	<%} %>
                                </td>
                                <td align="center"><img src="images/jf_16.jpg" width="12" height="12" /></td>
                              </tr>
                          </table></td>
                        </tr>
                    </table></td>
                  </tr>
              </table></td>
              <td valign="top"><table width="85%" border="0" align="center" cellspacing="0">
                  <tr>
                    <td colspan="2" width="82%" height="26" align="center" valign="bottom" style="font-weight: bold">分色预警趋势图</td>
                    <!-- <td width="18%" align="left" valign="bottom"></td> -->
                  </tr>
                  <tr>
                    <td colspan="2" align="center" valign="top">
                    <div id="chart3" align="center"></div>
                    <!--<img src="images/jf_22.jpg" width="435" height="198" /></td>
                  --></tr>
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

