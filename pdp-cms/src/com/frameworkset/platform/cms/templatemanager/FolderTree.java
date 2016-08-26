package com.frameworkset.platform.cms.templatemanager;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class FolderTree extends COMTree implements java.io.Serializable{
	private String rootPath = CMSUtil.getAppRootPath()+CMSUtil.getCMSContextPath()+CMSUtil.getWebSiteRootPath();

    
	public boolean hasSon(ITreeNode father){
		return false;
	}

	/**
	 * 资源管理树id：ResourceManager
	 * 站点id=site:siteid
	 * 频道id=channelid:site:siteid
	 * 频道根id=channel:site:siteid
	 */
	public boolean setSon(ITreeNode father, int curLevel){
		return false;
	}
}
