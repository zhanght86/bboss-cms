<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.docCommentManager.*"%>
<%@ page import="org.frameworkset.web.servlet.support.WebApplicationContextUtils"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String commenterName = accesscontroler.getUserName();
	
	int successflag = 0;
	String closeWinFlag = "0";         //关闭窗口标志
	
	String docId = request.getParameter("docId");
	String action = request.getParameter("action");
	String channelId = request.getParameter("channelId");

	//文档评论处理
	DocCommentManager idm = (DocCommentManager)WebApplicationContextUtils.getWebApplicationContext().getBeanObject("docCommentManager");
	try{
		if("add".equals(action)){
			closeWinFlag = "1";
			DocComment docComment = new DocComment();
			docComment.setDocComment(request.getParameter("docComment"));
			docComment.setDocId(Integer.parseInt(docId.trim()));
			docComment.setDocTitle(request.getParameter("docTitle"));
			docComment.setUserName(commenterName);
			docComment.setStatus(1);			//后台管理员增加的评论无需审核
			
			String remoteIP = com.frameworkset.util.StringUtil.getClientIP(request);
			docComment.setUserIP(remoteIP);//保存IP,weida
			
			idm.addOneComment(docComment);
			successflag = 1; 
		}else if("delete".equals(action)){
			String commentId = request.getParameter("commentId");
			String[] commentIds = request.getParameterValues("ID");
			if(commentIds!=null){
				idm.delCommentsByComId(commentIds);
			}
			else if(commentId!=null){
				closeWinFlag = "1";
				idm.delOneCommentByComId(Integer.parseInt(commentId));
			}
			successflag = 1; 
		}else if("publish".equals(action)){
			String commentId = request.getParameter("commentId");
			String[] commentIds = request.getParameterValues("ID");
			if(commentIds!=null){
				idm.updateCommentStatus(commentIds,1);
			}
			else if(commentId!=null){
				closeWinFlag = "1";
				idm.updateCommentStatus(commentId,1);
			}
			successflag = 1; 
		}else if("withdrawPub".equals(action)){
			String commentId = request.getParameter("commentId");
			String[] commentIds = request.getParameterValues("ID");
			if(commentIds!=null){
				int withdrawFlag = 1;
				for(int i=0;i<commentIds.length;i++){
					if(idm.getCommentByComId(Integer.parseInt(commentIds[i])).getStatus()!=1){    //只有处于发布状态的评论才可以撤发
						withdrawFlag = 0 ;
					}
				}
				if(withdrawFlag==1){
					idm.updateCommentStatus(commentIds,2);
					successflag = 1; 
				}
				else 
					successflag = 0;
			}
			else if(commentId!=null){
				closeWinFlag = "1";
				idm.updateCommentStatus(commentId,2);
				successflag = 1; 
			}
		}else if("close".equals(action)){
			if(docId != null){
				idm.switchDocCommentFunction(docId,"close","doc");
			}else{
				idm.switchDocCommentFunction(channelId,"close","chnl");
				closeWinFlag = "1";
			}
			successflag = 1; 
		}else if("open".equals(action)){
			if(docId != null){
				idm.switchDocCommentFunction(docId,"open","doc");
			}else{
				idm.switchDocCommentFunction(channelId,"open","chnl");
				closeWinFlag = "1";
			}
			//idm.switchDocCommentFunction(docId,"open","doc");   
			successflag = 1; 
	    } else if("closeCommentAuditSwitch".equals(action)) {	
	    	idm.switchDocCommentAudit(Integer.parseInt(channelId), 1);
	    	
	    	closeWinFlag = "1";
	    	successflag = 1; 
	    } else if ("openCommentAuditSwitch".equals(action)) {
	    	idm.switchDocCommentAudit(Integer.parseInt(channelId), 0);
	    	
	    	closeWinFlag = "1";
			successflag = 1; 
		}else if("emailReply".equals(action)){    //举报信息邮件回复
			String impeacheId = request.getParameter("impeacheId");
			String toEmail = request.getParameter("toEmail");
			String toName = request.getParameter("toName");
			String subject = request.getParameter("subject");
			String msg = request.getParameter("msg");
			idm.replyImpeach(impeacheId,toEmail,toName,subject,msg);
			successflag = 1; 
			closeWinFlag = "1";
		}
	}catch(DocCommentManagerException de){
		de.printStackTrace();
	}
%>
<%		 
	if(successflag == 1){
%>
		<script language = "javascript">
			alert("操作成功！");
			if("<%=closeWinFlag%>"=="1"){
				window.close();
				window.returnValue="cf";	
			} else{
				var str = parent.document.location.href;
				var end = str.indexOf("?");
				var strArray;
				if(end != -1)
					strArray= str.slice(0,end);
				else
					strArray = str;
				parent.document.location.href = strArray+"?"+parent.document.all.queryString.value;
			}
		</script>
<%
	}else if(successflag == 0){
%>
		<script language="javascript">
			if("<%=action%>"=="withdrawPub"){
				alert("处于未审状态的文档不能撤销发布，请检查！！");
			} else{
				alert("数据库操作失败！");
				if("<%=action%>"=="add")
					window.close();
			}
		</script>
<%
	}
%>