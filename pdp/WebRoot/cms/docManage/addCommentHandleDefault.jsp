<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.docCommentManager.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*,
				 com.frameworkset.platform.sysmgrcore.manager.db.*,
				 com.frameworkset.platform.sysmgrcore.entity.*"%>
<%
	int successflag = 0;
	
	String docId = request.getParameter("docId");
	String commenterName = request.getParameter("commenterName");
	String psword = request.getParameter("psword");
	String srcCommentId = request.getParameter("srcCommentId");
	String flag = request.getParameter("flag");              //为up表示顶操作，为response表示回复（回复操作要关闭窗口）
	
	String userhide = request.getParameter("userhide");	  //1或null表示匿名发表；0表示不匿名发表
	System.out.println("userhide:" + userhide);

	UserManager um = new UserManagerImpl();
	boolean isUserExist = false;
	if("0".equals(userhide) && commenterName!=null && psword!=null){
		User user = um.getUser("from User user where user.userName='" + commenterName + "' and user.userPassword = '" + psword + "'");
		isUserExist = user == null? false:um.isUserExist(user);
	}
	if("1".equals(userhide) || isUserExist ||"up".equals(flag)){	
		//文档评论处理
		DocCommentManager idm = new DocCommentManagerImpl();
		DocumentManager dm = new DocumentManagerImpl();
		String docTitle = dm.getDoc(docId.trim()).getSubtitle();
		String commenterIP =com.frameworkset.util.StringUtil.getClientIP(request);
		
		DocComment docComment = new DocComment();
		docComment.setComment(request.getParameter("docComment"));
		docComment.setDocId(Integer.parseInt(docId.trim()));
		docComment.setDocTitle(docTitle);
		docComment.setUserIP(commenterIP);
		docComment.setStatus(0);     //前台用户的评论为未审状态，待审核
		if(isUserExist){
			docComment.setUserName(commenterName);
		}
		if(srcCommentId!=null && srcCommentId.length()>0){
			int srcsrcCommentId = idm.getCommentByComId(Integer.parseInt(srcCommentId)).getSrcCommentId();
			int realSrcCommentId = srcsrcCommentId==0?Integer.parseInt(srcCommentId):srcsrcCommentId;
			docComment.setSrcCommentId(realSrcCommentId);
		}
		idm.addOneComment(docComment);
		successflag = 1;    
	}	
%>
<%		 
	if(successflag == 1){ 
%>
		<script language = "javascript">
			alert("操作成功！");
			if("<%=flag%>"=="response"){
				window.close();
				window.returnValue = "cf";
			}
			//var str = parent.document.location.href;
			//var end = str.indexOf("?");
			//var strArray;
			//if(end != -1)
			//	strArray= str.slice(0,end);
			//else
			//	strArray = str;
			//parent.document.location.href = strArray+"?"+parent.document.all.queryString.value;
			
			parent.document.location.reload();
		</script>
<%
	}else if(successflag == 0){
%>
		<script language="javascript">
			alert("用户名或密码错误！");
		</script>
<%
	}
%>