<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.Exception"%>
<%@ page import="java.io.*"%>
<%@ page import="com.frameworkset.platform.cms.votemanager.*"%>

<script language='javascript'>var s ="";</script>


<%          String strIP=com.frameworkset.util.StringUtil.getClientIP(request);
      	   
 
    	     VoteManager vote = new VoteManager();
			 List QuestionItems = vote.getActiveQuestion();
			 boolean flag = false;
			
		for (int j = 0; j < QuestionItems.size(); j++) {
			
		int id = ((Question) QuestionItems.get(j)).getId();
			if(vote.ipExist(id,strIP))
			{
			  if(vote.isRepeat(id))
			  {
			   vote.addIPlog(id,strIP);
			   String style = ((Question) QuestionItems.get(j)).getStyle();
				
				if (style.equals("radio")) 
				{
				String idd = request.getParameter("" + id);
					
					if (idd != null) {
						vote.updateVote(Integer.parseInt(idd));
						flag = true;
					}
					
				} else {
				
					String[] idd = request.getParameterValues("" + id);
					
					if (idd != null) {
					
						for (int i = 0; i < idd.length; i++) {
							int id0 = Integer.parseInt(idd[i]);
							vote.updateVote(id0);
						}
						flag = true;
					}
				}
			   
			  }
			  else
			  {
			  out.println("<script language='javascript'>s+='你已经投过票了！！';</script>");
			  }
				
				
				
			}
			else
			{
			vote.addIPlog(id,strIP);
			String style = ((Question) QuestionItems.get(j)).getStyle();
				
				if (style.equals("radio")) {
				
					String idd = request.getParameter("" + id);
					
					if (idd != null) {
						vote.updateVote(Integer.parseInt(idd));
						flag = true;
					}
					
				} else {
				
					String[] idd = request.getParameterValues("" + id);
					
					if (idd != null) {
					
						for (int i = 0; i < idd.length; i++) {
							int id0 = Integer.parseInt(idd[i]);
							vote.updateVote(id0);
						}
						flag = true;
					}
				}
			
			}
          }
          
			if (flag) {

				out.println("<script language='javascript'>alert(s+'感谢你投了宝贵的一票！！');</script>");
			} else {
				out.println("<script language='javascript'>alert(s+'请你至少选择一项！！');</script>");
			}

		%>
		
		<script language='javascript'>window.close();</script>
