<%@page import="java.net.URLDecoder"%>
<%     
  response.setHeader("Cache-Control", "no-cache"); 
  response.setHeader("Pragma", "no-cache"); 
  response.setDateHeader("Expires", -1);  
  response.setDateHeader("max-age", 0); 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri='/WEB-INF/dictionary.tld' prefix='dict'%>
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
<%@ page import="com.frameworkset.platform.dictionary.input.InputType"%>
<%@ page import="com.frameworkset.platform.dictionary.DictAttachField,
				com.frameworkset.platform.dictionary.input.InputTypeScript,
				com.frameworkset.platform.dictionary.input.InputTypeScriptImpl"%>
<%@ page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.Organization,
                 com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager,
                 com.frameworkset.platform.config.ConfigManager,
 				 com.frameworkset.platform.dictionary.DictKeyWordManager,
 				 com.frameworkset.platform.dictionary.DictKeyWordManagerImpl,
 				 com.frameworkset.platform.dictionary.KeyWord,
 				 com.frameworkset.platform.dictionary.input.InputTypeScript"%>
 				 
 <%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>			 
 				 
<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	DictKeyWordManager dictKeyWordManager = new DictKeyWordManagerImpl();
	KeyWord keyWord = null;
	String currentOrgName = accesscontroler.getChargeOrg() != null ?accesscontroler.getChargeOrg().getRemark5():"";
	String currentOrgIdNO = accesscontroler.getChargeOrgCode();
    String ids = (String)request.getParameter("dicttypeId");
    String validateState = request.getParameter("validateState");
    String[] id = ids.split(":");
    String dicttypeId = "";
    String parent_id = "";
    if(id !=null && id.length>1){
        dicttypeId = id[0];
        parent_id = id[1];
    }else{
    	dicttypeId = ids;
    }
    DictManagerImpl dictManager = new DictManagerImpl();
    //为了获取上线信息,做初始化.
    dictManager.init(request,response);
    Data dicttype = dictManager.getDicttypeById(dicttypeId);
    String dbname = dicttype.getDataDBName();
    String tablename = dicttype.getDataTableName();
    String dataNameField = dicttype.getDataNameField();
    String dataValueField = dicttype.getDataValueField();
    String dataValidate = dicttype.getData_validate_field();
    //是否显示 显示数据,真实数据
    String dictName = dicttype.getDataNameField()==null?"":dicttype.getDataNameField().trim();
    String dictValue = dicttype.getDataValueField()==null?"":dicttype.getDataValueField().trim();
    boolean showDictName = false;
    boolean showDictValue = false;   

    if(!"".equals(dictName)){
        showDictName = true;
    }    
    if(!"".equals(dictValue)){
        showDictValue = true;
    }
    
    //新维护两个字段
    String data_name_cn = dicttype.getField_name_cn()==""?RequestContextUtils.getI18nMessage("sany.pdp.dictmanager.db.field.name.gather.name.chinese.name", request):dicttype.getField_name_cn();
    String data_value_cn = dicttype.getField_value_cn()==""?RequestContextUtils.getI18nMessage("sany.pdp.dictmanager.db.field.name.gather.value.chinese.name", request):dicttype.getField_value_cn();
    
    //判断真实值是否可以修改
    boolean enableValeModify = dicttype.isEnableValueModify();
    
    //数据项所属机构,可多个,缺省是登陆用户的机构,在新增的时候体现,在列表体现
    boolean showDataValueOrg = false;
    String dataValueOrg = dicttype.getData_create_orgid_field();
    //表对象
    TableMetaData tableObj = DBUtil.getTableMetaData(dbname,tablename);
 //   String tableComments = tableObj.getRemarks();
 	String tableComments = dicttype.getDescription();//字典名称    
    String tableName = tableObj.getTableName();
    //字典数据:名称字段对象
    ColumnMetaData nameObj = DBUtil.getColumnMetaData(dbname,tablename,dataNameField);  
    
    //字典数据:值字段对象
    ColumnMetaData valueObj = DBUtil.getColumnMetaData(dbname,tablename,dataValueField);    
    
    
    String nameValidType = dictManager.getValidatorTypeByColumnMetaData(nameObj);
    String valueValidType = dictManager.getValidatorTypeByColumnMetaData(valueObj);
    String nameNullable = nameObj==null?"":nameObj.getNullable();
    String valueNulable = valueObj==null?"":valueObj.getNullable();
    if("yes".equalsIgnoreCase(nameNullable)){
        nameNullable = "";
    }else{
        nameNullable = "<span style='color:red'><pg:message code='sany.pdp.dictmanager.data.notnull'/></span>";
    }
    
    if("yes".equalsIgnoreCase(valueNulable)){
        valueNulable = "";
    }else{
        valueNulable = "<span style='color:red'><pg:message code='sany.pdp.dictmanager.data.notnull'/></span>";
    }
    
    //数据项是否有效,在列表中体现,同时显示"停用/启用功能"
    boolean showDataValidate = false;
    String dataValidateType = "";
    String dataValidateTypeName = "";
    if(dataValidate != null && dataValidate.trim().length()>0){
        //字段数据:是否有效
        showDataValidate = true;
    	ColumnMetaData validateObj = DBUtil.getColumnMetaData(dbname,tablename,dataValidate);
        dataValidateType = dictManager.getValidatorTypeByColumnMetaData(validateObj);
        dataValidateTypeName = validateObj.getTypeName();
    }
    
    String dataOrgType = "";
    String dataOrgTypeName = "";
    ColumnMetaData dataOrgObj = null;
    if(dataValueOrg != null && dataValueOrg.trim().length()>0){        
    	//字段数据:数据项所属机构
    	showDataValueOrg = true;
    	dataOrgObj = DBUtil.getColumnMetaData(dbname,tablename,dataValueOrg);
    	//所属机构的校验
        dataOrgType = dictManager.getValidatorTypeByColumnMetaData(dataOrgObj);
        dataOrgTypeName = dataOrgObj.getTypeName();
    }
    //处理附加字段
    String checkValue = request.getParameter("checkValue");
    if(checkValue != null)
    {
    	checkValue = URLDecoder.decode(checkValue, "UTF-8");
    }
	String value = "";
	String name = "";
	String primaryCondition = "";//主键条件
    if(checkValue != null){
    	String[] checkValues = checkValue.split(":");
    	if(checkValues.length == 1)
    	{
	    	value = checkValues[0];
	    	name = "";
	    	primaryCondition = "";
	    }
	    else if(checkValues.length == 2)
	    {
	    	value = checkValues[0];
	    	name = checkValues[1];
	    	primaryCondition = "";
	    }
	    else if(checkValues.length == 3)
	    {
	    	value = checkValues[0];
	    	name = checkValues[1];
	    	primaryCondition = checkValues[2];
	    }
    }
    //字典对应的主键条件
    //System.out.println("primaryCondition = " + primaryCondition);
    List dictatts = dictManager.getAllDictdataAttachFieldList(dicttypeId, name, value, primaryCondition);
    String ownerOrgId = "";
    String ownerOrgName = "";
    if(dataValueOrg != null && !"".equals(dataValueOrg)){
	    ownerOrgId = dictManager.getDictDataPropertyValue(dicttypeId,  value,name,dataValueOrg,primaryCondition);
	    
	    Organization organization = OrgCacheManager.getInstance().getOrganization(ownerOrgId);
	    if(organization != null){
	        ownerOrgName = organization.getRemark5();
	    }
    }
    
    //启用停用开关
	boolean state = ConfigManager.getInstance().getConfigBooleanValue("enableDictButton", true);
	
	//高级关键字段
	//String attachKeyColumnNames = dictKeyWordManager.getDicattachfieldKey(dicttypeId);
	Map keywords = dictKeyWordManager.getAllKeyWords(dicttypeId);
	
	String rootpath = request.getContextPath();
