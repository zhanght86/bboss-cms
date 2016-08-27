<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="java.util.*"%>

<%
		AccessControl accesscontroler = AccessControl.getInstance();
		accesscontroler.checkAccess(request, response);
		//本周的起始时间
	String start_weekDate;
	java.text.SimpleDateFormat   df=new   java.text.SimpleDateFormat("yyyy-MM-dd");   
    Calendar   calendar=Calendar.getInstance();   
    Calendar   cpcalendar=(Calendar)calendar.clone(); 
        
    Date currentDate = new Date(); 
    /**当前时间*/ 
    String riqi = df.format(currentDate); 
    //得到本周一的时间
    cpcalendar.set(Calendar.DAY_OF_WEEK,   Calendar.MONDAY); 
    start_weekDate = df.format(new Date(cpcalendar.getTimeInMillis()));
%>
<html>
	<frameset rows="120,*" border=0>
	<frame frameborder=0  noResize marginwidth="0" scrolling="auto"  name="forQuery" src="channelStatistic_query.jsp"></frame>	
	<frame frameborder=0  noResize  marginwidth="0" scrolling="auto"  name="forList" src="channelStatistic_list.jsp?startDate=<%=start_weekDate%>&endDate=<%=riqi%>"></frame>
	</frameset>
</html>
