package org.frameworkset.esb.datareuse.datasource.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.esb.AjaxResponseBean;
import org.frameworkset.esb.DropListEntity;
import org.frameworkset.esb.datareuse.datasource.entity.DatasourceBean;
import org.frameworkset.esb.datareuse.datasource.service.DatasourceService;
import org.frameworkset.esb.datareuse.datasource.util.DynamicDatasoureManagerUtil;
import org.frameworkset.esb.datareuse.util.Constants;
import org.frameworkset.esb.tools.StringTool;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.web.servlet.ModelAndView;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright:Copyright (c) 2011
 * </p>
 * <p>
 * Company:湖南科创
 * </p>
 * 
 * @author qian.wang
 * @version 1.0
 * @date 2011-4-1
 */

public class DatasourceController {
	private DatasourceService datasourceService;
	private String testSql;

	public void setDatasourceService(DatasourceService datasourceService) {
		this.datasourceService = datasourceService;
	}

	/**
	 * 将字符串转成utf-8编码
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String encode(String str) throws UnsupportedEncodingException {
		return java.net.URLEncoder.encode(str, "utf-8");
	}

	/**
	 * 转成json对象，直接用response的print方法
	 * 
	 * @param response
	 * @param ajaxResponseBean
	 * @throws IOException
	 */
	private void write(HttpServletResponse response,
			AjaxResponseBean ajaxResponseBean) throws IOException {
//		ObjectMapper objectMapper = new ObjectMapper();
		String responseText = StringUtil.object2json(ajaxResponseBean);
		response.getWriter().print(responseText);
	}

