package com.frameworkset.platform.sysmgrcore.orgmanager;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.frameworkset.platform.sysmgrcore.entity.Orgjob;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl;


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
public class OrgJobAction extends DispatchAction implements Serializable {
    public ActionForward addOrgJob(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
          OrgJobForm orgjobForm = (OrgJobForm) form;
        OrgManager orgManager = new OrgManagerImpl();
        Orgjob orgjob = new Orgjob();
        orgManager.storeOrgjob(orgjob);
       
        return (mapping.findForward("addOrgJob"));
    }

    public ActionForward deleteOrgJob(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        OrgJobForm orgjobForm = (OrgJobForm) form;
        OrgManager orgManager = new OrgManagerImpl();
        Orgjob orgjob = new Orgjob();
        orgManager.deleteOrgjob(orgjob);

        return (mapping.findForward("deleteOrgJob"));
    }
}
