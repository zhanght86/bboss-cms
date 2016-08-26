package com.frameworkset.platform.cms.docCommentManager.docCommentDictManager;

	import com.frameworkset.platform.cms.docCommentManager.DocCommentManager;
import com.frameworkset.platform.cms.docCommentManager.DocCommentManagerException;
import com.frameworkset.platform.cms.docCommentManager.DocCommentManagerImpl;
import com.frameworkset.util.ListInfo;

	public class DocCommentDictList extends
			com.frameworkset.common.tag.pager.DataInfoImpl implements java.io.Serializable {

		protected ListInfo getDataList(String sortKey, boolean desc, long offset,
				int maxPagesize) {
			String sql = "select a.* from td_cms_doccomment_dict a " +
					" order by id desc";
			DocCommentManager dcm = new DocCommentManagerImpl();
			ListInfo listInfo = null;
			try{
				listInfo = dcm.getCommentDictList(sql,(int)offset,maxPagesize);
			}catch(DocCommentManagerException de){
				de.printStackTrace();
			}
			return listInfo;
		}

		protected ListInfo getDataList(String arg0, boolean arg1) {
			// TODO Auto-generated method stub
			return null;
		}

	}
