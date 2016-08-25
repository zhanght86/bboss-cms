/*
 * Created on 2006-3-12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;
/**
 * @author ok
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OrgSearchList extends DataInfoImpl implements Serializable {
 
	
//	protected ListInfo getDataList_old(String sortKey, boolean desc, long offset,
//            int maxPagesize) {
//    	
//        ListInfo listInfo = new ListInfo();
//
//        String orgName = StringUtil.replaceNull(request.getParameter("orgName"));
//        String orgnumber = StringUtil.replaceNull(request.getParameter("orgnumber"));
//        String remark5 = StringUtil.replaceNull(request.getParameter("remark5"));
//        String orgId = StringUtil.replaceNull(request.getParameter("orgId"));
//        
//        boolean first = true;
//
//        String userId = super.accessControl.getUserID();
//        String userAccount = super.accessControl.getUserAccount();
//     
//        try{
//        	OrgManager orgManager = SecurityDatabase.getOrgManager();
//            List list = null;			
// 			PageConfig pageConfig = orgManager.getPageConfig();		
// 			pageConfig.setPageSize(maxPagesize);
// 			pageConfig.setStartIndex((int)offset);
// 			
// 			if (remark5.equals("") && orgnumber.equals("")) {		
//	 			list = orgManager.getOrgList("from Organization o order by o.orgSn,o.orgnumber asc");
//	 			listInfo.setTotalSize(pageConfig.getTotalSize());
//	 			listInfo.setDatas(list);
//	 			}else{
//	 				String sql = "from Organization o where ";
//	                if (!remark5.equals("")) {
//	                    if (first) {
//	                        sql += " o.remark5 like '%" + remark5 + "%'";
//	                        first = false;
//	                    } else {
//	                        sql += " and o.remark5 like '%" + remark5 + "%'";
//	                    }
//	                }
//	                if (!orgnumber.equals("")) {
//	                    if (first) {
//	                        sql += " o.orgnumber like '%" + orgnumber + "%'";
//	                        first = false;
//	                    } else {
//	                        sql += " and o.orgnumber like '%" + orgnumber + "%'";
//	                    }
//	                }
//	              
//	                list = orgManager.getOrgList(sql.toString() + "  order by o.orgSn,o.orgnumber asc");
//	                listInfo.setTotalSize(pageConfig.getTotalSize());
//	                listInfo.setDatas(list);
//	 			}
//            }catch(Exception e){
//                  e.printStackTrace();
//            }
// 			return listInfo;	
//      }   
	
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
            int maxPagesize) {
    	
        ListInfo listInfo = new ListInfo();

        String orgName = StringUtil.replaceNull(request.getParameter("orgName"));
        String orgnumber = StringUtil.replaceNull(request.getParameter("orgnumber"));
        //机构别名显示名称
        String remark5 = StringUtil.replaceNull(request.getParameter("remark5"));
        
        String userId = super.accessControl.getUserID();
        String userAccount = super.accessControl.getUserAccount();
     
//        try{
//        	OrgManager orgManager = SecurityDatabase.getOrgManager();
//            List list = null;			
// 			PageConfig pageConfig = orgManager.getPageConfig();		
// 			pageConfig.setPageSize(maxPagesize);
// 			pageConfig.setStartIndex((int)offset);
// 			String subsql = "select org_id from td_sm_organization where parent_id='0'";
// 			
// 			String sql = "select * from(";
//			sql += "select o.org_id,o.parent_id,o.org_name,o.orgnumber,o.org_sn,o.orgdesc,o.remark5,level as num from TD_SM_ORGANIZATION o ";
//			sql += "start with o.ORG_ID in ("+subsql+")  connect by prior o.ORG_ID=o.PARENT_ID order SIBLINGS by o.org_sn asc)";
//            sql += "abc where 1=1 ";
//            if(super.accessControl.isAdmin())
//            {}
//            else
//            {	//不是系统管理员需要权限过滤
//            	sql += " and org_id in (" +
//      		  	" select t.org_id from td_sm_organization t "     
//			    + " where t.org_id in " 
//			    + "("
//			    + " select uo.org_id from v_tb_res_org_user uo where uo.user_id='" +super.accessControl.getUserID()+ "' " 
//			    + " union " 
//			    + " select om.org_id from td_sm_organization om start with om.org_id in "
//			    + " (select mm.org_id from td_sm_orgmanager mm where mm.user_id='" + super.accessControl.getUserID() + "') " 
//			    + " connect by prior om.org_id = om.parent_id "
//			    + " ) " 
//			    + " start with t.parent_id ='0' "  
//			    + "connect by prior t.org_id=t.parent_id ) ";
//            }      
// 			if (remark5.equals("") && orgnumber.equals("")) {
//                DBUtil count = new DBUtil();
//                count.executeSelect(sql);
//                //sql += " order by  abc.orgnumber asc,abc.org_sn asc ";
//            	list = this.getQueryResult(sql,offset,maxPagesize);
//                listInfo.setTotalSize(count.size());                                
//                listInfo.setDatas(list);
//                
//	 			}else{
//	 				
//	                if (!remark5.equals("")) {
//	                	sql += " and abc.remark5 like '%" + remark5 + "%'";
//	                }
//	                if (!orgnumber.equals("")) {
//	                	sql += " and abc.orgnumber like '%" + orgnumber + "%'";
//	                }
//	                
//	                list = this.getQueryResult(sql.toString(),offset,maxPagesize);
//	                
//	                DBUtil count = new DBUtil();
//	                count.executeSelect(sql);
//	                listInfo.setTotalSize(count.size());
//	                listInfo.setDatas(list);
//	                
//	 			}
//            }catch(Exception e){
//                  e.printStackTrace();
//            }
        	//List list = null;
        	StringBuffer sql = new StringBuffer()
        		.append("select * from (")
        		.append("SELECT om.org_id, om.org_name, om.orgnumber, om.org_sn, om.orgdesc,")
        		.append("om.remark5, LEVEL AS num,parent_id FROM TD_SM_ORGANIZATION om ")
        		.append("START WITH ");
        	if(!super.accessControl.isAdmin()){
        		sql.append(" org_id IN (SELECT mm.org_id FROM TD_SM_ORGMANAGER mm WHERE mm.user_id = '")
        		.append(super.accessControl.getUserID()).append("') ");
        	}else {
        		sql.append(" org_id = '1' ");
        	}
        	sql.append(" CONNECT BY PRIOR om.org_id = om.parent_id   ORDER SIBLINGS BY om.org_sn ASC) abc where 1=1 ");
        	 if (!remark5.equals("")) {
             	sql.append( " and abc.remark5 like '%" + remark5 + "%'");
             }
             if (!orgnumber.equals("")) {
             	sql.append(" and abc.orgnumber like '%" + orgnumber + "%'");
             }
             
            
             return this.getQueryResult(sql.toString(),offset,maxPagesize);
        			
      }   
    /*
     * (non-Javadoc)
     * 
     * @see com.frameworkset.common.tag.pager.DataInfoImpl#getDataList(java.lang.String,
     *      boolean)
     */
    protected ListInfo getDataList(String arg0, boolean arg1) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * 
     * @param sql
     * @return 
     * LogSearchList.java
     * @author: ge.tao
     */
    protected ListInfo getQueryResult(String sql,long offset,int maxItem){
    	ListInfo listInfo = new ListInfo();
    	List datas = new ArrayList();
    	DBUtil db = new DBUtil();	    	
    	try{
    		//System.out.println("sql = " +sql);
    		db.executeSelect(sql,offset,maxItem);
               		
    		Organization org = null;
    		for(int i=0; i<db.size(); i++){    
    			org = new Organization();
    			org.setOrgId(db.getString(i,"org_id".toUpperCase()));
    			org.setParentId(db.getString(i,"parent_id".toUpperCase()));
    			org.setOrgName(db.getString(i,"org_name".toUpperCase()));
    			org.setOrgnumber(db.getString(i,"orgnumber".toUpperCase()));
    			org.setOrgSn(db.getString(i,"org_sn".toUpperCase()));
    			org.setOrgdesc(db.getString(i,"orgdesc".toUpperCase()));
    			org.setRemark5(db.getString(i,"remark5".toUpperCase()));
    			
    			datas.add(org);
			}
    		listInfo.setTotalSize(db.getTotalSize());
            listInfo.setDatas(datas);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return listInfo;
    }
}