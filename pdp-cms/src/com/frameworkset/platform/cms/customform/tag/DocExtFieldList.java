package com.frameworkset.platform.cms.customform.tag;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.cms.customform.CustomFormManagerException;
import com.frameworkset.platform.cms.customform.DocExtField;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 所有的扩展字段列表
 * @author jxw
 *
 */

public class DocExtFieldList extends DataInfoImpl implements java.io.Serializable
{
	/**
	 * 
	 * @param sql
	 * @param offset
	 * @param maxItem
	 * @return
	 * @throws DocumentManagerException
	 */
	public ListInfo getDocExtFieldList(String sql, int offset, int maxItem) throws CustomFormManagerException 
	{
		DBUtil dbUtil = new DBUtil();
		//DBUtil db = new DBUtil();
		try 
		{
			dbUtil.executeSelect(sql, offset, maxItem);
			ListInfo listInfo = new ListInfo();
			List list = new ArrayList();
			
			for (int i = 0; i < dbUtil.size(); i++) 
			{
				//t.fieldname t.fieldlabel t.fielddesc t.fieldtype t.maxlen t.inputtype
				DocExtField docExtField = new DocExtField();
				
				docExtField.setFieldId(dbUtil.getInt(i,"field_id"));
				docExtField.setFieldName(dbUtil.getString(i,"fieldname"));
				docExtField.setFieldLable(dbUtil.getString(i,"fieldlabel"));
				docExtField.setFieldDesc(dbUtil.getString(i,"fielddesc"));
				docExtField.setFieldType(dbUtil.getString(i,"fieldtype"));
				docExtField.setMaxlen(dbUtil.getInt(i,"maxlen"));
				docExtField.setInputType(dbUtil.getInt(i,"inputtype"));
				
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
	 */
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,int maxPagesize)
	{
		ListInfo listInfo = new ListInfo();
		
		String id = request.getParameter("id");//频道or站点id    其实暂时没考虑站点的
		
		String sql = "";
		
		if(!"".equals(id)&&id!=null)
		{
			sql = "select * from td_cms_extfield a where a.field_id" +
					" not in (select b.field_id from td_cms_channelfield b where b.channel_id = " + id + ")" +
					" order by a.field_id asc";
		}
		else
		{
			sql = "select * from td_cms_extfield order by field_id asc";
		}
		try
		{
			listInfo = getDocExtFieldList(sql,(int)offset,maxPagesize);
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
        return null;
	}

}
