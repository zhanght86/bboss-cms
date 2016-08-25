/*
 * Created on 2006-6-16
 * 
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.web.tag;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.FunctionDB;
import com.frameworkset.util.ListInfo;

/**
 * 用户管理 机构下的用户排序
 * @author ok edit by caix3
 * @version 2006-06-16, v1.0
 *          2012-04-11, v2.0
 */
public class Org_UserListSn extends DataInfoImpl {

   

    private Logger log = Logger.getLogger(UserListSn.class);

    protected ListInfo getDataListOld(String sortKey, boolean desc, long offset, int maxPagesize) {

        String userName = request.getParameter("userName");
        String userRealname = request.getParameter("userRealname");
        String orgId = request.getParameter("orgId");
        // 是否税管员
        String taxmanager = request.getParameter("taxmanager");
        // 是否递归
        String recursion = request.getParameter("intervalType");
        // 当前状态
        String userIsvalid = request.getParameter("userIsvalid");
        // 用户性别
        String userSex = request.getParameter("userSex");
        // 管理员查询
        String isOrgManager = request.getParameter("isOrgManager");

        // 增加对用户类型(liangbing.tao 2008-5-27)
        String userType = request.getParameter("userType");

        ListInfo listInfo = new ListInfo();

        try {

            UserManager userManager = SecurityDatabase.getUserManager();
            StringBuffer sb_user = new StringBuffer();
            sb_user.append("select a.*,'").append(orgId).append("' as org_id,(SELECT  MIN(same_job_user_sn) y ")
                    .append("FROM td_sm_userjoborg WHERE org_id = '").append(orgId)
                    .append("' and user_id=a.user_id) y from ");
            if (isOrgManager != null && !"".equals(isOrgManager)) {
                sb_user.append(" td_sm_user a where a.user_id in(select user_id from td_sm_orgmanager where org_id='")
                        .append(orgId).append("') ");
            } else {
                sb_user.append(" td_sm_user a ")
                        .append(" where a.user_id in (select distinct user_id from td_sm_userjoborg where org_id='")
                        .append(orgId).append("') ");
            }
            
            if (userName != null && !"".equals(userName)) {
                sb_user.append("and a.user_name like '%" + userName + "%' ");
            }
            if (userRealname != null && !"".equals(userRealname)) {
                sb_user.append("and a.user_realname like '%" + userRealname + "%' ");
            }
            if (userSex != null && !"".equals(userSex) && !"NaN".equals(userSex)) {
                sb_user.append("and a.user_sex ='" + userSex + "' ");
            }

            // 增加对用户类型的查询
            if (userType != null && !"".equals(userType) && !"NaN".equals(userType)) {
                sb_user.append("and a.user_type ='").append(userType).append("' ");
            }

            if (userIsvalid != null && !"".equals(userIsvalid) && !"NaN".equals(userIsvalid)) {
                sb_user.append("and a.user_Isvalid ='" + userIsvalid + "' ");
            }
            
            if (taxmanager != null && !"".equals(taxmanager)) {
                if ("1".equals(taxmanager)) {
                    sb_user.append("and a.istaxmanager='1' ");
                }
                if ("0".equals(taxmanager)) {
                    sb_user.append("and (a.istaxmanager is null or a.istaxmanager<>'1') ");
                }
                if ("3".equals(taxmanager)) {
                }
            }
            // }
            sb_user.append("order by y,a.user_id");

            log.warn("用户管理 机构下的用户排序 不递归---------------------------" + sb_user.toString());

            /* 递归查询 */
            if (recursion != null && recursion != "" && recursion.equals("1")) {
                StringBuffer rec_user = new StringBuffer();
                StringBuffer sub_user = new StringBuffer();
                if (userName != null && !userName.equals("")) {
                    sub_user.append("and t.user_name like '%" + userName + "%' ");
                }
                if (userRealname != null && !"".equals(userRealname)) {
                    sub_user.append("and t.user_realname like '%" + userRealname + "%' ");
                }
                if (userSex != null && !"".equals(userSex) && !"NaN".equals(userSex)) {
                    sub_user.append("and t.user_sex ='" + userSex + "' ");
                }
                if (userIsvalid != null && !"".equals(userIsvalid) && !"NaN".equals(userIsvalid)) {
                    sub_user.append("and t.user_Isvalid ='" + userIsvalid + "' ");
                }
                if (taxmanager != null && !"".equals(taxmanager)) {
                    if ("1".equals(taxmanager)) {
                        sub_user.append("and t.istaxmanager='1' ");
                    }
                    if ("0".equals(taxmanager)) {
                        sub_user.append("and (t.istaxmanager is null or t.istaxmanager<>'1') ");
                    }
                    if ("3".equals(taxmanager)) {
                    }
                }
                String temp = getRecOrderUserSql(orgId, sub_user.toString(), isOrgManager);
                rec_user.append(temp);
                log.warn("用户管理 机构下的用户排序 递归---------------------------" + rec_user.toString());
                // System.out.println("rec_user.toString() = " + rec_user.toString());
                listInfo = userManager.getUserList(rec_user.toString(), (int) offset, maxPagesize);
                return listInfo;
            }
            // System.out.println("sb_user.toString() = " + sb_user.toString());
            listInfo = userManager.getUserList(sb_user.toString(), (int) offset, maxPagesize);
            return listInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listInfo;
    }

    protected ListInfo getDataList(String arg0, boolean arg1) {
        return null;
    }

    private String getRecOrderUserSql(String orgId, String wherestr, String isOrgManager) {
//        int oid = 0;
//        try {
//            oid = Integer.parseInt(orgId);
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }

        String concat_ = DBUtil.getDBAdapter().concat(" org_tree_level", "'|%' ");
        StringBuffer sql = new StringBuffer().append("select t.user_id ");
        StringBuffer sql_temp = new StringBuffer()
                .append("from (select org_sn, a.org_id  from td_sm_organization a where a.ORG_ID in")
                .append(" (select w.org_id from TD_SM_ORGANIZATION w where w.org_tree_level like (select ")
                .append(concat_)
                .append("from TD_SM_ORGANIZATION c where c.org_id='")
                .append(orgId)
                .append("')")
                .append(" or w.org_id ='")
                .append(orgId)
                .append("')) a,")
                .append("(select min(tmp.same_job_user_sn) as same_job_user_sn,tmp.org_id,tmp.user_id from ((select ujo.*  from td_sm_userjoborg ujo where ujo.user_id in")
                .append(" (select ou.user_id from TD_SM_ORGUSER ou))) tmp  group by tmp.user_id, tmp.org_id) bb, td_sm_user t")
                .append(" where a.org_id = bb.org_id and bb.user_id = t.user_id ").append(wherestr)
                .append(" order by  bb.same_job_user_sn, t.user_id ");

        sql.append(sql_temp.toString());

        StringBuffer sql_ = new StringBuffer().append("select bb.same_job_user_sn,");

        PreparedDBUtil pe = new PreparedDBUtil();
        String user_id = null;
        String org_job = null;
        try {
            pe.preparedSelect(sql.toString());
            pe.executePrepared();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            user_id = pe.getString(0, "user_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        org_job = FunctionDB.getUserorgjobinfos(Integer.parseInt(user_id));
        sql_.append("'").append(org_job).append("'").append(" as org_job,t.*, a.org_id,a.org_sn ")
                .append(sql_temp.toString());

        log.warn(sql_.toString());
        return sql_.toString();
    }

    protected ListInfo getDataList(String sortKey, boolean desc, long offset, int maxPagesize) {

        String userName = request.getParameter("userName");
        String userRealname = request.getParameter("userRealname");
        String orgId = request.getParameter("orgId");
        // 是否税管员
        String taxmanager = request.getParameter("taxmanager");
        // 是否递归
        String recursion = request.getParameter("intervalType");
        // 当前状态
        String userIsvalid = request.getParameter("userIsvalid");
        // 用户性别
        String userSex = request.getParameter("userSex");

        // 增加对用户类型(liangbing.tao 2008-5-27)
        String userType = request.getParameter("userType");

        ListInfo listInfo = new ListInfo();

        try {

            UserManager userManager = SecurityDatabase.getUserManager();
            StringBuffer sb_user = new StringBuffer();
            
            sb_user.append("select distinct a.*,b.org_tree_level,b.org_sn,b.same_job_user_sn from td_sm_user a,(")
                .append("select ujo.user_id,ujo.same_job_user_sn,org.org_tree_level,org.org_id,")
                .append("org.org_sn from TD_SM_ORGANIZATION org,td_sm_userjoborg ujo where ");
            
            if (recursion != null && recursion != "" && recursion.equals("1")) {
                sb_user.append("org.org_id in (select w.org_id from TD_SM_ORGANIZATION w ")
                    .append("where w.org_tree_level like (select concat(org_tree_level, '|%') ")
                    .append("from TD_SM_ORGANIZATION c where c.org_id = '")
                    .append(orgId).append("') or w.org_id = '").append(orgId)
                    .append("') and ujo.org_id = org.org_id) b ");
            } else {
                sb_user.append("ujo.org_id = '").append(orgId)
                    .append("' and org.org_id='").append(orgId)
                    .append("') b ");
            }
            
            sb_user.append("where a.user_id = b.user_id ");
            
            if (userName != null && !"".equals(userName)) {
                sb_user.append("and a.user_name like '%" + userName + "%' ");
            }
            if (userRealname != null && !"".equals(userRealname)) {
                sb_user.append("and a.user_realname like '%" + userRealname + "%' ");
            }
            if (userSex != null && !"".equals(userSex) && !"NaN".equals(userSex)) {
                sb_user.append("and a.user_sex ='" + userSex + "' ");
            }

            if (userType != null && !"".equals(userType) && !"NaN".equals(userType)) {
                sb_user.append("and a.user_type ='").append(userType).append("' ");
            }

            if (userIsvalid != null && !"".equals(userIsvalid) && !"NaN".equals(userIsvalid)) {
                sb_user.append("and a.user_Isvalid ='" + userIsvalid + "' ");
            }
            
            if (taxmanager != null && !"".equals(taxmanager)) {
                if ("1".equals(taxmanager)) {
                    sb_user.append("and a.istaxmanager='1' ");
                }
                if ("0".equals(taxmanager)) {
                    sb_user.append("and (a.istaxmanager is null or a.istaxmanager<>'1') ");
                }
                if ("3".equals(taxmanager)) {
                }
            }
            
            sb_user.append("order by b.org_tree_level,b.org_sn,b.same_job_user_sn");
            
            listInfo = userManager.getUserList(sb_user.toString(), (int) offset, maxPagesize);
            
            return listInfo;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listInfo;
    }
    
}
