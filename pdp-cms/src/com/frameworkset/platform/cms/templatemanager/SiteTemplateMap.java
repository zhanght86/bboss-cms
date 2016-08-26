package com.frameworkset.platform.cms.templatemanager;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;

/**
 * 站点和模版关系
 */
public class SiteTemplateMap implements java.io.Serializable {
	/**
	 * 功能:创建站点模板映射信息
	 * 输入:模板ID和站点ID
	 * 输出:true:成功;false:失败 
	 */
	public boolean createSiteTempateMap(int templateid, int siteid)
			throws TemplateManagerException {
		PreparedDBUtil preDBUtil = new PreparedDBUtil();
		boolean ret = false;
		try {
			String sql = "insert into td_cms_site_tpl(" + "template_id,site_id)"
					+ "values(?,?)";
			preDBUtil.preparedInsert(sql);
			preDBUtil.setInt(1, templateid);
			preDBUtil.setInt(2, siteid);
			preDBUtil.executePrepared();
			ret = true;

		} catch (Exception e) {
			ret = false; //建模板出现异常
			System.out.print("新建模板站点映射出错!" + e);
			throw new TemplateManagerException(e.getMessage());
		}
		return ret;
	}

	/**
	 * 功能:删除站点模板映射信息
	 * 输入:模板ID和站点ID
	 * 输出:true:成功;false:失败    
	 * @param 
	 * @roseuid 45864967030D
	 */
	public boolean deleteSiteTempateMap(int templateid, int siteid)
			throws TemplateManagerException {
		boolean ret = false;
		String sql = "";
		DBUtil conn = new DBUtil();
		try {
			//清除站点模板映射信息
			sql = "delete from td_cms_site_tpl where site_id=" + siteid
					+ " and template_id=" + templateid + " ";
			conn.executeDelete(sql);
			ret = true;
		} catch (Exception e) {
			ret = false;
			System.out.print("删除站点模板映射信息出错!" + e);
			throw new TemplateManagerException(e.getMessage());
		}

		return ret;
	}

}
