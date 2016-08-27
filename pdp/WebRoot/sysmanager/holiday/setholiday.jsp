<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.holiday.*"%>
<%@ page import="java.util.*,com.frameworkset.util.StringUtil"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    String syear = StringUtil.replaceNull(request.getParameter("year"));
    String smonth = StringUtil.replaceNull(request
            .getParameter("month"));
    Date thisd = new Date();
    int year = 0;
    int month = 0;

    if (syear.equals(""))
        year = thisd.getYear() + 1900;
    else
        year = Integer.parseInt(syear);
    if (smonth.equals(""))
        month = thisd.getMonth() + 1;
    else
        month = Integer.parseInt(smonth);
        
	//当前用户是否拥有超级管理员权限与部门管理员权限
	boolean isAdminOrOrgManager = false;
	//是否是管理员
	boolean isOrgManager = new OrgAdministratorImpl().isOrgManager(accesscontroler.getUserID());
	boolean isAdmin = accesscontroler.isAdmin();
	if(isAdmin || isOrgManager){
		isAdminOrOrgManager = true;
	}	
           
%>
<title></title>

<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<script language="JavaScript" src="../scripts/common.js" type="text/javascript"></script>


<script language="JavaScript">


    function cmdOK_onclick()
    {
        document.dateform.submit();
    }
    
   function setholiday()
   {
    	document.setform.action.value=1;
    	document.setform.submit();
   }
   
    function mholiday(){
		document.setform.action.value=2;
		document.setform.submit();
   }
   
   function yearHoliday(){
       document.setform.action.value=3;
	   document.setform.submit();
   }
   
</script>
<body class="contentbodymargin">
<link rel="stylesheet" type="text/css" href="../../<%=session.getAttribute("current_cssname")%>">
<%if(isAdminOrOrgManager){ %>
<TABLE width="100%" cellpadding="1" cellspacing="1" bordercolor="#EEEEEE" class="thin">
	<tr align="center" height="20px" class="labeltable_middle_td">
		<td colspan="4">
			工作日列表（打勾为假日）
		</td>
	</tr>
	<form name="dateform" action="setholiday.jsp">
		<input type="hidden" name="action">
	<tr>

		<td align="center" colspan="4">
			<select NAME=year>
				<%int iyear = 2004;
            for (int i = 0; i < 10; i++)
            {
                if (year == iyear)
                {

                    %>
				<option value="<%=year%>" selected>
					<%=year%>
				</option>
				<%} else
                {

                    %>
				<option value="<%=iyear%>">
					<%=iyear%>
				</option>
				<%}
                iyear = iyear + 1;
            }

            %>
			</select>
			年

			<select NAME=month>
				<%int imonth = 1;
            for (int i = 0; i < 12; i++)
            {
                if (month == imonth)
                {

                    %>
				<option value="<%=month%>" selected>
					<%=month%>
				</option>
				<%} else
                {

                    %>
				<option value="<%=imonth%>">
					<%=imonth%>
				</option>
				<%}
                imonth = imonth + 1;
            }

            %>
			</select>
			月
			<input class="input" type="Button" name="cmdOK" value="确定" onClick="cmdOK_onclick()">			
		</td>

	</tr>	
	</form>
	</table>
	<table cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width=100% class="thin">
	<tr align="center" height="20px" class="labeltable_middle_td">
	    <td class="headercolor" align="left">星期日</td>
	    <td class="headercolor" align="left">星期一</td>
	    <td class="headercolor" align="left">星期二</td>
	    <td class="headercolor" align="left">星期三</td>
	    <td class="headercolor" align="left">星期四</td>
	    <td class="headercolor" align="left">星期五</td>
	    <td class="headercolor" align="left">星期六</td>
	</tr>
	<tr align="center" height="20px" class="labeltable_middle_td">
	<form name="setform" action="holidayadd.jsp">
		<input type="hidden" name="year" value="<%=year%>">
		<input type="hidden" name="month" value="<%=month%>">
		<input type="hidden" name="action">
		<%
		    String[] m = new String[10];
            Date dCalDate = null;
            int length = new Date(year, month, 0).getDate();            
            String ischecked = null;
            //补开头少的星期
            Date startDate = new Date(year - 1900, month - 1, 1);
            int lack_weekday = startDate.getDay();
            //补结尾的td
            int lack_td = 0 ;
            for (int i = 1; i <= length + lack_weekday; i++)
            {
                if(i <= lack_weekday){
                    out.print("<td></td>");
                    continue;
                }
                dCalDate = new Date(year - 1900, month - 1, i-lack_weekday);
                
                //取出指定年月得周末；用于默认设定
                int nn = 0;
                //add by ge.tao
                String month_str = "";
                if(month<10) month_str = "0" + month;
                else month_str = "" + month;
                String day_str = "";
                if(dCalDate.getDate()<10) day_str = "0" + dCalDate.getDate();
                else day_str = "" + dCalDate.getDate();
                //end
                String holiday = year + "-" + month_str + "-" + day_str;
                HolidayDao hm = new HolidayDao();
                ischecked = hm.findByDay(holiday);
        %>
			<td align="left">
				<input type="checkbox" id="chkTaskId" name="chkTaskId" value="<%=holiday%>" <%=ischecked%>>
				&nbsp;
				<%=holiday%>
				
				
			</td>

		<%
		    	if ((i != 0) && (i%7 == 0) ){
		%>
			    </tr>
			    <tr align="center" height="20px" class="labeltable_middle_td">
			
	    <%
	        	} 	 
	        	if(i==length + lack_weekday){//一个月最后一天
	        	    lack_td = 7 - dCalDate.getDay();
	        	} 
	        	      
	    }   
	        for(int i=1;i<lack_td;i++){
	            out.print("<td></td>");
	        }
	    %>
	</tr>
	<tr>
		<td align="center" colspan="7">
			<input class="input" type="Button" name="cxsd" value="保存自定义设置" onClick="setholiday()">
			<input class="input" type="Button" name="mrsd" value="保存本月默认设定（周六周日）" onClick="mholiday()">			
			<input class="input" type="Button" name="cmdOK" value="保存本年默认设定(周六周日)" onClick="yearHoliday()">
		</td>
	</tr>
	</form>
</table>
<%}else{ %>
<div align="center">没有权限！请与系统管理员联系</div>
<%} %>
</body>

