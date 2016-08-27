<%@ page contentType="text/html; charset=UTF-8" language="java"%>

<%@ page import="com.frameworkset.platform.cms.docCommentManager.*"%>
<%
	int successflag = 0;
	
	String commentId = request.getParameter("commentId");
	
	String impeacher = request.getParameter("impeacher");
	String email = request.getParameter("email");
	String reason = request.getParameter("reason");
	String description = request.getParameter("description");
	
	try{
		CommentImpeach commentImpeach = new CommentImpeach();
		commentImpeach.setImpeacher(impeacher);
		commentImpeach.setCommentId(Integer.parseInt(commentId.trim()));
		commentImpeach.setEmail(email);
		commentImpeach.setReason(Integer.parseInt(reason.trim()));
		commentImpeach.setDescription(description);
		DocCommentManager dcmi = new DocCommentManagerImpl();
		dcmi.addDocCommentImpeach(commentImpeach);
		successflag = 1;
	}catch(Exception e){
		e.printStackTrace();
	}
%>
<%		 
	if(successflag == 1){ 
%>
		<script language = "javascript">
			alert("操作成功！");
			window.close();	
		</script>
<%
	}else if(successflag == 0){
%>
		<script language="javascript">
			alert("操作失败！");
			window.close();	
		</script>
<%
	}
%>