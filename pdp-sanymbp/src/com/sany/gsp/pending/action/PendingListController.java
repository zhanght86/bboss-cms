package com.sany.gsp.pending.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.security.AccessControl;

/**
 * 
 * GSP待办列表及临时计划处理控制类
 * 
 * @author dingzy3
 * @since 2012-08-13
 */
public class PendingListController {

	private static Logger logger = Logger.getLogger(PendingListController.class);
	
	
	/**
	 * 显示待办列表
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public String showPendingList(ModelMap model) {
		AccessControl control = AccessControl.getAccessControl();
		String user = control.getUserAccount();
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		for(int i = 0; i < 20;i++)
		{
			Map<String,Object> record = new HashMap<String,Object>();
			/**
			 * for (int i = 0; vos != null && i < vos.length; i++) {
						vo = vos[i];
						String submitter = "";
						Calendar cReceiveDate = vo.getAcceptDate();// vo.getAcceptDate();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String receiveDate = "";
						if (cReceiveDate != null)
							receiveDate = sdf.format(cReceiveDate.getTime());

						int planCode = vo.getBoId();
						int noIndex = vo.getNoIndex();
						int moduleCode = vo.getModuleCode();
						String content = vo.getBoContent();
						String topic = vo.getModuleName();
				%>
				<li>
					<a data-ajax="false" id="viewPlanDetail" data-transition="pop" 	href="${ctx}/sanygsp/getPendingDigest.page?planCode=<%=planCode%>&noIndex=<%=noIndex%>&moduleCode=<%=moduleCode%>">
						<h3><%=submitter + "  " + receiveDate + "  <pg:message code='sany.gsp.tpp.plancode' />" + planCode%></h3>		
						<h3>
							<pg:message	code="sany.gsp.pending.process.theme" />：<%=topic%>
						</h3>
					</a>
				</li>
			 */
			record.put("planCode", "SY00000_"+i);
			record.put("noIndex", 10 );
			record.put("moduleCode", "moduleCode_"+i);
			record.put("submitter", "罗宏");
			record.put("receiveDate", "2014-03-11 12:00");
			record.put("topic", "采购特种钢10吨");
			
			data.add(record);
			
		}
		model.addAttribute("pendingList",data);
