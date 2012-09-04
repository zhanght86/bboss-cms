package com.frameworkset.platform.cms.imgmanager;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.cms.CMSManager;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.templatemanager.FileManagerImpl;
import com.frameworkset.platform.cms.templatemanager.FileResource;
import com.frameworkset.platform.cms.templatemanager.TemplateManagerException;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.FileUtil;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
public class ImageFolderTree extends COMTree implements java.io.Serializable{
	private String rootPath;
	private String siteId;
	private static final String IMG_FOLDER="/upimg/";/*默认图片上传路径*/
    //private static final String MEDIO_FILE="/medio/";/*默认多媒体上传路径*/
	
	private void setRootPath()throws TemplateManagerException{
		if(rootPath == null || rootPath.trim().length()==0||siteId==null||siteId.trim().length()==0){
			CMSManager cmsmanager = new CMSManager();
			cmsmanager.init(request,session,response,super.accessControl);
			Site site = cmsmanager.getCurrentSite();
			siteId = ""+site.getSiteId();
			String sitedir = site.getSiteDir();
			//rootPath = CMSUtil.getTemplateRootPath(sitedir)+IMG_FOLDER;
			rootPath =CMSUtil.getPath(CMSUtil.getSiteRootPath(sitedir), "_template"+getUploadImgFilePath());
			System.out.println(rootPath);
		}
	}
    
	public boolean hasSon(ITreeNode father){
		try {
			this.setRootPath();			
			if(father.isRoot()){
				return FileUtil.hasSubDirectory(this.rootPath);
			}else{
				return FileUtil.hasSubDirectory(this.rootPath,father.getId());
			}
			
		} catch (TemplateManagerException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 资源管理树id：ResourceManager
	 * 站点id=site:siteid
	 * 频道id=channelid:site:siteid
	 * 频道根id=channel:site:siteid
	 */
	public boolean setSon(ITreeNode father, int curLevel){
		//String fileFlag = (String)session.getAttribute("fileFlag");
		//图片浏览视图：list，列表；ppt，幻灯片；thumbnail，缩略图
		//String viewertype = request.getParameter("viewertype");
		List fileresources = null;
		if(father.isRoot())
			father.setNodeLink("chooseImageList.jsp?fileFlag=" + request.getParameter("fileFlag"));   
		try {
			this.setRootPath();
			fileresources = (new FileManagerImpl()).getDirectoryResource3(siteId,father.getId(),getUploadImgFilePath());
		} catch (TemplateManagerException e) {
			e.printStackTrace();
		}
		
		for(int i=0;fileresources!=null&&i<fileresources.size();i++){
			FileResource fr = (FileResource)fileresources.get(i);
			if(fr.isDirectory()){
				Map params = new HashMap();
				String uri = fr.getUri();
				if(!uri.endsWith("/")&&!uri.endsWith("\\")){
					uri= getUploadImgFilePath()+uri+"/";
				}
				params.put("uri",uri);
				addNode(father,uri,fr.getName(),"folder",
						true, curLevel, (String)null,(String)null,(String)null,params);
				
			}
		}
		return true;
	}
	public static String getUploadImgFilePath()
	{
	   return ImageFolderTree.IMG_FOLDER;
	}
	/*
	 * 初始化上传目录
	 * 
	 */
	public void initLoadPath()
	{
		CMSManager cmsmanager = new CMSManager();
		cmsmanager.init(request,session,response,super.accessControl);
		Site site = cmsmanager.getCurrentSite();
		siteId = ""+site.getSiteId();
		String sitedir = site.getSiteDir();
		rootPath =CMSUtil.getPath(CMSUtil.getSiteRootPath(sitedir), "_template"+getUploadImgFilePath());
		File file1 = new File(rootPath);
		if(!file1.exists())
		{
			file1.mkdir();
		}
		
	}
}