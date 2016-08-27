<%@page import="java.io.InputStream"%>
<%@page import="org.activiti.engine.impl.*"%>
<%@page import="org.activiti.engine.impl.pvm.*"%>
<%@page import="org.activiti.engine.impl.pvm.process.*"%>
<%@page import="org.activiti.engine.repository.*"%>
<%@page import="org.activiti.engine.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	ProcessEngine processEngine = ProcessEngineConfiguration
		.createProcessEngineConfigurationFromResource("activiti.cfg.xml")
		.buildProcessEngine();
	
	RepositoryService repositoryService = processEngine.getRepositoryService();
	String process_key = request.getParameter("process_key");
	ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
		.processDefinitionKey(process_key)
		.latestVersion()
		.singleResult();
	
	String diagramResourceName = processDefinition.getDiagramResourceName();
	System.out.println("-----------++++++++++   "+diagramResourceName);
	//diagramResourceName = "demo.png";
	InputStream is = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), diagramResourceName);
	
	byte[] b = new byte[1024];
	int len = -1;
	while((len = is.read(b, 0, 1024)) != -1) {
		response.getOutputStream().write(b, 0, len);
		// 防止异常：getOutputStream() has already been called for this response
		out.clear();
		out = pageContext.pushBody();
	}	

%>