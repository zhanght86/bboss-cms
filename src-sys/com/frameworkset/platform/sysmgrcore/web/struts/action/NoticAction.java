package com.frameworkset.platform.sysmgrcore.web.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class NoticAction extends BasicAction {
	
	public ActionForward addNotic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
//		SchedularManagerImpl smi = new SchedularManagerImpl();
//		NoticForm noticForm = (NoticForm) form;
//		Notic notic = new Notic();
//		
//		notic.setBeginTime(StringUtil.stringToDate(noticForm.getBeginTime()));
//		notic.setEndTime(StringUtil.stringToDate(noticForm.getEndTime()));
//		notic.setTopic(noticForm.getTopic());
//		notic.setPlace(noticForm.getPlace());
//		notic.setContent(noticForm.getContent());
//		notic.setExecutorID(noticForm.getExecutorID());
//		notic.setNoticPlannerID(noticForm.getNoticPlannerID());
//		notic.setSource(noticForm.getSource());
//		notic.setStatus(0);
//		
//		smi.addNotic(notic);
		return mapping.findForward("noticlist");

	}
	
	
	
}
