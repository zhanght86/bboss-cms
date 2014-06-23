/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.frameworkset.util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.BaseSPIManager;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProMap;
import org.frameworkset.util.ClassUtil.ClassInfo;
import org.frameworkset.util.ClassUtil.PropertieDescription;
import org.frameworkset.util.beans.PropertyAccessException;

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.ValueObjectUtil;

/**
 * <p>
 * Title: ParamsHandler.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2008
 * </p>
 * 
 * @Date 2010-1-11 下午03:50:21
 * @author biaoping.yin
 * @version 1.0
 */
public class ParamsHandler implements org.frameworkset.spi.InitializingBean {
	
	private static final Logger log = Logger.getLogger(ParamsHandler.class);
	private static Map<String,ParamsHandler> handlers = new HashMap<String,ParamsHandler>();
	static{
	 BaseApplicationContext.addShutdownHook(new Runnable() {
			
			public void run()
			{
		
				destory();
				
			}
		});
	}
	/**
	 * 缓存参数数据
	 * paramType:paramID:Params
	 * 参数类型      参数表        参数记录集
	 * 
	 */
	private Map<String,Map<String,Params>> cache = new HashMap<String,Map<String,Params>>();
	public static class Param {
		private String param_type;// 参数分类
		private String paramid; // 参数业务id
		private String name; // 参数名称
		private Object value;// 参数值
		private int rn; // 一个参数对应多个值时，对应行号
		
		private boolean isBigData = false;

		public boolean isBigData() {
			return isBigData;
		}



		public void setBigData(boolean isBigData) {
			this.isBigData = isBigData;
		}



		


		public Param()
		{
			
		}
		public Param(String paramType, String paramid, String name,
				Object value, int rn) {
			super();
			param_type = paramType;
			this.paramid = paramid;
			this.name = name;
			this.value = value;
			this.rn = rn;
			if(!(value instanceof String)){
				isBigData=true;
			}
		}
		
		

		public String getParam_type() {
			return param_type;
		}

		public void setParam_type(String paramType) {
			param_type = paramType;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public int getRn() {
			return rn;
		}

		public void setRn(int rn) {
			this.rn = rn;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getParamid() {
			return paramid;
		}

		public void setParamid(String paramid) {
			this.paramid = paramid;
		}

	}

	/**
	 * 参数集合
	 * 
	 * @author gao.tang
	 * 
	 */
	public static class Params{
		private String paramType;
		private String paramId;
		private String tableName;
		private String dbname;
		
		public int getSize(String code)
		{
			if (params == null || this.params.size() <= 0)
				return 0;
			int count = 0;
			for (Param param : params) {
				if (param.getName().equals(code))
					count ++;
			}
			return count;	
		}
		public String getMetaString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("paramType=").append(paramType)
			.append(",paramId=")
			.append(paramId)
			.append(",dbname=")
			.append(dbname)
			.append(",tableName=")
			.append(tableName);
			return builder.toString(); 
		}
		public String toString(){
			if(params == null || params.size() == 0){
				return null;
			}else{
				return super.toString();
			}
		}
		
		public boolean isEmpty(){
			if(params == null || params.size() == 0){
				return true;
			}else{
				return false;
			}
		}

		private List<Param> params = new ArrayList<Param>();

		public void addAttribute(String paramType, String paramid, String name,
				long value, int rn)

		{
			params.add(new Param(paramType, paramid, name, String
					.valueOf(value), rn));
		}

		public void addAttribute(String paramType, String paramid, String name,
				long value) {
			addAttribute(paramType, paramid, name, value, 0);
		}
		public void setParams(List<Param> params)
		{
			this.params = params;
		}

		public void addAttribute(String paramType, String paramid, String name,
				double value, int rn) {
			params.add(new Param(paramType, paramid, name, String
					.valueOf(value), rn));
		}
		
		public void addAttribute(String paramType, String paramid, String name,
				Object value) {
			params.add(new Param(paramType, paramid, name, value, 0));
		}
		
		public void addAttribute(String paramType, String paramid, String name,
				Object value, int rn) {
			params.add(new Param(paramType, paramid, name, value, rn));
		}

		public void addAttribute(String paramType, String paramid, String name,
				double value) {
			addAttribute(paramType, paramid, name, value, 0);
		}

		public void addAttribute(String paramType, String paramid, String name,
				boolean value) {
			addAttribute(paramType, paramid, name, value ? "Y" : "N");
		}

		public void addAttribute(String paramType, String paramid, String name,
				String value, int rn) {
			params.add(new Param(paramType, paramid, name, value, rn));
		}

		public void addAttribute(String paramType, String paramid, String name,
				String value) {
			addAttribute(paramType, paramid, name, value, 0);
		}

		private Object findAttribute(int nr, String code) {
			if (params == null || this.params.size() <= 0)
				return null;
			for (Param param : params) {
				if (param.getRn() == nr && param.getName().equals(code))
					return param.getValue();
			}
			return null;
		}

		public String getAttributeString(int nr, String code) {
			Object value = findAttribute(nr, code);
			if(value == null)
				return null;
			return String.valueOf(value);
		}
		
		public Object getAttributeObject(int nr, String code) {
			return findAttribute(nr, code);
		}

		public boolean getAttributeBoolean(int nr, String code, boolean def) {
			String value = this.getAttributeString(nr, code);
			if (this.isEmpty(value)) {
				return def;
			}
			return this.convertStringToBoolean(value);
		}

		public boolean getAttributeBoolean(int nr, String code) {
			String value = this.getAttributeString(nr, code);
			if (this.isEmpty(value)) {
				return false;
			}
			return this.convertStringToBoolean(value);
		}

		public long getAttributeInteger(String code) {
			return this.getAttributeInteger(0, code);

		}

		public long getAttributeInteger(int nr, String code) {
			String value = this.getAttributeString(nr, code);
			if (this.isEmpty(value)) {
				return 0;
			}
			return Long.parseLong(value);
		}

		public String getAttributeString(String code) {
			return getAttributeString(0, code);
		}

		public Object getAttributeObject(String code) {
			return getAttributeObject(0, code);
		}
		public boolean getAttributeBoolean(String code) {
			return getAttributeBoolean(0, code);
		}

		public List<Param> getParams() {
			return params;
		}
		/**
		 * 在内存中更新给定名称的参数值
		 * @param param
		 * @return true 有更新，false为插入参数
		 */
		public boolean updateParam(Param param)
		{
			if(this.params == null)
			{
				params = new ArrayList<Param>();
				params.add(param);
				return false;
			}
			else if(this.params.size() == 0)
			{
				params.add(param);
				return false;
			}
			else
			{
				int i = 0;
				for(Param param_:params)
				{
					if(param_.getName().equals(param.getName()))
					{
						params.remove(i);
						params.add(param);
						return true;
						
					}
					i ++;
				}
				
				params.add(param);	
				return false;
			}
			
		}

		public static Boolean convertStringToBoolean(String string) {
			if (isEmpty(string))
				return null;
			return Boolean.valueOf("Y".equalsIgnoreCase(string)
					|| "TRUE".equalsIgnoreCase(string)
					|| "YES".equalsIgnoreCase(string) || "1".equals(string));
		}

		/**
		 * Check if the string supplied is empty. A String is empty when it is
		 * null or when the length is 0
		 * 
		 * @param string
		 *            The string to check
		 * @return true if the string supplied is empty
		 */
		public static final boolean isEmpty(String string) {
			return string == null || string.length() == 0;
		}

		public String getParamType() {
			return paramType;
		}

		public void setParamType(String paramType) {
			this.paramType = paramType;
		}

		public String getParamId() {
			return paramId;
		}

		public void setParamId(String paramId) {
			this.paramId = paramId;
		}

		public String getTableName() {
			return tableName;
		}

		public void setTableName(String tableName) {
			this.tableName = tableName;
		}

		public String getDbname() {
			return dbname;
		}

		public void setDbname(String dbname) {
			this.dbname = dbname;
		}

	}

