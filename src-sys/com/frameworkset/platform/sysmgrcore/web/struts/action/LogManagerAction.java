package com.frameworkset.platform.sysmgrcore.web.struts.action;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.frameworkset.platform.sysmgrcore.entity.Log;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.db.LogManagerImpl;

public class LogManagerAction extends DispatchAction implements Serializable{
	private Logger log = Logger.getLogger(LogManagerAction.class);
	
	public LogManagerAction() {
	}
	
	public ActionForward deletelog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {//删除日志
		LogManager logManager = SecurityDatabase.getLogManager();
		String[] logid = request.getParameterValues("ID");
		//查询参数
		String operUser = request.getParameter("operUser");
		String oper = request.getParameter("oper");
		String type = request.getParameter("type");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		operUser = operUser==null?"":operUser;
		oper = oper==null?"":oper;
		type = type==null?"":type;  
		startDate = startDate==null?"":startDate;
		endDate = endDate==null?"":endDate;
		if (logid != null) {
			for (int i = 0; i < logid.length; i++) {
				LogManagerImpl logMgr = new LogManagerImpl();				
				Log log=new Log();
				Integer id = new Integer(0);
				if(logid[i].trim().length()>0){
					id = Integer.valueOf(logid[i]);
				
			}
			log.setLogId(id);
			logMgr.deleteLog(log);
				
			}
		}
		String forwardStr = "/sysmanager/logmanager/logList_tab.jsp?";
		forwardStr += "operUser=" + operUser;
		forwardStr += "&oper=" + oper;
		forwardStr += "&type=" + type;
		forwardStr += "&startDate=" + startDate;
		forwardStr += "&endDate=" + endDate;
		log.warn("forwardStr---------------------"+forwardStr); 
		ActionForward forward = new ActionForward(forwardStr);
		forward.setRedirect(true);
		//return mapping.findForward("logList");
		return forward;
	}
	
	public ActionForward deleteAlllog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {//删除日志	
		
			LogManagerImpl logMgr = new LogManagerImpl();
			logMgr.deleteAllLog();		
			//return mapping.findForward("logList");
			return mapping.findForward("logListtab");
	}
	
	
	public ActionForward permutelog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {//是否记录日志
		String status = request.getParameter("status");
		String id = request.getParameter("id");
		//System.out.println("//////////"+status);
		//System.out.println("//////////"+id);
		LogManager logManager = SecurityDatabase.getLogManager();
		logManager.updatelog(status,id);
		return mapping.findForward("logModuleList");

	}
	
	public ActionForward querylog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {//是否记录日志
		String status = request.getParameter("status");
		String id = request.getParameter("id");
		//System.out.println("//////////"+status);
		//System.out.println("//////////"+id);
		LogManager logManager = SecurityDatabase.getLogManager();
		logManager.updatelog(status,id);
		return mapping.findForward("logListtab");

	}
	
	

}
