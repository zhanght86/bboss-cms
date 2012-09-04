<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.dictionary.DictManager"%>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl"%>
<%@ page import="com.frameworkset.dictionary.Data"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String userId = accesscontroler.getUserID();	
	String opt = (String)request.getParameter("opt");
	String did = (String)request.getParameter("did");
	String type_name = (String)request.getParameter("type_name");
	String type_dsc = (String)request.getParameter("type_dsc");	
	String db_name = (String)request.getParameter("db_name");
	String table_name = (String)request.getParameter("table_name");
	String field_name = (String)request.getParameter("field_name");
	String field_value = (String)request.getParameter("field_value");
	//add by ge.tao
	//date 2008-01-25
	String field_name_cn = (String)request.getParameter("field_name_cn");
	String field_value_cn = (String)request.getParameter("field_value_cn");
	
	String field_order = (String)request.getParameter("field_order");
	String field_typeid = (String)request.getParameter("field_typeid");
	String field_parentid = (String)request.getParameter("field_parentid");
	String is_tree_str = (String)request.getParameter("is_tree");
	String dicttype_type_ = (String)request.getParameter("dicttype_type");
	//是否更新原来数据的类型ID,缺省是不更新:0 更新:1
	String update_typeId_ = (String)request.getParameter("update_typeId");
	//删除字典类型的时候,是否删除字典对应的字典数据项
	String delete_dictdata_ = (String)request.getParameter("delete_dictdata");
	//数据项是否有效字段
	String data_validate_field = (String)request.getParameter("field_data_validate");
	//数据项的创建机构
	String data_create_orgid_field = (String)request.getParameter("field_create_orgId");
	//值字段的生成规则
	String key_general_type_ = request.getParameter("key_general_type")==null?"":request.getParameter("key_general_type");
	
	//附加字段
	String [] att_name = (String [])request.getParameterValues("attachfieldname");
	String [] att_label = (String [])request.getParameterValues("attachlabel");
	String [] att_type = (String [])request.getParameterValues("attachinputtype");
	
	int is_tree = 0;
	int update_typeId = 0;
	int delete_dictdata = 0;
	int dicttype_type = 0;
	int key_general_type = 0;
	int user_id = 1;
	try{
	    is_tree = Integer.parseInt(is_tree_str);	   
	}catch(Exception e1){
	    is_tree = 0;
	}
	try{	    
	    update_typeId = Integer.parseInt(update_typeId_);
	}catch(Exception e2){
	    update_typeId = 0;
	}
	try{
	    delete_dictdata = Integer.parseInt(delete_dictdata_);
	}catch(Exception e3){
	    delete_dictdata = 0;
	}
	try{
	    dicttype_type = Integer.parseInt(dicttype_type_);
	}catch(Exception e4){
	    dicttype_type = 0;
	}
	try{
	    key_general_type = Integer.parseInt(key_general_type_);
	}catch(Exception e5){
	    key_general_type = 0;
	}
	try{
	    user_id = Integer.parseInt(userId);
	}catch(Exception e6){
	    user_id = 1;
	    e6.printStackTrace();
	}
	
	//缓冲数据标识
	String needcacheStr = request.getParameter("needcache");
	int needcache = 0;
	try{
		needcache = Integer.parseInt(needcacheStr);
	}catch(Exception e7){
		needcache = 0;
	}
	
	//值字段的值可改变标识
	String enable_value_modifyStr = request.getParameter("enable_value_modify");
	int enable_value_modify = 1;
	try{
		enable_value_modify = Integer.parseInt(enable_value_modifyStr);
	}catch(Exception e8){
		enable_value_modify = 0;
	}

	opt = opt==null?"":opt;
	did = did==null?"":did;
	type_name = type_name==null?"":type_name;
	type_dsc = type_dsc==null?"":type_dsc;
	db_name = db_name==null?"":db_name;
	table_name = table_name==null?"":table_name;
	field_name = field_name==null?"":field_name;
	field_value = field_value==null?"":field_value;
	field_order = field_order==null?"":field_order;
	field_typeid = field_typeid==null?"":field_typeid;
	field_parentid = field_parentid==null?"":field_parentid;
	
	DictManager dicManager = new DictManagerImpl();
	Data dicttype = new Data();
	//创建人ID, update的时候不更新.

	dicttype.setUser_id(user_id);
	dicttype.setDescription(type_dsc);
	dicttype.setName(type_name);	
	//dicttype.setParent(dict_parent);
	dicttype.setDataDBName(db_name);
	dicttype.setDataTableName(table_name);
	dicttype.setDataNameField(field_name);
	dicttype.setDataValueField(field_value);
	dicttype.setDataOrderField(field_order);
	dicttype.setDataTypeIdField(field_typeid);
	dicttype.setUpdate_dcitData_typeId(update_typeId);
	dicttype.setDelete_DictData(delete_dictdata);
	dicttype.setDicttype_type(dicttype_type);
	//值生成规则
	dicttype.setKey_general_type(key_general_type);
	//是否有效字段
	dicttype.setData_validate_field(data_validate_field);
	//字段数据项所属机构字段
	dicttype.setData_create_orgid_field(data_create_orgid_field);
	//新增字典类型成功标志 true:成功; false:失败	
	String addFlag = "";
	boolean deleteFlag = true;
	//新维护两个字段
	//add by ge.tao
	//date 2008-01-24
	dicttype.setField_name_cn(field_name_cn);
	dicttype.setField_value_cn(field_value_cn);
	
	//新维护字段：值字段的值可改变标识、缓冲数据标识
	//peng.yang 2008-04-08
	dicttype.setEnable_value_modify(enable_value_modify);
	dicttype.setNeedcache(needcache);
	
	if(is_tree==1){
	    dicttype.setDataParentIdFild(field_parentid);
	    dicttype.setIsTree(is_tree);
	}else{
	    dicttype.setDataParentIdFild("");
	    dicttype.setIsTree(is_tree);
	}	  
	if("".equals(did)){//无类型ID,insert,parent为0   	    
	    dicttype.setParent("0");
		addFlag = dicManager.addDicttype(dicttype);  
	}else{//有类型ID,update/delete/insert insert时,parent为did
	    if("update".equals(opt)){	    
	        dicttype.setDataId(did);
	    	addFlag = dicManager.updateDicttype(dicttype); 
	    }else if("delete".equals(opt)){
	    	dicttype.setDataId(did);
	        deleteFlag = dicManager.deletedicttype(dicttype); 
	    }else{
	        dicttype.setParent(did);
	        addFlag = dicManager.addDicttype(dicttype); 
	    }
	}
	
