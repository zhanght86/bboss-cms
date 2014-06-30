package com.sany.workflow.service;

import java.util.List;
import java.util.Map;

import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.Template;
import com.sany.workflow.entity.TempleCondition;

/**
 * 模板管理接口
 * 
 * @todo
 * @author tanx
 * @date 2014年6月9日
 * 
 */
public interface TempleService {

	/**
	 * 分页查询模板数据
	 * 
	 * @param temple
	 * @param offset
	 * @param pagesize
	 * @return 2014年6月9日
	 */
	public ListInfo queryTemples(TempleCondition template, long offset,
			int pagesize) throws Exception;

	/**
	 * 根据模板id获取模板信息
	 * 
	 * @param templeId
	 * @return 2014年6月10日
	 */
	public Template queryTemple(String templateId) throws Exception;

	/**
	 * 保存模板
	 * 
	 * @param temple
	 * @return 2014年6月10日
	 */
	public void saveTemple(TempleCondition template) throws Exception;

	/**
	 * 获取模板列表数据
	 * 
	 * @return 2014年6月10日
	 */
	public List<Template> queryTempleList(String templateType) throws Exception;

	/**
	 * 替换模板字段
	 * 
	 * @param map
	 * @return 2014年6月11日
	 */
	public void sendNotice(List<Map<String, String>> fieldList);

	/**
	 * 删除模板
	 * 
	 * @param Temples
	 *            2014年6月12日
	 */
	public String delTemplates(String templateIds) throws Exception;

}
