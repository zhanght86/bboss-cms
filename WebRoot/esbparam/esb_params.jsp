<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../mq/broker/brokerconfigure.jsp"%>
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
	String properMapname = request.getParameter("properMapnames");
	String[] properMapnames = properMapname.split(",");
	String paramshandler = request.getParameter("paramshandler");
	//map参数处理对象
	//gao.tang tihuan
	BrokerManagerInf brokerMan = (BrokerManagerInf)BaseSPIManager.getBeanObject(rpcbrokeraddr);
%>
<html>
  <head>
	<title>高级参数配置界面</title>
	<link href="../include/windows.css" rel="stylesheet" type="text/css">
  	<SCRIPT type=text/javascript src="../include/js/common.js"></SCRIPT>
	<SCRIPT type=text/javascript src="../include/js/vconf.js"></SCRIPT>
	<SCRIPT type=text/javascript src="../include/js/validator.js"></SCRIPT>
    
	<script type="text/javascript">
		function resetDefaul(){
			document.vForm.target="framehidden";
			document.vForm.action="reset_default.jsp";
			document.vForm.submit();
		}
		//返回到节点列表
		function cancel()
		{
			parent.window.location.href="mq_factory_list.jsp";
		}
	</script>
  </head>
  <body>
  	<form action="esb_params_do.jsp" method="post" name="vForm" target="framehidden">
  		<input type="hidden" name="factoryId" value="<%=factoryId %>">
  		<input type="hidden" name="paramsType" value="<%=paramsType %>">
  		<input type="hidden" name="properMapname" value="<%=properMapname %>">
  		<input type="hidden" name="paramshandler" value="<%=paramshandler %>">
  		<input type="hidden" name="rpcId" value="<%=rpcId %>">
  		
  		<!--  
  		<table width="100%" border="0" cellpadding="1" cellspacing="0"
					class="thin" bordercolor="#EEEEEE">
	    	<tr>
				<td align="center" colspan="3" valign="middle" height="50">
					连接工厂高级参数配置
				</td>
			</tr>
    	</table>
    	-->
    <%
		String keyParams = null;
		Pro proParams = null;
    	for(String mapName : properMapnames){
	    	Pro t_ = BaseSPIManager.getProBean(mapName);
	    	String label = t_.getLabel(); 
	    	if(label == null)
	    		label = mapName;
    	
    %>
	    <fieldset>
	    	<legend><%=label %></legend>
	    	<table width="100%" border="0" cellpadding="1" cellspacing="0"
						class="thin" bordercolor="#EEEEEE">
    <%
    		//从数据库中获取已经配置的参数
    		//ProMap<String,Pro> paramsConfMap = 
    		//	handler.getNodeParamsWithProMapName(mapName,factoryId,paramsType);
    		
    		ProMap<String,Pro> paramsConfMap = 
    				brokerMan.getNodeParamsWithProMapName(paramshandler,mapName,factoryId,paramsType);
    		
    		//是否是从数据库获取的数据
    		boolean persistentparams_ = (paramsConfMap != null) && (paramsConfMap.size() > 0);
    		//获取配置文件中的配置参数默认值
    		ProMap confMap = (ProMap) BaseSPIManager.getMapProperty(mapName);	
    		Iterator itParams = confMap.keySet().iterator();
    		while(itParams.hasNext()){
    			keyParams = (String)itParams.next();
    			proParams = confMap.getPro(keyParams);
    			String paramType = "string";
		  		if(proParams.getClazz() != null && !"".equals(proParams.getClazz())){
		  			paramType = proParams.getClazz();
		  			
		  		} 
		  		String value = proParams.getString();
		  		String name = proParams.getName();
		  	    System.out.println("name:"+name);
		  		if(persistentparams_){
	            	Pro newValue =  paramsConfMap.get(name);
	                if(newValue != null)
	                	value = newValue.getString();
	            }
		  		if(value == null || "".equals(value)){
		  			value = request.getParameter(name)==null?value:request.getParameter(name);
		  		}
    %>
    			<tr>
	   				<td width="200" valign="middle" align="right" height="25">
	   					<%=proParams.getLabel() %>:
	   				</td>
	   				<td width="200">
	   				<%if("int".equalsIgnoreCase(paramType)){ %>
	   					<input name="<%=name %>" type="text" id="<%=name %>" 
	   						alt="<%=proParams.getLabel() %>:无内容/数字/有空格/errArea{<%=name %>tip}"
	   						value="<%=value %>" size="50" maxlength="25" >
	   					<SPAN id="<%=name %>tip"></SPAN>
	   				<%}else if("string".equalsIgnoreCase(paramType)){ %>
	   					<input name="<%=name %>" type="text" id="<%=name %>" 
	   						alt="<%=proParams.getLabel() %>:有中文/errArea{<%=name %>tip}"
	   						value="<%=value==null?"":value %>" size="50" > 
	   					<SPAN id="<%=name %>tip"></SPAN>
	   				<%}else if("long".equalsIgnoreCase(paramType)){ %>
	   					<input name="<%=name %>" type="text" id="<%=name %>" 
	   						alt="<%=proParams.getLabel() %>:无内容/有中文/有空格/errArea{<%=name %>tip}"
	   						value="<%=value %>" size="50" > 
	   					<SPAN id="<%=name %>tip"></SPAN>
	   				<%}else if("uri".equalsIgnoreCase(name)){
	   					System.out.println("++++++++++++++++++++++++++++++++===");%>
	   					
	   				
	   				<%}else{ %>
						<select name="<%=name %>">
							<option value="true" <%if(value.equals("true")){out.print("selected");} %>>是</option>
							<option value="false" <%if(value.equals("false")){out.print("selected");} %>>否</option>
						</select>
	   				<%} %>
	   				</td>
	   				<td><%=proParams.getDescription() %></td>
	  			</tr>
    <% 
    		}
    %>
			</table>
    	</fieldset>
    	<br>
    <%		
    	}	
    %>
    	<table width="100%" border="0" cellpadding="1" cellspacing="0"
					class="thin" bordercolor="#EEEEEE">
      		<tr align="center">
	  			<td colspan="3">
	  				<input name="save" type="submit" value="保存" class="input">
	  				&nbsp;&nbsp;
	  				<!--  
	  				<input name="backa" type="button" id="backa" onClick="cancel()" value="返回" class="input">
	  				-->
	  				&nbsp;&nbsp;
	  				<input name="resetD" type="button" id="resetD" onClick="resetDefaul()" value="重置默认配置" class="input">
	  			</td>
	  		</tr>
      	</table>
  	</form>
  	<iframe name="framehidden" height="0" width="0" ></iframe>
	<SCRIPT type=text/javascript>
	onReady(function(){
	        var conf = (typeof $vconf == 'undefined') ? {} : $vconf;
	        var v = new Validator(conf);
	        v.init('vForm');
	});
	</SCRIPT>
  </body>
</html>
