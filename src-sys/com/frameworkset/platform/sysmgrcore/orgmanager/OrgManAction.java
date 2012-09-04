package com.frameworkset.platform.sysmgrcore.orgmanager;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.proxy.Interceptor;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class OrgManAction extends DispatchAction implements Serializable{
    public OrgManAction() {
    }

//    public ActionForward newsubOrg(ActionMapping mapping, ActionForm form,
//        HttpServletRequest request, HttpServletResponse response)
//        throws Exception {
//        Organization org = (Organization) form;
//        String parentId = org.getOrgId();
//        org = new Organization();
//        org.setParentId(parentId);
//        request.removeAttribute(mapping.getInput());
//        request.setAttribute("Organization", org);
//
//        return (mapping.findForward("newsubOrg"));
//    }
//
//    public ActionForward savesubOrg(ActionMapping mapping, ActionForm form,
//        HttpServletRequest request, HttpServletResponse response)
//        throws Exception {
//        Organization org = (Organization) form;
//        Interceptor s;
//        OrgManager orgManager = SecurityDatabase.getOrgManager();
//        org.setOrgId(null);
//        orgManager.insertOrg(org);
//        request.setAttribute("Organization", org);
//
//        return (mapping.findForward("successaddorg"));
//    }
//
//    public ActionForward updateOrg(ActionMapping mapping, ActionForm form,
//        HttpServletRequest request, HttpServletResponse response)
//        throws Exception {
//        Organization org = (Organization) form;
//        OrgManager orgManager = SecurityDatabase.getOrgManager();
//        orgManager.storeOrg(org);
//        request.setAttribute("Organization", org);
//
//        return (mapping.findForward("updateOrg"));
//    }
//
//    public ActionForward deleteOrg(ActionMapping mapping, ActionForm form,
//        HttpServletRequest request, HttpServletResponse response)
//        throws Exception {
//        Organization org = (Organization) form;
//
//        OrgManager orgManager = SecurityDatabase.getOrgManager();
//        orgManager.deleteOrg(org);
//
//        return (mapping.findForward("successaddorg"));
//    }
//
//    public ActionForward getOrgInfo(ActionMapping mapping, ActionForm form,
//        HttpServletRequest request, HttpServletResponse response)
//        throws Exception {
//        Organization org = (Organization) form;
//        String selected = request.getParameter("selected");
//        OrgManager orgManager = SecurityDatabase.getOrgManager();
//        org = orgManager.getOrgById(selected);
//
//        request.setAttribute("Organization", org);
//
//        return (mapping.findForward("orginfo"));
//    }
    
    /**
     * 获得机构下的用户排序号的最大值
     * @param orgId
     * @return 
     * OrgManAction.java
     * @author: ge.tao
     */
    public static int getMaxOrgUserSn(String orgId){
    	int max = 0;    	
        StringBuffer sql = new StringBuffer()
            .append("select max(user_sn) num from td_sm_user a inner join ( ")
            .append(" select user_id,min(job_sn) x,min(same_job_user_sn) y,")
            .append("min(job_starttime) job_starttime,min(job_fettle) job_fettle from td_sm_userjoborg ")
		    .append(" where org_id = '" + orgId +"' ")
		    .append(" group by user_id  ")
		    .append(" ) b on a.user_id=b.user_id ");
        DBUtil dbUtil = new DBUtil();
        try{
        	dbUtil.executeSelect(sql.toString());
        	if(dbUtil.size()>0){
        		max = dbUtil.getInt(0, "num");
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }
    	return ++max;
    }
}
