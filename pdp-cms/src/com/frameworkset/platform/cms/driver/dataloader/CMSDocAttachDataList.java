package com.frameworkset.platform.cms.driver.dataloader;

import java.util.List;

import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.ListInfo;
/**
 * 文档附件数据加载器
 * <p>Title: CMSDocAttachDataList.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-5-31 18:19:48
 * @author kai.hu
 * @version 1.0
 */
public class CMSDocAttachDataList extends CMSBaseListData implements java.io.Serializable {
    
	protected ListInfo getDataList(String arg0, boolean arg1, long arg2,
			int arg3) {
		try {
			if(context != null)
			{
				Document doc = ((ContentContext)super.context).getDocument();
				ListInfo lstifo = (CMSUtil.getCMSDriverConfiguration()
							  .getCMSService()
							  .getDocumentManager()
							  .getRelationAttachmentsOfDocument(doc,arg2,arg3));
				return lstifo;
			}
			else{
				Document doc = CMSUtil.getCMSDriverConfiguration()
						  .getCMSService()
						  .getDocumentManager().getDoc(documentid);
				ListInfo lstifo = (CMSUtil.getCMSDriverConfiguration()
							  .getCMSService()
							  .getDocumentManager()
							  .getRelationAttachmentsOfDocument(doc,arg2,arg3));
				return lstifo;
			}
		} catch (DocumentManagerException e) {
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			e.printStackTrace();
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return new ListInfo();
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		try {
			if(context != null)
			{
				Document doc = ((ContentContext)super.context).getDocument();
				ListInfo lstifo = new ListInfo();
				List datas = CMSUtil.getCMSDriverConfiguration()
						  .getCMSService()
						  .getDocumentManager()
						  .getRelationAttachmentsOfDocument(doc,count);
				lstifo.setDatas(datas);
				return lstifo;
			}
			else{
				Document doc = CMSUtil.getCMSDriverConfiguration()
						  .getCMSService()
						  .getDocumentManager().getDoc(documentid);
				ListInfo lstifo = new ListInfo();
				lstifo.setDatas(CMSUtil.getCMSDriverConfiguration()
							  .getCMSService()
							  .getDocumentManager()
							  .getRelationAttachmentsOfDocument(doc,count));
				return lstifo;
			}
		} catch (DocumentManagerException e) {
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			e.printStackTrace();
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return new ListInfo();
	}

}
