package com.frameworkset.platform.cms.templatemanager.tag;


import com.frameworkset.platform.cms.templatemanager.TemplateStyleManager;
import com.frameworkset.platform.cms.templatemanager.TemplateStyleManagerImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * @author peng.yang
 *
 */
public class TemplateStyleList extends DataInfoImpl implements java.io.Serializable{
	

	
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		String sql = "select t.* from td_cms_template_style t " +
				" order by style_order ";
		TemplateStyleManager tsm = new TemplateStyleManagerImpl();
		ListInfo listInfo = null;
		try{
			listInfo = tsm.getTemplateStyleList(sql,(int)offset,maxPagesize);
		}catch(Exception e){
			e.printStackTrace();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}
}
