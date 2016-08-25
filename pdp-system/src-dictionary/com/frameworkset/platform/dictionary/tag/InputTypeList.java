package com.frameworkset.platform.dictionary.tag;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.dictionary.input.InputType;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 输入类型列表
 * @author zjx
 *
 */
public class InputTypeList extends DataInfoImpl implements java.io.Serializable{

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		String descCondition = request.getParameter("typeName");
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		try {
			String condition="";
			//判断当前查询条件是否为空
			if(descCondition!=null && descCondition!=""){
				condition = " where t.INPUT_TYPE_NAME like '%" + descCondition + "%' ";
			}
			String sql = "select INPUT_TYPE_ID,INPUT_TYPE_DESC,DATASOURCE_PATH,INPUT_TYPE_NAME,SCRIPT " +
					     " from TB_SM_INPUTTYPE t "+condition;
			dbUtil.executeSelect(sql,(int)offset,maxPagesize);
			List inputTypes = new ArrayList();
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
			listInfo.setDatas(inputTypes);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}
}