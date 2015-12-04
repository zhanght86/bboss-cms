package com.frameworkset.platform.cms.customform;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.util.StringUtil;

/**
 * 自定义表单接口实现类
 * @author jxw
 *
 */

public class CustomFormManagerImpl implements CustomFormManager 
{
	public CustomFormManagerImpl()
	{
		
	}
	/**
	 * 设置文档的各项自定义表单
	 * @param TARGET_ID
	 * @param TARGET_TYPE
	 * @param FORM_TYPE
	 * @param FILE_NAME
	 * @return boolean
	 * @throws CustomFormManagerException
	 */
	public boolean setCustomForm(String targetId,String targetType,String formType,String fileName) throws CustomFormManagerException
	{
		boolean flag = false;
		DBUtil db = new DBUtil();
		try
		{
			String sql = "insert into TD_CMS_CUSTOM_FORM values(" + targetId + ",'" + targetType + 
				"','" + formType + "','" + fileName + "','')";
			db.executeInsert(sql);
			flag = true;
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new CustomFormManagerException(e.getMessage());
		}
		return flag;
	}
	/**
	 * 设置文档的各项自定义表单
	 * @param <CustomForm>List
	 * @return boolean
	 * @throws CustomFormManagerException
	 */
	public boolean addCustomForms(String id,List customForms) throws CustomFormManagerException
	{
		boolean flag = false;
		DBUtil db = new DBUtil();
		for(int i=0;i<customForms.size();i++)
		{
			CustomForm cf = (CustomForm)customForms.get(i);
			if(cf.getFileName()==null||"".equals(cf.getFileName()))
				continue;
			try
			{
				String sql = "insert into TD_CMS_CUSTOM_FORM values(" + id + ",'" + cf.getTargetType() + 
					"','" + cf.getFormType() + "','" + cf.getFileName() + "','')";
				db.executeInsert(sql);
				flag = true;
			}catch(Exception e)
			{
				e.printStackTrace();
				throw new CustomFormManagerException(e.getMessage());
			}
		}
		return flag;
	}
	/**
	 * update设置文档的各项自定义表单
	 * @param TARGET_ID
	 * @param TARGET_TYPE
	 * @param FORM_TYPE
	 * @param FILE_NAME
	 * @return boolean
	 * @throws CustomFormManagerException
	 */
	public boolean updateCustomFormSet(String targetId,String targetType,String formType,String fileName) throws CustomFormManagerException
	{
		boolean flag = false;
		DBUtil db = new DBUtil();
		if(fileName==null||"".equals(fileName))
			return true;
		try
		{
			String sql = "update TD_CMS_CUSTOM_FORM set file_name = '" + fileName + "' where " +
				"TARGET_ID = " + targetId + " and TARGET_TYPE = '" + targetType + "' and " +
				"FORM_TYPE = '"	+ formType + "'";
			db.executeUpdate(sql);System.out.println(sql);
			flag = true;
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new CustomFormManagerException(e.getMessage());
		}
		return flag;
	}
	/**
	 * get设置文档的自定义表单的filename
	 * @param TARGET_ID
	 * @param TARGET_TYPE
	 * @param FORM_TYPE
	 * @param FILE_NAME
	 * @return String
	 * @throws CustomFormManagerException
	 */
	public String getCustomFormFilename(String targetId,String targetType,String formType) throws CustomFormManagerException
	{
		String filename = "";
		PreparedDBUtil db = new PreparedDBUtil();
		try
		{
			String sql = "select file_name from TD_CMS_CUSTOM_FORM where TARGET_ID = ? and TARGET_TYPE = ? and FORM_TYPE = ?";
			db.preparedSelect(sql);
			db.setInt(1, Integer.parseInt(targetId));
			db.setString(2, targetType);
			db.setString(3, formType);
			db.executePrepared();
			if(db.size()>0)
			{
				filename = db.getString(0,"file_name");
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new CustomFormManagerException(e.getMessage());
		}
		return filename;
	}
	/**
	 * 新增一项扩展字段前
	 * 判断是否存在字段名字相同的情况
	 * @param String fieldName
	 * @return boolean
	 * @throws CustomFormManagerException
	 */
	public boolean hasSameFieldName(String fieldName) throws CustomFormManagerException
	{
		boolean flag = false;
		PreparedDBUtil db = new PreparedDBUtil();
		String sql = "select 1 from TD_CMS_EXTFIELD where FIELDNAME = ?";
		try
		{
			db.preparedSelect(sql);
			db.setString(1, fieldName);
			db.executePrepared();
			if(db.size()>0)
				flag = true;
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new CustomFormManagerException(e.getMessage());
		}
		return flag;
	}
	/**
	 * 新增一项扩展字段
	 * @param <DocExtField>docExtField
	 * @return boolean
	 * @throws CustomFormManagerException
	 */
	public boolean addDocExtField(DocExtField docExtField) throws CustomFormManagerException
	{
		boolean flag = false;
		PreparedDBUtil db = new PreparedDBUtil();
		DBUtil dbutil = new DBUtil(); 
		String sql = "insert into TD_CMS_EXTFIELD(FIELDNAME,FIELDLABEL,FIELDDESC,FIELDTYPE,MAXLEN,INPUTTYPE,FIELD_ID) " +
	    	"values (?,?,?,?,?,?,?)";
		try
		{
			db.preparedInsert(sql);
			
			long fieldId = dbutil.getNextPrimaryKey("TD_CMS_EXTFIELD") ;
			
			db.setString(1,docExtField.getFieldName());
			db.setString(2,docExtField.getFieldLable());
			db.setString(3,docExtField.getFieldDesc());
			db.setString(4,docExtField.getFieldType());
			if(docExtField.getMaxlen()==0)
				db.setNull(5,Types.INTEGER);
			else
				db.setInt(5,docExtField.getMaxlen());
			if(docExtField.getInputType()==0)
				db.setNull(6,Types.INTEGER);
			else
				db.setInt(6,docExtField.getInputType());
			
			db.setLong(7,fieldId) ;
			
			db.executePrepared() ;
			
			int id = (int) fieldId ;
			//判断是否是从频道过来的，是则需要同时新增一条引用记录
			if(!"null".equals(docExtField.getIdOfSiteOrChl()))
			{
				String sql2 = "";
				//站点
				if("1".equals(docExtField.getType()))
					sql2 = "insert into td_cms_sitefield(file_id,site_id) values (" + id + "," + docExtField.getIdOfSiteOrChl() + ")";
				//频道
				if("2".equals(docExtField.getType()))
					sql2 = "insert into td_cms_channelfield(field_id,channel_id) values (" + id + "," + docExtField.getIdOfSiteOrChl() + ")";
				dbutil.executeInsert(sql2);
			}
			//保存取值范围信息
			PreparedDBUtil pd = new PreparedDBUtil();
			if(docExtField.getInputType()==1)//枚举类型
			{
				ArrayList values = docExtField.getEno();
				for(int i=0;i<values.size();i++)
				{
//					sql = "insert into td_cms_extvaluescope(field_id,value,DESCRIPTION) values " +
//							"(" + id + ",'" + ((String)values.get(i)).split(";")[0] +"','" + ((String)values.get(i)).split(";")[1] +"')";
					
					long Id = pd.getNextPrimaryKey("td_cms_extvaluescope") ;
					
					sql = "insert into td_cms_extvaluescope(field_id,value,DESCRIPTION,ID) values(?,?,?,?) ";
					pd.preparedInsert(sql);
					pd.setInt(1, id);
					pd.setString(2, ((String)values.get(i)).split(";")[0]);
					pd.setString(3, ((String)values.get(i)).split(";")[1]);
					pd.setLong(4,Id) ;
					pd.executePrepared();
				}
			}
			if(docExtField.getInputType()==2)//连续类型
			{
//				sql = "insert into td_cms_extvaluescope(field_id,minvalue,maxvalue) values " +
//						"(" + id + "," + docExtField.getMinvalue() + "," + docExtField.getMaxvalue() +")";
				sql = "insert into td_cms_extvaluescope(field_id,minvalue,maxvalue,ID) values (?,?,?,?)";
				
				long Id = pd.getNextPrimaryKey("td_cms_extvaluescope") ;
				
				
				pd.preparedInsert(sql);
				pd.setInt(1, id);
				pd.setInt(2, docExtField.getMinvalue());
				pd.setInt(3, docExtField.getMaxvalue());
				pd.setLong(4,Id) ;
				
				pd.executePrepared();
			}
			flag = true;
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new CustomFormManagerException(e.getMessage());
		}
		return flag;
		
	}
	/**
	 * 从所有扩展字段中选择一些作为自己的扩展字段，是一种引用关系
	 * @param String[] ids
	 * @param String type
	 * 		1：站点
	 * 		2：频道
	 * @param String id 
	 * 		站点或频道的id
	 * @throws CustomFormManagerException
	 */
	public boolean addDocExtFields(String[] ids,String type,String id) throws CustomFormManagerException
	{
		boolean flag = false;
		DBUtil db = new DBUtil();
		for(int i=0;i<ids.length;i++)
		{
			String sql = "";
			if("1".equals(type))
			{
				sql = "insert into td_cms_sitefield(file_id,site_id) values (" + ids[i] + "," + id + ")";
			}
			if("2".equals(type))
			{
				sql = "insert into td_cms_channelfield(field_id,channel_id) values (" + ids[i] + "," + id + ")";
			}
			try
			{
				db.executeInsert(sql);
				
				flag = true;
			}catch(Exception e)
			{
				e.printStackTrace();
				throw new CustomFormManagerException(e.getMessage());
			}
		}
		return flag;
		
	}
	/**
	 * 删除扩展字段（在维护界面上删）
	 * @param String[] ids
	 * @return boolean
	 * @throws CustomFormManagerException
	 */
	public boolean delDocExtField(String[] ids) throws CustomFormManagerException
	{
		boolean flag = false;
		DBUtil db = new DBUtil();
		for(int i=0;i<ids.length;i++)
		{
			
			String sql1 = "delete from td_cms_extfield where field_id = " + ids[i];
			String sql2 = "delete from td_cms_extfieldvalue where field_id = " + ids[i];
			String sql3 = "delete from td_cms_channelfield where field_id = " + ids[i];
			String sql4 = "delete from td_cms_sitefield where file_id = " + ids[i];
			String sql5 = "delete from TD_CMS_EXTVALUESCOPE where field_id = " + ids[i];
			try
			{
				db.executeDelete(sql2);
				db.executeDelete(sql3);
				db.executeDelete(sql4);
				db.executeDelete(sql5);
				db.executeDelete(sql1);//先后顺序不能乱
				
				flag = true;
			}catch(Exception e)
			{
				e.printStackTrace();
				throw new CustomFormManagerException(e.getMessage());
			}
		}
		return flag;
	}
	/**
	 * 删除具体的站点或频道的扩展字段
	 * @param String[] ids
	 * @param String type
	 * 		1：站点
	 * 		2：频道
	 * @param String id 
	 * 		站点或频道的id
	 * @return boolean
	 * @throws CustomFormManagerException
	 */
	public boolean delDocExtFieldOfSiteOrChl(String[] ids,String type,String id) throws CustomFormManagerException
	{
		boolean flag = false;
		DBUtil db = new DBUtil();
		for(int i=0;i<ids.length;i++)
		{
			String sql1 = "";
			String sql2 = "";
			if("1".equals(type))
			{
				sql1 = "delete from td_cms_sitefield where field_id = " + ids[i];
			}
			if("2".equals(type))
			{
				sql1 = "delete from td_cms_channelfield where field_id = " + ids[i];
				sql2 = "delete from td_cms_extfieldvalue where field_id = " + ids[i] + 
					" and document_id in (select document_id from td_cms_document where channel_id = " + id + ")";
			}
			try
			{
				db.executeDelete(sql1);
				if("2".equals(type))
				{
					db.executeDelete(sql2);
				}
				
				flag = true;
			}catch(Exception e)
			{
				e.printStackTrace();
				throw new CustomFormManagerException(e.getMessage());
			}
		}
		return flag;
	}
	/**
	 * get所有的扩展字段列表
	 * 
	 * @return java.util.List
	 * @throws CustomFormManagerException
	 */
	public List getDocExtFieldList() throws CustomFormManagerException
	{
		List list = new ArrayList();
		DBUtil db = new DBUtil();
		String sql = "";
		try
		{
			db.executeSelect(sql);
			if(db.size()>0)
			{
				
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new CustomFormManagerException(e.getMessage());
		}
		return list;
	}
	/**
	 * get具体的站点或频道的扩展字段列表
	 * @param String id 
	 * 		站点或频道的id
	 * @param String type
	 * 		1：站点
	 * 		2：频道
	 * @return java.util.List
	 * @throws CustomFormManagerException
	 */
	public List getDocExtFieldOfSiteOrChlList(String id,String type) throws CustomFormManagerException
	{
		List list = new ArrayList();
		DBUtil db = new DBUtil();
		String sql = "";
		try
		{
			db.executeSelect(sql);
			if(db.size()>0)
			{
				
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new CustomFormManagerException(e.getMessage());
		}
		return list;
	}
	/**
	 * 保存文档的扩展字段的内容
	 * @param String docid 
	 * 		文档的id
	 * @param String[] ids
	 * 		该文档所有的扩展字段
	 * @param String[] values
	 * 		相应的扩展字段内容
	 * @return java.util.List
	 * @throws CustomFormManagerException
	 */
	public boolean saveDocExtFieldValues(String docid,String[] ids,String[] values,String[] types) throws CustomFormManagerException
	{
		boolean flag = false;
		PreparedDBUtil db = new PreparedDBUtil();
		try
		{
			int did = Integer.parseInt(docid);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			for(int i=0;i<ids.length;i++)
			{
				if("".equals(ids[i])||ids[i]==null)
					continue;
//				String fildName = "fieldvalue";
//				if (types[i].equals("0"))
//					fildName = "NUMBERVALUE";
//				if (types[i].equals("2"))
//					fildName = "DATEVALUE";
//				if (types[i].equals("3"))
//					fildName = "CLOBVALUE";
				if(values[i].equals(""))
					continue;
				
				String sql = "";
				String sqltemp = "select field_id from td_cms_extfieldvalue where field_id = ? and document_id = ?" ;
				db.preparedSelect(sqltemp);
				db.setInt(1, Integer.parseInt(ids[i]));
				db.setInt(2, did);
				db.executePrepared();
				if(db.size()>0)
				{
					
					if (types[i].equals("1")||types[i].equals("4"))
					{
						
						sql = "update td_cms_extfieldvalue set fieldvalue = ? where field_id = ? and document_id = ?" ;
						PreparedDBUtil updatedb = new PreparedDBUtil();
						updatedb.preparedUpdate(sql);
						updatedb.setString(1, StringUtil.replaceAll(values[i],"'","''"));
						updatedb.setInt(2, Integer.parseInt(ids[i]));
						updatedb.setInt(3, did);
						updatedb.executePrepared();
					}
					if (types[i].equals("0"))
					{
						sql = "update td_cms_extfieldvalue set NUMBERVALUE =? where field_id = ? and document_id = ?" ;
						PreparedDBUtil updatedb = new PreparedDBUtil();
						updatedb.preparedUpdate(sql);
						updatedb.setInt(1, Integer.parseInt(values[i]));
						updatedb.setInt(2, Integer.parseInt(ids[i]));
						updatedb.setInt(3, did);
						updatedb.executePrepared();
					}
					if (types[i].equals("2"))
					{
						sql = "update td_cms_extfieldvalue set DATEVALUE =? where field_id = ? and document_id = ?";
						PreparedDBUtil updatedb = new PreparedDBUtil();
						updatedb.preparedUpdate(sql);
						updatedb.setDate(1, format.parse(values[i]));
						updatedb.setInt(2, Integer.parseInt(ids[i]));
						updatedb.setInt(3, did);
						updatedb.executePrepared();
					}
					if (types[i].equals("3"))
					{
						sql = "update td_cms_extfieldvalue set CLOBVALUE = ? where field_id = ? and document_id = ?";
						PreparedDBUtil updatedb = new PreparedDBUtil();
						updatedb.preparedUpdate(sql);
						updatedb.setClob(1,StringUtil.replaceAll(values[i],"'","''"));
						updatedb.setInt(2, Integer.parseInt(ids[i]));
						updatedb.setInt(3, did);
						updatedb.executePrepared();
					}
					if (types[i].equals("5")||types[i].equals("6"))
					{
						sql = "update td_cms_extfieldvalue set CLOBVALUE = ? where field_id = ? and document_id = ?";						
						PreparedDBUtil updatedb = new PreparedDBUtil();
						updatedb.preparedUpdate(sql);
						updatedb.setClob(1,StringUtil.replaceAll(values[i],"'","''"));
						updatedb.setInt(2, Integer.parseInt(ids[i]));
						updatedb.setInt(3, did);
						updatedb.executePrepared();
					}
//					db.executeUpdate(sql);
				}
				else
				{
					if (types[i].equals("1")||types[i].equals("4"))
					{
						sql = "insert into td_cms_extfieldvalue (field_id,document_id,fieldvalue) " +
							"values (?,?,?)";
						PreparedDBUtil updatedb = new PreparedDBUtil();
						updatedb.preparedInsert(sql);
						updatedb.setString(3, StringUtil.replaceAll(values[i],"'","''"));
						updatedb.setInt(1, Integer.parseInt(ids[i]));
						updatedb.setInt(2, did);
						updatedb.executePrepared();
					}
					if (types[i].equals("3"))
					{
						sql = "insert into td_cms_extfieldvalue (field_id,document_id,CLOBVALUE) " +
							"values (?,?,?)";
						PreparedDBUtil updatedb = new PreparedDBUtil();
						updatedb.preparedInsert(sql);
						updatedb.setClob(3,StringUtil.replaceAll(values[i],"'","''"));
						updatedb.setInt(1, Integer.parseInt(ids[i]));
						updatedb.setInt(2, did);
						updatedb.executePrepared();
					}
					if (types[i].equals("0") && !values[i].equals(""))
					{
						sql = "insert into td_cms_extfieldvalue (field_id,document_id,NUMBERVALUE) " +
							"values (?,?,?)";
						PreparedDBUtil updatedb = new PreparedDBUtil();
						updatedb.preparedInsert(sql);
						updatedb.setInt(3, Integer.parseInt(values[i]));
						updatedb.setInt(1, Integer.parseInt(ids[i]));
						updatedb.setInt(2, did);
						updatedb.executePrepared();
					}
					if (types[i].equals("2"))
					{
						sql = "insert into td_cms_extfieldvalue (field_id,document_id,DATEVALUE) " +
							"values (?,?,?)";
						PreparedDBUtil updatedb = new PreparedDBUtil();
						updatedb.preparedInsert(sql);
						updatedb.setDate(3, format.parse(values[i]));
						updatedb.setInt(1, Integer.parseInt(ids[i]));
						updatedb.setInt(2, did);
						updatedb.executePrepared();
					}
					if (types[i].equals("5")||types[i].equals("6"))
					{
						sql = "insert into td_cms_extfieldvalue (field_id,document_id,CLOBVALUE) " +
							"values (?,?,?)";
						PreparedDBUtil updatedb = new PreparedDBUtil();
						updatedb.preparedInsert(sql);
						updatedb.setClob(3,StringUtil.replaceAll(values[i],"'","''"));
						updatedb.setInt(1, Integer.parseInt(ids[i]));
						updatedb.setInt(2, did);
						updatedb.executePrepared();
					}
					
					
				}
			}
			flag = true;
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new CustomFormManagerException(e.getMessage());
		}
		return flag;
	}
	
	/**
	 * 根据扩展字段ID取得其内容
	 * @param String id 
	 * 		扩展字段id
	 * @return java.util.List
	 * @throws CustomFormManagerException
	 */
	public DocExtField getExtFieldBy(String id) throws CustomFormManagerException
	{
		DocExtField field = new DocExtField();
		DBUtil db = new DBUtil();
		String sql = "";
		try
		{
			sql = "select * from TD_CMS_EXTFIELD where FIELD_ID="+id;
			db.executeSelect(sql);
			if(db.size()>0)
			{
				field.setFieldName(db.getString(0,"FIELDNAME"));
				field.setFieldType(db.getString(0,"FIELDTYPE"));
				field.setFieldLable(db.getString(0,"FIELDLABEL"));
				field.setFieldDesc(db.getString(0,"FIELDDESC"));
				field.setMaxlen(db.getInt(0,"MAXLEN"));
				field.setInputType(db.getInt(0,"INPUTTYPE"));
			}
			sql = "select * from TD_CMS_EXTVALUESCOPE where FIELD_ID="+id;
			db.executeSelect(sql);
			if(db.size()>0){
				field.setMinvalue(db.getInt(0,"MINVALUE"));
				field.setMaxvalue(db.getInt(0,"MAXVALUE"));
				ArrayList list = new ArrayList();
				for (int i=0;i<db.size();i++){
					String s = db.getString(i,"VALUE")+";"+db.getString(i,"DESCRIPTION");
					list.add(s);
				}
				field.setEno(list);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new CustomFormManagerException(e.getMessage());
		}
		return field;
	}
	/**
	 * 修改一项扩展字段
	 * @param <DocExtField>docExtField
	 * @return boolean
	 * @throws CustomFormManagerException
	 */
	public boolean updateDocExtField(DocExtField docExtField) throws CustomFormManagerException
	{
		boolean flag = false;
		PreparedDBUtil db = new PreparedDBUtil();
		DBUtil dbutil = new DBUtil(); 
		String id = String.valueOf(docExtField.getFieldId());
		String sql = "update TD_CMS_EXTFIELD set FIELDNAME='"+docExtField.getFieldName()+"',FIELDLABEL='"+docExtField.getFieldLable()+"'," +
				"FIELDDESC='"+docExtField.getFieldDesc()+"',FIELDTYPE='"+docExtField.getFieldType()+"'," +
				"MAXLEN="+String.valueOf(docExtField.getMaxlen())+",INPUTTYPE="+String.valueOf(docExtField.getInputType())+" where FIELD_ID="+id;
		try
		{
			db.executeUpdate(sql);
			sql  = "delete from td_cms_extvaluescope where field_id="+id;
			dbutil.executeDelete(sql);
			//保存取值范围信息
			if(docExtField.getInputType()==1)//枚举类型
			{
				ArrayList values = docExtField.getEno();
				for(int i=0;i<values.size();i++)
				{
					long autoId = dbutil.getNextPrimaryKey("td_cms_extvaluescope") ;
					
					sql = "insert into td_cms_extvaluescope(id,field_id,value,DESCRIPTION) values " +
							"("+ autoId +"," + id + ",'" + ((String)values.get(i)).split(";")[0] +"','" + ((String)values.get(i)).split(";")[1] +"')";
					dbutil.executeInsert(sql);
				}
			}
			if(docExtField.getInputType()==2)//连续类型
			{
				long autoId = dbutil.getNextPrimaryKey("td_cms_extvaluescope") ;
				
				sql = "insert into td_cms_extvaluescope(id,field_id,minvalue,maxvalue) values " +
						"("+ autoId +"," + id + "," + docExtField.getMinvalue() + "," + docExtField.getMaxvalue() +")";
				dbutil.executeInsert(sql);
			}
			flag = true;
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new CustomFormManagerException(e.getMessage());
		}
		return flag;
		
	}
	
	/**
	 * //站点or频道 1：站点 2：频道
		String type = request.getParameter("type");
		//站点id or 频道id
		String id = request.getParameter("id");
		String docid = request.getParameter("docid");
	 * @throws CustomFormManagerException 
	 */
	public List<DocExtField> getDataFieldList(String type,String id,String docid ) throws CustomFormManagerException 
	{
		
		
//		//站点or频道 1：站点 2：频道
//		String type = request.getParameter("type");
//		//站点id or 频道id
//		String id = request.getParameter("id");
//		String docid = request.getParameter("docid");
		
		
		
			
			
			try 
			{
				DBUtil dbUtil = new DBUtil();
				String sql = "";
				//站点
				if("1".equals(type))
					sql = "select * from td_cms_extfield order by field_id asc";
				//频道
				if("2".equals(type))
				{
					if(StringUtil.isEmpty(docid))
					{
						sql = "select * from td_cms_extfield a where a.field_id " +
							"in (select b.field_id from td_cms_channelfield b where b.channel_id = " + id + " and b.field_owner=0 )" +
							" order by a.field_id asc";
					}
					else
					{
						sql = "select * from td_cms_extfield a where a.field_id " +
								"in (select b.field_id from td_cms_channelfield b where (b.channel_id = " + id + " and b.field_owner=0) or (b.channel_id = " + docid + " and b.field_owner=1) )" +
								" order by a.field_id asc";
					}
				}
				dbUtil.executeSelect(sql);
				List<DocExtField> list = new ArrayList<DocExtField>();
				
				for (int i = 0; i < dbUtil.size(); i++) 
				{
					
//					t.fieldname t.fieldlabel t.fielddesc t.fieldtype t.maxlen t.inputtype
					DocExtField docExtField = new DocExtField();
					
					docExtField.setFieldId(dbUtil.getInt(i,"field_id"));
					docExtField.setFieldName(dbUtil.getString(i,"fieldname"));
					docExtField.setFieldLable(dbUtil.getString(i,"fieldlabel"));
					docExtField.setFieldDesc(dbUtil.getString(i,"fielddesc"));
					docExtField.setFieldType(dbUtil.getString(i,"fieldtype"));
					docExtField.setMaxlen(dbUtil.getInt(i,"maxlen"));
					docExtField.setInputType(dbUtil.getInt(i,"inputtype"));
					docExtField.setField_owner(dbUtil.getInt(i, "field_owner"));
					
					
					list.add(docExtField);
				}
				
				return list;
			}
			catch (Exception e) 
			{
				
				throw new CustomFormManagerException(e);
			}
		
	
	}
	
	/**
	 * //站点or频道 1：站点 2：频道
		String type = request.getParameter("type");
		//站点id or 频道id
		String id = request.getParameter("id");
		String docid = request.getParameter("docid");
	 * @throws CustomFormManagerException 
	 */
	public Map<String,DocExtField> getDataFieldMap(String type,String id,String docid ) throws CustomFormManagerException 
	{
		
		
//		//站点or频道 1：站点 2：频道
//		String type = request.getParameter("type");
//		//站点id or 频道id
//		String id = request.getParameter("id");
//		String docid = request.getParameter("docid");
		
		
		
			
			
			try 
			{
				DBUtil dbUtil = new DBUtil();
				String sql = "";
				//站点
				if("1".equals(type))
					sql = "select * from td_cms_extfield order by field_id asc";
				//频道
				if("2".equals(type))
				{
					if(StringUtil.isEmpty(docid))
					{
						sql = "select * from td_cms_extfield a where a.field_id " +
							"in (select b.field_id from td_cms_channelfield b where b.channel_id = " + id + " and b.field_owner=0 )" +
							" order by a.field_id asc";
					}
					else
					{
						sql = "select * from td_cms_extfield a where a.field_id " +
								"in (select b.field_id from td_cms_channelfield b where (b.channel_id = " + id + " and b.field_owner=0) or (b.channel_id = " + docid + " and b.field_owner=1) )" +
								" order by a.field_id asc";
					}
				}
				dbUtil.executeSelect(sql);
				 Map<String,DocExtField> list = new HashMap<String,DocExtField>();
				
				for (int i = 0; i < dbUtil.size(); i++) 
				{
					
//					t.fieldname t.fieldlabel t.fielddesc t.fieldtype t.maxlen t.inputtype
					DocExtField docExtField = new DocExtField();
					
					docExtField.setFieldId(dbUtil.getInt(i,"field_id"));
					docExtField.setFieldName(dbUtil.getString(i,"fieldname"));
					docExtField.setFieldLable(dbUtil.getString(i,"fieldlabel"));
					docExtField.setFieldDesc(dbUtil.getString(i,"fielddesc"));
					docExtField.setFieldType(dbUtil.getString(i,"fieldtype"));
					docExtField.setMaxlen(dbUtil.getInt(i,"maxlen"));
					docExtField.setInputType(dbUtil.getInt(i,"inputtype"));
					docExtField.setField_owner(dbUtil.getInt(i, "field_owner"));
					
					
					list.put(docExtField.getFieldName(),docExtField);
				}
				
				return list;
			}
			catch (Exception e) 
			{
				
				throw new CustomFormManagerException(e);
			}
		
	
	}
	
	

}
