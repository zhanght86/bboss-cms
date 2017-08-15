package com.frameworkset.platform.cms.driver.dataloader;

import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.ListInfo;
/**
 * 增加根据文档关键字获取文档所属的站点中所有已发文档中标题
 * 或内容包含本关键字的文档列表的数据接口
 * <p>Title: CMSKeyWordRelatedListData.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-9-20 8:20:40
 * @author ge.tao
 * @version 1.0
 */

public class CMSKeyWordRelated3DocListData extends CMSBaseListData implements java.io.Serializable {
	/**
	* 分页显示时获取每页的数据项
	* sortKey:排序字段
	* desc:排序的秩序，true为降序，false为升序
	* @param offSet - 从数据源获取数据的游标位置
	*
	* @param pageItemsize - 每页显示的数据条数
	* @return java.util.List
	 * @throws DriverConfigurationException 
	 * @throws DocumentManagerException 
	*/
	protected ListInfo getDataList(String sortKey,
										 boolean desc,
										 long offSet,
										 int pageItemsize) {
		try {
			if(context != null){
				
				ContentContext contentctx = (ContentContext) context;
				ListInfo lstifo = (CMSUtil.getCMSDriverConfiguration()
							  .getCMSService()
							  .getDocumentManager()
							  .getKeyWord3RelationOfDocument(contentctx.getDocument().getSubtitle(),
															Integer.parseInt(contentctx.getContentid()),
															channel,
															contentctx.getSiteID(),
															offSet,pageItemsize));
				return lstifo;
			}else{
				Site siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
				ListInfo lstifo = (CMSUtil.getCMSDriverConfiguration()
							  .getCMSService()
							  .getDocumentManager()
							  .getKeyWord3RelationOfDocument(null,
									  						Integer.parseInt(documentid),
									  						channel,
									  						siteinfo.getSiteId() + "",
									  						offSet,
									  						pageItemsize));
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

	/**
	 * 如果分页tag只是作为列表来实现时，调用该方法
	 * sortKey:排序字段
	 * desc:排序的秩序，true为降序，false为升序
	 * @return java.util.List
	 */
	protected ListInfo getDataList(String sortKey,
									 boolean desc){
		
		try {
			if(context != null){
				ContentContext contentctx = (ContentContext) context;
				System.out.println("----------------------------------------"+contentctx.getContentid());
				ListInfo lstifo = new ListInfo();
				lstifo.setDatas(CMSUtil.getCMSDriverConfiguration()
							  .getCMSService()
							  .getDocumentManager()
							  .getKeyWord3RelationOfDocument(contentctx.getDocument().getSubtitle(),
									  						Integer.parseInt(((ContentContext)super.context).getContentid()),
									  						channel,
									  						contentctx.getSiteID(),
									  						count));
				return lstifo;
			}else{
				ListInfo lstifo = new ListInfo();
				Site siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
				lstifo.setDatas(CMSUtil.getCMSDriverConfiguration()
							  .getCMSService()
							  .getDocumentManager()
							  .getKeyWord3RelationOfDocument(null,Integer.parseInt(documentid),channel,siteinfo.getSiteId() + "",count));
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
