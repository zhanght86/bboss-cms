
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ page import="com.frameworkset.dictionary.Data" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityConstants" %>
<%@ page import="org.frameworkset.spi.BaseSPIManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>
<%@ page import="com.frameworkset.common.poolman.sql.*"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager,
				 com.frameworkset.platform.config.ConfigManager,
				 com.frameworkset.platform.dictionary.DictAttachField"%>
<%@ page import="java.util.*"%>			

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>	 

<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);	
	boolean isAdminFlag = accesscontroler.isAdmin();
	int i = 0;
	String currentOrgName = accesscontroler.getChargeOrg() != null ?accesscontroler.getChargeOrg().getRemark5():"";
    //机构编号
	String currentOrgIdNO = accesscontroler.getChargeOrgCode();
    //当前用户所属机构ID
    String currentOrgId = accesscontroler.getChargeOrgId();
	String did = (String)request.getAttribute("did");	
	if(did == null){
		did = request.getParameter("did");
	}
	//tab页
	//session.setAttribute("dictTabId", "2");
	//数据字典名称
	String dicttype_name = "";
	Data dtype = null;
	DictManager dictManager = null;
	if(did != null){
		dictManager = new DictManagerImpl();
		dtype = dictManager.getDicttypeById(did);
		dicttype_name = dtype.getName();
	}
	if(dtype == null){
		dtype = new Data();
	}	
	String dbname = dtype.getDataDBName();
    String tablename = dtype.getDataTableName();
    String dataNameField = dtype.getDataNameField();
    String dataValueField = dtype.getDataValueField();
    //新维护两个字段
    String data_name_cn = dtype.getField_name_cn()==""?RequestContextUtils.getI18nMessage("sany.pdp.dictmanager.db.field.name.gather.name.chinese.name", request):dtype.getField_name_cn();
    String data_value_cn = dtype.getField_value_cn()==""?RequestContextUtils.getI18nMessage("sany.pdp.dictmanager.db.field.name.gather.value.chinese.name", request):dtype.getField_value_cn();
    
    //字典类型说明
    String description = dtype.getDescription();
    //数据项是否有效,在列表中体现,同时显示"停用/启用功能"
    boolean showDataValidate = false;
    String dataValidate = dtype.getData_validate_field();
    //数据项所属机构,可多个,缺省是登陆用户的机构,在新增的时候体现,在列表体现
    boolean showDataValueOrg = false;
    String dataValueOrg = dtype.getData_create_orgid_field();
    
    //是否有排序字段,决定是否显示排序按钮
    boolean showOrderButton = false;
    String dataOrderField = dtype.getDataOrderField();
    if(dataOrderField != null && dataOrderField.trim().length()>0){
        showOrderButton = true;
    }
    
    //字典数据:名称字段对象
    ColumnMetaData nameObj = DBUtil.getColumnMetaData(dbname,tablename,dataNameField);
    //字典数据:值字段对象
    ColumnMetaData valueObj = DBUtil.getColumnMetaData(dbname,tablename,dataValueField);
    
    String nameValidType = dictManager.getValidatorTypeByColumnMetaData(nameObj);
    String valueValidType = dictManager.getValidatorTypeByColumnMetaData(valueObj);
    
    String dataValidateType = "";
    String dataValidateTypeName = "";
    String dataOrgType = "";
    String dataOrgTypeName = "";
    if(dataValidate != null && dataValidate.trim().length()>0){
        //字段数据:是否有效
        showDataValidate = true;
    	ColumnMetaData validateObj = DBUtil.getColumnMetaData(dbname,tablename,dataValidate);
        dataValidateType = dictManager.getValidatorTypeByColumnMetaData(validateObj);
        dataValidateTypeName = validateObj.getTypeName();
    }
    if(dataValueOrg != null && dataValueOrg.trim().length()>0){        
    	//字段数据:数据项所属机构
    	showDataValueOrg = true;
    	ColumnMetaData dataOrgObj = DBUtil.getColumnMetaData(dbname,tablename,dataValueOrg);
        dataOrgType = dictManager.getValidatorTypeByColumnMetaData(dataOrgObj);
        dataOrgTypeName = dataOrgObj.getTypeName();
    }
    
    //不能为空的 并且没有被使用的 数据库表字段
    String unableNullColumnNames = "";
    if(dtype!=null){
    	unableNullColumnNames = dictManager.getUnableNullColumnNames(dtype);
    }    
	
    //查看采集的数据是否要过滤, 不是自己采集的,不能编辑修改删除....
    boolean needGatherFilter = dictManager.isDicttypeGatherMustFilter(dtype);
	int dicttype_type = dtype.getDicttype_type();
	
	//启用停用灰掉开关
	boolean state = false;
	
	boolean stateDel = false;
	
	List dictatts = dictManager.getDictdataAttachFieldList(did,-1);
	
	String rootpath = request.getContextPath();

%>
<html>
	<head>
		<title>属性容器</title>
		<script language="javascript" src="../scripts/selectTime.js"></script>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="../user/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
		<script language="javascript" src="js/checkUnique.js"></script>
		<script language="JavaScript"
			src="<%=request.getContextPath()%>/public/datetime/calender_date.js"></script>
		<script language="javascript" src="../scripts/selectTime.js"></script>	
  
