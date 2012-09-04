package com.frameworkset.platform.cms.templatemanager;

import java.util.List;

import com.frameworkset.util.ListInfo;


/**
 * 管理模板风格表相关操作
 * @author benq
 *
 */
public interface TemplateStyleManager extends java.io.Serializable {
	
	/**
	 * 通过styleId返回模板风格名称
	 * @param styleId 模板风格表ID
	 * @return
	 */
	public String getTemplateStyleName(String styleId);
	
	
	/**
	 * 查询所有的名字和编号
	 * @return
	 */
	public List getAllTemplateStyleIdAndName();
	
	/**
	 * 获取模板风格信息列表
	 * @param sql
	 * @param offset
	 * @param maxItem
	 * @return
	 */
	public ListInfo getTemplateStyleList(String sql, int offset, int maxItem);
	
	/**
	 * 新增一条模板风格信息
	 * @param tsi
	 */
	public boolean addTemplateStyle(TemplateStyleInfo tsi);
	
	/**
	 * 更新一条模板风格信息
	 * @param styleId
	 * @param tsi
	 * @return
	 */
	public boolean updateTemplateStyle(String styleId, TemplateStyleInfo tsi) ;
	
	
	/**
	 * 单个删除模板信息
	 * @param styleId
	 * @return
	 */
	public boolean delTemplateStyle(String styleId) ;
	
	/**
	 * 批量删除模板风格信息
	 * @param styleIds
	 * @return
	 */
	public boolean delTemplateStyles(String[] styleIds) ;
	
	/**
	 * 查找最大的排序号
	 * @return
	 */
	public int getMaxTemplateStyleOrder();
	
	/**
	 * 保存排序
	 * @param styleIds
	 * @param strOrderNums
	 */
	public boolean updateTemplateStyleOrder(String styleIds,String strOrderNums) ;
}
