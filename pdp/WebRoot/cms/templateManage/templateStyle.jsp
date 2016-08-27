<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="java.util.List" %>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<select name="type" class="cms_select" id="type" style="width:<%=request.getParameter("select_width")%>">
	<option value="-1">通用</option>
	<%
		String temType = request.getParameter("templateType");
		//用户没有选择任何模板类型时返回,通常是在加载queryFrame.jsr页面时发生
		if(null == temType || temType.equals("")){
			return ;
		}
		TemplateManager tm = new TemplateManagerImpl();
		TemplateStyleManager tsm = new TemplateStyleManagerImpl();
		List ts = tm.getTemplateStyleList(temType);
		//没有除"通用"以外的任何其它子类型时直接返回
		if(ts.size()==0){
			return ;
		}
		for(int i=0;i<ts.size();i++){
			String styleId = ts.get(i).toString();
			String styleName = tsm.getTemplateStyleName(styleId);
			//避免出现空选项
			if(null == styleName || styleName.equals("")){
				return ;
			}
	%>
			<option value="<%=styleId%>"><%=styleName%></option>
	<%
		}
	%>
</select>