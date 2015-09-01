/*
 * Created on 2006-3-12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.purviewmanager.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.persitent.util.SQLUtil;

import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

/**
 * @author ok
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class OrgSearchList extends DataInfoImpl implements Serializable {

	private SQLUtil sqlUtilInsert = SQLUtil
			.getInstance("org/frameworkset/insert.xml");

	// protected ListInfo getDataList(String sortKey, boolean desc, long offset,
	// int maxPagesize) {
	//    	
	// ListInfo listInfo = new ListInfo();
	//
	// // String orgName =
	// StringUtil.replaceNull(request.getParameter("orgName"));
	// String orgnumber =
	// StringUtil.replaceNull(request.getParameter("orgnumber"));
	// String
	// orgcreator=StringUtil.replaceNull(request.getParameter("orgcreator"));
	// //机构别名显示名称
	// String remark5 = StringUtil.replaceNull(request.getParameter("remark5"));
	//        
	// // String userId = super.accessControl.getUserID();
	// // String userAccount = super.accessControl.getUserAccount();
	//     
	// try{
	// // OrgManager orgManager = SecurityDatabase.getOrgManager();
	// List list = null;
	// // PageConfig pageConfig = orgManager.getPageConfig();
	// // pageConfig.setPageSize(maxPagesize);
	// // pageConfig.setStartIndex((int)offset);
	// String subsql =
	// "select org_id from td_sm_organization where parent_id='0'";
	// 			
	// StringBuffer sql = new StringBuffer()
	// .append("select abc.* from(");
	// // System.out.println(sql);
	// sql.append("select o.org_id,o.parent_id,o.org_name,o.orgnumber,o.creator,o.org_sn,o.orgdesc,o.remark5,level as num,u.user_name,u.user_realname from TD_SM_ORGANIZATION o ")
	// .append(" left join td_sm_user u on u.user_id=o.creator ");
	// sql.append("start with o.ORG_ID in ("+subsql+")  connect by prior o.ORG_ID=o.PARENT_ID order SIBLINGS by o.org_sn asc)");
	// sql.append("abc where 1=1 ");
	//            
	// if(super.accessControl.isAdmin())
	// {}
	// else
	// { //不是系统管理员需要权限过滤----sql语句性能太差
	// // sql.append(" and org_id in (")
	// // .append(" select t.org_id from td_sm_organization t ")
	// // .append(" where t.org_id in ")
	// // .append("(")
	// //
	// .append(" select uo.org_id from v_tb_res_org_user uo where uo.user_id='").append(super.accessControl.getUserID()).append("' ")
	// // .append( " union ")
	// // .append(
	// " select om.org_id from td_sm_organization om start with om.org_id in ")
	// // .append(
	// " (select mm.org_id from td_sm_orgmanager mm where mm.user_id='").append(
	// super.accessControl.getUserID() ).append( "') ")
	// // .append( " connect by prior om.org_id = om.parent_id ")
	// // .append( " ) ")
	// // .append( " start with t.parent_id ='0' ")
	// // .append( "connect by prior t.org_id=t.parent_id ) ");
	// String curUserId = super.accessControl.getUserID();
	// StringBuffer all_orgs = new StringBuffer()//可管理机构集合
	// .append("select org_id from td_sm_organization where org_id in (")
	// .append("select distinct org.org_id from td_sm_organization org start with org.org_id in(")
	// .append("select o.org_id from td_sm_organization o, td_sm_orgmanager om where ")
	// .append("o.org_id = om.org_id and om.user_id='").append(curUserId).append("') ")
	// .append(" connect by prior org.org_id = org.parent_id) ");
	// sql.append(" and org_id in (").append(all_orgs).append(") ");
	// } //System.out.println(sql);
	// if (remark5.equals("") && orgnumber.equals("") &&orgcreator.equals("")) {
	// DBUtil count = new DBUtil();
	// count.executeSelect(sql.toString());
	// //sql += " order by  abc.orgnumber asc,abc.org_sn asc ";
	// list = this.getQueryResult(sql.toString(),offset,maxPagesize);
	// listInfo.setTotalSize(count.size());
	// listInfo.setDatas(list);
	//                
	// }else{
	//	 				
	// if (!remark5.equals("")) {
	// sql.append( " and abc.remark5 like '%" ).append( remark5 ).append( "%'");
	// }
	// if (!orgnumber.equals("")) {
	// sql.append( " and abc.orgnumber like '%").append(
	// orgnumber).append("%'");
	// }
	// if (!orgcreator.equals("")) {
	// sql.append( " and abc.user_name like '%").append(orgcreator).append(
	// "%'");
	// }
	// //System.out.println(sql);
	// list = this.getQueryResult(sql.toString(),offset,maxPagesize);
	//	                
	// DBUtil count = new DBUtil();
	// count.executeSelect(sql.toString());
	// listInfo.setTotalSize(count.size());
	// listInfo.setDatas(list);
	//	                
	// }
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	// return listInfo;
	// }
	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// com.frameworkset.common.tag.pager.DataInfoImpl#getDataList(java.lang.String,
	// * boolean)
	// */
	// protected ListInfo getDataList(String arg0, boolean arg1) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//    
	// /**
	// *
	// * @param sql
	// * @return
	// * LogSearchList.java
	// * @author: ge.tao
	// */
	// protected List getQueryResult(String sql,long offset,int maxItem){
	// List datas = new ArrayList();
	// DBUtil db = new DBUtil();
	// try{
	// db.executeSelect(sql,offset,maxItem);
	//               		
	// Organization org = null;
	// for(int i=0; i<db.size(); i++){
	// org = new Organization();
	// org.setOrgId(db.getString(i,"org_id".toUpperCase()));
	// org.setParentId(db.getString(i,"parent_id".toUpperCase()));
	// org.setOrgName(db.getString(i,"org_name".toUpperCase()));
	// org.setOrgnumber(db.getString(i,"orgnumber".toUpperCase()));
	// org.setOrgSn(db.getString(i,"org_sn".toUpperCase()));
	// org.setOrgdesc(db.getString(i,"orgdesc".toUpperCase()));
	// org.setRemark5(db.getString(i,"remark5".toUpperCase()));
	// org.setCreator(db.getString(i,"creator".toUpperCase()));
	//    			
	// datas.add(org);
	// }
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	// return datas;
	// }

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {

		ListInfo listInfo = new ListInfo();

		// String orgName =
		// StringUtil.replaceNull(request.getParameter("orgName"));
		String orgnumber = StringUtil.replaceNull(request
				.getParameter("orgnumber"));
		String orgcreator = StringUtil.replaceNull(request
				.getParameter("orgcreator"));
		// 机构别名显示名称
		String remark5 = StringUtil
				.replaceNull(request.getParameter("remark5"));
		
		String isEffective = StringUtil.replaceNull(request
                .getParameter("isEffective"));
		// String userId = super.accessControl.getUserID();
		// String userAccount = super.accessControl.getUserAccount();

		try {
			// OrgManager orgManager = SecurityDatabase.getOrgManager();
			List list = null;
			// PageConfig pageConfig = orgManager.getPageConfig();
			// pageConfig.setPageSize(maxPagesize);
			// pageConfig.setStartIndex((int)offset);
			// String subsql =
			// "select org_id from td_sm_organization where parent_id='0'";

			String sql = null;
			StringBuffer condition = new StringBuffer();  
			if (super.accessControl.isAdmin()) {
				sql = sqlUtilInsert.getSQL("OrgSearchLish_getDataList");
			} else { // 不是系统管理员需要权限过滤----sql语句性能太差
			// sql.append(" and org_id in (")
			// .append(" select t.org_id from td_sm_organization t ")
			// .append(" where t.org_id in ")
			// .append("(")
			// .append(" select uo.org_id from v_tb_res_org_user uo where uo.user_id='").append(super.accessControl.getUserID()).append("' ")
			// .append( " union ")
			// .append(
			// " select om.org_id from td_sm_organization om start with om.org_id in ")
			// .append(
			// " (select mm.org_id from td_sm_orgmanager mm where mm.user_id='").append(
			// super.accessControl.getUserID() ).append( "') ")
			// .append( " connect by prior om.org_id = om.parent_id ")
			// .append( " ) ")
			// .append( " start with t.parent_id ='0' ")
			// .append( "connect by prior t.org_id=t.parent_id ) ");
				String curUserId = super.accessControl.getUserID();
				String managerorg = "select o.* from td_sm_organization o, td_sm_orgmanager om " +
						"where o.org_id = om.org_id and om.user_id = " + curUserId;
				
				DBUtil db = new DBUtil();
				db.executeSelect(managerorg);
				StringBuffer sql_ = new StringBuffer();
				for(int i = 0; i < db.size(); i ++)
				{
					
					if(sql_ == null)
						sql_ .append("select * from td_sm_organization where org_tree_level like '" +  db.getString(i, "org_tree_level") + "|%'");
					else
						sql_ .append("  select * from td_sm_organization where org_tree_level like '" +  db.getString(i, "org_tree_level") + "|%'");
				}
				if(sql_ == null)
					sql_ .append(managerorg);
				else
					sql_ .append(" union ").append(managerorg);
				
				
				
				sql = "select * from (" + sql_ .toString() + ") o where 1=1 ${where_condition} order by o.org_tree_level ";
			}
			
//			// System.out.println(sql);
//			if (remark5.equals("") && orgnumber.equals("") && orgcreator.equals("") && isEffective.equals("2")) {
//				Map<String, String> variablevalues = new HashMap<String, String>();
//				variablevalues.put("where_condition", "and o.remark3='2'");
//				System.out.println(sql);
//				sql = sqlUtilInsert.evaluateSQL("test", sql, variablevalues);
//				System.out.println(sql);
////				sql = com.frameworkset.util.VariableHandler.substitution(sql,"");				
//				listInfo = this.getQueryResult(sql, offset, maxPagesize);
//				
//
//			} else 
			{
				
				if (!remark5.equals("")) {
					condition.append(" and o.remark5 like '%").append(remark5).append(
							"%'");
				}
				if (!orgnumber.equals("")) {
					condition.append(" and o.orgnumber like '%").append(orgnumber)
							.append("%'");
				}
				if (!orgcreator.equals("")) {
					condition.append(" and o.creator in(select user_id from td_sm_user where user_name like '%").append(orgcreator)
							.append("%')");
				}
//				if (!isEffective.equals("2") && !isEffective.equals("")) {
//				    condition.append("and o.remark3='").append(isEffective).append("'");
//                }
				if (!isEffective.equals("")) {
				    condition.append("and o.remark3='").append(isEffective.equals("2")?"0":isEffective).append("'");
                }
				Map<String, String> variablevalues1 = new HashMap<String, String>();
				variablevalues1.put("where_condition", condition.toString());
				sql = sqlUtilInsert.evaluateSQL("test", sql, variablevalues1);
//				sql = com.frameworkset.util.VariableHandler.substitution(sql,condition.toString());
				listInfo = this.getQueryResult(sql.toString(), offset, maxPagesize);
				

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.frameworkset.common.tag.pager.DataInfoImpl#getDataList(java.lang.
	 * String, boolean)
	 */
	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param sql
	 * @return LogSearchList.java
	 * @author: ge.tao
	 */
	protected ListInfo getQueryResult(String sql, long offset, int maxItem) {
		List datas = new ArrayList();
		ListInfo listInfo = new ListInfo();
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql, offset, maxItem);

			Organization org = null;
			for (int i = 0; i < db.size(); i++) {
				org = new Organization();
				org.setOrgId(db.getString(i, "org_id".toUpperCase()));
				org.setParentId(db.getString(i, "parent_id".toUpperCase()));
				org.setOrgName(db.getString(i, "org_name".toUpperCase()));
				org.setOrgnumber(db.getString(i, "orgnumber".toUpperCase()));
				org.setOrgSn(db.getString(i, "org_sn".toUpperCase()));
				org.setOrgdesc(db.getString(i, "orgdesc".toUpperCase()));
				org.setRemark5(db.getString(i, "remark5".toUpperCase()));
				org.setCreator(db.getString(i, "creator".toUpperCase()));

				datas.add(org);
			}
			listInfo.setDatas(datas);
			listInfo.setTotalSize(db.getTotalSize());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInfo;
	}
}