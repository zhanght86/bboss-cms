package com.frameworkset.platform.cms.templatemanager.tag;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.orm.adapter.DB;
import com.frameworkset.platform.cms.templatemanager.TemplateManager;
import com.frameworkset.platform.cms.templatemanager.TemplateManagerImpl;
import com.frameworkset.util.ListInfo;

public class TemplateofSiteList extends DataInfoImpl {

	
			
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
//		ListInfo listInfo = new ListInfo();	
		String siteId=request.getParameter("siteId");
//		String action = request.getParameter("action");
//		String name = request.getParameter("name");
//		if(null==name) name = "";
//		String creatorUser = request.getParameter("creatorUser");
//		if(null==creatorUser) creatorUser = "";
//		String type = request.getParameter("type");
//		String templateStyle = request.getParameter("templateStyle");
//		DB db = DBUtil.getDBAdapter();
//		
//		String TimeBgin=request.getParameter("TimeBgin");
//		String TimeEnd=request.getParameter("TimeEnd");
//		//String channelIds=request.getParameter("channelIds");
//		
//		StringBuffer sqlBuffer = new StringBuffer();
//		sqlBuffer.append(" select t1.*,t3.user_name,nvl(t4.style_name,'未知风格(' || t1.TEMPLATE_STYLE || ')') as style_name from td_cms_template t1 inner join td_cms_site_tpl t2 on ");
//		sqlBuffer.append(" t1.template_id = t2.template_id ");
//		sqlBuffer.append(" and t2.site_id = ");
//		sqlBuffer.append(siteId);
//		if(action!=null && action.equals("search"))
//		{
//			sqlBuffer.append(" and t1.name like '%");
//			sqlBuffer.append(name);
//			sqlBuffer.append("%' ");
//			if(null!=type && !type.equals(""))
//			{
//				sqlBuffer.append(" and t1.type = ");
//				sqlBuffer.append(type);
//			}
//			if(null!=TimeBgin && !TimeBgin.equals(""))
//			{
//				sqlBuffer.append(" and t1.createtime>=to_date('"+TimeBgin+"','yyyy-mm-dd')");
//			}
//			if(null!=TimeEnd && !TimeEnd.equals(""))
//			{
//				sqlBuffer.append(" and t1.createtime<to_date('"+TimeEnd+"','yyyy-mm-dd')+1");
//			}
//
//		}
//		sqlBuffer.append(" inner join td_sm_user t3 ");
//		sqlBuffer.append("on t1.createuser = t3.user_id");
//		if(action!=null && action.equals("search"))
//		{
//			sqlBuffer.append(" and t3.user_name like '%");
//			sqlBuffer.append(creatorUser);
//			sqlBuffer.append("%'");
//		}
//		sqlBuffer.append(" left join td_cms_template_style t4 on t1.template_style = t4.style_id ");
//		if(null!=templateStyle && !templateStyle.equals("-2")){
//			int styleId = Integer.parseInt(templateStyle);
//			sqlBuffer.append(" and t4.style_id = "+styleId);
//		}
//		sqlBuffer.append(" order by t1.type,t1.template_style,t1.PERSISTTYPE,t1.createtime desc");
		ListInfo listInfo  = null;
		if (siteId != null && siteId.length() > 0) { 
			try {
				TemplateManager templatemanagerM=new TemplateManagerImpl();
				  listInfo = templatemanagerM.getTemplateInfoListofSite(request, (int)offset, maxPagesize);

			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
		return listInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.common.tag.pager.DataInfoImpl#getDataList(java.lang.String,
	 *      boolean)
	 */
	protected ListInfo getDataList(String arg0, boolean arg1) {
		
		return null;
	}
}