	package com.frameworkset.platform.cms.documentmanager.tag;

	import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

	public class DocTemplateList extends DataInfoImpl implements java.io.Serializable {

		protected ListInfo getDataList(String sortKey, boolean desc, long offset,
	            int maxPagesize) {				
			//String flag = request.getParameter("flag");			
			ListInfo listInfo=null;
			DocumentManagerImpl docManager = new DocumentManagerImpl();
			String sql="select * from td_cms_doc_template order by creattime desc,id desc";
			try{
				listInfo = docManager.getDocTPLList(sql,offset,maxPagesize);
			}catch(Exception ex){
				ex.printStackTrace();
				}
			return listInfo;
		}

		protected ListInfo getDataList(String arg0, boolean arg1) {
			// TODO Auto-generated method stub
			return null;
		}

	}
