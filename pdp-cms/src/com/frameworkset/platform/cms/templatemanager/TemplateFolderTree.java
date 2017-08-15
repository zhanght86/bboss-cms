package com.frameworkset.platform.cms.templatemanager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.cms.CMSManager;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.FileUtil;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
/**
 * 
 * <p>Title: TemplateFolderTree.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-10-10 17:41:00
 * @author ge.tao
 * @version 1.0
 */
public class TemplateFolderTree extends COMTree implements java.io.Serializable{
	private String rootPath;
	private String siteId;

	
	private void setRootPath()throws TemplateManagerException{
		if(rootPath == null || rootPath.trim().length()==0||siteId==null || siteId.trim().length()==0){
			CMSManager cmsmanager = new CMSManager();
			cmsmanager.init(request,session,response,super.accessControl);
			Site site = cmsmanager.getCurrentSite();
			siteId  = ""+site.getSiteId();
			String sitedir = site.getSiteDir();
			rootPath = CMSUtil.getTemplateRootPath(sitedir);
		}
	}
    
	public boolean hasSon(ITreeNode father){
		try {
			this.setRootPath();
			//是跟节点的话,包含文件视图和模板视图两个子节点
			if(father.isRoot()){
				return true;
			}
			
			//是模板视图的话,没有子节点
			if(father.getId().startsWith("////")){
				return false;
			}
			
			//如果只有一个/,表明是根目录;否则就是子目录
			if(father.getId().equals("file:view")){
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

		List fileresources = null;
		String parentPath = "";
		try {
			this.setRootPath();
			//添加顶级站点
			if(father.isRoot()){
				Map params = new HashMap();
				params.put("siteId",siteId);
				
				if (super.accessControl.checkPermission(
						siteId,
						AccessControl.SITE_TEMPLATEVIEW_PERMISSION,
						AccessControl.SITETPL_RESOURCE)) {
						addNode(father,"////"+siteId,"模板视图","root",
						true, curLevel, (String)null,(String)null,(String)null,params);
				}
			
				
				params = new HashMap();
				params.put("uri","");
				if (super.accessControl.checkPermission(
						siteId,
						AccessControl.SITE_FILEVIEW_PERMISSION,
						AccessControl.SITEFILE_RESOURCE)) {
				addNode(father,"file:view","文件视图","root",
						true, curLevel, (String)null,(String)null,(String)null,params);
				}
				
				//新增一个节点,模板风格管理
				Map tmplStyle = new HashMap();
				String styleUrl = "templatestyle_list.jsp";
				tmplStyle.put("uri",styleUrl);
				addNode(father,styleUrl,"模板风格管理","root",
						true, curLevel, (String)null,(String)null,(String)null,tmplStyle);
				
				//新增一个节点,公共模板包管理
				Map publicTmpl = new HashMap();
				String publicUri = "publicTmplZipManager.jsp";
				publicTmpl.put("uri",publicUri);
				addNode(father,publicUri,"公共模板包管理","root",
						true, curLevel, (String)null,(String)null,(String)null,publicTmpl);
				//新增一个节点,私有模板包管理
				Map privateTmpl = new HashMap();
				String privateUri = "privateTmplZipManager.jsp";
				privateTmpl.put("uri",privateUri);
				addNode(father,privateUri,"私有模板包管理","root",
						true, curLevel, (String)null,(String)null,(String)null,privateTmpl);				
				
				return true;
				
			}
			else
			{
				if(!father.getId().equals("file:view"))
					parentPath = father.getId();
				else
					parentPath = "";
			}
			
			
			
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
}
