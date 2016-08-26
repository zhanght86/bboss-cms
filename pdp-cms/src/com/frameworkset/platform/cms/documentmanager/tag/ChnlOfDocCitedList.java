	package com.frameworkset.platform.cms.documentmanager.tag;

	import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

	/**
	 * 模板的频道引用列表，用于引用查看
	 * @author Administrator
	 *
	 */
	public class ChnlOfDocCitedList extends DataInfoImpl implements java.io.Serializable{

		protected ListInfo getDataList(String sortKey, boolean desc, long offset,
				int maxPagesize) {
			ListInfo listInfo = null;	
			String docId=request.getParameter("docId");
			DocumentManager dm = new DocumentManagerImpl();
			try { 
				listInfo = dm.getChnlListOfDocCited(docId,(int)offset,maxPagesize);			
			} catch (DocumentManagerException e) {
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
