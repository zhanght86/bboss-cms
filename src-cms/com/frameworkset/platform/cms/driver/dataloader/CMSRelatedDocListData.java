package com.frameworkset.platform.cms.driver.dataloader;

import java.util.List;

import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.ListInfo;

public class CMSRelatedDocListData extends CMSBaseListData implements java.io.Serializable {

	protected ListInfo getDataList(String arg0, boolean arg1, long arg2,
			int arg3) {
		//		 TODO Auto-generated method stub
		//		 TODO Auto-generated method stub
		try {
			ContentContext contentctx = (ContentContext) context;

			/**
			 * 如果存在发布上下文，则表示为发布过程中调用本方法
			 */
			if (this.documentid!=null && !"".equals(this.documentid)){
				ListInfo lstifo = (CMSUtil.getCMSDriverConfiguration()
						.getCMSService().getDocumentManager()
						.getRelatedDocList(documentid,arg2,arg3));
				return lstifo;
				
			}else if (context != null) {
				if (this.site == null) {
					ListInfo lstifo = (CMSUtil.getCMSDriverConfiguration()
							.getCMSService().getDocumentManager()
							.getRelatedDocList(contentctx.getContentid() + "",arg2,arg3));
					return lstifo;
				} else {
					Site siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
					ListInfo lstifo = (CMSUtil.getCMSDriverConfiguration()
							.getCMSService().getDocumentManager()
							.getRelatedDocList(contentctx.getContentid() + "",arg2,arg3));
					return lstifo;
				}
			} else {
				if (this.channel == null || this.channel.equals("")
						|| this.site == null || this.site.equals(""))
					throw new IllegalArgumentException("没有指定站点的名称和频道名称");

				Site siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
				ListInfo lstifo = (CMSUtil.getCMSDriverConfiguration()
						.getCMSService().getDocumentManager()
						.getRelatedDocList(contentctx.getContentid() + "",arg2,arg3));
				return lstifo;

			}
		} catch (DocumentManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SiteManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ListInfo();
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {

		ContentContext contentctx = (ContentContext) context;
		try {

			/**
			 * 如果存在发布上下文，则表示为发布过程中调用本方法
			 * 
			 */
			if (this.documentid!=null && !"".equals(this.documentid)){
				ListInfo lstifo = new ListInfo();
				lstifo.setDatas(CMSUtil.getCMSDriverConfiguration()
						.getCMSService().getDocumentManager()
						.getRelatedDocList(documentid,count));
				return lstifo;
				
			}else if (context != null) {
				if (this.site == null) {
					List published = CMSUtil.getCMSDriverConfiguration()
							.getCMSService().getDocumentManager()
							.getRelatedDocList(contentctx.getContentid() + "",
									count);
					ListInfo listInfo = new ListInfo();
					listInfo.setDatas(published);
					return listInfo;
				} else {

					List published = CMSUtil.getCMSDriverConfiguration()
							.getCMSService().getDocumentManager()
							.getRelatedDocList(contentctx.getContentid() + "",
									count);
					ListInfo listInfo = new ListInfo();
					listInfo.setDatas(published);
					return listInfo;
				}
			} else {

				List published = CMSUtil.getCMSDriverConfiguration()
						.getCMSService().getDocumentManager()
						.getRelatedDocList(contentctx.getContentid() + "",
								count);
				ListInfo listInfo = new ListInfo();
				listInfo.setDatas(published);
				return listInfo;
			}

		} catch (DocumentManagerException e) {

			e.printStackTrace();
		} catch (DriverConfigurationException e) {

			e.printStackTrace();
		}
		return new ListInfo();
	}

}
