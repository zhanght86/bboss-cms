package com.frameworkset.platform.cms.documentmanager;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.sql.ColumnMetaData;
import com.frameworkset.common.poolman.sql.TableMetaData;
import com.frameworkset.orm.engine.model.SchemaType;

public class DocumentExtColumnManager implements java.io.Serializable {
	private String dateFormat = "'yyyy-mm-dd hh24:mi:ss'";	
	
	private static final List fixColumns = new ArrayList();
	
	private static final List extColumns = new ArrayList();
	
	
	static {
		init();
	}	
	
	private static void init()
	{
		TableMetaData table = DBUtil.getTableMetaData("td_cms_document");
		//System.out.println("private static void init():td_cms_document=" + table);
		Set columns = table.getColumns();		
		
		Iterator iter = columns.iterator();
		extColumns.clear();
		fixColumns.clear();
		while (iter.hasNext()){			
			ColumnMetaData col = (ColumnMetaData)iter.next();
			col.setRemarks(getColCommandByName(col.getColumnName()));
			if(col.getColumnName().startsWith("EXT_") || col.getColumnName().startsWith("ext_")){
				extColumns.add(col);
			}else{
				fixColumns.add(col);
			}
		}	
	}
	
	public static List getExtColumns() {
		return extColumns;
	}

	public static List getFixColumns() {
		return fixColumns;
	}
	
