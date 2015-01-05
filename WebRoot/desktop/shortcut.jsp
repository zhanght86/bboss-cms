<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@page import="com.frameworkset.platform.framework.MenuHelper"%>
<%@page import="com.frameworkset.platform.framework.ModuleQueue"%>
<%@page import="com.frameworkset.platform.framework.Module"%>
<%@page import="com.frameworkset.platform.framework.Item"%>
<%@page import="com.frameworkset.platform.framework.ItemQueue"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<div id="shortcutcontainer">
 	<style>
 	 	<pg:list requestKey="menulist">
 		#<pg:cell colName="id"/>-shortcut img {
   			 width:48px;
    		height:48px;
    	background: url(<%=request.getContextPath()%>/<pg:cell colName="imageUrl"/>) center no-repeat;
    	filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath()%>/<pg:cell colName="imageUrl"/>', sizingMethod='image');
		}
	
		</pg:list>
		
		
		


			


 	</style>
 	
	
    <dl id="x-shortcuts">
        <pg:list requestKey="menulist">
	        <dt id="<pg:cell colName="id"/>-shortcut">
	            <a href="javascript:void" onclick="ShortCut('<pg:cell colName="id"/>','<pg:cell colName="name"/>','<pg:cell colName="pathU"/>');"><img src="${pageContext.request.contextPath}/desktop/images/s.gif"/>
	            <div><pg:cell colName="name"/></div></a>
	        </dt>
	        
	        
        </pg:list>
        
    </dl>
    
    
   
</div>