	public boolean cleanCache(String paramid,String paramType)
	{
		Map<String,Params> table = this.cache.get(paramType);
		if(table == null)
			return false;
		synchronized(table)
		{
			Object obj = table.remove(paramid);
			if(obj == null)
				return false;
			else
				return true;
		}
		
	}
	public boolean cleanCaches()
	{
		this.cache.clear();
		return true;
	}
	public void _destory()
	{
		this.cache.clear();
		cache = null;
	}
	
	public static boolean cleanAllCache()
	{
		init();
		Iterator<Map.Entry<String, ParamsHandler>> it = handlers.entrySet().iterator();
		while(it.hasNext())
		{
			ParamsHandler handler = it.next().getValue();
			handler.cleanCaches();
		}
		return true;
	}
	
	public static void destory()
	{
		loaded = false;
		if(handlers != null)
		{
			Iterator<Map.Entry<String, ParamsHandler>> it = handlers.entrySet().iterator();
			while(it.hasNext())
			{
				ParamsHandler handler = it.next().getValue();
				handler._destory();
			}
			handlers.clear();
			handlers = null;
		}
		
	}
	
	
	
	
	
	
	public boolean cleanCaches(Map<String,Object> keys)
	{
		if(keys == null || keys.size() == 0) return false;
		Iterator<String> it = keys.keySet().iterator();
		while(it.hasNext())
		{
			String v = it.next();
			String[] vs = v.split("\\^\\^");
			cleanCache(vs[0],vs[1]);
		}
		return true;
		
		
	}
	
