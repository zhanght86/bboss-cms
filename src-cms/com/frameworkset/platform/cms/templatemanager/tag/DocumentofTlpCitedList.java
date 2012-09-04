	package com.frameworkset.platform.cms.templatemanager.tag;

	import com.frameworkset.platform.cms.templatemanager.TemplateManager;
import com.frameworkset.platform.cms.templatemanager.TemplateManagerException;
import com.frameworkset.platform.cms.templatemanager.TemplateManagerImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

	public class DocumentofTlpCitedList extends DataInfoImpl implements java.io.Serializable{

		protected ListInfo getDataList(String sortKey, boolean desc, long offset,
				int maxPagesize) {
			ListInfo listInfo = null;	
			//String templateType=request.getParameter("templateType");
			String templateId=request.getParameter("templateId");
			TemplateManager tm = new TemplateManagerImpl();
			try {
				listInfo = tm.getDocumentListofTlpCited(templateId,(int)offset,maxPagesize);
			} catch (TemplateManagerException e) {
				e.printStackTrace();
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