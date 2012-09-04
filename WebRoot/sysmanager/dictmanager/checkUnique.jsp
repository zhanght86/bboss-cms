<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>

<%
	
	String dicttypeId = request.getParameter("dicttypeId");
	String fieldName = request.getParameter("fieldName");
	String fieldValue = request.getParameter("fieldValue");
	DictManager dictManager = new DictManagerImpl();
	out.print(dictManager.checkDictDataPropertyValueUnique(dicttypeId,fieldName,fieldValue));
%>

