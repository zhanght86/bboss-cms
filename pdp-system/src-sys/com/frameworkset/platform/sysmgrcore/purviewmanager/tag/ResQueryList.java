package com.frameworkset.platform.sysmgrcore.purviewmanager.tag;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.config.ConfigException;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.platform.sysmgrcore.entity.ResPermission;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.orm.adapter.DB;
import com.frameworkset.util.ListInfo;

public class ResQueryList extends DataInfoImpl implements Serializable  {

	protected ListInfo getDataList(String sortKey, boolean desc) {
		return null;
	}

	protected ListInfo getDataList(String sortKey, boolean desc, long offSet,
			int pageItemsize) {
		//资源类别   必选
		String restypeIdValue = request.getParameter("restypeId");
		String restypeId = null;
		if(restypeIdValue != null){
			String[] restypeIdValues = restypeIdValue.split("\\$\\$");
			restypeId = restypeIdValues[0];
		}
		//资源类别对应的res_id和res_name  必选
		String resId = request.getParameter("resId");
		String resName = request.getParameter("resName");
		//资源类别对应的操作ID   可选
		String opId = request.getParameter("operategroup");
		
		//授予的对象查询，包括（机构，用户，角色，机构岗位）  必选
		String type = request.getParameter("type");
		//授予对象的查询范围	必选
		String selid = request.getParameter("selid");
		String selname = request.getParameter("selname");
		
		//是否递归
		String isRecursion = request.getParameter("isRecursion");
		
		Map param = new HashMap();
		param.put("restypeId", restypeId);
		param.put("resId", resId);
		param.put("resName", resName);
		param.put("opId", opId);
		param.put("type", type);
		param.put("selid", selid);
		param.put("selname", selname);
		param.put("isRecursion", isRecursion);
		
		
		if(restypeId != null && resId != null && type != null && selid != null){
			return getResList(param, offSet, pageItemsize);
		}else{
			return null;
		}
	}
	
