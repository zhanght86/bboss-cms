<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="java.io.*"%>

<%@ page import="com.frameworkset.platform.cms.votemanager.*"%>
<%
VoteManager vote = new VoteManager();
		
String title = request.getParameter("title");
		String style = request.getParameter("style");
		String startTime = request.getParameter("startyear") + "-"
				+ request.getParameter("startmonth")+ "-"
				+ request.getParameter("startday");
		String endTime = request.getParameter("endyear") + "-"
				+ request.getParameter("endmonth")+ "-"
				+ request.getParameter("endday");
		String active = request.getParameter("active");
		Question question = new Question();
		question.setTitle(title);
		if(style.equals("checkbox"))
		{
		 style="checkboxstyle";
		}
		else
		{
		style="radiostyle";
		}
		question.setStyle(style);
		question.setStrStartime(startTime);
		question.setStrEndtime(endTime);
		question.setActive(Integer.parseInt(active));
       
  //  System.out.println(style);
		int n = Integer.parseInt(request.getParameter("n"));
		Collection items = new ArrayList();
		Item item = null;
		for (int i = 1; i <= n; i++) {
			item=new Item();
			//System.out.println(request.getParameter("option" + i));
			item.setOptions(request.getParameter("option" + i));
			items.add(item);
			item=null;
		}

	question.setItems(items);
		int i = vote.addVote(question);
		if (i == 1) {
			// 撰写新增投票成功
			//out.println("<h1>新增投票成功!</h1>");
			out.println("<script language='javascript'>alert('新增投票成功！！'); self.location='votelist.jsp';</script>");
		} else {
			// 否则，表明新增投票失败：
			out.println("<script language='javascript'>alert('新增投票失败！！');</script>");
		}
	
		%>
		
		