//		pendingServiceImpl.savePendingContent(request, null, false);

		return "path:main";
	}

	/**
	 * 根据计划单号得到计划单摘要
	 * 
	 * @param request
	 * @param response
	 * @param planCode
	 *            计划单号
	 * @param model
	 * @return
	 */
	public String getPendingDigest(String planCode, String noIndex, ModelMap model) {

//		TppPlanVO vo = new TppPlanVO();
//		try {
//			
//			vo = tppService.getClientCall().getTppPlanByCode(Integer.parseInt(planCode));
//		} catch (Exception e) {
//			request.setAttribute("ERROR", "接口服务调用失败:" + e.getMessage());
//			logger.error(e.getMessage(), e);
//		}
//		request.setAttribute("planCode", planCode);
//		request.setAttribute("noIndex", noIndex);
//		request.setAttribute("moduleCode", request.getParameter("moduleCode"));
//		request.setAttribute("tppPlanVO", vo);

		return "path:digest";
	}

	/**
	 * 审批记录页面
	 * 
	 * @param request
	 * @param response
	 * @param moduleCode
	 *            GSP待办 moduleCode，用来获取待办审批记录
	 * @param model
	 * @return
	 * 
	 */
	public String approveView(String boId, String moduleCode,
			String planCode, String noIndex, ModelMap model) {
		
		List<Map<String,Object>> doneVos = new ArrayList<Map<String,Object>>();
		for(int i = 0; i < 10; i ++)
		{
			Map<String,Object> vo = new HashMap<String,Object>();
			vo.put("userName", "张三"+i);
			vo.put("realDate", new Date());
			vo.put("boContent", "十吨特种钢材用于构建中国第一艘航母.");
			doneVos.add(vo);
		}
		model.addAttribute("planCode", planCode);
		model.addAttribute("noIndex", noIndex);
		model.addAttribute("moduleCode", moduleCode);
		model.addAttribute("doneVos", doneVos);
		
		List<Map<String,Object>> rejectList = new ArrayList<Map<String,Object>>();
		
		{
			Map<String,Object> vo = new HashMap<String,Object>();
			vo.put("nodeCode", "submit");
			vo.put("nodeName", "提交.");
			rejectList.add(vo);
			vo = new HashMap<String,Object>();
			vo.put("nodeCode", "firstaudit");
			vo.put("nodeName", "初审.");
			rejectList.add(vo);
			
			vo = new HashMap<String,Object>();
			vo.put("nodeCode", "fengxianaudit");
			vo.put("nodeName", "风险初评.");
			rejectList.add(vo);
		}
		model.addAttribute("rejectList", rejectList);
//		AlreadyDealVO[] doneVos = new AlreadyDealVO[0];
//		
//		if(null==planCode)
//			planCode = boId;
//
//		// 流程记录
//		try {
//			
//			long start = System.currentTimeMillis();
//			doneVos = tppService.getClientCall().getAlreadyDealVo(boId, moduleCode, planCode,
//					StringUtil.isEmpty(noIndex) ? "0" : noIndex);
//			
//			System.out.println("=========="+(System.currentTimeMillis() - start));
//		} catch (Exception e) {
//			request.setAttribute("ERROR", "接口服务调用失败:" + e.getMessage());
//			logger.error(e.getMessage(), e);
//		}
//		request.setAttribute("planCode", planCode);
//		request.setAttribute("noIndex", noIndex);
//		request.setAttribute("moduleCode", moduleCode);
//		request.setAttribute("doneVos", doneVos);
//
//		// 可驳回节点
//		TppNodeUserVO[] rejectList = new TppNodeUserVO[0];
//
//		try {
//			long start = System.currentTimeMillis();
//			rejectList = tppService.getClientCall().getActivtiesNodes(Integer.parseInt(planCode), Integer.parseInt(StringUtil.isEmpty(noIndex) ? "0" : noIndex));
//			System.out.println("==========reject"+(System.currentTimeMillis() - start));
//		} catch (Exception e) {
//			request.setAttribute("ERROR", "接口服务调用失败:" + e.getMessage());
//			logger.error(e.getMessage(), e);
//		}
//
//		request.setAttribute("rejectList", rejectList);

		return "path:approveView";
	}

	/**
	 * 
	 * 审批
	 * 
	 * @param request
	 * @param response
	 * @param userId
	 * @param planCode
	 *            计划单号
	 * @param planItemCode
	 *            计划单行号
	 * @param pass
	 *            "true"通过，其他为流程节点的名字则驳回
	 * @param opinion
	 *            审批意见
	 * @param model
	 * @return
	 */
	public String approve(HttpServletRequest request, HttpServletResponse response, String userId, String planCode,
			String planItemCode, String pass, String opinion, ModelMap model) {

//		String returnStr = null;
//		try {
//			
//			returnStr = tppService.getClientCall().approve(Utilities.getDESCipher(userId), Integer.parseInt(planCode),
//					Integer.parseInt(StringUtil.isEmpty(planItemCode) ? "0" : planItemCode), pass, opinion);
//			
//			if(!"true".equalsIgnoreCase(returnStr)){
//				request.setAttribute("ERRORMSG", returnStr);
//			}
//		} catch (Exception e) {
//			request.setAttribute("ERROR", "接口服务调用失败:" + e.getMessage());
//			logger.error(e.getMessage(), e);			
//		}

		return "path:approve";
	}

	/**
	 * 详情信息
	 * 
	 * @param request
	 * @param response
	 * @param planCode
	 * @param planItemCode
	 * @param moduleCode
	 * @param model
	 * @return
	 */
	public String detail(HttpServletRequest request, HttpServletResponse response, String planCode, String noIndex, String moduleCode, ModelMap model) {

//		TppPlanDetailVO[] detailVos = new TppPlanDetailVO[0];
//		try {
//			detailVos = tppService.getClientCall().getTppPlanDetailByCode(Integer.parseInt(planCode),Integer.parseInt(StringUtil.isEmpty(noIndex)?"-1":noIndex));
//		} catch (Exception e) {
//			request.setAttribute("ERROR", "接口服务调用失败:" + e.getMessage());
//			logger.error(e.getMessage(), e);
//		}
//
//		request.setAttribute("moduleCode", moduleCode);
//		request.setAttribute("noIndex", noIndex);
//		request.setAttribute("planCode", planCode);
//		request.setAttribute("detailVos", detailVos);

		return "path:detail";

	}

	@Deprecated
	public String approveFlow(HttpServletRequest request, HttpServletResponse response, String userId, String planCode,
			String planItemCode, String opinion, ModelMap model) {

//		TppNodeUserVO[] rejectList = new TppNodeUserVO[0];
//
//		try {
//			rejectList = tppService.getClientCall().getActivtiesNodes(Integer.parseInt(planCode),
//					Integer.parseInt(StringUtil.isEmpty(planItemCode) ? "0" : planItemCode));
//		} catch (Exception e) {
//			request.setAttribute("ERROR", "接口服务调用失败:" + e.getMessage());
//			logger.error(e.getMessage(), e);
//		}
//
//		request.setAttribute("userId", userId);
//		request.setAttribute("planCode", planCode);
//		request.setAttribute("planItemCode", planItemCode);
//		request.setAttribute("opinion", opinion);
//		request.setAttribute("rejectList", rejectList);

		return "path:approveFlow";
	}

	/**
	 * 待办信息
	 * 
	 * @param request
	 * @param isSession
	 *            true 则存入 session false 存入 request
	 */
	private void addPendingBean(HttpServletRequest request, boolean isSession) {

//		PendingBean pendingBean = new PendingBean();
//
//		try {
//			AccessControl control = AccessControl.getAccessControl();		
//			pendingBean = pendingService.getClientCall().getPendingContent(Utilities.getDESCipher(control.getPrincipal().getName()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (isSession)
//			request.getSession().setAttribute("pendingBean", pendingBean);
//		else
//			request.setAttribute("pendingBean", pendingBean);
	}
}
