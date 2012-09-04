/**
 * 输入类型实现类
 */
package com.frameworkset.platform.dictionary.input;

import java.util.ArrayList;
import java.util.List;

import org.frameworkset.event.EventHandle;

import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.common.poolman.DBUtil;

/**
 * @author Administrator
 *
 */
public class InputTypeManagerImpl extends EventHandle implements InputTypeManager {

	/**
	 * 新增输入类型信息
	 * @param inputType 输入类型实体对象
	 */
	public boolean insertInputType(InputType inputType) throws ManagerException {
		boolean success=false;
		try 
		{
			DBUtil db = new DBUtil();
			//判断输入名称是否重复
			StringBuffer judge = new StringBuffer()
			    .append("select count(*) as num from TB_SM_INPUTTYPE where INPUT_TYPE_NAME ='")
			    .append(inputType.getInputTypeName()).append("' ");
			db.executeSelect(judge.toString());
			if(db.size()>0 && db.getInt(0,"num")>0){//有输入名称重复的记录.
				return false;
			}
			int id = (int)db.getNextPrimaryKey("TB_SM_INPUTTYPE");
			//执行更新操作
			StringBuffer sql = new StringBuffer()
				.append("insert into TB_SM_INPUTTYPE(INPUT_TYPE_DESC,DATASOURCE_PATH,INPUT_TYPE_NAME,SCRIPT,INPUT_TYPE_ID) " )
				.append("values('").append(inputType.getInputTypeDesc()).append("' ")
				.append(",'").append(inputType.getDataSourcePath()).append("' ")
				.append(",'").append(inputType.getInputTypeName()).append("' ")
				.append(",'").append(inputType.getInputScript()).append("',").append(id).append(" )");
			db.executeInsert(sql.toString());
			success=true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return success;
	}
	/**
	 * 修改输入类型信息
	 * @param inputType 输入类型实体对象
	 */
	public boolean updateInputType(InputType inputType) throws ManagerException {
		boolean success=false;
		try 
		{
			DBUtil db = new DBUtil();
			//判断更新后的输入名称是否和其他输入类型的名称重复
			StringBuffer judge = new StringBuffer()
			    .append("select count(*) as num from TB_SM_INPUTTYPE where INPUT_TYPE_NAME ='")
			    .append(inputType.getInputTypeName()).append("' and INPUT_TYPE_ID !=")
			    .append(inputType.getInputTypeId());
			db.executeSelect(judge.toString());
			if(db.size()>0 && db.getInt(0,"num")>0){//有输入名称重复的记录.
				return false;
			}
			//执行更新操作
			StringBuffer sql = new StringBuffer()
			    .append("update TB_SM_INPUTTYPE t set t.DATASOURCE_PATH='").append(inputType.getDataSourcePath())
			    .append("',t.INPUT_TYPE_DESC='").append(inputType.getInputTypeDesc())
			    .append("',t.INPUT_TYPE_NAME='").append(inputType.getInputTypeName())
			    .append("',t.SCRIPT='").append(inputType.getInputScript()).append("' ")
			    .append(" where t.INPUT_TYPE_ID=").append(inputType.getInputTypeId());
			db.executeUpdate(sql.toString());
			success=true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return success;
	}
	/**
	 * 删除输入类型信息
	 * @param inputType 输入类型实体对象
	 * 如果改类型被引用了,不允许删除
	 * int 0:全部删除成功; 1:部分删除成功; 2:全部不能被删除
	 */
	public int deleteInputType(String[] inputTypeIds) throws ManagerException {
		int flag = 0;
		try 
		{
			DBUtil db = new DBUtil();
			DBUtil deleteBatch = new DBUtil();
			int canNotDeleteCount = 0;
			for(int i=0;i<inputTypeIds.length;i++){
				String inputTypeId = inputTypeIds[i];
				//判断是否被字典类型--附加字段引用.
				String judge = "select count(*) as num from TD_SM_DICATTACHFIELD where INPUT_TYPE_ID="+inputTypeId;
				db.executeSelect(judge);
				if(db.size()>0 && db.getInt(0,"num")>0){//被引用了 不能删除, 不能删除的计数+1
					canNotDeleteCount ++ ;
				}else{//没被引用,可以删除
					String sql="delete from TB_SM_INPUTTYPE t where t.INPUT_TYPE_ID = "+inputTypeId;
					deleteBatch.addBatch(sql);
				}
//				自动过滤被引用的输入类型,无法给出提示
//				StringBuffer delete_sql = new StringBuffer()
//					.append("delete TB_SM_INPUTTYPE t where t.INPUT_TYPE_ID = ")
//					.append("(select '").append(inputTypeId).append("' as INPUT_TYPE_ID from dual not exists ")
//					.append("(select * from TD_SM_DICATTACHFIELD where INPUT_TYPE_ID=")
//					.append(inputTypeId).append("))");
				
			}
			deleteBatch.executeBatch();
			if(canNotDeleteCount==0){//全部删除成功
				flag = this.ALL_DELETE;
			}else if(canNotDeleteCount==inputTypeIds.length){//全部删除不成功
				flag = this.NO_DELETE;
			}else{//部分删除成功
				flag = this.PART_DELETE;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 获取输入类型信息
	 * @return list<InputType>
	 */
	public List getInputTypeList() throws ManagerException {
		List inputTypes = new ArrayList();
		DBUtil dbUtil = new DBUtil();
		try {
			String sql ="select INPUT_TYPE_ID,INPUT_TYPE_DESC,DATASOURCE_PATH,INPUT_TYPE_NAME,SCRIPT " +
					    "from TB_SM_INPUTTYPE ";
			dbUtil.executeSelect(sql);
			InputType type  = null;
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				type  = new InputType();				
				type.setInputTypeId(Integer.valueOf(dbUtil.getString(i,"INPUT_TYPE_ID")));
				type.setInputTypeDesc(dbUtil.getString(i,"INPUT_TYPE_DESC"));
				type.setDataSourcePath(dbUtil.getString(i,"DATASOURCE_PATH"));
				type.setInputScript(dbUtil.getString(i,"SCRIPT"));
				type.setInputTypeName(dbUtil.getString(i,"INPUT_TYPE_NAME"));
				inputTypes.add(type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputTypes;
	}
	
	/**
	 * 根据字典类型ID,获取与之关联的输入类型列表
	 * @return list<InputType>
	 */
	public List getInputTypeList(String dicttypeId) throws ManagerException {
		List inputTypes = new ArrayList();
		DBUtil dbUtil = new DBUtil();
		try {
			String sql ="select t.INPUT_TYPE_ID,t.INPUT_TYPE_DESC,t.DATASOURCE_PATH,t.INPUT_TYPE_NAME,t.SCRIPT " +
					    "from TB_SM_INPUTTYPE t,TD_SM_DICATTACHFIELD d where t.INPUT_TYPE_ID=d.INPUT_TYPE_ID "+ 
					    "and d.DICTTYPE_ID='" + dicttypeId + "'";
			dbUtil.executeSelect(sql);
			InputType type  = null;
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				type  = new InputType();				
				type.setInputTypeId(Integer.valueOf(dbUtil.getString(i,"INPUT_TYPE_ID")));
				type.setInputTypeDesc(dbUtil.getString(i,"INPUT_TYPE_DESC"));
				type.setDataSourcePath(dbUtil.getString(i,"DATASOURCE_PATH"));
				type.setInputScript(dbUtil.getString(i,"SCRIPT"));
				type.setInputTypeName(dbUtil.getString(i,"INPUT_TYPE_NAME"));
				inputTypes.add(type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputTypes;
	}
	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.input.InputTypeManager#isColumnUsedByDictType(java.lang.String, java.lang.String)
	 */
	public boolean isColumnUsedByDictType(String dicttypeId, String columnName) {
		boolean flag = false;
		if(dicttypeId==null || dicttypeId.trim().length()==0 || columnName==null || columnName.trim().length()==0){
			return flag;
		}
		DBUtil dbUtil = new DBUtil();
		try {
			String sql ="select count(*) as num from TD_SM_DICATTACHFIELD d where  d.DICTTYPE_ID='" +
					    dicttypeId + "' and UPPER(d.TABLE_COLUMN)='" + columnName.toUpperCase() + "'";
			dbUtil.executeSelect(sql);			
			if(dbUtil.size()>0 && dbUtil.getInt(0,"num")>0){
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.input.InputTypeManager#getInputTypeInfoById(java.lang.String)
	 */
	public InputType getInputTypeInfoById(String id) {
		InputType type  = null;
		if(id==null || id.trim().length()==0) return type;
		StringBuffer sql = new StringBuffer();
		DBUtil dbUtil = new DBUtil();
		sql.append("select t.INPUT_TYPE_ID,t.INPUT_TYPE_DESC,t.DATASOURCE_PATH,t.INPUT_TYPE_NAME,t.SCRIPT ")
		    .append("from TB_SM_INPUTTYPE t where t.INPUT_TYPE_ID='").append(id).append("'");
		try {			
			dbUtil.executeSelect(sql.toString());			
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				type  = new InputType();				
				type.setInputTypeId(Integer.valueOf(dbUtil.getString(0,"INPUT_TYPE_ID")));
				type.setInputTypeDesc(dbUtil.getString(0,"INPUT_TYPE_DESC"));
				type.setDataSourcePath(dbUtil.getString(0,"DATASOURCE_PATH"));
				type.setInputScript(dbUtil.getString(0,"SCRIPT"));
				type.setInputTypeName(dbUtil.getString(0,"INPUT_TYPE_NAME"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return type;
	}
}
