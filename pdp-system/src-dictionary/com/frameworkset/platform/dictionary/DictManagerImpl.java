package com.frameworkset.platform.dictionary;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.RollbackException;

import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.poolman.sql.ColumnMetaData;
import com.frameworkset.common.poolman.sql.PrimaryKey;
import com.frameworkset.common.poolman.sql.PrimaryKeyCacheManager;
import com.frameworkset.common.poolman.sql.PrimaryKeyMetaData;
import com.frameworkset.dictionary.Data;
import com.frameworkset.dictionary.DataManager;
import com.frameworkset.dictionary.Item;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.dictionary.input.BaseInputTypeScript;
import com.frameworkset.platform.dictionary.input.CurrentOrgScript;
import com.frameworkset.platform.dictionary.input.CurrentTimeScript;
import com.frameworkset.platform.dictionary.input.CurrentUserScript;
import com.frameworkset.platform.dictionary.input.DateTypeScript;
import com.frameworkset.platform.dictionary.input.DictTypeScript;
import com.frameworkset.platform.dictionary.input.InputTypeManager;
import com.frameworkset.platform.dictionary.input.InputTypeManagerImpl;
import com.frameworkset.platform.dictionary.input.InputTypeScriptImpl;
import com.frameworkset.platform.dictionary.input.OtherInputTypeScript;
import com.frameworkset.platform.dictionary.input.PrimaryKeyTypeScript;
import com.frameworkset.platform.dictionary.input.TextTypeScript;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.event.ACLEventType;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

public class DictManagerImpl extends EventHandle implements DictManager  {
     
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	AccessControl accessControl = null;

	/**
	 * 初始化函数,获取当前页面的信息
	 */
	public void init(HttpServletRequest request,HttpServletResponse response)
	{

		this.request = request;
		this.response = response; 
		accessControl = AccessControl.getAccessControl();
	}

	private static Logger logger = LoggerFactory.getLogger(DictManagerImpl.class.getName());

	/**
	 * 根据名称获取字典类型
	 * modify by ge.tao
	 * 2007-11-12
	 */
	public Data getDicttypeByName(String name) throws ManagerException {
		Data dicttype = null;
		if(strIsNull(name))
			return null;		
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		StringBuffer sql = new StringBuffer()		
			.append("select  DICTTYPE_ID,DICTTYPE_NAME,DICTTYPE_DESC,DICTTYPE_PARENT,DATA_TABLE_NAME, ")
			.append("DATA_NAME_FILED ,DATA_VALUE_FIELD,DATA_ORDER_FIELD,DATA_TYPEID_FIELD,DATA_DBNAME, ")
			.append("DATA_PARENTID_FIELD,IS_TREE,DICTTYPE_TYPE,DATA_VALIDATE_FIELD,DATA_CREATE_ORGID_FIELD,KEY_GENERAL_TYPE ")
			//新维护两个字段
			.append(",DATA_NAME_CN,DATA_VALUE_CN,NAME_GENERAL_TYPE,KEY_GENERAL_INFO,NEEDCACHE,ENABLE_VALUE_MODIFY ")
			.append("from TD_SM_DICTTYPE where DICTTYPE_NAME=?");	
		try {			

			dbUtil.preparedSelect(sql.toString());
			dbUtil.setString(1, name);
			dbUtil.executePrepared();
			if(dbUtil.size()>0){				
				//必选的 肯定有值	
				dicttype = new Data();
				String dicttype_id = String.valueOf(dbUtil.getString(0,"DICTTYPE_ID"));
				dicttype.setDataId(dicttype_id);
				dicttype.setName(String.valueOf(dbUtil.getString(0,"DICTTYPE_NAME")));
				dicttype.setDescription(String.valueOf(dbUtil.getString(0,"DICTTYPE_DESC")));
				dicttype.setKey_general_type(dbUtil.getInt(0,"KEY_GENERAL_TYPE"));
				//新维护两个字段
				dicttype.setField_name_cn(dbUtil.getString(0,"DATA_NAME_CN"));
				dicttype.setField_value_cn(dbUtil.getString(0,"DATA_VALUE_CN"));
				//新维护四个字段：NAME_GENERAL_TYPE、KEY_GENERAL_INFO、NEEDCACHE、ENABLE_VALUE_MODIFY
				dicttype.setName_general_type(dbUtil.getString(0,"NAME_GENERAL_TYPE"));
				dicttype.setKey_general_info(dbUtil.getString(0,"KEY_GENERAL_INFO"));
				dicttype.setNeedcache(dbUtil.getInt(0,"NEEDCACHE"));
				dicttype.setEnable_value_modify(dbUtil.getInt(0,"ENABLE_VALUE_MODIFY"));

				dicttype.setParent(String.valueOf(dbUtil.getString(0,"DICTTYPE_PARENT")));
				dicttype.setDataParentIdFild(String.valueOf(dbUtil.getString(0,"DATA_PARENTID_FIELD")));
				dicttype.setIsTree(dbUtil.getInt(0,"IS_TREE")); 
				dicttype.setDicttype_type(dbUtil.getInt(0,"DICTTYPE_TYPE"));
				if(strIsNull(dbUtil.getString(0,"DATA_DBNAME"))){
					dicttype.setDataDBName(DEFAULT_DATA_DBNAME);
				}else{
				    dicttype.setDataDBName(String.valueOf(dbUtil.getString(0,"DATA_DBNAME")));
				}
				if(strIsNull(dbUtil.getString(0,"DATA_TABLE_NAME")) && strIsNull(dbUtil.getString(0,"DATA_DBNAME"))){
					dicttype.setDataTableName(DEFAULT_DATA_TABLENAME);
				}else{
					dicttype.setDataTableName(String.valueOf(dbUtil.getString(0,"DATA_TABLE_NAME")));
				}
				//不是必填的,只有当dbname和tablename都为空时,才能赋缺省值, 否则就确实是空
				if(strIsNull(dbUtil.getString(0,"DATA_NAME_FILED"))){
					if(strIsNull(dbUtil.getString(0,"DATA_TABLE_NAME")) && strIsNull(dbUtil.getString(0,"DATA_DBNAME"))){
						dicttype.setDataNameField(DEFAULT_DATA_NAMEFIELD);
					}else{
						dicttype.setDataNameField(String.valueOf(dbUtil.getString(0,"DATA_NAME_FILED")));
					}
				}else{
					dicttype.setDataNameField(String.valueOf(dbUtil.getString(0,"DATA_NAME_FILED")));
				}
				if(strIsNull(dbUtil.getString(0,"DATA_VALUE_FIELD"))){
					if(strIsNull(dbUtil.getString(0,"DATA_TABLE_NAME")) && strIsNull(dbUtil.getString(0,"DATA_DBNAME"))){
						dicttype.setDataValueField(DEFAULT_DATA_VALUEFIELD);
					}else{
						dicttype.setDataValueField(String.valueOf(dbUtil.getString(0,"DATA_VALUE_FIELD")));
					}
				}else{
				    dicttype.setDataValueField(String.valueOf(dbUtil.getString(0,"DATA_VALUE_FIELD")));
				}
				if(strIsNull(dbUtil.getString(0,"DATA_ORDER_FIELD"))){
					if(strIsNull(dbUtil.getString(0,"DATA_TABLE_NAME")) && strIsNull(dbUtil.getString(0,"DATA_DBNAME"))){
						dicttype.setDataOrderField(DEFAULT_DATA_ORDERFIELD);
					}else{
						dicttype.setDataOrderField(String.valueOf(dbUtil.getString(0,"DATA_ORDER_FIELD")));
					}
				}else{
					dicttype.setDataOrderField(String.valueOf(dbUtil.getString(0,"DATA_ORDER_FIELD")));
				}
				if(strIsNull(dbUtil.getString(0,"DATA_TYPEID_FIELD"))){
					if(strIsNull(dbUtil.getString(0,"DATA_TABLE_NAME")) && strIsNull(dbUtil.getString(0,"DATA_DBNAME"))){
						dicttype.setDataTypeIdField(DEFAULT_DATA_TYPEIDFIELD);
					}else{
						dicttype.setDataTypeIdField(String.valueOf(dbUtil.getString(0,"DATA_TYPEID_FIELD")));
					}
				}else{
					dicttype.setDataTypeIdField(String.valueOf(dbUtil.getString(0,"DATA_TYPEID_FIELD")));
				}
//				add 2007-12-06
				dicttype.setData_validate_field(String.valueOf(dbUtil.getString(0,"DATA_VALIDATE_FIELD")));
				dicttype.setData_create_orgid_field(String.valueOf(dbUtil.getString(0,"DATA_CREATE_ORGID_FIELD")));

				dicttype.setNextKeyValue("");
			}
		} catch (Exception e) {
			//throw new ManagerException(e.getMessage());
			e.printStackTrace();
		}

		return dicttype;
	}

	/**
	 * 判断字典类型是否缓冲数据
	 * @param dataId
	 * @return
	 */
	public boolean isCachable(String dataId)
	{
		int needcache = 0;

		String sql = "select NEEDCACHE from TD_SM_DICTTYPE where DICTTYPE_ID = ?";
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedSelect(sql);
			db.setString(1, dataId);
			db.executePrepared();
			if(db.size()>0)
			{
				needcache = db.getInt(0, "NEEDCACHE");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return needcache == 1;
	}

	/**
	 * 根据字典类型id 获取字典类型对象 
	 * add by ge.tao
	 * 2007-11-12
	 */
	public Data getDicttypeById(String id) throws ManagerException {
		Data dicttype = null;
		if(strIsNull(id)) return dicttype;
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		StringBuffer sql = new StringBuffer()
			.append("select DICTTYPE_ID,DICTTYPE_NAME,DICTTYPE_DESC,DICTTYPE_PARENT,DATA_TABLE_NAME, ")
			.append("DATA_NAME_FILED ,DATA_VALUE_FIELD,DATA_ORDER_FIELD,DATA_TYPEID_FIELD,DATA_DBNAME, ")
			.append("DATA_PARENTID_FIELD,IS_TREE,DICTTYPE_TYPE,DATA_VALIDATE_FIELD,DATA_CREATE_ORGID_FIELD,KEY_GENERAL_TYPE,user_id ")
			//新维护两个字段
			.append(",DATA_NAME_CN,DATA_VALUE_CN,NAME_GENERAL_TYPE,KEY_GENERAL_INFO,NEEDCACHE,ENABLE_VALUE_MODIFY ")
			.append("from TD_SM_DICTTYPE where DICTTYPE_ID= ?");		
		try {
			dbUtil.preparedSelect(sql.toString());
			dbUtil.setString(1, id);
			dbUtil.executePrepared();
			if(dbUtil.size()>0){
				dicttype = new Data();
				//必选的 肯定有值
				dicttype.setDataId(dbUtil.getString(0,"DICTTYPE_ID"));
				dicttype.setName(dbUtil.getString(0,"DICTTYPE_NAME"));
				dicttype.setDescription(dbUtil.getString(0,"DICTTYPE_DESC"));				
				dicttype.setKey_general_type(dbUtil.getInt(0,"KEY_GENERAL_TYPE"));
				//新维护两个字段
				dicttype.setField_name_cn(dbUtil.getString(0,"DATA_NAME_CN"));
				dicttype.setField_value_cn(dbUtil.getString(0,"DATA_VALUE_CN"));

				dicttype.setParent(dbUtil.getString(0,"DICTTYPE_PARENT"));
				dicttype.setDataParentIdFild(dbUtil.getString(0,"DATA_PARENTID_FIELD"));
				dicttype.setIsTree(dbUtil.getInt(0,"IS_TREE")); 
				dicttype.setDicttype_type(dbUtil.getInt(0,"DICTTYPE_TYPE"));

				dicttype.setUser_id(dbUtil.getInt(0,"user_id"));

				if(strIsNull(dbUtil.getString(0,"DATA_DBNAME"))){
					dicttype.setDataDBName(DEFAULT_DATA_DBNAME);
				}else{
				    dicttype.setDataDBName(dbUtil.getString(0,"DATA_DBNAME"));
				}
				if(strIsNull(dbUtil.getString(0,"DATA_TABLE_NAME")) && strIsNull(dbUtil.getString(0,"DATA_DBNAME"))){
					dicttype.setDataTableName(DEFAULT_DATA_TABLENAME);
				}else{
					dicttype.setDataTableName(dbUtil.getString(0,"DATA_TABLE_NAME"));
				}
				//数据库表被字典类型的使用情况
				if(!strIsNull(dbUtil.getString(0,"DATA_TABLE_NAME")) 
						&& !strIsNull(dbUtil.getString(0,"DATA_DBNAME"))){
					String typeIdField = dbUtil.getString(0,"DATA_TYPEID_FIELD")==null?"":dbUtil.getString(0,"DATA_TYPEID_FIELD");
					if(this.strIsNull(typeIdField)){//没有指定类型ID, 该表被其他字典独占
						dicttype.setDicttypeUseTabelstate(DICTTYPE_USE_TALBE_SINGLE);
					}else{//指定了字典类型ID,改表可以被多个字典类型公用
						dicttype.setDicttypeUseTabelstate(DICTTYPE_USE_TABLE_SHARE);
					}
					//dicttype.setDicttypeUseTabelstate(DICTTYPE_USE_TALBE_SINGLE);
					//dicttype.setDicttypeUseTabelstate(getDictTypeUseTableStates(dbUtil.getString(0,"DATA_DBNAME"),dbUtil.getString(0,"DATA_TABLE_NAME")));

				}

				//不是必填的,只有当dbname和tablename都为空时,才能赋缺省值, 否则就确实是空
				if(strIsNull(dbUtil.getString(0,"DATA_NAME_FILED"))){
					if(strIsNull(dbUtil.getString(0,"DATA_TABLE_NAME")) && strIsNull(dbUtil.getString(0,"DATA_DBNAME"))){
						dicttype.setDataNameField(DEFAULT_DATA_NAMEFIELD);
					}else{
						dicttype.setDataNameField(String.valueOf(dbUtil.getString(0,"DATA_NAME_FILED")));
					}
				}else{
					dicttype.setDataNameField(String.valueOf(dbUtil.getString(0,"DATA_NAME_FILED")));
				}
				if(strIsNull(dbUtil.getString(0,"DATA_VALUE_FIELD"))){
					if(strIsNull(dbUtil.getString(0,"DATA_TABLE_NAME")) && strIsNull(dbUtil.getString(0,"DATA_DBNAME"))){
						dicttype.setDataValueField(DEFAULT_DATA_VALUEFIELD);
					}else{
						dicttype.setDataValueField(String.valueOf(dbUtil.getString(0,"DATA_VALUE_FIELD")));
					}
				}else{
				    dicttype.setDataValueField(String.valueOf(dbUtil.getString(0,"DATA_VALUE_FIELD")));
				}
				if(strIsNull(dbUtil.getString(0,"DATA_ORDER_FIELD"))){
					if(strIsNull(dbUtil.getString(0,"DATA_TABLE_NAME")) && strIsNull(dbUtil.getString(0,"DATA_DBNAME"))){
						dicttype.setDataOrderField(DEFAULT_DATA_ORDERFIELD);
					}else{
						dicttype.setDataOrderField(String.valueOf(dbUtil.getString(0,"DATA_ORDER_FIELD")));
					}
				}else{
					dicttype.setDataOrderField(String.valueOf(dbUtil.getString(0,"DATA_ORDER_FIELD")));
				}
				if(strIsNull(dbUtil.getString(0,"DATA_TYPEID_FIELD"))){
					if(strIsNull(dbUtil.getString(0,"DATA_TABLE_NAME")) && strIsNull(dbUtil.getString(0,"DATA_DBNAME"))){
						dicttype.setDataTypeIdField(DEFAULT_DATA_TYPEIDFIELD);
					}else{
						dicttype.setDataTypeIdField(String.valueOf(dbUtil.getString(0,"DATA_TYPEID_FIELD")));
					}
				}else{
					dicttype.setDataTypeIdField(String.valueOf(dbUtil.getString(0,"DATA_TYPEID_FIELD")));
				}				
				//add 2007-12-06
				dicttype.setData_validate_field(String.valueOf(dbUtil.getString(0,"DATA_VALIDATE_FIELD")));
				dicttype.setData_create_orgid_field(String.valueOf(dbUtil.getString(0,"DATA_CREATE_ORGID_FIELD")));
				//下一个主键值
				dicttype.setNextKeyValue("");
				//新维护四个字段：NAME_GENERAL_TYPE、KEY_GENERAL_INFO、NEEDCACHE、ENABLE_VALUE_MODIFY
				dicttype.setName_general_type(dbUtil.getString(0,"NAME_GENERAL_TYPE"));
				dicttype.setKey_general_info(dbUtil.getString(0,"KEY_GENERAL_INFO"));
				dicttype.setNeedcache(dbUtil.getInt(0,"NEEDCACHE"));
				dicttype.setEnable_value_modify(dbUtil.getInt(0,"ENABLE_VALUE_MODIFY"));
			}
		} catch (Exception e) {
			//throw new ManagerException(e.getMessage());
			e.printStackTrace();
		}
		return dicttype;
	}	


	/**
	 * 新增字典类型 重复过滤
	 * 把数据表中指定的类型ID字段,更新成当前新建的类型ID
	 * @return 不能为空的字段名称 col1,col2,.... 当为-1时,表示有重复(名字)记录
	 * add by ge.tao
	 * 2007-11-12
	 */
	public String addDicttype(Data dicttype)throws ManagerException{
		if(dicttype == null) return "";
		
		TransactionManager tm = new TransactionManager();
		try{			
			tm.begin();
			//判断是否有同名记录
			String isRepeat_sql = "select count(1) as num from TD_SM_DICTTYPE where DICTTYPE_NAME=?";
//			judge.executeSelect(isRepeat_sql);
			int dictnum = SQLExecutor.queryObject(int.class, isRepeat_sql, dicttype.getName());
			if(dictnum>0){//有重复记录
				return "-1";
			}

			//判断字典类型映射的表, 是否被其他字典映射, 并且这个字典是否指定了字典类型. 
			//(1)如果这个字典没有指定类型字段, 表被改字典独占, 选表的时候做了过滤 这里不用考虑
			//(2)如果这个字典指定了类型字段, 表可以被共享, 但是这个字典也必须指定类型字段
			StringBuffer needTypeColumn_sql = new StringBuffer().append("select * from TD_SM_DICTTYPE ")
			.append(" where upper(DATA_DBNAME)=? and upper(DATA_TABLE_NAME)=? and (DATA_TYPEID_FIELD is null or DATA_TYPEID_FIELD='')");
			//.append(" and dicttype_id <> '").append(dicttype.getDataId()).append("'");
			final 	StringBuffer infos = new StringBuffer();
			SQLExecutor.queryByNullRowHandler(new NullRowHandler(){

				@Override
				public void handleRow(Record typeJudge) throws Exception {
					
						if(infos.length()==0){
							infos.append(typeJudge.getString("DATA_TABLE_NAME"));
						}else{
							infos.append(",").append(typeJudge.getString("DATA_TABLE_NAME"));
						}
					}	
					
				}
				
			, needTypeColumn_sql.toString(), dicttype.getDataDBName().toUpperCase(),dicttype.getDataTableName().toUpperCase());
//			typeJudge.executeSelect(needTypeColumn_sql.toString());
			if(infos.length()>0){
				
				
				return "-4:" + infos.toString();
			}
			needTypeColumn_sql.setLength(0);
			needTypeColumn_sql = new StringBuffer().append("select DATA_TYPEID_FIELD from TD_SM_DICTTYPE ")
			.append(" where upper(DATA_DBNAME)=? and upper(DATA_TABLE_NAME)=? and DATA_TYPEID_FIELD is not null");
			//.append(" and dicttype_id <> '").append(dicttype.getDataId()).append("'");
//			typeJudge.executeSelect(needTypeColumn_sql.toString());
			infos.setLength(0);
			SQLExecutor.queryByNullRowHandler(new NullRowHandler(){

				@Override
				public void handleRow(Record typeJudge) throws Exception {
					
						if(infos.length()==0){
							infos.append(typeJudge.getString("DATA_TYPEID_FIELD"));
						}
					}	
					
				}
				
			, needTypeColumn_sql.toString(), dicttype.getDataDBName().toUpperCase(),dicttype.getDataTableName().toUpperCase());
			//标识字典表是否已经被其他字典使用
			boolean tablemultiDict = false;
			if(infos.length()>0 ){//要和其他表共享表, 必须指定字典类型字段. 否则返回-2 提示要指定字典类型字段.
				String fff = infos.toString();
				tablemultiDict = true;
				if(this.strIsNull(dicttype.getDataTypeIdField())){//必须要指定字典类型字段
					return "-2";
				}else if(!dicttype.getDataTypeIdField().equals(fff)){//必须指定相同的类型字段
					String dataTypeIdFieldName = fff;
					return "-3:" + dataTypeIdFieldName;
				}
			}

			//往tabelinfo 表里面插入记录
			if(dicttype.getKey_general_type() == KEY_CREATE_TYPE){
				ColumnMetaData columnObj = DBUtil.getColumnMetaData(dicttype.getDataDBName(),dicttype.getDataTableName(),dicttype.getDataValueField());
				String columnType = columnObj.getTypeName();
				if(columnType.equalsIgnoreCase("long") || columnType.equalsIgnoreCase("number")){
					columnType = "int";
				}else{
					columnType = "string";
				}
				String juedge_tableinfo = "select count(*) as num from tableinfo where upper(TABLE_NAME)='"+ 
					dicttype.getDataTableName().toUpperCase() + "' and upper(TABLE_ID_NAME)='" + dicttype.getDataValueField().toUpperCase() + "'";
				DBUtil tmp = new DBUtil();
				//查询对应的数据库表的主键信息是否存在于tableinfo表中，不在则添加进去
				tmp.executeSelect(dicttype.getDataDBName(),juedge_tableinfo);
				if(tmp.size()>0 && tmp.getInt(0,"num")>0){//已经有主键配置 不插tableinfo
					// do nothing
				}else{
					StringBuffer insert_tableinfo = new StringBuffer()
				    .append("insert into tableinfo(TABLE_NAME,TABLE_ID_NAME,TABLE_ID_INCREMENT,")
				    .append("TABLE_ID_VALUE,TABLE_ID_GENERATOR,TABLE_ID_TYPE,TABLE_ID_PREFIX)values(")
				    .append("'").append(dicttype.getDataTableName().toUpperCase()).append("',")//表名
				    .append("'").append(dicttype.getDataValueField().toUpperCase()).append("',")//表主键名称
				    .append("1 ,")//主键递增步长
				    .append("0,")//表主键起始值
				    .append("'',")//id生成规则
				    .append("'").append(columnType).append("',")//id的类型 int string
				    .append("'')");//id的前缀
					tmp.executeInsert(dicttype.getDataDBName(),insert_tableinfo.toString()); 

					//modify by ge.tao
					//date 2008-01-22
					//刷缓冲
					PrimaryKeyCacheManager.getInstance().loaderPrimaryKey(dicttype.getDataDBName(),
							dicttype.getDataTableName().toUpperCase());
				}

			}

			PreparedDBUtil dbUtil = new PreparedDBUtil();
			String dicttype_id = dbUtil.getNextStringPrimaryKey("TD_SM_DICTTYPE");
			StringBuffer insert_dicttype = new StringBuffer()
				.append("insert into TD_SM_DICTTYPE(DICTTYPE_NAME,DICTTYPE_DESC,DICTTYPE_PARENT,DATA_TABLE_NAME, ")
				.append("DATA_NAME_FILED ,DATA_VALUE_FIELD,DATA_ORDER_FIELD,DATA_TYPEID_FIELD,DATA_DBNAME,")
				.append("DATA_PARENTID_FIELD,IS_TREE,DICTTYPE_TYPE,DATA_VALIDATE_FIELD,DATA_CREATE_ORGID_FIELD,KEY_GENERAL_TYPE,USER_ID")
				//add by ge.tao
				//date 2008-01-24
				//多维护两个字段
				.append(",DATA_NAME_CN,DATA_VALUE_CN,NEEDCACHE,ENABLE_VALUE_MODIFY,dicttype_id)")
				.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?")
				//add by ge.tao
				//date 2008-01-24
				//多维护两个字段
				.append(",?,?")
				.append(",?)");

			dbUtil.preparedInsert(insert_dicttype.toString());;
			dbUtil.setString(1,dicttype.getName());
			dbUtil.setString(2,dicttype.getDescription());
			dbUtil.setString(3,dicttype.getParent());			
			dbUtil.setString(4,dicttype.getDataTableName());
			dbUtil.setString(5,dicttype.getDataNameField());		
			dbUtil.setString(6,dicttype.getDataValueField());		
			dbUtil.setString(7,dicttype.getDataOrderField());
			dbUtil.setString(8,dicttype.getDataTypeIdField());
			dbUtil.setString(9,dicttype.getDataDBName());
			dbUtil.setString(10,dicttype.getDataParentIdFild());
			dbUtil.setInt(11,dicttype.getIsTree());
			dbUtil.setInt(12,dicttype.getDicttype_type());
			dbUtil.setString(13,dicttype.getData_validate_field());
			dbUtil.setString(14,dicttype.getData_create_orgid_field());
			dbUtil.setInt(15,dicttype.getKey_general_type());
			dbUtil.setInt(16,dicttype.getUser_id());
			dbUtil.setString(17,dicttype.getField_name_cn());
			dbUtil.setString(18,dicttype.getField_value_cn());
			dbUtil.setInt(19, dicttype.getNeedcache());
			dbUtil.setInt(20, dicttype.getEnable_value_modify());
			dbUtil.setString(21,dicttype_id);
			//获取主键
			dbUtil.executePrepared();
			String dicttype_keyId = String.valueOf(dicttype_id);
			//设置对象的属性,为发event准备
			dicttype.setDataId(dicttype_keyId);	
			if(dicttype.getUpdate_dcitData_typeId()==UPDATE_DICTDATA_TYPEID){//禁用字典数据更新功能
				//不能做成事务, 因为字典类型表 和 字典数据表可能在不同的数据库中 
				//更新数据表的 类型ID字段
//				StringBuffer update_dictdata = new StringBuffer()
//					.append("update ").append(dicttype.getDataTableName()).append(" set ")
//					.append(dicttype.getDataTypeIdField()).append("='").append(dicttype_keyId).append("' ");
//				DBUtil db = new DBUtil();
//				db.executeUpdate(dicttype.getDataDBName(),update_dictdata.toString());
			}
			String ret = this.getUnableNullColumnNames(dicttype);
			//发送事件
			tm.commit();
			Event event = new EventImpl(dicttype, DictionaryChangeEvent.DICTIONARY_ADD);
			super.change(event,true);		


			//检测是否要强制添加附加字段,检测当前表是否有不能为空的字段;			
			return ret;
		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			tm.releasenolog();
		}

		return "";
	}

	/**
	 * 更新字典类型 类型名称不能修改
	 * add by ge.tao
	 * 2007-11-12
	 */
	public String updateDicttype(Data dicttype)throws ManagerException{
		if(dicttype == null) return "";
		DBUtil judge = new DBUtil();
		DBUtil typeJudge = new DBUtil();
		//update前的类型名称重名检查
		StringBuffer isRepeat = new StringBuffer()
			.append("select count(*) as num from TD_SM_DICTTYPE where DICTTYPE_NAME='").append(dicttype.getName())
			.append("' and DICTTYPE_ID !=").append(dicttype.getDataId());
		TransactionManager tm = new TransactionManager();
		try{
			tm.begin();
			judge.executeSelect(isRepeat.toString());
			if(judge.size()>0 && judge.getInt(0,"num")>0){
				return "-1";
			}

			//判断字典类型映射的表, 是否被其他字典映射, 并且这个字典是否指定了字典类型. 
			//(1)如果其他字典没有指定类型字段, 表被该字典独占, 选表的时候做了过滤 这里不用考虑
			//(2)如果其他字典指定了类型字段, 表可以被共享, 但是这个字典也必须指定类型字段
			StringBuffer needTypeColumn_sql = new StringBuffer().append("select DATA_TABLE_NAME from TD_SM_DICTTYPE ")
			.append(" where upper(DATA_DBNAME)='").append(dicttype.getDataDBName().toUpperCase()).append("' and upper(DATA_TABLE_NAME)='")
			.append(dicttype.getDataTableName().toUpperCase()).append("' and (DATA_TYPEID_FIELD is null or DATA_TYPEID_FIELD='')")
			.append(" and dicttype_id <> '").append(dicttype.getDataId()).append("'");
			typeJudge.executeSelect(needTypeColumn_sql.toString());
			if(typeJudge.size()>0){
				//表被使用，并且没有设置类型字段，不允许共享字典
				StringBuffer infos = new StringBuffer();
				infos.setLength(0);
				for(int i = 0; i < typeJudge.size(); i ++){
					if(infos.length()==0){
						infos.append(typeJudge.getString(i,"DATA_TABLE_NAME"));
					}else{
						infos.append(",").append(typeJudge.getString(i,"DATA_TABLE_NAME"));
					}
				}	
				infos.setLength(0);
				return "-4:" + infos.toString();
			}
			needTypeColumn_sql.setLength(0);
			needTypeColumn_sql = new StringBuffer().append("select DATA_TYPEID_FIELD from TD_SM_DICTTYPE ")
			.append(" where upper(DATA_DBNAME)='").append(dicttype.getDataDBName().toUpperCase()).append("' and upper(DATA_TABLE_NAME)='")
			.append(dicttype.getDataTableName().toUpperCase()).append("' and DATA_TYPEID_FIELD is not null")
			.append(" and dicttype_id <> '").append(dicttype.getDataId()).append("'");
			typeJudge.executeSelect(needTypeColumn_sql.toString());
			if(typeJudge.size()>0 ){//要和其他表共享表, 必须指定字典类型字段. 否则返回-2 提示要指定字典类型字段.
				if(this.strIsNull(dicttype.getDataTypeIdField())){//必须要指定字典类型字段
					return "-2";
				}else if(!dicttype.getDataTypeIdField().equals(typeJudge.getString(0,"DATA_TYPEID_FIELD"))){//必须指定相同的类型字段
					String dataTypeIdFieldName = typeJudge.getString(0,"DATA_TYPEID_FIELD");
					return "-3:" + dataTypeIdFieldName;
				}

			}

			//往tabelinfo 表里面插入记录
			if(dicttype.getKey_general_type() == KEY_CREATE_TYPE){
				ColumnMetaData columnObj = DBUtil.getColumnMetaData(dicttype.getDataDBName(),dicttype.getDataTableName(),dicttype.getDataValueField());
				String columnType = columnObj.getTypeName();
				if(columnType.equalsIgnoreCase("long") || columnType.equalsIgnoreCase("number")){
					columnType = "int";
				}else{
					columnType = "string";
				}
				String juedge_tableinfo = "select count(*) as num from tableinfo where upper(TABLE_NAME)='"+ 
					dicttype.getDataTableName().toUpperCase() + "' " ;
					//"and TABLE_ID_NAME='" + dicttype.getDataValueField() + "'";
				DBUtil tmp = new DBUtil();
				tmp.executeSelect(dicttype.getDataDBName(),juedge_tableinfo);
				if(tmp.size()>0 && tmp.getInt(0,"num")>0){//已经有主键配置 不插tableinfo
					// do nothing
				}else{
					StringBuffer insert_tableinfo = new StringBuffer()
				    .append("insert into tableinfo(TABLE_NAME,TABLE_ID_NAME,TABLE_ID_INCREMENT,")
				    .append("TABLE_ID_VALUE,TABLE_ID_GENERATOR,TABLE_ID_TYPE,TABLE_ID_PREFIX)values(")
				    .append("'").append(dicttype.getDataTableName()).append("',")//表名
				    .append("'").append(dicttype.getDataValueField()).append("',")//表主键名称
				    .append("1 ,")//主键递增步长
				    .append("0,")//表主键起始值
				    .append("'',")//id生成规则
				    .append("'").append(columnType).append("',")//id的类型 int string
				    .append("'')");//id的前缀
					tmp.executeInsert(dicttype.getDataDBName(),insert_tableinfo.toString());

					//modify by ge.tao
					//date 2008-01-22
					//刷缓冲
					PrimaryKeyCacheManager.getInstance().loaderPrimaryKey(dicttype.getDataDBName(),
							dicttype.getDataTableName().toUpperCase());
				}


			}

			//没有重复类型名称
			PreparedDBUtil dbUtil = new PreparedDBUtil();
			StringBuffer sql = new StringBuffer()
			.append("update TD_SM_DICTTYPE set DICTTYPE_NAME=?,")
			.append("DICTTYPE_DESC=?,DATA_TABLE_NAME=? ,")
			.append("DATA_NAME_FILED=? ,DATA_VALUE_FIELD=? ,DATA_ORDER_FIELD=?,DATA_TYPEID_FIELD = ?,")
			.append("DATA_DBNAME=?,DATA_PARENTID_FIELD=?,IS_TREE=?,DICTTYPE_TYPE=?, ")
			.append("DATA_VALIDATE_FIELD=?,DATA_CREATE_ORGID_FIELD=?,KEY_GENERAL_TYPE=?  ")
			//新维护两个字段
			.append(",DATA_NAME_CN=?,DATA_VALUE_CN=?,NEEDCACHE=?,ENABLE_VALUE_MODIFY=? ")
			.append("where DICTTYPE_ID=?");
			dbUtil.preparedUpdate(sql.toString());
			dbUtil.setString(1,dicttype.getName());
			dbUtil.setString(2,dicttype.getDescription());		
			dbUtil.setString(3,dicttype.getDataTableName());
			dbUtil.setString(4,dicttype.getDataNameField());		
			dbUtil.setString(5,dicttype.getDataValueField());		
			dbUtil.setString(6,dicttype.getDataOrderField());
			dbUtil.setString(7,dicttype.getDataTypeIdField());
			dbUtil.setString(8,dicttype.getDataDBName());
			dbUtil.setString(9,dicttype.getDataParentIdFild());
			dbUtil.setInt(10,dicttype.getIsTree());
			dbUtil.setInt(11,dicttype.getDicttype_type());
			dbUtil.setString(12,dicttype.getData_validate_field());
			dbUtil.setString(13,dicttype.getData_create_orgid_field());
			dbUtil.setInt(14,dicttype.getKey_general_type());
			//新维护两个字段
			dbUtil.setString(15,dicttype.getField_name_cn());
			dbUtil.setString(16,dicttype.getField_value_cn());

			dbUtil.setInt(17, dicttype.getNeedcache());//是否缓冲数据
			dbUtil.setInt(18, dicttype.getEnable_value_modify());//值字段的值可否改变
			//dbUtil.setString(15,dicttype.getDataId());
			dbUtil.setString(19,dicttype.getDataId());

			dbUtil.executePrepared();	
			if(dicttype.getUpdate_dcitData_typeId()==UPDATE_DICTDATA_TYPEID){//禁用字典数据更新功能
				//更新数据表的 类型ID字段的数据 值为当前类型ID 
//				StringBuffer update_dictdata = new StringBuffer()
//				.append("update ").append(dicttype.getDataTableName()).append(" set ")
//				.append(dicttype.getDataTypeIdField()).append("='").append(dicttype.getDataId()).append("' ");
//				DBUtil db = new DBUtil();
//				db.executeUpdate(dicttype.getDataDBName(),update_dictdata.toString());
			}
			//检测是否要强制添加附加字段,检测当前表是否有不能为空的字段;
			String unableNullColumnNames = "";
			List unableNullColumns = this.getUnableNullColumns(dicttype.getDataDBName(),dicttype.getDataTableName());
			for(int i=0;unableNullColumns!=null && i<unableNullColumns.size();i++){
				ColumnMetaData  metaData = (ColumnMetaData)unableNullColumns.get(i);
				if(COLUMN_AVIALABLE == columnUseStatue(dicttype,metaData.getColumnName(),"")){//没有被属性使用的
					if(this.strIsNull(unableNullColumnNames)){
						unableNullColumnNames += metaData.getColumnName();
					}else{
						unableNullColumnNames += "," + metaData.getColumnName();
					}
				}
			}
			tm.commit();
			//发送事件
			Event event = new EventImpl(dicttype, DictionaryChangeEvent.DICTIONARY_INFO_UPDATE);
			super.change(event,true);
			return unableNullColumnNames;
		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			tm.releasenolog();
		}
		return "";
	}

	/**
	 * 添加字典数据 树形的 平铺的
	 * add by ge.tao
	 * 2007-11-12
	 */
	public boolean addDictdata(Item dictdata)throws ManagerException{		
		boolean r = false;
		if(dictdata == null) return r;
		int maxNo = 0;		
		DBUtil db = new DBUtil();
		DBUtil pd = new DBUtil();
		//字典类型ID
		String dicttypeId = dictdata.getDataId();
		//根据字典类型ID,获取字典类型对象
		Data dicttype = new Data();
		dicttype = getDicttypeById(dicttypeId);	
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org_field = dicttype.getData_create_orgid_field();
		int is_tree = dicttype.getIsTree();

		//判断数据是否有重复
		StringBuffer isRepeat_sql = new StringBuffer();
		isRepeat_sql.append("select count(*) as num from ").append(data_table_name).append(" where ")
			.append(data_name_filed).append("='").append(dictdata.getName()).append("' ");
		if(!strIsNull(data_typeid_field) ){
			isRepeat_sql.append(" and ").append(data_typeid_field).append("='").append(dicttypeId).append("'");
		}else{
			if(DEFAULT_DATA_TABLENAME.equalsIgnoreCase(data_typeid_field)){
				isRepeat_sql.append(" and ").append(DEFAULT_DATA_TYPEIDFIELD)
				.append("='").append(dicttypeId).append("'");
			}
		}
		try{
			db.executeSelect(isRepeat_sql.toString());
			if(db.size()>0){
				int count = db.getInt(0,"num");
				if(count>0){//已有重复记录 可能是值相同,或者是名称相同
					return false;
				}
			}			

			//处理基础字段			
			StringBuffer insert_sqlstr = new StringBuffer();			
			insert_sqlstr.append("insert into ").append(data_table_name).append("(")
				.append(data_name_filed).append(",").append(data_value_field);
			if(DICTDATA_IS_TREE==is_tree){//插入 父类ID字段
				String data_parentId = dicttype.getDataParentIdFild();
				insert_sqlstr.append(", ").append(data_parentId);
			}
			if(!this.strIsNull(data_order_field)){//插入排序字段
				insert_sqlstr.append(", ").append(data_order_field);
			}
			if(!this.strIsNull(data_typeid_field)){//插入类型ID字段
				insert_sqlstr.append(", ").append(data_typeid_field);
			}
			if(!this.strIsNull(data_validate_field)){//插入是否失效字段
				insert_sqlstr.append(", ").append(data_validate_field);
			}
			if(!this.strIsNull(data_org_field)){//插入所属机构字段
				insert_sqlstr.append(", ").append(data_org_field);
			}
			//--结束基础字段处理			


			insert_sqlstr.append(")values('").append(dictdata.getName()).append("','")
				.append(dictdata.getValue()).append("' ");
			if(DICTDATA_IS_TREE==is_tree){//插入 父类ID字段**值
				insert_sqlstr.append(", '").append(dictdata.getParentId()).append("' ");
			}
			if(!this.strIsNull(data_order_field)){//插入排序字段**值
				//根据字典类型ID,获取当该典数据的最大排序号
				String maxOrderNo = "select max("+ data_order_field +") as orderno from  " + data_table_name ;
				if(!this.strIsNull(data_typeid_field)){//插入类型ID字段
					maxOrderNo += " where "+ data_typeid_field +" = '"  + dicttypeId + "'";
				}				                    
				db.executeSelect(maxOrderNo);
				if(db.size()>0){
					maxNo = db.getInt(0,0) + 1;
				}
				insert_sqlstr.append(", '").append(maxNo).append("' ");
			}
			if(!this.strIsNull(data_typeid_field)){//插入类型ID字段**值
				insert_sqlstr.append(", '").append(dictdata.getDataId()).append("' ");
			}		
			if(!this.strIsNull(data_validate_field)){//插入是否失效字段的值 缺省是1 有效
				insert_sqlstr.append(",  ").append(dictdata.getDataValidate()).append(" ");
			}
			if(!this.strIsNull(data_org_field)){//插入所属机构字段的值
				insert_sqlstr.append(", '").append(dictdata.getDataOrg()).append("' ");
			}
			insert_sqlstr.append(")");
			//返回主键
			Object ob = pd.executeInsert(data_dbName,insert_sqlstr.toString());
			insert_sqlstr.setLength(0);
			/**
			 * 只有在往td_sm_dictdata表中插入数据时才需要设置ItemId
			 */
			dictdata.setItemId(String.valueOf(ob));			
			Event event = new EventImpl(dictdata, DictionaryChangeEvent.DICTIONARY_DATA_ADD);
			super.change(event,true);
			r = true;
		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			e.printStackTrace();
		}		
		return r;
	}


	/**
	 * 更新字典数据
	 */
	public boolean updateDictdata(Item dictdata) throws ManagerException{
		boolean r = false;
		//暂时不实现
		return r;
	}	

	/**
	 * 根据字典类型id,
	 * 删除一个字典类型 字典类型的数据
	 * 递归删除字典类型的 子字典类型/子字典类型的数据
	 * 原来:删除编码机构关系
	 * 改进:可删除 字典类型被机构编码关系	
	 * add by ge.tao
	 * 2007-11-12
	 * 
	 * 删除字典类型的时候要注意:
	 * 字典类型是否在字典数据表中指定了类型ID字段,如果指定了,那么要删除字典类型,必须先删除字典数据 否则有外键关联
	 */
	public boolean deletedicttype(String dicttypeId) throws ManagerException {
		boolean result = false;
		DBUtil dbUtil = new DBUtil();
		DBUtil db = new DBUtil();
		TransactionManager tm = new TransactionManager();
		if(strIsNull(dicttypeId)) return result;	
//		判断类型是否被编码机构关系引用,如被引用,则不允许删除.
		StringBuffer isUsed_sql = new StringBuffer();
		isUsed_sql.append("select count(*) as num from TD_SM_TAXCODE_ORGANIZATION where  DICTTYPE_ID = '")
				  .append(dicttypeId).append("' ");

		try{
			tm.begin();
			db.executeSelect(isUsed_sql.toString());
			if(db.size()>0 && db.getInt(0,"num")>0){
				return false;
			}

			//获取所有子类型
			List childDittypes = new ArrayList(); 
			childDittypes.add(getDicttypeById(dicttypeId));
					this.getRecursionChildDicttypeList(childDittypes,dicttypeId);
			Data[] data = new Data[childDittypes.size()];
			for(int i=childDittypes.size()-1;i>=0;i--){
			    String dicttypeId_ = ((Data)childDittypes.get(i)).getDataId();
				//删除数据字典类型的SQL

				Data dicttype = getDicttypeById(dicttypeId_);	
				data[i] = dicttype;
				//数据保存字段:
				String data_dbName = dicttype.getDataDBName();
				String data_table_name = dicttype.getDataTableName();
				String data_typeid_field = dicttype.getDataTypeIdField();				

				//删除机构编码关系 like '%类型ID:%'
				StringBuffer delete_orgTax = new StringBuffer();
				delete_orgTax.append("delete TD_SM_TAXCODE_ORGANIZATION where DICTTYPE_ID = '")
				   .append(dicttypeId_).append("' ");
				//先
				dbUtil.addBatch(delete_orgTax.toString());
				delete_orgTax.setLength(0);

				//删除数据字典数据的SQL
				String delete_data_sql = "";
				if(!this.strIsNull(data_typeid_field)){//判断有没有类型ID字段
					delete_data_sql = "delete " + data_table_name ;
					delete_data_sql = "delete " + data_table_name + " where " + data_typeid_field + " = '" + 
					dicttypeId_ + "'";
					//字典数据 立即删除 数据库不同 不能做事务
					db.executeDelete(data_dbName,delete_data_sql);
				}	

				if (dicttypeId_ != null) {						
					//字典类型 批处理删除 后
					String delete_type_sql = "delete TD_SM_DICTTYPE where DICTTYPE_ID='"+dicttypeId_+"'";
					dbUtil.addBatch(delete_type_sql);
					result = true;

				}

			}
			//删除字典类型		
			dbUtil.executeBatch();
			tm.commit();
			Event event = new EventImpl(data, DictionaryChangeEvent.DICTIONARY_DELETE);
			super.change(event,true);	
			Event event1 = new EventImpl("",
					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
			super.change(event1);

		} catch (Exception e) {	
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		}
		return result;
	}
	private void deletedict_(Data dicttype)
	{

	}
	/**
	 * 根据字典类型对象,
	 * 删除一个字典类型 判断是否删除 字典类型的数据
	 * 递归删除字典类型的 判断是否删除 子字典类型/子字典类型的数据
	 * 原来:删除编码机构关系
	 * 改进:
	 * 与deletedicttype(String dicttypeId)区别
	 * (1)该方法可以选择删除其数据项
	 * (2)如被编码机构关系引用,则类型不允许被删除
	 * add by ge.tao
	 * 2007-11-12
	 * 
	 * 删除字典类型的时候要注意:
	 * 字典类型是否在字典数据表中指定了类型ID字段,如果指定了,那么要删除字典类型,必须先删除字典数据 否则有外键关联
	 */
	public boolean deletedicttype(Data dicttype) throws ManagerException {
		boolean result = false;
		DBUtil dbUtil = new DBUtil();
		DBUtil db = new DBUtil();
		TransactionManager tm = new TransactionManager();
		if(dicttype==null) return result;
		boolean is_delete_dictdata = false;

		//判断类型是否被编码机构关系引用,如被引用,则不允许删除.
		StringBuffer isUsed_sql = new StringBuffer();
		isUsed_sql.append("select count(*) as num from TD_SM_TAXCODE_ORGANIZATION where DICTTYPE_ID='")
				  .append(dicttype.getDataId()).append("' ");
		try{
			tm.begin();
			db.executeSelect(isUsed_sql.toString());
			if(db.size()>0 && db.getInt(0,"num")>0){
				return false;
			}

			//获取所有子类型
			List childDittypes = new ArrayList(); 
			childDittypes.add(getDicttypeById(dicttype.getDataId()));
			this.getRecursionChildDicttypeList(childDittypes,dicttype.getDataId());

			Data[] data = new Data[childDittypes.size()];
			for(int i=childDittypes.size()-1;i>=0;i--){
			    String dicttypeId_ = ((Data)childDittypes.get(i)).getDataId();
			    //删除字典类型的附加字段的SQL
			    String delete_typeatt_sql = "delete from TD_SM_DICATTACHFIELD where DICTTYPE_ID='"+dicttypeId_+"'";	

			    //删除数据字典类型的SQL
				String delete_type_sql = "delete from TD_SM_DICTTYPE where DICTTYPE_ID='"+dicttypeId_+"'";			
				Data subdicttype = getDicttypeById(dicttypeId_);	
				data[i] = subdicttype;
				//数据保存字段:
				String data_dbName = subdicttype.getDataDBName();
				String data_table_name = subdicttype.getDataTableName();
				String data_typeid_field = subdicttype.getDataTypeIdField();	

				//删除数据字典数据的SQL
				String delete_data_sql = "";
				if(!this.strIsNull(data_typeid_field)){//判断有没有类型ID字段
					delete_data_sql = "delete from " + data_table_name ;
					delete_data_sql = "delete from " + data_table_name + " where " + data_typeid_field + " = '" + 
					dicttypeId_ + "'";
					//字典数据 立即删除 数据库不同 不能做事务
					db.executeDelete(data_dbName,delete_data_sql);
				}	

				if (dicttypeId_ != null) {

					//删除字典类型的资源操作授予记录
					String delete_res_dicttype = "delete from TD_SM_roleresop  " +
												"where restype_id='dict' and res_id ='" + dicttypeId_ + "'";
					dbUtil.addBatch(delete_res_dicttype);
					//字典类型 批处理删除 
					//字典类型关联的附加字段
					dbUtil.addBatch(delete_typeatt_sql);
					dbUtil.addBatch(delete_type_sql);
					result = true;

				}

			}
			//删除字典类型		
			//删除字典类型关联的附加字段
			dbUtil.executeBatch();
			tm.commit();
			Event event = new EventImpl(data, DictionaryChangeEvent.DICTIONARY_DELETE);
			super.change(event,true);	
//			Event event1 = new EventImpl("",
//					ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
//			super.change(event1);
		} catch (Exception e) {
			result = false;
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据字典类型,删除一个字典类型的具体数据
	 * 包含递归删除
	 * 删除编码机构关系	 
	 * @param dictdataId 需要删除的字典类型的ID
	 * @param dictdataValue 需要删除的字典数据的值
	 * @param dictdataValue 需要删除的字典数据的名称 为了从缓存的itemsIdxByName中去掉
	 * @return 删除成功则返回 true 否则返回 false
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 * add by ge.tao
	 * 2007-11-12
	 */
	public boolean deletedictdata(String dicttypeId,String dictdataValue,String dictdataName,String primaryCondition) throws ManagerException {
		boolean result = false;
		//if(strIsNull(dicttypeId) || strIsNull(dictdataValue) || strIsNull(dictdataName)) return result;
		//数据项是否被授权，如果授权不能删除
		if(this.isAccredit(dicttypeId, dictdataValue, dictdataName)){
			return result;
		}
//		DBUtil dbUtil = new DBUtil();
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		Data dicttype = new Data();
		Item dictdata = new Item();
		dicttype = getDicttypeById(dicttypeId);		
		//数据保存字段:
		int isTree = dicttype.getIsTree();
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String dicttype_field = dicttype.getDataTypeIdField();
//		String data_order_field	= dicttype.getDataOrderField();
//		String data_typeid_field = dicttype.getDataTypeIdField();
//		dbUtil.setBatchDBName(data_dbName);
		String sql = "";
		StringBuffer delete_orgTax = new StringBuffer();
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			if(DICTDATA_IS_TREE==isTree){//如果数据项是树形,递归删除子数据
				String parentId_field = dicttype.getDataParentIdFild();
				//删除机构编码关系 
				delete_orgTax.append("delete TD_SM_TAXCODE_ORGANIZATION where DATA_VALUE in (")
				   .append(" select ")
				   .append(data_value_field).append(" from  ").append(data_table_name).append(" start with ")
				   .append(data_value_field).append("='").append(dictdataValue).append("' connect by prior ")
				   .append(data_value_field).append("=").append(parentId_field).append(")")
				   .append(" and dicttype_id='").append(dicttypeId).append("'");
				dbUtil.preparedDelete(data_dbName,delete_orgTax.toString());
				dbUtil.addPreparedBatch();
				delete_orgTax.setLength(0);

			    sql = "delete from "+ data_table_name +" a where "+ data_value_field +" in ("+ 
			    	"select " + data_value_field + " from " + data_table_name + " start with " +
			    	data_value_field +"= '" + dictdataValue + "' connect by prior " + data_value_field + 
			    	"= " + parentId_field + ")";
			    if(dicttype_field != null && !dicttype_field.equals(""))
			    	sql += " and " + dicttype_field  + "='" + dicttypeId +  "'";

			    sql += primaryCondition;
			    dbUtil.preparedDelete(data_dbName,sql);
			    dbUtil.addPreparedBatch();
//			    dbUtil.addBatch(sql);
			}else{//如果数据项是不树形,删除数据
				//删除机构编码关系 like '%:数据Value%'			
				delete_orgTax.append("delete from TD_SM_TAXCODE_ORGANIZATION where DATA_VALUE= '")
				   .append(dictdataValue).append("' ")
				   .append(" and dicttype_id='").append(dicttypeId).append("'");
				//先

//				dbUtil.addBatch(delete_orgTax.toString());
				dbUtil.preparedDelete(data_dbName,delete_orgTax.toString());
			    dbUtil.addPreparedBatch();
				delete_orgTax.setLength(0);

			    sql = "delete from "+ data_table_name +" where "+ data_value_field +" = '"+ dictdataValue +"' "+ 
			          "and "+data_name_filed+"='"+dictdataName+"' ";
			    if(dicttype_field != null && !dicttype_field.equals(""))
			    {
			    	if(dicttype_field.startsWith("a."))
			    		dicttype_field = dicttype_field.substring(2);
			    	sql += " and " + dicttype_field  + "='" + dicttypeId +  "'";
			    }

			    sql += primaryCondition;
//			    dbUtil.addBatch(sql);
			    dbUtil.preparedDelete(data_dbName,sql);
			    dbUtil.addPreparedBatch();
			}		
			dbUtil.executePreparedBatch();				
			result = true;
			tm.commit();
			//构造字典数据对象,发送消息
//			dictdata.setDataId(dictdataValue);
			dictdata.setDataId(dicttypeId);
			dictdata.setValue(dictdataValue);
			//为了从缓存的itemsIdxByName中去掉
			dictdata.setName(dictdataName);
			if(isCachable(dicttypeId)){
				Event event = new EventImpl(dictdata, DictionaryChangeEvent.DICTIONARY_DATA_DELETE);
				super.change(event,true);
			}
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			logger.error("",e);
			//throw new ManagerException(e.getMessage());
			result = false;
			e.printStackTrace();
		}		

		return result;
	}


	/**
	 * 根据字典类型,删除一个字典类型的具体数据
	 * 包含递归删除
	 * 删除编码机构关系	 
	 * @param dictdataId 需要删除的字典类型的ID
	 * @param dictdataValue 需要删除的字典数据的值
	 * @param dictdataValue 需要删除的字典数据的名称 为了从缓存的itemsIdxByName中去掉
	 * @return 删除成功则返回 true 否则返回 false
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 * add by ge.tao
	 * 2007-11-12
	 */
	public boolean deletedictdataByName(String dicttypeId,String dictdataName,String primaryCondition) throws ManagerException {
		boolean result = false;
		if(dictdataName == null) return result;
//		数据项是否被授权，如果授权不能删除
		if(this.isAccredit(dicttypeId, "", dictdataName)){
			return result;
		}
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		Item dictdata = new Item();
		dicttype = getDicttypeById(dicttypeId);		
		//数据保存字段:
		int isTree = dicttype.getIsTree();
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String dicttype_field = dicttype.getDataTypeIdField();

//		String data_order_field	= dicttype.getDataOrderField();
//		String data_typeid_field = dicttype.getDataTypeIdField();
		dbUtil.setBatchDBName(data_dbName);
		String sql = "";

		StringBuffer delete_orgTax = new StringBuffer();
		try {
			if(DICTDATA_IS_TREE==isTree){//如果数据项是树形,递归删除子数据
				String parentId_field = dicttype.getDataParentIdFild();
				//删除机构编码关系 
				delete_orgTax.append("delete TD_SM_TAXCODE_ORGANIZATION where DATA_VALUE in (")
				   .append(" select ")
				   .append(data_value_field).append(" from  ").append(data_table_name).append(" start with ")
				   .append(data_value_field).append("='").append("").append("' connect by prior ")
				   .append(data_value_field).append("=").append(parentId_field).append(")")
				   .append(" and dicttype_id='").append(dicttypeId).append("'");
				dbUtil.addBatch(delete_orgTax.toString());
				delete_orgTax.setLength(0);

			    sql = "delete from "+ data_table_name +" where "+ data_value_field +" in ("+ 
			    	"select " + data_value_field + " from " + data_table_name + " start with " +
			    	data_value_field +"= '' connect by prior " + data_value_field + 
			    	"= " + parentId_field + ")";
			    if(dicttype_field != null && !dicttype_field.equals(""))
			    	sql += " and " + dicttype_field  + "='" + dicttypeId +  "'";
			    dbUtil.addBatch(sql);
			}else{//如果数据项是不树形,删除数据
				//删除机构编码关系 like '%:数据Value%'			
				delete_orgTax.append("delete TD_SM_TAXCODE_ORGANIZATION where DATA_VALUE= '' ")
					.append(" and dicttype_id='").append(dicttypeId).append("'");
				//先
				dbUtil.addBatch(delete_orgTax.toString());
				delete_orgTax.setLength(0);

			    sql = "delete from "+ data_table_name +" a where "+data_name_filed+"='"+dictdataName+"' ";
			    if(dicttype_field != null && !dicttype_field.equals(""))
			    	sql += " and " + dicttype_field  + "='" + dicttypeId +  "'";

			    sql += primaryCondition;
			    dbUtil.addBatch(sql);
			}		
			dbUtil.executeBatch();				
			result = true;
			//构造字典数据对象,发送消息
			dictdata.setDataId(dicttypeId);
			dictdata.setValue("");
			//为了从缓存的itemsIdxByName中去掉
			dictdata.setName(dictdataName);
			Event event = new EventImpl(dictdata, DictionaryChangeEvent.DICTIONARY_DATA_DELETE);
			super.change(event,true);
		} catch (Exception e) {
			logger.error("",e);

			//throw new ManagerException(e.getMessage());
			e.printStackTrace();
		}finally{
			dbUtil.resetBatch();
		}


		return result;
	}

	/**
	 * 根据字典类型,删除一个字典类型的具体数据
	 * 包含递归删除
	 * 删除编码机构关系	 
	 * @param dictdataId 需要删除的字典类型的ID
	 * @param dictdataValue 需要删除的字典数据的值
	 * @param dictdataValue 需要删除的字典数据的名称 为了从缓存的itemsIdxByName中去掉
	 * @return 删除成功则返回 true 否则返回 false
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 * add by ge.tao
	 * 2007-11-12
	 */
	public boolean deletedictdataByValue(String dicttypeId,String dictdataValue,String primaryCondition) throws ManagerException {
		boolean result = false;
		if(dictdataValue == null) return result;
//		数据项是否被授权，如果授权不能删除
		if(this.isAccredit(dicttypeId, dictdataValue, "")){
			return result;
		}
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		Item dictdata = new Item();
		dicttype = getDicttypeById(dicttypeId);		
		//数据保存字段:
		int isTree = dicttype.getIsTree();
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String dictdataName = "";
		String dicttype_field = dicttype.getDataTypeIdField();

//		String data_order_field	= dicttype.getDataOrderField();
//		String data_typeid_field = dicttype.getDataTypeIdField();
		dbUtil.setBatchDBName(data_dbName);
		String sql = "";

		StringBuffer delete_orgTax = new StringBuffer();
		try {
			if(DICTDATA_IS_TREE==isTree){//如果数据项是树形,递归删除子数据
				String parentId_field = dicttype.getDataParentIdFild();
				//删除机构编码关系 
				delete_orgTax.append("delete TD_SM_TAXCODE_ORGANIZATION where DATA_VALUE in (")
				   .append(" select ")
				   .append(data_value_field).append(" from  ").append(data_table_name).append(" start with ")
				   .append(data_value_field).append("='").append(dictdataValue).append("' connect by prior ")
				   .append(data_value_field).append("=").append(parentId_field).append(")")
				   .append(" and dicttype_id='").append(dicttypeId).append("'");
				dbUtil.addBatch(delete_orgTax.toString());
				delete_orgTax.setLength(0);

			    sql = "delete from "+ data_table_name +" where "+ data_value_field +" in ("+ 
			    	"select " + data_value_field + " from " + data_table_name + " start with " +
			    	data_value_field +"= '" + dictdataValue + "' connect by prior " + data_value_field + 
			    	"= " + parentId_field + ")";	
			    if(dicttype_field != null && !dicttype_field.equals(""))
			    	sql += " and " + dicttype_field  + "='" + dicttypeId +  "'";
			    dbUtil.addBatch(sql);
			}else{//如果数据项是不树形,删除数据
				//删除机构编码关系 like '%:数据Value%'			
				delete_orgTax.append("delete TD_SM_TAXCODE_ORGANIZATION where DATA_VALUE= '")
				   .append(dictdataValue).append("' ").append(" and dicttype_id='").append(dicttypeId).append("'");
				//先
				dbUtil.addBatch(delete_orgTax.toString());
				delete_orgTax.setLength(0);

			    sql = "delete from "+ data_table_name +" a where "+ data_value_field +" = '"+ dictdataValue +"' "+ 
			          "and "+data_name_filed+"='"+dictdataName+"' ";
			    if(dicttype_field != null && !dicttype_field.equals(""))
			    	sql += " and " + dicttype_field  + "='" + dicttypeId +  "'";

			    sql += primaryCondition;
			    dbUtil.addBatch(sql);
			}		
			dbUtil.executeBatch();				
			result = true;
			//构造字典数据对象,发送消息
			dictdata.setDataId(dicttypeId);
			dictdata.setValue(dictdataValue);
			//为了从缓存的itemsIdxByName中去掉
			dictdata.setName(dictdataName);
			Event event = new EventImpl(dictdata, DictionaryChangeEvent.DICTIONARY_DATA_DELETE);
			super.change(event,true);
		} catch (Exception e) {
			logger.error("",e);

			//throw new ManagerException(e.getMessage());
			e.printStackTrace();
		}finally{
			dbUtil.resetBatch();
		}

		return result;
	}

	/**
	 * 更新字典数据的顺序
	 * add by ge.tao
	 * 2007-11-12
	 */
	public boolean updateArrangeDictOrderNo(String dicttypeId,String[] dataValues) throws ManagerException{
		boolean flag = false;	
		if(strIsNull(dicttypeId) || dataValues == null) return flag;
		DBUtil db = new DBUtil();
		Data dicttype = new Data();
		dicttype = getDicttypeById(dicttypeId);	
		List dictdatas = new ArrayList();
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
//		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
//		String data_typeid_field = dicttype.getDataTypeIdField();
		String sql = "";
		try	{	
			db.setBatchDBName(data_dbName);
			for(int i=0;i<dataValues.length;i++){
				sql = "update " + data_table_name + " set " + data_order_field + "='" + i+ "' " +
					  "where "+ data_value_field +" = '" + dataValues[i]+ "'";
				db.addBatch(sql);
				//构造字典数据对象
				Item dictdata = new Item();
				dictdata.setDataId(dicttypeId);
				dictdata.setOrder(i);
				dictdata.setValue(data_value_field);
				dictdatas.add(dictdata);
			}		
			//把数据项set到数据类型中
			dicttype.setAllitems(dictdatas);
			db.executeBatch();
			Event event = new EventImpl(dicttype, DictionaryChangeEvent.DICTIONARY_DATA_ORDERCHANGE);
			super.change(event,true);
			flag = true;
		}catch(Exception e){
			e.printStackTrace();

			//throw new ManagerException(e.getMessage());
		}finally{
			db.resetBatch();
		}
		return flag;
	}	


	/**
	 * 查看指定字典类型(通过类型ID)是否包含子字典类型
	 */
	public boolean isContainChildDicttype(String dicttypeId)throws ManagerException{
		boolean flag = false;
		if(strIsNull(dicttypeId)) return flag;
		DBUtil db = new DBUtil();
		String sql = "select 1 from td_sm_dicttype where DICTTYPE_PARENT='"+ dicttypeId +"'";
		try {
			db.executeSelect(sql);
			if(db.size()>0){
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 查看指定字典类型(通过类型ID)是否包含子业务字典类型
	 */
	public boolean isContainChildBusinessDicttype(String dicttypeId)throws ManagerException{
		boolean flag = false;
		if(strIsNull(dicttypeId)) return flag;
		DBUtil db = new DBUtil();
		String sql = "select 1 from td_sm_dicttype where DICTTYPE_TYPE in (" + 
				ALLREAD_BUSINESS_DICTTYPE + "," + PARTREAD_BUSINESS_DICTTYPE + 
				") and DICTTYPE_PARENT='"+ dicttypeId +"'";
		try {
			db.executeSelect(sql);
			if(db.size()>0){
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 根据字典类型id获得字典的数据集合
	 * 不翻页 不递归
	 * 判断是否有类型ID字段 判断是否有排序字段
	 * add by ge.tao
	 * 2007-11-12
	 */
	public List getChildDictdataListByTypeId(String dicttypeId) throws ManagerException {
		List list = new ArrayList();
		if(this.strIsNull(dicttypeId)) return list;
		DBUtil dbUtil = new DBUtil();		
		Data dicttype = getDicttypeById(dicttypeId);		
		//数据保存字段:
		try {	
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org = dicttype.getData_create_orgid_field();
		int is_tree = dicttype.getIsTree();
		StringBuffer sql = new StringBuffer();
		if(DICTDATA_IS_TREE==is_tree){
			String data_parentId_field = dicttype.getDataParentIdFild();
			sql.append("select 1 ");			
			if(!this.strIsNull(data_name_filed)){
				sql.append(",").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",").append(data_value_field);
			}	
			if(!this.strIsNull(data_validate_field)){
				sql.append(",").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",").append(data_org);
			}
			sql.append(" from ").append(data_table_name).append(" where ").append(data_parentId_field)
			.append("='' or ").append(data_parentId_field).append(" is null ");
		}else{		
			sql.append("select 1 ");			
			if(!this.strIsNull(data_name_filed)){
				sql.append(",").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",").append(data_value_field);
			}	
			if(!this.strIsNull(data_validate_field)){
				sql.append(",").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",").append(data_org);
			}
			sql.append(" from ").append(data_table_name).append(" where 1=1 ");
		}
		//判断是否有 类型ID字段
		if(!this.strIsNull(dicttype.getDataTypeIdField())){
			sql.append(" and ").append(data_typeid_field).append("='").append(dicttypeId).append("'  ");
		}
		//判断是否有 排序字段 没有排序字段 就按 值排序
		if(!this.strIsNull(dicttype.getDataOrderField())){
			sql.append(" order by ").append(data_order_field);
		}else{
			if(!this.strIsNull(data_value_field)){
				sql.append(" order by ").append(data_value_field);
			}
		}

			dbUtil.executeSelect(data_dbName,sql.toString());
			for(int i= 0;i < dbUtil.size();i++){
				Item dictdata = new Item();
//				dictdata.setItemId(dbUtil.getString(i,"dictdata_id"));
				dictdata.setName(dbUtil.getString(i,data_name_filed)==null?"":dbUtil.getString(i,data_name_filed));
				dictdata.setValue(dbUtil.getString(i,data_value_field)==null?"":dbUtil.getString(i,data_value_field));
//				dictdata.setOrder(dbUtil.getInt(i,data_order_field));
				//数据项的ID设置为它的值
				dictdata.setItemId(dictdata.getValue());
				dictdata.setDataId(dicttypeId);
				if(!this.strIsNull(data_org)){
					dictdata.setDataOrg(dbUtil.getString(i,data_org));
				}
				if(!this.strIsNull(data_validate_field)){
					dictdata.setDataValidate(dbUtil.getInt(i,data_validate_field));
				}
				list.add(dictdata);
			}
		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.DictManager#getChildDictdataListByDataId(java.lang.String)
	 * 根据字典类型id,字典数据Value获得,该字典数据的子的数据
	 * 判断有没有类型ID字段 有没有排序字段
	 * 不递归 
	 */
	public List getChildDictdataListByDataId(String dicttypeid ,String dictdataValue) throws ManagerException {
		List list = null;
		if(this.strIsNull(dictdataValue)) return list;
		DBUtil dbUtil = new DBUtil();
		list = new ArrayList();
		Data dicttype = getDicttypeById(dicttypeid);		
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org = dicttype.getData_create_orgid_field();

		int is_tree = dicttype.getIsTree();		
		StringBuffer sql = new StringBuffer();
		if(DICTDATA_IS_TREE==is_tree){
			String data_parentId_field = dicttype.getDataParentIdFild();
			sql.append("select 1 ");			
			if(!this.strIsNull(data_name_filed)){
				sql.append(",").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",").append(data_value_field);
			}	
			if(!this.strIsNull(data_validate_field)){
				sql.append(",").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",").append(data_org);
			}
//			if(data_parentId_field != null && !data_parentId_field.trim().equals(""))
//			{
//				sql.append(",").append(data_parentId_field);
//			}

			sql.append(" from ").append(data_table_name).append(" where ").append(data_parentId_field)
			.append("='").append(dictdataValue).append("' ");
		}else{
			sql.append("select 1 ");			
			if(!this.strIsNull(data_name_filed)){
				sql.append(",").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",").append(data_value_field);
			}	
			if(!this.strIsNull(data_validate_field)){
				sql.append(",").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",").append(data_org);
			}
			sql.append(" from ").append(data_table_name).append(" where ").append(data_value_field)
			.append("='").append(dictdataValue).append("' ");
		}
		//判断是否有 类型ID字段
		if(!this.strIsNull(dicttype.getDataTypeIdField())){
			sql.append(" and ").append(data_typeid_field).append("='").append(dicttypeid).append("'  ");
		}

		//判断是否有 排序字段 没有排序字段 就按 值排序
		if(!this.strIsNull(dicttype.getDataOrderField())){
			sql.append(" order by ").append(data_order_field);
		}else{
			if(!this.strIsNull(data_value_field)){
				sql.append(" order by ").append(data_value_field);
			}
		}

		try {			
            //System.out.println(sql.toString());
			dbUtil.executeSelect(data_dbName,sql.toString());
			for(int i= 0;i < dbUtil.size();i++){
				Item dictdata = new Item();				
				dictdata.setName(dbUtil.getString(i,data_name_filed)==null?"":dbUtil.getString(i,data_name_filed));
				dictdata.setValue(dbUtil.getString(i,data_value_field)==null?"":dbUtil.getString(i,data_value_field));				
				dictdata.setDataId(dicttypeid);
				//biaoping.yin 修改于2008.05.22
				dictdata.setParentId(dictdataValue);
				//在缓存获取 子数据项的时候 设置子数据的ID = value
			    dictdata.setItemId(dictdata.getValue());
				if(!this.strIsNull( data_org)){
					dictdata.setDataOrg(dbUtil.getString(i,data_org));
				}
				if(!this.strIsNull(data_validate_field)){
					dictdata.setDataValidate(dbUtil.getInt(i, data_validate_field));
				}
				list.add(dictdata);
			}
		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			System.out.println("error info: table '" + data_table_name + "' keyvalue validate");
			e.printStackTrace();
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.DictManager#getChildDictdataListByDataId(java.lang.String)
	 * 根据字典类型id,字典数据id获得,该字典数据的子的数据 递归
	 * 判断有没有类型ID字段 有没有排序字段
	 * 递归 树形机构的字典 
	 * dictdataid=""获取所有的字典数据
	 */
	public List getRecursionChildDictdataListByDataId(String dicttypeid ,String dictdataid) throws ManagerException {
		List list = new ArrayList();
		if(this.strIsNull(dictdataid)) return list;
		DBUtil dbUtil = new DBUtil();
		Data dicttype = getDicttypeById(dicttypeid);		
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();		
		int is_tree = dicttype.getIsTree();	
		String data_parentId_field = "";
		StringBuffer sql = new StringBuffer();
		if(DICTDATA_IS_TREE==is_tree){
			data_parentId_field = dicttype.getDataParentIdFild();
			sql.append("select 1 ");			
			if(!this.strIsNull(data_name_filed)){
				sql.append(",").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",").append(data_value_field);
			}	
			if(this.strIsNull(dictdataid)){
				sql.append(",").append(data_parentId_field)
				.append(" from ").append(data_table_name).append(" start with ").append(data_value_field)
				.append(" is null connect by prior ").append(data_value_field)
				.append("=").append(data_parentId_field).append(" where 1=1 ");
			}else{
				sql.append(",").append(data_parentId_field)
				.append(" from ").append(data_table_name).append(" start with ").append(data_value_field)
				.append("='").append(dictdataid).append("' connect by prior ").append(data_value_field)
				.append("=").append(data_parentId_field).append(" where 1=1 ");
			}
		}
		//判断是否有 类型ID字段
		if(!this.strIsNull(dicttype.getDataTypeIdField())){
			sql.append(" and ").append(data_typeid_field).append("='").append(dicttypeid).append("'  ");
		}

		//判断是否有 排序字段 没有排序字段 就按 值排序
		if(!this.strIsNull(dicttype.getDataOrderField())){
			sql.append(" order by ").append(data_order_field);
		}else{
			if(!this.strIsNull(data_value_field)){
				sql.append(" order by ").append(data_value_field);
			}
		}

		try {			
			dbUtil.executeSelect(data_dbName,sql.toString());
			for(int i= 0;i < dbUtil.size();i++){
				Item dictdata = new Item();
//				dictdata.setItemId(dbUtil.getString(i,"dictdata_id"));
				dictdata.setName(dbUtil.getString(i,data_name_filed)==null?"":dbUtil.getString(i,data_name_filed));
				dictdata.setValue(dbUtil.getString(i,data_value_field)==null?"":dbUtil.getString(i,data_value_field));
//				dictdata.setOrder(dbUtil.getInt(i,data_order_field));				
				dictdata.setDataId(dicttypeid);
				dictdata.setItemId(dictdata.getValue());
				if(DICTDATA_IS_TREE==is_tree){
					//设置父类ID
					dictdata.setParentId(dbUtil.getString(i,data_parentId_field));
				}
				list.add(dictdata);
			}
		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			e.printStackTrace();
		}
		return list;
	}


	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.DictManager#getDictdataList(java.lang.String)
	 * 原来的方法
	 * 根据字典类型ID,获取该类型的直接子数据项的列表
	 * 不递归,不翻页
	 */
	public List getDictdataList(String dicttypeid) throws ManagerException {
		return getChildDictdataListByTypeId(dicttypeid); 
	}

	/**
	 * 根据混合id获取该类型字典数据集合
	 * ids 是123:123:aa 字典类型ID:数据项ID:数据项名称 
	 * 不递归 翻页
	 * 判断是否有排序字段,是否有类型ID字段
	 */
	public ListInfo getDictdataList(String ids, int offset, int size) throws ManagerException {
		ListInfo listInfo = new ListInfo();
		List list = new ArrayList();
		if(this.strIsNull(ids)) return null;
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		String[] ids_ = ids.split(":");
		String dicttypeid = "";
		String data_parentId = "";
		if(ids_.length==3){
			dicttypeid = ids_[0];
			data_parentId = ids_[1];
		}else{
			dicttypeid = ids;
		}
		dicttype = getDicttypeById(dicttypeid);	
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org = dicttype.getData_create_orgid_field();
		int is_tree = dicttype.getIsTree();
		StringBuffer sql = new StringBuffer();
		if(DICTDATA_IS_TREE==is_tree){//树形,根节点取出一级数据项,其他节点,取出其对应的子数据项
			String data_parentId_field = dicttype.getDataParentIdFild();
			sql.append("select 1 ");			
			if(!this.strIsNull(data_name_filed)){
				sql.append(",").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",").append(data_value_field);
			}			
			if(!this.strIsNull(data_validate_field)){
				sql.append(",").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",").append(data_org);
			}
			sql.append(" from ").append(data_table_name).append(" where ");
			if("root".equalsIgnoreCase(data_parentId) || "".equalsIgnoreCase(data_parentId)){//根节点取出一级数据项
				sql.append(data_parentId_field).append("='' or ").append(data_parentId_field).append(" is null");
			}else{
				sql.append(data_parentId_field).append("='").append(data_parentId).append("'  ");	
			}
		}else{//平铺,取出所有记录
			sql.append("select 1 ");			
			if(!this.strIsNull(data_name_filed)){
				sql.append(",").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",").append(data_org);
			}
			sql.append(" from ").append(data_table_name).append(" where 1 = 1 ");
		}
		//判断是否有类型ID字段
		if(!this.strIsNull(data_typeid_field)){
			sql.append(" and ").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
		}
		//判断是否有排序字段 没有排序字段 就按值排序
		if(!this.strIsNull(data_order_field)){
			sql.append(" order by ").append(data_order_field);
		}else{
			if(!this.strIsNull(data_value_field)){
				sql.append(" order by ").append(data_value_field);
			}
		}

		try {			
			dbUtil.executeSelect(data_dbName,sql.toString(),offset,size);
			for(int i= 0;i < dbUtil.size();i++){
				Item dictdata = new Item();
//				dictdata.setItemId(dbUtil.getString(i,"dictdata_id"));
				dictdata.setName(dbUtil.getString(i,data_name_filed)==null?"":dbUtil.getString(i,data_name_filed));
//				System.out.println("name = = = = = " + dbUtil.getString(i,data_name_filed)==null?"":dbUtil.getString(i,data_name_filed));
				dictdata.setValue(dbUtil.getString(i,data_value_field)==null?"":dbUtil.getString(i,data_value_field));
//				System.out.println("value = = = = = " + dbUtil.getString(i,data_value_field)==null?"":dbUtil.getString(i,data_value_field));
//				dictdata.setOrder(dbUtil.getInt(i,data_order_field));				
				dictdata.setDataId(dicttypeid);
//				System.out.println("dicttypeid = " + dicttypeid);
				dictdata.setFlag(false);
				if(!this.strIsNull(data_org)){
					dictdata.setDataOrg(dbUtil.getString(i,data_org));
//					System.out.println("org = " + dbUtil.getString(i,data_org));
				}
				if(!this.strIsNull(data_validate_field)){
					dictdata.setDataValidate(dbUtil.getInt(i,data_validate_field));
//					System.out.println("DataValidate = " + dbUtil.getInt(i,data_validate_field));
				}else{
					dictdata.setDataValidate(1);
				}
//				System.out.println("....................................................................");
				list.add(dictdata);				
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			//e.printStackTrace();
		}
		return listInfo;		
	}

	/**
	 * 根据混合id获取该类型字典数据集合
	 * ids 是123:123:aa 字典类型ID:数据项ID:数据项名称 
	 * 不递归 翻页
	 * 判断是否有排序字段,是否有类型ID字段
	 * @param ids
	 * @param showdata 查询条件：显示数据
	 * @param realitydata 查询条件：真实数据
	 * @param occurOrg 查询条件：所属机构
	 * @param isaVailability 查询条件：是否有效
	 * @param offset
	 * @param size
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getDictdataList(String ids,String showdata, String realitydata, 
			String occurOrg, String isaVailability, int offset, int size) throws ManagerException {
		ListInfo listInfo = new ListInfo();
		List list = new ArrayList();
		if(this.strIsNull(ids)) return null;
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		String[] ids_ = ids.split(":");
		String dicttypeid = "";
		String data_parentId = "";
		if(ids_.length==3){
			dicttypeid = ids_[0];
			data_parentId = ids_[1];
		}else{
			dicttypeid = ids;
		}
		dicttype = getDicttypeById(dicttypeid);	
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org = dicttype.getData_create_orgid_field();
		int is_tree = dicttype.getIsTree();
		StringBuffer sql = new StringBuffer();
		Map map = new HashMap();
		map.put(data_name_filed.toLowerCase(), null);
		map.put(data_value_field.toLowerCase(), null);
		map.put(data_org.toLowerCase(), null);
		List dictatts = getDictdataAttachFieldList(dicttypeid,-1);
		for(int i = 0;dictatts != null && i < dictatts.size(); i ++)
		{
			DictAttachField df = (DictAttachField)dictatts.get(i);
			map.put(df.getTable_column().toLowerCase(), null);
		}
		String primaryColumnNames = getPrimaryColumnNames(data_table_name, map);

		if(DICTDATA_IS_TREE==is_tree){//树形,根节点取出一级数据项,其他节点,取出其对应的子数据项
			String data_parentId_field = dicttype.getDataParentIdFild();
			if(!this.strIsNull(data_org)){//如果机构字段不为空
				sql.append("select * from (");//当数据库表中-有数据的字段-换成机构字段时必须把不是机构ID的数据也查询出来，供用户修改成机构ID数据
			}
			sql.append("select 1 ");			
			if(!this.strIsNull(data_name_filed)){
				sql.append(",a.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",a.").append(data_value_field);
			}			
			if(!this.strIsNull(data_validate_field)){
				sql.append(",a.").append(data_validate_field);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",a.").append(data_order_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",a.").append(data_org).append("||' '||org_name as ").append(data_org)
					.append(" from ").append(data_table_name).append(" a,td_sm_organization tt where 1 = 1 ")
					.append("and a.").append(data_org).append(" = tt.org_id ");
			}else{
				sql.append(" from ").append(data_table_name).append(" a where 1 = 1 ");
			}
			if("root".equalsIgnoreCase(data_parentId) || "".equalsIgnoreCase(data_parentId)){//根节点取出一级数据项
				sql.append(" and (a." + data_parentId_field).append("='' or a.").append(data_parentId_field).append(" is null)");
			}else{
				sql.append(" and a." + data_parentId_field).append("='").append(data_parentId).append("'  ");	
			}

			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and a.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}

			//如果机构字段不为空则显示机构ID加名称
			if(!this.strIsNull(data_org)){
				sql.append(" union (select 1,a.").append(data_name_filed).append(",a.").append(data_value_field);
				if(!this.strIsNull(data_validate_field)){
					sql.append(",a.").append(data_validate_field);
				}
				if(!this.strIsNull(data_order_field)){
					sql.append(",a.").append(data_order_field);
				}
				//append("||'' as ")防止整型数据类型不统一
				sql.append(",a.").append(data_org).append("||'' as ").append(data_org)
					.append(" from ").append(data_table_name).append(" a where 1 = 1 ");
				if("root".equalsIgnoreCase(data_parentId) || "".equalsIgnoreCase(data_parentId)){//根节点取出一级数据项
					sql.append(" and (a." + data_parentId_field).append("='' or a.").append(data_parentId_field).append(" is null)");
				}else{
					sql.append(" and a." + data_parentId_field).append("='").append(data_parentId).append("'  ");	
				}
				//判断是否有类型ID字段
				if(!this.strIsNull(data_typeid_field)){
					sql.append(" and a.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
				}

				sql.append(" minus select 1,a.").append(data_name_filed).append(",a.").append(data_value_field);
				if(!this.strIsNull(data_validate_field)){
					sql.append(",a.").append(data_validate_field);
				}
				if(!this.strIsNull(data_order_field)){
					sql.append(",a.").append(data_order_field);
				}
				sql.append(",a.").append(data_org).append("||'' as ").append(data_org)
					.append(" from ").append(data_table_name)
					.append(" a,td_sm_organization tt where 1 = 1 and a.").append(data_org).append("=tt.org_id ");
				//判断是否有类型ID字段
				if(!this.strIsNull(data_typeid_field)){
					sql.append(" and a.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
				}
				if("root".equalsIgnoreCase(data_parentId) || "".equalsIgnoreCase(data_parentId)){//根节点取出一级数据项
					sql.append(" and (a." + data_parentId_field).append("='' or a.").append(data_parentId_field).append(" is null)");
				}else{
					sql.append(" and a." + data_parentId_field).append("='").append(data_parentId).append("'  ");	
				}
				sql.append(")) a where 1=1 ");
			}

			//条件查询
			if(!"".equals(showdata)){
				sql.append(" and a.").append(data_name_filed).append(" like ")
					.append("'%").append(showdata).append("%'");
			}
			if(!"".equals(realitydata)){
				sql.append(" and a.").append(data_value_field).append(" like ")
					.append("'%").append(realitydata).append("%'");
			}
			if(!"".equals(occurOrg)){
				sql.append(" and a.").append(data_org).append(" like ")
					.append("'%").append(occurOrg).append("%'");
			}
			if(!"-1".equals(isaVailability)){
				sql.append(" and a.").append(data_validate_field).append("=")
					.append("'").append(isaVailability).append("'");
			}

		}else{//平铺,取出所有记录
			if(!this.strIsNull(data_org)){
				sql.append("select * from (");
			}
			sql.append("select 1 ");			
			if(!this.strIsNull(data_name_filed)){
				sql.append(",a.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",a.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",a.").append(data_validate_field);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",a.").append(data_order_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",a.").append(data_org).append("||' '||org_name as ").append(data_org);
			}

			sql.append(primaryColumnNames);
			sql.append(" from ").append(data_table_name).append(" a");
			if(!this.strIsNull(data_org)){
				sql.append(",td_sm_organization tt where 1 = 1 and a.").append(data_org).append("=tt.org_id");
			}else{
				sql.append(" where 1 = 1 ");
			}
			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and a.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}
			//如果机构字段不为空则显示机构ID加名称
			if(!this.strIsNull(data_org)){
				sql.append(" union (select 1,a.").append(data_name_filed).append(",a.").append(data_value_field);
				if(!this.strIsNull(data_validate_field)){
					sql.append(",a.").append(data_validate_field);
				}
				if(!this.strIsNull(data_order_field)){
					sql.append(",a.").append(data_order_field);
				}
				sql.append(primaryColumnNames);//++primary
				//append("||'' as ")防止整型数据类型不统一
				sql.append(",a.").append(data_org).append("||'' as ").append(data_org)
					.append(" from ").append(data_table_name).append(" a where 1 = 1 ");
				//判断是否有类型ID字段
				if(!this.strIsNull(data_typeid_field)){
					sql.append(" and a.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
				}

				sql.append(" minus select 1,a.").append(data_name_filed).append(",a.").append(data_value_field);
				if(!this.strIsNull(data_validate_field)){
					sql.append(",a.").append(data_validate_field);
				}
				if(!this.strIsNull(data_order_field)){
					sql.append(",a.").append(data_order_field);
				}
				sql.append(primaryColumnNames);//++primary
				sql.append(",a.").append(data_org).append("||'' as ").append(data_org)
					.append(" from ").append(data_table_name)
					.append(" a,td_sm_organization tt where 1 = 1 and a.").append(data_org).append("=tt.org_id ");
				//判断是否有类型ID字段
				if(!this.strIsNull(data_typeid_field)){
					sql.append(" and a.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
				}
				sql.append(")) a where 1=1 ");
			}
			//条件查询
			if(!"".equals(showdata)){
				sql.append(" and a.").append(data_name_filed).append(" like ")
					.append("'%").append(showdata).append("%'");
			}
			if(!"".equals(realitydata)){
				sql.append(" and a.").append(data_value_field).append(" like ")
					.append("'%").append(realitydata).append("%'");
			}
			if(!"".equals(occurOrg)){
				sql.append(" and a.").append(data_org).append(" like ")
					.append("'%").append(occurOrg).append("%'");
			}
			if(!"-1".equals(isaVailability)){
				sql.append(" and a.").append(data_validate_field).append("=")
					.append("'").append(isaVailability).append("'");
			}
		}

		//判断是否有排序字段 没有排序字段 就按值排序
		if(!this.strIsNull(data_order_field)){
			sql.append(" order by a.").append(data_order_field);
		}else{
			if(!this.strIsNull(data_value_field)){
				sql.append(" order by a.").append(data_value_field);
			}
		}

		try {			
			if(offset==-2 && size == -2){
				dbUtil.executeSelect(data_dbName,sql.toString());
			}else{
				dbUtil.executeSelect(data_dbName,sql.toString(),offset,size);
			}
			for(int i= 0;i < dbUtil.size();i++){
				Item dictdata = new Item();
//				dictdata.setItemId(dbUtil.getString(i,"dictdata_id"));
				dictdata.setName(dbUtil.getString(i,data_name_filed)==null?"":dbUtil.getString(i,data_name_filed));
//				System.out.println("name = = = = = " + dbUtil.getString(i,data_name_filed)==null?"":dbUtil.getString(i,data_name_filed));
				dictdata.setValue(dbUtil.getString(i,data_value_field)==null?"":dbUtil.getString(i,data_value_field));
//				System.out.println("value = = = = = " + dbUtil.getString(i,data_value_field)==null?"":dbUtil.getString(i,data_value_field));
//				dictdata.setOrder(dbUtil.getInt(i,data_order_field));				
				dictdata.setDataId(dicttypeid);
//				System.out.println("dicttypeid = " + dicttypeid);
				dictdata.setFlag(false);
				if(!this.strIsNull(data_org)){
					dictdata.setDataOrg(dbUtil.getString(i,data_org));
//					System.out.println("org = " + dbUtil.getString(i,data_org));
				}
				if(!this.strIsNull(data_validate_field)){
					dictdata.setDataValidate(dbUtil.getInt(i,data_validate_field));
//					System.out.println("DataValidate = " + dbUtil.getInt(i,data_validate_field));
				}else{
					dictdata.setDataValidate(1);
				}
//				System.out.println("....................................................................");
				//将主键信息存入map;主键信息只将高级字段中的主键字段得到，不包括字典定义中的主键字段
				if(!"".equals(primaryColumnNames)){
					Map primaryKey = new HashMap();
					String primaryColumnName = primaryColumnNames.substring(1);
					String[] primaryColumnNameArr = primaryColumnName.split(",");
					for(int primaryCount = 0; primaryCount < primaryColumnNameArr.length; primaryCount++){
						//System.out.println(dbUtil.getString(i, primaryColumnNameArr[primaryCount].substring(2)));
						primaryKey.put(primaryColumnNameArr[primaryCount].substring(2).toLowerCase(), dbUtil.getString(i, primaryColumnNameArr[primaryCount].substring(2)));
					}
					dictdata.setPrimarykeys(primaryKey);
				}
				list.add(dictdata);				
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			//e.printStackTrace();
		}
		return listInfo;		
	}

	public ListInfo getDictdataList(String ids,String showdata, String realitydata, 
			String occurOrg, String isaVailability,String attachFieldSql, int offset, int size) throws ManagerException{
		ListInfo listInfo = new ListInfo();
		List list = new ArrayList();
		if(this.strIsNull(ids)) return null;
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		String[] ids_ = ids.split(":");
		String dicttypeid = "";
		String data_parentId = "";
		if(ids_.length==3){
			dicttypeid = ids_[0];
			data_parentId = ids_[1];
		}else{
			dicttypeid = ids;
		}
		dicttype = getDicttypeById(dicttypeid);	
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org = dicttype.getData_create_orgid_field();
		int is_tree = dicttype.getIsTree();
		StringBuffer sql = new StringBuffer();
		Map map = new HashMap();
		map.put(data_name_filed.toLowerCase(), null);
		map.put(data_value_field.toLowerCase(), null);
		map.put(data_org.toLowerCase(), null);
		List dictatts = getDictdataAttachFieldList(dicttypeid,-1);
		for(int i = 0;dictatts != null && i < dictatts.size(); i ++)
		{
			DictAttachField df = (DictAttachField)dictatts.get(i);
			map.put(df.getTable_column().toLowerCase(), null);
		}
		String primaryColumnNames = getPrimaryColumnNames(data_table_name, map);

		if(DICTDATA_IS_TREE==is_tree){//树形,根节点取出一级数据项,其他节点,取出其对应的子数据项
			String data_parentId_field = dicttype.getDataParentIdFild();
			if(!this.strIsNull(data_org)){//如果机构字段不为空
				sql.append("select * from (");//当数据库表中-有数据的字段-换成机构字段时必须把不是机构ID的数据也查询出来，供用户修改成机构ID数据
			}
			sql.append("select 1 ");			
			if(!this.strIsNull(data_name_filed)){
				sql.append(",a.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",a.").append(data_value_field);
			}			
			if(!this.strIsNull(data_validate_field)){
				sql.append(",a.").append(data_validate_field);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",a.").append(data_order_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",a.").append(data_org).append("||' '||tt.remark5 as ").append(data_org)
					.append(" from ").append(data_table_name).append(" a,td_sm_organization tt where 1 = 1 ")
					.append("and a.").append(data_org).append(" = tt.org_id ");
			}else{
				sql.append(" from ").append(data_table_name).append(" a where 1 = 1 ");
			}
			if("root".equalsIgnoreCase(data_parentId) || "".equalsIgnoreCase(data_parentId)){//根节点取出一级数据项
				sql.append(" and (a." + data_parentId_field).append("='' or a.").append(data_parentId_field).append(" is null)");
			}else{
				sql.append(" and a." + data_parentId_field).append("='").append(data_parentId).append("'  ");	
			}

			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and a.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}
			//高级字段查询条件
			if(attachFieldSql != null && !"".equals(attachFieldSql)){
				sql.append(attachFieldSql);
			}

			//如果机构字段不为空则显示机构ID加名称
			if(!this.strIsNull(data_org)){
				sql.append(" union (select 1,a.").append(data_name_filed).append(",a.").append(data_value_field);
				if(!this.strIsNull(data_validate_field)){
					sql.append(",a.").append(data_validate_field);
				}
				if(!this.strIsNull(data_order_field)){
					sql.append(",a.").append(data_order_field);
				}
				//append("||'' as ")防止整型数据类型不统一
				sql.append(",a.").append(data_org).append("||'' as ").append(data_org)
					.append(" from ").append(data_table_name).append(" a where 1 = 1 ");
				if("root".equalsIgnoreCase(data_parentId) || "".equalsIgnoreCase(data_parentId)){//根节点取出一级数据项
					sql.append(" and (a." + data_parentId_field).append("='' or a.").append(data_parentId_field).append(" is null)");
				}else{
					sql.append(" and a." + data_parentId_field).append("='").append(data_parentId).append("'  ");	
				}
				//判断是否有类型ID字段
				if(!this.strIsNull(data_typeid_field)){
					sql.append(" and a.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
				}
				//高级字段查询条件
				if(attachFieldSql != null && !"".equals(attachFieldSql)){
					sql.append(attachFieldSql);
				}

				sql.append(" minus select 1,a.").append(data_name_filed).append(",a.").append(data_value_field);
				if(!this.strIsNull(data_validate_field)){
					sql.append(",a.").append(data_validate_field);
				}
				if(!this.strIsNull(data_order_field)){
					sql.append(",a.").append(data_order_field);
				}
				sql.append(",a.").append(data_org).append("||'' as ").append(data_org)
					.append(" from ").append(data_table_name)
					.append(" a,td_sm_organization tt where 1 = 1 and a.").append(data_org).append("=tt.org_id ");
				//判断是否有类型ID字段
				if(!this.strIsNull(data_typeid_field)){
					sql.append(" and a.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
				}
				//高级字段查询条件
				if(attachFieldSql != null && !"".equals(attachFieldSql)){
					sql.append(attachFieldSql);
				}
				if("root".equalsIgnoreCase(data_parentId) || "".equalsIgnoreCase(data_parentId)){//根节点取出一级数据项
					sql.append(" and (a." + data_parentId_field).append("='' or a.").append(data_parentId_field).append(" is null)");
				}else{
					sql.append(" and a." + data_parentId_field).append("='").append(data_parentId).append("'  ");	
				}
				sql.append(")) a where 1=1 ");
			}

			//条件查询
			if(!"".equals(showdata)){
				sql.append(" and a.").append(data_name_filed).append(" like ")
					.append("'%").append(showdata).append("%'");
			}
			if(!"".equals(realitydata)){
				sql.append(" and a.").append(data_value_field).append(" like ")
					.append("'%").append(realitydata).append("%'");
			}
			if(!"".equals(occurOrg)){
				sql.append(" and a.").append(data_org).append(" like ")
					.append("'%").append(occurOrg).append("%'");
			}
			if(!"-1".equals(isaVailability)){
				sql.append(" and a.").append(data_validate_field).append("=")
					.append("'").append(isaVailability).append("'");
			}

		}else{//平铺,取出所有记录
			sql.append("select 1 ");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",a.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",a.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",a.").append(data_validate_field);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",a.").append(data_order_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",nvl(a.").append(data_org).append(",'机构ID为空')||' '||nvl(tt.remark5,'机构名称为空') as ").append(data_org);
			}
			
			 for(int z=0;dictatts!= null && z<dictatts.size();z++){
			        DictAttachField dictatt = (DictAttachField)dictatts.get(z);
				//InputType inputType = dictatt.getInputType();	

			        sql.append(",").append(dictatt.getTable_column());
			 }
			sql.append(primaryColumnNames);
			sql.append(" from ").append(data_table_name).append(" a");
			if(!this.strIsNull(data_org)){
				sql.append(" LEFT JOIN TD_SM_ORGANIZATION tt ON a.").append(data_org).append("=tt.org_id  where 1 = 1 ");
			}else{
				sql.append(" where 1 = 1 ");
			}
			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and a.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}
			//高级字段查询条件
			if(attachFieldSql != null && !"".equals(attachFieldSql)){
				sql.append(attachFieldSql);
			}


			//条件查询
			if(!"".equals(showdata)){
				sql.append(" and a.").append(data_name_filed).append(" like ")
					.append("'%").append(showdata).append("%'");
			}
			if(!"".equals(realitydata)){
				sql.append(" and a.").append(data_value_field).append(" like ")
					.append("'%").append(realitydata).append("%'");
			}
			if(!"".equals(occurOrg)){
				sql.append(" and nvl(a.").append(data_org).append(",'机构ID为空')||' '||nvl(tt.remark5,'机构名称为空') like ")
					.append("'%").append(occurOrg).append("%'");
			}
			if(!"-1".equals(isaVailability)){
				sql.append(" and a.").append(data_validate_field).append("=")
					.append("'").append(isaVailability).append("'");
			}
		}

		//判断是否有排序字段 没有排序字段 就按值排序
		if(!this.strIsNull(data_order_field)){
			sql.append(" order by a.").append(data_order_field);
			if(!this.strIsNull(data_value_field)){
				sql.append(",a.").append(data_name_filed);
			}
		}else{
			if(!this.strIsNull(data_value_field)){
				sql.append(" order by a.").append(data_name_filed);
			}
		}

		try {			
			if(offset==-2 && size == -2){
				dbUtil.executeSelect(data_dbName,sql.toString());
			}else{
//				System.out.println("sql = " + sql.toString());
				dbUtil.executeSelect(data_dbName,sql.toString(),offset,size);
			}
			for(int i= 0;i < dbUtil.size();i++){
				Item dictdata = new Item();
				dictdata.setName(dbUtil.getString(i,data_name_filed)==null?"":dbUtil.getString(i,data_name_filed));
				dictdata.setValue(dbUtil.getString(i,data_value_field)==null?"":dbUtil.getString(i,data_value_field));		
				dictdata.setDataId(dicttypeid);
				dictdata.setFlag(false);
				if(!this.strIsNull(data_org)){
					dictdata.setDataOrg(dbUtil.getString(i,data_org));
				}
				if(!this.strIsNull(data_validate_field)){
					dictdata.setDataValidate(dbUtil.getInt(i,data_validate_field));
				}else{
					dictdata.setDataValidate(1);
				}
				if(!"".equals(primaryColumnNames)){
					Map primaryKey = new HashMap();
					String primaryColumnName = primaryColumnNames.substring(1);
					String[] primaryColumnNameArr = primaryColumnName.split(",");
					for(int primaryCount = 0; primaryCount < primaryColumnNameArr.length; primaryCount++){
						primaryKey.put(primaryColumnNameArr[primaryCount].substring(2).toLowerCase(), dbUtil.getString(i, primaryColumnNameArr[primaryCount].substring(2)));
					}
					dictdata.setPrimarykeys(primaryKey);
				}
				list.add(dictdata);				
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		}catch(Exception e){
			e.printStackTrace();
		}
		return listInfo;
	}

	/**
	 * 机构授权编码维护列表。超级管理员-分为已设置项列表与未设置项列表 gao.tang 2008.1.4
	 * @param id 字典ID
 	 * @param orgId 根据机构判断已设置项与未设置项
	 * @param showdata 数据项名称
	 * @param identifier 已设置项与未设置项标识：值为sealed时标识为未设置项，值为selected时标识为已设置项
	 * @param offset
	 * @param size
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getDictdataList(String id, String orgId, String showdata,String identifier, int offset, int size) throws ManagerException {
		ListInfo listInfo = new ListInfo();
		List list = new ArrayList();
		if(this.strIsNull(id)) return null;
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		String dicttypeid = id;
		dicttype = getDicttypeById(dicttypeid);	
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org = dicttype.getData_create_orgid_field();
		StringBuffer sql = new StringBuffer();
		//平铺,取出所有记录
		if(identifier.equals("sealed")){//未设置项的数据
			sql.append("select * from (select 1 ");			
			if(!this.strIsNull(data_name_filed)){
				sql.append(",t.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",t.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",t.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",org.orgnumber||' '||org.org_name as ").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",t.").append(data_order_field).append(" as orby ");
			}

			if(!this.strIsNull(data_org)){
				sql.append(" from td_sm_organization org, ").append(data_table_name).append(" t where 1 = 1 ")
				.append("and org.org_id=t.").append(data_org);
				if(!orgId.equals("") && orgId != null){
					sql.append(" and t.").append(data_org).append(" in (select org_id from  td_sm_organization start with ")
					.append("org_id='").append(orgId).append("' connect by prior parent_id = org_id) ");
				}
			}else{
				sql.append(" from ").append(data_table_name).append(" t where 1 = 1 ");
			}
			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and ").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}
			sql.append(" minus  select 1 ");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",t.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",t.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",t.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",org.orgnumber||' '||org.org_name as ").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",t.").append(data_order_field).append(" as orby ");
			}

			if(!this.strIsNull(data_org)){
				sql.append(" from td_sm_organization org, TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" t ")
					.append("where tax.data_value = t.").append(data_value_field)
					.append(" and org.org_id=t.").append(data_org);
			}else{
				sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" t ")
					.append("where tax.data_value = t.").append(data_value_field);
			}
			sql.append(" and tax.opcode = 'read' and tax.data_name = t.")
				.append(data_name_filed)
				.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
				.append("and tax.org_id = '").append(orgId).append("') o where 1=1 ");
		}else{//已设置项数据
			sql.append("select 1 ");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",o.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",o.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",o.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",org.orgnumber||' '||org.org_name as ").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",o.").append(data_order_field).append(" as orby ");
			}

			if(!this.strIsNull(data_org)){
				sql.append(" from td_sm_organization org,TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" o ")
					.append("where tax.data_value = o.").append(data_value_field)
					.append(" and org.org_id=o.").append(data_org);
			}else{
				sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" o ")
					.append("where tax.data_value = o.").append(data_value_field);
			}
			sql.append(" and tax.opcode = 'read' and ")
				.append("tax.data_name = o.").append(data_name_filed)
				.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
				.append("and tax.org_id = '").append(orgId).append("' ");
		}

		if(!"".equals(showdata)){
			sql.append(" and o.").append(data_name_filed).append(" like ")
				.append("'%").append(showdata).append("%'");
		}

		//判断是否有排序字段 没有排序字段 就按值排序
		if(!this.strIsNull(data_order_field)){
			sql.append(" order by orby");
		}else{
			if(!this.strIsNull(data_value_field)){
				sql.append(" order by o.").append(data_value_field);
			}
		}

		try {			
			dbUtil.executeSelect(data_dbName,sql.toString(),offset,size);
			for(int i= 0;i < dbUtil.size();i++){
				Item dictdata = new Item();
//				dictdata.setItemId(dbUtil.getString(i,"dictdata_id"));
				dictdata.setName(dbUtil.getString(i,data_name_filed)==null?"":dbUtil.getString(i,data_name_filed));
//				System.out.println("name = = = = = " + dbUtil.getString(i,data_name_filed)==null?"":dbUtil.getString(i,data_name_filed));
				dictdata.setValue(dbUtil.getString(i,data_value_field)==null?"":dbUtil.getString(i,data_value_field));
//				System.out.println("value = = = = = " + dbUtil.getString(i,data_value_field)==null?"":dbUtil.getString(i,data_value_field));
//				dictdata.setOrder(dbUtil.getInt(i,data_order_field));				
				dictdata.setDataId(dicttypeid);
//				System.out.println("dicttypeid = " + dicttypeid);
				dictdata.setFlag(false);

				if(!this.strIsNull(data_org)){
					dictdata.setDataOrg(dbUtil.getString(i,data_org));
//					System.out.println("org = " + dbUtil.getString(i,data_org));
				}
				if(!this.strIsNull(data_validate_field)){
					dictdata.setDataValidate(dbUtil.getInt(i,data_validate_field));
//					System.out.println("DataValidate = " + dbUtil.getInt(i,data_validate_field));
				}else{
					dictdata.setDataValidate(1);
				}
//				System.out.println("....................................................................");
				list.add(dictdata);				
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			//e.printStackTrace();
		}
		return listInfo;		
	}

	/**
	 * 部门管理员 
	 * 有权限看到 授权字典数据项 翻页
	 * 
	 * 根据混合id获取该类型字典数据集合
	 * ids 是123:123:aa 字典类型ID:数据项ID:数据项名称 
	 * 不递归 翻页
	 * 判断是否有排序字段,是否有类型ID字段
	 * @param ids
	 * @param showdata 查询条件：显示数据
	 * @param realitydata 查询条件：真实数据
	 * @param occurOrg 查询条件：所属机构
	 * @param isaVailability 查询条件：是否有效
	 * @param offset
	 * @param size
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getOrgManagerDictdataList(String ids,String showdata, String realitydata, 
			String occurOrg, String isaVailability, int offset, int size,String userId) throws ManagerException {
		if(this.strIsNull(userId)){
			return getDictdataList(ids,showdata, realitydata, occurOrg,  isaVailability,  offset,  size);
		}
		ListInfo listInfo = new ListInfo();
		List list = new ArrayList();
		if(this.strIsNull(ids)) return null;
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		String[] ids_ = ids.split(":");
		String dicttypeid = "";
		String data_parentId = "";
		if(ids_.length==3){
			dicttypeid = ids_[0];
			data_parentId = ids_[1];
		}else{
			dicttypeid = ids;
		}
		dicttype = getDicttypeById(dicttypeid);	
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org = dicttype.getData_create_orgid_field();
		int is_tree = dicttype.getIsTree();
		StringBuffer sql = new StringBuffer();
		//平铺,取出所有记录
		sql.append("select 1 ");			
		if(!this.strIsNull(data_name_filed)){
			sql.append(",t.").append(data_name_filed);
		}
		if(!this.strIsNull(data_value_field)){
			sql.append(",t.").append(data_value_field);
		}
		if(!this.strIsNull(data_validate_field)){
			sql.append(",t.").append(data_validate_field);
		}
		if(!this.strIsNull(data_org)){
			sql.append(",t.").append(data_org);
		}
		sql.append(" from ").append(data_table_name).append(" t, TD_SM_TAXCODE_ORGANIZATION torg  ")
		.append(" where  torg.DATA_VALUE= t.").append(data_value_field)
		.append(" and torg.DATA_NAME=t.").append(data_name_filed)
		.append(" and torg.DICTTYPE_ID='").append(dicttypeid).append("' and torg.ORG_ID in (")
		.append("select om.ORG_ID from TD_SM_ORGMANAGER om where om.USER_ID=").append(userId).append(") and torg.opcode='read' ");
		//条件查询
		if(!"".equals(showdata)){
			sql.append(" and t.").append(data_name_filed).append(" like ")
				.append("'%").append(showdata).append("%'");
		}
		if(!"".equals(realitydata)){
			sql.append(" and t.").append(data_value_field).append(" like ")
				.append("'%").append(realitydata).append("%'");
		}
		if(!"".equals(occurOrg)){
			sql.append(" and t.").append(data_org).append(" like ")
				.append("'%").append(occurOrg).append("%'");
		}
		if(!"-1".equals(isaVailability)){
			sql.append(" and t.").append(data_validate_field).append("=")
				.append("'").append(isaVailability).append("'");
		}

		//判断是否有类型ID字段
		if(!this.strIsNull(data_typeid_field)){
			sql.append(" and t.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
		}
		//判断是否有排序字段 没有排序字段 就按值排序
		if(!this.strIsNull(data_order_field)){
			sql.append(" order by t.").append(data_order_field);
		}else{
			if(!this.strIsNull(data_value_field)){
				sql.append(" order by t.").append(data_value_field);
			}
		}

		try {			
			if(offset==-2 && size == -2){//不翻页
				dbUtil.executeSelect(data_dbName,sql.toString());
			}else{
				dbUtil.executeSelect(data_dbName,sql.toString(),offset,size);
			}
			for(int i= 0;i < dbUtil.size();i++){
				Item dictdata = new Item();
				dictdata.setName(dbUtil.getString(i,data_name_filed)==null?"":dbUtil.getString(i,data_name_filed));
				dictdata.setValue(dbUtil.getString(i,data_value_field)==null?"":dbUtil.getString(i,data_value_field));
				dictdata.setDataId(dicttypeid);
				dictdata.setFlag(false);
				if(!this.strIsNull(data_org)){
					dictdata.setDataOrg(dbUtil.getString(i,data_org));
				}
				if(!this.strIsNull(data_validate_field)){
					dictdata.setDataValidate(dbUtil.getInt(i,data_validate_field));
				}else{
					dictdata.setDataValidate(1);
				}
				list.add(dictdata);				
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			//e.printStackTrace();
		}
		return listInfo;		
	}

	/**
	 * 机构权限看到 授权字典数据项 翻页
	 * 
	 * 根据混合id获取该类型字典数据集合
	 * ids 是123:123:aa 字典类型ID:数据项ID:数据项名称 
	 * 不递归 翻页
	 * 判断是否有排序字段,是否有类型ID字段
	 * @param ids
	 * @param showdata 查询条件：显示数据
	 * @param realitydata 查询条件：真实数据
	 * @param occurOrg 查询条件：所属机构
	 * @param isaVailability 查询条件：是否有效
	 * @param offset
	 * @param size
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getOrgReadDictdataList(String ids,String showdata, String realitydata, 
			String occurOrg, String isaVailability, int offset, int size, String orgId) throws ManagerException {
		ListInfo listInfo = new ListInfo();
		List list = new ArrayList();
		if(this.strIsNull(ids)) return null;
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		String[] ids_ = ids.split(":");
		String dicttypeid = "";
		String data_parentId = "";
		if(ids_.length==3){
			dicttypeid = ids_[0];
			data_parentId = ids_[1];
		}else{
			dicttypeid = ids;
		}
		dicttype = getDicttypeById(dicttypeid);	
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org = dicttype.getData_create_orgid_field();
		int is_tree = dicttype.getIsTree();
		StringBuffer sql = new StringBuffer();
		//平铺,取出所有记录
		sql.append("select 1 ");			
		if(!this.strIsNull(data_name_filed)){
			sql.append(",t.").append(data_name_filed);
		}
		if(!this.strIsNull(data_value_field)){
			sql.append(",t.").append(data_value_field);
		}
		if(!this.strIsNull(data_validate_field)){
			sql.append(",t.").append(data_validate_field);
		}
		if(!this.strIsNull(data_org)){
			sql.append(",t.").append(data_org);
		}
		sql.append(" from ").append(data_table_name).append(" t, TD_SM_TAXCODE_ORGANIZATION torg  ")
		.append(" where  torg.DATA_VALUE= t.").append(data_value_field)
		.append(" and torg.DATA_NAME=t.").append(data_name_filed)
		.append(" and torg.DICTTYPE_ID='").append(dicttypeid).append("' and torg.ORG_ID ='")
		.append(orgId).append("' and torg.opcode='read' ");
		//条件查询
		if(!"".equals(showdata)){
			sql.append(" and t.").append(data_name_filed).append(" like ")
				.append("'%").append(showdata).append("%'");
		}
		if(!"".equals(realitydata)){
			sql.append(" and t.").append(data_value_field).append(" like ")
				.append("'%").append(realitydata).append("%'");
		}
		if(!"".equals(occurOrg)){
			sql.append(" and t.").append(data_org).append(" like ")
				.append("'%").append(occurOrg).append("%'");
		}
		if(!"-1".equals(isaVailability)){
			sql.append(" and t.").append(data_validate_field).append("=")
				.append("'").append(isaVailability).append("'");
		}

		//判断是否有类型ID字段
		if(!this.strIsNull(data_typeid_field)){
			sql.append(" and t.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
		}
		//判断是否有排序字段 没有排序字段 就按值排序
		if(!this.strIsNull(data_order_field)){
			sql.append(" order by t.").append(data_order_field);
		}else{
			if(!this.strIsNull(data_value_field)){
				sql.append(" order by t.").append(data_value_field);
			}
		}

		try {			
			if(offset==-2 && size == -2){//不翻页
				dbUtil.executeSelect(data_dbName,sql.toString());
			}else{
				dbUtil.executeSelect(data_dbName,sql.toString(),offset,size);
			}
			for(int i= 0;i < dbUtil.size();i++){
				Item dictdata = new Item();
				dictdata.setName(dbUtil.getString(i,data_name_filed)==null?"":dbUtil.getString(i,data_name_filed));
				dictdata.setValue(dbUtil.getString(i,data_value_field)==null?"":dbUtil.getString(i,data_value_field));
				dictdata.setDataId(dicttypeid);
				dictdata.setFlag(false);
				if(!this.strIsNull(data_org)){
					dictdata.setDataOrg(dbUtil.getString(i,data_org));
				}
				if(!this.strIsNull(data_validate_field)){
					dictdata.setDataValidate(dbUtil.getInt(i,data_validate_field));
				}else{
					dictdata.setDataValidate(1);
				}
				list.add(dictdata);				
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			//e.printStackTrace();
		}
		return listInfo;	
	}

	/**
	 * 普通用户
	 * 有权限看到 授权字典数据项 翻页
	 * 
	 * 根据混合id获取该类型字典数据集合
	 * ids 是123:123:aa 字典类型ID:数据项ID:数据项名称 
	 * 不递归 翻页
	 * 判断是否有排序字段,是否有类型ID字段
	 * @param ids
	 * @param showdata 查询条件：显示数据
	 * @param realitydata 查询条件：真实数据
	 * @param occurOrg 查询条件：所属机构
	 * @param isaVailability 查询条件：是否有效
	 * @param offset
	 * @param size
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getNormalUserDictdataList(String ids,String showdata, String realitydata, 
			String occurOrg, String isaVailability, int offset, int size,String userId) throws ManagerException {
		if(this.strIsNull(userId) ){
			return getDictdataList(ids,showdata, realitydata, occurOrg,  isaVailability,  offset,  size);
		}
		ListInfo listInfo = new ListInfo();
		List list = new ArrayList();
		if(this.strIsNull(ids)) return null;
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		String[] ids_ = ids.split(":");
		String dicttypeid = "";
		String data_parentId = "";
		if(ids_.length==3){
			dicttypeid = ids_[0];
			data_parentId = ids_[1];
		}else{
			dicttypeid = ids;
		}
		dicttype = getDicttypeById(dicttypeid);	
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org = dicttype.getData_create_orgid_field();
		int is_tree = dicttype.getIsTree();
		StringBuffer sql = new StringBuffer();
		//平铺,取出所有记录
		sql.append("select * from ( ");
		sql.append("select 1 ");			
		if(!this.strIsNull(data_name_filed)){
			sql.append(",t.").append(data_name_filed);
		}
		if(!this.strIsNull(data_value_field)){
			sql.append(",t.").append(data_value_field);
		}
		if(!this.strIsNull(data_validate_field)){
			sql.append(",t.").append(data_validate_field);
		}
		if(!this.strIsNull(data_org)){
			sql.append(",t.").append(data_org);
		}
		//排序字段
		if(!this.strIsNull(data_order_field)){
			if((!data_order_field.equalsIgnoreCase(data_name_filed)) &&
				 (!data_order_field.equalsIgnoreCase(data_value_field))&&
				 (!data_order_field.equalsIgnoreCase(data_validate_field))&&
				 (!data_order_field.equalsIgnoreCase(data_org))
			  ){
			    sql.append(",  t.").append(data_order_field);
			}
		}

		sql.append(" from ").append(data_table_name).append(" t, TD_SM_TAXCODE_ORGANIZATION torg  ")
		.append(" where  torg.DATA_VALUE= t.").append(data_value_field)
		.append(" and torg.DATA_NAME=t.").append(data_name_filed)
		.append(" and torg.DICTTYPE_ID='").append(dicttypeid).append("' and torg.ORG_ID in (")
		.append("select ou.ORG_ID from TD_SM_ORGUSER ou where ou.USER_ID=").append(userId).append(") and torg.opcode='read' ");
		//条件查询
		if(!"".equals(showdata)){
			sql.append(" and t.").append(data_name_filed).append(" like ")
				.append("'%").append(showdata).append("%'");
		}
		if(!"".equals(realitydata)){
			sql.append(" and t.").append(data_value_field).append(" like ")
				.append("'%").append(realitydata).append("%'");
		}
		if(!"".equals(occurOrg)){
			sql.append(" and t.").append(data_org).append(" like ")
				.append("'%").append(occurOrg).append("%'");
		}
		if(!"-1".equals(isaVailability)){
			sql.append(" and t.").append(data_validate_field).append("=")
				.append("'").append(isaVailability).append("'");
		}

		//判断是否有类型ID字段
		if(!this.strIsNull(data_typeid_field)){
			sql.append(" and t.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
		}

		//本机构建的数据项 union到一起
		if(!this.strIsNull(data_org)){
			sql.append(" union ");
			sql.append("select 1 ");			
			if(!this.strIsNull(data_name_filed)){
				sql.append(",t.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",t.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",t.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",t.").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				if((!data_order_field.equalsIgnoreCase(data_name_filed)) &&
					 (!data_order_field.equalsIgnoreCase(data_value_field))&&
					 (!data_order_field.equalsIgnoreCase(data_validate_field))&&
					 (!data_order_field.equalsIgnoreCase(data_org))
				  ){
				    sql.append(",  t.").append(data_order_field);
				}
			}

			sql.append(" from ").append(data_table_name).append(" t ")
			.append(" where t.").append(data_org).append(" in (")
            .append("select ou.ORG_ID from TD_SM_ORGUSER ou where ou.USER_ID=").append(userId).append(") ");
			if(!"".equals(showdata)){
				sql.append(" and t.").append(data_name_filed).append(" like ")
					.append("'%").append(showdata).append("%'");
			}
			if(!"".equals(realitydata)){
				sql.append(" and t.").append(data_value_field).append(" like ")
					.append("'%").append(realitydata).append("%'");
			}
			if(!"".equals(occurOrg)){
				sql.append(" and t.").append(data_org).append(" like ")
					.append("'%").append(occurOrg).append("%'");
			}
			if(!"-1".equals(isaVailability)){
				sql.append(" and t.").append(data_validate_field).append("=")
					.append("'").append(isaVailability).append("'");
			}			
			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and t.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}			
		}
		sql.append(") abc ");
		//判断是否有排序字段 没有排序字段 就按值排序
		if(!this.strIsNull(data_order_field)){
			sql.append(" order by abc.").append(data_order_field);
		}else{
			if(!this.strIsNull(data_value_field)){
				sql.append(" order by abc.").append(data_value_field);
			}
		}

		try {			
			if(offset==-2 && size == -2){//不翻页
				dbUtil.executeSelect(data_dbName,sql.toString());
			}else{
				dbUtil.executeSelect(data_dbName,sql.toString(),offset,size);
			}
			for(int i= 0;i < dbUtil.size();i++){
				Item dictdata = new Item();
				dictdata.setName(dbUtil.getString(i,data_name_filed)==null?"":dbUtil.getString(i,data_name_filed));
				dictdata.setValue(dbUtil.getString(i,data_value_field)==null?"":dbUtil.getString(i,data_value_field));
				dictdata.setDataId(dicttypeid);
				dictdata.setFlag(false);
				if(!this.strIsNull(data_org)){
					dictdata.setDataOrg(dbUtil.getString(i,data_org));
				}
				if(!this.strIsNull(data_validate_field)){
					dictdata.setDataValidate(dbUtil.getInt(i,data_validate_field));
				}else{
					dictdata.setDataValidate(1);
				}
				list.add(dictdata);				
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			//e.printStackTrace();
		}
		return listInfo;		
	}


	/**
	 * 根据字典类型ID,获得指定字典类型的子字典类型列表
	 * 直接子类型 当dicttypeId='0'  获取所有类型
	 */
	public List getChildDicttypeList(String dicttypeId) throws ManagerException{

		return getPartChildDicttypeList(dicttypeId,-1);
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getBaseChildDicttypeList(java.lang.String)
	 * 基本字典列表
	 */
	public List getBaseChildDicttypeList(String dicttypeId) throws ManagerException {

		return getPartChildDicttypeList(dicttypeId,this.BASE_DICTTYPE);
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getAllReadChildDicttypeList(java.lang.String)
	 * 所有人可见字典列表
	 */
	public List getAllReadChildDicttypeList(String dicttypeId) throws ManagerException {
		// TODO Auto-generated method stub
		return getPartChildDicttypeList(dicttypeId,this.ALLREAD_BUSINESS_DICTTYPE);
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getPartReadChildDicttypeList(java.lang.String)
	 * 授权可见的字典列表
	 */
	public List getPartReadChildDicttypeList(String dicttypeId) throws ManagerException {
		// TODO Auto-generated method stub
		return getPartChildDicttypeList(dicttypeId,this.PARTREAD_BUSINESS_DICTTYPE);
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getBusinessChildDicttypeList(java.lang.String, int)
	 * 业务字典类表 包括 授权可见和全部可见
	 */
	public List getBusinessChildDicttypeList(String dicttypeId) throws ManagerException {
		// TODO Auto-generated method stub
		return getPartChildDicttypeList(dicttypeId,this.BUSINESS_DICTTYPE);
	}



	/**
	 * 根据字典类型ID,获得指定字典类型的子字典类型列表
	 * 直接子类型 当dicttypeId='0' is null 获取一级类型
	 * typeId==-1 获取全部的子字典
	 * typeId==0 获取基本子字典
	 * typeId==1 获取业务字典, 所有人可见的
	 * typeId==2 获取业务字典, 授权可见的
	 * typeId==3 获取业务字典,包括所有可见和授权可见两种
	 * typeId==5 维护常用的列表
	 * typeId==6 维护附件字段的 0 1 2
	 * typeId==7 权限字典 采集数据和不采集数据的 2 4
	 */
	public List getPartChildDicttypeList(String dicttypeId,int typeId) throws ManagerException{
		List dicttypes = new ArrayList();
		//if(strIsNull(dicttypeId)) return dicttypes;
		DBUtil dbUtil = new DBUtil();
		StringBuffer sql = new StringBuffer()
			.append("select DICTTYPE_TYPE,DICTTYPE_ID,DICTTYPE_NAME,DICTTYPE_DESC,DICTTYPE_PARENT,DATA_TABLE_NAME, ")
			.append("DATA_NAME_FILED ,DATA_VALUE_FIELD,DATA_ORDER_FIELD,DATA_TYPEID_FIELD,DATA_DBNAME, ")
			.append("DATA_PARENTID_FIELD ,IS_TREE,DATA_VALIDATE_FIELD,DATA_CREATE_ORGID_FIELD ")
			//新维护6个字段
			.append(",DATA_NAME_CN,DATA_VALUE_CN,NAME_GENERAL_TYPE,KEY_GENERAL_INFO,NEEDCACHE,ENABLE_VALUE_MODIFY ")
			.append("from TD_SM_DICTTYPE ");
		if(dicttypeId == null || dicttypeId.equals("") || dicttypeId.equals("0"))
		{// dicttypeId='' 获取全部
			sql.append("where (DICTTYPE_PARENT is null or DICTTYPE_PARENT=''  or DICTTYPE_PARENT='0') ");			
		}else{
			if(!"0".equalsIgnoreCase(dicttypeId)){// dicttypeId='0' 获取全部
				sql.append("where DICTTYPE_PARENT= '")
				.append(dicttypeId).append("' ");
			}
		}			

		if(typeId != ALL_DICTTYPE){//不是获取所有字典
			if(typeId == BUSINESS_DICTTYPE){//获取业务字典 三种
				sql.append(" and DICTTYPE_TYPE in (").append(ALLREAD_BUSINESS_DICTTYPE).append(",")
				   .append(PARTREAD_BUSINESS_DICTTYPE).append(",").append(BUSINESS_DICTTYPE_POWERONLY)
				   .append(") ");
			}else if(typeId == BUSINESS_DICTTYPE_USUALONLY){
				sql.append(" and DICTTYPE_TYPE  <> ").append(BASE_DICTTYPE);
			}else if(typeId == ATTACH_DICTTYPE){//维护附件字段的字典类型 0 1 2
				sql.append(" and DICTTYPE_TYPE in (").append(ALLREAD_BUSINESS_DICTTYPE).append(",")
				   .append(PARTREAD_BUSINESS_DICTTYPE).append(",").append(BASE_DICTTYPE)
				   .append(") ");
			}else if(typeId == POWER_DICTTYPE){//权限字典 采集数据和不采集数据的 2 4
				sql.append(" and DICTTYPE_TYPE in (").append(PARTREAD_BUSINESS_DICTTYPE).append(",")
				   .append(BUSINESS_DICTTYPE_POWERONLY).append(") ");
			}else{
				sql.append(" and DICTTYPE_TYPE =").append(typeId);
			}
		}
//		int id = Integer.parseInt(s);
		sql.append(" order by  DICTTYPE_ID desc ");
		//sql.append(" order by  TO_NUMBER(DICTTYPE_ID) desc ");
//		System.out.println(sql.toString());
		try {
			dbUtil.executeSelect(sql.toString());
			for(int i=0;i<dbUtil.size();i++){
				Data dicttype = new Data();
				dicttype.setDescription(dbUtil.getString(i,"DICTTYPE_DESC"));
				dicttype.setDataId(dbUtil.getString(i,"DICTTYPE_ID"));
				dicttype.setName(dbUtil.getString(i,"DICTTYPE_NAME"));
				dicttype.setParent(dbUtil.getString(i,"DICTTYPE_PARENT"));
				//新维护两个字段
				dicttype.setField_name_cn(dbUtil.getString(i,"DATA_NAME_CN"));
				dicttype.setField_value_cn(dbUtil.getString(i,"DATA_VALUE_CN"));
				//新维护四个字段：NAME_GENERAL_TYPE、KEY_GENERAL_INFO、NEEDCACHE、ENABLE_VALUE_MODIFY
				dicttype.setName_general_type(dbUtil.getString(i,"NAME_GENERAL_TYPE"));
				dicttype.setKey_general_info(dbUtil.getString(i,"KEY_GENERAL_INFO"));
				dicttype.setNeedcache(dbUtil.getInt(i,"NEEDCACHE"));
				dicttype.setEnable_value_modify(dbUtil.getInt(i,"ENABLE_VALUE_MODIFY"));

				dicttype.setDataParentIdFild(dbUtil.getString(i,"DATA_PARENTID_FIELD"));
				dicttype.setIsTree(dbUtil.getInt(i,"IS_TREE")); 
				if(strIsNull(dbUtil.getString(i,"DATA_DBNAME"))){
					dicttype.setDataDBName(DEFAULT_DATA_DBNAME);
				}else{
				    dicttype.setDataDBName(dbUtil.getString(i,"DATA_DBNAME"));
				}
				if(strIsNull(dbUtil.getString(i,"DATA_TABLE_NAME"))){
					dicttype.setDataTableName(DEFAULT_DATA_TABLENAME);
				}else{
					dicttype.setDataTableName(dbUtil.getString(i,"DATA_TABLE_NAME"));
				}
				if(strIsNull(dbUtil.getString(i,"DATA_NAME_FILED"))){
					dicttype.setDataNameField(DEFAULT_DATA_NAMEFIELD);
				}else{
					dicttype.setDataNameField(dbUtil.getString(i,"DATA_NAME_FILED"));
				}
				if(strIsNull(dbUtil.getString(i,"DATA_VALUE_FIELD"))){
					dicttype.setDataValueField(DEFAULT_DATA_VALUEFIELD);
				}else{
				    dicttype.setDataValueField(dbUtil.getString(i,"DATA_VALUE_FIELD"));
				}
				if(strIsNull(dbUtil.getString(i,"DATA_ORDER_FIELD"))){
					dicttype.setDataOrderField(DEFAULT_DATA_ORDERFIELD);
				}else{
					dicttype.setDataOrderField(dbUtil.getString(i,"DATA_ORDER_FIELD"));
				}
				if(strIsNull(dbUtil.getString(i,"DATA_TYPEID_FIELD"))){
					dicttype.setDataTypeIdField(DEFAULT_DATA_TYPEIDFIELD);
				}else{
					dicttype.setDataTypeIdField(dbUtil.getString(i,"DATA_TYPEID_FIELD"));
				}
				dicttype.setData_validate_field(String.valueOf(dbUtil.getString(0,"DATA_VALIDATE_FIELD")));
				dicttype.setData_create_orgid_field(String.valueOf(dbUtil.getString(0,"DATA_CREATE_ORGID_FIELD")));
				dicttype.setDicttype_type(dbUtil.getInt(i,"DICTTYPE_TYPE"));
				dicttypes.add(dicttype);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return dicttypes;
	}

	/**
	 * 根据字典类型ID,获得指定字典类型的子字典类型列表
	 * 递归子类型
	 */
	public List getRecursionChildDicttypeList(List dicttypes,String dicttypeId) throws ManagerException{
//		List dicttypes = new ArrayList();
//		if(true)
//			return dicttypes;
		if(strIsNull(dicttypeId)) return dicttypes;
		DBUtil dbUtil = new DBUtil();
		StringBuffer sql = new StringBuffer()
			.append("select DICTTYPE_ID,DICTTYPE_NAME,DICTTYPE_DESC,DICTTYPE_PARENT,DATA_TABLE_NAME, ")
			.append("DATA_NAME_FILED ,DATA_VALUE_FIELD,DATA_ORDER_FIELD,DATA_TYPEID_FIELD,DATA_DBNAME, ")
			.append("DATA_PARENTID_FIELD ,IS_TREE,DATA_VALIDATE_FIELD,DATA_CREATE_ORGID_FIELD ")
			//新维护两个字段
			.append(",DATA_NAME_CN,DATA_VALUE_CN,NAME_GENERAL_TYPE,KEY_GENERAL_INFO,NEEDCACHE,ENABLE_VALUE_MODIFY ")
			.append("from TD_SM_DICTTYPE where  DICTTYPE_PARENT= '").append(dicttypeId).append("' ");
		try {
			dbUtil.executeSelect(sql.toString());
			for(int i=0;i<dbUtil.size();i++){
				Data dicttype = new Data();
				dicttype.setDescription(dbUtil.getString(i,"DICTTYPE_DESC"));
				dicttype.setDataId(dbUtil.getString(i,"DICTTYPE_ID"));
				dicttype.setName(dbUtil.getString(i,"DICTTYPE_NAME"));
				dicttype.setParent(dbUtil.getString(i,"DICTTYPE_PARENT"));
				dicttype.setDataParentIdFild(dbUtil.getString(i,"DATA_PARENTID_FIELD"));
				dicttype.setIsTree(dbUtil.getInt(i,"IS_TREE")); 
				//新维护两个字段
				dicttype.setField_name_cn(dbUtil.getString(0,"DATA_NAME_CN"));
				dicttype.setField_value_cn(dbUtil.getString(0,"DATA_VALUE_CN"));

				//新维护四个字段：NAME_GENERAL_TYPE、KEY_GENERAL_INFO、NEEDCACHE、ENABLE_VALUE_MODIFY
				dicttype.setName_general_type(dbUtil.getString(i,"NAME_GENERAL_TYPE"));
				dicttype.setKey_general_info(dbUtil.getString(i,"KEY_GENERAL_INFO"));
				dicttype.setNeedcache(dbUtil.getInt(i,"NEEDCACHE"));
				dicttype.setEnable_value_modify(dbUtil.getInt(i,"ENABLE_VALUE_MODIFY"));

				if(strIsNull(dbUtil.getString(i,"DATA_DBNAME"))){
					dicttype.setDataDBName(DEFAULT_DATA_DBNAME);
				}else{
				    dicttype.setDataDBName(dbUtil.getString(i,"DATA_DBNAME"));
				}
				if(strIsNull(dbUtil.getString(i,"DATA_TABLE_NAME"))){
					dicttype.setDataTableName(DEFAULT_DATA_TABLENAME);
				}else{
					dicttype.setDataTableName(dbUtil.getString(i,"DATA_TABLE_NAME"));
				}
				if(strIsNull(dbUtil.getString(i,"DATA_NAME_FILED"))){
					dicttype.setDataNameField(DEFAULT_DATA_NAMEFIELD);
				}else{
					dicttype.setDataNameField(dbUtil.getString(i,"DATA_NAME_FILED"));
				}
				if(strIsNull(dbUtil.getString(i,"DATA_VALUE_FIELD"))){
					dicttype.setDataValueField(DEFAULT_DATA_VALUEFIELD);
				}else{
				    dicttype.setDataValueField(dbUtil.getString(i,"DATA_VALUE_FIELD"));
				}
				if(strIsNull(dbUtil.getString(i,"DATA_ORDER_FIELD"))){
					dicttype.setDataOrderField(DEFAULT_DATA_ORDERFIELD);
				}else{
					dicttype.setDataOrderField(dbUtil.getString(i,"DATA_ORDER_FIELD"));
				}
				if(strIsNull(dbUtil.getString(i,"DATA_TYPEID_FIELD"))){
					dicttype.setDataTypeIdField(DEFAULT_DATA_TYPEIDFIELD);
				}else{
					dicttype.setDataTypeIdField(dbUtil.getString(i,"DATA_TYPEID_FIELD"));
				}
				dicttype.setData_validate_field(String.valueOf(dbUtil.getString(0,"DATA_VALIDATE_FIELD")));
				dicttype.setData_create_orgid_field(String.valueOf(dbUtil.getString(0,"DATA_CREATE_ORGID_FIELD")));
				dicttypes.add(dicttype);
				this.getRecursionChildDicttypeList(dicttypes, dbUtil.getString(i,"DICTTYPE_ID"));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return dicttypes;
	}

	/**
	 * 判断字符串是否为空
	 */
	public boolean strIsNull(String str){
		if(str==null || str.trim().length()==0 || "".equalsIgnoreCase(str) || "null".equalsIgnoreCase(str)){
			return true;
		}else{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.DictManager#isContainDictdata(java.lang.String)
	 * 是否包含字典数据
	 * String ids 字典类型ID:数据项的ID:数据项的值 如: 123:45:aa 根节点 123:root:root
	 */
	public boolean isContainDictdata(String ids) throws ManagerException {
		if(strIsNull(ids)) return false;
		String[] mutiIds = ids.split(":");
		if(mutiIds.length==3){
			List subDatas = null;
			String dicttypeId = mutiIds[0];
			String dictdataId = mutiIds[1];
			if("root".equalsIgnoreCase(dictdataId)){//是根节点,判断字典类型下,是否有字典数据
				subDatas = getChildDictdataListByTypeId(dicttypeId);				
			}else{//判断字典数据下,是否有子数据
				subDatas = getChildDictdataListByDataId(dicttypeId,dictdataId);	
			}			
			if(subDatas != null && subDatas.size()>0){//字典类型下 有字典数据
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.DictManager#getChildDictdataList(java.lang.String)
	 * 获取字典数据
	 * 根据字典类型ID:数据项的ID:数据项名称, 如123:45:aa 根节点 123:root:root
	 * @param ids (字典类型ID:数据项的ID) 组合ID
	 * @return List<com.frameworkset.dictionary.Item>
	 * 不递归
	 */
	public List getChildDictdataList(String ids) throws ManagerException {
		List subDatas = null;
		if(strIsNull(ids)) return subDatas;
		String[] mutiIds = ids.split(":");		
		if(mutiIds.length==3){			
			String dicttypeId = mutiIds[0];
			String dictdataId = mutiIds[1];
			if("root".equalsIgnoreCase(dictdataId)){//是根节点,判断字典类型下,是否有字典数据
				subDatas = getChildDictdataListByTypeId(dicttypeId);				
			}else{//判断字典数据下,是否有子数据
				subDatas = getChildDictdataListByDataId(dicttypeId,dictdataId);				
			}
		}
		return subDatas;
	}



	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.DictManager#getTaxCodesByUserId(java.lang.String)
	 * @return List<com.frameworkset.dictionary.Item>
	 * 根据用户ID,获取用户所在机构的指定税种的编码,如果dicttypeName=="" 取出所有的税种编码
	 * 从TD_SM_TAXCODE_ORGANIZATION中获取
	 */
	public List getTaxCodesByUserIdAndTypeName(String userId,String dicttypeName) {
		List taxCodes = new ArrayList();
		if(strIsNull(userId)) return taxCodes;
		StringBuffer dataTableInfo_sql = new StringBuffer()
			.append("select tco.data_value,tco.DATA_NAME,dt.data_dbname, dt.data_table_name,dt.data_value_field, ")
			.append("dt.data_parentid_field, dt.data_name_filed,dt.data_order_field,dt.data_typeid_field,")
			.append("dt.is_tree from td_sm_orguser ou, ")
			.append("TD_SM_TAXCODE_ORGANIZATION tco ,td_sm_dicttype dt where tco.ORG_ID=ou.ORG_ID ")  
			.append("and dt.dicttype_id=tco.dicttype_id ")
			.append("and ou.user_id=")
			.append(userId);
		if(!strIsNull(dicttypeName)){
			dataTableInfo_sql.append(" and dt.DICTTYPE_NAME='").append(dicttypeName).append("'"); 
		}
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(dataTableInfo_sql.toString());
			if(db.size()>0){
				int is_tree = db.getInt(0,"is_tree");
				String data_value = db.getString(0,"data_value");
				String table_name = db.getString(0,"data_table_name");
				String data_dbname = db.getString(0,"data_dbname");
				String data_name_filed = db.getString(0,"data_name_filed");
				String data_value_field = db.getString(0,"data_value_field");				
				String data_order_field = db.getString(0,"data_order_field");
				String data_typeid_field = db.getString(0,"data_typeid_field");
				StringBuffer sql = new StringBuffer();
				if(DICTDATA_IS_TREE==is_tree){//树形数据项,递归
					String parentid = db.getString(0,"data_parentid_field");
					//约定:data_value_field作为ID,parentid和ID作为构造树形的属性					
					sql.append("select ").append(data_name_filed).append(",")
						.append(data_value_field).append(",").append(data_order_field).append(",")
						.append(data_typeid_field).append(" from ").append(table_name)
						.append(" start with ").append(data_value_field)
						.append("='").append(data_value).append("' connect by proir ")
						.append(data_value_field).append("=").append(parentid);
				}else{//平铺的数据项,不递归
					sql.append("select ").append(data_name_filed).append(",")
					   .append(data_value_field).append(",").append(data_order_field).append(",")
					   .append(data_typeid_field).append(" from ").append(table_name)
					   .append(" where ").append(data_value_field).append("='")
					   .append(data_value).append("'");
				}
				try {	
					DBUtil dbUtil = new DBUtil();
					dbUtil.executeSelect(data_dbname,sql.toString());
					for(int i= 0;i < dbUtil.size();i++){
						Item dictdata = new Item();
						dictdata.setName(dbUtil.getString(i,data_name_filed));
						dictdata.setValue(dbUtil.getString(i,data_value_field));
						dictdata.setOrder(dbUtil.getInt(i,data_order_field));				
						dictdata.setDataId(dbUtil.getString(i,data_typeid_field));
						taxCodes.add(dictdata);
					}
				}catch(Exception e){
					//throw new ManagerException(e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return taxCodes;
	}


	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.DictManager#getTaxCodesByOrgId(java.lang.String)
	 * @return List<com.frameworkset.dictionary.Item>
	 * 根据机构ID,获取机构的指定税种的编码,如果dicttypeName=="" 取出所有的税种编码 
	 * 从 TD_SM_TAXCODE_ORGANIZATION 表中获取 从授权角度出发
	 */
	public List getTaxCodesByOrgIdAndTypeName(String orgId,String dicttypeName) {
		List taxCodes = new ArrayList();
		if(strIsNull(orgId)) return taxCodes;
		StringBuffer dataTableInfo_sql = new StringBuffer()
			.append("select tco.data_value,dt.data_dbname, dt.data_table_name,dt.data_value_field, ")
			.append("dt.data_parentid_field, dt.data_name_filed,dt.data_order_field,dt.data_typeid_field,")
			.append("dt.is_tree from TD_SM_TAXCODE_ORGANIZATION tco ,td_sm_dicttype dt ")
			.append(" where dt.dicttype_id=tco.dicttype_id ")  ;
		if(!strIsNull(dicttypeName)){
			dataTableInfo_sql.append(" and dt.DICTTYPE_NAME='").append(dicttypeName).append("'"); 
		}
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(dataTableInfo_sql.toString());
			if(db.size()>0){
				int is_tree = db.getInt(0,"is_tree");
				String data_value = db.getString(0,"data_value");
				String table_name = db.getString(0,"data_table_name");
				String data_dbname = db.getString(0,"data_dbname");
				String data_name_filed = db.getString(0,"data_name_filed");
				String data_value_field = db.getString(0,"data_value_field");				
				String data_order_field = db.getString(0,"data_order_field");
				String data_typeid_field = db.getString(0,"data_typeid_field");
				StringBuffer sql = new StringBuffer();
				if(DICTDATA_IS_TREE==is_tree){//树形数据项,递归
					String parentid = db.getString(0,"data_parentid_field");
					//约定:data_value_field作为ID,parentid和ID作为构造树形的属性					
					sql.append("select ").append(data_name_filed).append(",")
						.append(data_value_field).append(",").append(data_order_field).append(",")
						.append(data_typeid_field).append(" from ").append(table_name)
						.append(" start with ").append(data_value_field)
						.append("='").append(data_value).append("' connect by proir ")
						.append(data_value_field).append("=").append(parentid);
				}else{//平铺的数据项,不递归
					sql.append("select ").append(data_name_filed).append(",")
					   .append(data_value_field).append(",").append(data_order_field).append(",")
					   .append(data_typeid_field).append(" from ").append(table_name)
					   .append(" where ").append(data_value_field).append("='")
					   .append(data_value).append("'");
				}
				try {	
					DBUtil dbUtil = new DBUtil();
					dbUtil.executeSelect(data_dbname,sql.toString());
					for(int i= 0;i < dbUtil.size();i++){
						Item dictdata = new Item();
						dictdata.setName(dbUtil.getString(i,data_name_filed));
						dictdata.setValue(dbUtil.getString(i,data_value_field));
						dictdata.setOrder(dbUtil.getInt(i,data_order_field));				
						dictdata.setDataId(dbUtil.getString(i,data_typeid_field));
						taxCodes.add(dictdata);
					}
				}catch(Exception e){
					//throw new ManagerException(e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return taxCodes;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.DictManager#isTreeDictdata(java.lang.String)
	 * 根据字典类型ID, 判读该字典的数据项是否树形
	 * @param dicttypeId 数据字典类型ID
	 * @return boolean true:是树形;false:不是树形
	 */
	public boolean isTreeDictdata(String dicttypeId) {
		if(strIsNull(dicttypeId)) return false;
		String sql = "select IS_TREE from TD_SM_DICTTYPE where DICTTYPE_ID='"+dicttypeId+"'";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if(db.size()>0){
				int is_tree = db.getInt(0,"IS_TREE");
				if(is_tree==1){ 
					return true;
				}else{
					return false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.DictManager#storeDictdataAttachField(java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String)
	 * 保存字典数据项的附加字段
	 * 如果附件字段存在update
	 * 否则insert
	 * 不用!!!!!!!!!!!!!!!!!!!
	 */
	public void storeDictdataAttachField(String[] attNames, String[] attLabels, String[] attTypes, String dicttypeId) {
		if(strIsNull(dicttypeId) || attNames.length==0) return;
		DBUtil db = new DBUtil();
		DBUtil batchDB = new DBUtil();
		try {
			for(int i=0;i<attNames.length;i++){
			    String attName = attNames[i];
			    String attLabel = attLabels[i];
			    String attType = attTypes[i];
				String judge = "select count(*) as num from TD_SM_DICATTACHFIELD " +
						"where DICTTYPE_ID='"+ dicttypeId +"' and FIELD_NAME='" +
						attName+"' ";				
				db.executeSelect(judge);
				if(db.size()>0){
					String sql = "";
					if(db.getInt(0,"num")>0){//数据库表中有记录 update
					    sql = "update TD_SM_DICATTACHFIELD set LABEL= '" +
					    		attLabel + "',INPUT_TYPE_ID="+ attType + 
					    		" where DICTTYPE_ID='"+ dicttypeId +"' and FIELD_NAME='" +
								attName+"' ";
					}else{//数据库表中没有记录 insert
						sql = "insert into TD_SM_DICATTACHFIELD(FIELD_NAME,LABEL,INPUT_TYPE_ID,DICTTYPE_ID,) " +
			    					"values('" + attName + "','" + attLabel + "'," + attType + 
			    					",'" +dicttypeId+"') ";
					}
					batchDB.addBatch(sql);					
				}				

			}
			batchDB.executeBatch();
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}finally{
			batchDB.resetBatch();
		}

	}

	/* (non-Javadoc)--gao.tang 0
	 * @see com.frameworkset.platform.dictionary.DictManager#storeOrgTaxcode(java.lang.String, java.lang.String, java.lang.String)
	 * 保存机构与编码之间的关系 TD_SM_TAXCODE_ORGANIZAT
	 * 先删除,后保存//	 
	 */
	public void storeOrgTaxcode(String orgId, String dicttypeId, String[] dictdataValues) {
		if(strIsNull(orgId) || strIsNull(dicttypeId) || dictdataValues==null ) return ;
		DBUtil db = new DBUtil();
		DBUtil addDB = new DBUtil(); 
		TransactionManager tm = new TransactionManager();
		try{
			//子机构sql
			StringBuffer orgs_sql = new StringBuffer()
			.append("select org.org_id from td_sm_organization org start with org.org_id='")
			.append(orgId).append("' connect by prior org.org_id=org.parent_id ");	

			//上级机构
			StringBuffer orgs_parent_sql = new StringBuffer()
			.append("select org.org_id from td_sm_organization org start with org.org_id='")
			.append(orgId).append("' connect by prior org.parent_id=org.org_id ");	

			//保存新数据
			//保存当前机构的
			String[] dataValues = new String[dictdataValues.length];//保存数据项的值 数组
			String[] dataNames = new String[dictdataValues.length];//保存数据项的名称 数组
			for(int i=0;i<dictdataValues.length;i++){

				String dictdataValue = dictdataValues[i];				
				//dictdataValue value:name;
				if(dictdataValue.trim().length()==0) continue;
				String dictdataValuesa[] = dictdataValue.split(":");
				if(dictdataValuesa.length>=2){
					dataValues[i] = dictdataValuesa[0];
					dataNames[i] = dictdataValuesa[1];
				}else{
					dataValues[i] = "";
					dataNames[i] = "";
				}
			}

            //(1) 对照传入的最新的 - 原来老的 数据项 = 冗余的数据项
			//本身多于的"常用"权限
			//本身和子机构多于的"可见"权限
			StringBuffer uselessValue_sql = new StringBuffer();
			uselessValue_sql.append("select data_value from (")
			    .append("select t.data_value from  td_sm_taxcode_organization t where ")
			    .append(" t.DICTTYPE_ID='").append(dicttypeId).append("' and t.ORG_ID='").append(orgId).append("'")
			    .append(" minus ")
			    .append("select t.data_value from  td_sm_taxcode_organization t where ")
			    .append(" t.data_value in (''");
			for(int i=0;i<dataValues.length;i++){				
				uselessValue_sql.append(",'").append(dataValues[i]).append("'");
			}
			uselessValue_sql.append("))");
			tm.begin();
			//(2)删除
			//删除本身多于的"常用"权限			
			StringBuffer delete_useless_usual = new StringBuffer();
			delete_useless_usual.append("delete td_sm_taxcode_organization where DICTTYPE_ID='")
				.append(dicttypeId).append("' and ORG_ID='").append(orgId).append("' and opcode='usual' ")
				.append(" and data_value in (").append(uselessValue_sql.toString()).append(") ");
			//删除本身和子机构 多于的"可见"权限
			StringBuffer delete_subandself_read = new StringBuffer();
			delete_subandself_read.append("delete td_sm_taxcode_organization where DICTTYPE_ID='")
				.append(dicttypeId).append("' and  opcode='read' ")
				.append(" and data_value in (").append(uselessValue_sql.toString()).append(") ")
				.append(" and ORG_ID in (").append(orgs_sql.toString()).append(") "); 
			//加到批处理
			db.addBatch(delete_useless_usual.toString());
			db.addBatch(delete_subandself_read.toString());
			db.executeBatch();


			//(3)保存/更新			
			//本机构 上级机构
			DBUtil parentDB = new DBUtil();
			parentDB.executeSelect(orgs_parent_sql.toString());
			for(int p=0;p<parentDB.size();p++){
				String subOrgId = parentDB.getString(p,"org_id");
				for(int i=0;i<dataValues.length;i++){
					if(dataValues[i].trim().length()==0) continue;
					StringBuffer addNewData = new StringBuffer();
					addNewData.setLength(0);
					addNewData.append("insert into TD_SM_TAXCODE_ORGANIZATION(ORG_ID,DICTTYPE_ID,DATA_VALUE,OPCODE,DATA_NAME) ")
					   .append("(select '").append(subOrgId).append("' as ORG_ID, '").append(dicttypeId)
					   .append("' as DICTTYPE_ID, '").append(dataValues[i]).append("' as DATA_VALUE, 'read' as OPCODE, '")
					   .append(dataNames[i]).append("' as DATA_NAME from ")
					   .append("dual where not exists (select * from TD_SM_TAXCODE_ORGANIZATION where ORG_ID='")
					   .append(subOrgId).append("' and DICTTYPE_ID='").append(dicttypeId)
					   .append("' and DATA_VALUE='" ).append(dataValues[i])
					   .append("' and OPCODE='read' ")
					   .append(")) ");
					addDB.addBatch(addNewData.toString());
					addNewData.setLength(0);
					if(i==900){
						addDB.executeBatch();
					}
				}
			}
			addDB.executeBatch();
			tm.commit();	
            //发事件
			Event event = new EventImpl(dicttypeId,DictionaryChangeEvent.DICTIONARY_USERORG_CHANGE_READ);
			super.change(event,true);

		}catch(Exception e){
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		}
	}

	private int getDictdataValue(String dicttypeId){
		DBUtil db = new DBUtil();
		String sql = "select t.IS_TREE from TD_SM_DICTTYPE t where t.dicttype_id='"+dicttypeId+"'";
		int isTree = 0;
		try {
			db.executeSelect(sql);
			isTree = db.getInt(0,"IS_TREE");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isTree;
	}




	/**
	 * 根据机构ID和字典类型ID,和操作码,获取该机构在该字典类型下绑定的字典数据列表
	 * @param orgId
	 * @param dicttypeId
	 * @param opcode
	 * @return 
	 * DictManagerImpl.java
	 * @author: ge.tao
	 */
	public List getDictdatasByOrgIdAndTypeIdAndOpcode(String orgId,String dicttypeId,String opcode) {
		List dictdataValues = new ArrayList();
		if(strIsNull(orgId) || strIsNull(dicttypeId)) return dictdataValues;
		DBUtil db = new DBUtil();
		StringBuffer sql = new StringBuffer()
			.append("select DATA_VALUE,DATA_NAME from TD_SM_TAXCODE_ORGANIZATION where ORG_ID='")
			.append(orgId).append("' and DICTTYPE_ID='").append(dicttypeId).append("' ");
		if(!"".equalsIgnoreCase(opcode)){
			sql.append("and OPCODE='").append(opcode).append("' ");
		}
		try {
			db.executeSelect(sql.toString());
			sql.setLength(0);
			for(int i=0;i<db.size();i++){
				if(db.getString(i,"DATA_VALUE")==null){
					continue;
				}
				dictdataValues.add(db.getString(i,"DATA_VALUE")+":"+db.getString(i,"DATA_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return dictdataValues;
	}

	/* (non-Javadoc)gao.tang--0
	 * @see com.frameworkset.platform.dictionary.DictManager#getDictdatasByOrgIdAndTypeId()
	 * 根据机构ID和字典类型ID,获取该机构在该字典类型下绑定的字典数据列表 所有(可见/常用)列表
	 * @return List<String dictdataValue>
	 * 从TD_SM_TAXCODE_ORGANIZATION中取
	 */
	public List getDictdatasByOrgIdAndTypeId(String orgId,String dicttypeId) {			
		return this.getDictdatasByOrgIdAndTypeIdAndOpcode(orgId,dicttypeId,"");
	}

	/* (non-Javadoc)gao.tang--0
	 * @see com.frameworkset.platform.dictionary.DictManager#getDictdatasByOrgIdAndTypeId()
	 * 根据机构ID和字典类型ID,获取该机构在该字典类型下绑定的字典数据列表.常用设置字典 usual
	 * @return List<String dictdataValue>
	 * 从TD_SM_TAXCODE_ORGANIZATION中取
	 */
	public List getDictdatasByOrgIdAndTypeId2(String orgId,String dicttypeId) {

		return this.getDictdatasByOrgIdAndTypeIdAndOpcode(orgId,dicttypeId,"usual");
	}

	/* (non-Javadoc)gao.tang--0
	 * @see com.frameworkset.platform.dictionary.DictManager#getDictdatasByOrgIdAndTypeId()
	 * 根据机构ID和字典类型ID,获取该机构在该字典类型下绑定的字典数据列表.可见设置字典 read
	 * @return List<String dictdataValue>
	 * 从TD_SM_TAXCODE_ORGANIZATION中取
	 */
	public List getReadDictdatasByOrgIdAndTypeId(String orgId,String dicttypeId) {

		return this.getDictdatasByOrgIdAndTypeIdAndOpcode(orgId,dicttypeId,"read");
	}

//	/* (non-Javadoc)
//	 * @see com.frameworkset.platform.dictionary.DictManager#getDictdatasByOrgIdAndTypeId()
//	 * 根据机构ID和字典类型ID,获取该机构在该字典类型下绑定的字典数据列表
//	 * @return List<String dictdataValue>
//	 * 从TD_SM_ROLERESOP中取 
//	 */
//	public List getDictdatasByOrgIdAndTypeId(String orgId,String dicttypeId) {
//		List dictdataValues = new ArrayList();
//		if(strIsNull(orgId) || strIsNull(dicttypeId)) return dictdataValues;
//		DBUtil db = new DBUtil();
//		StringBuffer sql = new StringBuffer()
//			.append("select RES_ID from TD_SM_ROLERESOP where OP_ID='read' and RESTYPE_ID='orgTaxcode'  ")
//			.append(" and TYPES='organization' and  ROLE_ID='").append(orgId).append("'  and ")
//			.append("RES_ID like '").append(dicttypeId).append(":%' ");
//		logger.warn(sql.toString());
//		try {
//			db.executeSelect(sql.toString());
//			sql.setLength(0);
//			for(int i=0;i<db.size();i++){
//				if(db.getString(i,"RES_ID")==null){
//					continue;
//				}
//				String typedataValue = "";
//				String[] strs = db.getString(i,"RES_ID").split(":");
//				if(strs.length>1){
//					typedataValue = strs[1];
//				}else{
//					typedataValue = db.getString(i,"RES_ID");
//				}
//				dictdataValues.add(typedataValue);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//		// TODO Auto-generated method stub
//		return dictdataValues;
//	}



	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getVvalidatorTypeByColumnMetaData(com.frameworkset.common.poolman.sql.ColumnMetaData)
	 * return 校验类型在"../user/validateForm.js中定义 有:
	 */
	public String getValidatorTypeByColumnMetaData(ColumnMetaData obj) {
//		if(obj==null) return "string";
//		if("date".equalsIgnoreCase(obj.getTypeName())){
//			return "datetime";
//		}else if("NUMBER".equalsIgnoreCase(obj.getTypeName()) || "LONG".equalsIgnoreCase(obj.getTypeName())){
//			return "int";
//		}else{
//			return "string";
//		}
		if(obj == null ) return "string";
		return this.getValidatorTypeByColumnMetaData(obj,obj.getNullable());
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getVvalidatorTypeByColumnMetaData(com.frameworkset.common.poolman.sql.ColumnMetaData)
	 * return 校验类型在"../user/validateForm.js中定义 有:
	 */
	public String getValidatorTypeByColumnMetaData(ColumnMetaData obj,String nullable) {
		if(obj==null) return "string";
		if(!obj.getNullable().equalsIgnoreCase("yes"))
		{
			nullable = "no";
		}

//		obj.getDECIMAL_DIGITS() > 0;//判断是否是浮点类型
		if("date".equalsIgnoreCase(obj.getTypeName())){
			if("yes".equalsIgnoreCase(nullable)){
				return "datetimeNull";
			}else{
			    return "datetime";
			}
		}else if("NUMBER".equalsIgnoreCase(obj.getTypeName()) || "LONG".equalsIgnoreCase(obj.getTypeName())){
			if("yes".equalsIgnoreCase(nullable)){
				if(obj.getDECIMAL_DIGITS() > 0){
					return "stringNull";
				}
				return "intNull";
			}else{
				if(obj.getDECIMAL_DIGITS() > 0){
					return "string";
				}
				return "int";
			}
		}else{
			if("yes".equalsIgnoreCase(nullable)){
				return "stringNull";
			}else{
			    return "string";
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#dictTypeHasDatas(java.lang.String)
	 * 指定的类型ID, 是否已有字典数据 true:有;false:没有
	 */
	public boolean dictTypeHasDatas(String dicttypeId) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(this.strIsNull(dicttypeId)) return flag;
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		try {
			dicttype = getDicttypeById(dicttypeId);
			//数据保存字段:
			String data_dbName = dicttype.getDataDBName();
			String data_table_name = dicttype.getDataTableName();
			String data_typeid_field = dicttype.getDataTypeIdField();
			StringBuffer judge = new StringBuffer()
				.append("select count(*) as num from ").append(data_table_name);
			if(!this.strIsNull(data_typeid_field)){
				judge.append(" where ").append(data_typeid_field).append("='").append(dicttypeId).append("'");
			}
			dbUtil.executeSelect(data_dbName,judge.toString());
			if(dbUtil.size()>0 && dbUtil.getInt(0,"num")>0){
				flag = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#storeOrgTaxcode(java.lang.String, java.lang.String, java.lang.String)
	 * 先删除,后保存 TD_SM_TAXCODE_ORGANIZATION
	 * 把编码机构关系保存到 资源表
	 * 编码 全部是usual 
	 */
	public void storeUsualOrgTaxcode(String orgId, String dicttypeId, String[] dictdataValues) {
		if(strIsNull(orgId) || strIsNull(dicttypeId) || dictdataValues==null ) return ;
		DBUtil db = new DBUtil();
		DBUtil addDB = new DBUtil();
		TransactionManager tm = new TransactionManager();
		try{
			tm.begin();
			//子机构sql
			StringBuffer orgs_sql = new StringBuffer();
			orgs_sql.append("select org.org_id from td_sm_organization org start with org.org_id='")
			.append(orgId).append("' connect by prior org.org_id=org.parent_id ");		


			//保存新数据
			//保存当前机构的
			String[] dataValues = new String[dictdataValues.length];//保存数据项的值 数组
			String[] dataNames = new String[dictdataValues.length];//保存数据项的名称 数组
			for(int i=0;i<dictdataValues.length;i++){
				String dictdataValue = dictdataValues[i];
				if(dictdataValue.trim().length()==0) continue;
				//dictdataValue value:name;
				String dictdataValuesa[] = dictdataValue.split(":");
				if(dictdataValuesa.length>=2){
					dataValues[i] = dictdataValuesa[0];
					dataNames[i] = dictdataValuesa[1];
				}else{
					dataValues[i] = "";
					dataNames[i] = "";
				}
			}

            //(1) 对照传入的最新的 - 原来老的 数据项 = 冗余的数据项
			//本身多于的"常用"权限
			StringBuffer uselessValue_sql = new StringBuffer();
			uselessValue_sql.append("select data_value from (")
			    .append("select t.data_value from  td_sm_taxcode_organization t where ")
			    .append(" t.DICTTYPE_ID='").append(dicttypeId).append("' and t.ORG_ID='").append(orgId).append("'")
			    .append(" minus ")
			    .append("select t.data_value from  td_sm_taxcode_organization t where ")
			    .append(" t.data_value in (''");
			for(int i=0;i<dataValues.length;i++){				
				uselessValue_sql.append(",'").append(dataValues[i]).append("'");
			}
			uselessValue_sql.append("))");

			//(2)删除
			//删除本身 多于的"常用"权限
			StringBuffer delete_subandself_read = new StringBuffer();
			delete_subandself_read.append("delete td_sm_taxcode_organization where DICTTYPE_ID='")
				.append(dicttypeId).append("' and opcode='usual' ")
				.append(" and data_value in (").append(uselessValue_sql.toString()).append(") ")
				.append(" and ORG_ID ='").append(orgId).append("' "); 
			//加到批处理
			db.addBatch(delete_subandself_read.toString());
			db.executeBatch();

			//(3)保存/更新			
			//不用 保存到 常用维护 和上级机构无关			
			for(int i=0;i<dataValues.length;i++){
				if(dataValues[i].trim().length()==0) continue;
				StringBuffer addNewData = new StringBuffer();
				addNewData.setLength(0);
				addNewData.append("insert into TD_SM_TAXCODE_ORGANIZATION(ORG_ID,DICTTYPE_ID,DATA_VALUE,OPCODE,DATA_NAME) ")
				   .append("(select '").append(orgId).append("' as ORG_ID, '").append(dicttypeId)
				   .append("' as DICTTYPE_ID, '").append(dataValues[i]).append("' as DATA_VALUE, 'usual' as OPCODE, '")
				   .append(dataNames[i]).append("' as DATA_NAME from ")
				   .append("dual where not exists (select * from TD_SM_TAXCODE_ORGANIZATION where ORG_ID='")
				   .append(orgId).append("' and DICTTYPE_ID='").append(dicttypeId)
				   .append("' and DATA_VALUE='" ).append(dataValues[i])
				   .append("' and OPCODE='usual' ")
				   .append(")) ");
				addDB.addBatch(addNewData.toString());
				addNewData.setLength(0);
				if(i==900){
					addDB.executeBatch();
				}
			}

			addDB.executeBatch();
			tm.commit();			
            //发事件
			Event event = new EventImpl(dicttypeId,DictionaryChangeEvent.DICTIONARY_USERORG_CHANGE_READ);
			super.change(event,true);

		}catch(Exception e){
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	/**
	 * 判断字典类型的数据,是否有
	 */
	public boolean getOrgisReadRes(String orgId, String dataId, String value){
		boolean state = false;
		//没选机构,返回所有的数据.
		if(this.strIsNull(orgId)) return true;
		DBUtil db = new DBUtil();
		int dicttype_type = this.getDicttype_type(dataId);
		//只判断 授权字典类型
		if(dicttype_type == PARTREAD_BUSINESS_DICTTYPE){
			try{
				StringBuffer sql = new StringBuffer();
				sql.append("select * from TD_SM_TAXCODE_ORGANIZATION where OPCODE in('read','usual') ")
				   .append("and ORG_ID='").append(orgId).append("' and DICTTYPE_ID='")
				   .append(dataId).append("' ")
				   .append("and DATA_VALUE='").append(value).append("'");
				db.executeSelect(sql.toString());
				if(db.size()>0){
					state = true;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{//其他字典类型不做判断,显示所有数据
			return true;
		}
		return state;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getDictDataOpcodeOrgs(java.lang.String, java.lang.String, java.lang.String)
	 * 根据字典类型ID,数据项ID和操作码,获取符合条件的机构列表, 或者创建改数据项的机构. 
	 * 
	 */
	public List getDictDataOpcodeOrgs( String dicttypeId, String dictdataValue,String opcode) {
		// TODO Auto-generated method stub
		List orgs = null;
		if(this.strIsNull(dicttypeId) || this.strIsNull(dictdataValue) ){
			return orgs;
		}
		StringBuffer sql = new StringBuffer()
			.append("select org_tax.org_id,org_tax.dicttype_id,org_tax.data_value,org_tax.opcode ")
			.append("from td_sm_taxcode_organization org_tax  where ")
			.append("org_tax.dicttype_id='").append(dicttypeId).append("' and ")
			.append("org_tax.data_value='").append(dictdataValue).append("' and ")
			.append("org_tax.opcode='").append(opcode).append("'");
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql.toString());
			orgs = new ArrayList();
			for(int i=0;i<db.size();i++){				
				String orgId = db.getString(i,"org_id");
				orgId = orgId==null?"":orgId;
				orgs.add(orgId);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
			return null;
		}
		//前提是 指定了所属机构.
		Data dicttype = null;
		try {
			dicttype = this.getDicttypeById(dicttypeId);
			if(dicttype != null){
				String ownOrg = dicttype.getData_create_orgid_field();
				if(!this.strIsNull(ownOrg)){
					DBUtil selfDB = new DBUtil();

					String dbName = dicttype.getDataDBName();
					String tableName = dicttype.getDataTableName();
					String valueField = dicttype.getDataValueField();
					StringBuffer self_sql = new StringBuffer()
					.append("select to_char(").append(ownOrg).append(") as ").append(ownOrg)
					.append(" from ").append(tableName).append(" where ").append(valueField)
					.append("='").append(dictdataValue).append("' ");
					try {
						selfDB.executeSelect(dbName,self_sql.toString());
						if(selfDB.size()>0 && selfDB.getString(0,ownOrg)!=null){//是本机构建立的,可见
							if(orgs == null){
								orgs = new ArrayList();								
							}
							orgs.add(selfDB.getString(0,ownOrg));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}
				}
			}
		} catch (ManagerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return orgs;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getDictDataOpcodeOrgs(java.lang.String, java.lang.String, java.lang.String)
	 * 根据字典类型ID,数据项ID和操作码,获取符合条件的机构列表, 
	 * 不包括本机构 创建数据项的机构. 
	 * 
	 */
	public List getExpSelfDictDataOpcodeOrgs( String dicttypeId, String dictdataValue,String opcode) {
		// TODO Auto-generated method stub
		List orgs = null;
		if(this.strIsNull(dicttypeId) || this.strIsNull(dictdataValue) ){
			return orgs;
		}
		StringBuffer sql = new StringBuffer()
			.append("select org_tax.org_id,org_tax.dicttype_id,org_tax.data_value,org_tax.opcode ")
			.append("from td_sm_taxcode_organization org_tax  where ")
			.append("org_tax.dicttype_id='").append(dicttypeId).append("' and ")
			.append("org_tax.data_value='").append(dictdataValue).append("' and ")
			.append("org_tax.opcode='").append(opcode).append("'");
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql.toString());
			orgs = new ArrayList();
			for(int i=0;i<db.size();i++){				
				String orgId = db.getString(i,"org_id");
				orgId = orgId==null?"":orgId;
				orgs.add(orgId);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
			return null;
		}
//		//前提是 指定了所属机构.
//		Data dicttype = null;
//		try {
//			dicttype = this.getDicttypeById(dicttypeId);
//			if(dicttype != null){
//				String ownOrg = dicttype.getData_create_orgid_field();
//				if(!this.strIsNull(ownOrg)){
//					DBUtil selfDB = new DBUtil();
//					
//					String dbName = dicttype.getDataDBName();
//					String tableName = dicttype.getDataTableName();
//					String valueField = dicttype.getDataValueField();
//					StringBuffer self_sql = new StringBuffer()
//					.append("select to_char(").append(ownOrg).append(") as ").append(ownOrg)
//					.append(" from ").append(tableName).append(" where ").append(valueField)
//					.append("='").append(dictdataValue).append("' ");
//					try {
//						selfDB.executeSelect(dbName,self_sql.toString());
//						if(selfDB.size()>0 && selfDB.getString(0,ownOrg)!=null){//是本机构建立的,可见
//							if(orgs == null){
//								orgs = new ArrayList();								
//							}
//							orgs.add(selfDB.getString(0,ownOrg));
//						}
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						return null;
//					}
//				}
//			}
//		} catch (ManagerException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		return orgs;
	}
    
	/**
	 * 根据字典类型ID,获取字典类型的种类
	 */
	public int getDicttype_type(String dicttypeId){
		int type = 0;
		String sql = "select DICTTYPE_TYPE from TD_SM_DICTTYPE where DICTTYPE_ID='"+dicttypeId+"'";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			type = db.getInt(0,"DICTTYPE_TYPE");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return type;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getPowerDictdataList(java.lang.String, java.lang.String, java.lang.String)
	 * 获取有权限的字典列表
	 */
	public List getPowerDictdataList(String dicttypeid, String orgId, String opcode) throws ManagerException {
		StringBuffer sql = new StringBuffer()
			.append("select DATA_VALUE from TD_SM_TAXCODE_ORGANIZATION where DICTTYPE_ID='")
			.append(dicttypeid).append("' and ORG_ID='").append(orgId).append("' and OPCODE='")
			.append(opcode).append("' ");
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql.toString());
			DataManager dm = new DictDataProvide();
			for(int i=0;i<db.size();i++){
				String dataValue = db.getString(i,"DATA_VALUE");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String[] getOrgDictdataValue(String orgId, String dicttypeid){
		String[] values = null;
		DBUtil db = new DBUtil();
		String sql = "select distinct t.data_value from td_sm_taxcode_organization t where t.org_id='"+orgId+"' and t.dicttype_id='"+dicttypeid+"'";
		try {
			db.executeSelect(sql);
			values = new String[db.size()];
			for(int i = 0; i < db.size(); i++){
				values[i] = db.getString(i, "data_value");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return values;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#deletedictdatas(java.lang.String, java.lang.String[])
	 * 删除多个字典数据项
	 * String[] dictdataInfos<dictdataValue:dictdataName>
	 */
	public boolean deletedictdatas(String dicttypeId, String[] dictdataInfos) throws ManagerException {
		boolean state = false;
		
		for(int i=0;i<dictdataInfos.length;i++){
			if(dictdataInfos[i]==null || dictdataInfos[i].trim().length()==0){
				continue;
			}
			String[] info = null;
			try {
				info = java.net.URLDecoder.decode(dictdataInfos[i],"UTF-8").split(":");
			} catch (UnsupportedEncodingException e) {
				throw new ManagerException(e);
			}
			if(info.length>0){
				String dictdataValue = "";
				String dictdataName = "";
				String primaryCondition = "";
				if(info.length==1){
					try {
						dictdataValue = java.net.URLDecoder.decode(info[0],"UTF-8");
					} catch (UnsupportedEncodingException e) {
						throw new ManagerException(e);
					}
				}else if(info.length==2){
					
					try {
						dictdataValue = java.net.URLDecoder.decode(info[0],"UTF-8");
						dictdataName = java.net.URLDecoder.decode(info[1],"UTF-8");
					} catch (UnsupportedEncodingException e) {
						throw new ManagerException(e);
					}

				}else if(info.length==3){
					try {
						dictdataValue = java.net.URLDecoder.decode(info[0],"UTF-8");
						dictdataName = java.net.URLDecoder.decode(info[1],"UTF-8");
					} catch (UnsupportedEncodingException e) {
						throw new ManagerException(e);
					}
					primaryCondition = info[2];
				} 
				primaryCondition = primaryCondition.replace("a.", "");
				if(!"".equals(dictdataValue) && !"".equals(dictdataName)){
					state = this.deletedictdata(dicttypeId,dictdataValue,dictdataName,primaryCondition);
				}
				else if(!"".equals(dictdataName))
				{
					state = this.deletedictdataByName(dicttypeId,dictdataName,primaryCondition);
				}
				else if(!"".equals(dictdataValue) )
				{
					state = this.deletedictdataByValue(dicttypeId,dictdataValue,primaryCondition);
				}else{
					state = this.deletedictdata(dicttypeId,dictdataValue,dictdataName,primaryCondition);
				}
			}
		}
		return state;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getDictdataAttachFieldList(java.lang.String, int, long)
	 * 获取字典类型的附加(高级)字段列表 翻页
	 */
	public ListInfo getDictdataAttachFieldList(String dicttypeId, int offset, long maxPagesize) {
		ListInfo listInfo = new ListInfo();
		List list = new ArrayList();
		if(this.strIsNull(dicttypeId)) return null;
		DBUtil dbUtil = new DBUtil();
		StringBuffer sql = new StringBuffer();	
		sql.append("select t.DICTTYPE_ID,t.FIELD_NAME,t.LABEL,t.INPUT_TYPE_ID || '' as INPUT_TYPE_ID,t.TABLE_COLUMN, ")
		   .append("t.ISNULLABLE,b.SCRIPT,t.ISUNIQUE ,b.INPUT_TYPE_NAME ")
		   .append("from TD_SM_DICATTACHFIELD t,TB_SM_INPUTTYPE b where t.INPUT_TYPE_ID=b.INPUT_TYPE_ID ")
		   .append("and t.DICTTYPE_ID='").append(dicttypeId).append("' order by t.sn");
		try {			
			dbUtil.executeSelect(sql.toString(),offset,(int)maxPagesize);
			for(int i= 0;i < dbUtil.size();i++){
				DictAttachField dictatt = new DictAttachField();
//				InputType inputType = new InputType();

				String fieldName = dbUtil.getString(i,"FIELD_NAME");
				dictatt.setDicttypeId(dbUtil.getString(i,"DICTTYPE_ID"));
				dictatt.setDictFieldName(dbUtil.getString(i,"LABEL"));
				dictatt.setDictField(fieldName);
				dictatt.setInputTypeId(dbUtil.getString(i,"INPUT_TYPE_ID"));
				dictatt.setTable_column(dbUtil.getString(i,"TABLE_COLUMN"));
				dictatt.setIsnullable(dbUtil.getInt(i,"ISNULLABLE"));
				dictatt.setIsunique(dbUtil.getInt(i,"ISUNIQUE"));
				//几种普通的输入类型,不需要写script,直接生成
				//如:text								
				dictatt.setInputTypeName(dbUtil.getString(i,"INPUT_TYPE_NAME"));
//				dictatt.setInputType(inputType);

				list.add(dictatt);				
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			e.printStackTrace();
		}
		return listInfo;	
	}

	private String getNullString(int nullable)
	{
		if(nullable == DictAttachField.ISNULLABLE)
			return "yes";
		else
		{
			return "no";
		}
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getDictdataAttachFieldList(java.lang.String, int, long)
	 * 获取字典类型的附加(高级)字段列表 不翻页
	 * 当count=-1时,取出全部的记录
	 */
	public List getDictdataAttachFieldList(String dicttypeId,int count) {
		List list = new ArrayList();
		if(this.strIsNull(dicttypeId)) return null;
		
		StringBuffer sql = new StringBuffer();	
		sql.append("select t.DICTTYPE_ID,t.FIELD_NAME,t.LABEL,t.INPUT_TYPE_ID as INPUT_TYPE_ID,t.TABLE_COLUMN, ")
		   .append("t.ISNULLABLE,t.ISUNIQUE, b.SCRIPT ,b.INPUT_TYPE_NAME, t.DATEFORMAT ")
		   .append("from TD_SM_DICATTACHFIELD t,TB_SM_INPUTTYPE b where t.INPUT_TYPE_ID=b.INPUT_TYPE_ID ")
		   .append("and t.DICTTYPE_ID=?");
		if(count != -1){
			sql.append(" and rownum<=?");
		}
		//高级字段排序
		sql.append(" order by t.SN");
		try {	
			Data dicttype = this.getDicttypeById(dicttypeId);
			String dbName = dicttype.getDataDBName();
			String tableName = dicttype.getDataTableName();
			PreparedDBUtil dbUtil = new PreparedDBUtil ();
			dbUtil.preparedSelect(sql.toString());
			dbUtil.setString(1, dicttypeId);
			if(count != -1){
				dbUtil.setInt(2, count);
			}
			
			dbUtil.executePrepared();
			for(int i= 0;i < dbUtil.size();i++){
				DictAttachField dictatt = new DictAttachField();
//				InputType inputType = new InputType();
				BaseInputTypeScript typeScript = null;
				dictatt.setTable_column(dbUtil.getString(i,"TABLE_COLUMN"));
				ColumnMetaData columnObj = DBUtil.getColumnMetaData(dbName,tableName,dictatt.getTable_column());
				//数据库字段的类型
				String typeName = columnObj.getTypeName();
				//字段在数据库中定义的长度
				int columnObjLength = columnObj.getColunmSize();

				String fieldName = dbUtil.getString(i,"FIELD_NAME");
				dictatt.setDicttypeId(dbUtil.getString(i,"DICTTYPE_ID"));
				dictatt.setDictFieldName(dbUtil.getString(i,"LABEL"));
				dictatt.setDictField(fieldName);
				dictatt.setInputTypeId(dbUtil.getString(i,"INPUT_TYPE_ID"));				
				dictatt.setIsnullable(dbUtil.getInt(i,"ISNULLABLE"));
				dictatt.setIsunique(dbUtil.getInt(i,"ISUNIQUE"));
				dictatt.setColumnTypeName(typeName);
				dictatt.setInputTypeName(dbUtil.getString(i,"INPUT_TYPE_NAME"));
				//域的值设置为空...
				dictatt.setFieldValue("");
				dictatt.setDateFormat(dbUtil.getString(i,"DATEFORMAT"));
				dictatt.setMaxLength(columnObjLength);

				//数据库字段在页面的校验类型
				String fieldValidType = getValidatorTypeByColumnMetaData(columnObj,getNullString(dictatt.getIsnullable()));

				//如果是日期类型，则需要指定日期格式---设置验证类型dictatt.setFieldValidType(fieldValidType);
//				System.out.println("dateformat = " + dbUtil.getString(i,"DATEFORMAT"));
				if(InputTypeManager.BASE_INPUTTYPE_CURRENT_DATE.equalsIgnoreCase(dictatt.getInputTypeName()) ||
						InputTypeManager.BASE_INPUTTYPE_DATE.equalsIgnoreCase(dictatt.getInputTypeName())){
					if(dbUtil.getString(i,"DATEFORMAT").equals("yyyy-MM-dd")){
//						还未添加。。。。
						if(columnObj.getNullable().equals("yes")){
							dictatt.setFieldValidType("dateNull");
						}else{
							dictatt.setFieldValidType("date");
						}
					}else{
						dictatt.setFieldValidType(fieldValidType);
					}
				}else{
					dictatt.setFieldValidType(fieldValidType);
				}

				//几种普通的输入类型,不需要写script,直接生成
				//如:text
				if("text".equalsIgnoreCase(dictatt.getInputTypeName()) 
						|| InputTypeManager.BASE_INPUTTYPE_TEXT.equalsIgnoreCase(dictatt.getInputTypeName())){	
					//选择文本
					typeScript = new TextTypeScript(dictatt);
//					inputType.setInputScript(typeScript.getEditExtendHtmlContent());
				}else if(InputTypeManager.BASE_INPUTTYPE_DICT.equalsIgnoreCase(dictatt.getInputTypeName())){
					//选择字典 类型 fieldName是 fieldName:dtypeId:opcode;
					//生成一个select框,
					typeScript = new DictTypeScript(dictatt);
					//inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_DATE.equalsIgnoreCase(dictatt.getInputTypeName())){
					//选择日期 
					typeScript = new DateTypeScript(dictatt);
					//inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_PK.equalsIgnoreCase(dictatt.getInputTypeName())){
					//选择主键 
					typeScript = new PrimaryKeyTypeScript(dictatt);
					//inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_ORG.equalsIgnoreCase(dictatt.getInputTypeName())){
					//选择机构
					typeScript = new InputTypeScriptImpl(dictatt);
					//inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_USER.equalsIgnoreCase(dictatt.getInputTypeName())){
					//选择用户
					typeScript = new InputTypeScriptImpl(dictatt);
					//inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_CURRENT_DATE.equalsIgnoreCase(dictatt.getInputTypeName())){
					//当前时间
					typeScript = new CurrentTimeScript(dictatt);
					//inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_CURRENT_USER.equalsIgnoreCase(dictatt.getInputTypeName())){
					//当前用户
					typeScript = new CurrentUserScript(dictatt);
					//inputType.setInputScript(typeScript.getContextExtendHtml(dictatt,this.request,this.response));
				}else if(InputTypeManager.BASE_INPUTTYPE_CURRENT_ORG.equalsIgnoreCase(dictatt.getInputTypeName())){
					//当前机构
					typeScript = new CurrentOrgScript(dictatt);
					//inputType.setInputScript(typeScript.getContextExtendHtml(dictatt,this.request,this.response));
				}else{
					typeScript = new OtherInputTypeScript(dictatt,dbUtil.getString(i,"SCRIPT"));
					//inputType.setInputScript(dbUtil.getString(i,"SCRIPT"),fieldName);
				}
//				dictatt.setInputType(inputType);
				dictatt.setInputTypeScript(typeScript);
				list.add(dictatt);				
			}

		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			e.printStackTrace();
		}
		return list;	
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getAllDictdataAttachFieldList(java.lang.String)
	 * 获取字典类型的所有附加(高级)字段列表,生成更新页面的时候
	 * 把区对应的值设置到input的字段中去.
	 * 根据字典的名称和值字段.update数据
	 */
	public List getAllDictdataAttachFieldList(String dicttypeId,String nameKey,String valueKey) {
		List list = new ArrayList();
		if(this.strIsNull(dicttypeId)) return null;
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		StringBuffer sql = new StringBuffer();	
		sql.append("select t.DICTTYPE_ID,t.FIELD_NAME,t.LABEL,to_char(t.INPUT_TYPE_ID) as INPUT_TYPE_ID,t.TABLE_COLUMN, ")
		   .append("t.ISNULLABLE,t.ISUNIQUE ,b.SCRIPT ,b.INPUT_TYPE_NAME,t.dateformat ")
		   .append("from TD_SM_DICATTACHFIELD t,TB_SM_INPUTTYPE b where t.INPUT_TYPE_ID=b.INPUT_TYPE_ID ")
		   .append("and t.DICTTYPE_ID=?");

		try {	
			Data dicttype = this.getDicttypeById(dicttypeId);
			String dbName = dicttype.getDataDBName();
			String tableName = dicttype.getDataTableName();

			dbUtil.preparedSelect(sql.toString());
			dbUtil.setString(1, dicttypeId);
			dbUtil.executePrepared();
			BaseInputTypeScript typeScript = null;
			for(int i= 0;i < dbUtil.size();i++){
				StringBuffer fileValueSql = new StringBuffer();
				DictAttachField dictatt = new DictAttachField();
				//InputType inputType = new InputType();

				dictatt.setTable_column(dbUtil.getString(i,"TABLE_COLUMN"));
				ColumnMetaData columnObj = DBUtil.getColumnMetaData(dbName,tableName,dictatt.getTable_column());
				//数据库字段的类型
				String typeName = columnObj.getTypeName();
				//字段在数据库中定义的长度
				int columnObjLength = columnObj.getColunmSize();

				String fieldName = dbUtil.getString(i,"FIELD_NAME");
				dictatt.setDicttypeId(dbUtil.getString(i,"DICTTYPE_ID"));
				dictatt.setDictFieldName(dbUtil.getString(i,"LABEL"));
				dictatt.setDictField(fieldName);
				dictatt.setInputTypeId(dbUtil.getString(i,"INPUT_TYPE_ID"));				
				dictatt.setIsnullable(dbUtil.getInt(i,"ISNULLABLE"));
				dictatt.setIsunique(dbUtil.getInt(i,"ISUNIQUE"));
				dictatt.setColumnTypeName(typeName);
				dictatt.setMaxLength(columnObjLength);
				dictatt.setInputTypeName(dbUtil.getString(i,"INPUT_TYPE_NAME"));
				dictatt.setDateFormat(dbUtil.getString(i,"dateformat"));

				//数据库字段在页面的校验类型
				String fieldValidType = getValidatorTypeByColumnMetaData(columnObj,getNullString(dictatt.getIsnullable()));

				//如果是日期类型，则需要指定日期格式---dictatt.setFieldValidType(fieldValidType)设置验证类型
				if(InputTypeManager.BASE_INPUTTYPE_CURRENT_DATE.equalsIgnoreCase(dictatt.getInputTypeName()) ||
						InputTypeManager.BASE_INPUTTYPE_DATE.equalsIgnoreCase(dictatt.getInputTypeName())){
					if(dbUtil.getString(i,"DATEFORMAT").equals("yyyy-MM-dd")){
						//还未添加。。。。
						if(columnObj.getNullable().equals("yes")){
							dictatt.setFieldValidType("dateNull");
						}else{
							dictatt.setFieldValidType("date");
						}
					}else{
						dictatt.setFieldValidType(fieldValidType);
					}

				}else{
					dictatt.setFieldValidType(fieldValidType);
				}
//				String[] filedNames = fieldName.split(":");

				//获取域的值 开始---------------------------------------------------
				String filedValue = "";
				//域对应值的类型 varchar date number typeName
//				String filedType = typeName;

				if("date".equalsIgnoreCase(typeName)){
					if(dbUtil.getString(i,"DATEFORMAT").equals("yyyy-MM-dd")){
						fileValueSql.append("select to_char(").append(dictatt.getTable_column()).append(",'yyyy-mm-dd') ")
						.append(" as ").append(dictatt.getTable_column());
					}else{
						fileValueSql.append("select to_char(").append(dictatt.getTable_column()).append(",'yyyy-mm-dd hh24:mi:ss') ")
							.append(" as ").append(dictatt.getTable_column());
					}
				}else{
					fileValueSql.append("select to_char(").append(dictatt.getTable_column()).append(") ")
					.append(" as ").append(dictatt.getTable_column());
				}

				fileValueSql.append(" from ").append(tableName)
				    .append(" a where to_char(").append(dicttype.getDataNameField()).append(")='")
				    .append(nameKey).append("' and to_char(").append(dicttype.getDataValueField())
				    .append(")='").append(valueKey).append("' ");

				DBUtil tmpDB = new DBUtil();

				tmpDB.executeSelect(fileValueSql.toString());

				if(tmpDB.size()>0){					
					filedValue = tmpDB.getString(0,dictatt.getTable_column());					
				}
				//System.out.println("field_value--------------------------"+filedValue);
				dictatt.setFieldValue(filedValue);
				//处理域的值结束--------------------------------------------------

				//几种普通的输入类型,不需要写script,直接生成
				//如:text
				if("text".equalsIgnoreCase(dictatt.getInputTypeName()) 
						|| InputTypeManager.BASE_INPUTTYPE_TEXT.equalsIgnoreCase(dictatt.getInputTypeName())){	
					//选择文本
					typeScript = new TextTypeScript(dictatt);
//					inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_DICT.equalsIgnoreCase(dictatt.getInputTypeName())){
					//选择字典 类型 fieldName是 fieldName:dtypeId:opcode;
					//生成一个select框,
					typeScript = new DictTypeScript(dictatt);
					//inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_DATE.equalsIgnoreCase(dictatt.getInputTypeName())){
					//选择日期 
					typeScript = new DateTypeScript(dictatt);

//					inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_PK.equalsIgnoreCase(dictatt.getInputTypeName())){
					//选择主键 
					typeScript = new PrimaryKeyTypeScript(dictatt);
					//inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_ORG.equalsIgnoreCase(dictatt.getInputTypeName())){
					//选择机构
					typeScript = new InputTypeScriptImpl(dictatt);
					//inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_USER.equalsIgnoreCase(dictatt.getInputTypeName())){
					//选择用户
					typeScript = new InputTypeScriptImpl(dictatt);
					//inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_CURRENT_DATE.equalsIgnoreCase(dictatt.getInputTypeName())){
					//当前时间
					typeScript = new CurrentTimeScript(dictatt);
					//inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_CURRENT_USER.equalsIgnoreCase(dictatt.getInputTypeName())){
					//当前用户
					typeScript = new CurrentUserScript(dictatt);
					//inputType.setInputScript(typeScript.getContextExtendHtml(dictatt,this.request,this.response));
				}else if(InputTypeManager.BASE_INPUTTYPE_CURRENT_ORG.equalsIgnoreCase(dictatt.getInputTypeName())){
					//当前机构
					typeScript = new CurrentOrgScript(dictatt);
					//inputType.setInputScript(typeScript.getContextExtendHtml(dictatt,this.request,this.response));
				}else{
					typeScript = new OtherInputTypeScript(dictatt,dbUtil.getString(i,"SCRIPT"));
					//inputType.setInputScript(dbUtil.getString(i,"SCRIPT"),dictatt.getTable_column());
				}
				//dictatt.setInputType(inputType);
				dictatt.setInputTypeScript(typeScript);
				list.add(dictatt);		
				fileValueSql.setLength(0);
			}

		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			e.printStackTrace();
		}
		return list;	
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getAllDictdataAttachFieldList(java.lang.String)
	 * 获取字典类型的所有附加(高级)字段列表,生成更新页面的时候
	 * 把区对应的值设置到input的字段中去.
	 * 根据字典的名称和值字段.update数据
	 */
	public List getAllDictdataAttachFieldList(String dicttypeId,String nameKey,String valueKey,String primaryCondition) {
		List list = new ArrayList();
		if(this.strIsNull(dicttypeId)) return null;
		DBUtil dbUtil = new DBUtil();
		StringBuffer sql = new StringBuffer();	
		sql.append("select t.DICTTYPE_ID,t.FIELD_NAME,t.LABEL,t.INPUT_TYPE_ID||'' as INPUT_TYPE_ID,t.TABLE_COLUMN, ")
		   .append("t.ISNULLABLE,t.ISUNIQUE ,b.SCRIPT ,b.INPUT_TYPE_NAME,t.dateformat ")
		   .append("from TD_SM_DICATTACHFIELD t,TB_SM_INPUTTYPE b where t.INPUT_TYPE_ID=b.INPUT_TYPE_ID ")
		   .append("and t.DICTTYPE_ID='").append(dicttypeId).append("' order by t.SN ");

		try {	
			Data dicttype = this.getDicttypeById(dicttypeId);
			String dbName = dicttype.getDataDBName();
			String tableName = dicttype.getDataTableName();

			dbUtil.executeSelect(sql.toString());
			BaseInputTypeScript typeScript = null;
			for(int i= 0;i < dbUtil.size();i++){
				StringBuffer fileValueSql = new StringBuffer();
				DictAttachField dictatt = new DictAttachField();
				//InputType inputType = new InputType();

				dictatt.setTable_column(dbUtil.getString(i,"TABLE_COLUMN"));
				ColumnMetaData columnObj = DBUtil.getColumnMetaData(dbName,tableName,dictatt.getTable_column());
				//数据库字段的类型
				String typeName = columnObj.getTypeName();
				//字段在数据库中定义的长度
				int columnObjLength = columnObj.getColunmSize();

				String fieldName = dbUtil.getString(i,"FIELD_NAME");
				dictatt.setDicttypeId(dbUtil.getString(i,"DICTTYPE_ID"));
				dictatt.setDictFieldName(dbUtil.getString(i,"LABEL"));
				dictatt.setDictField(fieldName);
				dictatt.setInputTypeId(dbUtil.getString(i,"INPUT_TYPE_ID"));				
				dictatt.setIsnullable(dbUtil.getInt(i,"ISNULLABLE"));
				dictatt.setIsunique(dbUtil.getInt(i,"ISUNIQUE"));
				dictatt.setColumnTypeName(typeName);
				dictatt.setMaxLength(columnObjLength);
				dictatt.setInputTypeName(dbUtil.getString(i,"INPUT_TYPE_NAME"));
				dictatt.setDateFormat(dbUtil.getString(i,"dateformat"));

				String fieldValidType = getValidatorTypeByColumnMetaData(columnObj,getNullString(dictatt.getIsnullable()));

				//如果是日期类型，则需要指定日期格式---dictatt.setFieldValidType(fieldValidType)设置验证类型
				if(InputTypeManager.BASE_INPUTTYPE_CURRENT_DATE.equalsIgnoreCase(dictatt.getInputTypeName()) ||
						InputTypeManager.BASE_INPUTTYPE_DATE.equalsIgnoreCase(dictatt.getInputTypeName())){
					if(dbUtil.getString(i,"DATEFORMAT").equals("yyyy-MM-dd")){
						//还未添加。。。。
						if(columnObj.getNullable().equals("yes")){
							dictatt.setFieldValidType("dateNull");
						}else{
							dictatt.setFieldValidType("date");
						}
					}else{
						dictatt.setFieldValidType(fieldValidType);
					}

				}else{
					dictatt.setFieldValidType(fieldValidType);
				}
//				String[] filedNames = fieldName.split(":");

				//获取域的值 开始---------------------------------------------------
				String filedValue = "";
				//域对应值的类型 varchar date number typeName
//				String filedType = typeName;

				if("date".equalsIgnoreCase(typeName)){
					if(dbUtil.getString(i,"DATEFORMAT").equals("yyyy-MM-dd")){
						fileValueSql.append("select to_char(").append(dictatt.getTable_column()).append(",'yyyy-mm-dd') ")
						.append(" as ").append(dictatt.getTable_column());
					}else{
						fileValueSql.append("select to_char(").append(dictatt.getTable_column()).append(",'yyyy-mm-dd hh24:mi:ss') ")
							.append(" as ").append(dictatt.getTable_column());
					}
				}else{
					fileValueSql.append("select ").append(dictatt.getTable_column()).append("||'' ")
					.append(" as ").append(dictatt.getTable_column());
				}

				fileValueSql.append(" from ").append(tableName)
				    .append(" a where ");
				if(nameKey==null || "".equals(nameKey)){
					fileValueSql.append(" ((").append(dicttype.getDataNameField()).append("||'') is null ")
					.append(" or (").append(dicttype.getDataNameField()).append("||'') = '")
					.append(nameKey).append("') ");
				}else{
					fileValueSql.append("(").append(dicttype.getDataNameField()).append("||'')='")
				    	.append(nameKey).append("' ");
				}
				if(valueKey==null || "".equals(valueKey)){
					fileValueSql.append(" and ((").append(dicttype.getDataValueField()).append(" || '') is null ")
					.append(" or (").append(dicttype.getDataValueField()).append("||'') = '")
					.append(valueKey).append("') ");
				}else{
					fileValueSql.append(" and (").append(dicttype.getDataValueField()).append("||'')='")
				    	.append(valueKey).append("' ");
				}
//				fileValueSql.append("to_char(").append(dicttype.getDataNameField()).append(")='")
//				    .append(nameKey).append("' and to_char(").append(dicttype.getDataValueField())
//				    .append(")='").append(valueKey).append("' ");
				//主键信息
				fileValueSql.append(primaryCondition);
				DBUtil tmpDB = new DBUtil();

				tmpDB.executeSelect(dbName,fileValueSql.toString());

				if(tmpDB.size()>0){					
					filedValue = tmpDB.getString(0,dictatt.getTable_column());					
				}
				//System.out.println("field_value--------------------------"+filedValue);
				dictatt.setFieldValue(filedValue);
				//处理域的值结束--------------------------------------------------

				//几种普通的输入类型,不需要写script,直接生成
				//如:text
				if("text".equalsIgnoreCase(dictatt.getInputTypeName()) 
						|| InputTypeManager.BASE_INPUTTYPE_TEXT.equalsIgnoreCase(dictatt.getInputTypeName())){	
					//选择文本
					typeScript = new TextTypeScript(dictatt);
//					inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_DICT.equalsIgnoreCase(dictatt.getInputTypeName())){
					//选择字典 类型 fieldName是 fieldName:dtypeId:opcode;
					//生成一个select框,
					typeScript = new DictTypeScript(dictatt);
					//inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_DATE.equalsIgnoreCase(dictatt.getInputTypeName())){
					//选择日期 
					typeScript = new DateTypeScript(dictatt);

//					inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_PK.equalsIgnoreCase(dictatt.getInputTypeName())){
					//选择主键 
					typeScript = new PrimaryKeyTypeScript(dictatt);
					//inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_ORG.equalsIgnoreCase(dictatt.getInputTypeName())){
					//选择机构
					typeScript = new InputTypeScriptImpl(dictatt);
					//inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_USER.equalsIgnoreCase(dictatt.getInputTypeName())){
					//选择用户
					typeScript = new InputTypeScriptImpl(dictatt);
					//inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_CURRENT_DATE.equalsIgnoreCase(dictatt.getInputTypeName())){
					//当前时间
					typeScript = new CurrentTimeScript(dictatt);
					//inputType.setInputScript(typeScript.getExtendHtmlContent(dictatt));
				}else if(InputTypeManager.BASE_INPUTTYPE_CURRENT_USER.equalsIgnoreCase(dictatt.getInputTypeName())){
					//当前用户
					typeScript = new CurrentUserScript(dictatt);
					//inputType.setInputScript(typeScript.getContextExtendHtml(dictatt,this.request,this.response));
				}else if(InputTypeManager.BASE_INPUTTYPE_CURRENT_ORG.equalsIgnoreCase(dictatt.getInputTypeName())){
					//当前机构
					typeScript = new CurrentOrgScript(dictatt);
					//inputType.setInputScript(typeScript.getContextExtendHtml(dictatt,this.request,this.response));
				}else{
					typeScript = new OtherInputTypeScript(dictatt,dbUtil.getString(i,"SCRIPT"));
					//inputType.setInputScript(dbUtil.getString(i,"SCRIPT"),dictatt.getTable_column());
				}
				//dictatt.setInputType(inputType);
				dictatt.setInputTypeScript(typeScript);
				list.add(dictatt);		
				fileValueSql.setLength(0);
			}

		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			e.printStackTrace();
		}
		return list;	
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#addDictdataAttachField(com.frameworkset.platform.dictionary.DictAttachField)
	 */
	public boolean storeDictdataAttachField(DictAttachField dictatt) {
		if(dictatt == null) return false;

		StringBuffer judge = new StringBuffer();
		DBUtil db = new DBUtil();		
		judge.append("select count(*) as num from TD_SM_DICATTACHFIELD where TABLE_COLUMN='")
			.append(dictatt.getTable_column()).append("' and DICTTYPE_ID ='").append(dictatt.getDicttypeId()).append("'");		
		try {
			db.executeSelect(judge.toString());
			if(db.size()>0 && db.getInt(0,"num")>1){//字段已经被使用了
				return false;
			}

			StringBuffer sql = new StringBuffer();
			sql.append("insert into TD_SM_DICATTACHFIELD(DICTTYPE_ID,FIELD_NAME,LABEL,")
				.append("INPUT_TYPE_ID,TABLE_COLUMN,ISNULLABLE,ISUNIQUE,dateformat)values(?,?,?,?,?,?,?,?)");
			PreparedDBUtil pd = new PreparedDBUtil();
			pd.preparedInsert(sql.toString());
			pd.setString(1,dictatt.getDicttypeId());
			pd.setString(2,dictatt.getDictField());
			pd.setString(3,dictatt.getDictFieldName());
			int inputTypeid = 0;
			inputTypeid = Integer.parseInt(dictatt.getInputTypeId());
			pd.setInt(4,inputTypeid);
			pd.setString(5,dictatt.getTable_column());
			pd.setInt(6,dictatt.getIsnullable());
			pd.setInt(7,dictatt.getIsunique());
			pd.setString(8,dictatt.getDateFormat());
			pd.executePrepared();
			sql.setLength(0);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}


	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#deleteDictdataAttachField(List<com.frameworkset.platform.dictionary.DictAttachField>)
	 */
	public int deleteDictdataAttachField(String dicttypeId, String[] tableColumns) {
		if(this.strIsNull(dicttypeId) || tableColumns==null ) return ALL_DELETE_FAILD;
		DBUtil db = new DBUtil();
		DBUtil judgeDB = new DBUtil();
		Data dicttype = null;
		try {
			dicttype = this.getDicttypeById(dicttypeId);
			String dbName = dicttype.getDataDBName();
			String tableName = dicttype.getDataTableName();
			int deleteFaildCount = 0;
			for(int i=0;i<tableColumns.length;i++){
				if(this.strIsNull(tableColumns[i])) continue;
				String dictattField = tableColumns[i];
				//判断字段是否有数据
//				String judge = "select count(*) as num from " + tableName + " where "+
//					dictattField + " is not null";
//				judgeDB.executeSelect(dbName,judge);
//				if(judgeDB.size()>0 && judgeDB.getInt(0,"num")>0){//有数据,删除失败计数,继续下次循环
//					deleteFaildCount++;
//					continue;
//				}
				StringBuffer sql = new StringBuffer();
				sql.append("delete  TD_SM_DICATTACHFIELD where DICTTYPE_ID='")
					.append(dicttypeId).append("' and TABLE_COLUMN='")
					.append(dictattField).append("' ");
				db.addBatch(sql.toString());
				sql.setLength(0);
			}
			db.executeBatch();
			if(deleteFaildCount == 0){
				return ALL_DELETE_SUCCESS;
			}else if(deleteFaildCount == tableColumns.length){
				return ALL_DELETE_FAILD;
			}else{
				return PART_DELETE_SUCCESS;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}finally{
			db.resetBatch();
		}
		return ALL_DELETE_FAILD;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#updateDictdataAttachField(com.frameworkset.platform.dictionary.DictAttachField)
	 * 修改扩展字段
	 */
	public boolean updateDictdataAttachField(DictAttachField dictatt) {
		if(dictatt == null) return false;
		PreparedDBUtil pd = new PreparedDBUtil();
		try {			

			StringBuffer sql = new StringBuffer();
			sql.append("update TD_SM_DICATTACHFIELD set FIELD_NAME=?,LABEL=?,")
				.append("INPUT_TYPE_ID=?,ISNULLABLE=?,ISUNIQUE=?,dateformat=? where TABLE_COLUMN=? and DICTTYPE_ID=? ");
			pd.preparedUpdate(sql.toString());

			pd.setString(1,dictatt.getDictField());
			pd.setString(2,dictatt.getDictFieldName());
			int inputTypeid = 0;
			try{
				inputTypeid = Integer.parseInt(dictatt.getInputTypeId());
			}catch(Exception e){
				inputTypeid = 0;
				e.printStackTrace();
			}
			pd.setInt(3,inputTypeid);
			pd.setInt(4,dictatt.getIsnullable());
			pd.setInt(5,dictatt.getIsunique());
			pd.setString(6,dictatt.getDateFormat());
			pd.setString(7,dictatt.getTable_column());
			pd.setString(8,dictatt.getDicttypeId());

			pd.executePrepared();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#columnUseStatue(com.frameworkset.dictionary.Data, java.lang.String, java.lang.String)
	 * 当前数据库字段的被字典类型的使用情况
	 */
	public int columnUseStatue(Data dicttype, String columnName, String selectedValue) {
		if(dicttype==null) return COLUMN_AVIALABLE;
		InputTypeManager inputImpl = new InputTypeManagerImpl();
		if(inputImpl.isColumnUsedByDictType(dicttype.getDataId(),columnName)){//还有, 附加字段使用的字段也要过滤
			return COLUMN_USED;
		}
		if(columnName.equalsIgnoreCase(dicttype.getData_create_orgid_field()) ||
		   columnName.equalsIgnoreCase(dicttype.getData_validate_field()) ||
		   columnName.equalsIgnoreCase(dicttype.getDataNameField()) ||
		   columnName.equalsIgnoreCase(dicttype.getDataOrderField()) ||
		   columnName.equalsIgnoreCase(dicttype.getDataParentIdFild()) ||
		   columnName.equalsIgnoreCase(dicttype.getDataTypeIdField()) ||
		   columnName.equalsIgnoreCase(dicttype.getDataValueField())						   
		)
		{//该数据库字段被其他基础属性使用了			
			return COLUMN_USED;
		}else if(columnName.equalsIgnoreCase(selectedValue)){//该数据库字段被被当前属性选中了
			return COLUMN_SELECTED;
		}else{//可供选择的
			return COLUMN_AVIALABLE;
		}

	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#columnUseStatue(com.frameworkset.dictionary.Data, java.lang.String, java.lang.String)
	 * 当前数据库字段的被字典类型的高级字段使用情况
	 */
	public int advanceColumnUseStatue(Data dicttype, String columnName, String selectedValue) {
		if(dicttype==null) return COLUMN_AVIALABLE;
		InputTypeManager inputImpl = new InputTypeManagerImpl();
		if(inputImpl.isColumnUsedByDictType(dicttype.getDataId(),columnName)){//还有, 附加字段使用的字段也要过滤
			return COLUMN_ADVANCE_USED;
		}
		if(columnName.equalsIgnoreCase(dicttype.getData_create_orgid_field()) ||
		   columnName.equalsIgnoreCase(dicttype.getData_validate_field()) ||
		   columnName.equalsIgnoreCase(dicttype.getDataNameField()) ||
		   columnName.equalsIgnoreCase(dicttype.getDataOrderField()) ||
		   columnName.equalsIgnoreCase(dicttype.getDataParentIdFild()) ||
		   columnName.equalsIgnoreCase(dicttype.getDataTypeIdField()) ||
		   columnName.equalsIgnoreCase(dicttype.getDataValueField())						   
		)
		{//该数据库字段被其他基础属性使用了			
			return COLUMN_USED;
		}
		else if(columnName.equalsIgnoreCase(selectedValue)){//该数据库字段被被当前属性选中了
			return COLUMN_SELECTED;
		}else{//可供选择的
			return COLUMN_AVIALABLE;
		}		
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getDictAttachFieldInfo(java.lang.String, java.lang.String)
	 * 根据类型ID和数据库表字段,获取字典附加字段对象信息
	 */
	public DictAttachField getDictAttachFieldInfo(String dicttypeId, String tableColumn) {
		DictAttachField dictatt = null;
		if(this.strIsNull(dicttypeId) || this.strIsNull(tableColumn)) return dictatt;
		DBUtil dbUtil = new DBUtil();
		StringBuffer sql = new StringBuffer();	
		sql.append("select t.DICTTYPE_ID,t.FIELD_NAME,t.LABEL,t.INPUT_TYPE_ID||''  as INPUT_TYPE_ID,t.TABLE_COLUMN,t.ISNULLABLE ")
		   .append(", b.INPUT_TYPE_NAME, t.ISUNIQUE ")
		   .append("from TD_SM_DICATTACHFIELD t,TB_SM_INPUTTYPE b where t.INPUT_TYPE_ID=b.INPUT_TYPE_ID and DICTTYPE_ID='").append(dicttypeId).append("' and TABLE_COLUMN='")
		   .append(tableColumn).append("' ");
		try {	
			dbUtil.executeSelect(sql.toString());
			if(dbUtil.size()>0){
				dictatt = new DictAttachField();
				DBUtil judgeDB = new DBUtil();
				Data dicttype = this.getDicttypeById(dicttypeId);
				String dbName = dicttype.getDataDBName();
				String tableName = dicttype.getDataTableName();
//				判断字段是否有数据
				String judge = "select count(*) as num from " + tableName + " where "+
					tableColumn + " is not null";
				judgeDB.executeSelect(dbName,judge);
				if(judgeDB.size()>0 && judgeDB.getInt(0,"num")>0){//有数据
					dictatt.setUsed(true);
				}else{
					dictatt.setUsed(false);
				}
				dictatt.setDictField(dbUtil.getString(0,"DICTTYPE_ID"));
				dictatt.setDictFieldName(dbUtil.getString(0,"LABEL"));
				dictatt.setDictField(dbUtil.getString(0,"FIELD_NAME"));
				dictatt.setInputTypeId(dbUtil.getString(0,"INPUT_TYPE_ID"));
				dictatt.setTable_column(dbUtil.getString(0,"TABLE_COLUMN"));
				dictatt.setIsnullable(dbUtil.getInt(0,"ISNULLABLE"));
				dictatt.setInputTypeName(dbUtil.getString(0,"INPUT_TYPE_NAME"));
				dictatt.setIsunique(dbUtil.getInt(0,"ISUNIQUE"));
				return dictatt;				
			}
		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#addAdvanceDictdata(com.frameworkset.dictionary.Item, javax.servlet.http.HttpServletRequest)
	 * 字典数据的高级添加,包含高级(附件)字段
	 */
	public boolean addAdvanceDictdata(Item dictdata, HttpServletRequest request) throws ManagerException {
		boolean r = false;
		if(dictdata == null || request == null ) return r;
		int maxNo = 0;		
		DBUtil db = new DBUtil();
		DBUtil pd = new DBUtil();
		//字典类型ID
		String dicttypeId = dictdata.getDataId();
		if(this.strIsNull(dicttypeId)) return false;
		//根据字典类型ID,获取字典类型对象
		Data dicttype = new Data();
		dicttype = getDicttypeById(dicttypeId);	
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org_field = dicttype.getData_create_orgid_field();
		int KEY_GENERAL_TYPE = dicttype.getKey_general_type();
		int is_tree = dicttype.getIsTree();

		//判断数据是否有重复
		StringBuffer isRepeat_sql = new StringBuffer();
		isRepeat_sql.append("select count(1) as num from ").append(data_table_name).append(" where (")
			.append(data_name_filed).append("='").append(dictdata.getName()).append("' or ")
			.append(data_value_field).append("='").append(dictdata.getValue()).append("' ) ");
		if(!strIsNull(data_typeid_field) ){
			isRepeat_sql.append(" and ").append(data_typeid_field).append("='").append(dicttypeId).append("'");
		}else{
			if(DEFAULT_DATA_TABLENAME.equalsIgnoreCase(data_typeid_field)){
				isRepeat_sql.append(" and ").append(DEFAULT_DATA_TYPEIDFIELD)
				.append("='").append(dicttypeId).append("'");
			}
		}
		try{
//			db.executeSelect(isRepeat_sql.toString());
//			if(db.size()>0){
//				int count = db.getInt(0,"num");
//				if(count>0){//已有重复记录 可能是值相同,或者是名称相同
//					return false;
//				}
//			}			

			//处理基础字段			
			StringBuffer insert_sqlstr = new StringBuffer();			
			insert_sqlstr.append("insert into ").append(data_table_name).append("(");
			if(!this.strIsNull(data_name_filed)){//插入名称字段
				insert_sqlstr.append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){//插入值字段
			    if(insert_sqlstr.toString().endsWith("(") ){
			    	insert_sqlstr.append(data_value_field);
			    }else{
			    	insert_sqlstr.append(",").append(data_value_field);
			    }
			}
			if(DICTDATA_IS_TREE==is_tree){//插入 父类ID字段
				String data_parentId = dicttype.getDataParentIdFild();
				if(insert_sqlstr.toString().endsWith("(")){
			    	insert_sqlstr.append(data_parentId);
			    }else{
				    insert_sqlstr.append(",").append(data_parentId);
			    }
			}
			if(!this.strIsNull(data_order_field)){//插入排序字段
				if(insert_sqlstr.toString().endsWith("(")){
			    	insert_sqlstr.append(data_order_field);
			    }else{
				    insert_sqlstr.append(", ").append(data_order_field);
			    }
			}
			if(!this.strIsNull(data_typeid_field)){//插入类型ID字段
				if(insert_sqlstr.toString().endsWith("(")){
			    	insert_sqlstr.append(data_typeid_field);
			    }else{
				    insert_sqlstr.append(", ").append(data_typeid_field);
			    }
			}
			if(!this.strIsNull(data_validate_field)){//插入是否失效字段
				if(insert_sqlstr.toString().endsWith("(")){
			    	insert_sqlstr.append(data_validate_field);
			    }else{
				    insert_sqlstr.append(", ").append(data_validate_field);
			    }
			}
			if(!this.strIsNull(data_org_field)){//插入所属机构字段
				if(insert_sqlstr.toString().endsWith("(")){
			    	insert_sqlstr.append(data_org_field);
			    }else{
				    insert_sqlstr.append(", ").append(data_org_field);
			    }
			}
			//--结束基础字段处理

			//处理附加字段
			//得到这个类型的附加字段列表
			List dictatts = this.getDictdataAttachFieldList(dicttypeId,-1);
			List values = new ArrayList();
			for(int i=0;dictatts!=null && i<dictatts.size();i++){
				DictAttachField dictatt = (DictAttachField)dictatts.get(i);
//				System.out.println("附加字段 ： " + dictatt.getTable_column().toLowerCase());
//				String test = dictatt.getTable_column().toLowerCase();
				//获取request里面的字段
				String requestValue = request.getParameter(dictatt.getTable_column().toLowerCase())==null?"":request.getParameter(dictatt.getTable_column().toLowerCase());
//				
//				System.out.println("值 ： " + request.getParameter(test));

				//对应的数据库字段
				String tableColumn = dictatt.getTable_column();
				//保存的值是  value:type
				values.add(requestValue);
				if(insert_sqlstr.toString().endsWith("(")){
			    	insert_sqlstr.append(tableColumn);
			    }else{
			    	insert_sqlstr.append(",").append(tableColumn);	
			    }
			}
			//--结束附加字段处理

			String itemid = "";
			//设置主键
			if(KEY_GENERAL_TYPE == 1) // 主键自动生成
			{
				if(data_table_name.trim().equalsIgnoreCase("TD_SM_DICTDATA") )
				{
					insert_sqlstr.append(",DICTDATA_ID");
					itemid = DBUtil.getNextStringPrimaryKey("TD_SM_DICTDATA");
				}
				else
				{
					itemid = dictdata.getValue();
				}

			}
			else //手动生成主键
			{
				PrimaryKey primaryKey = PrimaryKeyCacheManager.getInstance()
				.getPrimaryKeyCache(data_dbName).getIDTable(
						data_table_name);
				if(primaryKey != null) //如果有主键
				{
					if(primaryKey.getPrimaryKeyName().trim().equalsIgnoreCase(data_value_field)) //如果主键已经包含在sql语句中
					{
						itemid = dictdata.getValue();
					}
					else //如果没有包含则需要生成主键值,并且添加到sql语句中
					{
						itemid = String.valueOf(primaryKey.generateObjectKey().getPrimaryKey());
						insert_sqlstr.append(",").append(primaryKey.getPrimaryKeyName());
					}
				}
				else
				{
					itemid = dictdata.getValue();
				}

			}
			insert_sqlstr.append(")values(");			

			if(!this.strIsNull(data_name_filed)){//插入名称字段**值				
				insert_sqlstr.append(" '").append(dictdata.getName()).append("' ");

			}
			if(!this.strIsNull(data_value_field)){//插入值字段**值
				if(insert_sqlstr.toString().endsWith("(")){
					insert_sqlstr.append(" '").append(dictdata.getValue()).append("' ");
			    }else{
				    insert_sqlstr.append(", '").append(dictdata.getValue()).append("' ");
			    }
			}	
			if(DICTDATA_IS_TREE==is_tree){//插入 父类ID字段**值
				if(insert_sqlstr.toString().endsWith("(")){
					insert_sqlstr.append(" '").append(dictdata.getParentId()).append("' ");
			    }else{
			    	insert_sqlstr.append(", '").append(dictdata.getParentId()).append("' ");
			    }
			}
			if(!this.strIsNull(data_order_field)){//插入排序字段**值
				//根据字典类型ID,获取当该典数据的最大排序号
				String maxOrderNo = "select max("+ data_order_field +") as orderno from  " + data_table_name ;
				if(!this.strIsNull(data_typeid_field)){//插入类型ID字段
					maxOrderNo += " where "+ data_typeid_field +" = "  + dicttypeId;
				}				                    
				db.executeSelect(dicttype.getDataDBName(),maxOrderNo);
				if(db.size()>0){
					maxNo = db.getInt(0,0) + 1;
				}
				if(insert_sqlstr.toString().endsWith("(")){
					insert_sqlstr.append(" '").append(maxNo).append("' ");
			    }else{
			    	insert_sqlstr.append(", '").append(maxNo).append("' ");
			    }
			}
			if(!this.strIsNull(data_typeid_field)){//插入类型ID字段**值
				if(insert_sqlstr.toString().endsWith("(")){
					insert_sqlstr.append(" '").append(dictdata.getDataId()).append("' ");
			    }else{
			    	insert_sqlstr.append(", '").append(dictdata.getDataId()).append("' ");
			    }
			}		
			if(!this.strIsNull(data_validate_field)){//插入是否失效字段的值 缺省是1 有效
				if(insert_sqlstr.toString().endsWith("(")){
					insert_sqlstr.append(" ").append(dictdata.getDataValidate());
			    }else{
			    	insert_sqlstr.append(", ").append(dictdata.getDataValidate());
			    }
			}
			if(!this.strIsNull(data_org_field)){//插入所属机构字段的值
				if(insert_sqlstr.toString().endsWith("(")){
					insert_sqlstr.append(" '").append(dictdata.getDataOrg()).append("' ");
			    }else{
			    	insert_sqlstr.append(", '").append(dictdata.getDataOrg()).append("' ");
			    }
			}
			//处理字段的值(考虑三种类型 varchar,date number)
			for(int i=0;values!=null && i<values.size();i++){

//				String[] infos = tmp.split("\\^");
				String value = String.valueOf(values.get(i));

//				String columnName = "";
				DictAttachField dictatt = (DictAttachField)dictatts.get(i);	
				String type = dictatt.getColumnTypeName();
//					columnName = infos[2]; 
					ColumnMetaData obj = DBUtil.getColumnMetaData(data_table_name, dictatt.getTable_column());
					if("date".equalsIgnoreCase(type)){//日期型的
						if(insert_sqlstr.toString().endsWith("(")){
							insert_sqlstr.append(" to_date('").append(value).append("','yyyy-mm-dd hh24:mi:ss') ");
					    }else{
					    	insert_sqlstr.append(",to_date('").append(value).append("','yyyy-mm-dd hh24:mi:ss') ");
					    }
					}else if("number".equalsIgnoreCase(type) || "long".equalsIgnoreCase(type)){//数字型的
						try {
							if(obj.getDECIMAL_DIGITS() > 0){
								double v = Double.parseDouble(value);
								if(insert_sqlstr.toString().endsWith("(")){
									insert_sqlstr.append(" ").append(v).append(" ");
							    }else{
							    	insert_sqlstr.append(",").append(v).append(" ");
							    }
							}else{
								int v = 0;
								v = Integer.parseInt(value);
								if(insert_sqlstr.toString().endsWith("(")){
									insert_sqlstr.append(" ").append(v).append(" ");
							    }else{
							    	insert_sqlstr.append(",").append(v).append(" ");
							    }
							}
						} catch (RuntimeException e) {
							if(insert_sqlstr.toString().endsWith("(")){
								insert_sqlstr.append(" 0");
						    }else{
						    	insert_sqlstr.append(",").append("0 ");
						    }
						}
					}else{//串型的 lob型的暂时没做处理
						if(insert_sqlstr.toString().endsWith("(")){
							insert_sqlstr.append(" '").append(value).append("' ");
					    }else{
					    	insert_sqlstr.append(",'").append(value).append("' ");
					    }
					}
				}

			if(KEY_GENERAL_TYPE == 1) // 主键自动生成
			{
				if(data_table_name.trim().equalsIgnoreCase("TD_SM_DICTDATA") )
				{
					insert_sqlstr.append(",'").append(itemid).append("'");
//					itemid = DBUtil.getNextStringPrimaryKey("TD_SM_DICTDATA");
				}
				else
				{
//					itemid = dictdata.getValue();
				}

			}
			else //手动生成主键
			{
				PrimaryKey primaryKey = PrimaryKeyCacheManager.getInstance()
				.getPrimaryKeyCache(data_dbName).getIDTable(
						data_table_name);
				if(primaryKey != null) //如果有主键
				{
					if(primaryKey.getPrimaryKeyName().trim().equalsIgnoreCase(data_value_field)) //如果主键已经包含在sql语句中
					{
//						itemid = dictdata.getValue();
					}
					else //如果没有包含则需要生成主键值,并且添加到sql语句中
					{
//						itemid = String.valueOf(primaryKey.generateObjectKey().getPrimaryKey());
//						insert_sqlstr.append(",").append(primaryKey.getPrimaryKeyName());
						insert_sqlstr.append(",'").append(itemid).append("'");
					}
				}

			}
			insert_sqlstr.append(")");
			//返回主键

//			Object ob = pd.executeInsert(data_dbName,insert_sqlstr.toString());
			pd.executeInsert(data_dbName,insert_sqlstr.toString());
			insert_sqlstr.setLength(0);
//			dictdata.setItemId(String.valueOf(ob));
			dictdata.setItemId(itemid);
			if(isCachable(dictdata.getDataId())){
				Event event = new EventImpl(dictdata, DictionaryChangeEvent.DICTIONARY_DATA_ADD);
				super.change(event,true);
			}
			r = true;
		}catch(Exception e){
			//throw new ManagerException(e.getMessage());
			e.printStackTrace();
		}		
		return r;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#updateAdvanceDictdata(com.frameworkset.dictionary.Item, javax.servlet.http.HttpServletRequest)
	 * 字典数据的高级(附件)字段的更新
	 */
	public boolean updateAdvanceDictdata(Item dictdata, HttpServletRequest request) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 更新字典排序
	 * @param dicttypeId
	 * @param docid
	 * @return
	 * @throws ManagerException
	 */
	public boolean updateDictArr(String dicttypeId, String[] docid) throws ManagerException {
		boolean state = false;
		DBUtil dbUtil = new DBUtil();
		boolean isCacha = isCachable(dicttypeId);
		Data data = this.getDicttypeById(dicttypeId);
		String DATA_TABLE_NAME = data.getDataTableName();
		String DATA_NAME_FILED = data.getDataNameField();
		String DATA_VALUE_FIELD = data.getDataValueField();
		String DATA_ORDER_FIELD = data.getDataOrderField();

		String order_sql = toString(docid,DATA_TABLE_NAME,DATA_VALUE_FIELD,DATA_NAME_FILED,DATA_ORDER_FIELD);

		DBUtil db_order = new DBUtil();
		int[] order_val = new int[docid.length];

		StringBuffer update_dict = new StringBuffer();
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
//			System.out.println("order_sql = " + order_sql);
			db_order.executeSelect(order_sql);
			if(db_order.size() == docid.length){
				for(int i = 0; i < order_val.length; i++){
					order_val[i] = db_order.getInt(i, DATA_ORDER_FIELD);
				}
				//Arrays.sort(order_val);
			}
			for(int i = 0; i < docid.length; i++){
				String[] doc = docid[i].split(":");
				String value = doc[0];
				String name = doc[1];
				update_dict.append("update ").append(DATA_TABLE_NAME).append(" a set ")
					.append(DATA_ORDER_FIELD).append("=").append(order_val[i]).append(" where ")
					.append(DATA_NAME_FILED);
				if(name == null || "".equals(name)){
					update_dict.append(" is null  and ").append(DATA_VALUE_FIELD);
				}else{
					update_dict.append("='").append(name).append("' and ").append(DATA_VALUE_FIELD);
				}
				if(value == null || "".equals(value)){
					update_dict.append(" is null ");
				}else{
					update_dict.append("='").append(value).append("' ");
				}

				//如果doc长度为3时，则DATA_NAME_FILED与DATA_VALUE_FIELD不能唯一确定一条记录；doc[2]为主键条件
				if(doc.length == 3){
					update_dict.append(doc[2]);
				}
				dbUtil.addBatch(update_dict.toString());
				update_dict.setLength(0);
			}
			dbUtil.executeBatch();
			//字典类型的 所有数据项
			List items = this.getChildDictdataListByTypeId(data.getDataId());
			data.setAllitems(items);
			data.setItems(items);
			tm.commit();
			if(isCacha){
				Event event = new EventImpl(data, DictionaryChangeEvent.DICTIONARY_DATA_ORDERCHANGE);
				super.change(event,true);
			}
			state = true;
		}catch (SQLException e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return state;
	}

	/**
	 * 
	 * @param checkboxIdVal 页面checkbox传入需要排序的值
	 * @param tableName 	数据库表
	 * @param valueField	对应的值字段
	 * @param nameField		对应的名称字段
	 * @param orderField	对应的排序字段
	 * @return
	 */
	private String toString(String[] checkboxIdVal, String tableName, 
			String valueField, String nameField, String orderField){
		StringBuffer s = new StringBuffer();
		//length值为3的时，说明"名称字段"和"值字段"没有配置主键
		if(checkboxIdVal[0].split(":").length == 3){
			s.append("select ").append(orderField).append(" from ")
				.append(tableName).append(" a where ");
			for(int i = 0; i < checkboxIdVal.length; i++){
				String[] splitStrs = checkboxIdVal[i].split(":"); 
				splitStrs[2] = splitStrs[2].replaceFirst("and", "");
				if(i == 0){
					s.append(" (").append(splitStrs[2]).append(") ");
				}else{
					s.append(" or (").append(splitStrs[2]).append(") ");
				}
			}
			s.append(" order by ").append(orderField);
		}else{
			s.append("select ").append(orderField).append(" from ")
				.append(tableName).append(" where ").append(valueField)
				.append("||':'||").append(nameField).append(" in (");
			for(int i = 0; i < checkboxIdVal.length; i++){
				s.append("'").append(checkboxIdVal[i]).append("'");
				if(i < checkboxIdVal.length - 1){
					s.append(",");
				}
			}
			s.append(") order by ").append(orderField);
		}
		return s.toString();
	}

	/**
	 * 更新树形字典列表排序
	 * @param dicttypeId
	 * @param dictdataValue
	 * @param docid
	 * @return
	 * @throws ManagerException
	 */
	public boolean updateDictArrTree(String dicttypeId, String dictdataValue, String[] docid) throws ManagerException {
		boolean state = false;
		//判断字典是否缓冲数据
		boolean isCacha = this.isCachable(dicttypeId);
		DBUtil dbUtil = new DBUtil();
		Data data = this.getDicttypeById(dicttypeId);
		String DATA_TABLE_NAME = data.getDataTableName();
		String DATA_NAME_FILED = data.getDataNameField();
		String DATA_VALUE_FIELD = data.getDataValueField();
		String DATA_ORDER_FIELD = data.getDataOrderField();
		String DATA_PARENTID_FIELD = data.getDataParentIdFild();
		String DATA_TYPEID_FIELD = data.getDataTypeIdField();


		String order_sql = toString(docid,DATA_TABLE_NAME,DATA_VALUE_FIELD,DATA_NAME_FILED,DATA_ORDER_FIELD);

		DBUtil db_order = new DBUtil();
		int[] order_val = new int[docid.length];

		StringBuffer update_tree_dict = new StringBuffer();

		TransactionManager tm = new TransactionManager();

		try{
			tm.begin();
			db_order.executeSelect(order_sql);
			if(db_order.size() == docid.length){
				for(int i = 0; i < order_val.length; i++){
					order_val[i] = db_order.getInt(i, DATA_ORDER_FIELD);
				}
				//Arrays.sort(order_val);
			}
			for(int i = 0; i < docid.length; i++){
				String[] doc = docid[i].split(":");
				String value = doc[0];
				String name = doc[1];
				if(!"".equals(DATA_TYPEID_FIELD) && DATA_TYPEID_FIELD!=null){
					if("".equals(dictdataValue)){
						update_tree_dict.append("update ").append(DATA_TABLE_NAME).append(" a set ")
						.append(DATA_ORDER_FIELD).append("=").append(order_val[i]).append(" where ")
						.append(DATA_NAME_FILED).append("='").append(name)
						.append("' and ").append(DATA_VALUE_FIELD).append("='").append(value).append("' and ")
						.append(DATA_TYPEID_FIELD).append("='").append(dicttypeId).append("' and ")
						.append(DATA_PARENTID_FIELD).append(" is null ");
						dbUtil.addBatch(update_tree_dict.toString());
						update_tree_dict.setLength(0);
					}else{
						update_tree_dict.append("update ").append(DATA_TABLE_NAME).append(" a set ")
						.append(DATA_ORDER_FIELD).append("=").append(order_val[i]).append(" where ")
						.append(DATA_NAME_FILED).append("='").append(name)
						.append("' and ").append(DATA_VALUE_FIELD).append("='").append(value).append("' and ")
						.append(DATA_TYPEID_FIELD).append("='").append(dicttypeId).append("' and ")
						.append(DATA_PARENTID_FIELD).append("='").append(dictdataValue).append("' ");
						dbUtil.addBatch(update_tree_dict.toString());
						update_tree_dict.setLength(0);
					}
				}else{
					if("".equals(dictdataValue)){
						update_tree_dict.append("update ").append(DATA_TABLE_NAME).append(" a set ")
						.append(DATA_ORDER_FIELD).append("=").append(order_val[i]).append(" where ")
						.append(DATA_NAME_FILED).append("='").append(name)
						.append("' and ").append(DATA_VALUE_FIELD).append("='").append(value).append("' and ")
						.append(DATA_PARENTID_FIELD).append(" is null ");
						dbUtil.addBatch(update_tree_dict.toString());
						update_tree_dict.setLength(0);
					}else{
						update_tree_dict.append("update ").append(DATA_TABLE_NAME).append(" a set ")
						.append(DATA_ORDER_FIELD).append("=").append(order_val[i]).append(" where ")
						.append(DATA_NAME_FILED).append("='").append(name)
						.append("' and ").append(DATA_VALUE_FIELD).append("='").append(value).append("' and ")
						.append(DATA_PARENTID_FIELD).append("='").append(dictdataValue).append("' ");
						dbUtil.addBatch(update_tree_dict.toString());
						update_tree_dict.setLength(0);
					}
				}
			}
			dbUtil.executeBatch();
			tm.commit();
			//直接子节点
			List items = this.getChildDictdataListByDataId(data.getDataId(),dictdataValue);	
			if(items!=null){//非一级数据项
				Item dictdata = new Item();
				dictdata.setItemId(dictdataValue);
				dictdata.setValue(dictdataValue);
				dictdata.setDataId(data.getDataId());
				dictdata.setSubItems(items);
				if(isCacha){
					Event event = new EventImpl(dictdata, DictionaryChangeEvent.DICTIONARY_TREE_DATA_ORDERCHANGE);
					super.change(event,true);
				}
			}else{//一级数据项,dictdataValue==""
				Data dicttype = new Data();
				dicttype.setDataId(data.getDataId());
				dicttype.setItems(this.getChildDictdataListByTypeId(data.getDataId()));
				if(isCacha){
					Event event = new EventImpl(dicttype, DictionaryChangeEvent.DICTIONARY_TREE_DATA_ORDERCHANGE);
					super.change(event,true);
				}
			}
			state = true;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		}
		return state;
	}

	/**
	 * 树形启用，停用字典项
	 * @param dicttypeId
	 * @param dictdataValue 父节点ID
	 * @param docid
	 * @param flag
	 * @param isRescure 是否递归启用/停用 子数据项
	 * @return
	 * @throws ManagerException
	 */
	public boolean changeState(String dicttypeId, String dictdataValue, String[] docid, String flag, String isRescure) throws ManagerException {
		boolean state = false;
		DBUtil dbUtil = new DBUtil();
		Data data = this.getDicttypeById(dicttypeId);
		String DATA_TABLE_NAME = data.getDataTableName();
		String DATA_NAME_FILED = data.getDataNameField();
		String DATA_VALUE_FIELD = data.getDataValueField();
		String DATA_PARENTID_FIELD = data.getDataParentIdFild();
		String DATA_TYPEID_FIELD = data.getDataTypeIdField();
		String DATA_VALIDATE_FIELD = data.getData_validate_field();
		List mutiItems = new ArrayList();
		StringBuffer update_change_dict = new StringBuffer();
		try{
			for(int i = 0; i < docid.length; i++){
				String[] doc = docid[i].split(":");
				String value = doc[0];
				String name = doc[1];
				Item itemForRefresh = new Item();
				itemForRefresh.setDataId(dicttypeId);
				itemForRefresh.setItemId(value);
				itemForRefresh.setValue(value);
				mutiItems.add(itemForRefresh);
				//子数据项的集合
				//add by ge.tao
				String subsql = "";
				if(ISRESCURE_FLAG.equalsIgnoreCase(isRescure)){					
					subsql = "select " + DATA_VALUE_FIELD + " from " + DATA_TABLE_NAME + 
						" start with " + DATA_VALUE_FIELD + "='" + value + "' connect by prior " + 
						DATA_VALUE_FIELD + "=" + DATA_PARENTID_FIELD;					
				}else{
					subsql = value;
				}
				//--end
				if(!"".equals(DATA_TYPEID_FIELD) && DATA_TYPEID_FIELD!=null){
					if("".equals(dictdataValue)){//顶级节点
						update_change_dict.append("update ").append(DATA_TABLE_NAME).append(" set ")
						.append(DATA_VALIDATE_FIELD).append("='").append(flag).append("' where ")
						.append(DATA_VALUE_FIELD).append("in (").append(subsql).append(") and ")
						.append(DATA_TYPEID_FIELD).append("='").append(dicttypeId).append("'  ");
						dbUtil.addBatch(update_change_dict.toString());						
					}else{
						update_change_dict.append("update ").append(DATA_TABLE_NAME).append(" set ")
						.append(DATA_VALIDATE_FIELD).append("='").append(flag).append("' where ")
						.append(DATA_VALUE_FIELD).append(" in (").append(subsql).append(") and ")
						.append(DATA_TYPEID_FIELD).append("='").append(dicttypeId).append("' ");
						dbUtil.addBatch(update_change_dict.toString());						
					}
				}else{
					if("".equals(dictdataValue)){//顶级节点
						update_change_dict.append("update ").append(DATA_TABLE_NAME).append(" set ")
						.append(DATA_VALIDATE_FIELD).append("='").append(flag).append("' where ")
						.append(DATA_VALUE_FIELD).append(" in (").append(subsql).append(") ");
						dbUtil.addBatch(update_change_dict.toString());						
					}else{
						update_change_dict.append("update ").append(DATA_TABLE_NAME).append(" set ")
						.append(DATA_VALIDATE_FIELD).append("='").append(flag).append("' where ")
						.append(DATA_VALUE_FIELD).append(" in (").append(subsql).append(")  ");
						dbUtil.addBatch(update_change_dict.toString());						
					}
				}
//				System.out.println("-------change state sql------------" + update_change_dict.toString());
				update_change_dict.setLength(0);				
			}
			dbUtil.executeBatch();

			//构造Item, 刷新内存
			Item item = new Item();
			item.setDataId(dicttypeId);
			item.setSubItems(mutiItems);
			int flag_ = 0;
			try{
				flag_ = Integer.parseInt(flag);
			}catch(Exception e){
				flag_ = 0;
			}
			item.setDataValidate(flag_);
			if(ISRESCURE_FLAG.equalsIgnoreCase(isRescure)){		
				item.setFlag(true);
			}else{
				item.setFlag(false);
			}
			if(isCachable(dicttypeId)){
				Event event = new EventImpl(item, DictionaryChangeEvent.DICTIONARY_DATA_VALIDATE_UPDATE);
				super.change(event,true);
			}

			state = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbUtil.resetBatch();
		}
		return state;
	}

	/**
	 * 列表启用，停用字典项
	 * @param dicttypeId
	 * @param docid
	 * @param flag
	 * @return
	 * @throws ManagerException
	 */
	public boolean changeState(String dicttypeId, String[] docid, String flag) throws ManagerException {
		boolean state = false;
		DBUtil dbUtil = new DBUtil();
		Data data = this.getDicttypeById(dicttypeId);
		String DATA_TABLE_NAME = data.getDataTableName();
		String DATA_NAME_FILED = data.getDataNameField();
		String DATA_VALUE_FIELD = data.getDataValueField();
		String DATA_VALIDATE_FIELD = data.getData_validate_field();
		List mutiItems = new ArrayList();
		StringBuffer update_dict = new StringBuffer();
		try {
			for(int i = 0; i < docid.length; i++){
				String[] doc = docid[i].split(":");
				String value = doc[0];
				String name = doc[1];
				Item itemForRefresh = new Item();
				itemForRefresh.setDataId(dicttypeId);
				itemForRefresh.setItemId(value);
				itemForRefresh.setValue(value);
				mutiItems.add(itemForRefresh);

				update_dict.append("update ").append(DATA_TABLE_NAME).append(" set ")
					.append(DATA_VALIDATE_FIELD).append("=").append(flag).append(" where ")
					.append(DATA_NAME_FILED).append("='").append(name)
					.append("' and ").append(DATA_VALUE_FIELD).append("='").append(value).append("'");

				dbUtil.addBatch(update_dict.toString());
				update_dict.setLength(0);
			}
			dbUtil.executeBatch();

			//构造Item, 刷新内存
			Item item = new Item();
			item.setDataId(dicttypeId);
			item.setSubItems(mutiItems);			
			int flag_ = 0;
			try{
				flag_ = Integer.parseInt(flag);
			}catch(Exception e){
				flag_ = 0;
			}
			item.setDataValidate(flag_);
			item.setFlag(false);

			if(isCachable(dicttypeId)){
				Event event = new EventImpl(item, DictionaryChangeEvent.DICTIONARY_DATA_VALIDATE_UPDATE);
				super.change(event,true);
			}

			state = true;
		}catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbUtil.resetBatch();
		}
		return state;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getDictTypeUseTableStates(java.lang.String, java.lang.String)
	 * 根据数据源名称和数据库表名称,判断数据库表被字典类型的使用情况
	 * 0:数据库表没有被任何字典类型使用
	 * 1:数据库表被字典类型独占(字典数据没有指定类型ID)
	 * 2:数据库表被多个字典类型公用(字典数据指定了类型ID)
	 */
	public int getDictTypeUseTableStates(String dbName, String tableName) {
		DBUtil dbUtil = new DBUtil();
		StringBuffer sql = new StringBuffer().append("select t.DATA_TYPEID_FIELD from td_sm_dicttype t where ")
			.append("t.data_table_name='").append(tableName).append("' and t.DATA_DBNAME='").append(dbName).append("'");
		try {
			dbUtil.executeSelect(sql.toString());
			if(dbUtil.size()==0){//没有被字典类型使用
				return DICTTYPE_USE_TABLE_FREE;
			}else {
				String typeIdField = dbUtil.getString(0,"DATA_TYPEID_FIELD")==null?"":dbUtil.getString(0,"DATA_TYPEID_FIELD");
				if(this.strIsNull(typeIdField)){//没有指定类型ID, 该表被其他字典独占
					return DICTTYPE_USE_TALBE_SINGLE;
				}else{//指定了字典类型ID,改表可以被多个字典类型公用
					return DICTTYPE_USE_TABLE_SHARE;
				}
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getUnableNullColumns(java.lang.String, java.lang.String)
	 * @return List<ColumnMetaData>
	 * 根据数据源和数据库表名,获取数据库表不能为空的字段列表
	 */
	public List getUnableNullColumns(String dbName, String tableName) {
		List list = null;
		if(this.strIsNull(dbName) || this.strIsNull(tableName)){
			return list;
		}
		Set set = DBUtil.getColumnMetaDatas(dbName,tableName);
		Iterator it = set.iterator();
		if(it.hasNext()){
			list = new ArrayList();
		}
		while(it.hasNext()){
			ColumnMetaData  metaData = (ColumnMetaData)it.next();			
			if("no".equalsIgnoreCase(metaData.getNullable())){
				//判断当前表的字段的主键是否在tableinfo表中配置,primaryKey!=null就是配置了主键,并且主键的名称==当前列的名称 跳过
				PrimaryKey primaryKey = PrimaryKeyCacheManager.getInstance().getPrimaryKeyCache(dbName).getIDTable(tableName.toLowerCase());
				if(primaryKey!=null && primaryKey.getPrimaryKeyName().equalsIgnoreCase(metaData.getColumnName())){
					continue;
				}
				list.add(metaData);	
			}
		}
		return list;
	}

	/**
	 * 根据数据源和数据库表名,获取数据库表不能为空的字段列的串 col1,col2...
	 */
	public String getUnableNullColumnNames(Data dicttype) {
		String unableNullColumnNames = "";
		List unableNullColumns = this.getUnableNullColumns(dicttype.getDataDBName(),dicttype.getDataTableName());
		for(int i=0;unableNullColumns!=null && i<unableNullColumns.size();i++){
			ColumnMetaData  metaData = (ColumnMetaData)unableNullColumns.get(i);
			if(COLUMN_AVIALABLE == columnUseStatue(dicttype,metaData.getColumnName(),"")){//没有被属性使用的
				if(this.strIsNull(unableNullColumnNames)){
					unableNullColumnNames += metaData.getColumnName();
				}else{
					unableNullColumnNames += "," + metaData.getColumnName();
				}
			}
		}		
		return unableNullColumnNames;
	}

	/**
	 * 得到高级字段配置对应的数据库字段
	 * @param dicttypeId
	 * @return
	 */
	public String getColumnName(String dicttypeId){
		String columnName = "";
		DBUtil dbUtil = new DBUtil();
		String sql = "select a.table_column from TD_SM_DICATTACHFIELD a where a.dicttype_id='"+dicttypeId+"'";
		try {
			dbUtil.executeSelect(sql);
			if(dbUtil.size() > 0){
				for(int i = 0; i < dbUtil.size(); i++){
					if("".equals(columnName)){
						columnName = dbUtil.getString(i, "table_column");
					}else{
						columnName += "," + dbUtil.getString(i, "table_column"); 
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return columnName;
	}

	/**
	 * 得附字段值
	 * @param dicttypeId
	 * @param name
	 * @param value
	 * @param attachField
	 * @return
	 * @throws ManagerException 
	 */
	public String getAttachValue(String dicttypeId, String name, String value, String attachField) throws ManagerException{
		String attachFieldValue = "";
		DBUtil dbUtil_attachTypeName = new DBUtil();
		DBUtil dbUtil = new DBUtil();
		Data data = this.getDicttypeById(dicttypeId);
		String data_table_name = data.getDataTableName();
		String data_name_filed = data.getDataNameField();
		String data_value_field = data.getDataValueField();
		String tableColumn = "";
		//考虑attachField=NAME:100024:all的情况.
		if(attachField.split(":").length>0){
			tableColumn = attachField.split(":")[0];
		}else{
			tableColumn = attachField;
		}
		StringBuffer sql_attachTypeName = new StringBuffer()
			.append("select tt.input_type_name from tb_sm_inputtype tt ")
			.append("where tt.input_type_id in (select t.input_type_id from td_sm_dicattachfield t ")
			.append("where t.dicttype_id = '").append(dicttypeId).append("' ")
			.append("and t.table_column = '").append(tableColumn).append("') ");
		StringBuffer sql = new StringBuffer();
		try{
			dbUtil_attachTypeName.executeSelect(sql_attachTypeName.toString());
			if(dbUtil_attachTypeName.getString(0,"input_type_name").equals("选择人员") ||
					dbUtil_attachTypeName.getString(0,"input_type_name").equals("当前用户")){
				sql.append("select a.").append(tableColumn).append("||' '||USER_NAME as ").append(tableColumn)
					.append(" from ").append(data_table_name)
					.append(" a, TD_SM_USER where a.").append(data_name_filed).append(" ='").append(name)
					.append("' and a.").append(data_value_field).append("='").append(value)
					.append("' and USER_ID=a.").append(tableColumn);
				dbUtil.executeSelect(sql.toString());
			}else if(dbUtil_attachTypeName.getString(0,"input_type_name").equals("选择机构") ||
					dbUtil_attachTypeName.getString(0,"input_type_name").equals("当前机构")){
				sql.append("select a.").append(tableColumn).append("||' '||ORG_NAME as ").append(tableColumn)
					.append(" from ").append(data_table_name)
					.append(" a, td_sm_organization tt where a.").append(data_name_filed).append(" ='").append(name)
					.append("' and a.").append(data_value_field).append("='").append(value)
					.append("' and tt.ORG_ID=a.").append(tableColumn);
			dbUtil.executeSelect(sql.toString());
			}else if(dbUtil_attachTypeName.getString(0,"input_type_name").equals("选择时间") ||
					dbUtil_attachTypeName.getString(0,"input_type_name").equals("当前时间")){
				sql.append("select to_char(")
					.append(tableColumn).append(",'yyyy-mm-dd hh24:mi:ss') as ").append(tableColumn).append(" from ").append(data_table_name)
					.append(" where ").append(data_name_filed).append(" ='").append(name)
					.append("' and ").append(data_value_field).append("='").append(value)
					.append("'");
				dbUtil.executeSelect(sql.toString());
			}else{
				sql.append("select ")
				.append(tableColumn).append(" from ").append(data_table_name)
				.append(" where ").append(data_name_filed).append(" ='").append(name)
				.append("' and ").append(data_value_field).append("='").append(value)
				.append("'");
			dbUtil.executeSelect(sql.toString());
			}
			if(dbUtil.size() > 0){
				attachFieldValue = dbUtil.getString(0, tableColumn);
			}
		}catch(Exception e){
			e.printStackTrace();
			attachFieldValue = "无效ID";
		}
		return attachFieldValue;
	}

	public String getPrimarykeysCondition(Map primarykeys){
		if(primarykeys != null && primarykeys.size() > 0){
			Iterator iterator = primarykeys.keySet().iterator();
			StringBuffer condition = new StringBuffer();
			while(iterator.hasNext()){
				String columnName = (String)iterator.next();
				condition.append(" and a.").append(columnName).append("='").append(primarykeys.get(columnName)).append("'");
			}
			return condition.toString();
		}
		return "";
	}

	public String getOrgNames(String orgids) throws Exception
	{
		if(!StringUtil.isEmpty(orgids))
		{
			
			StringBuffer orgsql = new StringBuffer("select org_id ||' '|| remark5 as orgname from td_sm_organization where org_id in (");
			String[] array = orgids.split(",");
			for(int i = 0; i < array.length; i ++){
				if(i == 0)
					orgsql.append("?");
				else 
					orgsql.append(",?");
			}
			orgsql.append(")");
			PreparedDBUtil db = new PreparedDBUtil();
			db.preparedSelect(orgsql.toString());
			for(int i = 0; i < array.length; i ++){
				db.setString(i+1, array[i]);
			}
			final StringBuffer temp = new StringBuffer();
			db.executePreparedWithRowHandler(new NullRowHandler(){

				@Override
				public void handleRow(Record origine)
						throws Exception {
					if(temp.length() == 0)
					{
						temp.append(origine.get("ORGNAME"));
					}
					else
						temp.append(",").append(origine.get("ORGNAME"));
					
				}
				
			});
			orgids = temp.toString();
			
		}
		else
			orgids = "";
		return orgids;
	}
	
	
	public String getUserNames(String userids) throws Exception
	{
		if(!StringUtil.isEmpty(userids))
		{
			
			StringBuffer orgsql = new StringBuffer("select (u.user_id ||' '||u.USER_REALNAME) as username from TD_SM_USER u where u.user_id in (");
			String[] array = userids.split(",");
			for(int i = 0; i < array.length; i ++){
				if(i == 0)
					orgsql.append("?");
				else 
					orgsql.append(",?");
			}
			orgsql.append(")");
			PreparedDBUtil db = new PreparedDBUtil();
			db.preparedSelect(orgsql.toString());
			for(int i = 0; i < array.length; i ++){
				db.setString(i+1, array[i]);
			}
			final StringBuffer temp = new StringBuffer();
			db.executePreparedWithRowHandler(new NullRowHandler(){

				@Override
				public void handleRow(Record origine)
						throws Exception {
					if(temp.length() == 0)
					{
						temp.append(origine.get("USERNAME"));
					}
					else
						temp.append(",").append(origine.get("USERNAME"));
					
				}
				
			});
			userids = temp.toString();
			
		}
		else
			userids = "";
		return userids;
	}
	
	
	/**
	 * 得附字段值
	 * @param dicttypeId
	 * @param name
	 * @param value
	 * @param attachField
	 * @param primarykeys
	 * @return
	 * @throws ManagerException 
	 */
	public String getAttachValue(String dicttypeId, String name, String value, String attachField,Map primarykeys) throws ManagerException{
		String attachFieldValue = "";
		PreparedDBUtil dbUtil_attachTypeName = new PreparedDBUtil();
		DBUtil dbUtil = new DBUtil();
		Data data = this.getDicttypeById(dicttypeId);
		String data_table_name = data.getDataTableName();
		String data_name_filed = data.getDataNameField();
		String data_value_field = data.getDataValueField();
		String condition = getPrimarykeysCondition(primarykeys);
		//primarykeys.keySet().iterator();
		String tableColumn = "";
		//考虑attachField=NAME:100024:all的情况.
		if(attachField.split(":").length>0){
			tableColumn = attachField.split(":")[0];
		}else{
			tableColumn = attachField;
		}
		StringBuffer sql_attachTypeName = new StringBuffer()
			.append("select tt.input_type_name from tb_sm_inputtype tt ")
			.append("where tt.input_type_id in (select t.input_type_id from td_sm_dicattachfield t ")
			.append("where t.dicttype_id = ? ")
			.append("and t.table_column = ?) ");
		StringBuffer sql = new StringBuffer();
		try{
			dbUtil_attachTypeName.preparedSelect(sql_attachTypeName.toString());
			dbUtil_attachTypeName.setString(1, dicttypeId);
			dbUtil_attachTypeName.setString(2, tableColumn);
			dbUtil_attachTypeName.executePrepared();
			if(dbUtil_attachTypeName.getString(0,"input_type_name").equals("选择人员") ||
					dbUtil_attachTypeName.getString(0,"input_type_name").equals("当前用户")){
				sql.append("select a.").append(tableColumn).append(" as username  from ").append(data_table_name)
					.append(" a where ");
//					.append(data_name_filed).append(" ='").append(name)
//					.append("' and a.").append(data_value_field).append("='").append(value)
				if(name == null || name.equals("")){
					sql.append("(a.").append(data_name_filed).append(" ='").append(name).append("'")
					.append(" or a.").append(data_name_filed).append(" is null) ");
				}else{
					sql.append("a.").append(data_name_filed).append(" ='").append(name).append("'");
				}
				if(value == null || value.equals("")){
					sql.append(" and (a.").append(data_value_field).append("='").append(value).append("' or a.")
					.append(data_value_field).append(" is null) ") ;
				}else{
					sql.append(" and a.").append(data_value_field).append("='").append(value).append("'");
				}
				
				sql.append(condition);
				//System.out.println(sql.toString());
			
				dbUtil.executeSelect(data.getDataDBName(),sql.toString());
				if(dbUtil.size() > 0){
					attachFieldValue = dbUtil.getString(0, "username");
					attachFieldValue = this.getUserNames(attachFieldValue);
				}
						
				
				//dbUtil.executeSelect(sql.toString());
			}else if(dbUtil_attachTypeName.getString(0,"input_type_name").equals("选择机构") ||
					dbUtil_attachTypeName.getString(0,"input_type_name").equals("当前机构")){
				sql.append("select a.").append(tableColumn).append(" from ").append(data_table_name)
					.append(" a where ");
//					.append(data_name_filed).append(" ='").append(name)
//					.append("' and a.").append(data_value_field).append("='").append(value)
				if(name == null || name.equals("")){
					sql.append("(a.").append(data_name_filed).append(" ='").append(name).append("'")
					.append(" or a.").append(data_name_filed).append(" is null) ");
				}else{
					sql.append("a.").append(data_name_filed).append(" ='").append(name).append("'");
				}
				if(value == null || value.equals("")){
					sql.append(" and (a.").append(data_value_field).append("='").append(value).append("' or a.")
					.append(data_value_field).append(" is null) ") ;
				}else{
					sql.append(" and a.").append(data_value_field).append("='").append(value).append("'");
				}
				sql.append(condition);
				//System.out.println(sql.toString());
				dbUtil.executeSelect(data.getDataDBName(),sql.toString());
				if(dbUtil.size() > 0){
					attachFieldValue = dbUtil.getString(0, tableColumn);
					attachFieldValue = this.getOrgNames(attachFieldValue);
				}
			//dbUtil.executeSelect(sql.toString());
			}else if(dbUtil_attachTypeName.getString(0,"input_type_name").equals("选择时间") ||
					dbUtil_attachTypeName.getString(0,"input_type_name").equals("当前时间")){
//				sql.append("select to_char(")
//					.append(tableColumn).append(",'yyyy-mm-dd hh24:mi:ss') as ").append(tableColumn).append(" from ").append(data_table_name)
//					.append(" a where ");
				sql.append("select ")
				.append(tableColumn).append(" from ").append(data_table_name)
				.append(" a where ");
				if(name == null || name.equals("")){
					sql.append("(a.").append(data_name_filed).append(" ='").append(name).append("'")
					.append(" or a.").append(data_name_filed).append(" is null) ");
				}else{
					sql.append("a.").append(data_name_filed).append(" ='").append(name).append("'");
				}
				if(value == null || value.equals("")){
					sql.append(" and (a.").append(data_value_field).append("='").append(value).append("' or a.")
					.append(data_value_field).append(" is null) ") ;
				}else{
					sql.append(" and a.").append(data_value_field).append("='").append(value).append("'");
				}
				sql.append(condition);
				//System.out.println(sql.toString());
				dbUtil.executeSelect(data.getDataDBName(),sql.toString());
				if(dbUtil.size() > 0){
					attachFieldValue = dbUtil.getString(0, tableColumn);
				}
				//dbUtil.executeSelect(sql.toString());
			}else{
				sql.append("select a.").append(tableColumn).append("  from ").append(data_table_name)
					.append(" a where ");
				if(name == null || name.equals("")){
					sql.append("(a.").append(data_name_filed).append(" ='").append(name).append("'")
					.append(" or a.").append(data_name_filed).append(" is null) ");
				}else{
					sql.append("a.").append(data_name_filed).append(" ='").append(name).append("'");
				}
				if(value == null || value.equals("")){
					sql.append(" and (a.").append(data_value_field).append("='").append(value).append("' or a.")
					.append(data_value_field).append(" is null) ") ;
				}else{
					sql.append(" and a.").append(data_value_field).append("='").append(value).append("'");
				}
			//dbUtil.executeSelect(sql.toString());
				sql.append(condition);
				//System.out.println(sql.toString());
				dbUtil.executeSelect(data.getDataDBName(),sql.toString());
				if(dbUtil.size() > 0){
					attachFieldValue = dbUtil.getString(0, tableColumn);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			attachFieldValue = "无效ID";
		}
		return attachFieldValue;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getUsualRelationDicttypeList(java.lang.String)
	 * dicttype_type != 0
	 * 根据字典类型ID,获得所有需要维护 常用编码关系的字典类型
	 */
	public List getUsualRelationDicttypeList(String dicttypeId) throws ManagerException {

		return this.getPartChildDicttypeList(dicttypeId,BUSINESS_DICTTYPE_USUALONLY);
	}

	public String getPrimaryColumnNames(String tableName,Map filterkeys){

		Set set = DBUtil.getPrimaryKeyMetaDatas(tableName);
		if(set != null && set.size() > 0)
		{
			Iterator it = set.iterator();
			StringBuffer columnNames = new StringBuffer();
			while(it.hasNext())
			{
				PrimaryKeyMetaData key = (PrimaryKeyMetaData)it.next();

				String keyname = key.getColumnName();
				if(filterkeys.containsKey(keyname.toLowerCase())){
					continue;
				}
				columnNames.append(",a.").append(keyname);
			}
			return columnNames.toString();
		}
		return "";
	}
	/**
	 * 修改数据项得到主键信息条件
	 * @param tableName
	 * @param request
	 * @param filterkeys
	 * @return
	 */
	public String getUpdatePrimaryCondition(String tableName,HttpServletRequest request,Map filterkeys)
	{

		Set set = DBUtil.getPrimaryKeyMetaDatas(tableName);
		if(set != null && set.size() > 0)
		{
			StringBuffer con = new StringBuffer();
			Iterator it = set.iterator();
			while(it.hasNext())
			{
				PrimaryKeyMetaData key = (PrimaryKeyMetaData)it.next();

				String keyname = key.getColumnName();
				if(filterkeys.containsKey(keyname.toLowerCase())){
					continue;
				}
				String requestValue = request.getParameter(keyname.toLowerCase())==null?"":request.getParameter(keyname.toLowerCase());
				con.append(" and ").append(keyname).append("='").append(requestValue).append("' ");
			}
			return con.toString();
		}
		else
		{
			return "";
		}
	}
	/**
	 * 修改树形数据项
	 * @param dictdata 得到新的数据项
	 * @param request 
	 * @param keyName 修改条件数据库保存列的"名称字段"值
	 * @param keyValue 修改条件数据库保存列的"值字段"值
	 * @param parentId 父节点id,id不为空时用keyName与keyValue来做修改条件，否则加上parentId作为条件
	 * @return boolean 修改成功状态
	 * @throws ManagerException
	 */
	public boolean updateAdvanceDictdata(Item dictdata, HttpServletRequest request, 
			String keyName, String keyValue, String parentId)  throws ManagerException {
		boolean state = false;
		if(dictdata == null || request == null ) return state;
		DBUtil judge_db = new DBUtil();
		DBUtil update_db = new DBUtil();
		//字典类型ID
		String dicttypeId = dictdata.getDataId();

		if(this.strIsNull(dicttypeId)) return false;
		//根据字典类型ID,获取字典类型对象
		Data dicttype = new Data();
		dicttype = getDicttypeById(dicttypeId);
		//数据保存字段:
//		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_parentid_field = dicttype.getDataParentIdFild();
		String data_org_field = dicttype.getData_create_orgid_field();




		StringBuffer judge_sql = new StringBuffer()
			.append("select * from ").append(data_table_name)
			.append(" a where a.").append(data_name_filed).append("='").append(dictdata.getName())
			.append("' and a.").append(data_value_field).append("='").append(dictdata.getValue()).append("' ")
			.append(dictdata.getPrimaryCondition());
		if(!strIsNull(data_typeid_field) ){
			judge_sql.append(" and a.").append(data_typeid_field).append("='").append(dicttypeId).append("'");
		}

		try {
			if(!keyName.equals(dictdata.getName()) && !keyValue.equals(dictdata.getValue())){
				judge_db.executeSelect(judge_sql.toString());
				if(judge_db.size() > 0){
					return false;
				}
			}

			//基础字典修改
			StringBuffer update_sql = new StringBuffer()
				.append("update ").append(data_table_name).append(" a set ")
				.append(data_name_filed).append("='").append(dictdata.getName()).append("', ")
				.append(data_value_field).append("='").append(dictdata.getValue()).append("'");
			if(!this.strIsNull(data_validate_field)){//插入是否失效字段
			    update_sql.append(", ").append(data_validate_field)
			    	.append("='").append(dictdata.getDataValidate()).append("'");
			}
			if(!this.strIsNull(data_org_field)){//插入所属机构字段
			    update_sql.append(", ").append(data_org_field)
			    	.append("='").append(dictdata.getDataOrg()).append("'");
			}


			//处理附加字段
			//得到这个类型的附加字段列表
			List dictatts = this.getDictdataAttachFieldList(dicttypeId,-1);
			for(int i=0;dictatts!=null && i<dictatts.size();i++){
				DictAttachField dictatt = (DictAttachField)dictatts.get(i);
				String requestValue = request.getParameter(dictatt.getTable_column().toLowerCase())==null?"":request.getParameter(dictatt.getTable_column().toLowerCase());
				String tableColumn = dictatt.getTable_column();
				ColumnMetaData colum = DBUtil.getColumnMetaData(data_table_name, tableColumn);


				String type = dictatt.getColumnTypeName();
				if("date".equalsIgnoreCase(type)){
					update_sql.append(", ").append(tableColumn)
			    		.append("=TO_DATE('").append(requestValue).append("', 'YYYY-MM-DD HH24:MI:SS') ");
				}else if("number".equalsIgnoreCase(type) || "long".equalsIgnoreCase(type)){


					try {
						if(colum.getDECIMAL_DIGITS() > 0){
							double v = Double.parseDouble(requestValue);
							update_sql.append(", ").append(tableColumn)
						    	.append("=").append(v).append("");
						}else{
							int v = 0;
							v = Integer.parseInt(requestValue);
							 update_sql.append(", ").append(tableColumn)
						    	.append("=").append(v).append("");
						}
					} catch (NumberFormatException e) {
						 update_sql.append(", ").append(tableColumn)
					    	.append("=").append("0");
					}
				}else{
					update_sql.append(", ").append(tableColumn)
			    		.append("='").append(requestValue).append("'");
				}
			}
			//--结束附加字段处理

			//修改条件

			update_sql.append(" where ");
			if(keyName==null || keyName.equals("")){
				update_sql.append("(").append(data_name_filed).append(" is null or ").append(data_name_filed)
					.append("='') and ");	
			}else{
				update_sql.append(data_name_filed).append("='").append(keyName)
					.append("' and ");
			}
			if(keyValue==null || keyValue.equals("")){
				update_sql.append("(").append(data_value_field).append("='").append(keyValue)
					.append("' or ").append(data_value_field).append(" is null)");	
			}else{
			update_sql.append(data_value_field).append("='").append(keyValue)
				.append("' ");
			}
			if(!"".equals(parentId) && !"root".equals(parentId)){
				update_sql.append(" and ").append(data_parentid_field).append("='").append(parentId)
					.append("' ");
			}
			if(!this.strIsNull(data_typeid_field)){//类型ID字段**值
				update_sql.append(" and ").append(data_typeid_field).append("='").append(dicttypeId)
					.append("' ");
			}
//			Map map = new HashMap();//如果定义的名称字段，值字段，机构字段存在主键，不生成该字段的修改条件
//			map.put(data_name_filed.toLowerCase(),null);
//			map.put(data_value_field.toLowerCase(), null);
//			map.put(data_org_field.toLowerCase(), null);
//			String primaryKey = getUpdatePrimaryCondition(data_table_name,request,map);
//			
			String primaryKey = request.getParameter("primaryCondition")==null?"":request.getParameter("primaryCondition");
			update_sql.append(primaryKey);
			update_db.executeUpdate(dicttype.getDataDBName(),update_sql.toString());

			StringBuffer update_orgTax = new StringBuffer();
			update_orgTax.append("update TD_SM_TAXCODE_ORGANIZATION set DATA_NAME='")
			.append(dictdata.getName()).append("' where DATA_VALUE='")
			.append(dictdata.getValue()).append("' and DICTTYPE_ID='").append(dicttypeId).append("'");
			DBUtil db = new DBUtil();
			db.executeUpdate(update_orgTax.toString());
			if(isCachable(dictdata.getDataId())){
				Event event = new EventImpl(dictdata, DictionaryChangeEvent.DICTIONARY_DATA_INFO_UPDATE);
				super.change(event,true);
			}
			state = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return state;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#orgHasDictdataReadPower(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * 机构是否有对字典数据项的使用权限
	 */
	public boolean orgHasDictdataReadPower(String orgId, String dicttypeId,  String dictdataValue) {
		if(this.strIsNull(orgId) || this.strIsNull(dicttypeId) ||  this.strIsNull(dictdataValue)){
			return false;
		}	

		DBUtil db = new DBUtil();
		StringBuffer isChecked = new StringBuffer().append("select count(*) as num from ")
			.append("TD_SM_TAXCODE_ORGANIZATION where OPCODE='read' and ORG_ID='" )
	    	.append(orgId ).append("' and DICTTYPE_ID='" ).append(dicttypeId)
	    	.append( "' and DATA_VALUE='").append(dictdataValue ).append( "' ");
		try {
			db.executeSelect(isChecked.toString());
			if(db.size()>0 && db.getInt(0,"num")>0){//被选中
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//自己建的
		//前提是 指定了所属机构.
		Data dicttype = null;
		try {
			dicttype = this.getDicttypeById(dicttypeId);
			if(dicttype != null){
				String ownOrg = dicttype.getData_create_orgid_field();
				if(!this.strIsNull(ownOrg)){
					DBUtil selfDB = new DBUtil();
					StringBuffer self_create = new StringBuffer();
					String dbName = dicttype.getDataDBName();
					String tableName = dicttype.getDataTableName();
					String valueField = dicttype.getDataValueField();

					self_create.append("select count(*) as num from ").append(tableName).append(" where ").append(valueField)
					.append("='").append(dictdataValue).append("' and ").append(ownOrg).append("='").append(orgId).append("' ");
					try {
						selfDB.executeSelect(dbName,self_create.toString());
						if(selfDB.size()>0 && selfDB.getInt(0,"num")>0){//是本机构建立的,可见
							return true;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (ManagerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#orgHasDictdataUsualPower(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * 机构是否有对字典数据项的常用维护权限
	 */
	public boolean orgHasDictdataUsualPower(String orgId, String dicttypeId,  String dictdataValue) {
		if(this.strIsNull(orgId) || this.strIsNull(dicttypeId)  || this.strIsNull(dictdataValue)){
			return false;
		}
		DBUtil db = new DBUtil();
		StringBuffer isChecked = new StringBuffer().append("select count(*) as num from ")
			.append("TD_SM_TAXCODE_ORGANIZATION where OPCODE='usual' and ORG_ID='" )
	    	.append(orgId ).append("' and DICTTYPE_ID='" ).append(dicttypeId)
	    	.append( "' and DATA_VALUE='").append(dictdataValue ).append( "' ");
		try {
			db.executeSelect(isChecked.toString());
			if(db.size()>0 && db.getInt(0,"num")>0){//被选中
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#isContainAttachDictType(java.lang.String)
	 * 是否有 维护附加字段的 字典类型
	 */
	public boolean isContainAttachDictType(String dicttypeId) {
		boolean flag = false;
		if(strIsNull(dicttypeId)) return flag;
		DBUtil db = new DBUtil();
		String sql = "select 1 from td_sm_dicttype where DICTTYPE_TYPE in (" + 
				ALLREAD_BUSINESS_DICTTYPE + "," + PARTREAD_BUSINESS_DICTTYPE + "," + BASE_DICTTYPE + 
				") and DICTTYPE_PARENT='"+ dicttypeId +"'";
		try {
			db.executeSelect(sql);
			if(db.size()>0){
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getAttachDictTypeList(java.lang.String)
	 * 获取有 维护附加字段的 子字典列表
	 */
	public List getAttachDictTypeList(String dicttypeId) {
		try {
			return getPartChildDicttypeList(dicttypeId,ATTACH_DICTTYPE);
		} catch (ManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据机构ID与字典ID得到该字典数据项是否已经授权给该机构
	 * @param orgId
	 * @param dicttypeId
	 * @return
	 */
	private Map isExistTaxcode(String orgId, String dicttypeId, String opcode){
		Map map = new HashMap();
		String sql = "select DATA_VALUE from TD_SM_TAXCODE_ORGANIZATION where ORG_ID='" + orgId + "' and "
			+ "DICTTYPE_ID='" + dicttypeId + "' and opcode='" + opcode + "'";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			for(int i = 0; i < db.size(); i++){
				map.put(db.getString(i, "DATA_VALUE"), db.getString(i, "DATA_VALUE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 树形字典--根据值字段得到下级所有数据
	 * @param dicttypeId
	 * @param value
	 * @return
	 * @throws ManagerException
	 */
	private String[] getSunValue(String selectOrg, String dicttypeId,String value) throws ManagerException{
		String[] sunValues = null;
		Data dicttype = new Data();
		dicttype = getDicttypeById(dicttypeId);
		String dbName = dicttype.getDataDBName();
		String dbTableName = dicttype.getDataTableName();
		String parentIdField = dicttype.getDataParentIdFild();
		String valueField = dicttype.getDataValueField();
		String orgIdField = dicttype.getData_create_orgid_field(); 
		String curOrgId = accessControl.getChargeOrgId();
		//String curOrgId = request.getParameter("curOrgId");
//		System.out.println("curOrgId = " + curOrgId);
		StringBuffer sql = new StringBuffer();
		if(accessControl.isAdmin()){
			sql.append("select ").append(valueField).append(" from ").append(dbTableName)
				.append("  START WITH ").append(valueField).append(" = '").append(value)
				.append("' CONNECT BY PRIOR ").append(valueField).append(" = ").append(parentIdField);
		}else{
			sql.append("select ").append(valueField).append(" from ").append(dbTableName)
				.append(" where ");
			if(orgIdField != null && !"".equals(orgIdField)){
				sql.append(orgIdField).append("='").append(curOrgId).append("' or ");
			}
			if(!selectOrg.equals(curOrgId)){//如果所选机构是用户所属机构则只能操作自己采集的数据
				sql.append(valueField).append("='").append(value).append("' or ").append(valueField)
					.append(" in(SELECT data_value FROM TD_SM_TAXCODE_ORGANIZATION WHERE dicttype_id = '")
					.append(dicttypeId).append("' AND org_id='").append(curOrgId).append("' and opcode='read')")
					.append("  START WITH ").append(valueField).append(" = '").append(value)
					.append("' CONNECT BY PRIOR ").append(valueField).append(" = ").append(parentIdField);
			}
		}
		DBUtil db = new DBUtil();
		//System.out.println(sql.toString());
		try {
			db.executeSelect(dbName,sql.toString());
			if(db.size() > 0){
				sunValues = new String[db.size()];
				for(int i = 0; i < db.size(); i ++ ){
					sunValues[i] = db.getString(i, valueField);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagerException(e);
		}
		return sunValues;
	}

	private String[] getSunUsualValue(String selectOrg, String dicttypeId, String value) throws ManagerException{
		String[] sunUsualValues = null;
		Data dicttype = new Data();
		dicttype = getDicttypeById(dicttypeId);
		String dbName = dicttype.getDataDBName();
		String dbTableName = dicttype.getDataTableName();
		String parentIdField = dicttype.getDataParentIdFild();
		String valueField = dicttype.getDataValueField();
		//System.out.println("ddd = " + dicttype.getDicttype_type());
		StringBuffer sql = new StringBuffer();
		if((dicttype.getDicttype_type() == DictManager.ALLREAD_BUSINESS_DICTTYPE)
				|| (dicttype.getDicttype_type() == DictManager.BUSINESS_DICTTYPE_USUALONLY)){
			sql.append("select ").append(valueField).append(" from ").append(dbTableName)
				.append("  START WITH ").append(valueField).append(" = '").append(value)
				.append("' CONNECT BY PRIOR ").append(valueField).append(" = ").append(parentIdField);
		}else{
			sql.append("select ").append(valueField).append(" from (select * from ").append(dbTableName)
				.append(" where ").append(valueField).append(" in (SELECT data_value FROM TD_SM_TAXCODE_ORGANIZATION")
				.append("  WHERE dicttype_id='").append(dicttypeId).append("' AND OPCODE='read' and ORG_ID='")
				.append(selectOrg).append("'))").append("  START WITH ").append(valueField).append(" = '")
				.append(value)
				.append("' CONNECT BY PRIOR ").append(valueField).append(" = ").append(parentIdField);
		}
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(dbName,sql.toString());
			if(db.size() > 0){
				sunUsualValues = new String[db.size()];
				for(int i = 0; i < db.size(); i ++ ){
					sunUsualValues[i] = db.getString(i, valueField);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagerException(e);
		}
		return sunUsualValues;
	}

	/* 针对 翻页处理的 
	 * (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#storeOrgTaxcode(java.lang.String, java.lang.String, java.lang.String[], java.lang.String[])
	 * 保存机构编码关系 授权(可见) 
	 * 
	 */
	public void storeOrgTaxcode(String orgId, String dicttypeId, String[] dictdataValues, 
			String[] unselected_dictdataValue) throws ManagerException {
 		if(strIsNull(orgId) || strIsNull(dicttypeId) ) return ;
		DBUtil db = new DBUtil();
		DBUtil addDB = new DBUtil(); 
		TransactionManager tm = new TransactionManager();
		Map isExistTaxcodeMap = isExistTaxcode(orgId,dicttypeId,"read");//已经授权的
		try{
			//子机构sql
			StringBuffer orgs_sql = new StringBuffer()
				.append("select org.org_id from td_sm_organization org start with org.org_id='")
				.append(orgId).append("' connect by prior org.org_id=org.parent_id ");	

			//上级机构sql
			StringBuffer orgs_parent_sql = new StringBuffer()
				.append("select org.org_id from td_sm_organization org start with org.org_id='")
				.append(orgId).append("' connect by prior org.parent_id=org.org_id ");	

			//保存新数据 被选中的
			//保存当前机构的
			int dictdataValuesLength = 0;
			if(dictdataValues != null && dictdataValues[0] != null && !"".equals(dictdataValues[0])){
				dictdataValuesLength = dictdataValues.length;
			}
			String[] dataValues = new String[dictdataValuesLength];//保存数据项的值 数组
			String[] dataNames = new String[dictdataValuesLength];//保存数据项的名称 数组
			String[] isDeploys = new String[dictdataValuesLength];//树形数据项是否展开 数组--false没有被展开即下级数据没有读取出来--true展开
			for(int i=0;i<dictdataValuesLength;i++){				
				String dictdataValue = dictdataValues[i];				
				//dictdataValue value:name;
				if(dictdataValue.trim().length()==0) continue;
				String dictdataValuesa[] = dictdataValue.split(":");
				if(dictdataValuesa.length>=3){
					dataValues[i] = dictdataValuesa[0];
					dataNames[i] = dictdataValuesa[1];
					isDeploys[i] = dictdataValuesa[2];
				}else{
					dataValues[i] = "";
					dataNames[i] = "";
					isDeploys[i] = "";
				}
			}

			//未被 被选中的
			//串的格式: '','',''....
//			String un_dataValues = "";//保存数据项的值 数组	
//			(1)删除 未被选中的 可见和常用	
//			删除本身和子机构 多于的可见和常用权限
			tm.begin();
			int count = 0;
			for(int i=0;i<unselected_dictdataValue.length;i++){
				String dictdataValue = unselected_dictdataValue[i];
				String valueI = "";
				String isDeploy = "";
				if(dictdataValue.trim().length()==0) continue;
				String dictdataValuesa[] = dictdataValue.split(":");
				if(dictdataValuesa.length>=3){
					valueI = dictdataValuesa[0];	
					isDeploy = dictdataValuesa[2];
				}else{
					valueI = "";	
					isDeploy = "";
				}
				StringBuffer delete_subandself_read_usual = new StringBuffer();
				//如果该未选中数据项为已经授权的，用户取消授权且下级未展开，递归删除所有下级数据项
				if(isExistTaxcodeMap.get(valueI) != null && isDeploy.equals("false")){
					String[] values = getSunValue(orgId,dicttypeId,valueI);
					if(values != null){
						for(int v = 0; v < values.length; v++){
							delete_subandself_read_usual.append("delete td_sm_taxcode_organization where DICTTYPE_ID='")
								.append(dicttypeId).append("' and  opcode in ('read','usual') ")
								.append(" and data_value = '").append(values[v]).append("' ")
								.append(" and ORG_ID in (").append(orgs_sql.toString()).append(") "); 
							//加到批处理
							db.addBatch(delete_subandself_read_usual.toString());
							delete_subandself_read_usual.setLength(0);
							count ++;
						}
					}
				}else{
					delete_subandself_read_usual.append("delete td_sm_taxcode_organization where DICTTYPE_ID='")
						.append(dicttypeId).append("' and  opcode in ('read','usual') ")
						.append(" and data_value = '").append(valueI).append("' ")
						.append(" and ORG_ID in (").append(orgs_sql.toString()).append(") "); 
					//加到批处理
					db.addBatch(delete_subandself_read_usual.toString());
					delete_subandself_read_usual.setLength(0);
					count ++;
				}
				if(count > 900){
					db.executeBatch();
					count = 0;
				}				
			}
			if(count > 0){
				db.executeBatch();
			}
			//(1)删除 未被选中的 可见和常用	
			//删除本身和子机构 多于的可见和常用权限
//			if(!this.strIsNull(un_dataValues)){//有 没有被选中的
//				StringBuffer delete_subandself_read_usual = new StringBuffer();
//				delete_subandself_read_usual.append("delete td_sm_taxcode_organization where DICTTYPE_ID='")
//					.append(dicttypeId).append("' and  opcode in ('read','usual') ")
//					.append(" and data_value in (").append(un_dataValues).append(") ")
//					.append(" and ORG_ID in (").append(orgs_sql.toString()).append(") "); 
//				//加到批处理
//				db.addBatch(delete_subandself_read_usual.toString());
//				db.executeBatch();
//			}

			//(3)保存/更新			
			//本机构 上级机构
			DBUtil parentDB = new DBUtil();
			parentDB.executeSelect(orgs_parent_sql.toString());
			int uncount = 0;
			for(int p=0;p<parentDB.size();p++){
				//对机构上级循环
				String subOrgId = parentDB.getString(p,"org_id");
				for(int i=0;i<dataValues.length;i++){
					if(dataValues[i].trim().length()==0) continue;
					//如果选中数据项为未设置的权限，并且下级没有展开
					if(isExistTaxcodeMap.get(dataValues[i]) == null && isDeploys[i].equals("false")){
						String[] values = getSunValue(orgId,dicttypeId,dataValues[i]);
						if(values != null){
							for(int v = 0; v < values.length; v++){
								StringBuffer addNewData = new StringBuffer();
								addNewData.append("insert into TD_SM_TAXCODE_ORGANIZATION(ORG_ID,DICTTYPE_ID,DATA_VALUE,OPCODE,DATA_NAME) ")
								   .append("(select '").append(subOrgId).append("' as ORG_ID, '").append(dicttypeId)
								   .append("' as DICTTYPE_ID, '").append(values[v]).append("' as DATA_VALUE, 'read' as OPCODE, '")
								   .append(dataNames[i]).append("' as DATA_NAME from ")
								   .append("dual where not exists (select * from TD_SM_TAXCODE_ORGANIZATION where ORG_ID='")
								   .append(subOrgId).append("' and DICTTYPE_ID='").append(dicttypeId)
								   .append("' and DATA_VALUE='" ).append(values[v])
								   .append("' and OPCODE='read'")
								   .append(")) ");
								//System.out.println("----------------------"+addNewData.toString());
								addDB.addBatch(addNewData.toString());
								addNewData.setLength(0);
								uncount ++;
							}
						}
					}else{
						StringBuffer addNewData = new StringBuffer();
						addNewData.append("insert into TD_SM_TAXCODE_ORGANIZATION(ORG_ID,DICTTYPE_ID,DATA_VALUE,OPCODE,DATA_NAME) ")
						   .append("(select '").append(subOrgId).append("' as ORG_ID, '").append(dicttypeId)
						   .append("' as DICTTYPE_ID, '").append(dataValues[i]).append("' as DATA_VALUE, 'read' as OPCODE, '")
						   .append(dataNames[i]).append("' as DATA_NAME from ")
						   .append("dual where not exists (select * from TD_SM_TAXCODE_ORGANIZATION where ORG_ID='")
						   .append(subOrgId).append("' and DICTTYPE_ID='").append(dicttypeId)
						   .append("' and DATA_VALUE='" ).append(dataValues[i])
						   .append("' and OPCODE='read'")
						   .append(")) ");
						//System.out.println("----------------------"+addNewData.toString());
						addDB.addBatch(addNewData.toString());
						addNewData.setLength(0);
						uncount ++;
					}
					if(uncount > 900){
						addDB.executeBatch();
						uncount = 0;
					}
				}
			}
			if(uncount > 0){
				addDB.executeBatch();
			}
			tm.commit();
			//发事件
			Event event = new EventImpl(dicttypeId,DictionaryChangeEvent.DICTIONARY_USERORG_CHANGE_READ);
			super.change(event,true);
		}catch(Exception e){
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			throw new ManagerException(e);
		}
            

	}

	/* 
	 *  针对 翻页处理的 
	 * (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#storeUsualOrgTaxcode(java.lang.String, java.lang.String, java.lang.String[], java.lang.String)
	 * 保存机构编码关系 常用
	 */
	public void storeUsualOrgTaxcode(String orgId, String dicttypeId, String[] dictdataValues, 
			String[] unselected_dictdataValue) throws ManagerException {
		if(strIsNull(orgId) || strIsNull(dicttypeId) ) return ;
		DBUtil db = new DBUtil();
		DBUtil addDB = new DBUtil();
		TransactionManager tm = new TransactionManager();
		Map isExistTaxcodeMap = isExistTaxcode(orgId,dicttypeId,"usual");//已经设置为常用的
		try{
			//保存新数据
			//保存当前机构的
			String[] dataValues = new String[dictdataValues.length];//保存数据项的值 数组
			String[] dataNames = new String[dictdataValues.length];//保存数据项的名称 数组
			String[] isDeploys = new String[dictdataValues.length];//树形数据项是否展开 数组--false没有被展开即下级数据没有读取出来--true展开
			for(int i=0;i<dictdataValues.length;i++){
				String dictdataValue = dictdataValues[i];
				if(dictdataValue.trim().length()==0) continue;
				//dictdataValue value:name;
				String dictdataValuesa[] = dictdataValue.split(":");
				if(dictdataValuesa.length>=3){
					dataValues[i] = dictdataValuesa[0];
					dataNames[i] = dictdataValuesa[1];
					isDeploys[i] = dictdataValuesa[2];
				}else{
					dataValues[i] = "";
					dataNames[i] = "";
					isDeploys[i] = "";
				}
			}

			//保存新数据 未被 被选中的
			//保存当前机构的 '','',''....
//			(1)删除
//			删除本身 多于的"常用"权限
//			String un_dataValues = "";//保存数据项的值 数组		

			int count = 0;
			tm.begin();
			for(int i=0;i<unselected_dictdataValue.length ;i++){
				String dictdataValue = unselected_dictdataValue[i];
				String valueI = "";
				String isDeploy = "";
				if(dictdataValue.trim().length()==0) continue;
				String dictdataValuesa[] = dictdataValue.split(":");
				if(dictdataValuesa.length>=3){
					valueI = dictdataValuesa[0];	
					isDeploy = dictdataValuesa[2];
				}else{
					valueI = "";		
					isDeploy = "";
				}
				StringBuffer delete_subandself_read = new StringBuffer();
				//如果该未选中数据项为已经授权的，用户取消授权且下级未展开，递归删除所有下级数据项
				if(isExistTaxcodeMap.get(valueI) != null && isDeploy.equals("false")){
					String[] values = getSunUsualValue(orgId,dicttypeId,valueI);
					if(values != null){
						for(int v = 0; v < values.length; v++){
							delete_subandself_read.append("delete td_sm_taxcode_organization where DICTTYPE_ID='")
								.append(dicttypeId).append("' and opcode='usual' ")
								.append(" and data_value = '").append(values[v]).append("' ")
								.append(" and ORG_ID ='").append(orgId).append("' ");
							db.addBatch(delete_subandself_read.toString());
							delete_subandself_read.setLength(0);
							count ++;
						}
					}
				}else{
					delete_subandself_read.append("delete td_sm_taxcode_organization where DICTTYPE_ID='")
						.append(dicttypeId).append("' and opcode='usual' ")
						.append(" and data_value = '").append(valueI).append("' ")
						.append(" and ORG_ID ='").append(orgId).append("' ");
					db.addBatch(delete_subandself_read.toString());
					count ++;
				}

				delete_subandself_read.setLength(0);
				if(count > 900){
					db.executeBatch();
					count = 0;
				}

			}
			db.executeBatch();			
			//(1)删除
			//删除本身 多于的"常用"权限
//			if(!this.strIsNull(un_dataValues)){//有 没有被选中的
//				StringBuffer delete_subandself_read = new StringBuffer();
//				delete_subandself_read.append("delete td_sm_taxcode_organization where DICTTYPE_ID='")
//					.append(dicttypeId).append("' and opcode='usual' ")
//					.append(" and data_value in (").append(un_dataValues).append(") ")
//					.append(" and ORG_ID ='").append(orgId).append("' "); 
//				//加到批处理
//				db.addBatch(delete_subandself_read.toString());
//				db.executeBatch();
//			}



			//(2)保存/更新			
			//不用 保存到 常用维护 和上级机构无关
			int uncount = 0;
			for(int i=0;i<dataValues.length;i++){
				if(this.strIsNull(dataValues[i])) continue;
				//如果选中数据项以前为未设置的权限，并且下级没有展开
				if(isExistTaxcodeMap.get(dataValues[i]) == null && isDeploys[i].equals("false")){
					String[] values = getSunUsualValue(orgId,dicttypeId,dataValues[i]);
					StringBuffer addNewData = new StringBuffer();
					if(values != null){
						for(int v = 0; v < values.length; v++){

							addNewData.append("insert into TD_SM_TAXCODE_ORGANIZATION(ORG_ID,DICTTYPE_ID,DATA_VALUE,OPCODE,DATA_NAME) ")
							   .append("(select '").append(orgId).append("' as ORG_ID, '").append(dicttypeId)
							   .append("' as DICTTYPE_ID, '").append(values[v]).append("' as DATA_VALUE, 'usual' as OPCODE, '")
							   .append(dataNames[i]).append("' as DATA_NAME from ")
							   .append("dual where not exists (select * from TD_SM_TAXCODE_ORGANIZATION where ORG_ID='")
							   .append(orgId).append("' and DICTTYPE_ID='").append(dicttypeId)
							   .append("' and DATA_VALUE='" ).append(values[v])
							   .append("' and OPCODE='usual'")
							   .append(")) ");
							addDB.addBatch(addNewData.toString());
							addNewData.setLength(0);
							uncount ++;
						}
					}

				}
				else
				{
					StringBuffer addNewData = new StringBuffer();
					addNewData.append("insert into TD_SM_TAXCODE_ORGANIZATION(ORG_ID,DICTTYPE_ID,DATA_VALUE,OPCODE,DATA_NAME) ")
					   .append("(select '").append(orgId).append("' as ORG_ID, '").append(dicttypeId)
					   .append("' as DICTTYPE_ID, '").append(dataValues[i]).append("' as DATA_VALUE, 'usual' as OPCODE, '")
					   .append(dataNames[i]).append("' as DATA_NAME from ")
					   .append("dual where not exists (select * from TD_SM_TAXCODE_ORGANIZATION where ORG_ID='")
					   .append(orgId).append("' and DICTTYPE_ID='").append(dicttypeId)
					   .append("' and DATA_VALUE='" ).append(dataValues[i])
					   .append("' and OPCODE='usual'")
					   .append(")) ");
					addDB.addBatch(addNewData.toString());
					addNewData.setLength(0);
					uncount ++;
				}

				if(uncount > 900){
					addDB.executeBatch();
					uncount = 0;
				}
			}

			addDB.executeBatch();
			tm.commit();			
            //发事件
			Event event = new EventImpl(dicttypeId,DictionaryChangeEvent.DICTIONARY_USERORG_CHANGE_READ);
			super.change(event,true);

		}catch(Exception e){
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new ManagerException(e);
		}

	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getDictDataPropertyValue(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * 获取字典数据的属性值
	 */
	public String getDictDataPropertyValue(String dicttypeId, String dictdataValue, String dictdataName, String property) {
		if(strIsNull(dicttypeId) || strIsNull(dictdataValue) || strIsNull(property) ) return "";
		DBUtil db = new DBUtil();

		//根据字典类型ID,获取字典类型对象
		Data dicttype = null;
		try {
			dicttype = getDicttypeById(dicttypeId);
			if(dicttype != null){
				String data_dbName = dicttype.getDataDBName();
				String data_table_name = dicttype.getDataTableName();
				String data_name_filed = dicttype.getDataNameField();
				String data_value_field = dicttype.getDataValueField();
				StringBuffer sql = new StringBuffer();	
				ColumnMetaData columnObj = DBUtil.getColumnMetaData(data_dbName,data_table_name,property);
				if("date".equalsIgnoreCase(columnObj.getTypeName())){
					sql.append("select to_char('").append(property).append("', 'yyyy-mm-dd hh24:mi:ss') as ")
					.append(property);
				}else if("long".equalsIgnoreCase(columnObj.getTypeName()) || "number".equalsIgnoreCase(columnObj.getTypeName())){
					sql.append("select to_char(").append(property).append(") as ").append(property);
				}else{
					sql.append("select ").append(property);
				}
				sql.append(" from ").append(data_table_name)
				.append(" where ").append(data_name_filed).append("='").append(dictdataName).append("' ")
				.append(" and ").append(data_value_field).append("='").append(dictdataValue).append("' ");
				//执行
				db.executeSelect(data_dbName,sql.toString());
				sql.setLength(0);
				if(db.size()>0){
					return db.getString(0,property);
				}				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		//数据保存字段:

		return "";
	}

	/* (non-Javadoc)
	 * public String getDictDataPropertyValue(String dicttypeId, String dictdataValue, String dictdataName, String property)
	 * 重载上面方法，当name与value为空时以上方法存在问题
	 * @see com.frameworkset.platform.dictionary.DictManager#getDictDataPropertyValue(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * 获取字典数据的属性值
	 */
	public String getDictDataPropertyValue(String dicttypeId, String dictdataValue, String dictdataName, String property,String primaryCondition) {
		DBUtil db = new DBUtil();
		if(property == null || property.trim().equals(""))
		{
			return "";
		}
		//根据字典类型ID,获取字典类型对象
		Data dicttype = null;
		try {
			dicttype = getDicttypeById(dicttypeId);
			if(dicttype != null){
				String data_dbName = dicttype.getDataDBName();
				String data_table_name = dicttype.getDataTableName();
				String data_name_filed = dicttype.getDataNameField();
				String data_value_field = dicttype.getDataValueField();
				StringBuffer sql = new StringBuffer();	
				ColumnMetaData columnObj = DBUtil.getColumnMetaData(data_dbName,data_table_name,property);
				if("date".equalsIgnoreCase(columnObj.getTypeName())){
					sql.append("select to_char('").append(property).append("', 'yyyy-mm-dd hh24:mi:ss') as ")
					.append(property);
				}else if("long".equalsIgnoreCase(columnObj.getTypeName()) || "number".equalsIgnoreCase(columnObj.getTypeName())){
					sql.append("select to_char(").append(property).append(") as ").append(property);
				}else{
					sql.append("select ").append(property);
				}
				sql.append(" from ").append(data_table_name).append(" a where ");
				if(dictdataName == null || "".equals(dictdataName)){
					sql.append(" (").append(data_name_filed).append("='").append(dictdataName).append("' or ")
						.append(data_name_filed).append(" is null) ");
				}else{
					sql.append(data_name_filed).append("='").append(dictdataName).append("' ");
				}
				if(dictdataValue == null || dictdataValue.equals("")){
					sql.append(" and (").append(data_value_field).append("='")
						.append(dictdataValue).append("' or ").append(data_value_field)
						.append(" is null) ");
				}else{
					sql.append(" and ").append(data_value_field).append("='")
						.append(dictdataValue).append("' ");
				}
				sql.append(primaryCondition);
//				sql.append(data_name_filed).append("='").append(dictdataName).append("' ")
//				.append(" and ").append(data_value_field).append("='").append(dictdataValue).append("' ");
				//执行
				db.executeSelect(data_dbName,sql.toString());
				sql.setLength(0);
				if(db.size()>0){
					return db.getString(0,property);
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		//数据保存字段:

		return "";
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#checkDictDataPropertyValueUnique(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * 验证字典数据的属性的唯一性
	 */
	public String checkDictDataPropertyValueUnique(String dicttypeId,  String property, String value) {
		if(strIsNull(dicttypeId) || strIsNull(property) || strIsNull(value) ) return "";
		DBUtil db = new DBUtil();

		//根据字典类型ID,获取字典类型对象
		Data dicttype = null;
		try {
			dicttype = getDicttypeById(dicttypeId);
			if(dicttype != null){
				String data_dbName = dicttype.getDataDBName();
				String data_table_name = dicttype.getDataTableName();
				StringBuffer sql = new StringBuffer();	
				ColumnMetaData columnObj = DBUtil.getColumnMetaData(data_dbName,data_table_name,property);
				sql.append("select count(*) as num from ").append(data_table_name)
				.append(" where 1=1 ");
				if("date".equalsIgnoreCase(columnObj.getTypeName())){
					sql.append(" and " ).append(property).append("=")
					.append("to_date('").append(value).append("', 'yyyy-mm-dd hh24:mi:ss')  ");
				}else if("long".equalsIgnoreCase(columnObj.getTypeName()) || "number".equalsIgnoreCase(columnObj.getTypeName())){
					sql.append(" and  to_char(").append(property).append(") = '").append(value).append("' ");
				}else{
					sql.append(" and ").append(property).append("='").append(value).append("' ");
				}
				//执行
				db.executeSelect(data_dbName,sql.toString());
				sql.setLength(0);
				if(db.size()>0 && db.getInt(0,"num")>0){//值已经存在
					return "repeat";
				}				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		//数据保存字段:

		return "";
	}

	/**
	 * 部门管理员登陆能看见的机构授权编码维护列表。分为已设置项列表与未设置项列表 gao.tang 2008.1.4
	 * @param id 字典ID
 	 * @param orgId 根据机构判断已设置项与未设置项
	 * @param showdata 数据项名称:查找数据用
	 * @param identifier 已设置项与未设置项标识：值为sealed时标识为未设置项，值为selected时标识为已设置项
	 * @param offset
	 * @param size
	 * @param userId 用户ID，只有部门管理员才能维护字典数据
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getUserDictdataList(String id, String orgId, String showdata,String identifier, int offset, int size, String userId) throws ManagerException {
		ListInfo listInfo = new ListInfo();
		List list = new ArrayList();
		if(this.strIsNull(id)) return null;
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		String dicttypeid = id;
		dicttype = getDicttypeById(dicttypeid);	
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org = dicttype.getData_create_orgid_field();
		StringBuffer sql = new StringBuffer();
		//平铺,取出所有记录
		if(identifier.equals("sealed")){//未设置项的数据
			sql.append("select * from (select 1 ");			
			if(!this.strIsNull(data_name_filed)){
				sql.append(",t.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",t.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",t.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",org.orgnumber||' '||org.org_name as ").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",t.").append(data_order_field).append(" as orby ");
			}
			//如果设置了机构类型显示机构编码和机构名称
			if(!this.strIsNull(data_org)){
				sql.append(" from ").append(data_table_name).append(" t,TD_SM_TAXCODE_ORGANIZATION tax,td_sm_organization org where 1 = 1 ")
					.append("and org.org_id = t.").append(data_org);
			}else{
				sql.append(" from ").append(data_table_name).append(" t,TD_SM_TAXCODE_ORGANIZATION tax where 1 = 1 ");
			}
			sql.append(" and tax.org_id in (select org_id from td_sm_orgmanager where user_id='").append(userId)
				.append("') ")
				.append("and tax.opcode = 'read'")
				.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
                .append("and tax.data_value = t.").append(data_value_field)
                .append(" and tax.data_name = t.").append(data_name_filed);
			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and ").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}
			sql.append(" minus  select 1 ");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",t.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",t.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",t.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",org.orgnumber||' '||org.org_name as ").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",t.").append(data_order_field).append(" as orby ");
			}
//			如果设置了机构类型显示机构编码和机构名称
			if(!this.strIsNull(data_org)){
				sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax,td_sm_organization org, ").append(data_table_name).append(" t ")
				.append("where tax.data_value = t.").append(data_value_field)
				.append(" and org.org_id = t.").append(data_org);
			}else{
				sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" t ")
					.append("where tax.data_value = t.").append(data_value_field);
			}
			sql.append(" and tax.opcode = 'read' and tax.data_name = t.")
				.append(data_name_filed)
				.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
				.append("and tax.org_id = '").append(orgId).append("') o where 1=1 ");
		}else{//已设置项数据
			sql.append("select 1");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",o.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",o.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",o.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",o.").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",o.orby").append(" as orby ");
			}
			sql.append(" from (select 1 ");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",t.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",t.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",t.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",org.orgnumber||' '||org.org_name as ").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",t.").append(data_order_field).append(" as orby ");
			}
			if(!this.strIsNull(data_org)){
				sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax,td_sm_organization org, ").append(data_table_name).append(" t ")
				.append("where tax.data_value = t.").append(data_value_field)
				.append(" and org.org_id = t.").append(data_org);
			}else{
				sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" t ")
				.append("where tax.data_value = t.").append(data_value_field);
			}
			sql.append(" and tax.opcode = 'read' and ")
				.append("tax.data_name = t.").append(data_name_filed)
				.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
				.append("and tax.org_id in (select org_id from td_sm_orgmanager where user_id='").append(userId)
				.append("')) oo, ");
			sql.append("(select 1 ");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",t.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",t.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",t.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",org.orgnumber||' '||org.org_name as ").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",t.").append(data_order_field).append(" as orby ");
			}
			if(!this.strIsNull(data_org)){
				sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, td_sm_organization org,").append(data_table_name).append(" t ")
					.append("where tax.data_value = t.").append(data_value_field)
					.append(" and org.org_id = t.").append(data_org);
			}else{
				sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" t ")
					.append("where tax.data_value = t.").append(data_value_field);
			}
			sql.append(" and tax.opcode = 'read' and ")
				.append("tax.data_name = t.").append(data_name_filed)
				.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
				.append("and tax.org_id ='").append(orgId).append("') o where 1=1 and o.").append(data_name_filed)
				.append("=oo.").append(data_name_filed).append(" and o.").append(data_value_field)
				.append("=oo.").append(data_value_field);
		}

		if(!"".equals(showdata)){
			sql.append(" and o.").append(data_name_filed).append(" like ")
				.append("'%").append(showdata).append("%'");
		}

		//判断是否有排序字段 没有排序字段 就按值排序
		if(!this.strIsNull(data_order_field)){
			if(identifier.equals("sealed")){
				sql.append(" order by orby");
			}else{
				sql.append(" order by o.orby");
			}
		}else{
			if(!this.strIsNull(data_value_field)){
				sql.append(" order by o.").append(data_value_field);
			}
		}

		try {			
			dbUtil.executeSelect(data_dbName,sql.toString(),offset,size);
			for(int i= 0;i < dbUtil.size();i++){
				Item dictdata = new Item();
				dictdata.setName(dbUtil.getString(i,data_name_filed)==null?"":dbUtil.getString(i,data_name_filed));
				dictdata.setValue(dbUtil.getString(i,data_value_field)==null?"":dbUtil.getString(i,data_value_field));			
				dictdata.setDataId(dicttypeid);
				dictdata.setFlag(false);
				if(!this.strIsNull(data_org)){
					dictdata.setDataOrg(dbUtil.getString(i,data_org));
				}
				if(!this.strIsNull(data_validate_field)){
					dictdata.setDataValidate(dbUtil.getInt(i,data_validate_field));
				}else{
					dictdata.setDataValidate(1);
				}
				list.add(dictdata);				
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		}catch(Exception e){
			throw new ManagerException(e.getMessage());
		}
		return listInfo;		
	}

	/**
	 * 授权编码列表未设置项选中保存操作 gao.tang 2008.1.5
	 * @param orgId 授予机构ID
	 * @param dicttypeId 字典类型ID
	 * @param dictdataValues 字典数据项的名称与值
	 */
	public void storeReadOrgTaxcode(String orgId, String dicttypeId, String[] dictdataValues ){
		if(strIsNull(orgId) || strIsNull(dicttypeId) || dictdataValues[0].equals("") ) return ;
		//上级机构sql
		StringBuffer orgs_parent_sql = new StringBuffer()
			.append("select org.org_id from td_sm_organization org start with org.org_id='")
			.append(orgId).append("' connect by prior org.parent_id=org.org_id ");
		DBUtil parentDB = new DBUtil();
		DBUtil db = new DBUtil();
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			parentDB.executeSelect(orgs_parent_sql.toString());
			StringBuffer insertRead_sql = new StringBuffer();
			for(int p = 0; p < parentDB.size(); p++){
				String parentId = parentDB.getString(p, "org_id");
				for(int i = 0; i < dictdataValues.length; i++){
					String dictdataValue = dictdataValues[i];
					String[] name_value = dictdataValue.split(":");
					String dataName = "";
					String dataValue = "";
					if(name_value.length>=2){
						dataName = name_value[1];
						dataValue = name_value[0];
					}
					insertRead_sql.append("insert all when totalsize <= 0 then into td_sm_taxcode_organization ")
						.append("(org_id, dicttype_id, data_value, opcode, data_name) ")
						.append("values('").append(parentId).append("','").append(dicttypeId).append("','")
						.append(dataValue).append("','read','").append(dataName).append("') ")
						.append("select count(org_id) totalsize ")
						.append("from td_sm_taxcode_organization where org_id='").append(parentId).append("' ")
						.append("and dicttype_id = '").append(dicttypeId).append("' and data_value='")
						.append(dataValue).append("' and opcode = 'read' ");
					db.addBatch(insertRead_sql.toString());
					insertRead_sql.setLength(0);
					if(i==900){
						db.executeBatch();
					}
				} 
			}
			db.executeBatch();
			tm.commit();
			//发事件
			Event event = new EventImpl(dicttypeId,DictionaryChangeEvent.DICTIONARY_USERORG_CHANGE_READ);
			super.change(event,true);
		} catch (SQLException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 授权编码列表得到全部的未设置项和全部的已设置项
	 * @param id	字典ID
	 * @param orgId	机构ID
	 * @param identifier	已设置项与未设置项标识：值为sealed时标识为未设置项，值为selected时标识为已设置项	
	 * @param isAdmin	用户是否拥有超级管理员角色 
	 * @param userId	isAdmin为false，userId不为空
	 * @return
	 * @throws ManagerException 
	 */
	public String[] getDictdataValues(String id, String orgId, String identifier, boolean isAdmin, String userId) throws ManagerException{
		String[] dictdataValues = null;

		if(this.strIsNull(id)) return null;
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		String dicttypeid = id;
		dicttype = getDicttypeById(dicttypeid);	
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org = dicttype.getData_create_orgid_field();

		StringBuffer sql = new StringBuffer();
		if(isAdmin){
			if(identifier.equals("sealed")){//未设置项的数据
				sql.append("select * from (select 1 ");			
				if(!this.strIsNull(data_name_filed)){
					sql.append(",t.").append(data_name_filed);
				}
				if(!this.strIsNull(data_value_field)){
					sql.append(",t.").append(data_value_field);
				}
				if(!this.strIsNull(data_validate_field)){
					sql.append(",t.").append(data_validate_field);
				}
				if(!this.strIsNull(data_org)){
					sql.append(",org.orgnumber||' '||org.org_name as ").append(data_org);
				}
				if(!this.strIsNull(data_order_field)){
					sql.append(",t.").append(data_order_field).append(" as orby ");
				}
				if(!this.strIsNull(data_org)){
					sql.append(" from td_sm_organization org,").append(data_table_name).append(" t where 1 = 1 ")
						.append(" and org.org_id=t.").append(data_org);
				}else{
					sql.append(" from ").append(data_table_name).append(" t where 1 = 1 ");
				}
				if(!this.strIsNull(data_org)){
					sql.append(" and t.").append(data_org).append(" in (select org_id from  td_sm_organization start with ")
						.append("org_id='").append(orgId).append("' connect by prior parent_id = org_id) ");
				}
				//判断是否有类型ID字段
				if(!this.strIsNull(data_typeid_field)){
					sql.append(" and t.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
				}
				sql.append(" minus  select 1 ");
				if(!this.strIsNull(data_name_filed)){
					sql.append(",t.").append(data_name_filed);
				}
				if(!this.strIsNull(data_value_field)){
					sql.append(",t.").append(data_value_field);
				}
				if(!this.strIsNull(data_validate_field)){
					sql.append(",t.").append(data_validate_field);
				}
				if(!this.strIsNull(data_org)){
					sql.append(",org.orgnumber||' '||org.org_name as ").append(data_org);
				}
				if(!this.strIsNull(data_order_field)){
					sql.append(",t.").append(data_order_field).append(" as orby ");
				}
				if(!this.strIsNull(data_org)){
				sql.append(" from td_sm_organization org,TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" t ")
					.append("where tax.data_value = t.").append(data_value_field)
					.append(" and org.org_id = t.").append(data_org);
				}else{
					sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" t ")
					.append("where tax.data_value = t.").append(data_value_field);
				}
				sql.append(" and tax.opcode = 'read' and tax.data_name = t.")
					.append(data_name_filed)
					.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
					.append("and tax.org_id = '").append(orgId).append("') o where 1=1 ");
			}else{//已设置项数据
				sql.append("select 1 ");
				if(!this.strIsNull(data_name_filed)){
					sql.append(",o.").append(data_name_filed);
				}
				if(!this.strIsNull(data_value_field)){
					sql.append(",o.").append(data_value_field);
				}
				if(!this.strIsNull(data_validate_field)){
					sql.append(",o.").append(data_validate_field);
				}
				if(!this.strIsNull(data_org)){
					sql.append(",o.").append(data_org);
				}
				if(!this.strIsNull(data_order_field)){
					sql.append(",o.").append(data_order_field).append(" as orby ");
				}
				sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" o ")
					.append("where tax.data_value = o.").append(data_value_field)
					.append(" and tax.opcode = 'read' and ")
					.append("tax.data_name = o.").append(data_name_filed)
					.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
					.append("and tax.org_id = '").append(orgId).append("' ");
			}
			//判断是否有排序字段 没有排序字段 就按值排序
			if(!this.strIsNull(data_order_field)){
				sql.append(" order by orby");
			}else{
				if(!this.strIsNull(data_value_field)){
					sql.append(" order by o.").append(data_value_field);
				}
			}
		}else{
			//平铺,取出所有记录
			if(identifier.equals("sealed")){//未设置项的数据
				sql.append("select * from (select 1 ");			
				if(!this.strIsNull(data_name_filed)){
					sql.append(",t.").append(data_name_filed);
				}
				if(!this.strIsNull(data_value_field)){
					sql.append(",t.").append(data_value_field);
				}
				if(!this.strIsNull(data_validate_field)){
					sql.append(",t.").append(data_validate_field);
				}
				if(!this.strIsNull(data_org)){
					sql.append(",t.").append(data_org);
				}
				if(!this.strIsNull(data_order_field)){
					sql.append(",t.").append(data_order_field).append(" as orby ");
				}
				sql.append(" from ").append(data_table_name).append(" t,TD_SM_TAXCODE_ORGANIZATION tax where 1 = 1 ")
					.append("and tax.org_id in (select org_id from td_sm_orgmanager where user_id='").append(userId)
					.append("') ")
					.append("and tax.opcode = 'read'")
					.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
	                .append("and tax.data_value = t.").append(data_value_field)
	                .append(" and tax.data_name = t.").append(data_name_filed);
				//判断是否有类型ID字段
				if(!this.strIsNull(data_typeid_field)){
					sql.append(" and ").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
				}
				sql.append(" minus  select 1 ");
				if(!this.strIsNull(data_name_filed)){
					sql.append(",t.").append(data_name_filed);
				}
				if(!this.strIsNull(data_value_field)){
					sql.append(",t.").append(data_value_field);
				}
				if(!this.strIsNull(data_validate_field)){
					sql.append(",t.").append(data_validate_field);
				}
				if(!this.strIsNull(data_org)){
					sql.append(",t.").append(data_org);
				}
				if(!this.strIsNull(data_order_field)){
					sql.append(",t.").append(data_order_field).append(" as orby ");
				}
				sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" t ")
					.append("where tax.data_value = t.").append(data_value_field)
					.append(" and tax.opcode = 'read' and tax.data_name = t.")
					.append(data_name_filed)
					.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
					.append("and tax.org_id = '").append(orgId).append("') o where 1=1 ");
			}else{//已设置项数据
				sql.append("select 1");
				if(!this.strIsNull(data_name_filed)){
					sql.append(",o.").append(data_name_filed);
				}
				if(!this.strIsNull(data_value_field)){
					sql.append(",o.").append(data_value_field);
				}
				if(!this.strIsNull(data_validate_field)){
					sql.append(",o.").append(data_validate_field);
				}
				if(!this.strIsNull(data_org)){
					sql.append(",o.").append(data_org);
				}
				if(!this.strIsNull(data_order_field)){
					sql.append(",o.orby").append(" as orby ");
				}
				sql.append(" from (select 1 ");
				if(!this.strIsNull(data_name_filed)){
					sql.append(",t.").append(data_name_filed);
				}
				if(!this.strIsNull(data_value_field)){
					sql.append(",t.").append(data_value_field);
				}
				if(!this.strIsNull(data_validate_field)){
					sql.append(",t.").append(data_validate_field);
				}
				if(!this.strIsNull(data_org)){
					sql.append(",t.").append(data_org);
				}
				if(!this.strIsNull(data_order_field)){
					sql.append(",t.").append(data_order_field).append(" as orby ");
				}
				sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" t ")
					.append("where tax.data_value = t.").append(data_value_field)
					.append(" and tax.opcode = 'read' and ")
					.append("tax.data_name = t.").append(data_name_filed)
					.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
					.append("and tax.org_id in (select org_id from td_sm_orgmanager where user_id='").append(userId)
					.append("')) oo, ");
				sql.append("(select 1 ");
				if(!this.strIsNull(data_name_filed)){
					sql.append(",t.").append(data_name_filed);
				}
				if(!this.strIsNull(data_value_field)){
					sql.append(",t.").append(data_value_field);
				}
				if(!this.strIsNull(data_validate_field)){
					sql.append(",t.").append(data_validate_field);
				}
				if(!this.strIsNull(data_org)){
					sql.append(",t.").append(data_org);
				}
				if(!this.strIsNull(data_order_field)){
					sql.append(",t.").append(data_order_field).append(" as orby ");
				}
				sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" t ")
					.append("where tax.data_value = t.").append(data_value_field)
					.append(" and tax.opcode = 'read' and ")
					.append("tax.data_name = t.").append(data_name_filed)
					.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
					.append("and tax.org_id ='").append(orgId).append("') o where 1=1 and o.").append(data_name_filed)
					.append("=oo.").append(data_name_filed).append(" and o.").append(data_value_field)
					.append("=oo.").append(data_value_field);
			}
			//判断是否有排序字段 没有排序字段 就按值排序
			if(!this.strIsNull(data_order_field)){
				if(identifier.equals("sealed")){
					sql.append(" order by orby");
				}else{
					sql.append(" order by o.orby");
				}
			}else{
				if(!this.strIsNull(data_value_field)){
					sql.append(" order by o.").append(data_value_field);
				}
			}
		}
		try {
			dbUtil.executeSelect(data_dbName,sql.toString());
			dictdataValues = new String[dbUtil.size()];
			for(int i = 0; i < dbUtil.size(); i++){
				String value = dbUtil.getString(i, data_value_field);
				String name = dbUtil.getString(i, data_name_filed);
				if(!this.strIsNull(data_org)){
					String orgData = dbUtil.getString(i, data_org);
					dictdataValues[i] = value + ":" + name + ":" + orgData;
				}else{
					dictdataValues[i] = value + ":" + name;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dictdataValues;
	}

	/**
	 * 列表已设置项选中删除操作 gao.tang 2008.1.5
	 * @param orgId 删除机构ID
	 * @param dicttypeId 字典类型ID
	 * @param dictdataValues 字典数据项的名称与值
	 */
	public void deleteReadOrgTaxcode(String orgId, String dicttypeId, String[] dictdataValues ){
		if(strIsNull(orgId) || strIsNull(dicttypeId) || dictdataValues[0].equals("") ) return ;
		//下级机构sql
		StringBuffer orgs_child_sql = new StringBuffer()
			.append("select org.org_id from td_sm_organization org start with org.org_id='")
			.append(orgId).append("' connect by prior org.org_id=org.parent_id ");
		DBUtil db = new DBUtil();
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			StringBuffer delete_subandself_read_usual = new StringBuffer();
			for(int i = 0; i < dictdataValues.length; i++){
				String dictdataValue = dictdataValues[i];
				String[] name_value = dictdataValue.split(":");
				String dataName = "";
				String dataValue = "";
				if(name_value.length>=2){
					dataName = name_value[1];
					dataValue = name_value[0];
				}

				delete_subandself_read_usual.append("delete td_sm_taxcode_organization where DICTTYPE_ID='")
					.append(dicttypeId).append("' and  opcode in ('read','usual') ")
					.append(" and data_value = '").append(dataValue).append("' ")
					.append(" and data_name = '").append(dataName).append("' ")
					.append(" and ORG_ID in (").append(orgs_child_sql.toString()).append(") "); 
				//加到批处理
				db.addBatch(delete_subandself_read_usual.toString());
				delete_subandself_read_usual.setLength(0);
				if(i==900){
					db.executeBatch();
				}
			} 

			db.executeBatch();
			tm.commit();
			//发事件
			Event event = new EventImpl(dicttypeId,DictionaryChangeEvent.DICTIONARY_USERORG_CHANGE_READ);
			super.change(event,true);
		} catch (SQLException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 得到常用编码的全部的未设置项和全部的已设置项
	 * @param dicttypeId
	 * @param showdata
	 * @param orgId
	 * @param identifier 已设置项与未设置项标识：值为sealed时标识为未设置项，值为selected时标识为已设置项	
	 * @param offset
	 * @param size
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getUsualDictdataList(String dicttypeId, String showdata, 
			String orgId, String identifier, int offset, int size) throws ManagerException{
		ListInfo listInfo = new ListInfo();
		List list = new ArrayList();
		if(this.strIsNull(dicttypeId)) return null;
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		String dicttypeid = dicttypeId;
		dicttype = getDicttypeById(dicttypeid);	
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org = dicttype.getData_create_orgid_field();
		StringBuffer sql = new StringBuffer();

		//平铺,取出所有记录
		if(identifier.equals("sealed")){//未设置项的数据
			sql.append("select * from (select 1 ");			
			if(!this.strIsNull(data_name_filed)){
				sql.append(",").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",").append(data_order_field).append(" as orby ");
			}
			sql.append(" from ").append(data_table_name).append(" where 1 = 1 ");
			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and ").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}
			sql.append(" minus  select 1 ");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",t.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",t.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",t.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",t.").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",t.").append(data_order_field).append(" as orby ");
			}
			sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" t ")
				.append("where tax.data_value = t.").append(data_value_field)
				.append(" and tax.opcode = 'usual' and ")
				.append("tax.data_name = t.").append(data_name_filed)
				.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
				.append("and tax.org_id = '").append(orgId).append("') o where 1=1 ");
		}else{//已设置项数据
			sql.append("select 1 ");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",o.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",o.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",o.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",o.").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",o.").append(data_order_field).append(" as orby ");
			}
			sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" o ")
				.append("where tax.data_value = o.").append(data_value_field)
				.append(" and tax.opcode = 'usual' and ")
				.append("tax.data_name = o.").append(data_name_filed)
				.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
				.append("and tax.org_id = '").append(orgId).append("' ");
		}

		if(!"".equals(showdata)){
			sql.append(" and o.").append(data_name_filed).append(" like ")
				.append("'%").append(showdata).append("%'");
		}

		//判断是否有排序字段 没有排序字段 就按值排序
		if(!this.strIsNull(data_order_field)){
			sql.append(" order by orby");
		}else{
			if(!this.strIsNull(data_value_field)){
				sql.append(" order by o.").append(data_value_field);
			}
		}
		try {			
			dbUtil.executeSelect(data_dbName,sql.toString(),offset,size);
			for(int i= 0;i < dbUtil.size();i++){
				Item dictdata = new Item();
				dictdata.setName(dbUtil.getString(i,data_name_filed)==null?"":dbUtil.getString(i,data_name_filed));
				dictdata.setValue(dbUtil.getString(i,data_value_field)==null?"":dbUtil.getString(i,data_value_field));

				dictdata.setDataId(dicttypeid);
				dictdata.setFlag(false);
				if(!this.strIsNull(data_org)){
					dictdata.setDataOrg(dbUtil.getString(i,data_org));
				}
				if(!this.strIsNull(data_validate_field)){
					dictdata.setDataValidate(dbUtil.getInt(i,data_validate_field));
				}else{
					dictdata.setDataValidate(1);
				}
				list.add(dictdata);				
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		}catch(Exception e){
			throw new ManagerException(e.getMessage());
		}
		return listInfo;
	}

	/**
	 * 常用编码列表未设置项选中保存操作 gao.tang 2008.1.5
	 * @param orgId 授予机构ID
	 * @param dicttypeId 字典类型ID
	 * @param dictdataValues 字典数据项的名称与值
	 */
	public void store_UsualOrgTaxcode(String orgId, String dicttypeId, String[] dictdataValues ){
		if(strIsNull(orgId) || strIsNull(dicttypeId) || dictdataValues[0].equals("") ) return ;

		DBUtil db = new DBUtil();
		try {
			StringBuffer insertUsual_sql = new StringBuffer();
			for(int i = 0; i < dictdataValues.length; i++){
				String dictdataValue = dictdataValues[i];
				String[] name_value = dictdataValue.split(":");
				String dataName = "";
				String dataValue = "";
				if(name_value.length>=2){
					dataName = name_value[1];
					dataValue = name_value[0];
				}
				insertUsual_sql.append("insert into td_sm_taxcode_organization ")
					.append("(org_id, dicttype_id, data_value, opcode, data_name) ")
					.append("values('").append(orgId).append("','").append(dicttypeId).append("','")
					.append(dataValue).append("','usual','").append(dataName).append("') ");
				db.addBatch(insertUsual_sql.toString());
				insertUsual_sql.setLength(0);
				if(i==900){
					db.executeBatch();
				}
			}
			db.executeBatch();
			//发事件
			Event event = new EventImpl(dicttypeId,DictionaryChangeEvent.DICTIONARY_USERORG_CHANGE_READ);
			super.change(event,true);
		} catch (SQLException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			db.resetBatch();
		}
	}

	/**
	 * 常用编码列表已设置项选中删除操作 gao.tang 2008.1.5
	 * @param orgId 删除机构ID
	 * @param dicttypeId 字典类型ID
	 * @param dictdataValues 字典数据项的名称与值
	 */
	public void delete_UsualOrgTaxcode(String orgId, String dicttypeId, String[] dictdataValues ){
		if(strIsNull(orgId) || strIsNull(dicttypeId) || dictdataValues[0].equals("") ) return ;
		DBUtil db = new DBUtil();
		try {
			StringBuffer delete_usual = new StringBuffer();
			for(int i = 0; i < dictdataValues.length; i++){
				String dictdataValue = dictdataValues[i];
				String[] name_value = dictdataValue.split(":");
//				String dataName = "";
				String dataValue = "";
				if(name_value.length>=2){
//					dataName = name_value[1];
					dataValue = name_value[0];
				}
				delete_usual.append("delete td_sm_taxcode_organization where DICTTYPE_ID='")
					.append(dicttypeId).append("' and  opcode = 'usual' ")
					.append(" and data_value = '").append(dataValue).append("' ")
//					.append(" and data_name = '").append(dataName).append("' ")
					.append(" and ORG_ID = '").append(orgId).append("' "); 
				//加到批处理
				db.addBatch(delete_usual.toString());
				delete_usual.setLength(0);
				if(i==900){
					db.executeBatch();
				}
			} 
			db.executeBatch();
			//发事件
			Event event = new EventImpl(dicttypeId,DictionaryChangeEvent.DICTIONARY_USERORG_CHANGE_READ);
			super.change(event,true);
		} catch (SQLException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			db.resetBatch();
		}
	}

	/**
	 * 得到常用编码的全部的未设置项和全部的已设置项
	 * @param dicttypeId
	 * @param showdata
	 * @param orgId
	 * @param identifier 已设置项与未设置项标识：值为sealed时标识为未设置项，值为selected时标识为已设置项	
	 * @param offset
	 * @param size
	 * @return
	 * @throws ManagerException
	 */
	public String[] getUsualDictdata(String dicttypeId, 
			String orgId, String identifier) throws ManagerException{
		String[] dictdataValues = null;

		if(this.strIsNull(dicttypeId)) return null;
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		String dicttypeid = dicttypeId;
		dicttype = getDicttypeById(dicttypeid);	
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org = dicttype.getData_create_orgid_field();
		StringBuffer sql = new StringBuffer();

		//平铺,取出所有记录
		if(identifier.equals("sealed")){//未设置项的数据
			sql.append("select * from (select 1 ");			
			if(!this.strIsNull(data_name_filed)){
				sql.append(",").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",").append(data_order_field).append(" as orby ");
			}
			sql.append(" from ").append(data_table_name).append(" where 1 = 1 ");
			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and ").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}
			sql.append(" minus  select 1 ");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",t.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",t.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",t.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",t.").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",t.").append(data_order_field).append(" as orby ");
			}
			sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" t ")
				.append("where tax.data_value = t.").append(data_value_field)
				.append(" and tax.opcode = 'usual' and tax.data_name = t.")
				.append(data_name_filed)
				.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
				.append("and tax.org_id = '").append(orgId).append("') o where 1=1 ");
		}else{//已设置项数据
			sql.append("select 1 ");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",o.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",o.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",o.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",o.").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",o.").append(data_order_field).append(" as orby ");
			}
			sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" o ")
				.append("where tax.data_value = o.").append(data_value_field)
				.append(" and tax.opcode = 'usual' and ")
				.append("tax.data_name = o.").append(data_name_filed)
				.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
				.append("and tax.org_id = '").append(orgId).append("' ");
		}
		//判断是否有排序字段 没有排序字段 就按值排序
		if(!this.strIsNull(data_order_field)){
			sql.append(" order by orby");
		}else{
			if(!this.strIsNull(data_value_field)){
				sql.append(" order by o.").append(data_value_field);
			}
		}
		try {			
			dbUtil.executeSelect(data_dbName,sql.toString());
			dictdataValues = new String[dbUtil.size()];
			for(int i = 0; i < dictdataValues.length; i++){
				String value = dbUtil.getString(i, data_value_field);
				String name = dbUtil.getString(i, data_name_filed);
				dictdataValues[i] = value + ":" + name; 
			}
		}catch(Exception e){
			throw new ManagerException(e.getMessage());
		}
		return dictdataValues;
	}

	/**
	 * 在常用编码中过滤未设置的read数据项
	 * @param dicttypeId
	 * @param orgId
	 * @param showdata
	 * @param identifier
	 * @param offset
	 * @param size
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getReadDictdataList(String dicttypeId, String showdata, String orgId, 
			String identifier, int offset, int size) throws ManagerException{
		ListInfo listInfo = new ListInfo();
		List list = new ArrayList();
		if(this.strIsNull(dicttypeId)) return null;
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		String dicttypeid = dicttypeId;
		dicttype = getDicttypeById(dicttypeid);	
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org = dicttype.getData_create_orgid_field();
		StringBuffer sql = new StringBuffer();

		if(identifier.equals("sealed")){//未设置项的数据
			sql.append("select * from (select 1");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",o.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",o.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",o.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",o.").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",o.").append(data_order_field).append(" as orby ");
			}
			sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" o ")
			.append("where tax.data_value = o.").append(data_value_field)
			.append(" and tax.opcode = 'read' and ")
			.append("tax.data_name = o.").append(data_name_filed)
			.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
			.append("and tax.org_id = '").append(orgId).append("' ");
			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and o.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}
			sql.append(" minus select 1");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",o.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",o.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",o.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",o.").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",o.").append(data_order_field).append(" as orby ");
			}
			sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" o ")
			.append("where tax.data_value = o.").append(data_value_field)
			.append(" and tax.opcode = 'usual' and ")
			.append("tax.data_name = o.").append(data_name_filed)
			.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
			.append("and tax.org_id = '").append(orgId).append("' ");
			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and o.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}
			sql.append(") o where 1=1 ");
		}else{//已设置项数据
			sql.append("select 1");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",o.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",o.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",o.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",o.").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",o.").append(data_order_field).append(" as orby ");
			}
			sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" o ")
			.append("where tax.data_value = o.").append(data_value_field)
			.append(" and tax.opcode = 'usual' and ")
			.append("tax.data_name = o.").append(data_name_filed)
			.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
			.append("and tax.org_id = '").append(orgId).append("' ");
			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and o.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}
		}

		if(!"".equals(showdata)){
			sql.append(" and o.").append(data_name_filed).append(" like ")
				.append("'%").append(showdata).append("%'");
		}
		//判断是否有排序字段 没有排序字段 就按值排序
		if(!this.strIsNull(data_order_field)){
			sql.append(" order by orby");
		}else{
			if(!this.strIsNull(data_value_field)){
				sql.append(" order by o.").append(data_value_field);
			}
		}
		try {			
			dbUtil.executeSelect(data_dbName,sql.toString(),offset,size);
			for(int i= 0;i < dbUtil.size();i++){
				Item dictdata = new Item();
				dictdata.setName(dbUtil.getString(i,data_name_filed)==null?"":dbUtil.getString(i,data_name_filed));

				dictdata.setValue(dbUtil.getString(i,data_value_field)==null?"":dbUtil.getString(i,data_value_field));

				dictdata.setDataId(dicttypeid);
				dictdata.setFlag(false);
				if(!this.strIsNull(data_org)){
					dictdata.setDataOrg(dbUtil.getString(i,data_org));
				}
				if(!this.strIsNull(data_validate_field)){
					dictdata.setDataValidate(dbUtil.getInt(i,data_validate_field));
				}else{
					dictdata.setDataValidate(1);
				}
				list.add(dictdata);				
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		}catch(Exception e){
			throw new ManagerException(e.getMessage());
		}
		return listInfo;
	}

	/**
	 * 常用编码中得到已授权read项的未设置usual项
	 * @param dicttypeId
	 * @param orgId
	 * @return
	 * @throws ManagerException
	 */
	public String[] getUsuslSealedReadDictdataValus(String dicttypeId, String orgId) throws ManagerException{
		String[] dictdataValues = null;
		if(this.strIsNull(dicttypeId)) return null;
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		String dicttypeid = dicttypeId;
		dicttype = getDicttypeById(dicttypeid);	
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org = dicttype.getData_create_orgid_field();
		StringBuffer sql = new StringBuffer();

		//未设置项的数据
		sql.append("select 1");
		if(!this.strIsNull(data_name_filed)){
			sql.append(",o.").append(data_name_filed);
		}
		if(!this.strIsNull(data_value_field)){
			sql.append(",o.").append(data_value_field);
		}
		if(!this.strIsNull(data_validate_field)){
			sql.append(",o.").append(data_validate_field);
		}
		if(!this.strIsNull(data_org)){
			sql.append(",o.").append(data_org);
		}
		if(!this.strIsNull(data_order_field)){
			sql.append(",o.").append(data_order_field).append(" as orby ");
		}
		sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" o ")
		.append("where tax.data_value = o.").append(data_value_field)
		.append(" and tax.opcode = 'read' and ")
		.append("tax.data_name = o.").append(data_name_filed)
		.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
		.append("and tax.org_id = '").append(orgId).append("' ");
		//判断是否有类型ID字段
		if(!this.strIsNull(data_typeid_field)){
			sql.append(" and o.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
		}
		sql.append(" minus select 1");
		if(!this.strIsNull(data_name_filed)){
			sql.append(",o.").append(data_name_filed);
		}
		if(!this.strIsNull(data_value_field)){
			sql.append(",o.").append(data_value_field);
		}
		if(!this.strIsNull(data_validate_field)){
			sql.append(",o.").append(data_validate_field);
		}
		if(!this.strIsNull(data_org)){
			sql.append(",o.").append(data_org);
		}
		if(!this.strIsNull(data_order_field)){
			sql.append(",o.").append(data_order_field).append(" as orby ");
		}
		sql.append(" from TD_SM_TAXCODE_ORGANIZATION tax, ").append(data_table_name).append(" o ")
		.append("where tax.data_value = o.").append(data_value_field)
		.append(" and tax.opcode = 'usual' and ")
		.append("tax.data_name = o.").append(data_name_filed)
		.append(" and tax.dicttype_id = '").append(dicttypeid).append("' ")
		.append("and tax.org_id = '").append(orgId).append("' ");
		//判断是否有类型ID字段
		if(!this.strIsNull(data_typeid_field)){
			sql.append(" and o.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
		}
		try {
			dbUtil.executeSelect(data_dbName,sql.toString());
			dictdataValues = new String[dbUtil.size()];
			for(int i = 0; i < dictdataValues.length; i++){
				String value = dbUtil.getString(i, data_value_field);
				String name = dbUtil.getString(i, data_name_filed);
				dictdataValues[i] = value + ":" + name; 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dictdataValues;
	}

	/**
	 * 根据字典表名称得到所有配置了该表的字典类型名称
	 * @param dictdataTablename
	 * @return
	 */
	public String getDicttypeNames(String dictdataTablename){
		String dicttNames = "";
		String sql = "select t.dicttype_name from td_sm_dicttype t where upper(t.data_table_name)='" +dictdataTablename.toUpperCase() +"'";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			for(int i = 0; i < db.size(); i++){
				if(dicttNames.equals("")){
					dicttNames = db.getString(i,"dicttype_name"); 
				}else{
					dicttNames += "," + db.getString(i,"dicttype_name");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dicttNames;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#isDicttypeGatherMustFilter(com.frameworkset.dictionary.Data)
	 * 字典类型的数据采集, 是否要分级授权.
	 * 两个必要条件:
	 *           (1)必须是授权字典
	 *           (2)字典必须配置了"所属机构"属性
	 */
	public boolean isDicttypeGatherMustFilter(Data dicttype) {
		if(dicttype==null) return false;
		//2008-03-27 gao.tang 修改，放出所有类型不能修改
//		if(dicttype.getDicttype_type()== PARTREAD_BUSINESS_DICTTYPE && 
//		  !this.strIsNull(dicttype.getData_create_orgid_field())){
		if(!this.strIsNull(dicttype.getData_create_orgid_field())){
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getOtherDicttypePKColumnByTable(java.lang.String, java.lang.String)
	 * 判断数据库表是否被其他字典类型映射, 如果映射了, 那么其他字典指定的值(主键)字段 是那个字段
	 */
	public String getOtherDicttypePKColumnByTable(String dbName, String tableName) {
		String pkName = "";
		StringBuffer sql = new StringBuffer().append("select t.data_value_field from ")
		.append("td_sm_dicttype t where t.data_dbname=")
		.append("'").append(dbName).append("' ")
		.append("and t.data_table_name=")
		.append("'").append(tableName).append("' ");
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql.toString());
			if(db.size()>0){//有记录, 表示该表已经被其他字典引用, 并且配置了主键, 返回配置的主键字段名称
				pkName = db.getString(0,"data_value_field")==null?"":db.getString(0,"data_value_field");
				return pkName;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pkName;
	}

	/**
	 * 在授权业务字典中，选择用户所属机构时显示用户所属机构的已设置项与未设置项的数据项
	 * @param CurOrg 当前机构ID
	 * @param dicttypeId 字典类型ID
	 * @param identifier 已设置项与未设置项标识：值为sealed时标识为未设置项，值为selected时标识为已设置项  
	 * @param showDate 数据项查询数据
	 * @param offset 
	 * @param size
	 * @return ListInfo
	 * @author: gao.tang 2008.1.15
	 * @throws ManagerException 
	 */
	public ListInfo getCurOrgGatherDictDataList(String CurOrg, String dicttypeId, 
			String identifier, String showdata, int offset, int size) throws ManagerException {
		ListInfo listInfo = new ListInfo();
		List list = new ArrayList();
		if(this.strIsNull(dicttypeId)) return null;
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		String dicttypeid = dicttypeId;
		dicttype = getDicttypeById(dicttypeid);	
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org = dicttype.getData_create_orgid_field();
		StringBuffer sql = new StringBuffer();
		//如果该字典没有配置机构字段，返回null
		if(this.strIsNull(data_org)){
			return null;
		}
		if(identifier.equals("sealed")){//未设置项的数据
			sql.append("select * from (select 1");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",o.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",o.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",o.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",org.orgnumber").append("||' '||org.org_name as ").append(data_org)
				.append(",org.org_id as orgid");
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",o.").append(data_order_field).append(" as orby ");
			}
			sql.append(" from ").append(data_table_name).append(" o,td_sm_organization org ")
			.append("where o.").append(data_org).append("='").append(CurOrg).append("' ")
			.append(" and org.org_id = o.").append(data_org);
			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and o.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}
			sql.append(" minus select 1");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",o.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",o.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",o.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",org.orgnumber").append("||' '||org.org_name as ").append(data_org)
				.append(",org.org_id as orgid");
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",o.").append(data_order_field).append(" as orby ");
			}
			sql.append(" from ").append(data_table_name).append(" o,TD_SM_TAXCODE_ORGANIZATION tax ,td_sm_organization org ")
			.append("where o.").append(data_org).append("='").append(CurOrg).append("' and tax.org_id = '")
			.append(CurOrg).append("' and tax.dicttype_id = '").append(dicttypeId).append("' and ")
			.append("tax.data_name = o.").append(data_name_filed).append(" and tax.data_value = o.").append(data_value_field)
			.append(" and org.org_id = o.").append(data_org);
			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and o.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}
			sql.append(" and tax.opcode = 'read') o where 1 = 1 ");
		}else{
			sql.append("select 1");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",o.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",o.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",o.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",org.orgnumber").append("||' '||org.org_name as ").append(data_org)
					.append(",org.org_id as orgid");
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",o.").append(data_order_field).append(" as orby ");
			}
			sql.append(" from ").append(data_table_name).append(" o,TD_SM_TAXCODE_ORGANIZATION tax, td_sm_organization org ")
			.append("where " +
					//"o.").append(data_org).append("='").append(CurOrg).append("' and" +
							" tax.org_id = '")
			.append(CurOrg).append("' and tax.dicttype_id = '").append(dicttypeId).append("' and ")
			.append("tax.data_name = o.").append(data_name_filed).append(" and tax.data_value = o.").append(data_value_field)
			.append(" and tax.opcode = 'read' ")
			.append("and org.org_id = o.").append(data_org);
			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and o.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}
		}
		if(!"".equals(showdata)){
			sql.append(" and o.").append(data_name_filed).append(" like ")
				.append("'%").append(showdata).append("%'");
		}
		//判断是否有排序字段 没有排序字段 就按值排序
		if(!this.strIsNull(data_order_field)){
			sql.append(" order by orby");
		}else{
			if(!this.strIsNull(data_value_field)){
				sql.append(" order by o.").append(data_value_field);
			}
		}
		try {			
			dbUtil.executeSelect(data_dbName,sql.toString(),offset,size);
			for(int i= 0;i < dbUtil.size();i++){
				Item dictdata = new Item();
				dictdata.setName(dbUtil.getString(i,data_name_filed)==null?"":dbUtil.getString(i,data_name_filed));
				dictdata.setValue(dbUtil.getString(i,data_value_field)==null?"":dbUtil.getString(i,data_value_field));

				dictdata.setDataId(dicttypeid);
				dictdata.setFlag(false);
				if(!this.strIsNull(data_org)){
					dictdata.setDataOrg(dbUtil.getString(i,data_org));
					dictdata.setOrgId(dbUtil.getString(i,"orgid"));
				}
				if(!this.strIsNull(data_validate_field)){
					dictdata.setDataValidate(dbUtil.getInt(i,data_validate_field));
				}else{
					dictdata.setDataValidate(1);
				}
				list.add(dictdata);				
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		}catch(Exception e){
			throw new ManagerException(e.getMessage());
		}
		return listInfo;
	}

	/**
	 * 机构授权编码，当字典设置了机构字段-保存数据项向上保存不超过本级机构
	 * @param curOrg 用户所在机构
	 * @param orgId 授权给当前所选机构
	 * @param dicttypeId
	 * @param dictdataValues
	 * @author gao.tang 2008.1.16
	 */
	public void storeReadOrgTaxcode(String curOrg, String orgId, String dicttypeId, String[] dictdataValues ){
		if(strIsNull(orgId) || strIsNull(dicttypeId) || dictdataValues[0].equals("") ) return ;
		//上级机构sql
		StringBuffer orgs_parent_sql = new StringBuffer()
			.append("select a.org_id from (select org.org_id from td_sm_organization org ")
			.append("start with org.org_id = '").append(orgId).append("' ")
			.append("connect by prior org.parent_id = org.org_id) a ")
			.append("join (select org.org_id from td_sm_organization org ")
			.append("start with org.org_id = '").append(curOrg).append("' ")
			.append("connect by prior org.org_id = org.parent_id) b on a.org_id = b.org_id");
		DBUtil parentDB = new DBUtil();
		TransactionManager tm = new TransactionManager();
		DBUtil db = new DBUtil();
		try {
			tm.begin();
			parentDB.executeSelect(orgs_parent_sql.toString());
			StringBuffer insertRead_sql = new StringBuffer();
			for(int p = 0; p < parentDB.size(); p++){
				String parentId = parentDB.getString(p, "org_id");
				for(int i = 0; i < dictdataValues.length; i++){
					String dictdataValue = dictdataValues[i];
					String[] name_value = dictdataValue.split(":");
					String dataName = "";
					String dataValue = "";
					if(name_value.length>=2){
						dataName = name_value[1];
						dataValue = name_value[0];
					}
					insertRead_sql.append("insert all when totalsize <= 0 then into td_sm_taxcode_organization ")
						.append("(org_id, dicttype_id, data_value, opcode, data_name) ")
						.append("values('").append(parentId).append("','").append(dicttypeId).append("','")
						.append(dataValue).append("','read','").append(dataName).append("') ")
						.append("select count(org_id) totalsize ")
						.append("from td_sm_taxcode_organization where org_id='").append(parentId).append("' ")
						.append("and dicttype_id = '").append(dicttypeId).append("' and data_value='")
						.append(dataValue).append("' and opcode = 'read' ");
					db.addBatch(insertRead_sql.toString());
					insertRead_sql.setLength(0);
					if(i==900){
						db.executeBatch();
					}
				} 
			}
			db.executeBatch();
			tm.commit();
			//发事件
			Event event = new EventImpl(dicttypeId,DictionaryChangeEvent.DICTIONARY_USERORG_CHANGE_READ);
			super.change(event,true);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 机构授权编码-选择用户所属机构时得到用户所属机构的所有数据项的已设置项与未设置项 gao.tang 2008.1.16
	 * @param CurOrg
	 * @param dicttypeId
	 * @param identifier
	 * @return
	 * @throws ManagerException
	 */
	public String[] getCurOrgSelfGatherDictDataValues(String CurOrg, String dicttypeId, 
			String identifier) throws ManagerException {
		String[] dictDataValues = null;
		if(this.strIsNull(dicttypeId)) return null;
		DBUtil dbUtil = new DBUtil();
		Data dicttype = new Data();
		String dicttypeid = dicttypeId;
		dicttype = getDicttypeById(dicttypeid);	
		//数据保存字段:
		String data_dbName = dicttype.getDataDBName();
		String data_table_name = dicttype.getDataTableName();
		String data_name_filed = dicttype.getDataNameField();
		String data_value_field = dicttype.getDataValueField();
		String data_order_field	= dicttype.getDataOrderField();
		String data_typeid_field = dicttype.getDataTypeIdField();
		String data_validate_field = dicttype.getData_validate_field();
		String data_org = dicttype.getData_create_orgid_field();
		StringBuffer sql = new StringBuffer();
		if(identifier.equals("sealed")){//未设置项的数据
			sql.append("select * from (select 1");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",o.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",o.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",o.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",org.orgnumber").append("||' '||org.org_name as ").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",o.").append(data_order_field).append(" as orby ");
			}
			sql.append(" from ").append(data_table_name).append(" o,td_sm_organization org ")
			.append("where o.").append(data_org).append("='").append(CurOrg).append("' ")
			.append(" and org.org_id = o.").append(data_org);
			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and o.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}
			sql.append(" minus select 1");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",o.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",o.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",o.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",org.orgnumber").append("||' '||org.org_name as ").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",o.").append(data_order_field).append(" as orby ");
			}
			sql.append(" from ").append(data_table_name).append(" o,TD_SM_TAXCODE_ORGANIZATION tax ,td_sm_organization org ")
			.append("where o.").append(data_org).append("='").append(CurOrg).append("' and tax.org_id = '")
			.append(CurOrg).append("' and tax.dicttype_id = '").append(dicttypeId).append("' and ")
			.append("tax.data_name = o.").append(data_name_filed).append(" and tax.data_value = o.").append(data_value_field)
			.append(" and org.org_id = o.").append(data_org);
			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and o.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}
			sql.append(" and tax.opcode = 'read') o where 1 = 1 ");
		}else{
			sql.append("select 1");
			if(!this.strIsNull(data_name_filed)){
				sql.append(",o.").append(data_name_filed);
			}
			if(!this.strIsNull(data_value_field)){
				sql.append(",o.").append(data_value_field);
			}
			if(!this.strIsNull(data_validate_field)){
				sql.append(",o.").append(data_validate_field);
			}
			if(!this.strIsNull(data_org)){
				sql.append(",org.orgnumber").append("||' '||org.org_name as ").append(data_org);
			}
			if(!this.strIsNull(data_order_field)){
				sql.append(",o.").append(data_order_field).append(" as orby ");
			}
			sql.append(" from ").append(data_table_name).append(" o,TD_SM_TAXCODE_ORGANIZATION tax, td_sm_organization org ")
			.append("where o.").append(data_org).append("='").append(CurOrg).append("' and tax.org_id = '")
			.append(CurOrg).append("' and tax.dicttype_id = '").append(dicttypeId).append("' and ")
			.append("tax.data_name = o.").append(data_name_filed).append(" and tax.data_value = o.").append(data_value_field)
			.append(" and tax.opcode = 'read' ")
			.append("and org.org_id = o.").append(data_org);
			//判断是否有类型ID字段
			if(!this.strIsNull(data_typeid_field)){
				sql.append(" and o.").append(data_typeid_field).append("='").append(dicttype.getDataId()).append("' ");
			}
		}
		try {
			dbUtil.executeSelect(data_dbName,sql.toString());
			dictDataValues = new String[dbUtil.size()];
			for(int i = 0; i < dictDataValues.length; i++){
				String value = dbUtil.getString(i, data_value_field);
				String name = dbUtil.getString(i, data_name_filed);
				dictDataValues[i] = value + ":" + name; 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dictDataValues;
	}

	/**
	 * 超级管理员登陆选择了设置字典机构授权编码，当字典设置了机构字段-保存数据项向上保存不超过数据项所属机构
	 * @param orgId 授权给当前所选机构
	 * @param dicttypeId
	 * @param dictdataValues （value:name:orgnumber org_name）
	 * @author gao.tang 2008.1.16
	 */
	public void storeReadOrgTaxcodeorAdmin(String orgId, String dicttypeId, String[] dictdataValues){
		if(strIsNull(orgId) || strIsNull(dicttypeId) || dictdataValues[0].equals("") ) return ;

		String value = "";
		String name = "";
		String oid = "";
		TransactionManager tm = new TransactionManager();
		DBUtil parentDB = new DBUtil();
		DBUtil db = new DBUtil();
		StringBuffer insertRead_sql = new StringBuffer();
		try {
			tm.begin();
			for(int i = 0; i < dictdataValues.length; i++){
				String[] valueNameOrgid_Name = dictdataValues[i].split(":");
				if(valueNameOrgid_Name.length >= 3){
					value = valueNameOrgid_Name[0];
					name = valueNameOrgid_Name[1];
					oid = valueNameOrgid_Name[2].split(" ")[0];
					//数据项上级机构sql
					StringBuffer orgs_parent_sql = new StringBuffer()
						.append("select a.org_id from (select org.org_id from td_sm_organization org ")
						.append("start with org.org_id = '").append(orgId).append("' ")
						.append("connect by prior org.parent_id = org.org_id) a ")
						.append("join (select org.org_id from td_sm_organization org ")
						.append("start with org.org_id in (select org_id from td_sm_organization where orgnumber='").append(oid).append("') ")
						.append("connect by prior org.org_id = org.parent_id) b on a.org_id = b.org_id");

					parentDB.executeSelect(orgs_parent_sql.toString());
					for(int p = 0; p < parentDB.size(); p++){
						String parentId = parentDB.getString(p, "org_id");
						insertRead_sql.append("insert all when totalsize <= 0 then into td_sm_taxcode_organization ")
							.append("(org_id, dicttype_id, data_value, opcode, data_name) ")
							.append("values('").append(parentId).append("','").append(dicttypeId).append("','")
							.append(value).append("','read','").append(name).append("') ")
							.append("select count(org_id) totalsize ")
							.append("from td_sm_taxcode_organization where org_id='").append(parentId).append("' ")
							.append("and dicttype_id = '").append(dicttypeId).append("' and data_value='")
							.append(value).append("' and opcode = 'read' ");
						db.addBatch(insertRead_sql.toString());
						insertRead_sql.setLength(0);
						if(i*p==900){
							db.executeBatch();
						}
					}
			}
		}
			db.executeBatch();
			tm.commit();
			//发事件
			Event event = new EventImpl(dicttypeId,DictionaryChangeEvent.DICTIONARY_USERORG_CHANGE_READ);
			super.change(event,true);
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	/**
	 * 判断数据项是否被授权
	 * @param dicttypeId
	 * @param dictdataValue
	 * @param dictdataName
	 * @return
	 */
	private boolean isAccredit(String dicttypeId, String dictdataValue, String dictdataName){
		boolean state = false;
		StringBuffer sql = new StringBuffer()
			.append("select * from td_sm_taxcode_organization tax where tax.dicttype_id = '")
			.append(dicttypeId).append("' ");
		if(!"".equals(dictdataValue)){
			sql.append(" and tax.data_value = '").append(dictdataValue).append("' ");
		}
		if(!"".equals(dictdataName)){
			sql.append(" and tax.data_name = '").append(dictdataName).append("'");
		}
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql.toString());
			if(db.size() > 0){
				state = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return state;
	} 

	public boolean storeAdvanceFieldArr(String dicttypeId, String docid) {
		DBUtil db = new DBUtil();
		boolean state = false;
		String[] docids = docid.split(",");
		StringBuffer sql = new StringBuffer();
		for(int i = 0; i < docids.length; i++){
			sql.append("update TD_SM_DICATTACHFIELD set SN=").append(i+1)
				.append(" where DICTTYPE_ID='").append(dicttypeId).append("' ")
				.append(" and UPPER(TABLE_COLUMN)='").append(docids[i].toUpperCase()).append("'");
			try {
				db.addBatch(sql.toString());
				sql.setLength(0);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(docids.length > 0){
			try {
				db.executeBatch();
			} catch (Exception e) {

				e.printStackTrace();
			}finally{
				db.resetBatch();
			}
			state = true;
		}
		return state;
	}

}