<SCRIPT language="javascript">
	var jsAccessControl = new JSAccessControl("#DAE0E9","#F6F8FB","#F6F8FB");
	var win;
	var featrue = "dialogWidth=600px;dialogHeight=500px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;help=0;dialogLeft="+(screen.availWidth-600)/2+";dialogTop="+(screen.availHeight-500)/2;
	function unable(){
	    if(document.all.newbt) document.all.newbt.disabled = true;
		if(document.all.up) document.all.up.disabled = true;
        if(document.all.down) document.all.down.disabled = true;        
        if(document.all.top) document.all.top.disabled = true;
        if(document.all.bottom) document.all.bottom.disabled = true;
        if(document.all.save_order) document.all.save_order.disabled = true;
        if(document.all.deletebt) document.all.deletebt.disabled = true;
        if(document.all.changeState) document.all.changeState.disabled = true;
	}
	function closeWin(){
	    win.close();
	}
	function enable(){	  
	    if(document.all.newbt) document.all.newbt.disabled = false;
		if(document.all.up) document.all.up.disabled = false;
        if(document.all.down) document.all.down.disabled = false;        
        if(document.all.top) document.all.top.disabled = false;
        if(document.all.bottom) document.all.bottom.disabled = false;
        if(document.all.save_order) document.all.save_order.disabled = false;
        if(document.all.deletebt) document.all.deletebt.disabled = false;
        if(document.all.changeState) document.all.changeState.disabled = false;
	}
	function afterAddRefresh(queryString){
	    queryDictItem(queryString);//document.location.href = document.location.href;
	}
	function afterDeleteRefresh(queryString){
	    afterAddRefresh(queryString);
	}
	function dealRecord(dealType) {
	    var isSelect = false;
	    var outMsg;
		var selecet_value = "";   
		//var arr = document.getElementsByName("dictDataId");
		var arr = document.frames[0].document.getElementsByName("dictDataId");
		document.all("queryString").value = document.frames[0].document.all("queryString").value;
	    for (var i=0;i<arr.length;i++) {
			if (arr[i].checked){
		       	isSelect=true;

		       	if(selecet_value=="") selecet_value = selecet_value + encodeURIComponent(encodeURIComponent(arr[i].value)); 
		       	else selecet_value = selecet_value + "," + encodeURIComponent(encodeURIComponent(arr[i].value));
			}
	    }
	    if (isSelect){
	    	if (dealType==1){
	    		outMsg = '<pg:message code="sany.pdp.dictmanager.dict.delete.confirm"/>';
	        	
	    		$.dialog.confirm(outMsg, function() {
	    			unable();					
					var path = "dictDeletedata.jsp?did=<%=did%>&infos="+selecet_value;
                    //win = window.showModelessDialog("doing.jsp","",featrue);
					document.dictList.action = path;
					document.dictList.submit();
	    		});
			} 
	    }else{
	    	$.dialog.alert('<pg:message code="sany.pdp.common.operation.select.one"/>');
	    	return false;
	    }
		return false;
	}	
	function newDict(){
	    var unableNullColumnNames = "<%=unableNullColumnNames%>";
	    if(unableNullColumnNames.length>0){
	        var msg = "<%=unableNullColumnNames%>"+'<pg:message code="sany.pdp.dictmanager.db.field.notnull"/>';
			msg = msg + '<pg:message code="sany.pdp.dictmanager.db.field.needconfig"/>';
		    $.dialog.alert(msg);
		    return false;
	    }
		if (validateForm(dictList) ){		
			unable();
			var path = "dictSavedata.jsp?did=<%=did%>";	
			win = window.showModelessDialog("doing.jsp","",featrue);
			document.dictList.action = path;
			document.dictList.submit();
			
		}
	}
	//高级添加
	function advance_newDict(){
	    var unableNullColumnNames = "<%=unableNullColumnNames%>";
	    if(unableNullColumnNames.length>0){
	        var msg = "<%=unableNullColumnNames%>"+'<pg:message code="sany.pdp.dictmanager.db.field.notnull"/>';
			msg = msg + '<pg:message code="sany.pdp.dictmanager.db.field.needconfig"/>';
		    $.dialog.alert(msg);
		    return false;
	    }
	    
	    var url="<%=request.getContextPath()%>/sysmanager/dictmanager/newWin_dictdata.jsp?dicttypeId=<%=did%>";
		$.dialog({title:'<pg:message code="sany.pdp.dictmanager.data.gather.add"/>',width:420,height:280, content:'url:'+url});   	
	    
		/*
	    var path = "newWin_dictdata.jsp?dicttypeId=<%=did%>";
	    var featrue = "dialogWidth=600px;dialogHeight=500px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;help=0;dialogLeft="+(screen.availWidth-600)/2+";dialogTop="+(screen.availHeight-500)/2;
	    //var win = window.open(path)
	    var win = window.showModalDialog(path,window,featrue);
	    if(win=="ok"){
	        window.location.href = window.location.href;
	    }
	    */
	}
	
	function topTr()
	{
		//var all = document.getElementsByName("dictDataId");
		var all = document.frames[0].document.getElementsByName("dictDataId");
		var flag = 0;
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				flag ++;
				if(flag > 1)
				{
					$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.move.single"/>');
					return false;
				}
			}
		}
		if(flag < 1)
		{
			$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.move"/>');
			return false;
		}
		for(var i=0;i<all.length;i++){
			upTr();
		}
	}
	function upTr()
	{
		//var all = document.getElementsByName("dictDataId");
		var all = document.frames[0].document.getElementsByName("dictDataId");
		var row;
		var flag = 0;
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				row = all[i];
				flag ++;
				if(flag > 1)
				{
					$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.move.single"/>');
					return false;
				}
			}
		}
		if(flag < 1)
		{
			$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.move"/>');
			return false;
		}
		var tmp = row;
		upRow(row);
		tmp.checked = true;
	}
	function downTr()
	{
		//var all = document.getElementsByName("dictDataId");
		var all = document.frames[0].document.getElementsByName("dictDataId");
		var row;
		var flag = 0;
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				row = all[i];
				flag ++;
				if(flag > 1)
				{
					$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.move.single"/>');
					return false;
				}
			}
		}
		if(flag < 1)
		{
			$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.move"/>');
			return false;
		}
		var tmp = row;
		downRow(row);
		tmp.checked = true;
	}
	function base()
	{
		//var all = document.getElementsByName("dictDataId");
		var all = document.frames[0].document.getElementsByName("dictDataId");
		var flag = 0;
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				flag ++;
				if(flag > 1)
				{
					$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.move"/>');
					return false;
				}
			}
		}
		if(flag < 1)
		{
			$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.move"/>');
			return false;
		}
		for(var i=0;i<all.length;i++){
			downTr();
		}
	}
	//表格row上移
	function upRow(e){
		var _row=e.parentElement.parentElement;
		if(_row.previousSibling.previousSibling)
		{
			//alert(_row.previousSibling.outerHTML);
			swapNode(_row,_row.previousSibling);
		}
	}
	//表格row下移(说明注释性的文字也算 一个nextSibling）
	function downRow(e)
	{
		//通过链接对象获取表格行的引用
		var _row=e.parentElement.parentElement;
		//如果不是最后一行，则与下一行交换顺序
		if(_row.nextSibling.nextSibling)
		{	
			//alert(_row.nextSibling.outerHTML);
			swapNode(_row,_row.nextSibling);
		}

	}
	//
	function swapNode(node1,node2){
		
		if (node1.id == 'head' || node2.id == 'head') {
			return;
		}
		
		//获取父结点
		var _parent=node1.parentNode;
		//获取两个结点的相对位置
		var _t1=node1.nextSibling;
		var _t2=node2.nextSibling;
		//将node2插入到原来node1的位置
		if(_t1)_parent.insertBefore(node2,_t1);
		else _parent.appendChild(node2);
		//将node1插入到原来node2的位置
		if(_t2)_parent.insertBefore(node1,_t2);
		else _parent.appendChild(node1);
	}
	//保存置顶顺序
	function subform()
	{
		//var all = document.getElementsByName("dictDataId");
		var all = document.frames[0].document.getElementsByName("dictDataId");
		var docid="";
		var falg = false;
		for(var i=0;i<all.length;i++){
			if(falg){
				docid += "," + all[i].value;
			}else{
				docid += all[i].value;
				falg = true;
			}
		}
		if(docid == "")
		{
			$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.data.null"/>');
			return false;
		}
		
		document.forms[0].action = "save_dict_arr.jsp?did=<%=did%>&docid=" + docid;
		document.forms[0].target = "hiddenFrame";
		document.forms[0].submit();
	}

	function changeState_(flag){
		var all = document.frames[0].document.getElementsByName("dictDataId");
		if(all.length==0){
			$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.data.null"/>');
			return false;
		}
		var docid="";
		falg=false;
		for(var i=0;i<all.length;i++){
			if(all[i].checked){
				if(falg){
					docid += "," + all[i].value;
				}else{
					docid = all[i].value;
					falg=true;
				}
			}
		}
		if(docid == "")
		{
			$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.data.select"/>');
			return false;
		}
		document.all.docid.value = docid;
		document.forms[0].action = "changeStatelist.jsp?did=<%=did%>&flag="+flag;
		document.forms[0].target = "hiddenFrame";
		document.forms[0].submit();
	}
	
	function listQuery(){
		var path = "dictAttachList.jsp?did=<%=did%>";
		//window.open(path);
		var winQuery = window.showModalDialog(path,window,"dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;");
	}
	
	//修改
	function advance_EditDict(){
		var obj=document.frames[0].document.getElementsByName("dictDataId");
		var queryString = document.frames[0].document.all("queryString").value;
		var checkValue = "";
		var n = 0;
		var win = "";
		var validateState = "";
		for(var i = 0; i < obj.length; i++){
			var objText = document.getElementById("a"+i);
			if(obj[i].checked){
				checkValue = obj[i].value;
				if(objText){
					validateState = objText.innerText;
				}
				n++;
				if(n > 1){
					$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.data.modfiy.select.one"/>');
					return;
				}
			}
		}
		if(checkValue!=""){
			
			var url="<%=request.getContextPath()%>/sysmanager/dictmanager/EditWin_dictdata.jsp?dicttypeId=<%=did%>&checkValue="+encodeURIComponent(encodeURIComponent(checkValue))+"&validateState="+validateState;
			$.dialog({title:'<pg:message code="sany.pdp.dictmanager.data.gather.modfiy"/>',width:420,height:280, content:'url:'+url});   	
			
		    //var path = "EditWin_dictdata.jsp?dicttypeId=<%=did%>&checkValue="+checkValue+"&validateState="+validateState;
		    //var featrue = "dialogWidth=600px;dialogHeight=500px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;help=0;dialogLeft="+(screen.availWidth-600)/2+";dialogTop="+(screen.availHeight-500)/2;
		    //window.open(path)
		    //win = window.showModalDialog(path,window,featrue);
	    }else{
	    	$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.data.modfiy.select.one"/>');
	    	return;
	    }
	}
	var num = 1;
	function queryDictItem(queryString)
	{
		var showdata = document.all.showdata.value;
		var realitydata = document.all.realitydata.value;
		var occurOrg = "";
		if(document.all.occurOrg){
			occurOrg = document.all.occurOrg.value;
		}
		var isaVailability = "-1";
		if(document.all.isaVailability){
			isaVailability = document.all.isaVailability.value;
		}
		document.getElementById("numCount").value = num;
		//var path = "dictList.jsp?did=<%=did%>&showdata="+showdata+"&realitydata="+realitydata+"&occurOrg="+occurOrg+"&isaVailability="+isaVailability;
		//dictList.action=path;
		//document.location.href=path;
		//dictList.submit();
		dictList.target = "dictListIframe";
		var path = "dictList_iframe.jsp?did=<%=did%>";
		if(queryString!=-1){
			path += "&" + queryString; 
		}
		dictList.action=path;
		dictList.submit();	
		//var tablesFrame= document.getElementsByName("dictListIframe");
		//tablesFrame[0].src = path;
	}
	
	function resetQuery(){
		document.all.showdata.value="";
		document.all.realitydata.value="";
		if(document.all.occurOrg){
			document.all.occurOrg.value="";
		}
		if(document.all.isaVailability){
			document.all.isaVailability.value="-1";
		}
	}
	
	function highLevel(){        
		if(document.all.highLevelButtonSpan.innerText == '<pg:message code="sany.pdp.dictmanager.option.advance"/>'){
            document.getElementById("highLevel1").style.display = "block";
            document.all.highLevelButtonSpan.innerText = '<pg:message code="sany.pdp.dictmanager.option.normal"/>';
        }else{
            document.getElementById("highLevel1").style.display = "none";
            document.all.highLevelButtonSpan.innerText = '<pg:message code="sany.pdp.dictmanager.option.advance"/>';
        }
    }  
    window.onload = function initPage(){
        //document.all.highLevelButton.value = "高级条件";
        //document.getElementById("searchAdv").style.display = "none";
        document.all.isHeighlevel.value = "common";
    }
    
    //改变条件生成相应的html
    function queryConditionChange(obj,index,targetObj){
    	//选中条件的值
    	var selectConditionValue = obj.options[obj.selectedIndex].value;
    	var type = obj.options[obj.selectedIndex].optype;
    	var dictfield = obj.options[obj.selectedIndex].dictfield;
    	
    	var htmlStr = "";
    	if(type=="-1"){
    		htmlStr = "<input name='advancedvalue"+index+"' value='' type='text' />";
    	}else if(type=="选择机构" || type=="当前机构"){
    		htmlStr = "<input type='hidden' name='advancedvalue"+index+"' value=''>" +
    			"<input name='advancedvalue"+index+"_name' readonly='true' org_id='advancedvalue"+index+"' onclick='selectOrg(this)' style='width:200px' />";
    		targetObj.innerHTML = htmlStr;
    	}else if(type=="文本类型" || type=="text"){
    		htmlStr = "<input type='text' name='advancedvalue"+index+"' value='' style='width:200px'>";
    		targetObj.innerHTML = htmlStr;
    	}else if(type=="选择字典"){
    		var fieldNames = dictfield.split(":");
    		var fname = "";
			var dtypeId = "";
			var opcode = "";
			if(fieldNames.length==3){
				fname = fieldNames[0];
				dtypeId = fieldNames[1];
				opcode = fieldNames[2];
			}
    	    htmlStr = "<input type='hidden' name='advancedvalue" + index + "' />" +
    	    	"<iframe scrolling='no' frameborder='0' marginwidth='1' height='25' width='' name='dictSelect"+index+"' src='selectDict.jsp?dtypeId="+dtypeId+"&columnName=advancedvalue"+index +"&index"+index+"'></iframe>";    
    	    targetObj.innerHTML = htmlStr;
    	}else if(type=="选择时间" || type=="当前时间"){ 	
 			var htmlStr = "查询起止时间:" ;
			htmlStr += "<input type='text' name='startDate"+ index +"' size='20' onclick='selectTime(\"document.all.startDate"+ index +"\",0)' readonly='true'>";
			htmlStr +="到<input type='text' name='endDate"+ index +"' size='20' onclick='selectTime(\"document.all.endDate"+ index +"\",0)' readonly='true'>";
 			targetObj.innerHTML = htmlStr;
    	}else if(type=="当前用户" || type=="选择人员"){
    		htmlStr = "<input type='hidden' name='advancedvalue"+index+"' value=''>" +
    			"<input name='advancedvalue"+index+"_name' readonly='true' org_id='advancedvalue"+index+"' onclick='selectUser(this)' style='width:200px' />";
    		targetObj.innerHTML = htmlStr;
    	}else{
    		htmlStr = "<input name='advancedvalue"+index+"' value='' />";
    		targetObj.innerHTML = htmlStr;
    	}
    	
    }
    
    function returnHtmlStr(obj,index,advancedName){
    	//选中条件的值
    	var type = obj.options[obj.selectedIndex].optype;
    	var dictfield = obj.options[obj.selectedIndex].dictfield;

    	var htmlStr = "";
    	var advancedvalueObj = document.all.item(advancedName);
    	var startDD = "startDate" + index;
    	var endDD = "endDate" + index;
    	if(advancedvalueObj!=null || document.all.item(startDD)!=null || document.all.item(endDD)!=null){
	    	if(type=="-1"){
	    		htmlStr = "<input name='advancedvalue"+index+"' value='"+advancedvalueObj.value+"' type='text' class='w120'/>";
	    	}else if(type=="选择机构" || type=="当前机构"){
	    		var selectadvancedName = advancedName + "_name";
	    		htmlStr = "<input type='hidden' name='advancedvalue"+index+"' value='"+advancedvalueObj.value+"'>" +
	    			"<input name='advancedvalue"+index+"_name' readonly='true' org_id='advancedvalue"+index+"' onclick='selectOrg(this)' class='w120' value='"+document.all.item(selectadvancedName).value+"' />";
	  
	    	}else if(type=="文本类型" || type=="text"){
	    		htmlStr = "<input type='text' name='advancedvalue"+index+"' value='"+advancedvalueObj.value+"' class='w120'>";
	
	    	}else if(type=="选择字典"){
	    		var fieldNames = dictfield.split(":");
	    		var fname = "";
				var dtypeId = "";
				var opcode = "";
				if(fieldNames.length==3){
					fname = fieldNames[0];
					dtypeId = fieldNames[1];
					opcode = fieldNames[2];
				}
	    	    htmlStr = "<input type='hidden' name='advancedvalue" + index + "' value='"+advancedvalueObj.value+"' />" +
	    	    	"<iframe scrolling='no' frameborder='0' marginwidth='1' height='25' width='' name='dictSelect"+index+"' src='selectDict.jsp?dtypeId="+dtypeId+"&columnName=advancedvalue"+index +"&selectedId="+advancedvalueObj.value+"'></iframe>";    
	
	    	}else if(type=="选择时间" || type=="当前时间"){
	    		var startD = "startDate" + index;
	    		var endD = "endDate" + index;	
	 			var htmlStr = "查询起止时间:" ;
				htmlStr += "<input type='text' name='startDate"+ index +"' size='20' onclick='selectTime(\"document.all.startDate"+ index +"\",0)' readonly='true' value='"+document.all.item(startD).value+"' class='w120'>";
				htmlStr +="到<input type='text' name='endDate"+ index +"' size='20' onclick='selectTime(\"document.all.endDate"+ index +"\",0)' readonly='true' value='"+document.all.item(endD).value+"' class='w120'>";
	    	}else if(type=="当前用户" || type=="选择人员"){
	    		var selectadvancedName = advancedName + "_name";
	    		htmlStr = "<input type='hidden' name='advancedvalue"+index+"' value='"+advancedvalueObj.value+"'>" +
	    			"<input name='advancedvalue"+index+"_name' readonly='true' org_id='advancedvalue"+index+"' onclick='selectUser(this)' class='w120' value='"+document.all.item(selectadvancedName).value+"' />";
	    	}else{
	    		htmlStr = "<input name='advancedvalue"+index+"' value='"+advancedvalueObj.value+"' class='w120'/>";
	    	}
    	}else{
	    	if(type=="-1"){
	    		htmlStr = "<input name='advancedvalue"+index+"' value='' type='text' class='w120'/>";
	    	}else if(type=="选择机构" || type=="当前机构"){
	    		htmlStr = "<input type='hidden' name='advancedvalue"+index+"' value=''>" +
	    			"<input name='advancedvalue"+index+"_name' readonly='true' org_id='advancedvalue"+index+"' onclick='selectOrg(this)' class='w120'/>";
	  
	    	}else if(type=="文本类型" || type=="text"){
	    		htmlStr = "<input type='text' name='advancedvalue"+index+"' value='' class='w120'>";
	
	    	}else if(type=="选择字典"){
	    		var fieldNames = dictfield.split(":");
	    		var fname = "";
				var dtypeId = "";
				var opcode = "";
				if(fieldNames.length==3){
					fname = fieldNames[0];
					dtypeId = fieldNames[1];
					opcode = fieldNames[2];
				}
	    	    htmlStr = "<input type='hidden' name='advancedvalue" + index + "' />" +
	    	    	"<iframe scrolling='no' frameborder='0' marginwidth='1' height='25' width='' name='dictSelect"+index+"' src='selectDict.jsp?dtypeId="+dtypeId+"&columnName=advancedvalue"+index +"'></iframe>";    
	
	    	}else if(type=="选择时间" || type=="当前时间"){ 	
	 			var htmlStr = "查询起止时间:" ;
				htmlStr += "<input type='text' name='startDate"+ index +"' size='20' onclick='selectTime(\"document.all.startDate"+ index +"\",0)' readonly='true' class='w120' >";
				htmlStr +="到<input type='text' name='endDate"+ index +"' size='20' onclick='selectTime(\"document.all.endDate"+ index +"\",0)' readonly='true' class='w120' >";
	    	}else if(type=="当前用户" || type=="选择人员"){
	    		htmlStr = "<input type='hidden' name='advancedvalue"+index+"' value=''>" +
	    			"<input name='advancedvalue"+index+"_name' readonly='true' org_id='advancedvalue"+index+"' onclick='selectUser(this)' class='w120' />";
	    	}else{
	    		htmlStr = "<input name='advancedvalue"+index+"' value='' class='w120'/>";
	    	}
    	}
    	
    	return htmlStr;
    }
    
    
    function changeFlag(nFlag,condition){
    	//如果num是小于且状态为0返回
		if(num <=1 && nFlag == 0){
			return;
		}
		if(nFlag == 1){
			num ++;
		}else if(nFlag == 0){
			num --;
		}
		if(num <1){
			$.dialog.alert('no');
			return ;
		}
		var str = "<table id='searchAdv' width='100%' border='0' cellpadding='0' cellspacing='0'  class='table2'>";
		//alert(num)
		for(var i = 1; i <= num; i ++ ){
			str += '<tr><th width="86px"><pg:message code="sany.pdp.dictmanager.data.advance"/>：</th><td width="373px"><select id="selectColumn'+i+'" name="highLevelColumn'+i+'"'
				+ 'onchange="queryConditionChange(this,'+i+',document.getElementById(\'query'+i+'\'))" class="w120" >'
				+ '<option value="">--<pg:message code="sany.pdp.common.operation.select"/>--</option>';
			<% 
					for(int z = 0; z < dictatts.size(); z++){
						DictAttachField dictatt = (DictAttachField)dictatts.get(z);
						String columnName = dictatt.getTable_column().toLowerCase();
						String dictField = dictatt.getDictField();
						String type = dictatt.getInputTypeName();
				%>
					str += '<option dictfield="<%=dictField %>" optype="<%=type%>" value="<%=columnName %>|<%=type%>"><%=dictatt.getDictFieldName()%></option>';
				<% 
					}
				%>
			
			str += '</select></td>';
			if(condition == 1){
				if(i < num){
					var obj = document.getElementById("selectColumn" + i);
					var targetObj = document.getElementById("query" + i);
					var advanced = "advancedvalue" + i;
					str += "<th>&nbsp;</th><td><div id='query"+i+"'>";
					str += returnHtmlStr(obj,i,advanced);
					str += "</div></td>";
				}else{
					str += "<th>&nbsp;</th><td><div id='query"+i+"'><input name='advancedvalue"+i+"' value='' type='text' class='w120'/></div></td>";
				}
			}else{
				var obj = document.getElementById("selectColumn" + i);
				var targetObj = document.getElementById("query" + i);
				var advanced = "advancedvalue" + i;
				str += "<th>&nbsp;</th><td><div id='query"+i+"'>";
				str += returnHtmlStr(obj,i,advanced);
				str += "</div></td>";
			}
			str += "</tr>";
			//str +="<td width='16%' height='30' colspan='1' align='center' valign='middle'>";
			//	str += "<select name=logical"+ i+ "><option value='and' selected><pg:message code="sany.pdp.common.and"/></option><option value='or'><pg:message code="sany.pdp.common.or"/></option><option value='and not'><pg:message code="sany.pdp.common.include.not"/></option></select>";
			//str += "</td></tr>";
		}
		str += '<tr><th>&nbsp;</th><td>&nbsp;</td><th>&nbsp;</th><td>'
			+'<a href="javascript:void(0)" class="bt_2" id="add"  onclick="changeFlag(1,1)"><span>+</span> </a>'
			+'<a href="javascript:void(0)" class="bt_2" id="reduce"  onclick="changeFlag(0,0)"><span>-</span> </a>'
			+'</td></tr>';
		str += '</table>';
		rememberSelectedField();
		//rememberInputLogic();
		
		document.all.highLevel1.innerHTML = str;
		selectField(condition);
		//selectLogic();
    }
    
    var arrSelectIndex = "";
	function rememberSelectedField()
	{
		arrSelectIndex = "";
		for(var i=1;i<=num;i++)
		{
			var selectID = "selectColumn" + i;
			if (document.all( selectID ) != null)
			{
				var selectedIndex = document.all( selectID ).selectedIndex;
				arrSelectIndex += selectedIndex + "|";
			}
		}
		//去除最后一个|字符
		if (arrSelectIndex.length > 1)
		{
			arrSelectIndex = arrSelectIndex.substring(0,arrSelectIndex.length - 1);
		}	
		
	}

	function selectField(condition)
	{
	    var arrIndex = arrSelectIndex.split('|');
		for(var i=1;i<= num;i++)
		{
			var selectID = "selectColumn" + i;
			//下拉框的长度
			var j = document.all( selectID ).length;

			if( j > i ){
				j = i-1;
			}else{
				j = j -1;
			}

			if(condition == 1)
			{	
				if(i == num)
				{
					document.all( selectID ).options(0).selected = true;
				}
				else
				{
					document.all( selectID ).options(parseInt(arrIndex[i-1])).selected = true;
				}
			}
			else if(condition == 0)
			{
				document.all( selectID ).options(parseInt(arrIndex[i-1])).selected = true;
			}
		}
	}
	
	var arrInputLogic = "";
	function rememberInputLogic()
	{
		arrInputLogic = "";
		for(var i=1;i<= num;i++)
		{
			var selectID = "logical" + i;
			if (document.all( selectID ) != null)
			{
				arrInputLogic += document.all( selectID ).value + "|";
			}
		}
		if (arrInputLogic.length > 1)
		{
			arrInputLogic = arrInputLogic.substring(0,arrInputLogic.length - 1);
		}	
	}

	function selectLogic()
	{
	   if (arrInputLogic=='') return;
		var arrIndex = arrInputLogic.split('|');
		for(var i=1;i<= num;i++)
		{
			if (arrIndex[i-1] != null && arrIndex[i-1] != '')
			{
				var selectID = "logical" + i;
				document.all( selectID ).value=arrIndex[i-1];
			}
		}
	}
