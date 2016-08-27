<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>
<%@ page import="com.frameworkset.common.poolman.sql.TableMetaData"%>
<%@ page import="com.frameworkset.platform.dictionary.DictManager"%>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl"%>
<%@ page import="com.frameworkset.dictionary.Data"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
    session.setAttribute("dictTabId", "1");
	//did 字典类型ID
	String did = request.getParameter("did");
	did = did==null?"":did;
    DictManager dicManager = new DictManagerImpl();
    Data dicttype = dicManager.getDicttypeById(did);
    int dicttypeUseTabelState = dicttype.getDicttypeUseTabelstate();
    boolean hasData = dicManager.dictTypeHasDatas(did);
	String promptStr = "";
	if(hasData){
	    promptStr = RequestContextUtils.getI18nMessage("sany.pdp.dictmanager.dict.data.type.warning.exist", request);
	}
    
    String field_name_cn = dicttype.getField_name_cn()==""?RequestContextUtils.getI18nMessage("sany.pdp.dictmanager.db.field.name.gather.name.chinese.name", request):dicttype.getField_name_cn();
    String field_value_cn = dicttype.getField_value_cn()==""?RequestContextUtils.getI18nMessage("sany.pdp.dictmanager.db.field.name.gather.value.chinese.name", request):dicttype.getField_value_cn();
    
    String type_name = dicttype.getName();    
    String type_desc = dicttype.getDescription();
    String datadbName = dicttype.getDataDBName();
    String dataTabelname = dicttype.getDataTableName();
    String dataNameField = dicttype.getDataNameField();
    String dataValueField = dicttype.getDataValueField();
    String dataOrderField = dicttype.getDataOrderField();
    String dataTypeIdField = dicttype.getDataTypeIdField();
    String dataParentIdFild = dicttype.getDataParentIdFild();
    String field_create_orgId = dicttype.getData_create_orgid_field();
    String field_data_validate = dicttype.getData_validate_field();
    
    //缓冲数据标识
    int needcache = dicttype.getNeedcache();
    //值字段的值可改变标识
    int enable_value_modify = dicttype.getEnable_value_modify();
    
    int is_tree = dicttype.getIsTree();
    int dicttype_type = dicttype.getDicttype_type();
    type_name = type_name==null?"":type_name;
    type_desc = type_desc==null?"":type_desc;    
    datadbName = datadbName==null?"":datadbName;
    dataTabelname = dataTabelname==null?"":dataTabelname;
    dataNameField = dataNameField==null?"":dataNameField;
    dataValueField = dataValueField==null?"":dataValueField;
    dataOrderField = dataOrderField==null?"":dataOrderField;
    dataTypeIdField = dataTypeIdField==null?"":dataTypeIdField;
    dataParentIdFild = dataParentIdFild==null?"":dataParentIdFild;    
	field_data_validate = field_data_validate==null?"":field_data_validate;
	field_create_orgId = field_create_orgId==null?"":field_create_orgId;
	
	int key_general_type = dicttype.getKey_general_type();
	
	List tlist = new ArrayList();
    if(!"".equals(did) && !"null".equals(did) ){		
		DBUtil db = new DBUtil();
		String sql = "select input_type_desc from tb_sm_inputtype where input_type_id =" + did;
		db.executeSelect(sql);
		for(int i=0;i<db.size();i++){
			tlist.add(db.getString(i,"input_type_desc"));
		}
	}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>字典类型添加</title>
  <script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>