	private ListInfo getResList(Map param, long offSet, int pageItemsize){
		String restypeId = (String)param.get("restypeId");
		String resId = (String)param.get("resId");
		String resName = (String)param.get("resName");
		String opId = (String)param.get("opId");
		String type = (String)param.get("type");
		String selid = (String)param.get("selid");
		String selname = (String)param.get("selname");
		String isRecursion = (String)param.get("isRecursion");
		
		DB dbconcat = DBUtil.getDBAdapter();
		
		//递归查询子机构
//		String sql_org = "select org_id from td_sm_organization start with org_id='"+selid
//			+"' connect by prior org_id=parent_id";
		String concat_ = DBUtil.getDBAdapter().concat(" org_tree_level","'|%' ");
		String sql_org = "select t.org_id from TD_SM_ORGANIZATION t where t.org_tree_level like (select "
			             + concat_
			             + " from TD_SM_ORGANIZATION c  where c.org_id = '"  +
                          selid + "') or t.org_id = '" + selid + "'";
                               
		
		ResourceManager rsManager = new ResourceManager();
		StringBuffer sql = new StringBuffer();
		if(type.equals("user")){
			sql.append("select * from ( ")
				.append("SELECT b.remark5 AS remark5, b.user_id, b.user_name, b.user_realname, a.op_id, ")
				.append("'用户自身资源' AS resresource, a.authorization_stime, a.AUTO ")
				.append("FROM td_sm_roleresop a ")
				.append("INNER JOIN ")
				.append("(SELECT DISTINCT b.user_id, b.user_name, b.user_realname,c.org_name as remark5 ")
//				.append("(SELECT DISTINCT b.user_id, b.user_name, b.user_realname,nvl(c.remark5,c.org_name) as remark5 ")
				.append("FROM td_sm_userjoborg a RIGHT JOIN td_sm_user b ")
				.append("ON a.user_id = b.user_id,td_sm_organization c ")
				.append("WHERE a.org_id in (");
			if(isRecursion.equals("0")){
				sql.append("'").append(selid).append("'");
			}else{
				sql.append(sql_org);
			}
			
			String concat1 = dbconcat.concat("'来自用户自身角色【'","d.role_name","'】'");
			sql.append(") and c.org_id=a.org_id) b ON a.role_id = b.user_id ")
				.append("WHERE a.restype_id = '").append(restypeId).append("' AND a.TYPES = 'user' ").append(" and a.res_id = '").append(resId).append("' ")
				
				.append("UNION ")
				.append("SELECT b.remark5 AS remark5, b.user_id, b.user_name, b.user_realname, a.op_id, ").append(concat1).append(" AS resresource, ")
				.append("a.authorization_stime, a.AUTO ")
				.append("FROM td_sm_roleresop a, ")
//				.append("(SELECT DISTINCT b.user_id, b.user_name, b.user_realname,nvl(c.remark5,c.org_name) as remark5 ")
				.append("(SELECT DISTINCT b.user_id, b.user_name, b.user_realname,c.org_name as remark5 ")
				.append("FROM td_sm_userjoborg a RIGHT JOIN td_sm_user b ")
				.append("ON a.user_id = b.user_id,td_sm_organization c ")
				.append("WHERE a.org_id in (");
			if(isRecursion.equals("0")){
				sql.append("'").append(selid).append("'");
			}else{
				sql.append(sql_org);
			}
			String concat2 = dbconcat.concat("'来自【'","b.remark5","'】机构的角色【'","d.role_name","'】'");
			sql.append(") and a.org_id=c.org_id) b, ")
				.append("td_sm_userrole c LEFT JOIN td_sm_role d ON c.role_id = d.role_id ")
				.append("WHERE a.restype_id = '").append(restypeId).append("' ")
				.append("AND a.TYPES = 'role' ")
				.append("AND a.res_id = '").append(resId).append("' ")
				.append("AND c.user_id = b.user_id ")
				.append("AND c.role_id = a.role_id ")
				
				.append("UNION ")
				.append("SELECT b.remark5 AS remark5, b.user_id, b.user_name, b.user_realname, a.op_id, ").append(concat2).append(" AS resresource, ")
				.append("a.authorization_stime, a.AUTO ")
				.append("FROM td_sm_roleresop a, ")
				.append("(SELECT DISTINCT b.user_id, b.user_name, b.user_realname, c.role_id, d.org_name as remark5 ")
//				.append("(SELECT DISTINCT b.user_id, b.user_name, b.user_realname, c.role_id, nvl(d.remark5,d.org_name) as remark5 ")
				.append(" FROM td_sm_userjoborg a RIGHT JOIN td_sm_user b ")
				.append("ON a.user_id = b.user_id ")
				.append(", td_sm_orgrole c, td_sm_organization d ")
				.append("WHERE a.org_id in (");
			if(isRecursion.equals("0")){
				sql.append("'").append(selid).append("'");
			}else{
				sql.append(sql_org);
			}
			String concat3 = dbconcat.concat("'来自机构【'","b.remark5","'】'");
			sql.append(") AND c.org_id = a.org_id and a.org_id = d.org_id) b, ")
				.append("td_sm_role d ")
				.append("WHERE a.restype_id = '").append(restypeId).append("' ")
				.append("AND a.TYPES = 'role' ")
				.append("AND a.res_id = '").append(resId).append("' ")
				.append("AND a.role_id = b.role_id ")
				.append("AND b.role_id = d.role_id ")
				
				.append("UNION ")
				.append("SELECT b.remark5 AS remark5, b.user_id, b.user_name, b.user_realname, a.op_id, ").append(concat3).append(" AS resresource, a.authorization_stime, a.AUTO ")
				.append("FROM td_sm_roleresop a, ")
				.append("(SELECT DISTINCT b.user_id, b.user_name, b.user_realname,c.org_name as remark5,c.org_id ")
//				.append("(SELECT DISTINCT b.user_id, b.user_name, b.user_realname,nvl(c.remark5,c.org_name) as remark5,c.org_id ")
				.append("FROM td_sm_userjoborg a RIGHT JOIN td_sm_user b ")
				.append("ON a.user_id = b.user_id,td_sm_organization c ")
				.append("WHERE a.org_id in (");
			if(isRecursion.equals("0")){
				sql.append("'").append(selid).append("'");
			}else{
				sql.append(sql_org);
			}
			sql.append(") and a.org_id = c.org_id) b ")
				.append("WHERE a.restype_id = '").append(restypeId).append("' ")
				.append("AND a.TYPES = 'organization' ")
				.append("AND a.res_id = '").append(resId).append("' ")
				.append("AND role_id = ");
			if(isRecursion.equals("0")){
				sql.append("'").append(selid).append("' ");
			}else{
				sql.append("b.org_id ");
			}
			String concat4 = dbconcat.concat("'来自岗位【'","b.job_name","'】下的角色【'","b.role_name"," '】'");
			sql.append("UNION ")
				.append("SELECT b.remark5 AS remark5, b.user_id, b.user_name, b.user_realname, a.op_id, ").append(concat4).append(" AS resresource,a.authorization_stime, a.AUTO ")
				.append("FROM td_sm_roleresop a, ")
				.append("(SELECT DISTINCT b.user_id, b.user_name, b.user_realname, a.job_id, ")
				.append("d.role_name, d.role_id, e.job_name,f.org_name as remark5 ")
//				.append("d.role_name, d.role_id, e.job_name,nvl(f.remark5,f.org_name) as remark5 ")
				.append("FROM td_sm_userjoborg a RIGHT JOIN td_sm_user b ")
				.append("ON a.user_id = b.user_id ")
				.append(", td_sm_orgjobrole c, td_sm_role d, td_sm_job e,td_sm_organization f ")
				.append("WHERE a.org_id in (");
			if(isRecursion.equals("0")){
				sql.append("'").append(selid).append("'");
			}else{
				sql.append(sql_org);
			}
			sql.append(") AND c.org_id = a.org_id ")
				.append("AND c.role_id = d.role_id AND e.job_id = a.job_id AND e.job_id <> '1' and c.org_id=f.org_id) b ")
				.append("WHERE a.restype_id = '").append(restypeId).append("' AND a.TYPES = 'role' ")
				.append("AND a.res_id = '").append(resId).append("' AND a.role_id = b.role_id ) a ")
				.append("where 1 = 1 ");
			if(opId != null && !"".equals(opId)){
				sql.append(" and a.op_id = '").append(opId).append("' ");
			}
			sql.append("order by a.user_name,a.op_id");
		}else if(type.equals("role")){
			sql.append("SELECT b.role_name, c.type_name, d.user_realname, a.op_id, ")
				.append("'角色自身资源' AS resresource, a.authorization_stime,a.auto ")
				.append("FROM td_sm_roleresop a, td_sm_role b, td_sm_roletype c, td_sm_user d ")
				.append("WHERE a.TYPES = 'role' ")
				.append("AND a.restype_id = '").append(restypeId).append("' ")
				.append("AND a.res_id = '").append(resId).append("' ");
			if(selid != null && !"".equals(selid)){
				sql.append("AND a.role_id in (").append("select role_id from td_sm_role where ROLE_TYPE='").append(selid).append("') ");
			}
			sql.append(" AND a.role_id = b.role_id AND b.role_type = c.type_id AND b.owner_id = d.user_id ");
			if(opId != null && !"".equals(opId)){
				sql.append(" and a.op_id = '").append(opId).append("' ");
			}
			sql.append("order by c.type_name,b.role_name");
				
				
		}else if(type.equals("org")){
			sql.append("select * from (")
			 	.append("select b.remark5, a.op_id,'机构自身资源' AS resresource, a.authorization_stime,a.auto ")
				.append("from td_sm_roleresop a,(select org_id,remark5 from td_sm_organization where org_id in (");
			if(isRecursion.equals("0")){
				sql.append("'").append(selid).append("'");
			}else{
				sql.append(sql_org);
			}
			sql.append(")) b where types='organization' and b.org_id = a.role_id and a.restype_id='")
				.append(restypeId).append("' and a.res_id = '").append(resId).append("' ");
			if(opId != null && !"".equals(opId)){
				sql.append(" and a.op_id = '").append(opId).append("' ");
			}
			String concat5 = dbconcat.concat("'来自角色【'","b.role_name","'】'");
			sql.append(" union ")
				.append("select b.remark5,a.op_id,").append(concat5).append(" AS resresource, a.authorization_stime,a.auto ")
				.append("from td_sm_roleresop a,")
				.append("(select org.remark5,r.role_name from td_sm_organization org,td_sm_orgrole orgr,td_sm_role r ")
				.append("where r.role_id=orgr.role_id and org.org_id=orgr.org_id and org.org_id in(");
			if(isRecursion.equals("0")){
				sql.append("'").append(selid).append("'");
			}else{
				sql.append(sql_org);
			}
			sql.append(") ) b where a.types='role' ")
				.append(" and a.restype_id='").append(restypeId).append("' and a.res_id = '").append(resId).append("' ");
			if(opId != null && !"".equals(opId)){
				sql.append(" and a.op_id = '").append(opId).append("' ");
			}
			sql.append(") a order by a.remark5 ");
			
		}else if(type.equals("orgjob")){
			String concat6 = dbconcat.concat("'来自角色【'","b.role_name","'】'");
			sql.append("SELECT b.remark5 AS remark5, b.job_name, b.user_realname, a.op_id, ").append(concat6).append(" AS resresource, ")
				.append("a.authorization_stime, a.AUTO ")
				.append("FROM td_sm_roleresop a, ")
				.append("(SELECT a.role_id, b.role_name, c.job_name, d.user_realname,e.org_name as remark5 ")
//				.append("(SELECT a.role_id, b.role_name, c.job_name, d.user_realname,nvl(e.remark5,e.org_name) as remark5 ")
				.append("FROM td_sm_orgjobrole a, td_sm_role b, td_sm_job c, td_sm_user d,td_sm_organization e ")
				.append("WHERE a.org_id in (");
			if(isRecursion.equals("0")){
				sql.append("'").append(selid).append("'");
			}else{
				sql.append(sql_org);
			}
			sql.append(") AND a.role_id = b.role_id AND a.job_id = c.job_id AND c.owner_id = d.user_id and a.org_id=e.org_id) b ")
				.append("WHERE a.role_id = b.role_id ")
				.append("and a.restype_id='").append(restypeId).append("' ")
				.append("AND a.TYPES = 'role' and a.res_id = '").append(resId).append("' ");
			if(opId != null && !"".equals(opId)){
				sql.append(" and a.op_id = '").append(opId).append("' ");
			}
			sql.append("order by b.job_name,a.op_id ");
		}
		DBUtil db = new DBUtil();
		ListInfo listInfo = new ListInfo();
		
		try {
			System.out.println("sql:"+sql.toString());
			String gresid  = rsManager.getGlobalResourceid(restypeId);
			db.executeSelect(sql.toString(), offSet, pageItemsize);
			if(db.size() > 0){
				int size = db.size();
				List list = new ArrayList();
				ResPermission resPermission = null;
				com.frameworkset.platform.config.model.Operation operation  = null;
				for(int i = 0; i < size; i++){
					resPermission = new ResPermission();
					String op_id = db.getString(i, "op_id");
					if(gresid == null || !gresid.equals(resId)){
						operation  = rsManager.getOperation(restypeId,op_id);
						 
					}else{
						operation  = rsManager.getGlobalOperation(restypeId,op_id);
					}
					resPermission.setOpId(op_id);
					if(operation == null){
						resPermission.setOpName("未知资源("+op_id+")");
					}else{
						resPermission.setOpName(operation.getName());
					}
					System.out.println("resresource = " + db.getString(i, "resresource"));
					resPermission.setResResource(db.getString(i, "resresource"));
					resPermission.setSDate(db.getDate(i, "AUTHORIZATION_STIME"));
					resPermission.setAuto(db.getString(i, "auto"));
					
					if(type.equals("user")){
						resPermission.setUserRealname(db.getString(i, "user_realname"));
						resPermission.setRemark5(db.getString(i, "remark5"));
						resPermission.setUserId(db.getString(i, "user_id"));
						resPermission.setUserName(db.getString(i, "user_name"));
						
					}else if(type.equals("role")){
						resPermission.setUserRealname(db.getString(i, "user_realname"));
						resPermission.setRoleName(db.getString(i, "role_name"));
						resPermission.setRoleTypeName(db.getString(i, "type_name"));
					}else if(type.equals("orgjob")){
						resPermission.setUserRealname(db.getString(i, "user_realname"));
						resPermission.setRemark5(db.getString(i, "remark5"));
						resPermission.setJobName(db.getString(i, "job_name"));
					}else if(type.equals("org")){
						resPermission.setRemark5(db.getString(i, "remark5"));
					}
					list.add(resPermission);
				}
				listInfo.setDatas(list);
				listInfo.setTotalSize(db.getTotalSize());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ConfigException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
		return listInfo;
	}

}