</SCRIPT>
	<body>
		<sany:menupath menuid="dictmanage_content_0"/>
		<form name="dictList" method="post" target="hiddenFrame">
		<input type="hidden" name="isHeighlevel" id="isHeighlevel" />
		<input type="hidden" name="numCount" id="numCount" />
		<input type="hidden" name="docid">
		<div class="mcontent">
			<div id="searchblock">
				<div  class="search_top">
			    	<div class="right_top"></div>
			    	<div class="left_top"></div>
	      		</div>
				<div class="search_box">
					<table width="98.4%" border="0" cellspacing="0" cellpadding="0">
						<tr>
		      				<td class="left_box"></td>
		      				<td>
		      					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
		      						<tr>
		      							<th><%=data_name_cn%>：</th>
		      							<td><input name="showdata" value="" type="text"  class="w120" /></td>
		      							<th><%=data_value_cn%>：</th>
		      							<td><input name="realitydata" value="" type="text"  class="w120" /></td>
		      						</tr>
		      						<tr>
		      							<%if(showDataValueOrg){%>
		      							<th><pg:message code="sany.pdp.dictmanager.data.gather.org"/>：</th>
		      							<td><input name="occurOrg" value="" type="text"  class="w120" /></td>
		      							<%}%>
		      							<%if(showDataValidate){%>
		      							<th><pg:message code="sany.pdp.dictmanager.data.enable"/>：</th>
		      							<td>
		      								<select name="isaVailability" class="w120" >
									    		<option value="-1">--<pg:message code="sany.pdp.common.operation.select"/>--</option>
									    		<option value="1"><pg:message code="sany.pdp.dictmanager.data.enable.yes"/></option>
									    		<option value="0"><pg:message code="sany.pdp.dictmanager.data.enable.no"/></option>
									    	 </select>
		      							</td>
		      							<%}%>
		      						</tr>
		      						<tr>
		      							<td colspan="4">
		      								<div id="highLevel1" style="display:none">
		      								<table id="searchAdv" width="100%" border="0" cellpadding="0" cellspacing="0"  class="table2">
		      									<tr>
		      										<th width="86px"><pg:message code="sany.pdp.dictmanager.data.advance"/>：</th>
		      										<td width="373px">
		      											<select id="selectColumn1" name="highLevelColumn1" 
															onchange="queryConditionChange(this,1,document.getElementById('query1'))" class="w120">
															<option value="" optype="-1">--<pg:message code="sany.pdp.common.operation.select"/>--</option>
															<% 
																for(int z = 0; z < dictatts.size(); z++){
																	DictAttachField dictatt = (DictAttachField)dictatts.get(z);
																	String columnName = dictatt.getTable_column().toLowerCase();
																	String dictField = dictatt.getDictField();
																	String type = dictatt.getInputTypeName();
															%>
																<option dictfield="<%=dictField %>" optype="<%=type%>" value="<%=columnName %>|<%=type%>"><%=dictatt.getDictFieldName()%></option>
															<% 
																}
															%>
															</select>
		      										</td>
		      										<th>&nbsp;</th>
		      										<td>
		      											<div id="query1">
															<input name="advancedvalue1" value="" type="text"  class="w120"/>
														</div>
		      										</td>
		      									</tr>
		      									<tr>
		      										<th>&nbsp;</th>
		      										<td>&nbsp;</td>
		      										<th>&nbsp;</th>
		      										<td>
		      											<a href="javascript:void(0)" class="bt_2" id="add"  onclick="changeFlag(1,1)"><span>+</span> </a>
		      											<a href="javascript:void(0)" class="bt_2" id="reduce"  onclick="changeFlag(0,0)"><span>-</span> </a>
		      										</td>
		      									</tr>
		      								</table>
		      								</div>
		      							</td>
		      						</tr>
		      						<tr>
		      							<th>&nbsp;</th>
		      							<td>&nbsp;</td>
		      							<th>&nbsp;</th>
		      							<td>
		      								<a href="javascript:void(0)" class="bt_1" id="highLevelButton"  onclick="highLevel()"><span id="highLevelButtonSpan"><pg:message code="sany.pdp.dictmanager.option.advance"/></span> </a>
		      								<a href="javascript:void(0)" class="bt_1" id="queryBut"  onclick="queryDictItem('-1')"><span><pg:message code="sany.pdp.common.operation.search"/></span> </a>
		      								<a href="javascript:void(0)" class="bt_2" id="resetBut"  onclick="resetQuery()"><span><pg:message code="sany.pdp.common.operation.reset"/></span> </a>
		      							</td>
		      						</tr>
		      					</table>
		      				</td>
		      				<td class="right_box"></td>
		      			</tr>
					</table>
				</div>
				<div class="search_bottom">
					<div class="right_bottom"></div>
					<div class="left_bottom"></div>
				</div>
			</div>
			
			<div class="title_box">
				<div class="rightbtn">
					<%
 				        if(showOrderButton){
 				        //accesscontroler.checkPermission("", "", AccessControl.DICTGATHER_RESOURCE){
 				    %>
 				    	<a href="javascript:void(0)" class="bt_small" id="top"  onclick="topTr()"><span><pg:message code="sany.pdp.dictmanager.data.move.top"/></span> </a>
 				    	<a href="javascript:void(0)" class="bt_small" id="up"  onclick="upTr()"><span><pg:message code="sany.pdp.dictmanager.data.move.up"/></span> </a>
 				    	<a href="javascript:void(0)" class="bt_small" id="down"  onclick="downTr()"><span><pg:message code="sany.pdp.dictmanager.data.move.down"/></span> </a>
 				    	<a href="javascript:void(0)" class="bt_small" id="bottom"  onclick="base()"><span><pg:message code="sany.pdp.dictmanager.data.move.bottom"/></span> </a>
 				    	<a href="javascript:void(0)" class="bt_small" id="bottsub"  onclick="subform()"><span><pg:message code="sany.pdp.dictmanager.data.sort.save"/></span> </a>
 				    <%
 				        //}
 				        }
 				    %>
					<%
						if(showDataValidate){
							if (!state) {
					%>
						<a href="javascript:void(0)" class="bt_small" id="changeState1"  onclick="changeState_(1)"><span><pg:message code="sany.pdp.dictmanager.data.enable.yes"/></span> </a>
						<a href="javascript:void(0)" class="bt_small" id="changeState0"  onclick="changeState_(0)"><span><pg:message code="sany.pdp.dictmanager.data.enable.no"/></span> </a>
 				    <%
							} else {
					%>	
					
					<%		
							}
 				         }
 				    %>
 				    <%
			 			 if(dicttype_type==DictManager.PARTREAD_BUSINESS_DICTTYPE){
			 			     ResourceManager resManager = new ResourceManager();
							 String resId = dtype.getDataId();
							 if (resId != null && !resId.equals("") && accesscontroler.checkPermission(resId,
			   							"datamanage", AccessControl.DICT_RESOURCE)){ //采集数据
			 			 %>    	
			 			 	 <a href="javascript:void(0)" class="bt_small" id="advance_newbt"  onclick="advance_newDict()"><span><pg:message code="sany.pdp.common.operation.add"/></span> </a>							    
						 <%
						     }
						     if (resId != null && !resId.equals("") && accesscontroler.checkPermission(resId,
			   							"edit", AccessControl.DICT_RESOURCE)){ //数据编辑
						 %>	 		 
						 	 <a href="javascript:void(0)" class="bt_small" id="advance_updatebt"  onclick="advance_EditDict()"><span><pg:message code="sany.pdp.common.operation.modfiy"/></span> </a>							    
						 <%
						     }
						     if (resId != null && !resId.equals("") && accesscontroler.checkPermission(resId,
			   							"delete", AccessControl.DICT_RESOURCE)){ //数据删除
						 %>
						 <%if(!stateDel){%>
						 	<a href="javascript:void(0)" class="bt_small" id="deletebt"  onclick="javascript:dealRecord(1);"><span><pg:message code="sany.pdp.common.batch.delete"/></span> </a>
						 	<%}%>							    
						 <%
						     }
						 }else{
						 %>	 
						 	<a href="javascript:void(0)" class="bt_small" id="advance_newbt"  onclick="advance_newDict()"><span><pg:message code="sany.pdp.common.operation.add"/></span> </a>							    
						    <a href="javascript:void(0)" class="bt_small" id="advance_updatebt"  onclick="advance_EditDict()"><span><pg:message code="sany.pdp.common.operation.modfiy"/></span> </a>							    
						    <%if(!stateDel){%>
						    <a href="javascript:void(0)" class="bt_small" id="deletebt"  onclick="javascript:dealRecord(1);"><span><pg:message code="sany.pdp.common.batch.delete"/></span> </a>							    
						    <%}%>
						 <%
						 }
						 %>
						 <input name="queryString" value="" type="hidden"/>
						 	<!--  <a href="javascript:void(0)" class="bt_small" id="queryG"  onclick="listQuery()"><span>新窗口打开</span> </a>-->							    
				</div>
				<strong><pg:message code="sany.pdp.dictmanager.data.list"/></strong>
			</div>
			
			<iframe name="dictListIframe" src="dictList_iframe.jsp?did=<%=did%>" style="width:100%" height="800px" scrolling="auto" frameborder="0" marginwidth="1" marginheight="1"></iframe>
			
		</div>
		
		</form>
		
	</body>
	<iframe height="0" width="0" name="hiddenFrame"></iframe>	
	<script type="text/javascript">   
  
function reinitIframe(){   
  
var iframe = document.getElementById("dictListIframe");   
  
try{   
  
iframe.height =  iframe.contentWindow.document.documentElement.scrollHeight;   
  
}catch (ex){}   
  
}   
  
window.setInterval("reinitIframe()", 200);   
  
</script>  
</html>