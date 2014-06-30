package com.sany.workflow.action;

import java.util.List;

import org.frameworkset.util.ParamsHandler;
import org.frameworkset.util.ParamsHandler.Param;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.cms.util.StringUtil;
import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.Template;
import com.sany.workflow.entity.TempleCondition;
import com.sany.workflow.service.TempleService;

/**
 * @todo 模板管理模块
 * @author tanx
 * @date 2014年6月9日
 * 
 */
public class TempleManageAction {

	private TempleService templeService;

	/**
	 * 跳转至模板管理模块
	 * 
	 * @param model
	 * @return 2014年6月9日
	 */
	public String templeManager(ModelMap model) {

		return "path:templeManager";
	}

	/**
	 * 跳转至模板编辑页面
	 * 
	 * @param model
	 * @return 2014年6月9日
	 */
	public String editTemple(String templeId, String state, ModelMap model) throws Exception{

		// 修改
		if (StringUtil.isNotEmpty(templeId)) {

			Template templeBean = templeService.queryTemple(templeId);
			model.addAttribute("templeBean", templeBean);

		}

		// 获取模板字段列表
		ParamsHandler paramHandler = ParamsHandler
				.getParamsHandler("sys.msg.paramshandler");
		List<Param> paramsList = paramHandler.getParams("msg", "msg")
				.getParams();

		model.addAttribute("paramsList", paramsList);
		// state = 0 (新增、修改) = 1 (明细查看)
		model.addAttribute("state", state);

		return "path:editTemple";
	}

	/**
	 * 加载模板数据
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param temple
	 * @param model
	 * @return 2014年6月9日
	 */
	public String queryTempleData(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			TempleCondition temple, ModelMap model) throws Exception {

		ListInfo listInfo = templeService
				.queryTemples(temple, offset, pagesize);

		model.addAttribute("listInfo", listInfo);

		return "path:templeList";

	}

	/**
	 * 保存模板
	 * 
	 * @param temple
	 * @return 2014年6月10日
	 */
	public @ResponseBody
	String saveTemple(TempleCondition temple) {

		try {

			templeService.saveTemple(temple);

			return "success";
		} catch (Exception e) {
			return "fail" + e.getMessage();
		}

	}

	/**
	 * 获取模板列表
	 * 
	 * @param templeType
	 * @return 2014年6月5日
	 */
	public @ResponseBody(datatype = "json")
	List<Template> getTempleList(String templeType) throws Exception {

		List<Template> templeList = templeService.queryTempleList(templeType);

		return templeList;

	}

	/**
	 * 删除模板
	 * 
	 * @param Temples
	 * @return 2014年6月12日
	 */
	public @ResponseBody
	String delTemples(String templeIds) {
		try {

			return templeService.delTemplates(templeIds);

		} catch (Exception e) {
			return "fail" + e.getMessage();
		}

	}

}