	public static String getColCommandByName(String name){
		String command = "";
		StringBuffer sql = new StringBuffer();
		sql.append("select b.comments  from ");
		sql.append("USER_TAB_COLUMNS a, USER_COL_COMMENTS b where  a.TABLE_NAME=b.table_name and ");
		sql.append("a.COLUMN_NAME=b.column_name and ");
		sql.append("a.column_name='").append(name).append("'");
		DBUtil db = new DBUtil();
		try{			
			db.executeSelect(sql.toString());
			if(db.size()>0){
				command = db.getString(0, "comments");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return command;
			
	}
	
	
	/**
	 * 获得指定表名的所有列信息 对于所有数据库
	 * @param tableName
	 * @return
	 */
	public List getTableColumnsInfo(String tableName){
		List list = new ArrayList();
		
		tableName = tableName.toLowerCase();	
		TableMetaData table = DBUtil.getTableMetaData(tableName);
		Set columns = table.getColumns();		
		Iterator iter = columns.iterator();
		while (iter.hasNext()){			
			ColumnMetaData col = (ColumnMetaData)iter.next();
			String column_name = col.getColumnName();
		    String data_type = col.getTypeName();
		    String data_length = String.valueOf(col.getColunmSize());
		    String comments = col.getRemarks();
		    String[] ob = new String[]{column_name,data_type,data_length,comments};
		    list.add(ob);
		}		
		return list;
	}
	
	/**
	 * 获得指定表名的所有列信息 对于oracle
	 * @param tableName
	 * @return
	 */
	public List getTableColumnsInfo_oracle(String tableName){
		List list = new ArrayList();
		tableName = tableName.toLowerCase();
		StringBuffer sql = new StringBuffer();
		//select a.table_name,a.column_name,a.data_type,a.data_length,b.comments  from   
		//USER_TAB_COLUMNS a, USER_COL_COMMENTS b where  a.TABLE_NAME=b.table_name and  
		//a.COLUMN_NAME=b.column_name and lower(a.TABLE_NAME)='test'
		sql.append("select a.table_name,a.column_name,a.data_type,a.data_length,b.comments  from ");
		sql.append("USER_TAB_COLUMNS a, USER_COL_COMMENTS b where  a.TABLE_NAME=b.table_name and ");
		sql.append("a.COLUMN_NAME=b.column_name and lower(a.TABLE_NAME)='").append(tableName).append("'");
		String sqlstr = sql.toString();		
		DBUtil db = new DBUtil();
		try{			
			db.executeSelect(sqlstr);
			for(int i=0;i<db.size();i++){
				String column_name = db.getString(i,"column_name");
			    String data_type = db.getString(i,"data_type");
			    String data_length = db.getString(i,"data_length");
			    String comments = db.getString(i,"comments");
			    String[] ob = new String[50];
			    if(column_name.startsWith("ext_") || column_name.startsWith("EXT_")){
			        ob = new String[]{column_name,data_type,data_length,comments};
			    }
			    list.add(ob);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}	
		return list;
	}
	
	/**
	 * 提供扩展字段构造 SQL的语句  构造字段
	 * @param HashMap map 文档对象的扩展字段属性.
	 * @param tableName
	 * @return
	 */
	public String appendExtSQLField(HashMap map,String tableName,boolean flag){		
//		List list = getTableColumnsInfo_oracle(tableName);		
		StringBuffer column_names = new StringBuffer();
		//boolean flag = false;
		for(int i=0;i<extColumns.size();i++){
			
			ColumnMetaData str = (ColumnMetaData)extColumns.get(i);
			if(map.containsKey(str.getColumnName().toLowerCase()))
			{
				if(!flag)
				{
					
					column_names .append( str.getColumnName());
					flag = true;
				}
				else
				{
					column_names.append(",") .append( str.getColumnName());
				}
			}
           
		}
		return column_names.toString();
	}
	
	
	/**
	 * 构建扩展字段的查询串
	 * @param HashMap map 文档对象的扩展字段属性.
	 * @param tableName
	 * @return
	 * 
	 * added by biaoping.yin on 2007.11.20
	 */
	public static String buildExtQueryString(String tablealias){		
//		
		StringBuffer column_names = new StringBuffer();
		
		
		for(int i=0;i<extColumns.size();i++){
			
			ColumnMetaData str = (ColumnMetaData)extColumns.get(i);
			
			if(i == 0)
			{
				if(tablealias != null && !tablealias.equals(""))
				{
					column_names.append(tablealias).append(".") .append( str.getColumnName());
				}
				else
				{
					column_names.append( str.getColumnName());
				}
				
			}
			else
			{
				if(tablealias != null && !tablealias.equals(""))
				{
					column_names.append(",").append(tablealias).append(".") .append( str.getColumnName());
				}
				else
				{
					column_names.append(",").append( str.getColumnName());
				}
			}
			
           
		}
		return column_names.toString();
	}
	
	/**
	 * 提供扩展字段构造 SQL的语句  构造字段
	 * @param HashMap map 文档对象的扩展字段属性.
	 * @param tableName
	 * @return
	 */
	public String appendExtUpdateSQLField(HashMap map,String tableName){		
		//List list = getTableColumnsInfo_oracle(tableName);		
		String column_names = ",";
		for(int i=0;i<extColumns.size();i++){
			ColumnMetaData str = (ColumnMetaData)extColumns.get(i);
			if(map.containsKey(str.getColumnName().toLowerCase())) column_names += (str.getColumnName() + "=?");
            if(i!=extColumns.size()-1) column_names += ",";
		}
		return column_names;
	}
	
	/**
	 * 提供扩展字段构造SQL的语句 提供值
	 * 如: insert into ... (...)  values (...,a,b,to_date('','yyyy-mm-dd'));
	 * @param request
	 * @param tableName
	 * @return 
	 */
	public String appendExtInsertSQLValue(HashMap map,String tableName){	
		
//		List list = getTableColumnsInfo_oracle(tableName);
		String column_values = ",";
		for(int i=0;i<extColumns.size();i++){
			ColumnMetaData str = (ColumnMetaData)extColumns.get(i);
            String column_name = str.getColumnName().toLowerCase();
//            String data_type = str.getTypeName();
            if(map.containsKey(column_name.toLowerCase())){
	            if(str.getSchemaType() == SchemaType.DATE){
	            	column_values += DBUtil.getDBDate(map.get(column_name).toString());
	            }else{
	            	column_values += map.get(column_name);
	            }
            }else continue;
            if(i!=extColumns.size()-1) column_values += ",";
		}		
		return column_values;
	}
	
	/**
	 * 提供扩展字段构造 SQL的语句 更新
	 * 如: update ... set a = to_date('','yyyy-mm-dd'),b=... )+ this.updateSQL(request,"td_cms_document")
	 * @param request
	 * @param tableName
	 * @return
	 */
	public String appendExtUpdateSQL(HashMap map,String tableName){
		String subsql = ",";
//		List list = getTableColumnsInfo_oracle(tableName);
		for(int i=0;i<extColumns.size();i++){
			ColumnMetaData str = (ColumnMetaData)extColumns.get(i);
            String column_name = str.getColumnName().toLowerCase();
//            String data_type = str[1];
            if(map.containsKey(column_name)){
            	if(str.getSchemaType() == SchemaType.DATE){
	            	subsql += column_name+"="+DBUtil.getDBDate(map.get(column_name).toString());
	            }else{
	            	subsql += column_name + "="+map.get(column_name);
	            }
            }else continue;
            if(i!=extColumns.size()-1) subsql += ",";
		}		
		return subsql;
	}
	
	/* 非预处理 */
	/* 预处理 */
	
	/**
	 * 提供扩展字段构造 预处理的SQL的语句 加 "?"
	 * @param tableName
	 * @return
	 */
	public String appendExtPreparedQuestionMark(HashMap map,String tableName){	
		String  question_mark= ",";
//		List list = getTableColumnsInfo_oracle(tableName);
		for(int i=0;i<extColumns.size();i++){
			ColumnMetaData str = (ColumnMetaData)extColumns.get(i);
            String column_name = str.getColumnName().toLowerCase();
//            String data_type = str[1];
            if(map.containsKey(column_name)){
            	if(str.getSchemaType() == SchemaType.DATE){
	            	question_mark += column_name+"=to_date(?, "+dateFormat+")";
	            }else{
	            	question_mark += column_name + "=?";
	            }
            }else continue;
            if(i!=extColumns.size()-1) question_mark += ",";
		}	
		return question_mark;
	}
		
	/**
	 * 提供扩展字段构造 预处理的SQL的语句 加 "?"
	 * @param tableName
	 * @return
	 */
	public String appendExt(HashMap map,String tableName){	
		String  mark= "";
//		List list = getTableColumnsInfo_oracle(tableName);
		for(int i=0;i<extColumns.size();i++){
			ColumnMetaData str = (ColumnMetaData)extColumns.get(i);
            String column_name = str.getColumnName().toLowerCase();
//            String data_type = str[1];
            if(map.containsKey(column_name)){
            	mark += ",?";
            }else continue;
		}	
		return mark;
	}
		
	/**
	 * 提供扩展字段构造 预处理的SQL的语句 提供值
	 * 如: stm.setString; db.setTimestamp; db.setInt;
	 * @param request
	 * @param tableName
	 * @return
	 */
	public int appendExtPreparedValue(PreparedDBUtil db ,HashMap map,String tableName,int index)throws Exception{
//		List list = getTableColumnsInfo_oracle(tableName);
		for(int i=0;i<extColumns.size();i++){
			ColumnMetaData str = (ColumnMetaData)extColumns.get(i);
            String column_name = str.getColumnName();
            column_name = column_name.toLowerCase();
            if(map.containsKey(column_name)){      
            	if(str.getSchemaType() == SchemaType.DATE){
	            	SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh24:mi:ss");
	            	Date date = df.parse((String)map.get(column_name));
	            	db.setTimestamp(index,new Timestamp(date.getTime()));
	            }else if(str.getSchemaType() == SchemaType.NUMERIC ){
	    			db.setInt(index,Integer.parseInt((String)map.get(column_name)));
	            }else{
	            	db.setString(index,(String)map.get(column_name));
	            }
	            index ++;
            }else continue;
		}
		return index;
	}	
	
	
	
	/*根据特定信息 得到 文档对象的指定信息或者对象*/
	
	/**
	 * 根据特定的文档(文档ID)
	 * 获取该文档库扩展字段特定字段的信息
	 * tableName:TD_CMS_DOCUMENT
	 */
	public String getExtColumnValue(String document_id,String column_name){
		DBUtil db = new DBUtil();
		String column_value = ""; 
		String sqlstr = "select "+column_name+" from TD_CMS_DOCUMENT where DOCUMENT_ID="+document_id;
		try{			
			db.executeSelect(sqlstr);
			if(db.size()>0){
				column_value = db.getString(0,column_name);
			}
		}catch(SQLException e){  
			e.printStackTrace();
		}
		return column_value;
	}
	
	/**
	 * 根据特定的文档(文档ID)
	 * 获取该文档库扩展字段特定字段对象
	 * tableName:TD_CMS_DOCUMENT
	 */
	public HashMap getExtColumnInfo(String document_id){
		HashMap map = new HashMap();
		DBUtil db = new DBUtil();		
//		List list = getTableColumnsInfo_oracle("TD_CMS_DOCUMENT");		
		for(int i=0;i<extColumns.size();i++){
			ColumnMetaData str = (ColumnMetaData)extColumns.get(i);
			String column_names = str.getColumnName().toLowerCase();
			String sqlstr = "select "+column_names+" from TD_CMS_DOCUMENT where DOCUMENT_ID="+document_id;
			try{			
				db.executeSelect(sqlstr);
				if(db.size()>0){
					/*转载数据*/
					map.put(column_names,db.getObject(0,column_names));
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
		}	
		return map;
	}
	
	/**
	 * 根据已经查询出来的数据,把扩展字段装载到文档对象
	 * 获取该文档库扩展字段特定字段对象
	 * tableName:TD_CMS_DOCUMENT
	 */
	public HashMap getExtColumnInfo(int j,DBUtil db){
		HashMap map = new HashMap();	
//		List list = getTableColumnsInfo_oracle("TD_CMS_DOCUMENT");		
		for(int i=0;i<extColumns.size();i++){
			ColumnMetaData str = (ColumnMetaData)extColumns.get(i);
			String column_names = str.getColumnName().toLowerCase();
			try {
				Object value = db.getObject(j,column_names);
				if(value != null)
					map.put(column_names,value);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}	
		return map;
	}
	
	/**
	 * 根据已经查询出来的数据,把扩展字段装载到文档对象
	 * 获取该文档库扩展字段特定字段对象
	 * tableName:TD_CMS_DOCUMENT
	 */
	public HashMap getExtColumnInfo(Record db){
		HashMap map = new HashMap();	
//		List list = getTableColumnsInfo_oracle("TD_CMS_DOCUMENT");		
		for(int i=0;i<extColumns.size();i++){
			ColumnMetaData str = (ColumnMetaData)extColumns.get(i);
			String column_names = str.getColumnName().toLowerCase();
			try {
				Object value = db.getObject(column_names);
				if(value != null)
					map.put(column_names,value);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}	
		return map;
	}
    
	/**
	 * 
	 * @param table_name 表名
	 * @param db_name 数据源名称:bspf
	 */
	public static void updateTableMetaData(String table_name,String db_name)
	{
		synchronized (DocumentExtColumnManager.class)
		{
			DBUtil.updateTableMetaData(table_name,db_name);
			init();
		}
	}
	
	public boolean isSetLength(String type){
		if("long".equals(type) || "date".equals(type) || "blob".equals(type) || "clob".equals(type) ) return false;
		else return true;
	}
	
	public String[] createOperateSQL(HttpServletRequest request){
		String[] oldColumns = request.getParameterValues("col_name");
		String oldColumn = request.getParameter("old_name");
		String column_name = request.getParameter("column_name");
		String column_type = request.getParameter("column_type");
		String column_length = request.getParameter("column_length");
		String column_comment = request.getParameter("column_comment");
		String operate_type = request.getParameter("operate_type");	
		
		String table_name = "td_cms_document";
		int length = 3;
		if(oldColumns!=null) length = oldColumns.length>=2?oldColumns.length:2;
		String[] sqlstr = new String[length];	
		if(isSetLength(column_type)){
	        column_type = column_type+"("+column_length+")";
		}
		
		if("add".equals(operate_type)){
			sqlstr[0] = "alter table "+table_name+" add "+column_name+" "+column_type;
			sqlstr[1] = "comment on column  "+table_name+"."+column_name+" is '"+column_comment+"' ";
		}else if("edit".equals(operate_type)){
			if(oldColumn.toLowerCase().equals(column_name)) sqlstr[0] = "";
			else sqlstr[0] = "ALTER   TABLE "+table_name+" RENAME  COLUMN  "+oldColumn+"   TO   "+column_name;
		
		    sqlstr[1] = "alter   table   "+table_name+"  modify("+column_name+"  "+column_type+")";
		    sqlstr[2] = "comment on column  "+table_name+"."+column_name+" is '"+column_comment+"' ";
			
		}else{
			for(int i=0;i<oldColumns.length;i++){
				if(oldColumns[i]==null || oldColumns[i]=="" || oldColumns[i].length()<=0) continue;
			    sqlstr[i] = "ALTER TABLE "+table_name+" DROP COLUMN "+oldColumns[i];
			}
		}
		
		return sqlstr;
	}


}