%>
	<script language="javascript">
	var msgs = '<pg:message code="sany.pdp.common.operation.success"/>';
<%
		
		if("".equals(addFlag) && deleteFlag){
%>	
		msgs = '<pg:message code="sany.pdp.common.operation.success"/>';
		//if(parent.win) parent.win.alert("操作成功!");
<%
		}else{
		    if("-1".equals(addFlag)){//有重复记录
%>		
				//if(parent.win) parent.win.alert("操作失败\n字典类型已经存在【<%=type_name%>】!");
				msgs = '<pg:message code="sany.pdp.dictmanager.dict.save.fail.typeexist"/>'+"【<%=type_name%>】!";
<%
		    }else if("-2".equals(addFlag)){//和其他表共享表, 并且没有指定字典类型字段
%>		    
		        //if(parent.win) parent.win.alert("操作失败\n字典指定的数据库表和其他字典共享, \n必须指定【字典类型字段】!");
		        //msgs = '<pg:message code="sany.pdp.dictmanager.dict.save.fail.field.referenceother"/>';
<%		        
		    }else if(addFlag.startsWith("-4")){//该表被其他字典类型独占, 不允许被引用
		        String sigleTableName = "";
		        String[] temps = addFlag.split(":");
		        if(temps.length>1){
		            sigleTableName = temps[1];
		        }		        
%>		    
		        var sigleTableName = "<%=sigleTableName%>";
		        //if(parent.win) 
		        //parent.win.alert("操作失败\n数据库表【"+sigleTableName+"】被独占, \n不允许引用!");
		        msgs = '<pg:message code="sany.pdp.dictmanager.dict.save.fail.field.cannotreference.begin"/>'+"【"+sigleTableName+"】"+'<pg:message code="sany.pdp.dictmanager.dict.save.fail.field.cannotreference.end"/>';
<%		        
		    }else if(addFlag.startsWith("-3")){//和其他表共享表, 并且没有指定和其他表相同的字段类型字段
		        String sameColumnName = "";
		        String[] temps = addFlag.split(":");
		        if(temps.length>1){
		            sameColumnName = temps[1];
		        }		        
%>		    
		        var sameColumnName = "<%=sameColumnName%>"
		        //if(parent.win) 
		        //parent.win.alert("操作失败\n字典指定的数据库表和其他字典共享, \n必须把"+sameColumnName+"指定【字典类型字段】!");
		        msgs = '<pg:message code="sany.pdp.dictmanager.dict.save.fail.field.referenceother.begin"/>'+sameColumnName+'<pg:message code="sany.pdp.dictmanager.dict.save.fail.field.referenceother.end"/>';
<%		        
		    }else if(addFlag.length()>0 && !"-1".equals(addFlag)&& !"-2".equals(addFlag) && 
		             !addFlag.startsWith("-3") && !addFlag.startsWith("-4")){//指定的表有不能为空的字段,并且这些字段没有配置到字典类型的属性中
%>
				//var msg = "操作成功! \n";
				//msg = msg + "但是<%=addFlag%>字段是不能为空的字段,\n ";
				//msg = msg + "必须在【字典高级字段配置】模块中配置为附加字段!"
		        //if(parent.win) parent.win.alert(msg);
		        
		        msgs = '<pg:message code="sany.pdp.common.operation.success"/>'+" \n";
		        msgs = msgs + "<pg:message code='sany.pdp.dictmanager.dict.save.fail.field.notnull.begin'/>"+"<%=addFlag%>";
		        msgs = msgs + '<pg:message code="sany.pdp.dictmanager.dict.save.fail.field.notnull.end"/>' + "\n";
		        msgs = msgs + '<pg:message code="sany.pdp.dictmanager.dict.save.fail.key.notconfig"/>';
<%		        
		    }
		    if(!deleteFlag){
%>		
				//parent.win.alert("字典类型已经被机构编码关系引用,\n不能被删除!");
				msgs = '<pg:message code="sany.pdp.dictmanager.dict.save.fail.type.cannotdelete"/>';
<%		    
		    }
		}
%>
		//if(parent.win)
		//{
			//parent.win.alert(msgs);
			//parent.enable();
			//parent.win.close();
		//}
		//else
		//{
			
			$.dialog.alert(msgs, function() {
				<%
		        if("insert".equals(opt)){
		            if("".equals(did)) did = "0";
		            if("-1".equals(addFlag) || "-2".equals(addFlag)){
		                //no fresh
		            }else{
		            	out.print("parent.afterAddRefresh()");
		            }
		        }else if("update".equals(opt)) {
		            out.print("parent.afterUpdateRefresh('"+did+"')");
		        }else if("delete".equals(opt)){
		            out.print("parent.afterDeleteRefresh()");
		        } 
		%>
			
			//this.close();
			});
		//}
		
	</script>

 


 


