<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../include/global1.jsp"%>

<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.dictionary.Data" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.dictionary.input.InputTypeManagerImpl" %>
<%@ page import="com.frameworkset.platform.dictionary.input.InputTypeManager" %>
<%@ page import="com.frameworkset.platform.dictionary.input.InputType" %>
<%@ page import="com.frameworkset.platform.dictionary.DictAttachField" %>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>
<%@ page import="com.frameworkset.common.poolman.sql.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
    String dicttypeId = (String)request.getParameter("dicttypeId");    
    String tableColumn = (String)request.getParameter("tableColumn");
    dicttypeId = dicttypeId==null?"":dicttypeId;
    tableColumn = tableColumn==null?"":tableColumn;
    DictManager dictManager = new DictManagerImpl();    
    Data dicttype = null;
    DictAttachField dictatt = null;
    String dictName = "";
    String dbname = "";
    String tablename = "";
    boolean isNullstate = true;
    ColumnMetaData metaData = null;
    if(dicttypeId.trim().length()>0){
    	dicttype = dictManager.getDicttypeById(dicttypeId);
    	dictName = dicttype.getName();
    	dbname = dicttype.getDataDBName();
    	tablename = dicttype.getDataTableName();
    	if(tableColumn.trim().length()>0){
    	    dictatt = dictManager.getDictAttachFieldInfo(dicttypeId,tableColumn);
    	    metaData = DBUtil.getColumnMetaData(dbname,tablename,tableColumn);
    	    isNullstate = !"no".equalsIgnoreCase(metaData.getNullable());
    	}
    }    
    String dictField = dictatt==null?"":dictatt.getDictField();
    String dictFieldName = dictatt==null?"":dictatt.getDictFieldName();
    String inputTypeId = dictatt==null?"":dictatt.getInputTypeId();
    String inputTypeName = dictatt==null?"":dictatt.getInputTypeName();
    String table_column = dictatt==null?"":dictatt.getTable_column();
    
    String isnullable = dictatt==null?"":String.valueOf(dictatt.getIsnullable());
    String isunique = dictatt==null?"":String.valueOf(dictatt.getIsunique());
    //System.out.println("isunique = " + isunique);
    //判断改字段是否已经保存了数据,如果保存了,不允许修改,否则允许修改
    boolean hasData = false;    
    if(dictField.trim().length()>0){
       hasData = true;
        
    }   
    
    InputTypeManager inputImpl = new InputTypeManagerImpl();
    List inputList = inputImpl.getInputTypeList();
    
    //表对象
    TableMetaData tableObj = DBUtil.getTableMetaData(dbname,tablename);
    String tableComments = tableObj.getRemarks();
    String tableName = tableObj.getTableName();
