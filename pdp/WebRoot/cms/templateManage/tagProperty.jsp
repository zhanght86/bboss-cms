<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
	
	String tagId = request.getParameter("tagId");
	String attrsStr = request.getParameter("attrs");
	Map attrsMap = new HashMap();
	if(attrsStr!=null && attrsStr.trim().length()!=0){
		String[] attrs = attrsStr.split(";;;");
		for(int i=0;i<attrs.length;i++){
			String[] pair = attrs[i].split(":::");
			attrsMap.put(pair[0].replace("cms_attr_name_",""),pair[1]);
		}
	}
	String styleAttrsStr = request.getParameter("styleAttrs");
	Map styleAttrsMap = new HashMap();
	if(styleAttrsStr!=null && styleAttrsStr.trim().length()!=0){
		String[] attrs = styleAttrsStr.split(";;;");
		for(int i=0;i<attrs.length;i++){
			String[] pair = attrs[i].split(":::");
			styleAttrsMap.put(pair[0].replaceAll("cms_attr_name_",""),pair[1]);
		}
	}
	System.out.println("tagId = " + tagId);
	System.out.println("attrs = " + attrsStr);
	System.out.println("普通属性: " + attrsMap);
	System.out.println("样式属性: " + styleAttrsMap);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>标签属性</title>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>

<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
td {
	padding-right: 3px;
	padding-left: 3px;
}
</style>
<script language="javascript">
function setAttrs(){
	var tags = document.all.form1.elements;
	var theAttrs = " ";
	var style = "";
	for(var i=0;i<tags.length;i++){
		var e = tags[i]; 
		if(e.type!="button" && e.type!="reset" && e.type!="submit"){
			var attr = getAttr(e.name);
			if(attr!=-1){
				if(attr.style == "1"){
					style += (attr.name+":"+e.value+";");
				}else{
					theAttrs += ("cms_attr_name_"+attr.name+'="'+e.value+'" ');
				}
			}
		}
	}
	var win = window.dialogArguments; 
	win.setTagAttributes(theAttrs+'cms_attr_name_style="'+style+'"');
	top.close();
	
}

var tagAttrArray = new Array();

//标签的属性类:名字,label,值,是否必须的,是否是样式
function TagAttribute(name,label,value,required,style){
	this.name = name;
	this.label = label;
	this.value = value;
	this.required = required;
	this.style = style;
}

function addAttr(name,label,value,required,style){
	tagAttrArray[tagAttrArray.length] = new TagAttribute(name,label,value,required,style);
}

function getAttr(name){
	for(var i=0;i<tagAttrArray.length;i++){
		if(tagAttrArray[i].name == name){
			return tagAttrArray[i];
		}
	}
	return -1;
}
function buildView(){

		
	document.write('<table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolordark="#FFFFFF" bordercolorlight="#0000cc"><tr><td width="80px" height="25px" align="right" valign="middle" nowrap>属性名字</td>			<td align="left" valign="middle" nowrap>属性值</td></tr>');	
	for(var i=0;i<tagAttrArray.length;i++){
		document.write('<tr><td width="80px" height="25px" align="right" valign="middle" nowrap>&nbsp;');
		document.write(tagAttrArray[i].label);
		document.write('</td><td height="25" align="left" valign="middle" nowrap>');
		document.write('<input name="'+tagAttrArray[i].name+'" type="text" id="'+tagAttrArray[i].name+'" ');	
		document.write('value="'+tagAttrArray[i].value+'" />');
		if(tagAttrArray[i].required == "1"){
			document.write('<span style=\"color:#FF0000\">*</span>');
		}
		document.write('</td></tr>');
	}
	document.write('</table>');
}
</script>
</head>
<body>
<form id="form1" name="form1" method="post">
<script language="javascript">
<%
try {

   DBUtil conn = new DBUtil(); 

	String sql = "select TAG_ID,ATTR_NAME,ATTR_LABEL, "
				+"ATTR_DEFAULT_VALUE,REQUIRED,STYLE FROM TB_CMS_TPL_TAG_ATTR "
				+"where tag_id = '"+tagId+"' order by tag_id,required desc";
	conn.executeSelect(sql);
	out.println("addAttr('tagTypeId','标签类型id','"+attrsMap.get("tagTypeId")+"','1','0');");
	out.println("addAttr('desc','标签描述','"+attrsMap.get("desc")+"','1','0');");
	for(int i=0;i<conn.size();i++){
		String name = conn.getString(i,"ATTR_NAME");
		String label = conn.getString(i,"ATTR_LABEL");
		
		if(label==null || label.equals("")){
			label = name;
		}
		String r = conn.getString(i,"REQUIRED");
		String s = conn.getString(i,"STYLE");
		
		Object obj = null;
		if("1".equals(s)){
			obj = styleAttrsMap.get(name);	
		}else{
			obj = attrsMap.get(name);	
		}
		
		String value = "";
		if(obj!=null){
			value = obj.toString();
		}else{
			value = conn.getString(i,"ATTR_DEFAULT_VALUE");
		}
		out.println("addAttr('"+name+"','"+label+"','"+value+"','"+r+"','"+s+"');");
	}
%>
	if(tagAttrArray.length == 0){
		alert("没有找到该标签的属性.");
		self.close();
	}
	buildView();
<%
} catch (SQLException e) {
	System.out.print("取标签的属性出错!" + e);
	%>
		alert('取标签的属性出错,请检查!原因是:\n<%=e.getMessage().replaceAll("\n", "").replaceAll("'","")%>');
	<%	
}
%>
</script>
<input type="button" class="cms_button" onClick="setAttrs()" value="确认">
<input type="button" class="cms_button" onClick="window.close()" value="取消">
</form>
<iframe id="blockbox" name="blockbox" style="display:none"></iframe>
</body>
</html>