<script language="javascript">
    //如果有数据,不允许修改
    var hasData = <%=hasData%>
	var sel = new Array();
    var win;
    var featrue = "dialogWidth=600px;dialogHeight=500px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;help=0;dialogLeft="+(screen.availWidth-600)/2+";dialogTop="+(screen.availHeight-500)/2;
	function getDbNames(e){
	    //if(hasData && e.value!=""){
            //return;
        //}
        
        var url="<%=request.getContextPath()%>/sysmanager/dictmanager/select_dbName.jsp?did=<%=did%>&dbname="+e.value;
    	$.dialog({title:'<pg:message code="sany.pdp.dictmanager.db.select"/>',width:300,height:300, content:'url:'+url}, function(returnValue) {
    	});  
        
		//var path = "select_dbName.jsp?did=<%=did%>&dbname="+e.value;
	    //win = window.showModalDialog(path,window,featrue);
	    //e.value = win;
	}
    function getTables(e){
        //if(hasData && e.value!=""){
            //return;
        //}
        
        
        var dbname = document.all.db_name.value;
        var ustate = document.all.createTypeForm.dicttypeUseTabelState.value;
        
        if(dbname!=""){
        	var url="<%=request.getContextPath()%>/sysmanager/dictmanager/tableList.jsp?dbname="+dbname+"&tablename="+e.value+"&ustate="+ustate;
    		$.dialog({title:'<pg:message code="sany.pdp.dictmanager.db.table.select"/>',width:600,height:500, content:'url:'+url}, function(returnValue) {
        	}); 
        	
			//var path = "select_tableName.jsp?did=<%=did%>&dbname="+dbname+"&tablename="+e.value;
			//win = window.showModalDialog(path,window,featrue);
			//e.value = win;
		}
	}	
    function getColumns(e){
        var selected_values = "";   
        //动态的字段处理
        //if(hasData && e.value!=""){
            //return;
        //}
        if(document.all.attachfieldname){
            var arr = new Array();
	        arr = document.getElementsByName("attachfieldname");
	     	for(var i=0;i<arr.length;i++){
	            if(selected_values=="") selected_values = selected_values+arr[i].value;
	            else selected_values = selected_values+","+arr[i].value;
	        }
        }        
        var dbname = document.all.db_name.value;
        var tablename = document.all.table_name.value;
        if(dbname!="" && tablename!=""){
	       
	       var url="<%=request.getContextPath()%>/sysmanager/dictmanager/select_tableField.jsp?dbname="+dbname+"&tablename="+tablename+"&column="+e.value+"&otherSelected="+selected_values+"&fieldName="+e.name;
    		$.dialog({title:'<pg:message code="sany.pdp.dictmanager.db.table.field.select"/>',width:700,height:500, content:'url:'+url}, function(returnValue) {
        	}); 
	       
	        //var path = "select_tableField.jsp?didcolumn=<%=did%>&dbname="+dbname+"&tablename="+tablename+"&column="+e.value+"&otherSelected="+selected_values+"&fieldName="+e.name;
			//win = window.showModalDialog(path,window,featrue);
			//e.value = win;
		}
    }
    
    
	function addRow(){
		var etable = document.all("EWTable");
		var row1 = etable.insertRow();
		var td1 = document.createElement("td");
		var td2 = document.createElement("td");
		var td3 = document.createElement("td");
		var td4 = document.createElement("td");
		td1.innerHTML = "<td align='center'><input type='checkbox' name='id' onclick ='checkOne(\"checkBoxAll\",\"id\")'/></td>"
		td2.innerHTML = "<td align='center'><input type='text' name='attachfieldname' readonly='true' onclick='getColumns(this)'></td>";
		td3.innerHTML = "<td align='center'><input type='text' name='attachlabel' value='' /></td>";
		td4.innerHTML = "<td align='center'><input type='text' name='attachinputtype' class='select'></td>";
        row1.appendChild(td1);
		row1.appendChild(td2);
		row1.appendChild(td3);
		row1.appendChild(td4);		
	}
	function delRow(){
		var etable = document.all("EWTable");
		var isSlected = false;
		if(document.all("id")){
			var del = document.all("id");
			for(var i=0;i<del.length;i++){
				if(del[i].checked){
				    isSlected  = true;
					etable.deleteRow(i+1);
					i=i-1;
				}
			}
		}
		if(!isSlected){
		    $.dialog.alert('<pg:message code="sany.pdp.dictmanager.db.field.select"/>');
		}
	}

	//删除总按钮行为
	function checkAll(totalCheck,checkName){
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
	   if(selectAll[0].checked==true){
		   for (var i=0; i<o.length; i++){
			  if(!o[i].disabled){
				o[i].checked=true;
			  }
		   }
	   }else{
		   for (var i=0; i<o.length; i++){
			  o[i].checked=false;
		   }
	   }
	}

	//各个删除按钮行为 
	function checkOne(totalCheck,checkName){
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
		var cbs = true;
		for (var i=0;i<o.length;i++){
			if(!o[i].disabled){
				if (o[i].checked==false){
					cbs=false;
				}
			}
		}
		if(cbs){
			selectAll[0].checked=true;
		}else{
			selectAll[0].checked=false;
		}
	}
	function unable(){
		document.all.newButton.disabled = true;
        document.all.saveButton.disabled = true;
        document.all.deleteButton.disabled = true;
        document.all.keyField.disabled = true;
	}
	function enable(){	    	    
		document.all.newButton.disabled = false;
        document.all.saveButton.disabled = false;
        document.all.deleteButton.disabled = false;
        document.all.keyField.disabled = false;
	}
    function highLevel(){        
        if(document.all.highLevelButtonSpan.innerText == '<pg:message code="sany.pdp.dictmanager.option.advance"/>'){
            document.getElementById("searchAdv").style.display = "block";
            document.all.highLevelButtonSpan.innerText = '<pg:message code="sany.pdp.dictmanager.option.normal"/>';
        }else{
            document.getElementById("searchAdv").style.display = "none";
            document.all.highLevelButtonSpan.innerText = '<pg:message code="sany.pdp.dictmanager.option.advance"/>';
        }    
    }    
    //初始化select
    function initSelect(did){
       if(did != "") {
       	   getfields.location.href = "dictSelectsInit.jsp?did="+did;
       }
    }
    function newDict(did){
    	
    	$.dialog.confirm('<pg:message code="sany.pdp.dictmanager.dict.modfiy.giveup.confirm"/>', function() {
    		window.location.href = "newDict.jsp?did="+did;
    	});
    }  
    
    function saveit(){
	    var dicttypeUseTabelState = document.all.dicttypeUseTabelState.value;
	    if(document.all.type_name.value==""){
	        $.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.type.name.notnull"/>');
	        document.all.type_name.focus();
	        return false;
	    }
	    if(document.all.type_dsc.value==""){
	        $.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.type.description.notnull"/>');
	        document.all.type_dsc.focus();
	        return false;
	    }
	    var arr = document.getElementsByName("is_tree");	 
	    var dicttypeUseTabelState = document.getElementsByName("dicttypeUseTabelState");   
	    if(document.all.db_name && document.all.db_name.value !="" &&  
	      document.all.table_name && document.all.table_name.value !=""){
	        
	        if((document.all.field_name && document.all.field_name.value=="" ) ||  
	           (document.all.field_value && document.all.field_value.value=="") 
	          
	        ){
	            $.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.field.notnull"/>');
		        return false;
	        }	    
		                    
            if(arr[0].checked==true && (document.all.field_parentid && document.all.field_parentid.value=="" ) ){
                $.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.field.mustfull"/>');
		        return false;
            }            
            if(dicttypeUseTabelState==1){
                $.dialog.alert('<pg:message code="sany.pdp.dictmanager.db.table.used.other"/>');
                return false;
            }else if(dicttypeUseTabelState==2){
                if(document.all.field_typeid && document.all.field_typeid.value==""){
					$.dialog.alert('<pg:message code="sany.pdp.dictmanager.db.table.used.other.field"/>');
	                return false;                    
                }
            }
            //选了更新数据, 必须填写字典类型字段
            var update_dcitData_typeId;
            if(document.all.update_dcitData_typeId) update_dcitData_typeId = document.all.update_dcitData_typeId;
            if(update_dcitData_typeId.checked && document.all.field_typeid && document.all.field_typeid.value==""){
                $.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.field.notnull.update"/>');
	            return false;
            }
            
	    }
	    var is_tree = 0;
	    if(arr[0].checked==true){
	        is_tree = 1;
	    }
		unable();
		
		var path = "dictSaveType.jsp?did=<%=did%>&opt=update&is_tree="+is_tree;
		var updateTypeids = document.getElementsByName("update_dcitData_typeId");
		if(updateTypeids[0]){
		    if(updateTypeids[0].checked==true){
		        path = path + "&update_typeId=1"
		    }
		}
		//win = window.showModelessDialog("doing.jsp","",featrue);
		document.createTypeForm.action = path;
		document.createTypeForm.submit();		
		
	}
	function deleteit(){
	    var delete_dictdata = 0;
	    var msg = '<pg:message code="sany.pdp.dictmanager.dict.delete.confirm"/>';
	    
	    $.dialog.confirm(msg, function() {
	    	unable();
			var path = "dictSaveType.jsp?did=<%=did%>&opt=delete&delete_dictdata="+delete_dictdata;
			//win = window.showModelessDialog("doing.jsp","",featrue);
			document.createTypeForm.action = path;
			document.createTypeForm.submit();	
	    });
    }
    function afterUpdateRefresh(did){
        window.location.href = window.location.href
        var path = getNavigatorContent().location.href;
        var index = path.indexOf("&did=");
        if(index > 0){
            path = path.substring(0,index);
        }
        path = path +"&did="+did;
        getNavigatorContent().location.href=path;
        
    }
    function afterDeleteRefresh(){
        window.location.href = "newDict.jsp?did=0";
        getNavigatorContent().location.href=getNavigatorContent().location.href;
    }
    /**
     * e 触发改变显示的对象
     * labelDiv 改变显示的label的DIV名称
     * filedDiv 改变显示的字段的DIV名称
     * changeFiled 改变显示的字段名称
     */
    function changeDisplay(e,labelDiv,filedDiv,changeFiled){ 
        var str = e.name;      
        if(document.getElementsByName(str)){        
            var arr = document.getElementsByName(str);
            if(arr[0].checked==true){
                if(document.getElementsByName(filedDiv)){
                    document.getElementsByName(labelDiv)[0].style.display="block";
                    document.getElementsByName(filedDiv)[0].style.display="block";
                }
            }else{
                if(document.getElementsByName(filedDiv)){
                    document.getElementsByName(changeFiled)[0].value="";
                    document.getElementsByName(labelDiv)[0].style.display="none";
                    document.getElementsByName(filedDiv)[0].style.display="none";
                }
            }
        }
    } 
    window.onload = function initPage(){
        document.getElementById("searchAdv").style.display = "none";
        document.all.highLevelButtonSpan.innerText = '<pg:message code="sany.pdp.dictmanager.option.advance"/>';
        
        var is_tree = <%=is_tree%>;        
        if(is_tree==1){
            var arr = document.getElementsByName("is_tree"); 
            if(arr[0]){ 
	            arr[0].checked=true;
	            if(document.all.field_parentid){
		            document.all.parentIdField.style.display="block";
		            document.all.parentIdLabel.style.display="block";
		        }
	        }
        }
        var dicttype_type="<%=dicttype_type%>"
        var types = document.all("dicttype_type").options;
        for(var i=0;i<types.length;i++){
            if(types[i].value==dicttype_type){
                types[i].selected = true;
                break;
            }
        }  
        
        var key_general_type = "<%=key_general_type%>";
        var keytypes = document.all("key_general_type").options;
        for(var i=0;i<keytypes.length;i++){
            if(keytypes[i].value==key_general_type){
                keytypes[i].selected = true;
                break;
            }
        }
    }
    
    function keyDefine(dicttypeId){
    	//unable();
    	var keywin;
    	var dbname = document.all.db_name.value;
        var tablename = document.all.table_name.value;
        
        var url="<%=request.getContextPath()%>/sysmanager/dictmanager/keyFieldDefine.jsp?dictTypeId="+dicttypeId+"&dbname="+dbname+"&tablename="+tablename;
    	$.dialog({title:'<pg:message code="sany.pdp.dictmanager.dict.key.field.define"/>',width:800,height:500, content:'url:'+url});   
        
        
        //var url = "keyFieldDefine.jsp?dictTypeId="+dicttypeId+"&dbname="+dbname+"&tablename="+tablename;
        //keywin = window.showModalDialog(url,window,"dialogWidth=800px;dialogHeight=500px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;help=0;dialogLeft="+(screen.availWidth-600)/2+";dialogTop="+(screen.availHeight-500)/2);
        //if(keywin=="ok"){
        	//window.location.reload();
       // }
    }
    
    //如果选择缓冲字典数据，那么值字段就不允许选择修改。值字段允许修改，那么字典数据就不能缓冲。
    function onclikHiden(ck){
    	var evm = "enable_value_modify";
    	var nee = "needcache";
    	var ckName = ck.name;
    	if (ckName == evm) {
    		var ckOther = document.getElementsByName(nee)[0];
    		if(ck.checked){
    			ckOther.checked = false;
    			ckOther.disabled = "disabled";
    		}else{
    			ckOther.disabled = "";
    			
    		}
    	} else if (ckName == nee) {
    		var tree = document.getElementsByName("is_tree")[0];
    		if(tree.checked){
    			ck.checked = true;
    			$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.tree.cache.must"/>');
    			return false;
    		}
    		
    		var ckOther = document.getElementsByName(evm)[0];
    		if(ck.checked){
    			ckOther.checked = false;
    			ckOther.disabled = "disabled";
    		}else{
    			ckOther.disabled = "";
    		}
    	} else {
    		//如果选择字典数据为树型，那么字典数据必须缓冲。
    		var ckOther1 = document.getElementsByName(nee)[0];
    		var ckOther2 = document.getElementsByName(evm)[0];
    		if(ck.checked){
	    		ckOther1.checked = true;
	    		ckOther2.checked = false;
	    		ckOther2.disabled = "disabled";
    		}else{
    			ckOther1.checked = false;
    			ckOther1.disabled = "";
    			ckOther2.disabled = "";
    		}
    	}
    }
