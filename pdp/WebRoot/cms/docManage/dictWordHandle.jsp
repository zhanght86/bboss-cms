<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.docCommentManager.*"%>
<%@ page import="com.frameworkset.platform.cms.docCommentManager.docCommentDictManager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String commenterName = accesscontroler.getUserName();
	
	int successflag = 0;
	String closeWinFlag = "0";         //关闭窗口标志
	
	String action = request.getParameter("action");

	//字典词汇处理
	DocCommentDictManager docCommentDictManager = new DocCommentDictManagerImpl();
	try{
		if("add".equals(action)){
			closeWinFlag = "1";
			DocCommentDict docCommentDict = new DocCommentDict();
			docCommentDict.setWord(request.getParameter("word"));
			docCommentDict.setSiteId(Long.parseLong(request.getParameter("siteId")));
			docCommentDict.setDescription(request.getParameter("description"));
			
			docCommentDictManager.addOneWord(docCommentDict);
			successflag = 1; 
		}else if("delete".equals(action)){
			String[] wordIds = request.getParameterValues("ID");
			if(wordIds!=null){
				docCommentDictManager.deleteWords(wordIds);
			}
			successflag = 1; 
		}else if("edit".equals(action)){
			closeWinFlag = "1";
			String wordId = request.getParameter("wordId");
			DocCommentDict docCommentDict = new DocCommentDict();
			docCommentDict.setWordId(Integer.parseInt(wordId));
			docCommentDict.setWord(request.getParameter("word"));
			docCommentDict.setSiteId(Long.parseLong(request.getParameter("siteId")));
			docCommentDict.setDescription(request.getParameter("description"));
			docCommentDictManager.updateWord(wordId,docCommentDict); 
			successflag = 1; 
		}
	}catch(DocCommentDictManagerException de){
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