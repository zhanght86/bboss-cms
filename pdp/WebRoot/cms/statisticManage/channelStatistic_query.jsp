<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.*"%>
<%@page import=" java.text.DateFormat,
 java.text.SimpleDateFormat,
 java.util.Calendar,
 java.util.Date"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	
	
	//本周的起始时间
		String start_weekDate;
		//本月的起始时间
		String start_monthDate;
		//本季的起始时间
		String start_quarterDate;
		//本年的起始时间
		String start_yearDate;
		//当前的时间
		String riqi ;
		
		java.text.SimpleDateFormat   df=new   java.text.SimpleDateFormat("yyyy-MM-dd");   
        Calendar   calendar=Calendar.getInstance();   
        Calendar   cpcalendar=(Calendar)calendar.clone(); 
        
        Date currentDate = new Date(); 
        /**当前时间*/ 
        riqi = df.format(currentDate); 
             
        //得到当月,因一月为零，所以要加1
        int month = currentDate.getMonth()+1;
        
        
        //得到本周一的时间
        cpcalendar.set(Calendar.DAY_OF_WEEK,   Calendar.MONDAY); 
        start_weekDate = df.format(new Date(cpcalendar.getTimeInMillis()));
        
        //得到本月的起始时间         
        cpcalendar.set(Calendar.DAY_OF_MONTH,   1);
        start_monthDate = df.format(new Date(cpcalendar.getTimeInMillis()));
        
        //得到本季的起始时间
        
        //如果现在属于第一季度
        if(month>=1 && month<=3){
        	cpcalendar.set(Calendar.MONTH,   0);
            start_quarterDate = df.format(new Date(cpcalendar.getTimeInMillis()));
        	//start_quarterDate = year+"-01-01";
        }
        //如果现在属于第二季度
        else if(month>=4 && month<=6){
        	cpcalendar.set(Calendar.MONTH,   3);
            start_quarterDate = df.format(new Date(cpcalendar.getTimeInMillis()));
        }
        //如果现在属于第三季度
        else if(month>=7 && month<=9){
        	cpcalendar.set(Calendar.MONTH,   6);
            start_quarterDate = df.format(new Date(cpcalendar.getTimeInMillis()));
        }
        //如果现在属于第四季度
        else {
        	cpcalendar.set(Calendar.MONTH,   9);
            start_quarterDate = df.format(new Date(cpcalendar.getTimeInMillis()));
        }
        
        //得到本年的起始时间
        cpcalendar.set(Calendar.DAY_OF_YEAR,   1);
        start_yearDate = df.format(new Date(cpcalendar.getTimeInMillis()));
       
 %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>用户统计查询</title>
<script src="../inc/js/func.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/public/datetime/calender_date.js" ></script>
<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
<script language="JavaScript" src="../../sysmanager/scripts/selectTime.js" type="text/javascript"></script>
<script type="text/javascript" src="../../public/datetime/calender.js" language="javascript"></script>
<script type="text/javascript" src="../../public/datetime/calender_date.js" language="javascript"></script>
<script language="javascript">
	function query(){	
		//查询
		if(form1.startDate.value>form1.endDate.value)
		{
			alert("开始时间不能大于结束时间！");
			return;
		}
		form1.action="channelStatistic_list.jsp?startDate="+form1.startDate.value+"&endDate="+form1.endDate.value+"&maxPageItems="+form1.maxPageItems.value;
		form1.target="forList";
		form1.submit();	
	}
	function changeDate()
	{	//更改时间段
		//本周
		
		if(form1.dateType.value==1){
			form1.startDate.value="<%=start_weekDate%>";
			form1.endDate.value="<%=riqi%>";
			return;
		}
		//本月
		else if(form1.dateType.value==2){
			form1.startDate.value="<%=start_monthDate%>";
			form1.endDate.value="<%=riqi%>";
			return;
		}
		//本季
		else if(form1.dateType.value==3){
			form1.startDate.value="<%=start_quarterDate%>";
			form1.endDate.value="<%=riqi%>";
			return;
		}
		//本年
		else if(form1.dateType.value==4){
			form1.startDate.value="<%=start_yearDate%>";
			form1.endDate.value="<%=riqi%>";
			return;
		}
		
	}
	
	function doprint(){
		form1.action="channelStatistic_print.jsp?startDate="+form1.startDate.value+"&endDate="+form1.endDate.value+"&maxPageItems="+form1.maxPageItems.value;
		form1.target="_blank";
		form1.submit();
	}
</script>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
</head>
<body>
<form id="form1" name="form1" method="post">
<table width="95%" height="95%" border="0" cellpadding="0" cellspacing="0" align=center>
  <tr>
     <td colspan="3">
						<STRONG><FONT size="3" >频道统计</FONT></STRONG>
					</td>
  </tr>
  <tr>
    <td width="5" valign="top"><img src="../images/querybox_left.gif" width="5" height="62"></td>
    <td  style="background:url(../images/querybox_bg.gif) repeat-x top">
	<table width="100%" border="0" cellpadding="3" cellspacing="0" style="margin-top:5px"   class="query_table">
		<tr>
			<td  height='30' align="left" nowrap="nowrap" colspan="2">
				类型：
				<select name="dateType" class="cms_select" style="width:120" onChange="changeDate()">					
					<option value="1">本周</option>
					<option value="2">本月</option>
					<option value="3">本季</option>
					<option value="4">本年</option>
				</select>			          
			</td>
			<td  height='30' align="left" nowrap="nowrap" colspan="2">
				每页显示条数：
				<input name="maxPageItems" type="text" size="2" value="10">	          
			</td>
		</tr>
		<tr>
			<td  >请输入要查询的时间段：</td>
        	<td  >
            	 开始时间：<input type="text" name="startDate" onclick="showdate(document.all('startDate'))" readonly="true" value="<%=start_weekDate%>" validator="stringNull" cnname="开始时间" maxlength="40">
            </td>
            <td>
            	结束时间：<input type="text" name="endDate" onclick="showdate(document.all('endDate'))" readonly="true" value="<%=riqi%>" validator="stringNull" cnname="结束时间" maxlength="40">
            </td>
            <td>            
				<input name="search" type="button" class="cms_button" value="查询" onClick="query()">&nbsp;&nbsp;
				<input name="print" type="button" class="cms_button" value="打印" onClick="doprint()">
			</td>
        </tr>
	</table>
	</td>
    <td width="6" valign="top"><img src="../images/querybox_right.gif" width="6" height="62"></td>
  </tr>
</table>
</form>
</body>
</html>