	@SuppressWarnings("unchecked")
	public ModelAndView main(
			DatasourceBean paramObj,
			@PagerParam(name = PagerParam.SORT) String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "true") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			ModelMap model) throws Exception {

		String dsName = paramObj.getDs_name();
		String jdbcUrl = paramObj.getJdbc_url();
		String status = paramObj.getStatus();
		if (!StringUtil.isEmpty(dsName)) {
			dsName = StringTool.buildFuzzySearchString(dsName, true);
			paramObj.setDs_name(dsName);
		}
		if (!StringUtil.isEmpty(jdbcUrl)) {
			jdbcUrl = StringTool.buildFuzzySearchString(jdbcUrl, true);
			;
			paramObj.setJdbc_url(jdbcUrl);
		}
		if (status != null && !status.equals(""))
			model.addAttribute("status", status);
		ListInfo datas = null;
		try {
			datas = datasourceService.getDatasourceListInfo(sortKey, desc,
					offset, pagesize, paramObj);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

		ModelAndView view = new ModelAndView("/datareuse/datasource/main.jsp",
				"pagedata", datas);

		return view;
	}

	public ModelAndView add(ModelMap paramMap) {
		ModelAndView view = new ModelAndView("/datareuse/datasource/add.jsp");
		loadDropList(view);
		loadDsDropList(view);
		return view;
	}

	public String create(DatasourceBean datasourceBean,
			@RequestParam(name = "id") String id, ModelMap paramMap,
			HttpServletRequest request, HttpServletResponse response) {
		Timestamp times = new Timestamp(new Date().getTime());
		datasourceBean.setCreate_time(times);
		datasourceBean.setModify_time(times);
		AccessControl accesscontroler = AccessControl.getInstance();
		accesscontroler.checkAccess(request, response);
		String userName = accesscontroler.getUserAccount();
		datasourceBean.setModifier(userName);
		datasourceBean.setCreator(userName);
		try {
			datasourceService.insertDatasource(datasourceBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/datareuse/datasource/edit.jsp?close=true";
	}

	/**
	 * 停止加载的数据源
	 * 
	 * @param ds_name
	 * @param request
	 * @param response
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public ModelAndView stopDynamicDatasource(
			@RequestParam(name = "ds_name") String ds_name,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap paramMap) throws Exception {
		AccessControl accesscontroler = AccessControl.getInstance();
		accesscontroler.checkAccess(request, response);

		DynamicDatasoureManagerUtil.stopDynamicDatasource(ds_name);

		return edit(ds_name, paramMap);
	}

	public ModelAndView edit(@RequestParam(name = "ds_name") String id,
			ModelMap paramMap) throws Exception {
		DatasourceBean obj = datasourceService.findDatasourceById(id);

		try {
			String temp = DBUtil.getStatus(obj.getDs_name());
			obj.setWorkStatus(temp);
			if (("start").equals(temp)) {
				obj.setMaxnumactive(DBUtil.getMaxNumActive(obj.getDs_name()));
				obj.setNumactive(DBUtil.getNumActive(obj.getDs_name()));
				obj.setNumidle(DBUtil.getNumIdle(obj.getDs_name()));
			}
			
		} catch (Exception e) {
			throw e;
		}

		ModelAndView view = new ModelAndView("/datareuse/datasource/edit.jsp",
				"obj", obj);
		view.addObject("close", paramMap.get("close"));
		return view;
	}

	// 修改数据源的状态
	public void updatadatasourcestatus(DatasourceBean paramObj,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkAccess(request, response);
			String userId = accesscontroler.getUserAccount();
			Timestamp times = new Timestamp(new Date().getTime());
			paramObj.setModify_time(times);
			paramObj.setModifier(userId);
			datasourceService.updateDatasourceStatus(paramObj);
			response.getWriter().print("ok");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				response.getWriter().print(e);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public String update(DatasourceBean datasourceBean,
			@RequestParam(name = "id") String id, ModelMap paramMap,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AccessControl accesscontroler = AccessControl.getInstance();
		accesscontroler.checkAccess(request, response);
		String userId = accesscontroler.getUserAccount();
		Timestamp times = new Timestamp(new Date().getTime());
		datasourceBean.setModify_time(times);
		datasourceBean.setModifier(userId);
		datasourceService.updateDatasource(datasourceBean);

		return "/datareuse/datasource/edit.jsp?close=true";
	}

	public void test(DatasourceBean data, HttpServletResponse res)
			throws Exception {
		AjaxResponseBean ajaxResponseBean = new AjaxResponseBean();
		try {
			boolean result = DynamicDatasoureManagerUtil
					.validationJdbcConfig(data);

			ajaxResponseBean.setStatus("success");
			if (result) {
				ajaxResponseBean.setData("1");
			} else {
				ajaxResponseBean.setData("0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxResponseBean.setStatus("error");

			throw e;
		}
		write(res, ajaxResponseBean);
	}

	public void deleteDatasources(
			@RequestParam(name = "ds_name") String ds_name,
			HttpServletResponse res) {
		try {
			datasourceService.deleteDatasource(ds_name);

			res.getWriter().print("OK");
		} catch (Exception e) {
			try {
				res.getWriter().print(e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public void checkDs_name(@RequestParam(name = "ds_name") String ds_name,
			HttpServletResponse response) {
		DatasourceBean datasource = null;
		ds_name = ds_name.trim();
		try {
			datasource = datasourceService.findDatasourceById(ds_name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (datasource == null) {
			try {
				response.getWriter().print("ok");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				response.getWriter().print("fail");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void loadDropList(ModelAndView view) {
		DropListEntity dropModeTable = new DropListEntity();
		dropModeTable.setText(Constants.OBJECT_TYPE_TABLE);
		dropModeTable.setValue(Constants.OBJECT_TYPE_TABLE);
		DropListEntity dropModeView = new DropListEntity();
		dropModeView.setValue(Constants.OBJECT_TYPE_VIEW);
		dropModeView.setText(Constants.OBJECT_TYPE_VIEW);
		List<DropListEntity> objectTypeList = new ArrayList<DropListEntity>();
		objectTypeList.add(dropModeTable);
		objectTypeList.add(dropModeView);
		view.addObject("objectTypeList", objectTypeList);
	}

	private void loadDsDropList(ModelAndView view) {
		List<DatasourceBean> dsList;
		try {
			dsList = (List<DatasourceBean>) datasourceService
					.getDatasourceDropList();
			view.addObject("dsList", dsList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