	private void rnParams(List<Param> paramList )
	{
		if(paramList == null || paramList.size() == 0)
			return ;
		Map<String,Integer> rns = new HashMap<String,Integer>();
		for(Param param:paramList)
		{
			String name = param.getName();
			Integer rn = rns.get(name);
			if(rn == null)
			{
				rns.put(name,new Integer(0));
			}
			else
			{
				int newrn = rn.intValue()+1;
				param.setRn(newrn);
				rns.put(name,new Integer(newrn));
			}
		}
	}
	/**
	 * 自定义参数存储
	 * 
	 * @param params
	 * @param paramid
	 * @param paramType
	 * @return
	 */
	public boolean saveParams(Params params) {
		StringBuilder sql_del = new StringBuilder();
		sql_del.append("delete from ").append(tableName).append(
				" where NODE_ID=? and param_type= ?");
		StringBuilder sql_in = new StringBuilder();
		sql_in.append("insert into ").append(tableName).append(
				"(NODE_ID,NAME,VALUE,RN,PARAM_TYPE,BIGDATA,ISBIGDATA) values(?,?,?,?,?,?,?)");

		List<Param> paramList = params.getParams();
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		Object t = new Object();
		Map<String, Object> trace = new HashMap<String, Object>();

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			if(SimpleStringUtil.isEmpty(params.getParamId() )|| SimpleStringUtil.isEmpty(params.getParamType()))
			{
				rnParams(paramList );
				for (Param param : paramList) {
					String key = param.getParamid() + "^^" + param.getParam_type();
					if (trace.containsKey(key))
						continue;
					dbutil.preparedDelete(this.getDbname(), sql_del.toString());
					dbutil.setString(1, param.getParamid());
					dbutil.setString(2, param.getParam_type());
					dbutil.addPreparedBatch();
					trace.put(key, t);
	
				}
			}
			else
			{
				String key = params.getParamId() + "^^" + params.getParamType();
				trace.put(key, t);
				rnParams(paramList );
				dbutil.preparedDelete(this.getDbname(), sql_del.toString());
				dbutil.setString(1, params.getParamId());
				dbutil.setString(2, params.getParamType());
				dbutil.addPreparedBatch();
			}
			for (Param param : paramList) {
				if(SimpleStringUtil.isEmpty(param.getName()))
					continue;
				dbutil.preparedInsert(this.getDbname(), sql_in.toString());
				dbutil.setString(1, param.getParamid());
				dbutil.setString(2, param.getName());
				if(!param.isBigData()){
					dbutil.setString(3, String.valueOf(param.getValue()));
				}else{
					dbutil.setNull(3, java.sql.Types.VARCHAR);
					
//					byte[] buf = null;
//					ByteArrayOutputStream bo = null;
//					ObjectOutputStream oo = null;
//					
//					try {
//						// 将对象转换为二进制流
//						bo = new ByteArrayOutputStream();
//						oo = new ObjectOutputStream(bo);
//
//						oo.writeObject(param.getValue());
//						
//						// 写入对象流到数据库blob字段中
//						buf = bo.toByteArray();					
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} finally {
//						// 关闭流
//						if (bo != null)
//							try {
//								bo.close();
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						if (oo != null)
//							try {
//								oo.close();
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//					}
//					dbutil.setBlob(3, buf);

				}
				dbutil.setInt(4, param.getRn());
				dbutil.setString(5, param.getParam_type());
				if(!param.isBigData()){
					dbutil.setNull(6, java.sql.Types.BLOB);
					dbutil.setInt(7, 0);
				}else{
					
					
					byte[] buf = null;
					ByteArrayOutputStream bo = null;
					ObjectOutputStream oo = null;
					
					try {
						// 将对象转换为二进制流
						bo = new ByteArrayOutputStream();
						oo = new ObjectOutputStream(bo);

						oo.writeObject(param.getValue());
						
						// 写入对象流到数据库blob字段中
						buf = bo.toByteArray();					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						// 关闭流
						if (bo != null)
							try {
								bo.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						if (oo != null)
							try {
								oo.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
					dbutil.setBlob(6, buf);
					dbutil.setInt(7, 1);
				}
				dbutil.addPreparedBatch();
			}
			dbutil.executePreparedBatch();
			tm.commit();
			cleanCaches(trace);
			trace = null;
			return true;
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		finally
		{
			tm.release();
		}
		return false;
	}

	public static Params getParams(String paramHandle,String paramid, String paramType) {
		return getParamsHandler(paramHandle).getParams(paramid, paramType);
	}
	
	public static String  getStringParam(String paramHandle,String paramid, String paramName,String paramType) {
		return getParamsHandler(paramHandle).getParams(paramid, paramType).getAttributeString(paramName);
	}

	public static <T>  T getParams(String paramHandle,String paramid, String paramType,Class<T> dataBean) {
		Params params = getParams( paramHandle, paramid,  paramType) ;
		return convertParamsToBean(params,dataBean);
	}
	
	
	private static <T>  T convertParamsToBean(Params params,Class<T> dataBean)
	{
		if(params == null)
			return null;
		
		ClassInfo beanInfo = null;
		try {
			beanInfo = ClassUtil.getClassInfo(dataBean);
		} catch (Exception e) {
			throw new PropertyAccessException( "获取bean ["+dataBean.getName()+"]信息失败",e);
		}
		
		String name = null;
		
		Object value =  null;
		Class type = null;
		T obj = null;
		try {
			obj = dataBean.newInstance();
		} catch (InstantiationException e1) {
			throw new RuntimeException("参数转对象错误,实例化对象失败 ",e1);
		} catch (IllegalAccessException e1) {
			throw new RuntimeException("参数转对象错误,实例化对象失败 ",e1);
		}
		List<PropertieDescription> attributes = beanInfo.getPropertyDescriptors();
		for(PropertieDescription property:attributes)
		{
			if(property.getName().equals("class"))
				continue;
			
			try {
				name = property.getName();
				
				type = property.getPropertyType();
				value =  params.getAttributeObject(name);
				value = ValueObjectUtil.typeCast(value, type);
				property.setValue(obj, value);
				
				
			} catch (SecurityException e) {
				log.error("参数转对象属性设置失败:bean class["+ dataBean.getCanonicalName() +"],param info["+ params.getMetaString()+ "]",e );
				continue;
			} catch (IllegalArgumentException e) {
				log.error("参数转对象属性设置失败:bean class["+ dataBean.getCanonicalName() +"],param info["+ params.getMetaString()+ "]",e );
				continue;
			} catch (IllegalAccessException e) {
				log.error("参数转对象属性设置失败:bean class["+ dataBean.getCanonicalName() +"],param info["+ params.getMetaString()+ "]" ,e);
				continue;
			} catch (InvocationTargetException e) {
				log.error("参数转对象属性设置失败:bean class["+ dataBean.getCanonicalName() +"],param info["+ params.getMetaString()+ "]" ,e);
				continue;
			} 
//			catch (InstantiationException e) {
//				log.error("参数转对象属性设置失败:bean class["+ dataBean.getCanonicalName() +"],param info["+ params.getMetaString()+ "]" ,e);
//				continue;
//			}
			catch (Exception e) {
				log.error("参数转对象属性设置失败:bean class["+ dataBean.getCanonicalName() +"],param info["+ params.getMetaString()+ "]" ,e);
				continue;
			}
		}
		
		return obj;
		
	}
	public Params getParams(String paramid, String paramType) {
		Map<String,Params> table  = this.cache.get(paramType);
		if(table  == null)
		{
			synchronized(cache)
			{
				table  = this.cache.get(paramType);
				if(table == null)
				{
					table = new HashMap<String,Params>();
					cache.put(paramType, table);
				}
			}
		}
		Params params = table.get(paramid);
		if(params != null)
			return params;
		else
		{
			synchronized(table)
			{
				params = table.get(paramid);
				if(params != null)
					return params;
				params = _getParams(paramid, paramType);
				table.put(paramid, params);
			}
		}
		return params;
	}
	public Params _getParams(String paramid, String paramType) {
		StringBuilder sql_query = new StringBuilder();
		sql_query.append("select * from ").append(tableName).append(
				" where NODE_ID=? and param_type= ? order by name,rn asc");
		PreparedDBUtil pd = new PreparedDBUtil();
		
		final Params params = new Params();
		try {
			pd.preparedSelect(this.getDbname(),sql_query.toString());
			pd.setString(1, paramid);
			pd.setString(2, paramType);
			params.setDbname(this.getDbname());
			params.setParamId(paramid);
			params.setParamType(paramType);
			params.setTableName(tableName);
			pd.executePreparedWithRowHandler(new NullRowHandler() {
				@Override
				public void handleRow(Record record) {
					try {
						int isbigData = record.getInt("ISBIGDATA");
						if(isbigData == 1){
							Blob blob = record.getBlob("BIGDATA");
							ObjectInputStream oi = null;
							
							try{
								oi = new ObjectInputStream(blob.getBinaryStream());
								Object obj = oi.readObject();
								params.addAttribute(record.getString("PARAM_TYPE"),
										record.getString("NODE_ID"), record
												.getString("NAME"), obj, record
												.getInt("RN"));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}finally{
								if(oi!=null)
									try {
										oi.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							}
							
						}else{
							params.addAttribute(record.getString("PARAM_TYPE"),
									record.getString("NODE_ID"), record
											.getString("NAME"), record
											.getString("VALUE"), record
											.getInt("RN"));
						}
						
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
			});
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return params;
	}
	
	public Param getParam(String paramid, String paramName,String paramType) {
		StringBuilder sql_query = new StringBuilder();
		sql_query.append("select * from ").append(tableName).append(
				" where NODE_ID=? and param_type= ? and name = ? order by rn asc");
		PreparedDBUtil pd = new PreparedDBUtil();
		final Param param = new Param();
		try {
			pd.preparedSelect(this.getDbname(),sql_query.toString());
			pd.setString(1, paramid);
			pd.setString(2, paramType);
			pd.setString(3, paramName);
			
			pd.executePreparedWithRowHandler(new NullRowHandler() {
				@Override
				public void handleRow(Record record) {
					try {
						int isbigData = record.getInt("ISBIGDATA");
						if(isbigData == 1){
							Blob blob = record.getBlob("BIGDATA");
							ObjectInputStream oi = null;
							
							try{
								oi = new ObjectInputStream(blob.getBinaryStream());
								Object obj = oi.readObject();
								param.setParam_type(record.getString("PARAM_TYPE"));
								param.setName(record
										.getString("NAME"));
								param.setParamid(record.getString("NODE_ID"));
								param.setBigData(true);
								param.setValue(obj);
								param.setRn( record
												.getInt("RN"));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}finally{
								if(oi!=null)
									try {
										oi.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							}
							
						}else{
							param.setParam_type(record.getString("PARAM_TYPE"));
							param.setName(record
									.getString("NAME"));
							param.setParamid(record.getString("NODE_ID"));
							param.setBigData(true);
							param.setValue(record.getString("value"));
							param.setRn( record
											.getInt("RN"));
						}
						
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
			});
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return param;
	}
	
	public List<Param> getMultiValueParam(String paramid, String paramName,String paramType) {
		StringBuilder sql_query = new StringBuilder();
		sql_query.append("select * from ").append(tableName).append(
				" where NODE_ID=? and param_type= ? and name = ? order by rn asc");
		PreparedDBUtil pd = new PreparedDBUtil();
		final List<Param> params = new ArrayList<Param>();
		
		try {
			pd.preparedSelect(this.getDbname(),sql_query.toString());
			pd.setString(1, paramid);
			pd.setString(2, paramType);
			pd.setString(3, paramName);
			
			pd.executePreparedWithRowHandler(new NullRowHandler() {
				@Override
				public void handleRow(Record record) {
					try {
						int isbigData = record.getInt("ISBIGDATA");
						Param param = new Param();
						if(isbigData == 1){
							Blob blob = record.getBlob("BIGDATA");
							ObjectInputStream oi = null;
							
							try{
								oi = new ObjectInputStream(blob.getBinaryStream());
								Object obj = oi.readObject();
								param.setParam_type(record.getString("PARAM_TYPE"));
								param.setName(record
										.getString("NAME"));
								param.setParamid(record.getString("NODE_ID"));
								param.setBigData(true);
								param.setValue(obj);
								param.setRn( record
												.getInt("RN"));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}finally{
								if(oi!=null)
									try {
										oi.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							}
							
						}else{
							param.setParam_type(record.getString("PARAM_TYPE"));
							param.setName(record
									.getString("NAME"));
							param.setParamid(record.getString("NODE_ID"));
							param.setBigData(true);
							param.setValue(record.getString("value"));
							param.setRn( record
											.getInt("RN"));
						}
						params.add(param);	
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
			});
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return params;
	}
	
	/**
	 * 没有一个key对应多value
	 * @param paramid
	 * @param paramType
	 * @return
	 */
	public Map<String,Object> getMapParams(String paramid, String paramType) {
		StringBuilder sql_query = new StringBuilder();
		sql_query.append("select * from ").append(tableName).append(
				" where NODE_ID=? and param_type= ? order by name, rn asc");
		PreparedDBUtil pd = new PreparedDBUtil();
		final Map<String,Object> params = new HashMap<String,Object>();
		try {
			pd.preparedSelect(this.getDbname(),sql_query.toString());
			pd.setString(1, paramid);
			pd.setString(2, paramType);

			pd.executePreparedWithRowHandler(new NullRowHandler() {
				@Override
				public void handleRow(Record record) {
					try {
						int isbigData = record.getInt("ISBIGDATA");
						if(isbigData == 1){
							Blob blob = record.getBlob("BIGDATA");
							ObjectInputStream oi = null;
							
							try{
								oi = new ObjectInputStream(blob.getBinaryStream());
								Object obj = oi.readObject();
								
								params.put(record.getString("NAME"), obj);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}finally{
								if(oi!=null)
									try {
										oi.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							}
							
						}else{
							params.put(record.getString("NAME"), record.getString("VALUE"));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
			});
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return params;
	}
	
	public boolean delParams(String paramid, String paramType){
		StringBuilder sql_del = new StringBuilder();
		sql_del.append("delete from ").append(tableName).append(
				" where NODE_ID=? and param_type= ?");
		PreparedDBUtil pd = new PreparedDBUtil();
		try {
			pd.preparedDelete(this.getDbname(),sql_del.toString());
			pd.setString(1, paramid);
			pd.setString(2, paramType);
			pd.executePrepared();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean delParam(String paramid,String paramName, String paramType){
		StringBuilder sql_del = new StringBuilder();
		sql_del.append("delete from ").append(tableName).append(
				" where NODE_ID=? and param_type= ? and name=?");
		PreparedDBUtil pd = new PreparedDBUtil();
		try {
			pd.preparedDelete(this.getDbname(),sql_del.toString());
			pd.setString(1, paramid);
			pd.setString(2, paramType);
			pd.setString(3, paramName);
			pd.executePrepared();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 客户端节点高级参数类型名称
	 */
	public static final String AMQ_PARAM_TYPE = "amq.connection.params";

	/**
	 * 客户端esb高级参数处理对象名称
	 */
	public static final String CMC_PARAMSHANDLER = "cmc.paramshandler";
	/**
	 * 服务端esb高级参数处理对象名称
	 */
	public static final String SMC_PARAMSHANDLER = "smc.paramshandler";
	/**
	 * 服务端。集群高级参数配置类型名称
	 */
	public static final String MQ_NETWORKCONNECTOR_PARAM = "mq.networkconnector.params";
	
	/**
	 * 服务端。基本参数配置类型名称
	 */
	public static final String MQ_BROKERSERVICE_PARAM = "mq.brokerservice.params";

	private String dbname = null;

	private String tableName = "ESB_PARAMS";

	private String configContextPath = null;

	private ApplicationContext applicationContext;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getConfigContextPath() {
		return configContextPath;
	}

	public void setConfigContextPath(String configContextPath) {
		this.configContextPath = configContextPath;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	/**
	 * 根据配置的map属性名称获取map集中的所有属性名称，属性名称根据有split分隔符拼接一个字符串返回
	 * 
	 * @param map
	 *            <String,Object> map
	 * @param split
	 *            连接key字符串的分隔连接符号
	 * @return
	 */
	public String getNodeParamsMapKeyStrings(String propertyMapName,
			String split) {
		ProMap proMap = (ProMap) applicationContext
				.getMapProperty(propertyMapName);
		if (proMap == null || proMap.size() == 0)
			throw new ParamException("请确认是否配置了属性名为：" + propertyMapName
					+ "的map属性集合！");
		Iterator<String> iterator = proMap.keySet().iterator();
		StringBuffer sb = new StringBuffer();
		while (iterator.hasNext()) {
			if (sb.length() == 0) {
				sb.append("'").append(iterator.next().toString()).append("'");
			} else {
				sb.append(split).append("'").append(iterator.next().toString())
						.append("'");
			}
		}
		return sb.toString();
	}

	/**
	 * 从数据库中获取已经配置的参数
	 * 
	 * @param propertyMapName
	 *            模板参数map配置的名称
	 * @param nodeId
	 *            配置节点id
	 * @@param params_type 参数类型
	 * @return
	 * 
	 */
	public ProMap<String, Pro> getNodeParamsWithProMapName(
			String propertyMapName, String nodeId, String params_type) {
		// 获取配置文件中的map参数属性配置名称
		String nameParam = getNodeParamsMapKeyStrings(propertyMapName, ",");
		StringBuilder sql = new StringBuilder();
		// 根据配置的map参数属性配置查找数据库，防止以前存在多余的字段
		sql.append("select NAME,VALUE from ").append(tableName).append(
				" where NODE_ID = ? and PARAM_TYPE=? and name in(").append(
				nameParam).append(")  order by name,rn asc");
		final ProMap<String, Pro> params = new ProMap<String, Pro>();
		PreparedDBUtil dbutil = new PreparedDBUtil();
		try {
			dbutil.preparedSelect(getDbname(), sql.toString());
			dbutil.setString(1, nodeId);
			dbutil.setString(2, params_type);
			dbutil.executePreparedWithRowHandler(new NullRowHandler() {
				@Override
				public void handleRow(Record record) {
					try {
						Pro rowValue = new Pro();
						rowValue.setName(record.getString("name"));

						rowValue.setValue(record.getString("value"));

						params.put(rowValue.getName(), rowValue);
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
			});
			return params;
		} catch (Exception e) {
			throw new ParamException(e);
		}
	}

	/**
	 * 从数据库中获取给定参数名称，配置节点id，参数类型的参数对象
	 * 
	 * @param propertyName
	 *            参数称
	 * @param nodeId
	 *            配置节点id
	 * @@param params_type 参数类型
	 * @return
	 * 
	 */
	public Pro getNodeParamWithProName(String propertyName, String nodeId,
			String params_type) {

		String sql = "select NAME,VALUE from " + tableName
				+ " where NODE_ID = ? and PARAM_TYPE=? and name=?  order by rn asc";

		Pro pro = null;
		PreparedDBUtil dbutil = new PreparedDBUtil();
		try {
			dbutil.preparedSelect(getDbname(), sql);
			dbutil.setString(1, nodeId);
			dbutil.setString(2, params_type);
			dbutil.setString(3, propertyName);
			pro = (Pro) dbutil.executePreparedForObject(Pro.class,
					new RowHandler<Pro>() {

						@Override
						public void handleRow(Pro rowValue, Record record) {
							try {
								rowValue.setName(record.getString("name"));

								rowValue.setValue(record.getString("value"));

							} catch (SQLException e) {
								e.printStackTrace();
							}

						}

					});

			return pro;
		} catch (Exception e) {
			throw new ParamException(e);
		}
	}

	/**
	 * 从数据库中获取已经配置的参数
	 * 
	 * @param propertyMapName
	 *            模板参数map配置的名称
	 * @param nodeId
	 *            配置节点id
	 * @param params_type
	 *            参数类型
	 * @return
	 * 
	 */
	public ProMap<String, Pro> getNodeParams(String nodeId, String params_type) {

		String sql = "select NAME,VALUE from " + tableName
				+ " where NODE_ID = ? and PARAM_TYPE=?  order by name,rn asc";
		final ProMap<String, Pro> params = new ProMap<String, Pro>();

		PreparedDBUtil dbutil = new PreparedDBUtil();
		try {
			dbutil.preparedSelect(getDbname(), sql);
			dbutil.setString(1, nodeId);
			dbutil.setString(2, params_type);
			dbutil.executePreparedWithRowHandler(new NullRowHandler() {

				public void handleRow(Record record) {
					try {
						Pro rowValue = new Pro();
						rowValue.setName(record.getString("name"));

						rowValue.setValue(record.getString("value"));

						params.put(rowValue.getName(), rowValue);
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}

			});

			return params;
		} catch (Exception e) {
			throw new ParamException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.chinacreator.mq.client.MqNodeServiceInf#
	 * getInitConnectionParamsMapProperty()
	 */
	public List<Pro> getInitNodeParams(String... properMapnames) {

		if (properMapnames == null || properMapnames.length == 0)
			return null;
		List<Pro> initParams = new ArrayList<Pro>();
		for (String name : properMapnames) {
			ProMap paramsConfMap = (ProMap) applicationContext
					.getMapProperty(name);
			Iterator it = paramsConfMap.keySet().iterator();
			Pro pro = null;
			String key = null;
			while (it.hasNext()) {

				key = (String) it.next();
				pro = paramsConfMap.getPro(key);
				initParams.add(pro);
			}
		}

		return initParams;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinacreator.mq.client.MqNodeServiceInf#insertConParams(java.lang
	 * .String, java.util.List)
	 */
	public boolean insertNodeParams(int NODE_ID, List<Pro> newparamvalues,
			String paramstype, String... properMapnames) {
		String sql_del = "delete from " + tableName
				+ " where NODE_ID=? and param_type= ?";
		// TransactionManager tm = new TransactionManager();
		PreparedDBUtil dbutil = new PreparedDBUtil();
		try {
			// tm.begin();
			dbutil.preparedDelete(this.getDbname(), sql_del);
			dbutil.setString(1, String.valueOf(NODE_ID));
			dbutil.setString(2, paramstype);
			dbutil.addPreparedBatch();
			if (newparamvalues == null || newparamvalues.size() == 0) {
				newparamvalues = getInitNodeParams(properMapnames);
			}
			StringBuffer sql = new StringBuffer();
			Pro mq_con_params = null;
			for (int i = 0; i < newparamvalues.size(); i++) {
				mq_con_params = newparamvalues.get(i);
				sql.append("insert into ").append(tableName).append(
						"(NODE_ID,NAME,VALUE,PARAM_TYPE) values(?,?,?,?)");
				dbutil.preparedInsert(this.getDbname(), sql.toString());
				dbutil.setString(1, String.valueOf(NODE_ID));
				dbutil.setString(2, mq_con_params.getName());
				dbutil.setString(3, mq_con_params.getString());
				dbutil.setString(4, paramstype);
				dbutil.addPreparedBatch();
				sql.setLength(0);
			}
			dbutil.executePreparedBatch();
			return true;
		} catch (Exception e) {

			e.printStackTrace();
			throw new ParamException(e);
		}
	}

	/**
	 * 根据节点id和参数类型删除参数配置
	 * 
	 * @param nodeIds
	 * @param paramstype
	 * @return
	 */
	public boolean delNodeParams(String[] nodeIds, String paramstype) {
		String sql_del = "delete from " + tableName
				+ " where NODE_ID=? and param_type= ?";
		PreparedDBUtil dbutil = new PreparedDBUtil();
		try {
			for (String nodeId : nodeIds) {
				dbutil.preparedDelete(getDbname(), sql_del);
				dbutil.setString(1, nodeId);
				dbutil.setString(2, paramstype);
				dbutil.addPreparedBatch();
			}
			dbutil.executePreparedBatch();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static boolean delNodeParams(String paramshandler,
			String[] businessIds, String properMapname) {
		ParamsHandler handle = getParamsHandler(paramshandler);
		String[] properMapnames = properMapname.split(",");

		for (String mapName : properMapnames) {
			Pro t_ = BaseSPIManager.getProBean(mapName);
			String paramsType = (String) t_.getExtendAttribute("paramstype");
			try {
				handle.delNodeParams(businessIds, paramsType);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public boolean insertConParams(int NODE_ID, List<Pro> params,
			String paramsType) {
		String sql_del = "delete from " + tableName
				+ " where NODE_ID=? and PARAM_TYPE=?";
		PreparedDBUtil dbutil = new PreparedDBUtil();
		try {
			dbutil.preparedDelete(this.getDbname(), sql_del);
			dbutil.setString(1, String.valueOf(NODE_ID));
			dbutil.setString(2, paramsType);
			dbutil.addPreparedBatch();
			StringBuffer sql = new StringBuffer();
			Pro mq_con_params = null;
			for (int i = 0; i < params.size(); i++) {
				mq_con_params = params.get(i);
				sql.append("insert into ").append(tableName).append(
						"(NODE_ID,NAME,VALUE,PARAM_TYPE) values(?,?,?,?)");
				dbutil.preparedInsert(this.getDbname(), sql.toString());
				dbutil.setString(1, String.valueOf(NODE_ID));
				dbutil.setString(2, mq_con_params.getName());
				dbutil.setString(3, mq_con_params.getString());
				dbutil.setString(4, paramsType);
				dbutil.addPreparedBatch();
				sql.setLength(0);
			}
			dbutil.executePreparedBatch();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	private static boolean loaded;
	private static void init()
	{
		if(loaded)
			return;
		synchronized(ParamsHandler.class)
		{
			if(loaded)
				return;
			BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/util/paramhandlers.xml");
			Set<String> keys =context.getPropertyKeys();
			if(keys == null || keys.size() == 0)
			{
				loaded = true;
				return;
			}
			Iterator<String> its = keys.iterator();
			while(its.hasNext())
			{
				String key = its.next();
				Object value = context.getBeanObject(key);
				if(value == null)
					continue;
				if(value instanceof ParamsHandler)
				{
					handlers.put(key, (ParamsHandler)value);
				}
			}
			loaded = true;
		}
	}
	public static ParamsHandler getParamsHandler(String name) {
		
		init();
//		return (ParamsHandler) DefaultApplicationContext.getApplicationContext("org/frameworkset/util/paramhandlers.xml")
//				.getBeanObject(name);
		return handlers.get(name);
	}

	public void afterPropertiesSet() throws Exception {
		this.applicationContext = ApplicationContext
				.getApplicationContext(this.configContextPath);
	}

	public static void insertParams(Map<String, String> paramsMap,
			String factoryId, String properMapname, String paramshandler) {

	}

	/**
	 * 
	 * 保存参数组，使用远程服务调用
	 * 
	 * @param request
	 * @param factoryId
	 *            业务主键ID
	 * @param properMapname
	 *            参数组名称
	 * @param paramshandler
	 *            参数处理类名字
	 * @param rpcbrokeraddr
	 *            远程组件调用地址
	 * @throws ParamException
	 */
	public static void insertParams(HttpServletRequest request,
			String factoryId, String properMapname, String paramshandler,
			String rpcbrokeraddr) throws ParamException {
		String[] properMapnames = properMapname.split(",");
		if (rpcbrokeraddr == null || "".equals(rpcbrokeraddr))
			rpcbrokeraddr = ParamProperties.PARAM_HANDLER_PRO;
		RpcParamsHandlerInf brokerMan = (RpcParamsHandlerInf) BaseSPIManager
				.getBeanObject(rpcbrokeraddr);

		String key = null;
		Pro pro = null;
		List<Pro> params = new ArrayList<Pro>();
		String msg = null;
		for (String mapName : properMapnames) {
			// 获取配置文件中的配置参数默认值
			ProMap confMap = (ProMap) BaseSPIManager.getMapProperty(mapName);
			Pro t_ = BaseSPIManager.getProBean(mapName);
			String paramsType = (String) t_.getExtendAttribute("paramstype");
			Iterator itParams = confMap.keySet().iterator();
			while (itParams.hasNext()) {
				key = (String) itParams.next();
				pro = new Pro();
				pro.setName(key);
				String value = request.getParameter(key);
				value = (value == null || "".equals(value)) ? "" : value;
				pro.setValue(value);
				params.add(pro);
			}
			try {
				brokerMan.insertConParams(paramshandler, Integer
						.parseInt(factoryId), params, paramsType);
				params.clear();
			} catch (Exception e) {
				throw new ParamException(e);
			}
		}
	}

	/**
	 * 
	 * 保存参数组，本地调用
	 * 
	 * @param request
	 * @param factoryId
	 *            业务主键ID
	 * @param properMapname
	 *            参数组名称
	 * @param paramshandler
	 *            参数处理类名字
	 * @throws ParamException
	 */
	public static void insertParams(HttpServletRequest request,
			String factoryId, String properMapname, String paramshandler)
			throws ParamException {
		insertParams(request, factoryId, properMapname, paramshandler, null);
	}

	/**
	 * 重置参数组，使用配置文件中的默认配置，适用远程服务调用
	 * 
	 * @param factoryId
	 * @param properMapname
	 * @param paramshandler
	 * @param rpcbrokeraddr
	 * @throws ParamException
	 */
	public static void resetParams(String factoryId, String properMapname,
			String paramshandler, String rpcbrokeraddr) throws ParamException {
		String[] properMapnames = properMapname.split(",");
		if (rpcbrokeraddr == null || "".equals(rpcbrokeraddr))
			rpcbrokeraddr = ParamProperties.PARAM_HANDLER_PRO;
		RpcParamsHandlerInf brokerMan = (RpcParamsHandlerInf) BaseSPIManager
				.getBeanObject(rpcbrokeraddr);

		String key = null;
		Pro pro = null;
		List<Pro> params = new ArrayList<Pro>();
		String msg = null;
		for (String mapName : properMapnames) {
			// 获取配置文件中的配置参数默认值
			ProMap confMap = (ProMap) BaseSPIManager.getMapProperty(mapName);
			Pro t_ = BaseSPIManager.getProBean(mapName);
			String paramsType = (String) t_.getExtendAttribute("paramstype");
			Iterator itParams = confMap.keySet().iterator();
			while (itParams.hasNext()) {
				key = (String) itParams.next();
				pro = new Pro();
				pro.setName(key);
				String value = confMap.getString(key);
				pro.setValue(value);
				params.add(pro);
			}
			try {
				brokerMan.insertConParams(paramshandler, Integer
						.parseInt(factoryId), params, paramsType);
				params.clear();
			} catch (Exception e) {
				throw new ParamException(e);
			}
		}
	}

	/**
	 * 重置参数组，使用配置文件中的默认配置，本地调用
	 * 
	 * @param factoryId
	 * @param properMapname
	 * @param paramshandler
	 * @throws ParamException
	 */
	public static void resetParams(String factoryId, String properMapname,
			String paramshandler) throws ParamException {
		resetParams(factoryId, properMapname, paramshandler, null);
	}
	/**
	 * 获取所有对应类型的参数名称为paramName的值，以nodeid为key的map对象
	 * @param paramType
	 * @return
	 */
	public Map<String,String> getStringParamMap(String paramType,String paramName) throws ParamException
	{
		StringBuilder sql_query = new StringBuilder();
		sql_query.append("select * from ").append(tableName).append(
				" where param_type= ? and NAME=? order by rn asc");
		final Map<String,String> params = new HashMap<String,String>();
		try {
			SQLExecutor.queryByNullRowHandler(new NullRowHandler(){

				@Override
				public void handleRow(Record origine) throws Exception {
					
					params.put(origine.getString("NODE_ID"), origine.getString("VALUE"));
				}
				
			}, sql_query.toString(), paramType,paramName);
			return params;
		} catch (Exception e) {
			throw new ParamException(e);
		}
	}
}
