<%
/**
 * 
 * <p>Title: 关键字设置保存页面</p>
 *
 * <p>Description: 关键字保存处理页面</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2006-9-15
 * @author gao.tang
 * @version 1.0
 */
 %>
 <%@ page language="java" contentType="text/html; charset=UTF-8"%>
 <%@ page import="com.frameworkset.platform.security.AccessControl,
 				com.frameworkset.platform.dictionary.KeyWord,
 				com.frameworkset.platform.dictionary.DictKeyWordManager,
 				com.frameworkset.platform.dictionary.DictKeyWordManagerImpl"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
 
 <%
 	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	String dicttypeId = request.getParameter("dictTypeId");
	//String[] checkBoxOne = request.getParameterValues("checkBoxOne");
	//columnName#javaProperty*columnName[i...]#javaProperty[i...]
	String checkValues = request.getParameter("checkValues");
	//System.out.println(checkValues);
	String[] checkValue = null;
	if(!"".equals(checkValues)){
		checkValue = checkValues.split("-#-");
	}
	KeyWord keyWord = null;
	List list = new ArrayList();
	for(int i = 0; checkValue != null && i < checkValue.length; i++){
		keyWord = new KeyWord();
		keyWord.setDictypeId(dicttypeId);
		keyWord.setFieldName(checkValue[i]);
		list.add(keyWord);
	}
	DictKeyWordManager dictKeyWordManager = new DictKeyWordManagerImpl();
	boolean state = dictKeyWordManager.delDefineKeyFields(list);
	
 %>
 <%
 	if(state){
 %>
 <script language="Javascript">
 	alert("操作成功！");
 	parent.parent.parentSubmit();
 </script>
 <%
 	}else{
 %>
 <script language="Javascript">
 	alert("操作失败！");
 </script>
 <%
 	}
 %>