%>  
<html>
    <head>
    <title>基础字典数据采集</title>
        <script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
        <script language="javascript" src="../scripts/selectTime.js"></script>
        <script language="javascript" src="js/checkUnique.js"></script>
        <SCRIPT language="javascript">
        	var api = frameElement.api, W = api.opener;
        
			//var jsAccessControl = new JSAccessControl("#DAE0E9","#F6F8FB","#F6F8FB");
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
			function afterAddRefresh(){
			    document.location.href = document.location.href;
			}
			function afterDeleteRefresh(){
			    afterAddRefresh();
			}
			
			function orgSelectFinal(obj){
				var valueWin = window.showModalDialog("orgSelectTree.jsp?orgNames="+obj.value,window,"dialogWidth:"+(400)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
				if(valueWin!=null){
					obj.value = valueWin.split("^")[0];
					if(document.all.dictDataValueOrg){
						document.all.dictDataValueOrg.value = valueWin.split("^")[1];
					}
				}
			}
			
	</SCRIPT>
		<script language="JavaScript" src="../user/common.js" type="text/javascript"></script>
    </head>
    <body> 
    	<div style="height: 10px">&nbsp;</div>
    	<div class="form_box">
    		<form name="form1" method="post">
    			<input type="hidden" name="dicttypeId" value="<%=dicttypeId%>">
            	<input type="hidden" name="keyName" value="<%=name%>">
            	<input type="hidden" name="keyValue" value="<%=value%>">
            	<table border="0" cellpadding="0" cellspacing="0" class="table4" >
            		<%
					    if(showDictName){
					    
					%>
		 				<tr>   					
		   					<th>
								<%=data_name_cn%>:
							</th>
							<td>
							
							    <input type="text" name="dictDataName" class="w120" maxlength="<%=nameObj==null?0:nameObj.getColunmSize()%>" validator="<%=nameValidType%>" cnname="<%=data_name_cn%>"
							     value="<%=name%>"
							     <%
							     	keyWord = new KeyWord();
							     	keyWord.setDictypeId(dicttypeId);
									keyWord.setFieldName(nameObj.getColumnName());
										if(dictKeyWordManager.defineKeyField(keyWord)){
									%>
										readonly="true"
									<%
										}
									%>
							     ><%=nameNullable%>
							</td>
						</tr>
					<%
					    }
					%>
					
					<%
					    if(showDictValue){
					%>
						<tr>
							<th>
								<%=data_value_cn%>:
							</th>
							<td>
								<input <% if(!enableValeModify) out.print("readonly=true"); %> type="text" name="dictDataValue" class="w120"  maxlength="<%=valueObj==null?0:valueObj.getColunmSize()%>"  validator="<%=valueValidType%>" cnname="<%=data_value_cn%>" value="<%=value%>"><%=valueNulable%>
							</td>
						</tr>
					<%
					    }
					%>
					
						<%				
						    if(showDataValidate){				        
						%>
						    <tr>
						    <th>
								<pg:message code="sany.pdp.dictmanager.data.enable"/>：
							</th>
						    <td>
								<select class="select"  class="w120"  <%if(!state){%>disabled="true"<%}%> name="dictDataValidate<%if(!state){%>1<%}%>">
								    <option value="1" <%if(validateState.equals(RequestContextUtils.getI18nMessage("sany.pdp.dictmanager.db.field.name.gather.name.chinese.name", request))){%>selected<%}%>><pg:message code="sany.pdp.dictmanager.data.enable.yes"/></option>
								    <option value="0" <%if(validateState.equals(RequestContextUtils.getI18nMessage("sany.pdp.dictmanager.db.field.name.gather.value.chinese.name", request))){%>selected<%}%>><pg:message code="sany.pdp.dictmanager.data.enable.no"/></option>
								</select>
								<%if(!state){%>
									<input name="dictDataValidate" type="hidden" value="1" />
								<%}%> 
							</td>
							</tr>
						<%       
						    }
						%>
						<%
						    if(showDataValueOrg){					        
						%>
						    <tr>
						    <th>
								<pg:message code="sany.pdp.dictmanager.data.gather.org"/>：
							</th>
							<td>
							    <input type="hidden" name="dictDataValueOrg"  value="<%=ownerOrgId%>">
							    <input type='text' class="w120"  name="dictDataValueOrg_name" readonly='true' value="<%=ownerOrgId%> <%=ownerOrgName%>"   >
							    <input type='hidden' name='ownOrgLenth' value="<%=dataOrgObj.getColunmSize()%>" >
							    
							</td>
							</tr>
						<%      
						    }
						%>
		 				<!--附加字段处理-->
		 				<%
		 				    for(int i=0;i<dictatts.size();i++){
		 				        DictAttachField dictatt = (DictAttachField)dictatts.get(i);
								InputTypeScript inputTypeScript = dictatt.getInputTypeScript();					
								
						%>
						    <tr>
						    <th>
								<%=dictatt.getDictFieldName()%>:
							</th>
							<td>
							    <%
							        if("text".equalsIgnoreCase(dictatt.getInputTypeName())
							           || "文本类型".equalsIgnoreCase(dictatt.getInputTypeName())){
							            //..
							        }else if("选择字典".equalsIgnoreCase(dictatt.getInputTypeName())){
							            //...
							        }else if("选择时间".equalsIgnoreCase(dictatt.getInputTypeName())){
							            //...
							        }else if("主键生成".equalsIgnoreCase(dictatt.getInputTypeName())){
							            //...
							        }else if("主键生成".equalsIgnoreCase(dictatt.getInputTypeName())){
							            //...
							        }else if("选择机构".equalsIgnoreCase(dictatt.getInputTypeName())
							        	|| "选择人员".equalsIgnoreCase(dictatt.getInputTypeName())){
							        	//..
							        }
							        String outputHtml = inputTypeScript.getEditExtendHtmlContent(request,response,keywords);
							       //System.out.println("dictatt.getDictFieldName() = " + dictatt.getDictFieldName());
							        //System.out.println("outputHtml = " + outputHtml);
							    %>
							    <!--输出的内容-->
							    <%=outputHtml%>
							</td>
							</tr>
						<%		
								
		 				    }
		 				%>
 			
            	</table>
            	<div class="btnarea" >
					<a href="javascript:void(0)" class="bt_1" id="newbt" onclick="saveit()"><span><pg:message code="sany.pdp.common.operation.save"/></span></a>
					<a href="javascript:void(0)" class="bt_2" id="advance_newbt" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.cancel"/></span></a>
				</div>
    		</form>
    	</div>
    	
	</body>
	<iframe name="hiddenFrame" width=0 height=0></iframe>
	<script>
	    var win;
	    function saveit(){
	        var ownOrgLenth = 0;
	        var dictDataValueOrg = "";
	        if(document.all.dictDataValueOrg) dictDataValueOrg = document.all.dictDataValueOrg.value;
	        if(document.all("ownOrgLenth")) ownOrgLenth = document.all("ownOrgLenth").value;
	        if(dictDataValueOrg.length>ownOrgLenth){
	        	W.$.dialog.alert('<pg:message code="sany.pdp.dictmanager.data.belong.org.notnull"/>'+ownOrgLenth);
	            return false;
	        }
	        if (validateForm(form1) ){	
		        document.form1.action="dictAdvanceUpdatedata.jsp?parentValue=<%=parent_id%>&primaryCondition=<%=primaryCondition%>";
		        document.form1.target="hiddenFrame";
		        //win = window.showModelessDialog("doing.jsp","",featrue);
				document.form1.submit();
		        
		    } 	
	    }
	    function afterSave(){	    	
 	        window.returnValue = "ok";
 	        window.close();
 	    }
 	    function saveFaild(){
 	    	
 	        //window.close();

 	    }
	</script>
</html>
