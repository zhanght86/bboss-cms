package com.frameworkset.platform.cms.customform.tag;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.customform.CustomFormManagerException;
import com.frameworkset.platform.cms.customform.DocExtField;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 具体的站点或频道的扩展字段列表
 * @author jxw
 *
 */

public class DocExtFieldOfSiteOrChlList extends DataInfoImpl implements java.io.Serializable
{
	/**
	 * 
	 * @param sql
	 * @param offset
	 * @param maxItem
	 * @return
	 * @throws CustomFormManagerException
	 */
	public ListInfo getDocExtFieldOfSiteOrChlList(String sql, int offset, int maxItem) throws CustomFormManagerException 
	{
		DBUtil dbUtil = new DBUtil();
		
		//文档id
		String docid = request.getParameter("docid");
		
		try 
		{
			dbUtil.executeSelect(sql, offset, maxItem);
			ListInfo listInfo = new ListInfo();
			List list = new ArrayList();
			
			for (int i = 0; i < dbUtil.size(); i++) 
			{
				
//				t.fieldname t.fieldlabel t.fielddesc t.fieldtype t.maxlen t.inputtype
				DocExtField docExtField = new DocExtField();
				
				docExtField.setFieldId(dbUtil.getInt(i,"field_id"));
				docExtField.setFieldName(dbUtil.getString(i,"fieldname"));
				docExtField.setFieldLable(dbUtil.getString(i,"fieldlabel"));
				docExtField.setFieldDesc(dbUtil.getString(i,"fielddesc"));
				docExtField.setFieldType(dbUtil.getString(i,"fieldtype"));
				docExtField.setMaxlen(dbUtil.getInt(i,"maxlen"));
				docExtField.setInputType(dbUtil.getInt(i,"inputtype"));
				if(!"".equals(docid)&&docid!=null)
				{
					DBUtil db = new DBUtil();
					String sqltemp = "select fieldvalue,numbervalue,to_char(datevalue,'YYYY-MM-DD') as datevalue,clobvalue from td_cms_extfieldvalue " +
					"where field_id = " + dbUtil.getInt(i,"field_id") + " and document_id = " + docid;
					try
					{
						db.executeSelect(sqltemp);
						if(db.size()>0){
							docExtField.setExtfieldvalue(db.getString(0,"fieldvalue"));
							docExtField.setNumbervalue(db.getString(0,"numbervalue"));
							docExtField.setDatevalue(db.getString(0,"datevalue"));
							docExtField.setClobvalue(db.getString(0,"clobvalue"));
						}
						
					}catch (Exception e) 
					{
						e.printStackTrace();
						throw new CustomFormManagerException(e.getMessage());
					}
				}
				
				list.add(docExtField);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
			return listInfo;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new CustomFormManagerException(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param sql
	 * @param offset
	 * @param maxItem
	 * @return
	 * @throws CustomFormManagerException
	 */
	public ListInfo getDocExtFieldOfSiteOrChlList(String sql) throws CustomFormManagerException 
	{
		DBUtil dbUtil = new DBUtil();
		
		//文档id
		String docid = request.getParameter("docid");
		TransactionManager tm = new TransactionManager(); 
		try 
		{
			tm.begin(tm.RW_TRANSACTION);
			dbUtil.executeSelect(sql);
			ListInfo listInfo = new ListInfo();
			List list = new ArrayList();
			
			for (int i = 0; i < dbUtil.size(); i++) 
			{
				
//				t.fieldname t.fieldlabel t.fielddesc t.fieldtype t.maxlen t.inputtype
				DocExtField docExtField = new DocExtField();
				
				docExtField.setFieldId(dbUtil.getInt(i,"field_id"));
				docExtField.setFieldName(dbUtil.getString(i,"fieldname"));
				docExtField.setFieldLable(dbUtil.getString(i,"fieldlabel"));
				docExtField.setFieldDesc(dbUtil.getString(i,"fielddesc"));
				docExtField.setFieldType(dbUtil.getString(i,"fieldtype"));
				docExtField.setMaxlen(dbUtil.getInt(i,"maxlen"));
				docExtField.setInputType(dbUtil.getInt(i,"inputtype"));
				if(!"".equals(docid)&&docid!=null)
				{
					DBUtil db = new DBUtil();
					String sqltemp = "select fieldvalue,numbervalue,to_char(datevalue,'YYYY-MM-DD') as datevalue,clobvalue from td_cms_extfieldvalue " +
					"where field_id = " + dbUtil.getInt(i,"field_id") + " and document_id = " + docid;
					try
					{
						db.executeSelect(sqltemp);
						if(db.size()>0){
							docExtField.setExtfieldvalue(db.getString(0,"fieldvalue"));
							docExtField.setNumbervalue(db.getString(0,"numbervalue"));
							docExtField.setDatevalue(db.getString(0,"datevalue"));
							docExtField.setClobvalue(db.getString(0,"clobvalue"));
						}
						
					}catch (Exception e) 
					{
						e.printStackTrace();
						throw new CustomFormManagerException(e.getMessage());
					}
				}
				
				list.add(docExtField);
			}
			listInfo.setDatas(list);
//			listInfo.setTotalSize(dbUtil.getTotalSize());
			tm.commit();
			return listInfo;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new CustomFormManagerException(e.getMessage());
		}
		finally
		{
			tm.release();
		}
	}
	/**
	 * 
	 */
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,int maxPagesize)
	{
		ListInfo listInfo = new ListInfo();
		
		//站点or频道 1：站点 2：频道
		String type = request.getParameter("type");
		//站点id or 频道id
		String id = request.getParameter("id");
		
		String sql = "";
		//站点
		if("1".equals(type))
			sql = "select * from td_cms_extfield order by field_id asc";
		//频道
		if("2".equals(type))
			sql = "select * from td_cms_extfield a where a.field_id " +
					"in (select b.field_id from td_cms_channelfield b where b.channel_id = " + id + ")" +
					" order by a.field_id asc";
		try
		{
			listInfo = getDocExtFieldOfSiteOrChlList(sql,(int)offset,maxPagesize);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return listInfo;
	}
	/**
	 * 
	 */
	protected ListInfo getDataList(String arg0, boolean arg1) 
	{
		ListInfo listInfo = new ListInfo();
		
		//站点or频道 1：站点 2：频道
		String type = request.getParameter("type");
		//站点id or 频道id
		String id = request.getParameter("id");
		
		String sql = "";
		//站点
		if("1".equals(type))
			sql = "select * from td_cms_extfield order by field_id asc";
		//频道
		if("2".equals(type))
			sql = "select * from td_cms_extfield a where a.field_id " +
					"in (select b.field_id from td_cms_channelfield b where b.channel_id = " + id + ")" +
					" order by a.field_id asc";
		try
		{
			listInfo = getDocExtFieldOfSiteOrChlList(sql);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return listInfo;
	}

}
