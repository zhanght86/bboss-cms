<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../mq/broker/brokerconfigure.jsp"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="org.frameworkset.spi.assemble.ProMap,
				org.frameworkset.spi.assemble.Pro,
				java.util.Iterator"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	//工厂ID
	String factoryId = request.getParameter("factoryId");
	//参数类型
	String paramsType = request.getParameter("paramsType");
	//参数map配置的名称，多个map配置用","隔开
	String properMapname = request.getParameter("properMapname");
	String[] properMapnames = properMapname.split(",");
	String paramshandler = request.getParameter("paramshandler");
	//map参数处理对象
	//ParamsHandler handler = (ParamsHandler) BaseSPIManager.getBeanObject(paramshandler);
	//gao.tang tihuan
	BrokerManagerInf brokerMan = (BrokerManagerInf)BaseSPIManager.getBeanObject(rpcbrokeraddr);
	
	String key = null;
	Pro pro = null;
	List<Pro> params = new ArrayList<Pro>();
	for(String mapName : properMapnames){
		//获取配置文件中的配置参数默认值
    	ProMap confMap = (ProMap) BaseSPIManager.getMapProperty(mapName);
    	Iterator itParams = confMap.keySet().iterator();
    	while(itParams.hasNext()){
    		key = (String)itParams.next();
			pro = new Pro();
			pro.setName(key);
			String value =  MQUtil.getStringFromRequest(request,key, "");
			pro.setValue(value);
			params.add(pro);
    	}	
	}
	String msg = null;
	try{
		//handler.insertConParams(Integer.parseInt(factoryId),params,paramsType);
		
		brokerMan.insertConParams(paramshandler,Integer.parseInt(factoryId),params,paramsType);
		//刷新connectionFactory工厂
		//JMXBrokerFacade.resetActiveMQConnectionFactory();
	}catch(Exception e){
		msg = MQUtil.formatErrorMsg(e.getMessage());
	}

%>
<%if(msg == null){ %>
	<script type="text/javascript">
	<!--
		alert("操作成功！");
		parent.document.location.href=parent.document.location.href;
	//-->
	</script>
<%}else{ %>
	<script type="text/javascript">
	<!--
		alert("操作失败！\n<%=msg%>");
	//-->
	</script>
<%} %>