</script>
</head>

<body>
<sany:menupath menuid="dictmanage_info"/>
<div class="mcontent">
	<div id="searchblock">
     	<div>
     		<form name="createTypeForm" method="post" target="savetype">
     			<input type="hidden" name="dicttypeUseTabelState" value="<%=dicttypeUseTabelState%>">
     			<table width="98.4%" border="0" cellspacing="0" cellpadding="0">
     				<tr>
		      			<td class="left_box" ></td>
		      			<td>
		      				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
		      					<tr>
		      						<th width="198px"><pg:message code="sany.pdp.dictmanager.dict.type.name"/>：</th>
		      						<td> <input type="text" name="type_name" size="25" class="w120" value="<%=type_name%>"/><font color="red">*</font></td>
		      						<th width="168px"><pg:message code="sany.pdp.dictmanager.dict.type.description"/>：</th>
		      						<td> 
		      							<textarea name="type_dsc" row=2  class="w120" value="<%=type_desc%>"><%=type_desc%></textarea><font color="red">*</font>
		      						</td>
		      					</tr>
		      					<tr>
		      						<td colspan="4">
		      							<table id="searchAdv" width="100%" border="0" cellpadding="0" cellspacing="0"  class="table2">
		      								<tr>
		      									<th><pg:message code="sany.pdp.dictmanager.db"/>：</th>
		      									<td><input type="text" readonly="true" name="db_name"  class="w120" onclick="getDbNames(this)" value="<%=datadbName%>" /><font color="red">*</font></td>
		      									<th><pg:message code="sany.pdp.dictmanager.db.table"/>：</th>
		      									<td><input type="text" readonly="true" id="table_name" name="table_name" class="w120" onclick="getTables(this)" value="<%=dataTabelname%>" /><font color="red">*</font></td>
		      								</tr>
		      								<tr>
		      									<th><pg:message code="sany.pdp.dictmanager.db.field.name"/>：</th>
		      									<td><input type="text" readonly="true" id="field_name" name="field_name"  class="w120" onclick="getColumns(this)" value="<%=dataNameField%>" /><font color="red">*</font></td>
		      									<th><pg:message code="sany.pdp.dictmanager.db.field.value"/>：</th>
		      									<td>
		      										<input type="text" readonly="true" id="field_value" name="field_value"  class="w120" onclick="getColumns(this)"  value="<%=dataValueField%>"/><font color="red">*</font>
		      											&nbsp;&nbsp;
		      										<pg:message code="sany.pdp.dictmanager.db.field.generate.rule"/>：
		      										 <select class="select" name="key_general_type" class="w120">
			        									<option value="0"><pg:message code="sany.pdp.dictmanager.db.field.generate.rule.record"/></option>
			        									<option value="1"><pg:message code="sany.pdp.dictmanager.db.field.generate.rule.auto"/></option>
			    									</select>
		      									</td>
		      								</tr>
		      								<tr>
		      									<th><pg:message code="sany.pdp.dictmanager.db.field.name.gather.name.chinese"/>：</th>
		      									<td><input type="text"  id="field_name_cn" name="field_name_cn" value="<%=field_name_cn%>"  class="w120" /></td>
		      									<th><pg:message code="sany.pdp.dictmanager.db.field.name.gather.value.chinese"/>：</th>
		      									<td><input type="text"  id="field_value_cn" name="field_value_cn" value="<%=field_value_cn%>"   class="w120" /></td>
		      								</tr>
		      								<tr>
		      									<th><pg:message code="sany.pdp.dictmanager.db.field.sort"/>：</th>
		      									<td><input type="text" readonly="true" id="field_order" name="field_order" class="w120" onclick="getColumns(this)" value="<%=dataOrderField%>" /></td>
		      									<th><pg:message code="sany.pdp.dictmanager.db.field.dicttype"/>：</th>
		      									<td><input type="text" readonly="true" id="field_typeid" name="field_typeid"  class="w120" onclick="getColumns(this)" value="<%=dataTypeIdField%>" /></td>
		      								</tr>
		      								<tr>
		      									<th><pg:message code="sany.pdp.dictmanager.db.field.data.token"/>：</th>
		      									<td><input type="text" readonly="true" id="field_data_validate" name="field_data_validate" class="w120" onclick="getColumns(this)" value="<%=field_data_validate%>" /></td>
		      									<th><pg:message code="sany.pdp.dictmanager.db.field.data.gather"/>：</th>
		      									<td><input type="text" readonly="true" id="field_create_orgId" name="field_create_orgId"  class="w120" onclick="getColumns(this)" value="<%=field_create_orgId%>" /></td>
		      								</tr>
		      								<tr>
		      									<th><pg:message code="sany.pdp.dictmanager.db.field.tree"/>：</th>
		      									<td><input type="checkbox" name="is_tree" onclick="changeDisplay(this,'parentIdLabel','parentIdField','field_parentid');onclikHiden(this);" /></td>
		      									<th><pg:message code="sany.pdp.dictmanager.db.field.modfiy"/>：</th>
		      									<td><input type="checkbox" name="enable_value_modify" value="1" <% if(enable_value_modify == 1) out.print("checked"); else if(needcache == 1) out.print("disabled=disabled"); %> onclick="onclikHiden(this)" />  </td>
		      								</tr>
		      								<tr>
		      									<th><pg:message code="sany.pdp.dictmanager.db.field.update"/>：</th>
		      									<td><input type="checkbox" name="update_dcitData_typeId" value="1"  /></td>
		      									<th><pg:message code="sany.pdp.dictmanager.db.field.cache"/>：</th>
		      									<td> <input type="checkbox" name="needcache" value="1" <% if(needcache == 1) out.print("checked"); else if(enable_value_modify == 1) out.print("disabled=disabled"); %> onclick="onclikHiden(this)" /></td>
		      								</tr>
		      								<tr>		    
												   <th>
											            <div id="parentIdLabel" style="display:none">
													    	<pg:message code="sany.pdp.dictmanager.db.field.id.parent"/>：
													    </div>
												    </th>    
												    <td>
												        <div id="parentIdField" style="display:none">			        
											      			<input type="text" id="field_parentid" name="field_parentid" readonly="true" class="w120" onclick="getColumns(this)" value="<%=dataParentIdFild%>" /><font color="red">*</font>
											      		</div>
													</td>
													<th>&nbsp;</th>
													<td>&nbsp;</td>
											    </tr>
		      							</table>
		      						</td>
		      					</tr>
		      					<tr>
		      						<th><pg:message code="sany.pdp.dictmanager.dict.type.class"/>：</th>
		      						<td>
		      							<select class="select" name="dicttype_type" value="<%=dicttype_type%>" class="w120">
			       							<option value="0"><pg:message code="sany.pdp.dictmanager.dict.type.class.base"/></option>
			       			 				<option value="1"><pg:message code="sany.pdp.dictmanager.dict.type.class.common"/></option>
			        						<option value="2"><pg:message code="sany.pdp.dictmanager.dict.type.class.authorize"/></option>
			        						<option value="4"><pg:message code="sany.pdp.dictmanager.dict.type.class.common.nokeep"/></option>
			        						<option value="5"><pg:message code="sany.pdp.dictmanager.dict.type.class.authorize.nokeep"/></option>
			    						</select>		  
		      						</td>
		      						<th>&nbsp;</th>
		      						<td>
		      							<a href="javascript:void(0)" class="bt_1" id="highLevelButton"  onclick="highLevel()"><span id="highLevelButtonSpan"><pg:message code="sany.pdp.dictmanager.option.advance"/></span> </a>
		      								  <%
												 if (accesscontroler.checkPermission(did,
										                   "edit", AccessControl.DICT_RESOURCE))
										        {%>		    
										        	<a href="javascript:void(0)" class="bt_1" id="saveButton" onclick="saveit()"><span><pg:message code="sany.pdp.common.operation.save"/></span> </a>
											    <%}%>	
											    <%
												 if (accesscontroler.checkPermission(did,
										                   "delete", AccessControl.DICT_RESOURCE))
										        {%>
													<a href="javascript:void(0)" class="bt_1" id="deleteButton" onclick="deleteit()"><span><pg:message code="sany.pdp.common.batch.delete"/></span> </a>
												<%}%>
												
												<%
												 if (accesscontroler.checkPermission(did,
										                   "addson", AccessControl.DICT_RESOURCE))
										        {%>
										        	<a href="javascript:void(0)" class="bt_1" id="newButton" onclick="newDict(<%=did%>)"><span><pg:message code="sany.pdp.common.operation.create"/></span> </a>
												<%}%>
												<%
													if(accesscontroler.isAdmin()){
												%>
													<a href="javascript:void(0)" class="bt_2" id="keyField" onclick="keyDefine(<%=did%>)"><span><pg:message code="sany.pdp.dictmanager.dict.key.field"/></span> </a>
												<%
													}
												%>	
		      						</td>
		      					</tr>
		      				</table>
		      			</td>
     			</table>
     		</form>
     	</div>
	</div>
		<br/>
		<div id="changeColor">
		<table width="98.5%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
			<pg:header>
				<th><pg:message code="sany.pdp.dictmanager.dict.type.class"/></th>
				<th><pg:message code="sany.pdp.dictmanager.dict.type.class.field.conf.advance"/></th>
				<th><pg:message code="sany.pdp.dictmanager.dict.type.class.base.gather"/></th> 
				<th><pg:message code="sany.pdp.dictmanager.dict.type.class.common.gather"/></th>
				<th><pg:message code="sany.pdp.dictmanager.dict.type.class.authorize.gather"/></th>
				<th><pg:message code="sany.pdp.dictmanager.dict.type.class.org.authorize.code.keep"/></th>
				<th><pg:message code="sany.pdp.dictmanager.dict.type.class.org.used.code.keep"/></th>
			</pg:header>
			<tr>	      	
				<td><pg:message code="sany.pdp.dictmanager.dict.type.class.base"/></td>
				<td><span style="color:red"><pg:message code="sany.pdp.dictmanager.dict.keep" /></span></td>
				<td><span style="color:red"><pg:message code="sany.pdp.dictmanager.dict.keep" /></span></td>
				<td><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td>
				<td><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td>
				<td><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td>
				<td><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td>
			</tr>
			<tr>	      	
				<td><pg:message code="sany.pdp.dictmanager.dict.type.class.common"/></td>
				<td><span style="color:red"><pg:message code="sany.pdp.dictmanager.dict.keep" /></span></td>
				<td><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td>
				<td><span style="color:red"><pg:message code="sany.pdp.dictmanager.dict.keep" /></span></td>
				<td><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td>
				<td><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td>
				<td><span style="color:red"><pg:message code="sany.pdp.dictmanager.dict.keep" /></span></td>
			</tr>
			<tr>
				<td><pg:message code="sany.pdp.dictmanager.dict.type.class.authorize"/></td>
				<td><span style="color:red"><pg:message code="sany.pdp.dictmanager.dict.keep" /></span></td>
				<td><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td> 
				<td ><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td>
				<td><span style="color:red"><pg:message code="sany.pdp.dictmanager.dict.keep" /></span></td>
				<td><span style="color:red"><pg:message code="sany.pdp.dictmanager.dict.keep" /></span></td>
				<td><span style="color:red"><pg:message code="sany.pdp.dictmanager.dict.keep" /></span></td>
			</tr>
			<tr>
				<td><pg:message code="sany.pdp.dictmanager.dict.type.class.common.nokeep"/></td>
				<td><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td>
				<td><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td> 
				<td><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td>
				<td><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td>
				<td><span style="color:red"><pg:message code="sany.pdp.dictmanager.dict.keep" /></span></td>
				<td><span style="color:red"><pg:message code="sany.pdp.dictmanager.dict.keep" /></span></td>
			</tr>
			<tr>
				<td><pg:message code="sany.pdp.dictmanager.dict.type.class.authorize.nokeep"/></td>
				<td><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td>
				<td><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td> 
				<td><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td>
				<td><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td>
				<td><pg:message code="sany.pdp.dictmanager.dict.nokeep" /></td>
				<td><span style="color:red"><pg:message code="sany.pdp.dictmanager.dict.keep" /></span></td>
			</tr>
		</table>
	</div>
</div>

</div>
</body>
<iframe id="getfields" name="getfields" border="0" width="0" height="0" src=""></iframe>
<iframe id="gettables" name="getfields" border="0" width="0" height="0" src=""></iframe>
<iframe id="savetype" name="savetype" border="0" width="0" height="0" src=""></iframe>

</html>

