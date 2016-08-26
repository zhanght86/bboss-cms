package com.frameworkset.platform.cms.searchmanager.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteCacheManager;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/**
 * 
 * 
 * <p>
 * Title: MutisiteSelect
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company: iSany
 * </p>
 * 
 * @Date Aug 17, 2007
 * @author da.wei
 * @version 1.0
 */
public class MutisiteSelect extends COMTree implements java.io.Serializable {

	public boolean hasSon(ITreeNode father) {
		List sitelist = null;
		// 加载站点
		if (father.isRoot()) {
			// SiteManager smi = new SiteManagerImpl();

			try {
				return SiteCacheManager.getInstance().hasSubSite("0");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// sitelist = smi.getTopSubSiteList();

		} else {

			String parentSiteid = father.getId();
			try {
				return SiteCacheManager.getInstance().hasSubSite(parentSiteid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return false;

	}

	public boolean setSon(ITreeNode father, int curLevel) {
		List sitelist = null;
		// 加载站点
		if (father.isRoot()) {
			// SiteManager smi = new SiteManagerImpl();

			// sitelist = smi.getTopSubSiteList();
			try {
				sitelist = SiteCacheManager.getInstance().getSubSites("0");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			String parentSiteid = father.getId();
			// sitelist = smi.getSubSiteList(parentSiteid);
			try {
				sitelist = SiteCacheManager.getInstance().getSubSites(
						parentSiteid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (sitelist != null && sitelist.size() > 0) {
			Iterator iterator = sitelist.iterator();

			while (iterator.hasNext()) {
				Site site = (Site) iterator.next();
				String siteName = site.getName();
				String siteId = "" + site.getSiteId();
				Map map = new HashMap();
				//map.put("siteId", siteId);
				map.put("siteId", siteId);
				map.put("siteName", site.getName());
				map.put("resName", siteName);
//				/map.put("isShowModeWindow", "1");

//				if (accessControl.checkPermission("" + site.getSiteId(),
//						AccessControl.WRITE_PERMISSION,
//						AccessControl.SITE_RESOURCE)) {
					ITreeNode tt = addNode(father, site.getSiteId() + "",
							site.getName() + "_" + site.getSecondName(), "site", false, curLevel, (String) null,
							site.getSecondName(), site.getSecondName(), map);					

//				} else {
//
//					if (accessControl.checkPermission("" + site.getSiteId(),
//							AccessControl.READ_PERMISSION,
//							AccessControl.SITE_RESOURCE)) {
//
//						addNode(father, site.getSiteId() + "", siteName,
//								"site", true, curLevel, (String) null,
//								(String) null, (String) null, map);
//
//					}
//
//				}
			}
		}

		return true;
	}

}
