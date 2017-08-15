package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.platform.config.ConfigException;
import com.frameworkset.platform.config.model.ResourceInfo;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.platform.sysmgrcore.entity.Roleresop;
import com.frameworkset.util.ListInfo;

public class RoleResList extends DataInfoImpl implements Serializable{
	private Logger logger = LoggerFactory.getLogger(RoleResList.class.getName());

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		String restypeId=request.getParameter("restypeId");
		String roleId = request.getParameter("roleId");
		if("".equals(roleId) && roleId==null){
			roleId = (String)session.getAttribute("currRoleId");
		}
		String resdescribe=request.getParameter("resdescribe");
		String operatetype=request.getParameter("operatetype");
		String resId = request.getParameter("resId");
//		if(resdescribe==null){
//			resdescribe="";
//		}
//		if(operatetype==null){
//			operatetype="";
//		}
//		System.out.println("restypeId========================="+restypeId);
//		System.out.println("roleId============================"+roleId);
//		System.out.println("resdescribe======================="+resdescribe);
//		System.out.println("operatetype======================="+operatetype);
//			 根据角色和资源类型得资源列表
		List list = new ArrayList();
		StringBuffer sql= new StringBuffer();
		sql.append("select * from td_sm_roleresop t where t.types='role' and t.role_id='")
		   .append(roleId).append("' ");
		if(!"".equals(restypeId) && restypeId!=null){
			sql.append("and t.restype_id='").append(restypeId).append("' ");
		}
		if(!"".equals(resdescribe) && resdescribe!=null){
			sql.append("and t.res_name like '%").append(resdescribe).append("%' ");
		}
		if(!"".equals(operatetype) && operatetype!=null){
			sql.append("and t.op_id='").append(operatetype).append("' ");
		}
		if(!"".equals(resId) && resId!=null){
			sql.append("and t.res_id like '%").append(resId).append("%' ");
		}
		sql.append("order by t.res_name");
//		if(resdescribe==""&&operatetype==""){
//			if(operatetype==""||resdescribe==""){
//				sql  = "select roleresop0_.OP_ID as OP_ID, roleresop0_.RES_ID as RES_ID, roleresop0_.ROLE_ID as ROLE_ID, roleresop0_.RESTYPE_ID as RESTYPE_ID, roleresop0_.RES_NAME as RES_NAME, roleresop0_.auto as auto from td_sm_roleresop roleresop0_ where roleresop0_.ROLE_ID='"+roleId+"' and roleresop0_.RESTYPE_ID='"+restypeId+"'";
//			}else{
//				if(operatetype==""){
//					sql  = "select roleresop0_.OP_ID as OP_ID, roleresop0_.RES_ID as RES_ID, roleresop0_.ROLE_ID as ROLE_ID, roleresop0_.RESTYPE_ID as RESTYPE_ID, roleresop0_.RES_NAME as RES_NAME, roleresop0_.auto as auto from td_sm_roleresop roleresop0_ where roleresop0_.ROLE_ID='"+roleId+"' and roleresop0_.RESTYPE_ID='"+restypeId+"' and roleresop0_.OP_ID like '%"+resdescribe+"%'";
//				}else{
//					sql  = "select roleresop0_.OP_ID as OP_ID, roleresop0_.RES_ID as RES_ID, roleresop0_.ROLE_ID as ROLE_ID, roleresop0_.RESTYPE_ID as RESTYPE_ID, roleresop0_.RES_NAME as RES_NAME, roleresop0_.auto as auto from td_sm_roleresop roleresop0_ where roleresop0_.ROLE_ID='"+roleId+"' and roleresop0_.RESTYPE_ID='"+restypeId+"' and roleresop0_.OP_ID like '%"+operatetype+"%'";
//				}
//			}
//		}else{
//			sql  = "select roleresop0_.OP_ID as OP_ID, roleresop0_.RES_ID as RES_ID, roleresop0_.ROLE_ID as ROLE_ID, roleresop0_.RESTYPE_ID as RESTYPE_ID, roleresop0_.RES_NAME as RES_NAME, roleresop0_.auto as auto from td_sm_roleresop roleresop0_ where roleresop0_.ROLE_ID='"+roleId+"' and roleresop0_.RESTYPE_ID='"+restypeId+"' and roleresop0_.RES_NAME like '%"+resdescribe+"%' and roleresop0_.OP_ID like '%"+operatetype+"%'";
//		}
		DBUtil dbUtil = new DBUtil();
		ResourceManager rsManager = new ResourceManager();	
		try {
			dbUtil.executeSelect(sql.toString(),(int)offset,maxPagesize);
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				String typeName = "";
				ResourceInfo resourceInfo = rsManager.getResourceInfoByType(dbUtil.getString(i,"restype_id"));
				if(resourceInfo != null)
					typeName = resourceInfo.getName();		
				else
				{
					typeName = "未知"; 
				}
				
				String opName = "";
				com.frameworkset.platform.config.model.Operation operation  = null;
				
				try {
					String gresid  = rsManager.getGlobalResourceid(dbUtil.getString(i,"restype_id"));
					if(gresid == null || !gresid.equals(dbUtil.getString(i,"RES_ID")))
					{
						operation  = rsManager.getOperation(dbUtil.getString(i,"restype_id"),dbUtil.getString(i, "op_id"));
						 
					}
					else
					{
						operation  = rsManager.getGlobalOperation(dbUtil.getString(i,"restype_id"),dbUtil.getString(i, "op_id"));
					}
					
					
				} catch (ConfigException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(operation != null)
				{
				    opName = operation.getName();
				}
				else
				{
					opName = "未知";
				}
				
				Roleresop roo = new Roleresop();
				roo.setAuto(dbUtil.getString(i,"auto"));
				roo.setOpId(dbUtil.getString(i,"OP_ID"));
				roo.setResId(dbUtil.getString(i,"RES_ID"));
				roo.setRoleId(dbUtil.getString(i,"ROLE_ID"));
				roo.setRestypeId(dbUtil.getString(i,"RESTYPE_ID"));
				roo.setResName(dbUtil.getString(i,"RES_NAME"));
				roo.setOpName(opName);
				roo.setResTypeName(typeName);
				list.add(roo);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		} catch (SQLException e) {
			e.getMessage();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}
}