%>  
<html>
    <head>        
        <title>【<%=dictName%>】设置附加字段</title>
        <link href="<%=rootpath%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">		
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
		<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="../../user/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
    </head>
    
    <body> 
        <form name="from1" method="post">
	    <table cellspacing="1" cellpadding="0" border="0" width=98%  class="thin">
	 		<tr valign='top'>
	   			<td height='30' valign='middle' style="text-align:center" colspan="6">
	   			    <img src='../../images/edit.gif' width="16" height="16" border=0 >&nbsp;<strong>【<%=dictName%>】设置附加字段 </strong>
	   			</td>
	 		</tr>	 		
	 		<tr >   					
   				<td height='30' colspan="1" style="width:20%;text-align:right">
					字典对应的表名:
				</td>
				<td height='30' colspan="1" valign='middle' style="text-align:left" >
				    <%=tableName%><br>
				    【<%=tableComments%>】
				</td>
			</tr >
			<tr>
				<td height='30' width="30%" colspan="1"  style="text-align:right">
					对应数据库字段*:
				</td>
				<td height='30' colspan="1" style="text-align:left" >
				    <input type="hidden" name="dictField"  value="<%=table_column%>" style="width:150px">
					<input readonly="true" type="text" name="table_column" value="<%=table_column%>" style="width:150px" onclick="javascript:setColumn(this);">
				</td>
	 		</tr>
			<tr>
				<td height='30' width="30%" colspan="1"  style="text-align:right">
					字典字段显示名称*:
				</td>
				<td height='30' colspan="1" style="text-align:left" >
					<input type="text" name="dictFieldName"  value="<%=dictFieldName%>" style="width:150px">
				</td>
	 		</tr>
	 		<tr>
				<td height='30' width="30%" colspan="1"  style="text-align:right">
					输入类型*:
				</td>
				<td height='30' colspan="1" style="text-align:left" >
					<select name="inputTypeId"  onchange="doSomething(this)"  style="width:150px">
					<%
					    for(int i=0;i<inputList.size();i++){
					        InputType type  = (InputType)inputList.get(i);
					%>
					    <option value="<%=type.getInputTypeId()%>"><%=type.getInputTypeName()%></option>
					<%    
					    }
					%>
					</select>&nbsp;&nbsp;&nbsp;&nbsp;<span id="att_html"></span>&nbsp;&nbsp;&nbsp;&nbsp;<span id="att_opcode"></span>
				</td>
	 		</tr>	 		
	 		<tr>
				<td height='30' width="30%" colspan="1"  style="text-align:right">
					字段是否为空*:
				</td>
				<td height='30' colspan="1" style="text-align:left" >
				    <select name="isnullable" value="<%=isnullable%>" style="width:150px" >
				        <option value="1">不可为空</option>
				        <%if(isNullstate){%>
				        <option value="0">可为空</option>	
				        <%}%>
				    </select>
				</td>
	 		</tr>
	 		<tr>
				<td height='30' width="30%" colspan="1"  style="text-align:right">
					字段是否唯一*:
				</td>
				<td height='30' colspan="1" style="text-align:left" >
				    <select name="isunique" value="<%=isunique%>" style="width:150px" >
				        <option value="1">唯一</option>
				        <option value="0">可重复</option>					        
				    </select>
				</td>
	 		</tr>	 		
	 		<tr>
	 		    <td height='30' width="30%" colspan="1"  style="text-align:right">					
				</td>
	 		    <td height='30' colspan="1" style="text-align:center" >
	 		    <%
	 		        if(hasData){
	 		    %>
	 		        <input type="button" value="保存" onclick="updateit()" class="input">
	 		    <%
	 		        }else{
	 		    %>
	 		        <input type="button" value="保存" onclick="saveit()" class="input">
	 		    <%
	 		        }
	 		    %>
	 		        <input type="button" value="关闭" onclick="window.close()" class="input">
	 		    </td>
	 		</tr>
	 		<input type="hidden" name="dicttypeId" value="<%=dicttypeId%>">
	 	</table>
	 	
	 	</form>
 	</body>
 	<script>
 	    var featrue = "dialogWidth=700px;dialogHeight=600px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;help=0;dialogLeft="+(screen.availWidth-700)/2+";dialogTop="+(screen.availHeight-600)/2;
 	    var restr = "";
 	    var win ;
 	    function checkForm(){
 	        var dictField = document.all.dictField.value.replace(/\s/ig, "");
 	        var dictFieldName = document.all.dictFieldName.value.replace(/\s/ig, "");
 	        var table_column = document.all.table_column.value.replace(/\s/ig, "");
 	        var isnullable = document.all.isnullable.value.replace(/\s/ig, "");
 	        if(dictField.length==0 || dictFieldName.length==0 || table_column.length==0 || isnullable.length==0){
 	            alert("带*的选项必须填写!");
 	            return false;
 	        }else{
 	            return true;
 	        }
 	        
 	    }
 	    function saveit(){
 	        var selectDicttye = "";
 	        var selectOpcode = "";
 	        //不同的输入类型,做不同的校验
 	        var op = document.all("inputTypeId").options;
 	        var selected_text = "";
 	        var selected_value = "";
 	        for(var i=0;i<op.length;i++){
 	            if(op[i].selected==true){
 	                selected_text = op[i].text;
 	                selected_value = op[i].value;
 	            }
 	        }
 	        
 	        if(document.all.selectDicttye){
 	            selectDicttye = document.all.selectDicttye.value; 
 	        } 	        
 	        if(document.all.selectOpcode){
 	            selectOpcode = document.all.selectOpcode.value.replace(/\s/ig, "");
 	        }
 	        if(selected_text=="主键生成"){
 	            if(selectDicttye=="sequence" && selectOpcode.length==0){
 	                alert("序列生成主键,必须填写序列名称");
 	                return false;
 	            }
 	            if(selectDicttye=="interface" && selectOpcode.length==0){
 	                alert("接口生成主键,必须填写接口的名称");
 	                return false;
 	            }
 	            
 	        }
 	        if(checkForm()){
 	            var path = "advanceFieldSave.jsp"; 	            
 	            if(selectDicttye.length>0 && selectOpcode.length>0){
 	                path = path + "?selectDicttye="+selectDicttye+"&opcode="+selectOpcode;
 	            } 	 	
 	            document.from1.action = path;        
	 	        document.from1.target = "hiddenFrame";
	 	        win = window.showModelessDialog("../doing.jsp","",featrue);
	 	        document.from1.submit();
	 	        
	 	    } 	        
 	    }
 	    function updateit(){
 	        var selectDicttye = "";
 	        var selectOpcode = "";
 	        //不同的输入类型,做不同的校验
 	        var op = document.all("inputTypeId").options;
 	        var selected_text = "";
 	        var selected_value = "";
 	        for(var i=0;i<op.length;i++){
 	            if(op[i].selected==true){
 	                selected_text = op[i].text;
 	                selected_value = op[i].value;
 	            }
 	        }
 	        
 	        if(document.all.selectDicttye){
 	            selectDicttye = document.all.selectDicttye.value; 
 	        } 	        
 	        if(document.all.selectOpcode){
 	            selectOpcode = document.all.selectOpcode.value.replace(/\s/ig, "");
 	        }
 	        if(selected_text=="主键生成"){
 	            if(selectDicttye=="sequence" && selectOpcode.length==0){
 	                alert("序列生成主键,必须填写序列名称");
 	                return false;
 	            }
 	            if(selectDicttye=="interface" && selectOpcode.length==0){
 	                alert("接口生成主键,必须填写接口的名称");
 	                return false;
 	            }
 	            
 	        }
 	        if(checkForm()){	 	     
 	            var path = "advanceFieldUpdate.jsp?op=update"; 	            
 	            if(selectDicttye.length>0 && selectOpcode.length>0){
 	                path = path + "&selectDicttye="+selectDicttye+"&opcode="+selectOpcode;
 	            }
 	            document.from1.action = path; 
	 	        document.from1.target = "hiddenFrame";
	 	        win = window.showModelessDialog("../doing.jsp","",featrue);
	 	        document.from1.submit();
	 	         
	 	    }	        
 	    }
 	    function setColumn(e){
 	        <%
 	        if(hasData){//如果是查看,不允许修改数据库表字段.
 	        %>
 	            return false;
 	        <%
 	        }
 	        %>
 	        var selected_value = e.value;
 	        var path = "select_tableField.jsp?dicttypeId=<%=dicttypeId%>&column="+selected_value;
 	        var win = window.showModalDialog(path,window,featrue);
 	        //alert(win);
 	        var wins = win.split(":");
 	        e.value = wins[0];
 	        document.all.dictField.value = wins[0];
 	        if(wins.length >= 2){
	 	        if("不能为空"==wins[1]){
		 	    	document.all.isnullable.options.remove(1);
		 	    	document.all.isnullable.selectindex=0;
		 	    }
	 	    }else if(document.all.isnullable.options.length<2){
	 	    	var temp = document.createElement("OPTION");   
	 	    	temp.text="可为空";
	 	    	temp.value="0";
	 	    	document.all.isnullable.options.add(temp);
		 	    document.all.isnullable.selectindex=1;
	 	    }
	 	    if(wins.length >= 3){
	 	    	if("unique"==wins[2]){
		 	    	document.all.isunique.options.remove(1);
		 	    	document.all.isunique.selectindex=0;
		 	    }
	 	    }else if(document.all.isunique.options.length<2){
	 	    	var temp = document.createElement("OPTION");   
	 	    	temp.text="可重复";
	 	    	temp.value="0";
	 	    	document.all.isunique.options.add(temp);
		 	    document.all.isunique.selectindex=1;
	 	    }
	 	        
 	    }
 	    function afterSave(){ 
 	        window.returnValue = "ok";
 	        window.close();
 	    }
 	    function saveFaild(){ 	        
 	        window.close();
 	    }
 	    function doSomething(){
 	        var op = document.all("inputTypeId").options;
 	        var selected_text = "";
 	        var selected_value = "";
 	        for(var i=0;i<op.length;i++){
 	            if(op[i].selected==true){
 	                selected_text = op[i].text;
 	                selected_value = op[i].value;
 	            }
 	        }
 	        
 	        if(selected_text=="选择字典"){
 	             creatDictHTML();
 	           
 	        }else if(selected_text=="主键生成"){
 	            //主键生成的规则: 自动生成的sequence
 	            creatPKHTML();
 	             
 	        }
 	        else if(selected_text=="当前时间" || selected_text=="选择时间")
 	        {
 	        	 //指定时间格式
 	            creatTimeFormatHTML();
 	        }
 	        else{
 	            document.all("att_html").innerHTML = "";
 	            document.all("att_opcode").innerHTML = "";  
 	            document.all.dictField.value = document.all.table_column.value;
 	        }
 	    }
 	    window.onload = function initPage(){
 	        var inputTypeId = "<%=inputTypeId%>"; 	       
 	        setSelectValue("inputTypeId",inputTypeId);
 	        
 	        var isnullable = "<%=isnullable%>"; 	        
 	        setSelectValue("isnullable",isnullable);
 	        
 	        var isunique = "<%=isunique%>";
 	        setSelectValue("isunique",isunique);
 	        
 	        //选择字典 或者 主键生成 赋值
 	        var dictFields = "<%=dictField%>";
 	        var inputTypeName = "<%=inputTypeName%>";
 	        if("选择字典"==inputTypeName){
 	            
 	            var dictID = "";
 	            var dictArrange = "";
 	            var arr0 = new Array();
 	            arr0 = dictFields.split(":");
 	            if(arr0.length==3){
 	                dictID = arr0[1];
 	                dictArrange = arr0[2];
 	            }
 	            creatDictHTML();
 	            setSelectValue("selectDicttye",dictID);
 	            setSelectValue("selectOpcode",dictArrange);
 	                
 	        }else if("主键生成"==inputTypeName){
 	            
 	            //主键生成的规则: 自动生成的sequence
 	            var dictID = "";
 	            var dictArrange = "";
 	            var arr0 = new Array();
 	            arr0 = dictFields.split(":");
 	            if(arr0.length==3){
 	                dictID = arr0[1];
 	                dictArrange = arr0[2];
 	            }
 	            creatPKHTML();
 	            setSelectValue("selectDicttye",dictID);
 	            setPKValue(dictID,dictArrange);
 	        }else if("当前时间"==inputTypeName || "选择时间"==inputTypeName){
 	            var dictID = "";
 	            var dictArrange = "";
 	            var arr0 = new Array();
 	            arr0 = dictFields.split(":");
 	            if(arr0.length==3){
 	                dictID = arr0[1];
 	                dictArrange = arr0[2];
 	            }
 	            creatTimeFormatHTML();
 	            setSelectValue("selectDicttye",dictID);
 	            setPKValue(dictID,dictArrange);
 	        }else{
 	            document.all("att_html").innerHTML = "";
 	            document.all("att_opcode").innerHTML = "";
 	        }
 	    }
 	    
 	    function creatDictHTML(){
 	        document.all("att_html").innerHTML = "";
 	        document.all("att_opcode").innerHTML = "";
 	        var html = '<select name="selectDicttye"  style="width:180px">';
 	        <%
 	               List businessTypes = dictManager.getChildDicttypeList("0");
 	               for(int i=0;i<businessTypes.size();i++){
 	                   Data dtype = (Data)businessTypes.get(i);
 	        %>
 	                  html = html + '<option value="<%=dtype.getDataId()%>"><%=dtype.getName().trim()%></option>'
 	        <%        
 	               }
 	        %>
 	        html = html + "</select>";
 	        var opcodeHtml = '<select name="selectOpcode"  style="width:50px">';
 	        opcodeHtml = opcodeHtml + "<option value='all'>所有</option>";
 	        opcodeHtml = opcodeHtml + "<option value='read'>可见</option>";
 	        opcodeHtml = opcodeHtml + "<option value='usual'>常用</option>";
 	        opcodeHtml = opcodeHtml + "</select>";
 	           
 	        document.all("att_html").innerHTML = html;
 	        document.all("att_opcode").innerHTML = opcodeHtml; 
 	    }
 	    
 	    function creatPKHTML(){
 	        document.all("att_html").innerHTML = "";
 	        document.all("att_opcode").innerHTML = "";
 	        var html = "<select name='selectDicttye' onclick='showText(this)' style='width:120px'>";
 	        html = html + "<option value='auto'>自动生成</option>";
 	        html = html + "<option value='sequence'>sequence生成</option>";
 	        html = html + "<option value='interface'>接口生成</option>";
 	        html = html + "<option value='handle'>手工录入</option>";
 	        html = html + "</select>"; 	        
 	        document.all("att_html").innerHTML = html; 
 	        	
    	        
 	    }
 	    
 	    function creatTimeFormatHTML(){
 	        document.all("att_html").innerHTML = "";
 	        document.all("att_opcode").innerHTML = "";
 	        var html = "<select name='selectDicttye'>";
 	        html = html + "<option value='yyyy-MM-dd HH:mm:ss' >yyyy-MM-dd HH:mm:ss</option>";
 	        html = html + "<option value='yyyy-MM-dd' >yyyy-MM-dd</option>";
 	        html = html + "</select>"; 	        
 	        document.all("att_html").innerHTML = html; 
 	        	
    	        
 	    }
 	    //主键生成的 填写的输入框
 	    function showText(e){
 	        if(e.value=='sequence' || e.value=='interface'){
 	            var opcodeHtml = '<input type="text" name="selectOpcode" value=""  style="width:150px" />';
 	            document.all("att_opcode").innerHTML = opcodeHtml;
 	        }else{
 	            document.all("att_opcode").innerHTML = "";
 	        }
 	         	        
 	    }
 	    
 	    function setSelectValue(selectName,v){ 	    
 	        var arr = document.all(selectName).options; 	        
 	        for(var i=0;i<arr.length;i++){
 	            if(arr[i].value==v){
 	                arr[i].selected = true;
 	            }
 	        }
 	    }
 	    function setPKValue(keyType,keyValue){
 	        if(keyValue!=""){
 	            var opcodeHtml = '<input type="text" name="selectOpcode" value=""  style="width:150px" />';
 	            document.all("att_opcode").innerHTML = opcodeHtml;
 	        }else{
 	            document.all("att_opcode").innerHTML = "";
 	        } 
 	        if(document.all.selectOpcode){
 	            var selectedValue = "";
 	            var arr = document.all("selectDicttye").options; 	        
	 	        for(var i=0;i<arr.length;i++){
	 	            if(arr[i].selected == true){
	 	                selectedValue = arr[i].value;
	 	                break;
	 	            }
	 	        }
	 	        if(keyType==selectedValue){
 	                document.all.selectOpcode.value = keyValue;
 	            }else{
 	                document.all.selectOpcode.value = "";
 	            }
 	        }
 	    }
 	    
 	    
 	    
 	</script>
 	<iframe name="hiddenFrame" src="" width=0 height=0></iframe>
</html>
