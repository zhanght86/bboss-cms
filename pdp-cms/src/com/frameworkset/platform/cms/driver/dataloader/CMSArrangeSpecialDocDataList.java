package com.frameworkset.platform.cms.driver.dataloader;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.ListInfo;
/**
 * 专题和文档的混合概览数据接口 获取 频道下的 文档 引用子频道 引用文档 (直接的另外实现)
 * <p>Title: CMSArrangeSpecialDocDataList.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date Jun 6, 2005 11:18:58 AM
 * @author yinbp
 * @version 1.0
 */
public class CMSArrangeSpecialDocDataList extends CMSBaseListData implements java.io.Serializable {
	
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
		ListInfo listInfo = new ListInfo();
		try {
			listInfo =(CMSUtil.getCMSDriverConfiguration()
					.getCMSService().getDocumentManager()
					.getArrangeSpecialDocList(channel,offSet,pageItemsize,this.params));
		} catch (DocumentManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listInfo;
	}

	/**
	 * 如果分页tag只是作为列表来实现时，调用该方法
	 * sortKey:排序字段
	 * desc:排序的秩序，true为降序，false为升序
	 * @return java.util.List
	 */
	protected ListInfo getDataList(String sortKey,
									 boolean desc){
		
		ListInfo listInfo = new ListInfo();
		List list = new ArrayList();
		try {
			list =(CMSUtil.getCMSDriverConfiguration()
					.getCMSService().getDocumentManager()
					.getArrangeSpecialDocList(channel,count,this.params)); 
			listInfo.setDatas(list);
		} catch (DocumentManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listInfo;
	}
	
	

}
