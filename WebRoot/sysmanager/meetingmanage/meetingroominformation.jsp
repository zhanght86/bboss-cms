<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%String orgId=request.getParameter("orgId");%>
<html>
<head>    
 <title></title>

<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<script language="JavaScript">
	var jsAccessControl = new JSAccessControl("#ff0000","#ffffff","#eeeeee");
	function validateOnlyCheck(checkName)
	{
		 var o = document.all.item(checkName);
		 for (var i=0,j=0; o && o.length && i<o.length; i++){
	   	  	if(o[i].checked)
	   	  	{
	   	  		j++;
	   	   	}
	   	   	if(j == 2)
	   	   	{
	   	   		return false;
	   	   	}
	   	   }
		 return true;
	}
	function validatecheck(checkName)
	{
			 var o = document.all.item(checkName);
		for (var i=0; o && o.length && i<o.length; i++){
	   	  	  if(o[i].checked)
	   	  	  	return true;
	   	   }
	   	   if(o && !o.length )
			{
				if(o.checked)
					return true;
			}
			return false;
	}
	function checkAll(totalCheck,checkName){	//复选框全部选中
	   var selectAll = document.all.item(totalCheck);
	   
	   var o = document.all.item(checkName);
	   
	   if(selectAll.checked==true){
		   for (var i=0; o && o.length && i<o.length; i++){
	      	  if(!o[i].disabled){
	      	  	o[i].checked=true;
	      	  }
		   }
		   if(o && !o.length && !o.checked)
			{
				o.checked=true;
			}
		   
	   }else{
		   for (var i=0; o && o.length && i<o.length; i++){
	   	  	  o[i].checked=false;
	   	   }
	   	   if(o && !o.length && !o.checked)
			{
				o.checked=false;
			}
	   	   
	   }
	}
	//单个选中复选框
	function checkOne(totalCheck,checkName){
	   var selectAll = document.all.item(totalCheck);
	   var o = document.all.item(checkName);
		var cbs = true;
		
		for (var i=0;o && o.length && i < o.length;i++){
			if(!o[i].disabled){
				if (o[i].checked==false){
					cbs=false;
				}
			}
		}
		if(o && !o.length && !o.checked)
		{
			cbs = false;
		}
		if(cbs){
			selectAll.checked=true;
		}
		else{
			selectAll.checked=false;
		}
	}
	function addMeetingRoom()
	{
		document.location.href="../meetingmanage/addRoom.jsp";
	}	
	function delMeetingRoom(checkName,dealType)
	{
    	if(!validatecheck(checkName))
			{
				alert("请选择要删除的会议室");
				return false;
			}
    	if (dealType==1)
    		{
	    		outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
	        	if (confirm(outMsg))
	        		{
			        	SchedularForm.action="../meetingmanage/meetingManager.do?method=delMeetingRoom";
						SchedularForm.submit();
				 		return true;
					}
			} 
    			return false;
			}
</script>
<body class="contentbodymargin">
<div id="contentborder" align="center">

<form name="SchedularForm" action="" method="post">

<table width="100%"   cellpadding="1" cellspacing="1">
  <tr>
    <td class="detailtitle">会议室名称</td>
    <td class="detailcontent"><input type="text" name="name"></td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td class="detailtitle" >可容纳人数</td>
    <td class="detailcontent"><input type="text" name="seatnumber"></td>
    <td><div align="center">
            <input name="Submit4" type="submit" class="input" value="查询">                
            <input name="Submit22" type="reset" class="input" value="重置">
          </div>
                <div align="center"></div></td>
  </tr>      
</table>
<hr width="98%">	
    
  <table width="100%" cellpadding="1" cellspacing="1" bordercolor="#EEEEEE">
    <!--分页显示开始,分页标签初始化-->
    <tr>
      <td height="30" class="detailtitle" align="left" colspan="6"><b>会议室信息</b></td>
    </tr>
  <pg:listdata dataInfo="MeetingRoomList" keyName="MeetingRoomList" />

					<pg:pager maxPageItems="15" scope="request" data="MeetingRoomList" isList="false">
						<th class="tablecells" width="10">
							<input type="checkBox" name="checkBox" onClick="checkAll('checkBox','ID')">
						</th>
						<TH class="headercolor">
						会议室名称
						</TH>
						<TH class="headercolor">
						会议室地点
						</TH>
						<TH class="headercolor">
						可容纳人数
						</TH>
						<TH class="headercolor">
						可用坐位数
						</TH>
							<!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center'>
										暂时没有会议室
									</td>
								</tr>
							</pg:notify>
							<!--list标签循环输出每条记录-->
						<pg:list>
							<tr class="" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'">
								<td class="tablecells" width="5%" nowrap="nowrap">
									<input type="checkBox" name="ID" onClick="checkOne('checkBox','ID')" value="<pg:cell colName="roomId" defaultValue=""/>">
								</td>
																
								<td class="tablecells" width="20%" nowrap="nowrap">

									<pg:equal colName="name" value="">&nbsp</pg:equal>
									<pg:cell colName="name" defaultValue="" />
								</td>
								<td class="tablecells" width="20%" nowrap="nowrap">

									<pg:equal colName="place" value="">&nbsp</pg:equal>
									<pg:cell colName="place" defaultValue="" />

								</td>
								<td class="tablecells" width="20%" nowrap="nowrap">

									<pg:equal colName="capacity" value="">&nbsp</pg:equal>
									<pg:cell colName="capacity"defaultValue="" />
								</td>
								<td class="tablecells" width="20%" nowrap="nowrap">

									<pg:equal colName="seatnumber" value="">&nbsp</pg:equal>
									<pg:cell colName="seatnumber" defaultValue="" />
								</t
							</tr>
						</pg:list>     
    <tr height="18px" >
      <td colspan=3 align=right class="detailcontent"><pg:index/>
          <input type="hidden" name="queryString" value="<pg:querystring/>"></td>
       <td colspan=2 align=left >   <input name="New" type="button" class="input" onclick="addMeetingRoom()" value="新建">  <input name="Del" type="button" class="input" onclick="javascript:delMeetingRoom('ID',1); return false;" value="删除"> <input name="Modify" type="button" class="input" onclick="ModifyMeetingRoom()" value="修改"> </td>
    </tr>
    </pg:pager>   
  </table> 
</form> 
 </div>
</body>
</html>

