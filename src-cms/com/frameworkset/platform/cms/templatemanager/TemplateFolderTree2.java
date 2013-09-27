package com.frameworkset.platform.cms.templatemanager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import com.frameworkset.platform.cms.CMSManager;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.FileUtil;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class TemplateFolderTree2 extends COMTree implements java.io.Serializable{
	private String rootPath;
	private String siteId;
	private String rooturi = "/uploadfiles";
	
	private void setRootPath()throws TemplateManagerException{
		if(rootPath == null || rootPath.trim().length()==0||siteId==null||siteId.trim().length()==0){
			CMSManager cmsmanager = new CMSManager();
			cmsmanager.init(request,session,response,super.accessControl);
			Site site = cmsmanager.getCurrentSite();
			siteId = ""+site.getSiteId();
			String sitedir = site.getSiteDir();
			rootPath = CMSUtil.getTemplateRootPath(sitedir) ;
			
//			System.out.println(rootPath);
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
		String parentPath = "";
		try {
			this.setRootPath();
			//添加顶级站点
			
				
			parentPath = father.getId();
			
			if(father.isRoot())
				father.setNodeLink("chooseImageList.jsp?fileFlag=" + request.getParameter("fileFlag") + "&uri="+ rooturi);
			
			
			fileresources = (new FileManagerImpl()).getDirectoryResource(siteId,parentPath);
		} catch (TemplateManagerException e) {
			e.printStackTrace();
		}
		
		for(int i=0;fileresources!=null&&i<fileresources.size();i++){
			FileResource fr = (FileResource)fileresources.get(i);
			if(fr.isDirectory()){
				Map params = new HashMap();
				String uri = fr.getUri();
			
				params.put("uri",uri);
				addNode(father,uri,fr.getName(),"folder",
						true, curLevel, (String)null,(String)null,(String)null,params);
				
			}
		}
		
		return true;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		// TODO Auto-generated method stub
		super.setPageContext(pageContext);
		String fileFlag = request.getParameter("fileFlag");
		if(fileFlag!=null && fileFlag.equals("1"))
		{
			
			rooturi = "";
		}
		
	}
}
