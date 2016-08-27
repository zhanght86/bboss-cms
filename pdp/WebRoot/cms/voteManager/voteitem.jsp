<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.frameworkset.platform.cms.votemanager.*"%>
<html>
<head>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>调查投票</title>
</head>
<body topmargin="1" rightmargin="1" scroll=no leftmargin="1">
<form name="form1" method="POST" action="voteactiontest.jsp">

<table width=270  border="1" cellpadding="0" align="center" cellspacing="0"   class="query_box">

	<%  
	    //获得当前IP
	    //根据IP去数据库找
	    //是否投过票
	    //返回值 两个  boolean flag1 boolean flag2 
	    
		VoteManager vote = new VoteManager();
		List QuestionItems=vote.getActiveQuestion();
		for(int j=0;j<QuestionItems.size();j++)
		{
		int idd =((Question)QuestionItems.get(j)).getId();
		String style=((Question)QuestionItems.get(j)).getStyle(); 
	
		List items = vote.getDiscriptionItems(idd);
		if(items.size()>0)
		{
		%>
		<tr height=12> <td width="100%"  colspan="2"  > 题目<%=j+1%>.
            【<%=((Item)items.get(0)).getTitle()%>】</td></tr>
	<% }
		%>

	  <% if(style.equals("radiostyle")){ 
			for (int i = 0; i < items.size(); i++) {
		%>
		<tr height=12><td><input type="radio" onclick="prompt(this)" name="<%=((Item)items.get(i)).getQid()%>" value="<%=((Item)items.get(i)).getId()%>"><%=((Item)items.get(i)).getOptions()%></td></tr>
		<%} 
	}
    else{
		for (int i = 0; i < items.size(); i++) {
	%>
	<tr height=12><td><input type="checkbox" onclick="prompt1(this)" name="<%=((Item)items.get(i)).getQid()%>" value="<%=((Item)items.get(i)).getId()%>"><%=((Item)items.get(i)).getOptions()%></td></tr>
		<%}
			}%> 
     <tr height=8><td> </td></tr>
    <% }%>
    <tr height=12>
					<td  bordercolor="#eeeee" bgcolor="#336699">
						<P align="center">
							<input type="button" value="投票"  onclick="abc()">
							<INPUT type="button" value="查看"  onclick="cw()">
						</P>
					</td>
				</tr>
		 </td>

	</tr>
	</table>
</form>
<a href=votelist.jsp>>>返回</a>
<iframe src="" name="iframe_delete" width=0 height=0></iframe>
</body>


  
<script language="JavaScript">
	function cw(){
	    window.open("seeresult.jsp","mywindow","toolbar=no,left=150,top=200,width=420,scrollbars=yes,height=450,menubar=no,systemMenu=no");
	}
	function abc(){

	    document.frames[0].src = "voteactiontest.jsp";
		document.forms[0].target = "iframe_delete";
		document.forms[0].action = "voteactiontest.jsp";
		document.forms[0].submit();
	}
	function prompt(e){
	  var voteid=e.name;
	  
	}
	function prompt1(e){
	  var voteid1=e.name;
	  
	}
</script>

</html>